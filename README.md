# Projet Android "Velib" réalisé par DAMOUR Quentin & MOUQUET César

Application mobile sur la plate-forme Android réalisé avec le langage Kotlin.

## Objectif de l'application :

- Afficher sur la carte les stations Vélib.
- Afficher le détail des stations Vélib : Nom de la station, nombre de vélos disponibles, et le nombre d'emplacements de retours disponibles.
- La possibilité de mettre en favoris vos stations préfèrés pour y accéder plus rapidement, et y accéder hors connexion. 

## Requirement :

- Au lancement de l'application, il faut autoriser l'application à utiliser les données de géocalisation.
- Lors du premier lancement de l'application, il est nécessaire d'être connecter à internet, pour permettre le bon fonctionnement de l'application. Une fois le premier chargement de l'application vous êtes libres de désactiver les données d'itinérance, mais vous n'aurez accès seulement à vos stations favorites.


## Aide au lancement :

Au lancement de l'application vous arriverez sur la carte du monde, vous aurez la possibilité de recentrer la caméra avec le bouton sur la droite géolocalisation.
Sur le volet en haut à droite, vous avez trois boutons :
- Le bouton en vert qui vous permet de rajouter vos stations en favoris, vous aurez la possibilité de rechercher vos stations favorites.
- Le coeur vous permettra d'accèder à vos stations favorites.
- La carte vous permet de recharger la carte et les données des stations.
Sur la carte vous pourrez apercevoir des cibles bleues, si vous cliquez sur un marqueur, vous allez pouvoir découvrir directement le détail de la station Vélib sur la carte! 

## Points à améliorer :

Cette application est loin d'être parfaite, il reste des points à améliorer :
- Nous avons un problème lorsque la liste de favoris est vide, si on veut afficher cette liste l'application cesse de fonctionner.
- Chargement très long pour recharger l'application.
- Nous n'avons pas réussi à positionner la caméra de l'utilisateur sur sa position au lancement de l'application.
- Nous avons eu des problèmes sur l'implémentation d'une fonction "supprimer" de la liste des favoris. Elle n'est donc pas disponible.
- Nous avons eu un problème sur l'update des favoris, nous avons des stations qui comportent des caractères " ' ", et nous n'avons pas réussi à les enlever, ce qui provoque une erreur dans la requête sql.
