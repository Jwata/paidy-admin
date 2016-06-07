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

INSERT INTO "consumers" ("entity_id", "status", "name", "email", "phone", "created_at", "test") VALUES
  ('aaa', 'Enabled', 'Name AAA', 'aaa@gmail.com', '11111111111', CURRENT_TIMESTAMP + random(), true),
  ('bbb', 'Enabled', null, 'bbb@gmail.com', '22222222222', CURRENT_TIMESTAMP + random(),true),
  ('ccc', 'Disabled', 'Name CCC', 'ccc@gmail.com', '33333333333', CURRENT_TIMESTAMP + random(), true);

# --- !Downs

DROP TABLE "consumers";