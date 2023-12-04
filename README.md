# cuillere-api
"La cuillère", l'application  qui permet de consulter et de noter les restaurants prés de chez soi.

# Schéma de la base

```mermaid
graph LR
A(restaurant) -- 0,N --- B((cuisine))
B -- 1,1 --- C(plat)
C -- 1,1 --- D((appartient))
D -- 0,N --- E(categorie_plat)
A -- 0,N --- F((note))
F -- 1,1 --- G(avis)
```

# Lancement de l'application

Il existe 2 profils

## Profil local
L'application utilise une base embarquée avec un petit jeu de donnée. Adapté pour tester une nouvelle fonctionnalitée. 
Le jeu de test est dans le fichier "data.sql"
Il s'agit du profil par défaut.

## Profil docker
L'application utilise une base de données et d'autres outils lancés avec le dockercompose qui se situe dans le répertoire "/docker"


