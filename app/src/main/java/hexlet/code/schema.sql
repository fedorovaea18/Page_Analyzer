DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
                      id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                      name VARCHAR(255) NOT NULL,
                      created_at TIMESTAMP
);
