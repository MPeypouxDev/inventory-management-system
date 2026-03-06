-- Catégories
MERGE INTO categories KEY(name) VALUES (1, 'Informatique', 'Matériel et accessoires informatiques');
MERGE INTO categories KEY(name) VALUES (2, 'Bureautique', 'Fournitures de bureau');
MERGE INTO categories KEY(name) VALUES (3, 'Mobilier', 'Meubles et équipements de bureau');

-- Fournisseurs
MERGE INTO suppliers KEY(name) VALUES (1, '15 Rue de la Paix Paris', 'Jean Dupont', 'contact@techpro.fr', 'TechPro', '0123456789');
MERGE INTO suppliers KEY(name) VALUES (2, '8 Avenue des Champs Lyon', 'Marie Martin', 'contact@officeshop.fr', 'OfficeShop', '0987654321');

-- Utilisateurs
MERGE INTO users KEY(username) VALUES (1, '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'ADMIN', 'admin');
MERGE INTO users KEY(username) VALUES (2, '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'USER', 'user');

-- Produits
MERGE INTO products KEY(name) VALUES (1, 5, 'Ordinateur portable haute performance', 'Laptop Pro 15', 899.99, 1199.99, 10, 1, 1);
MERGE INTO products KEY(name) VALUES (2, 10, 'Souris ergonomique sans fil', 'Souris Ergonomique', 15.99, 29.99, 25, 1, 1);
MERGE INTO products KEY(name) VALUES (3, 5, 'Bureau réglable en hauteur', 'Bureau Ajustable', 250.00, 399.99, 8, 3, 2);
MERGE INTO products KEY(name) VALUES (4, 20, 'Ramette de papier A4 500 feuilles', 'Papier A4', 3.50, 6.99, 100, 2, 2);