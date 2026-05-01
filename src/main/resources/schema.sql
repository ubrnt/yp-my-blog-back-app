CREATE TABLE IF NOT EXISTS posts (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    text        TEXT NOT NULL,
    image       BYTEA,
    likes_count INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS post_tags (
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag     VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id      BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    text    TEXT NOT NULL
);
