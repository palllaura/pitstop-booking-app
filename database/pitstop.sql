DROP DATABASE pitstop;

CREATE DATABASE pitstop;

create table Locations
(
    id        serial primary key,
    name      text unique  not null,
    api_address varchar(255),
    api_version integer,
    address varchar(255),
    supports_cars   boolean,
    supports_trucks boolean
);

INSERT INTO Locations (name, api_address, api_version, address, supports_cars, supports_trucks)
VALUES
('Manchester', 'http://localhost:9004/api/v2', 2, '14 Bury New Rd, Manchester', TRUE, TRUE),
('London', 'http://localhost:9003/api/v1', 1, '1A Gunton Rd, London', TRUE, FALSE)