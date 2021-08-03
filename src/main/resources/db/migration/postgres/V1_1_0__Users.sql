CREATE TABLE IF NOT EXISTS users(
    uuid UUID DEFAULT gen_random_uuid(),
    email VARCHAR (62) NOT NULL UNIQUE,
    first_name VARCHAR (46) NOT NULL,
    last_name VARCHAR (46) NOT NULL,
    password VARCHAR (255) NOT NULL,
    user_role VARCHAR (15) NOT NULL,
    user_status VARCHAR (15) NOT NULL,
    PRIMARY KEY (uuid));

INSERT INTO users (email,first_name,last_name,password,user_role,user_status)
values (
           '97musienko@gmail.com',
           'Yuriy',
           'Musienko',
           '$2y$12$QKQ2d03.6SCPEQhLlaZusOJT9Q/ODqnvFHT6JCdpqvxKeXHa7KviG',
           'ROLE_ADMIN',
           'ACTIVE'
       ),
       (
           'pavel@gmail.com',
           'Pavel',
           'Pavlov',
           '$2y$12$QKQ2d03.6SCPEQhLlaZusOJT9Q/ODqnvFHT6JCdpqvxKeXHa7KviG',
           'ROLE_USER',
           'ACTIVE'
       );