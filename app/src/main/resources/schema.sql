DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
                      id BIGINT GENERATED ALWAYS AS IDENTITY,
                      name VARCHAR(255),
                      created_at TIMESTAMP
);