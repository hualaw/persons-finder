-- Script to create a restricted database user for the application
-- Run this with psql connected to the 'personfinder' database as a superuser (e.g., 'hua').
-- Example: psql -U hua -d personfinder -f create_db_user.sql

-- 1. Create the new user
CREATE USER pfdbuser WITH PASSWORD 'pf_password_123';

-- 2. Grant connection permission to the database
GRANT CONNECT ON DATABASE personfinder TO pfdbuser;

-- 3. Grant specific permissions on all existing tables in the public schema
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO pfdbuser;

-- 4. Grant permissions on all sequences (needed for auto-incrementing IDs)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO pfdbuser;

-- 5. Ensure future tables and sequences automatically get these permissions
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO pfdbuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO pfdbuser;
