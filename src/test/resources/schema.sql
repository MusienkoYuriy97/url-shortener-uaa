DROP TABLE IF EXISTS activate_key;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users(
    uuid UUID DEFAULT gen_random_uuid(),
    email VARCHAR (62) NOT NULL UNIQUE,
    first_name VARCHAR (46) NOT NULL,
    last_name VARCHAR (46) NOT NULL,
    password VARCHAR (255) NOT NULL,
    user_role VARCHAR (15) NOT NULL,
    user_status VARCHAR (15) NOT NULL,
    PRIMARY KEY (uuid));

CREATE TABLE IF NOT EXISTS activate_key(
    uuid UUID DEFAULT gen_random_uuid(),
    simple_key VARCHAR (15) NOT NULL UNIQUE,
    user_uuid UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (uuid),
    FOREIGN KEY (user_uuid)
    REFERENCES users (uuid));