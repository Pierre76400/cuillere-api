# cuillere-api
Application de l'application "La cuillère" démo GreenIt

## Installation et lancement

### Installation
Installer docker-desktop
Installer postman et importer la collection qui se trouve dans le projet /document/cuillere-api.postman_collection.json
Un client Sql avec Ihm (J'utilise personnellement DBeaver)
### Lancement
Lancer le docker-compose du projet (Ouvrir une ligne de commande dans le répertoire "/docker" et lancer la commande "docker-compose up")
Exécuter la classe "CuillereApiApplication" avec le projet "docker"
Lancer l'appel rest pour initialiser la base (GET sur "localhost:8080/init" ou appel dans POSTMAN)

