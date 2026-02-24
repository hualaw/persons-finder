-- Populate 1,000,000 persons and corresponding locations for benchmarking
-- Usage: psql -U hua -d personfinder -f populate_1m.sql
-- NOTES:
-- 1) This script assumes the DB is empty or that you want to overwrite existing data.
-- 2) For faster loads you may drop the GIST index before running and recreate it concurrently after.
-- 3) Adjust the bounding box below to control clustering (currently ~NYC area).

-- Bounding box for generated locations (change as needed)
-- latitude range: min_lat .. max_lat
-- longitude range: min_lon .. max_lon
\set min_lat 40.5
\set max_lat 40.9
\set min_lon -74.25
\set max_lon -73.7
\set rows 1000000

-- Safety: truncate existing data and restart sequences
BEGIN;
TRUNCATE TABLE locations, persons RESTART IDENTITY CASCADE;
COMMIT;

-- Drop spatial index if exists to speed up bulk insert (will recreate later)
DROP INDEX IF EXISTS idx_locations_geom;

-- Insert persons in one bulk statement using generate_series
-- hobbies: single random hobby (sufficient for functional tests); adjust as needed
INSERT INTO persons (name, job_title, hobbies, bio)
SELECT
  ('person-' || to_char(gs, 'FM0000000'))::varchar,
  (ARRAY['Engineer','Teacher','Artist','Doctor','Lawyer','Developer','Manager'])[ (floor(random()*7)+1)::int ]::varchar,
  ARRAY[ (ARRAY['reading','travel','music','sports','gaming','cooking','hiking'])[ (floor(random()*7)+1)::int ] ]::text[],
  'A default bio.'
FROM generate_series(1, :rows) AS gs;

-- Insert locations for each person id from 1..rows using the same generate_series order
-- Note: This assumes persons table was empty and IDs start at 1 sequentially after TRUNCATE.
-- If your persons table is not empty, compute start_id and adapt the query.

-- Use a CTE to reference bounding box variables
WITH params AS (
  SELECT (:min_lat)::double precision AS min_lat, (:max_lat)::double precision AS max_lat,
         (:min_lon)::double precision AS min_lon, (:max_lon)::double precision AS max_lon
)
INSERT INTO locations (person_id, latitude, longitude, geom)
SELECT
  gs,
  (random() * (p.max_lat - p.min_lat) + p.min_lat)::double precision AS latitude,
  (random() * (p.max_lon - p.min_lon) + p.min_lon)::double precision AS longitude,
  ST_SetSRID(ST_MakePoint(
    (random() * (p.max_lon - p.min_lon) + p.min_lon)::double precision,
    (random() * (p.max_lat - p.min_lat) + p.min_lat)::double precision
  ), 4326)::geography
FROM generate_series(1, :rows) AS gs CROSS JOIN params p;

-- Recreate the spatial index using CONCURRENTLY for minimal locking (runs outside transaction)
VACUUM ANALYZE persons;
ANALYZE locations;

-- Create index concurrently (cannot run in a transaction block)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_locations_geom ON locations USING GIST (geom);

-- Ensure stats are updated
VACUUM ANALYZE locations;

-- Helpful check
SELECT count(*) AS persons_count FROM persons;
SELECT count(*) AS locations_count FROM locations;

-- Example explain for the typical nearby query (replace values):
-- EXPLAIN ANALYZE SELECT count(*) FROM locations WHERE ST_DWithin(geom, ST_SetSRID(ST_MakePoint(-74.0, 40.7),4326)::geography, 1000);

-- End of script

