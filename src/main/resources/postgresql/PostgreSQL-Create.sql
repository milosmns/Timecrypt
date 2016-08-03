-- ------------------------------------------------------------
-- All Timecrypt messages
-- ------------------------------------------------------------

CREATE TABLE message (
  id SERIAL  NOT NULL ,
  view_times INT   NOT NULL ,
  lifetime DATE  DEFAULT NULL NOT NULL ,
  email VARCHAR(80)  DEFAULT NULL  ,
  text VARCHAR(2500)   NOT NULL ,
  title VARCHAR(30)    ,
  locked BOOL  DEFAULT FALSE NOT NULL   ,
PRIMARY KEY(id));












