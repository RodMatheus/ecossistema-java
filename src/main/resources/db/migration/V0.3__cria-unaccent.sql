SET search_path TO public;
CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE OR REPLACE FUNCTION public.lunaccent(text)
    RETURNS text AS
$func$
SELECT unaccent('unaccent', lower($1))  -- schema-qualify function and dictionary
$func$  LANGUAGE sql IMMUTABLE;

CREATE OR REPLACE FUNCTION comum.lunaccent(text)
    RETURNS text AS
$func$
SELECT unaccent('unaccent', lower($1))  -- schema-qualify function and dictionary
$func$  LANGUAGE sql IMMUTABLE;