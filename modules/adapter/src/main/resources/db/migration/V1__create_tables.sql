create table users (
    id VARCHAR(36) NOT NULL,
    name VARCHAR(48) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW(),
    primary key (id)
);
