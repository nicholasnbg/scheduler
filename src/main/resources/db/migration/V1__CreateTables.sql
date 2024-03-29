CREATE TABLE IF NOT EXISTS zones (
  id UUID PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS commutes (
  init_zone UUID NOT NULL REFERENCES zones(id),
  end_zone UUID NOT NULL REFERENCES zones(id),
  duration INTEGER NOT NULL,
  PRIMARY KEY (init_zone, end_zone)
);

CREATE INDEX IF NOT EXISTS "zone_commutes" ON commutes (init_zone);

CREATE TABLE IF NOT EXISTS locations (
  id UUID PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  zone_id UUID NOT NULL REFERENCES zones(id),
  category VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS customers (
  id UUID PRIMARY KEY,
  customer_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicles (
  rego VARCHAR(10) PRIMARY KEY,
  capacity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
  id UUID PRIMARY KEY,
  customer UUID NOT NULL REFERENCES customers(id),
  passengers INTEGER NOT NULL,
  pickup_location UUID NOT NULL REFERENCES locations(id),
  dropoff_location UUID NOT NULL REFERENCES locations(id),
  duration INTEGER NOT NULL,
  date_at DATE NOT NULL,
  start_at TIME NOT NULL,
  vehicle VARCHAR NOT NULL REFERENCES vehicles(rego)
);

CREATE INDEX IF NOT EXISTS "booking_date_index" ON bookings (date_at);
