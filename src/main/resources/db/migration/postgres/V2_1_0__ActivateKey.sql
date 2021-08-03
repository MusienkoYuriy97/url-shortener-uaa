CREATE TABLE IF NOT EXISTS activate_key(
    uuid UUID DEFAULT gen_random_uuid(),
    simple_key VARCHAR (15) NOT NULL UNIQUE,
    user_uuid UUID NOT NULL,
    PRIMARY KEY (uuid),
    FOREIGN KEY (user_uuid)
    REFERENCES users (uuid));