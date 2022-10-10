--liquibase formatted sql
--changeset Pavel Bodiachevskii:events-trigger splitStatements:false

CREATE OR REPLACE FUNCTION events.notify_trigger() RETURNS trigger AS $trigger$
DECLARE
    payload TEXT;
BEGIN
    -- Build the payload
    payload := row_to_json(NEW);
    -- Notify the channel
    PERFORM pg_notify('events_events', payload);
RETURN NEW;
END;
$trigger$ LANGUAGE plpgsql;

CREATE TRIGGER events_notify AFTER INSERT ON events.events
    FOR EACH ROW EXECUTE PROCEDURE events.notify_trigger();