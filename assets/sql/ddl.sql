CREATE TABLE status (
  id INTEGER PRIMARY KEY,
  uuid TEXT NOT NULL,
  service INTEGER NOT NULL,
  expire_at INTEGER NOT NULL,
  UNIQUE service_uuid (service, uuid),
  INDEX expire_at (expire_at)
);

CREATE TABLE image_cache (
  id INTEGER PRIMARY KEY,
  url TEXT NOT NULL,
  expire_at INTEGER NOT NULL,
  UNIQUE url (url)
  INDEX expire_at (expire_at)
);

CREATE TABLE user_cache (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  service INTEGER NOT NULL,
  image_url NOT NULL,
  expire_at INTEGER NOT NULL
  UNIQUE service_name (service, name),
  INDEX expire_at (expire_at)
);