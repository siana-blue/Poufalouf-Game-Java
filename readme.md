# Poufalouf - Le Jeu (parmi tant d'autres) #

J'uploade ici un programme Java codé entre 2011 et 2012, mettant en scène un personnage nommé Poufalouf et son rival Douni.
Bon... le scénario s'arrête là, ce sont des histoires d'enfance adaptées en jeux vidéos.

Le jeu n'a aucun intérêt en soi puisque je ne l'ai pas terminé, mais le concept était le suivant :

- une map sur laquelle le personnage peut être déplacé, tirer avec Ctrl pour affaiblir son ennemi (voire le tuer),
sauter avec Alt et faire des tours de chaises (oui pourquoi pas...), afficher la zone d'interaction avec Douni grâce à Espace.
- une fois entré dans la zone d'interaction, le mode combat est lancé : il s'agit d'un combat à la Pokemon, avec la possibilité d'utiliser
deux objets par tour et une attaque qui conclut le tour. L'ordre des tours de jeu est tiré aléatoirement après que chaque personnage
ait joué, ce qui peut entraîner deux tours de suite.

Les musiques et tout le sound design est fait maison (j'assume), ainsi que les graphismes réalisés avec Blender.

## Installation ##

Utilise les librairies LWJGL de l'époque, les JAR externes suivants sont nécessaires pour lancer le projet :
- lwjgl.jar
- lwjgl_util.jar
- slick-util.jar
- jogg-0.0.7.jar
- jorbis-0.0.15.jar
Je n'ai pas fait le travail de rétrocompatibilité avec la nouvelle version de LWJGL, il faudrait utiliser les JAR de l'époque 2011/2012.

Le .JAR a été compilé avec la version 1.8 de Java.

Pour tester directement le logiciel, le JAR et les dépendances sont toutes dans l'archive zip présente dans ce repository.
AFIN D'EVITER UN DOUBLON : le dossier 'res' avec toutes les ressources graphiques et audio n'est présent que dans le zip.
Pour retrouver une configuraiton exécutable sous Eclipse, il faut mettre 'res' dans la racine du projet.

## Notes ##

J'ai retiré la Javadoc, elle est toujours en commentaires dans les fichiers sources, mais il faudrait la corriger des erreurs d'encodage.

Il y a des compile warnings, notamment des buffers ouverts et non fermés (leak), cela doit être à la marge, mais ce n'est pas corrigé
dans cette version d'origine non retravaillée depuis début 2012.
