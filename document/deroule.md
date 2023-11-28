# 0 - Intro
Présentation de l'application Rest. Le but est de lancr et présneter briévement les différents services Rest.

Lancer l'application (conteneur + SB) puis aller dans Postman , Onglet Application.
Présenter les 4 onglets et lancer quelques exemples (Garder les "mauvais exemples, comme les services qui récupére plusieurs Mo d'un coup (cf. get PLATS), pour plus tard)" )

# 1 - Base de donnée
TODO expliquer but de cette partie

Présentation de la table Restaurant sur l'analyseur de requête : présentation des données, du modéle de la place prise
Environ 30 000 restaurants, 100 000 avis et plats. Taille totale base 20 Mo

La table Restaurant a une taille de 3,5M
Demander à l'auditoire ou est ce que l'on peut gagner de la place

2 données peuvent être optimisés sur cette table
- La date de création qui n'a pas besoin d'heure
- La donnée "vegetarien" peut être représenté par un type booléen plutôt que par un varchar contenant les chaines "OUI" ou "NON"

## Démo

Migrer la donnée de date => datetime
```
ALTER TABLE restaurant ALTER COLUMN date_creation TYPE date
```


Migrer le booléen végétarien
```
ALTER TABLE restaurant ADD vegetarien_migration bool NULL;
update restaurant set vegetarien_migration=true where vegetarien ='OUI';
update restaurant set vegetarien_migration=false where vegetarien ='NON';
ALTER TABLE restaurant drop column vegetarien;
ALTER TABLE restaurant RENAME COLUMN vegetarien_migration TO vegetarien;
```

Faire un vaccum pour voir l'espace libéré
```
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.restaurant;
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.plat;
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.avis;
```

On est passé de 3,8Mo à 3,5M
TODO vérifier


## Démo partie table Avis

Avant modif 8.9 Mo

Passer le type de donnée de la colonne "note" de int8 à smallint

```
ALTER TABLE avis ALTER COLUMN note TYPE smallint
```

Supprimer la colonne "ville"

```
ALTER TABLE avis DROP COLUMN lieu;
```

Faire Vaccum
```
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.avis;
```
On est passé à 8,4 Mo


## Démo supprimer vieille tables inutiles
drop table tmp_restaurant_2015


## Ce qui n'est pas vu dans la démo
Table avis : supprimer les commentaires obsoléte (par exemple de + de 5 ans)
Autre idée : supprimer les doublons , par exemple les restaurants déterminer les doublons à partir adresse
faire une table "note" à part et mettre en évidence la place que l'on peut gagner avec un "bon" design
Ne pas utiliser des clob à la place varchar

## Faire le propre avant de passer à la suite
Exécutuer "dropDatabase.sql"
Redémarrer application
Lancer " commande init"


# 2 - Orm et requête SQL
Pagination, cache,requête

## Pagination
Lancer la recherche restaurant "/restaurants/_search" avec la valeur "assembleur"
=> 8 résultats temps recherche pas top mais raisonnable

Lancer la recherche restaurant "/restaurants/_search" avec la valeur "Abordable"
=> Cela retourne beaucoup des résultats, c'est lent et volumineux

Généralement seul les premiers résultats sont pertinents.
- Il faut rajouter les parametres numPage et taillePage dans la methode recherche du controler RestaurantControler
  => ",@RequestParam int numPage,@RequestParam int taillePage"
- Il faut passer ces paramétres au service restaurantService.rechercherRestaurant
- Au niveau de restaurantService.rechercherRestaurant il faut créer un objet pageable
  => "Pageable pageable= PageRequest.of(numPage,taillePage);"
- Prendre en compte cet objet au niveau du dao
- Trier par id (aller au niveau du dao et rajouter orderbyId
- Modifier le nombre de résultat au RestaurantControler.rechercherRestaurant
- Rajouter les paramétres de pagination dans l'appel Rest : &numPage=1&taillePage=20

A CREUSER (la requête count):
@Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
nativeQuery = true)
Page<User> findByLastname(String lastname, Pageable pageable);


