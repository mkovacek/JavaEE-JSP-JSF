CREATE TABLE mkovacek_meteo (
  id integer NOT NULL 
                PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
                (START WITH 1, INCREMENT BY 1),
  idAdresa integer NOT NULL,					
  izlazakSunca timestamp NOT NULL DEFAULT '',
  zalazakSunca timestamp NOT NULL DEFAULT '',
  temperatura varchar(255) NOT NULL DEFAULT '',
  naoblaka varchar(255) NOT NULL DEFAULT '',
  tlakZraka varchar(255) NOT NULL DEFAULT '',
  datum_kreiranja timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);