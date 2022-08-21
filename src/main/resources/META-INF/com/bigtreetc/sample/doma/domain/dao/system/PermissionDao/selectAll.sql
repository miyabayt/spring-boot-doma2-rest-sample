SELECT
  /*%expand*/*
FROM
  permissions
WHERE
/*%if criteria.id != null */
  AND permission_id = /* criteria.id */1
/*%end*/
