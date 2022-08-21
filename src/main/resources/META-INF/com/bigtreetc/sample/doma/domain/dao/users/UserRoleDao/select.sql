SELECT
  r.role_code
  , r.role_name
  , p.category_code
  , p.permission_code
  , p.permission_name
  , ur.version
FROM
  user_roles ur
  LEFT JOIN roles r
    ON ur.role_id = r.role_id
  LEFT JOIN permissions p
    ON r.permission_code = p.permission_code
WHERE
/*%if criteria.id != null */
  AND p.user_role_id = /* criteria.id */1
/*%end*/
/*%if criteria.permissionCode != null */
  AND permission_code = /* criteria.permissionCode */'01'
/*%end*/
