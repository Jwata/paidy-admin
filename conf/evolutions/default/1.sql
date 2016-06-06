# Consumers schema

# --- !Ups

CREATE TABLE "consumers" (
    "entity_id" VARCHAR PRIMARY KEY,
    "status" VARCHAR NOT NULL,
    "name" VARCHAR,
    "email" VARCHAR NOT NULL,
    "phone" VARCHAR NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "test" BOOLEAN NOT NULL
);

INSERT INTO "consumers" ("entity_id", "status", "name", "email", "phone", "test") VALUES
  ('aaa', 'enabled', 'Name AAA', 'aaa@gmail.com', '11111111111', true),
  ('bbb', 'enabled', null, 'bbb@gmail.com', '22222222222', true),
  ('ccc', 'disabled', 'Name CCC', 'ccc@gmail.com', '33333333333', true);

# --- !Downs

DROP TABLE "consumers";