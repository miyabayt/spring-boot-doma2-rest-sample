SELECT
    /*%expand*/*
FROM
    holidays
WHERE
/*%if criteria.id != null */
    AND holiday_id = /* criteria.id */1
/*%end*/
/*%if criteria.holidayName != null */
    AND holiday_name LIKE /* @infix(criteria.holidayName) */'元旦'
/*%end*/
/*%if criteria.holidayDate != null */
    AND holiday_date = /* criteria.holidayDate */'2000-01-01'
/*%end*/
ORDER BY
    holiday_id ASC
