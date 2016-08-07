-- Create/define pseudo_encrypt functions for BIGINT and INT
-- Based on Feistel Networks and Block Cypher design: https://www.schneier.com/academic/archives/1996/01/unbalanced_feistel_n.html
CREATE OR REPLACE FUNCTION "pseudo_encrypt_bigint"("VALUE" BIGINT)
  RETURNS BIGINT IMMUTABLE STRICT AS $function_pseudo_encrypt$
DECLARE
  l1 BIGINT;
  l2 BIGINT;
  r1 BIGINT;
  r2 BIGINT;
  i  INT :=0;
BEGIN
  l1:= ("VALUE" >> 32) & 4294967295 :: BIGINT;
  r1:= "VALUE" & 4294967295;
  WHILE i < 3 LOOP
    l2 := r1;
    r2 := l1 # ((((1366.0 * r1 + 150889) % 714025) / 714025.0) * 32767 * 32767) :: BIGINT;
    r1 := l2;
    l1 := r2;
    i := i + 1;
  END LOOP;
  RETURN ((l1 :: BIGINT << 32) + r1);
END;
$function_pseudo_encrypt$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "pseudo_encrypt_int"("VALUE" INT)
  RETURNS INT IMMUTABLE STRICT AS $function_pseudo_encrypt$
DECLARE
  l1 INT;
  l2 INT;
  r1 INT;
  r2 INT;
  i  INT :=0;
BEGIN
  l1:= ("VALUE" >> 16) & 65535;
  r1:= "VALUE" & 65535;
  WHILE i < 3 LOOP
    l2 := r1;
    r2 := l1 # ((((1366.0 * r1 + 150889) % 714025) / 714025.0) * 32767) :: INT;
    r1 := l2;
    l1 := r2;
    i := i + 1;
  END LOOP;
  RETURN ((l1 :: INT << 16) + r1);
END;
$function_pseudo_encrypt$ LANGUAGE plpgsql;

-- Create a sequence for generating the input to pseudo_encrypt() functions
DROP SEQUENCE IF EXISTS id_sequence;
CREATE SEQUENCE id_sequence MAXVALUE 2147483647;

-- Functions that increment the sequence above and generate random integers
CREATE OR REPLACE FUNCTION random_id_bigint()
  RETURNS BIGINT AS $$
SELECT pseudo_encrypt_bigint(nextval('id_sequence') :: BIGINT)
$$ LANGUAGE SQL;

CREATE OR REPLACE FUNCTION random_id_int()
  RETURNS INT AS $$
SELECT pseudo_encrypt_int(nextval('id_sequence') :: INT)
$$ LANGUAGE SQL;

-- And finally, actually create the table
DROP TABLE IF EXISTS message;
CREATE TABLE message (
  id         INT DEFAULT random_id_int() NOT NULL,
  view_times INT                         NOT NULL,
  lifetime   DATE        DEFAULT NULL,
  email      VARCHAR(80) DEFAULT NULL,
  text       VARCHAR(2500)               NOT NULL,
  title      VARCHAR(30) DEFAULT NULL,
  locked     BOOL        DEFAULT FALSE,
  PRIMARY KEY (id)
);












