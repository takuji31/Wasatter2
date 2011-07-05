CREATE TABLE status (
  id INTEGER PRIMARY KEY,
  uuid VARCHAR(255) NOT NULL,
  service INTEGER NOT NULL,
  
  UNIQUE service_uuid (uuid, service)
);