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

INSERT INTO categories (name, description)
SELECT 'Réseau', 'Équipements réseau et connectivité'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Réseau');

INSERT INTO categories (name, description)
SELECT 'Impression', 'Imprimantes et consommables'
    WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Impression');

-- Fournisseurs
INSERT INTO suppliers (address, contact, email, name, telephone)
SELECT '15 Rue de la Paix Paris', 'Jean Dupont', 'contact@techpro.fr', 'TechPro', '0123456789'
    WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'TechPro');

INSERT INTO suppliers (address, contact, email, name, telephone)
SELECT '8 Avenue des Champs Lyon', 'Marie Martin', 'contact@officeshop.fr', 'OfficeShop', '0987654321'
    WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'OfficeShop');

INSERT INTO suppliers (address, contact, email, name, telephone)
SELECT '22 Rue du Commerce Bordeaux', 'Pierre Bernard', 'contact@netplus.fr', 'NetPlus', '0556789012'
    WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'NetPlus');

-- Utilisateurs
INSERT INTO users (password, role, username)
SELECT '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'ADMIN', 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (password, role, username)
SELECT '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'USER', 'user'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

INSERT INTO users (password, role, username)
SELECT '$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS', 'USER', 'employe1'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'employe1');

-- Produits Informatique / TechPro
INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Ordinateur portable haute performance', 'Laptop Pro 15', 899.99, 1199.99, 10, s.id
FROM categories c, suppliers s WHERE c.name = 'Informatique' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Laptop Pro 15');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 10, c.id, 'Souris ergonomique sans fil', 'Souris Ergonomique', 15.99, 29.99, 25, s.id
FROM categories c, suppliers s WHERE c.name = 'Informatique' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Souris Ergonomique');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Clavier mécanique rétroéclairé', 'Clavier Mécanique', 45.00, 79.99, 15, s.id
FROM categories c, suppliers s WHERE c.name = 'Informatique' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Clavier Mécanique');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 3, c.id, 'Écran 27 pouces 4K', 'Écran 4K 27"', 250.00, 399.99, 3, s.id
FROM categories c, suppliers s WHERE c.name = 'Informatique' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Écran 4K 27"');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Disque SSD 1To NVMe', 'SSD 1To NVMe', 60.00, 99.99, 20, s.id
FROM categories c, suppliers s WHERE c.name = 'Informatique' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'SSD 1To NVMe');

-- Produits Bureautique / OfficeShop
INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 20, c.id, 'Ramette de papier A4 500 feuilles', 'Papier A4', 3.50, 6.99, 100, s.id
FROM categories c, suppliers s WHERE c.name = 'Bureautique' AND s.name = 'OfficeShop'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Papier A4');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 10, c.id, 'Stylos bille bleus lot de 10', 'Stylos Bille x10', 2.00, 4.99, 50, s.id
FROM categories c, suppliers s WHERE c.name = 'Bureautique' AND s.name = 'OfficeShop'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Stylos Bille x10');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Classeurs A4 dos 8cm lot de 5', 'Classeurs A4 x5', 8.00, 14.99, 30, s.id
FROM categories c, suppliers s WHERE c.name = 'Bureautique' AND s.name = 'OfficeShop'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Classeurs A4 x5');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 3, c.id, 'Agrafeuse de bureau professionnelle', 'Agrafeuse Pro', 12.00, 22.99, 2, s.id
FROM categories c, suppliers s WHERE c.name = 'Bureautique' AND s.name = 'OfficeShop'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Agrafeuse Pro');

-- Produits Mobilier / OfficeShop
INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 2, c.id, 'Bureau réglable en hauteur', 'Bureau Ajustable', 250.00, 399.99, 8, s.id
FROM categories c, suppliers s WHERE c.name = 'Mobilier' AND s.name = 'OfficeShop'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Bureau Ajustable');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 2, c.id, 'Chaise ergonomique de bureau', 'Chaise Ergonomique', 150.00, 249.99, 2, s.id
FROM categories c, suppliers s WHERE c.name = 'Mobilier' AND s.name = 'OfficeShop'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Chaise Ergonomique');

-- Produits Réseau / NetPlus
INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Switch réseau 8 ports Gigabit', 'Switch 8 ports', 35.00, 59.99, 12, s.id
FROM categories c, suppliers s WHERE c.name = 'Réseau' AND s.name = 'NetPlus'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Switch 8 ports');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Câble RJ45 Cat6 5m', 'Câble RJ45 5m', 3.00, 7.99, 50, s.id
FROM categories c, suppliers s WHERE c.name = 'Réseau' AND s.name = 'NetPlus'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Câble RJ45 5m');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 3, c.id, 'Routeur WiFi 6 double bande', 'Routeur WiFi 6', 80.00, 129.99, 3, s.id
FROM categories c, suppliers s WHERE c.name = 'Réseau' AND s.name = 'NetPlus'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Routeur WiFi 6');

-- Produits Impression / TechPro
INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 2, c.id, 'Imprimante laser noir et blanc', 'Imprimante Laser', 120.00, 199.99, 5, s.id
FROM categories c, suppliers s WHERE c.name = 'Impression' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Imprimante Laser');

INSERT INTO products (alert_threshold, category_id, description, name, purchase_price, sale_price, stock_quantity, supplier_id)
SELECT 5, c.id, 'Cartouche toner noir compatible', 'Toner Noir', 25.00, 44.99, 4, s.id
FROM categories c, suppliers s WHERE c.name = 'Impression' AND s.name = 'TechPro'
                                 AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Toner Noir');

-- Mouvements de stock
INSERT INTO movements_stock (date, quantity, reason, type, product_id, user_id)
SELECT NOW(), 5, 'Réapprovisionnement initial', 'ENTRY', p.id, u.id
FROM products p, users u
WHERE p.name = 'Laptop Pro 15' AND u.username = 'admin'
  AND NOT EXISTS (SELECT 1 FROM movements_stock WHERE product_id = p.id AND reason = 'Réapprovisionnement initial');

INSERT INTO movements_stock (date, quantity, reason, type, product_id, user_id)
SELECT NOW(), 2, 'Vente client', 'OUTGOING', p.id, u.id
FROM products p, users u
WHERE p.name = 'Souris Ergonomique' AND u.username = 'admin'
  AND NOT EXISTS (SELECT 1 FROM movements_stock WHERE product_id = p.id AND reason = 'Vente client');