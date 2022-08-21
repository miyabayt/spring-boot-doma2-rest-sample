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
  LEFT JOIN permissions p
    ON r.permission_code = p.permission_code
WHERE
  p.staff_role_id = /* id */1
