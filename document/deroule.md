# Avant la démo

## Pré requis
Pré requis:
- Installer docker-desktop
- Installer postman puis importer la collection qui se trouve dans le projet /document/cuillere-api.postman_collection.json
- Un client Sql avec Ihm (J'utilise personnellement DBeaver)
- Lancer sonar et anlyser le projet cf. chapitre # Installer sonar

## Init
Il faut avant la démo initialiser le projet:
- Lancer le docker-compose du projet (Ouvrir une ligne de commande dans le répertoire "/docker" et lancer la commande "docker-compose up")
- Exécuter la classe "CuillereApiApplication" avec le projet "docker"
- Lancer l'appel rest pour initialiser la base (GET sur "localhost:8080/init" ou appel dans POSTMAN)

# 0 - Intro
Présentation de l'application Rest. Le but est de lancer et présenter briévement les différents services Rest.

Lancer l'application (conteneur + SB) puis aller dans Postman , Onglet Application.
Présenter les 4 onglets et lancer quelques exemples (Garder les "mauvais exemples, comme les services qui récupére plusieurs Mo d'un coup (cf. get PLATS), pour plus tard)" )

# 1 - Base de donnée

Présentation de la table Restaurant sur l'analyseur de requête : présentation des données, du modéle de la place prise
Environ 30 000 restaurants, 100 000 avis et plats. Taille totale base 20 Mo

La table Restaurant a une taille de 3,5M
Demander à l'auditoire ou est ce que l'on peut gagner de la place

2 données peuvent être optimisés sur cette table
- La date de création qui n'a pas besoin d'heure
- La donnée "vegetarien" peut être représenté par un type booléen plutôt que par un varchar contenant les chaines "OUI" ou "NON"

## Démo

Migrer la donnée nom de char vers varchar:
```
ALTER TABLE restaurant ALTER COLUMN nom TYPE varchar(255)
```

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
```
La commande VACUUM dans PostgreSQL sert à libérer l'espace disque occupé par des objets obsolètes ou supprimés, à réorganiser les données stockées et à maintenir la performance du système. 

On est passé de 11 à 3.6 Mo


## Démo partie table Avis

Passer le type de donnée de la colonne "note" de int8 à smallint

```
ALTER TABLE avis ALTER COLUMN note TYPE smallint
```

Supprimer la colonne "lieu"

```
ALTER TABLE avis DROP COLUMN lieu;
```

Faire Vaccum
```
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.avis;
```
On est passé de 9,7 à 8,4 Mo

## Démo supprimer vieille tables inutiles
drop table tmp_restaurant_2015

## Ce qui n'est pas vu dans la démo
Table avis : supprimer les commentaires obsoléte (par exemple de + de 5 ans)
Autre idée : supprimer les doublons , par exemple les restaurants déterminer les doublons à partir adresse
faire une table "note" à part et mettre en évidence la place que l'on peut gagner avec un "bon" design
Ne pas utiliser des clob à la place varchar

## Faire le propre avant de passer à la suite
Exécuter "dropDatabase.sql"
Redémarrer application
Lancer " commande init"


# 2 - Orm et requête SQL
Pagination, cache,requête

## Pagination
Lancer la recherche restaurant "/restaurants/_search" avec la valeur "assembleur"
=> 8 résultats temps recherche pas top mais raisonnable

Lancer la recherche restaurant "/restaurants/_search" avec la valeur "Abordable"
=> Cela retourne beaucoup des résultats, c'est lent et volumineux
TODO noter taille et temps

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
=> Le résultat fait désormais quelque Ko et il est beaucop plus rapide

TODO
A CREUSER (la requête count):
@Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
nativeQuery = true)
Page<User> findByLastname(String lastname, Pageable pageable);


## Afficher les requêtes
Le service de recherche prend toujours trop de temps (380ms sur mon poste)
=> Il faut comprendre ce qui se passe donc afficher les requêtes
Pour afficher les requêtes : logging.level.org.hibernate.SQL=DEBUG dans application.properties
=> On voit plein de requêtes

## Lazy vs Eager
On voit plein de requêtes, les premiéres sont plein de requêtes pour récupére les avis. Hors on ne récupére pas les avis dans notre résultat !
Pourquoi ? Il faut aller dans l'entity Restaurant et regarder le mapping avec avis. La relation restaurant/entity est en eager !
=> PAsser la relation restaurant/entity en Lazy
=> relancer le test, c'est mieux

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

## Conclusion
Avant la conclusion lancer la réinit de la base

TODO A CREUSER:
Le code "avisRepository.deleteByDateCreationLessThan(datePurge);" fait la même chose que "avisRepository.findByDateCreationLessThan(datePurge).iterator().forEachRemaining(avis -> avisRepository.delete(avis))"
=> Lequel est le mieux , pour le moment j'ai gardé le 2éme car il illustre mieux la bad practice (par contre le 1er illustre le fait qu'il faut toujours faie attention aux requêtes génèrées)

# 3 - NEW VERSION Outils qualité de code et usine logicielle 
On a déja installé au préalable "sonar" et lancé l'analyse

Aller dans les défauts :
- Overview-> Overall code -> ConvertUtil : la méthode platEntityToDto est dupliqué => Il faut supprimer la copie
- AvisControler.java => Supprimer la ligne de code en commentaire
- DatabaseService.java la complexité du code


# 3 - VERSION DEPRECATED Outils qualité de code et usine logicielle 
On a déja installé au préalable "sonar"
=> Montrer le docker-compose
Etapes:
- La premiére fois on doit changer le mot (login/mdp par défaut : admin/admin)
- Créer un projet en local
- Saisir "cuillere-api" dans le nom du projet
- Use "global setting" puis next
-  How do you want to analyze your repository? => "Locally"
-  Générer le token=> Continue => Maven => Copier la ligne maven et la lancer (!!! Supprimer les "\" dans la cmd maven)
- Au bout de quelque minute(environ 2) le projet apparait

Aller dans le projet expliquer les différentes sections, bug, duplication couverture , ...
Aller dans quality profile et qu'on peut les personnaliser.Faire une démo :
- Appuyer sur create
- Extend an existing quality profile -> Java -> Sonar way -> Nommer la régle "eco"
- Aller dans activate more
- Rechercher dans le tag "eco-design"
- Appuyer sur "bulk change"
- Puis mettre le profil comme defaut
!!!! Ne pas lancer une nouvelle analyse car il y a une régle sonar du projet "eco-design" qui plante sur le projet

Expliquer la notion de quality gate

Aller sur le profil et expliquer la notion de "notification"

# 4 - Clean code
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

Refacto:
- 1ére chose à faire : rassembler les vaiables au + prés de leur utilisation
- Remplacer le code qui permet de récupérer l'entity Restaurant par restaurantRepository.findById(id).get()
- Externaliser le code de calcul
- Donner un indice 6371 c'est la longueur du rayon de la terre en Km => Créer constante pour le rayon de la terre
- Créer une constante pour le mile
- On comprend que le booléen "english" veut en fait représenter l'unité de mesure => On renomme la variable
- On comprend que c'est une méthode de calcul de distance => On renomme la méthode
- On revient sur la méthode initiale et on fait la conclusion

# 5 - Partie Asynchrone
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
- Dans le cadre de notre démo on le rajoute dans cuillere-api dans la réalité le consommateur serait surement dans un projet à part pour offrir un meilleur découpage et oermettre une scalabilité horizontale
- Le message est consommé. Montrer l'avis
- Relancer le test de création massif , montrer le résultat dans VisualVm et comparer à la solution synchrone (cf. screenshot)

# 6 - Obsolescence
Il existe des outils pour détecter le code mort :
- Dans eclipse, plugin Ucd detector
- Directement incorporé dans IntelliJ

Dans IntelliJ => Code | Analyze | Run Inspection by Name... | Unused declaration.
Préciser dans IntelliJ que l'on considére que le code appellé uniquement dans les TU est inutile ( Inspection option -> Entry Point -> When entry point are in test sources mark callees as (used/unused))

Aller dans RestaurantService la méthode getRestaurantFeteDesMeres2019 ne sert à rien
Aller dans PlatService une constante ne sert à rien

TODO vérifier la suppression du code mort dans IntelliJ avec les méthodes appelés uniquement dans les TU

```
 @Test
    void getRestaurantFeteDesMeres2019(){
        Restaurant leRipailleur= ModelHelper.createRestaurantLeRipailleur();

        CategoriePlat cpPrincipal=new CategoriePlat();
        cpPrincipal.setCode("CP");
        cpPrincipal.setLibelle("Plat principal");

        Plat boeuf=new Plat();
        boeuf.setLibelle("Boeuf bourguignon");
        boeuf.setCategoriePlat(cpPrincipal);
        boeuf.setPrix(10.5d);
        boeuf.setRestaurant(leRipailleur);

        leRipailleur.setPlats(List.of(boeuf));

        when(repository.getDetailsById(3l)).thenReturn(leRipailleur);

        RestaurantDetailDto res=restaurantService.getRestaurantFeteDesMeres2019(3l);

        assertEquals("Le Ripailleur",res.getNom());
        assertEquals(1,res.getPlats().size());
        assertEquals(9.45d,res.getPlats().get(0).getPrix(),0.01d);
    }
```
    

# 7 - Documentation
On souhaite avoir la documentation de notre api. On peut écrire le swagger à la main mais on n'est pas sur qu'il correspond au code.
=> Il faut générer la documentation à partir du code

```

   <!-- Pour activer swagger UI-->
   <!-- Pour accéder à Swagger : http://localhost:8080/swagger-ui/index.html -->
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>last-release-version</version>
   </dependency>
```

Aller sur swagger UI, Swagger UI expose la documentation et permet d'éxécuter des appels Rest

On peut rajouter de la documentation sur les méthodes , aller dans RestaurantControler.getRestaurant :
```
@Operation(summary = "Récupére un restaurant")
```
et sur les paramétres :
```
@Parameter(name = "idRestaurant", description = "Identifiant du restaurant", example = "45621") 
```
=> Redémarrer l'appli et remontrer le résultat






!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!! NON GARDE !!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

# Observabilité et métriques
https://www.baeldung.com/spring-boot-3-observability
On peut accéder aux metric via :
http://localhost:8080/actuator/metrics/avisService
Ne pas oublier d'exposer tous les actuators
management.endpoints.web.exposure.include=*

Exposer service restaurant dans metrics
PAsser ces metrics à prometheus
Brancher Grafana ?????

TODO préparer contener avec l'appli
TODO lien avec le GR491

# Détecter les dépendances inutiles
https://www.baeldung.com/maven-unused-dependencies
Outil maven qui permet de détecter les dépendances inutiles
maven-dependency-plugin

# Installer sonar
On a déja installé au préalable "sonar" dans le docker-compose
Etapes:
- La premiére fois on doit changer le mot (login/mdp par défaut : admin/admin)
- Créer un projet en local
- Saisir "cuillere-api" dans le nom du projet
- Use "global setting" puis next
-  How do you want to analyze your repository? => "Locally"
-  Générer le token=> Continue => Maven => Copier la ligne maven et la lancer (!!! Supprimer les "\" dans la cmd maven)
- Au bout de quelque minute(environ 2) le projet apparait
