SELECT
  r.role_code
  , r.role_name
  , p.permission_code
  , p.permission_name
  , sr.created_by
  , sr.created_at
  , sr.updated_by
  , sr.updated_at
  , sr.version
FROM
  staff_roles sr
  LEFT JOIN roles r
    ON sr.role_id = r.role_id
  LEFT JOIN role_permissions rp
    ON r.role_code = rp.role_code
  LEFT JOIN permissions p
    ON rp.permission_code = p.permission_code
WHERE
/*%if criteria.id != null */
  AND p.staff_role_id = /* criteria.id */1
/*%end*/
/*%if criteria.permissionCode != null */
  AND permission_code = /* criteria.permissionCode */'01'
/*%end*/
