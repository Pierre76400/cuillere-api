# 1ére partie
TODO expliquer but de cette partie

Présentation de la table Restaurant sur l'analyseur de requête : présentation des données, du modéle de la place prise
La table Restaurant a une taille de 3,5M
Demander à l'auditoire ou est ce que l'on peut gagner de la place

2 données peuvent être optimisé sur cette table
- La date de création qui n'a pas besoin d'heure
- La donnée "vegetarien" peut être représenté par un un type booléen plutôt que par un varchar contenant les chaines "OUI" ou "NON"  

## Démo 

Migrer la donnée de date => datetime
```
ALTER TABLE restaurant ADD date_creation_migration date NULL;
update restaurant set date_creation_migration=CAST(date_creation AS DATE);
ALTER TABLE restaurant drop column date_creation;
ALTER TABLE restaurant RENAME COLUMN date_creation_migration TO date_creation;

//TODO Vérifier que l'on ne peut pas faire les 4 opérations précdente en une seule
// ??? ALTER TABLE restaurant ALTER COLUMN date_creation TYPE date
```


Migrer le booléen végétarien
```
ALTER TABLE restaurant ADD vegetarien_migration bool NULL;
update restaurant set vegetarien_migration=true where vegetarien ='OUI';
update restaurant set vegetarien_migration=false where vegetarien ='NON';
ALTER TABLE restaurant drop column vegetarien;
ALTER TABLE restaurant RENAME COLUMN vegetarien_migration TO vegetarien;

//TODO Vérifier que l'on ne peut pas faire les 4 opérations précdente en une seule
// ??? ALTER TABLE restaurant ALTER COLUMN date_creation TYPE date
```

Faire un vaccum pour voir l'espace libéré
```
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.restaurant;
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.plat;
VACUUM (FULL, VERBOSE, ANALYZE, TRUNCATE) public.avis;
```

On est passé de 3,5Mo à 3M

## Démo partie table Avis

Avant modif 8.3 Mo

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
On est passé à 7.8 Mo

TODO
Table avis : supprimer les commentaires obsoléte (par exemple de + de 5 ans)
Autre idée : supprimer les doublons , par exemple les restaurants déterminer les doublons à partir adresse
            faire une table "note" à part et mettre en évidence la place que l'on peut gagner avec un "bon" design
Ne pas utiliser des clob à la place varchar

# Hibernate
Pagination, cache
## Afficher les requêtes
Faire appel au service qui récupére les restaurants(GET localhost:8080/restaurants)
Pour afficher les requêtes : logging.level.org.hibernate.SQL=DEBUG

## Eviter les N+1
Suite à l'appel au service qui récupére les restaurants(GET localhost:8080/restaurants) on voit une requête N+1
Il faut modifier la requête du repository RestaurantCustomRepository.getDetailsById pour charger les palts
oldRequete => from Restaurant r join fetch r.plats where r.id=:idRestaurant

## Lazy vs Eager
Je lance RestaurantControler.getPlat => ON s'apercoit qu'il n'y a des N+1
TODOrequête findPlatByIdRestaurant

## Le cache
Sur la méthode getPlat
Activer le cache dans application.properties:

hibernate.cache.use_second_level_cache=true
hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory

Rajouter l'annocation sur CategoriePlat:

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

ACTUELLEMENT NE MARCHE PAS


## Pagination
Sur la méthode recherche restaurant

## Utiliser les opérations bulk à la place d'opération unitaire




# Refacto
Une refacto 

# Partie Asynchrone
Simuler l'heure de pointe (cad les personnes qui donne les avis et qui consulte les restaurant de 20 à 22h)

# Obsolescence
Par exemple du code fait pour un truc exceptionnel (fête des méres 2022) => Outil détectiin code mort
Code dupliqué
Documentation

# Observabilité et métriques