## Afficher les requêtes
Le service de recherche prend toujours trop de temps (500ms sur mon poste)
=> Il faut comprendre ce qui se passe donc afficher les requêtes
Pour afficher les requêtes : logging.level.org.hibernate.SQL=DEBUG dans application.properties
=> On voit plein de requêtes

## Lazy vs Eager
On voit plein de requêtes, les premiéres sont plein de requêtes pour récupére les avis. Hors on ne récupére pas les avis dans notre résultat !
Pourquoi ? Il faut aller dans l'entity Restaurant et regarder le mapping avec avis. La relation restaurant/entity est en eager !
=> PAsser la relation restaurant/entity en Lazy
=> relancer le test, c'estm ieux

FIXME au niveau de la recherche , transformer le service pour qu'il n'affiche que les restuarant(pas de jointure avec les plats)

## Eviter les N+1
Suite à l'appel au service qui récupére les restaurants(GET localhost:8080/restaurants) on voit une requête N+1
Il faut modifier la requête du repository RestaurantCustomRepository.getDetailsById pour charger les plats
oldRequete => from Restaurant r join fetch r.plats where r.id=:idRestaurant
=> C'est meiux mais on charge toujours CatagoriePlat

## Le cache
Solution au pb précédent le cache

Le cache est déja activé dans le projet (cf. pom.xml et application.properties)
Il suffit de rajouter l'annotation sur CategoriePlat:

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

## Utiliser les opérations bulk à la place d'opération unitaire
On supprimer tous les commentaires obsolétes (de + de 5 ans)
On lance la suppression ("avis/_obsoletes")
En regardant les requêtes, on s'aperçoit qu'il y a un "select" qui retourne l'ensemble des résultats et autant de delete que le nombre de résultat retourné par le sélect:
select a1_0.id,a1_0.auteur,a1_0.commentaire,a1_0.date_creation,a1_0.lieu,a1_0.note,a1_0.restaurant_id from avis a1_0 where a1_0.date_creation<?
delete from avis where id=?
delete from avis where id=?

=> L'opération supprime environ 40 000 éléments et dure 50 minutes sur mon PC. On arrête l'appli

Pour optimiser on peut faire une seule requête pour supprimer l'ensemble des résultats
=> On remplace le code dans la méthode supprimeAvisObsoletes par "avisRepository.deleteBulkByDateCreationLessThan(datePurge);"

A CREUSER:
Le code "avisRepository.deleteByDateCreationLessThan(datePurge);" fait la même chose que "avisRepository.findByDateCreationLessThan(datePurge).iterator().forEachRemaining(avis -> avisRepository.delete(avis))"
=> Lequel est le mieux , pour le moment j'ai gardé le 2éme car il illustre mieux la bad practice (par contre le 1er illustre le fait qu'il faut toujours faie attention aux requêtes génèrées)


# 3 - Clean code
Aller sur la méthode RestaurantService.get
=> A quoi sert cette méthode ? Montrer un peu le code . Il faut rendre ce code plus lisible. On peut faire un call hierarchy, ou regarder le code pour savoir à quoi sert le code

On peut renommer le nom de la méthode et du Dto de retour
=> Renommer la méthode en getRestaurantDetail
=> Renommer le dto "Dto" en "RestaurantAvecInfoComplementaireDto"

On a récupéré un appel à la méthode, on l'éxécute dans postman 
=> On ne sait toujours pourquoi il y a autant de code pour retourner aussi peu de chose , ni à quoi correspond cette mystérieuse variable "d"

On retourne sur la méthode, on distingue qu'il y a plusieurs parties :
- Une partie requête
- Une partie remplissage de Dto
- Une partie calcul
=> On doit pouvoir externaliser ces méthodes, mais avant toute chose . Il faut avoir une sécurité pour le refacto, il faut une couverture de test
On a déja crée les test dans RestaurantServiceIT
FIXME retrouver le terme dans la présentation de Thomas Perrain et Y

