import mariadb

def init_db(SQL_HOST, SQL_PORT, ROOT_USER_PASSWORD, USER_NAME, USER_PASSWORD, DB_NAME, USERS_TABLE, COOKIE_TABLE, RESET_TABLE, REGISTER_TABLE):
    try:
        root_connection = mariadb.connect(
            host=SQL_HOST,
            port=SQL_PORT,
            user="root",
            passwd="root",
        )
    except mariadb.OperationalError:
        root_connection = mariadb.connect(
            host=SQL_HOST,
            port=SQL_PORT,
            user="root",
            passwd=ROOT_USER_PASSWORD,
        )

    cursor = root_connection.cursor()
    #change password of root to new password
    cursor.execute(f"ALTER USER `root`@`%` IDENTIFIED BY '{ROOT_USER_PASSWORD}'")
    cursor.execute(f"CREATE DATABASE IF NOT EXISTS {DB_NAME}")
    cursor.execute(f"CREATE USER IF NOT EXISTS `{USER_NAME}`@`%` IDENTIFIED BY '{USER_PASSWORD}'")
    cursor.execute(f"GRANT ALL privileges ON `{DB_NAME}`.* TO `{USER_NAME}`@`%`")
    root_connection.close()

    # Connect to database as non root to avoid mitigate damage to db
    user_connection = mariadb.connect(
        host=SQL_HOST,
        user=USER_NAME,
        port=SQL_PORT,
        password=USER_PASSWORD,
        database=DB_NAME
    )

    cursor = user_connection.cursor()
    create_user_table = f"""
    CREATE TABLE IF NOT EXISTS {USERS_TABLE} (
        user_id INT(11) NOT NULL AUTO_INCREMENT,
        username VARCHAR(255) NOT NULL UNIQUE,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARBINARY(255) NOT NULL,
        salt VARBINARY(255) NOT NULL,
        role VARCHAR(255) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (user_id)
    );
    """

    create_cookie_table = f"""
    CREATE TABLE IF NOT EXISTS {COOKIE_TABLE} (
        cookie_id INT(11) NOT NULL AUTO_INCREMENT,
        user_id INT(11) NOT NULL,
        cookie_data VARBINARY(255) NOT NULL UNIQUE,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        expired_at DATETIME NOT NULL,
        PRIMARY KEY (cookie_id),
        FOREIGN KEY (user_id) REFERENCES {USERS_TABLE}(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
    );
    """

    create_reset_table = f"""
    CREATE TABLE IF NOT EXISTS {RESET_TABLE} (
        reset_id INT(11) NOT NULL AUTO_INCREMENT,
        user_id INT(11) NOT NULL,
        reset_token VARBINARY(255) NOT NULL UNIQUE,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        expired_at DATETIME NOT NULL,
        PRIMARY KEY (reset_id),
        FOREIGN KEY (user_id) REFERENCES {USERS_TABLE}(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
    );
    """

    create_temp_user_table = f"""
    CREATE TABLE IF NOT EXISTS temp_users (
        temp_user_id INT(11) NOT NULL AUTO_INCREMENT,
        username VARCHAR(255) NOT NULL UNIQUE,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARBINARY(255) NOT NULL,
        salt VARBINARY(255) NOT NULL,
        role VARCHAR(255) NOT NULL,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (temp_user_id)
    );
    """

    create_register_table=f"""
    CREATE TABLE IF NOT EXISTS {REGISTER_TABLE} (
        register_id INT(11) NOT NULL AUTO_INCREMENT,
        temp_user_id INT(11) NOT NULL,
        register_token VARBINARY(255) NOT NULL UNIQUE,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        expired_at DATETIME NOT NULL,
        PRIMARY KEY (register_id),
        FOREIGN KEY (temp_user_id) REFERENCES temp_users(temp_user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
    );
    """

    cursor.execute(create_user_table)
    cursor.execute(create_cookie_table)
    cursor.execute(create_reset_table)
    cursor.execute(create_temp_user_table)
    cursor.execute(create_register_table)


    user_connection.commit()
    user_connection.close()


def create_users(SQL_HOST, SQL_PORT, USER_NAME, USER_PASSWORD, DB_NAME, USERS_TABLE):
    user_connection = mariadb.connect(
        host=SQL_HOST,
        port=SQL_PORT,
        user=USER_NAME,
        password=USER_PASSWORD,
        database=DB_NAME
    )


    cursor = user_connection.cursor()
    create_admin = f"""
        INSERT INTO {USERS_TABLE} (user_id, username, email, password, salt, role, created_at, updated_at)
        VALUES (1, 'admin', 'admin@mail.com', SHA2(CONCAT('admin',UNHEX('eabb53460fa953b6')), 256), 'eabb53460fa953b6', 'admin', NOW(), NOW());
    """

    create_author = f"""
        INSERT INTO {USERS_TABLE} (user_id, username, email, password, salt, role, created_at, updated_at)
        VALUES (2, 'author', 'author@mail.com', SHA2(CONCAT('author',UNHEX('6f7f4b9659cbfbe6')), 256), '6f7f4b9659cbfbe6', 'author', NOW(), NOW());
    """

    create_user = f"""
        INSERT INTO {USERS_TABLE} (user_id, username, email, password, salt, role, created_at, updated_at)
        VALUES  (3, 'user', 'user@mail.com', SHA2(CONCAT('user',UNHEX('391feef6e93e0b29')), 256), '391feef6e93e0b29', 'user', NOW(), NOW());
    """

    cursor.execute(create_admin)
    cursor.execute(create_author)
    cursor.execute(create_user)

    user_connection.commit()
    user_connection.close()