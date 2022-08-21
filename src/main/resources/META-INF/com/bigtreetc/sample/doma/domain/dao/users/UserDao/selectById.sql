SELECT
    user_id
    ,first_name
    ,last_name
    ,email
    ,tel
    ,zip
    ,address
    ,upload_file_id
    ,created_by
    ,created_at
    ,updated_by
    ,updated_at
    ,version
FROM
    users
WHERE
    user_id = /* id */1
