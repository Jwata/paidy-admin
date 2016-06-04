# Consumers schema

# --- !Ups

CREATE TABLE consumers (
    entity_id text PRIMARY KEY,
    status text NOT NULL,
    name text,
    email text NOT NULL,
    phone text NOT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    test boolean NOT NULL
);

INSERT INTO consumers (entity_id, status, name, email, phone, test) VALUES
  ('aaa', 'enabled', 'Name AAA', 'aaa@gmail.com', '11111111111', true),
  ('bbb', 'enabled', null, 'bbb@gmail.com', '22222222222', true),
  ('ccc', 'disabled', 'Name CCC', 'ccc@gmail.com', '33333333333', true);

# --- !Downs

DROP TABLE consumers;