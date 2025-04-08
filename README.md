# Blog Project

## Table Schemas
users Table:
```
+------------+----------------+------+-----+---------------------+-------------------------------+
| Field      | Type           | Null | Key | Default             | Extra                         |
+------------+----------------+------+-----+---------------------+-------------------------------+
| user_id    | int(11)        | NO   | PRI | NULL                | auto_increment                |
| username   | varchar(255)   | NO   | UNI | NULL                |                               |
| email      | varchar(255)   | NO   | UNI | NULL                |                               |
| password   | varbinary(255) | NO   |     | NULL                |                               |
| salt       | varbinary(255) | NO   |     | NULL                |                               |
| role       | varchar(255)   | NO   |     | NULL                |                               |
| created_at | datetime       | NO   |     | current_timestamp() |                               |
| updated_at | datetime       | NO   |     | current_timestamp() | on update current_timestamp() |
+------------+----------------+------+-----+---------------------+-------------------------------+
```
temp_users Table:
```
+--------------+----------------+------+-----+---------------------+-------------------------------+
| Field        | Type           | Null | Key | Default             | Extra                         |
+--------------+----------------+------+-----+---------------------+-------------------------------+
| temp_user_id | int(11)        | NO   | PRI | NULL                | auto_increment                |
| username     | varchar(255)   | NO   | UNI | NULL                |                               |
| email        | varchar(255)   | NO   | UNI | NULL                |                               |
| password     | varbinary(255) | NO   |     | NULL                |                               |
| salt         | varbinary(255) | NO   |     | NULL                |                               |
| role         | varchar(255)   | NO   |     | NULL                |                               |
| created_at   | datetime       | NO   |     | current_timestamp() |                               |
| updated_at   | datetime       | NO   |     | current_timestamp() | on update current_timestamp() |
+--------------+----------------+------+-----+---------------------+-------------------------------+
```

register_table:
```
+----------------+----------------+------+-----+---------------------+----------------+
| Field          | Type           | Null | Key | Default             | Extra          |
+----------------+----------------+------+-----+---------------------+----------------+
| register_id    | int(11)        | NO   | PRI | NULL                | auto_increment |
| temp_user_id   | int(11)        | NO   | MUL | NULL                |                |
| register_token | varbinary(255) | NO   | UNI | NULL                |                |
| created_at     | datetime       | NO   |     | current_timestamp() |                |
| expired_at     | datetime       | NO   |     | NULL                |                |
+----------------+----------------+------+-----+---------------------+----------------+
```
cookie_table:
```
+-------------+----------------+------+-----+---------------------+----------------+
| Field       | Type           | Null | Key | Default             | Extra          |
+-------------+----------------+------+-----+---------------------+----------------+
| cookie_id   | int(11)        | NO   | PRI | NULL                | auto_increment |
| user_id     | int(11)        | NO   | MUL | NULL                |                |
| cookie_data | varbinary(255) | NO   | UNI | NULL                |                |
| created_at  | datetime       | NO   |     | current_timestamp() |                |
| expired_at  | datetime       | NO   |     | NULL                |                |
+-------------+----------------+------+-----+---------------------+----------------+

```




