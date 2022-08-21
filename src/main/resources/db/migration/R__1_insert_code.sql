DELETE FROM code_categories WHERE created_by = 'init';
INSERT INTO code_categories(category_code, category_name, created_by, created_at, updated_by, updated_at) VALUES
('target', '対象区分', 'init', NOW(), 'init', NOW()),
('presence', '有無区分', 'init', NOW(), 'init', NOW());

DELETE FROM codes WHERE created_by = 'init';
INSERT INTO codes(category_code, code_value, code_name, code_alias, display_order, is_invalid, created_by, created_at, updated_by, updated_at) VALUES
('target', '1', '対象', NULL, 1, 0, 'init', NOW(), 'init', NOW()),
('target', '0', '非対象', NULL, 1, 0, 'init', NOW(), 'init', NOW()),
('presence', '1', '無', NULL, 1, 0, 'init', NOW(), 'init', NOW()),
('presence', '2', '有', NULL, 2, 0, 'init', NOW(), 'init', NOW());
