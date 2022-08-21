SELECT
  /*%expand*/*
FROM
  holidays
WHERE
/*%if criteria.id != null */
  AND holiday_id = /* criteria.id */1
/*%end*/
/*%if criteria.holidayName != null */
  AND holiday_name = /* criteria.holidayName */1
/*%end*/
LIMIT 1
