DELETE FROM roles WHERE created_by = 'init';
INSERT INTO roles (role_code, role_name, created_by, created_at, updated_by, updated_at) VALUES
('system_admin', 'システム管理者', 'init', NOW(), 'init', NOW()),
('operation_admin', '運用管理者', 'init', NOW(), 'init', NOW()),
('operator', '運用者', 'init', NOW(), 'init', NOW()),
('user', 'ユーザ', 'init', NOW(), 'init', NOW());

DELETE FROM permissions WHERE created_by = 'init';
INSERT INTO permissions (permission_code, permission_name, created_by, created_at, updated_by, updated_at) VALUES
('codeCategory:read', 'コード分類マスタ検索', 'init', NOW(), 'init', NOW()),
('codeCategory:save', 'コード分類マスタ登録・編集', 'init', NOW(), 'init', NOW()),
('code:read', 'コードマスタ検索', 'init', NOW(), 'init', NOW()),
('code:save', 'コードマスタ登録・編集', 'init', NOW(), 'init', NOW()),
('holiday:read', '祝日マスタ検索', 'init', NOW(), 'init', NOW()),
('holiday:save', '祝日マスタ登録・編集', 'init', NOW(), 'init', NOW()),
('mailTemplate:read', 'メールテンプレート検索', 'init', NOW(), 'init', NOW()),
('mailTemplate:save', 'メールテンプレート登録・編集', 'init', NOW(), 'init', NOW()),
('role:read', 'ロール検索', 'init', NOW(), 'init', NOW()),
('role:save', 'ロール登録・編集', 'init', NOW(), 'init', NOW()),
('uploadFile', 'ファイルアップロード', 'init', NOW(), 'init', NOW()),
('user:read', 'ユーザマスタ検索', 'init', NOW(), 'init', NOW()),
('user:save', 'ユーザマスタ登録・編集', 'init', NOW(), 'init', NOW()),
('staff:read', '担当者マスタ検索', 'init', NOW(), 'init', NOW()),
('staff:save', '担当者マスタ登録・編集', 'init', NOW(), 'init', NOW());

DELETE FROM role_permissions WHERE created_by = 'init';
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at, updated_by, updated_at)
SELECT 'system_admin', permission_code, 1, 'init', NOW(), 'init', NOW() FROM permissions;
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at, updated_by, updated_at)
SELECT 'operation_admin', permission_code, 0, 'init', NOW(), 'init', NOW() FROM permissions;
INSERT INTO role_permissions (role_code, permission_code, is_enabled, created_by, created_at, updated_by, updated_at)
SELECT 'operator', permission_code, 0, 'init', NOW(), 'init', NOW() FROM permissions;

DELETE FROM staff_roles WHERE created_by = 'init';
INSERT INTO staff_roles (staff_id, role_code, created_by, created_at, updated_by, updated_at) VALUES
((SELECT staff_id FROM staffs WHERE email = 'test@example.com'), 'system_admin', 'init', NOW(), 'init', NOW());

DELETE FROM user_roles WHERE created_by = 'init';
INSERT INTO user_roles (user_id, role_code, created_by, created_at, updated_by, updated_at) VALUES
((SELECT user_id FROM users WHERE email = 'test@example.com'), 'user', 'init', NOW(), 'init', NOW());
