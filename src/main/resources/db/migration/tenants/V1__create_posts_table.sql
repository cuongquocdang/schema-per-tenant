CREATE TABLE posts
(
    id         BIGSERIAL,
    content    VARCHAR,
    published  BOOLEAN,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    PRIMARY KEY (id)
);