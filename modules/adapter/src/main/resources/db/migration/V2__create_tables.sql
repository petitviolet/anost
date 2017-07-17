create table posts (
    id VARCHAR(36) NOT NULL,
    owner_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    file_type VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW(),
    primary key (id),
    foreign key (owner_id) references users(id)
);
