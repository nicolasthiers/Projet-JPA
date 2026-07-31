# Projet JPA — IMDb Cinéma

Projet Java/Hibernate (JPA) de gestion d'une base de données de type IMDb : films,
acteurs, réalisateurs, rôles, pays, langues et lieux. Le projet fournit :

- un **import** de données depuis des fichiers CSV vers une base MariaDB (`ImportApp`) ;
- un **menu console** permettant d'interroger cette base (`MenuApp`).

## Sommaire

- [Stack technique](#stack-technique)
- [Structure du projet](#structure-du-projet)
- [Modèle de données](#modèle-de-données)
- [Prérequis](#prérequis)
- [Configuration de la base de données](#configuration-de-la-base-de-données)
- [Compilation](#compilation)
- [Import des données CSV](#import-des-données-csv)
- [Menu de consultation](#menu-de-consultation)
- [Javadoc](#javadoc)

## Stack technique

- **Java 25**
- **Maven**
- **Hibernate ORM** (`hibernate-core` 7.4.4) comme implémentation JPA
- **MariaDB** (`mariadb-java-client` 3.5.9) comme SGBD

## Structure du projet

```
src/main/java/fr/imdb/
├── app/          # Points d'entrée (main)
│   ├── ImportApp.java     # Importe les CSV en base
│   └── MenuApp.java       # Menu console de consultation
├── dao/          # Accès aux données (un DAO par entité)
├── entities/     # Entités JPA (Film, Acteur, Realisateur, Role, Pays, Langue, ...)
└── service/      # Logique métier
    ├── ImportService.java  # Recherche/création des entités à l'import
    └── CsvImporter.java    # Lecture des fichiers CSV

src/main/resources/
├── META-INF/persistence.xml   # Configuration JPA / connexion à la base
├── pays.csv
├── acteurs.csv
├── realisateurs.csv
├── films.csv
├── film_realisateurs.csv
├── roles.csv
└── castingPrincipal.csv

conception/
└── Projet-JPA.vpp   # Diagrammes de conception (modèle Visual Paradigm)
```

## Modèle de données

- **Personne** *(classe abstraite)* : socle commun à `Acteur` et `Realisateur`
  (identifiant IMDb, identité, date et lieu de naissance).
- **Acteur** : `Personne` + taille, URL IMDb, et ses `Role` (rôles joués).
- **Realisateur** : `Personne` + URL IMDb, et ses `Film` réalisés (many-to-many).
- **Film** : titre, année, note, résumé ; lié à un `LieuDeTournage`, une `Langue`,
  un `Pays`, une liste de `Genre`, ses `Role` (casting) et ses `Realisateur`.
- **Role** : table d'association enrichie entre `Film` et `Acteur` (personnage
  joué, casting principal ou non).
- **Genre** *(enum)* : genres cinématographiques, converti vers/depuis son libellé
  texte en base via `GenreConverter`.
- **Pays**, **Langue**, **LieuDeNaissance**, **LieuDeTournage** : référentiels
  partagés entre plusieurs films/personnes.

## Prérequis

- JDK 25
- Maven 3.9+
- Un serveur MariaDB (ou MySQL) accessible en local

## Configuration de la base de données

1. Créer une base nommée `cinema` :
   ```sql
   CREATE DATABASE cinema CHARACTER SET utf8mb4;
   ```
2. Adapter si besoin les identifiants de connexion dans
   `src/main/resources/META-INF/persistence.xml` (URL JDBC, utilisateur, mot de passe).
   Par défaut, l'utilisateur est `root` sans mot de passe et l'URL pointe vers
   `jdbc:mariadb://localhost:3306/cinema`.
3. Le schéma est créé/mis à jour automatiquement au démarrage
   (`hibernate.hbm2ddl.auto=update`) : aucun script SQL manuel n'est nécessaire.

## Compilation

```bash
mvn compile
```

## Import des données CSV

`ImportApp` lit les fichiers CSV présents dans `src/main/resources` et peuple la
base, dans un ordre respectant les dépendances entre entités :

1. `pays.csv`
2. `acteurs.csv`
3. `realisateurs.csv`
4. `films.csv`
5. `film_realisateurs.csv` (associations film ↔ réalisateur)
6. `roles.csv` + `castingPrincipal.csv` (rôles des acteurs, avec indication du casting principal)

Chaque fichier est au format `;`-séparé, encodé en UTF-8, avec une ligne d'en-tête.
L'import est idempotent : relancer `ImportApp` ne crée pas de doublons (déduplication
par identifiant IMDb, ou par triplet film/acteur/personnage pour les rôles).

Les erreurs de lecture/parsing (ligne malformée, date ou taille invalide, etc.) sont
journalisées sans interrompre l'import, dans le fichier `erreurs_import.log` généré
à la racine du projet.

Pour lancer l'import :

```bash
mvn compile exec:java -Dexec.mainClass="fr.imdb.app.ImportApp"
```

*(ou exécuter directement `ImportApp` depuis votre IDE)*

## Menu de consultation

`MenuApp` propose un menu console interactif pour interroger la base :

1. Filmographie d'un acteur
2. Casting d'un film
3. Films sortis entre deux années
4. Films communs à deux acteurs
5. Acteurs communs à deux films
6. Films entre deux années avec un acteur donné
7. Quitter

Pour le lancer :

```bash
mvn compile exec:java -Dexec.mainClass="fr.imdb.app.MenuApp"
```

*(ou exécuter directement `MenuApp` depuis votre IDE)*

## Javadoc

Le code est documenté en Javadoc (entités, DAO, services, points d'entrée). Pour
générer la documentation HTML :

```bash
mvn javadoc:javadoc
```

La documentation générée est disponible dans `target/reports/apidocs/index.html`.
