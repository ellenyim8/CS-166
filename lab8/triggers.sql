
CREATE OR REPLACE LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION part_number()
RETURNS "trigger" AS 
$BODY$
BEGIN
	NEW.part_number = nextval('part_number_seq');
	RETURN NEW;
END 
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS part_trigger ON part_nyc;
CREATE TRIGGER part_trigger BEFORE INSERT on part_nyc FOR EACH ROW
EXECUTE PROCEDURE part_number();


