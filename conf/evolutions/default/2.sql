# Payments schema

# --- !Ups

CREATE TABLE "payments" (
    "entity_id" VARCHAR PRIMARY KEY,
    "merchant_id" VARCHAR NOT NULL,
    "consumer_id" VARCHAR NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "amount" DECIMAL NOT NULL,
    "test" BOOLEAN NOT NULL
);

CREATE INDEX ON "payments" ("consumer_id");

INSERT INTO "payments" ("entity_id", "merchant_id", "consumer_id", "amount", "test") VALUES
  (hash('SHA256', STRINGTOUTF8(random()::text), 1), hash('SHA256', STRINGTOUTF8(random()::text), 1), 'aaa', 10, true),
  (hash('SHA256', STRINGTOUTF8(random()::text), 1), hash('SHA256', STRINGTOUTF8(random()::text), 1), 'aaa', 10.5, true),
  (hash('SHA256', STRINGTOUTF8(random()::text), 1), hash('SHA256', STRINGTOUTF8(random()::text), 1), 'bbb', 20, true);

# --- !Downs

DROP TABLE "payments";