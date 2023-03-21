SELECT
  /*%expand*/*
FROM
  staffs
WHERE
/*%if criteria.id != null */
  AND staff_id = /* criteria.id */1
/*%end*/
/*%if criteria.email != null */
  AND email = /* criteria.email */'aaaa@bbbb.com'
/*%end*/
/*%if criteria.passwordResetToken != null */
  AND password_reset_token = /* criteria.passwordResetToken */'xxxx'
  AND token_expires_at > NOW()
/*%end*/
LIMIT 1
