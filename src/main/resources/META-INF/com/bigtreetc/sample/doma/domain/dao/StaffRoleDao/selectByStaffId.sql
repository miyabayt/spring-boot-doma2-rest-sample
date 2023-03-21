SELECT
  staff_role_id
  , sr.staff_id
  , r.role_code
  , r.role_name
  , p.permission_code
  , p.permission_name
  , sr.version
FROM
  staff_roles sr
  LEFT JOIN roles r
    ON sr.role_code = r.role_code
  LEFT JOIN role_permissions rp
    ON r.role_code = rp.role_code
  LEFT JOIN permissions p
    ON rp.permission_code = p.permission_code
WHERE
  sr.staff_id = /* id */1
