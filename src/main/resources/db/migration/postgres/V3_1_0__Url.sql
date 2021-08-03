CREATE TABLE IF NOT EXISTS url (
    uuid UUID DEFAULT gen_random_uuid(),
    origin_url VARCHAR (255) NOT NULL,
    short_url VARCHAR (255) NOT NULL,
    user_uuid UUID NOT NULL,
    PRIMARY KEY (uuid),
    FOREIGN KEY (user_uuid)
        REFERENCES users(uuid));

-- CREATE TABLE IF NOT EXISTS user_url (
--     uuid UUID DEFAULT gen_random_uuid(),
--     user_uuid UUID NOT NULL,
--     url_uuid UUID NOT NULL,
--     createdAt timestamp NOT NULL,
--     expiration timestamp NOT NULL,
--     PRIMARY KEY (uuid),
--     FOREIGN KEY (user_uuid)
--         REFERENCES users (uuid),
--     FOREIGN KEY (url_uuid)
--         REFERENCES url (uuid));