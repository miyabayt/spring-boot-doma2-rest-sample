SELECT
  /*%expand*/*
FROM
  roles
WHERE
/*%if criteria.id != null */
  AND role_id = /* criteria.id */1
/*%end*/
/*%if criteria.roleCode != null */
  AND role_code = /* criteria.roleCode */'user.editUser'
/*%end*/
LIMIT
  1
