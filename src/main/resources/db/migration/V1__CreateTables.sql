CREATE TABLE IF NOT EXISTS zones (
  id serial PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS commutes (
  init_zone INTEGER NOT NULL REFERENCES zones(id),
  init_zone_name VARCHAR NOT NULL,
  end_zone INTEGER NOT NULL REFERENCES zones(id),
  end_zone_name VARCHAR NOT NULL,
  duration INTEGER NOT NULL,
  PRIMARY KEY (init_zone, end_zone)
);

CREATE INDEX IF NOT EXISTS "zone_commutes" ON commutes (init_zone);

CREATE TABLE IF NOT EXISTS locations (
  name VARCHAR(30) PRIMARY KEY,
  zone_id INTEGER NOT NULL REFERENCES zones(id),
  category VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS customers (
  id serial PRIMARY KEY,
  customer_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
  rego VARCHAR(10) PRIMARY KEY,
  capacity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
  id serial PRIMARY KEY,
  customer INTEGER NOT NULL REFERENCES customers(id),
  passengers INTEGER NOT NULL,
  pickup_location VARCHAR NOT NULL REFERENCES locations(name),
  dropoff_location VARCHAR NOT NULL REFERENCES locations(name),
  duration INTEGER NOT NULL,
  date_at DATE NOT NULL,
  start_at TIME NOT NULL,
  vehicle VARCHAR NOT NULL REFERENCES vehicles(rego)
);

CREATE INDEX IF NOT EXISTS "booking_date_index" ON bookings (date_at);
