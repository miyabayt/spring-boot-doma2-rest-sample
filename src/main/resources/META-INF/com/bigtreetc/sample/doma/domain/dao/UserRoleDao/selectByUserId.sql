SELECT
  user_role_id
  , ur.user_id
  , r.role_code
  , r.role_name
  , p.permission_code
  , p.permission_name
  , ur.version
FROM
  user_roles ur
  LEFT JOIN roles r
    ON ur.role_code = r.role_code
  LEFT JOIN role_permissions rp
    ON r.role_code = rp.role_code
  LEFT JOIN permissions p
    ON rp.permission_code = p.permission_code
WHERE
  ur.user_id = /* id */1
