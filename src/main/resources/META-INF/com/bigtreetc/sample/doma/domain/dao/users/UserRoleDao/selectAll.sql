SELECT
  user_role_id
  , ur.user_id
  , ur.role_id
  , r.role_code
  , r.role_name
  , p.category_code
  , p.permission_code
  , p.permission_name
  , ur.version
FROM
  user_roles ur
  LEFT JOIN roles r
    ON ur.role_code = r.role_code
  LEFT JOIN permissions p
    ON r.permission_code = p.permission_code
WHERE
/*%if userCriteria.id != null */
  AND ur.user_id = /* userCriteria.id */1
/*%end*/
/*%if permissionCriteria.permissionCode != null */
  AND p.permission_code = /* permissionCriteria.permissionCode */'01'
/*%end*/
