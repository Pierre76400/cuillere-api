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

#TODO Rajouter une table qui ne sert : ex : tmp_restaurant_2015


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
Je lance RestaurantControler.getPlat => On ne s'apercoit qu'il y a des N+1 car il y a des eager
TODO requête findPlatByIdRestaurant (on fait une jointure donc on une grosse requête au lieu de 2)

## Le cache
Sur la méthode RestaurantControler.getPlat, on s'aperçoit qu'on récupére systématiquement les categorie_plat

Le cache est déja activé dans le projet (cf. pom.xml et application.properties)
Il suffit de rajouter l'annotation sur CategoriePlat:

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

## Pagination
Lancer la recherche restaurant "/restaurants/_search"
=> Cela retourne l'ensemble des résultats, c'est lent et volumineux
Généralement seul les premiers résultats sont pertinents.
Il faut paginer ("/restaurants/_search2")

TODO : Faut il mettre cette partie coté IHM ou coté back ?
TODO : Tester sur la base docker

## Utiliser les opérations bulk à la place d'opération unitaire
On supprimer tous les commentaires obsolétes (de + de 5 ans)
On lance la suppression ("avis/_obsoletes")
En regardant les requêtes, on s'aperçoit qu'il y a un "select" qui retourne l'ensemble des résultats et autant de delete que le nombre de résultat retourné par le sélect
Pour optimiser il faut une seule requête pour supprimer l'ensemble des résultats
On lance la suppression avec le bulk ("avis/_obsoletesBULK")

TODO : Tester sur la base docker (avec et sans bulk ,en fonction du temps de traitement je lancerai ou non les 2 suppressions en démo) 


# Refacto
Une refacto 

Faire un refacto sur une méthode qui retourne le détail d'un restaurant:
- Récupére les entity via des requêtes 
- Calcule la distance
- La méthode prend en paramétre un booléen "mile" pour indiquer si on doit convertir ou pas en mile
- Retourne les avis les plus pertinents


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
