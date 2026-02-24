-- Script to setup the personfinder database
-- Run this with psql connected to the 'postgres' database.
-- Example: psql -U hua -d postgres -f data.sql

-- DROP DATABASE IF EXISTS personfinder;
-- CREATE DATABASE personfinder;

\c personfinder

CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    job_title VARCHAR(255),
    hobbies TEXT[],
    bio TEXT
);

CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    person_id BIGINT NOT NULL UNIQUE,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    geom GEOGRAPHY(POINT, 4326),
    CONSTRAINT fk_person
        FOREIGN KEY(person_id)
        REFERENCES persons(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_locations_geom ON locations USING GIST (geom);