Refacto:
- 1ére chose à faire : rassembler les vaiables au + prés de leur utilisation
- Remplacer le code qui permet de récupérer l'entity Restaurant par restaurantRepository.findById(id).get()
- Externaliser le code de calcul
- Donner un indice 6371 c'est la longueur du rayon de la terre en Km => Créer constante pour le rayon de la terre
- Créer une constante pour le mile
- On comprend que le booléen "english" veut en fait représenter l'unité de mesure => On renomme la variable
- On comprend que c'est une méthode de calcul de distance => On renomme la méthode
- On revient sur la méthode initiale et on fait la conclusion

# 4 - Partie Asynchrone
Mise en situation : L'application marche de plus. Au début les performances sont trés bonnes, mais trés rapidement les performances se dégradent.
Notamment entre 20 et 22h tous les soirs.

Tous les soirs de nombreux utilisateur se connectent pour regarder dans quel restaurant ils vont aller
=> Lancer VisualVm pour voir ce qui se passe dans la VM.Aller dans PerfTest et lancer testParrallelGetRestaurant pour simuler la consultation du site
=> Montrer qu'il y a de l'activité sur la VM (mais sans plus) et pas d'activité au niveau de la base

Mais les utilisateurs donne aussi leur avis.Lancer la création des avis
=> !!! Gros impact au niveau VisualVm et au niveau de la base

Solution l'asynchrone, on n'a pas forcément besoin d'écrire les avis tout de suite, on peut les écrire quelque minutes  aprés.
=> Présenter le schéma

On va s'interresser en particulier à l'ajout des avis
=> Redémarrer l'appli, lancer Visual Vm sur l'appli. Lancer le test et prendre un screenshot de la mémoire/CPU

Mettre en place l'asynchrone:
- Rajouter le contener kafka (montrer le fichier docker-compose, il a déja été mise en place)
- A place d'écrire l'avis en base on envoi un message. Dans AvisControler.ajouterAvis mettre kafkaAvisService.sendMessage(avisCreationDto);
- Montrer le code de kafkaAvisService.sendMessage, cela publie un message sur le topic "cuillere-avis"
- Faire une démo. Ajouter un avis et voir dans AKHQ le message (http://localhost:19000/ui/docker-kafka-server) => Il faut rajouter un consommateur
- Rajout consommateur : 
```
	@KafkaListener(topics = "cuillere-avis")
	public void listenGroupFoo(AvisCreationDto message) {
		logger.info("Création de l'avis pour restaurant {}",message.getIdRestaurant());
		avisService.creerAvis(message);
	}
```
- Le message est consommé. Montrer l'avis
- Relancer le test de création massif , montrer le résultat dans VisualVm et comparer à la solution synchrone (cf. screenshot)

# 5 - Obsolescence
Il existe des outils pour détecter le code mort :
- Dans eclipse, plugin Ucd detector
- Directement incorporé dans IntelliJ

Dans IntelliJ => Code | Analyze | Run Inspection by Name... | Unused declaration.
Préciser dans IntelliJ que l'on considére que le code appellé uniquement dans les TU est inutile ( Inspection option -> Entry Point -> When entry point are in test sources mark callees as (used/unused))

Aller dans RestaurantService la méthode getRestaurantFeteDesMeres2019 ne sert à rien
Aller dans PlatService une constante ne sert à rien


# 6 - Documentation

FIXME nettoyer readme
FIXME rajouter un schéma dans le readme
FIXME faut rajouter des exemples pour les commentaires dans le code ?





!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!! NON GARDE !!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

# Observabilité et métriques
https://www.baeldung.com/spring-boot-3-observability
On peut accéder aux metric via :
http://localhost:8080/actuator/metrics/avisService
Ne pas oublier d'exposer tous les actuators
management.endpoints.web.exposure.include=*

TODO : exposer les services Avis dans AvisService
Exposer service restaurant dans metrics
PAsser ces metrics à prometheus
Brancher Grafana ?????

FIXME miniser les logs de l'appli(supprimer par exemple les niveau "info" pour le kafka)
