CREATE TABLE zones (
  id serial PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE commutes (
  init_zone INTEGER NOT NULL REFERENCES zones(id),
  end_zone INTEGER NOT NULL REFERENCES zones(id),
  duration INTEGER NOT NULL,
  PRIMARY KEY (init_zone, end_zone)
);

CREATE INDEX "zone_commutes" ON commutes (init_zone);

CREATE TABLE locations (
  name VARCHAR(30) PRIMARY KEY,
  zone_id INTEGER NOT NULL REFERENCES zones(id),
  category VARCHAR(30)
);

CREATE TABLE customers (
  id serial PRIMARY KEY,
  customer_name VARCHAR(50) NOT NULL
);

CREATE TABLE vehicles (
  rego VARCHAR(10) PRIMARY KEY,
  capacity INTEGER NOT NULL
);

CREATE TABLE bookings (
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

CREATE INDEX "booking_date_index" ON bookings (date_at);
