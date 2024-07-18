SELECT
  staff_role_id
  , sr.staff_id
  , r.role_code
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
    ON sr.role_code = r.role_code
  LEFT JOIN role_permissions rp
    ON r.role_code = rp.role_code
  LEFT JOIN permissions p
    ON rp.permission_code = p.permission_code
WHERE
/*%if staffRoleCriteria.staffId != null */
  AND sr.staff_id = /* staffRoleCriteria.staffId */1
/*%end*/
/*%if staffRoleCriteria.permissionCode != null */
  AND p.permission_code = /* staffRoleCriteria.permissionCode */'01'
/*%end*/
