SELECT
  /*%expand*/*
FROM
  role_permissions
WHERE
/*%if criteria.roleCode != null */
  AND role_code = /* criteria.roleCode */'user.editUser'
/*%end*/
LIMIT
  1
