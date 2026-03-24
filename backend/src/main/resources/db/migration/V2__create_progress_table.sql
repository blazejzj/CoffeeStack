CREATE TABLE progress (
    lesson_slug VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL,
    completed_at TIMESTAMP,
    PRIMARY KEY (lesson_slug, user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
)