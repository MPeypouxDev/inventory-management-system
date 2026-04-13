-- Catégories
INSERT INTO categories (name, description)
SELECT 'Informatique', 'Matériel et accessoires informatiques'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Informatique');

INSERT INTO categories (name, description)
SELECT 'Bureautique', 'Fournitures de bureau'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Bureautique');

INSERT INTO categories (name, description)
SELECT 'Mobilier', 'Meubles et équipements de bureau'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Mobilier');

-- Fournisseurs
INSERT INTO suppliers (address, contact, email, name, telephone)
SELECT '15 Rue de la Paix Paris', 'Jean Dupont', 'contact@techpro.fr', 'TechPro', '0123456789'
    WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'TechPro');

INSERT INTO suppliers (address, contact, email, name, telephone)
SELECT '8 Avenue des Champs Lyon', 'Marie Martin', 'contact@officeshop.fr', 'OfficeShop', '0987654321'
    WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'OfficeShop');

-- Utilisateurs
INSERT INTO users (password, role, username)
SELECT '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'ADMIN', 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (password, role, username)
SELECT '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'USER', 'user'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

-- Produits
INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Ordinateur portable haute performance', 'Laptop Pro 15', 899.99, 1199.99, 10, s.id
FROM categories c, suppliers s
WHERE c.name = 'Informatique' AND s.name = 'TechPro'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Laptop Pro 15');