# Stock Manager

> Application desktop de gestion de stock développée en Java 21 avec JavaFX et Spring Boot.

 ![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green?style=flat-square&logo=springboot)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?style=flat-square)
![H2](https://img.shields.io/badge/Database-H2-lightblue?style=flat-square)
![Tests](https://img.shields.io/badge/Tests-29%20passing-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)
 
 ## Aperçu
 
 | Dashboard                                    | Produits                                   | Mouvements                                     |
 |----------------------------------------------|--------------------------------------------|------------------------------------------------|
 | ![dashboard](docs/screenshots/dashboard.png) | ![produits](docs/screenshots/produits.png) | ![mouvements](docs/screenshots/mouvements.png) |
 
 ## Fonctionnalités
 
 - **Gestion des produits** - CRUD complet, alertes stock bas, historique
 - **Gestion des fournisseurs** - CRUD avec validation métier
 - **Mouvements de stock** - Entrées/Sorties avec mise à jour automatique
 - **Dashboard** - Statistiques en temps réel, graphiques BarChart et PieChart
 - **Filtres avancés** - Par catégorie, statut, type, produit et date
 - **Export** - Produits en Excel (.xlsx) et mouvements en PDF
 - **Multi-utilisateurs** - Authentification BCrypt, rôles ADMIN/USER
 - **Alertes visuelles** - Produits en stock bas surlignés en rouge
 - **Pagination** - Dans toutes les vues
 
 ## Stack technique
 | Technologie | Version | Usage |
 |-------------|---------|-------|
 | Java | 21 | Langage principal |
 | JavaFX | 21 | Interface graphique |
 | Spring Boot | 4.0.3 | Framework backend |
 | Spring Data JPA | - | Persistance des données |
 | H2 Database | - | Base de données embarquée |
 | BCrypt | - | Hashage des mots de passe |
 | Apache POI | - | Export Excel |
 | iText | - | Export PDF |
 | Lombok | - | Réduction du boilerplate |
 | JUnit 5 + Mockito | - | Tests unitaires |
 | Maven | - | Gestion des dépendances |
 | Launch4j | - | Packaging Windows .exe |
 
 ## Architecture
 
 src/main/java/com/stockmanager
 |-- config/ # Configuration Spring + Sécurité
 |-- controller/ # Contrôleurs JavaFX (MVC)
 |-- model/ # Entités JPA
 |-- repository/ # Spring Data JPA Repositories
 |-- service/ # Logique métier
 |-- util/ # Classes utilitaires
 
 **Design patterns utilisés** :
 - **MVC** - Séparation Controller / Service / Repository
 - **Singleton** - SessionManager (gestion de session utilisateur)
 - **Repository** - Abstraction de l'accès aux données (évolution moderne du pattern DAO)
 
 ## Installation et lancement
 
 ### Prérequis
 
 - Java 21+
 - Maven 3.8 +
 
 ### Lancer depuis les sources
 
 ```bash
 git clone https://github.com/MPeypouxDev/inventory-management-system.git
 cd inventory-management-system
 .\mvnw javafx:run
 ```

 ### Lancer le .exe (Windows)
 
 1. Télécharge le fichier `stock-manager.exe` depuis les [Releases](https://github.com/MPeypouxDev/inventory-management-system/releases)
 2. Double-clique sur `stock-manager.exe`
 
 ### Comptes par défaut
 
 | Rôle | Login | Mot de passe |
 |------|-------|--------------|
 | Administrateur | `admin` | `admin` |
 | Utilisateur | `user` | `user` |
 
 ## Tests
 
 ```bash
 .\mvnw test
 ```
 
 **29 tests unitaires** couvrant :
 - `AuthService` - authentification
 - `ProductService` - gestion produits
 - `CategoryService` - gestion catégories
 - `SupplierService`- gestion fournisseurs
 - `StockMovementService` - mouvements de stock
 - `UserService` - gestion utilisateurs
 
 ## Livrables
 
 | Livrable | Lien |
 |----------|------|
 | Code source | Ce repository |
 | Manuel utilisateur | [`docs/manuel-utilisateur.pdf`](src/main/resources/docs/manuel-utilisateur.pdf) |
 | Diagramme de classes | [`uml/class-diagram.png`](src/main/resources/uml/class-diagram.png) |
 | Diagramme cas d'utilisation Admin | [`uml/use-case-admin.png`](src/main/resources/uml/use-case-admin.png) |
 | Diagramme cas d'utilisation User | [`uml/use-case-user.png`](src/main/resources/uml/use-case-user.png) |
 | Vidéo démo | A venir |
 
 ## Auteur
 
 **Mathys Peypoux**
 Bac+3 Intelligence Artificielle & Big Data
 GitHub : [@MPeypouxDev](https://github.com/MPeypouxDev)
 
 ## Licence 
 
 Ce projet est sous licence MIT.
