SELECT
    /*%expand*/*
FROM
    codes
WHERE
/*%if criteria.id != null */
AND code_id = /* criteria.id */1
/*%end*/
/*%if criteria.categoryCode != null */
AND category_code = /* criteria.categoryCode */'GNR0001'
/*%end*/
/*%if criteria.codeValue != null */
AND code_value = /* criteria.codeValue */'01'
/*%end*/
ORDER BY code_id ASC, category_code ASC, display_order ASC
