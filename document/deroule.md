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


# Orm et requête SQL
Pagination, cache,requête

## Lazy vs Eager
Je lance RestaurantControler.getPlat => On ne s'apercoit qu'il y a des N+1 car il y a des eager
TODO requête findPlatByIdRestaurant (on fait une jointure donc on une grosse requête au lieu de 2)

## Pagination
Lancer la recherche restaurant "/restaurants/_search" avec la valeur "assembleur"
=> 8 résultats temps recherche pas top mais raisonnable

Lancer la recherche restaurant "/restaurants/_search" avec la valeur "Abordable"
=> Cela retourne beaucoup des résultats, c'est lent et volumineux

Généralement seul les premiers résultats sont pertinents.
Il faut rendre paginable ("/restaurants/_search2") :
- Il faut rajouter les parametres numPage et taillePage dans la methode recherche du controler RestaurantControler
  => ",@RequestParam int numPage,@RequestParam int taillePage"
- Il faut passer ces paramétres au service restaurantService.rechercherRestaurant
- Au niveau de X il faut créerun objet pageable
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

FIXME : supprimer les vieilles méthodes BULK (supprimeAvisObsoletesBulk2 ...)


# Refacto
Une refacto

Faire un refacto sur une méthode qui retourne le détail d'un restaurant:
- Récupére les entity via des requêtes
- Calcule la distance
- La méthode prend en paramétre un booléen "mile" pour indiquer si on doit convertir ou pas en mile
- Retourne les avis les plus pertinents

Constante : double earthRadiusKm = 6371; // Rayon de la Terre en kilomètres

Déroulé refacto:
- Regrouper le code (on distingue un peu plus ce que fait chaque partie)
- A la place du code query + mapping on utilise la méthode X
- Externaliser dans une méthode le code qui calcule la distiance.
- Demander à quoi sert la méthode et la renommer.
- Utiliser une constante pour le rayon (une en km l'autre en mile)

# Partie Asynchrone
Simuler l'heure de pointe (cad les personnes qui donne les avis et qui consulte les restaurant de 20 à 22h)

Reste à faire : rajouter conteneur kafka et consommation asynchrone.

# Obsolescence
Il existe des outils pour détecter le code mort
Pour détecter le code mort dans eclipse (Ucd detector)
Directement incorporé dans IntelliJ

Dans IntelliJ => Analyze | Run Inspection by Name... | Unused declaration.
Préciser dans IntelliJ que l'on considére que le code appellé uniquement dans les TU est inutile ( Inspection option -> Entry Point -> When entry point are in test sources mark callees as (used/unused))

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