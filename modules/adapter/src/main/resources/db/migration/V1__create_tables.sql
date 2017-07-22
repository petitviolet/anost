CREATE TABLE users (
    id VARCHAR(36) NOT NULL,
    name VARCHAR(48) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW(),
    UNIQUE(email),
    PRIMARY KEY (id)
);

CREATE TABLE auth_tokens(
    token VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(token),
    UNIQUE(user_id)
);
