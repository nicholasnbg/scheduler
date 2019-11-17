CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION random_between(low INT ,high INT)
   RETURNS INT AS
$$
BEGIN
   RETURN floor(random()* (high-low + 1) + low);
END;
$$ language 'plpgsql' STRICT;

INSERT INTO customers (id, customer_name)
VALUES
(uuid_generate_v4(), 'Customer A'),
(uuid_generate_v4(), 'Customer B'),
(uuid_generate_v4(), 'Customer C');

INSERT INTO vehicles (rego, capacity)
VALUES
('ABC-123', 6),
('XYZ-987', 5);

INSERT INTO zones (id, name)
VALUES
(uuid_generate_v4(), 'Zone A'),
(uuid_generate_v4(), 'Zone B'),
(uuid_generate_v4(), 'Zone C');

DO $$
declare zone_data record;

begin
    FOR zone_data in (select * from zones) loop
        insert into locations (id, name, zone_id)
        values
        (uuid_generate_v4(), 'location_a_' || zone_data.name, zone_data.id),
        (uuid_generate_v4(), 'location_b_' || zone_data.name, zone_data.id);
    end loop;
END$$;

DO $$
declare commute_data record;

begin
    for commute_data in
    (select
     a.id as start_id,
     b.id as end_id
     from zones a
     inner join zones b
     on b.id != a.id ) loop

        insert into commutes (init_zone, end_zone, duration)
        values
          (commute_data.start_id,
          commute_data.end_id,
          30);
    end loop;
END$$;

