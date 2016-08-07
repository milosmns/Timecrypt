INSERT INTO message (id, view_times, lifetime, email, text, title, locked)
VALUES (
  DEFAULT,
  1,
  '2019-01-01',
  NULL,
  'A sample plain text',
  NULL,
  FALSE
);

INSERT INTO message (id, view_times, lifetime, email, text, title, locked)
VALUES (
  DEFAULT,
  6,
  '2019-02-01',
  'mymail@gmails.com',
  'This one has mail and no lock',
  NULL,
  FALSE
);

INSERT INTO message (id, view_times, lifetime, email, text, title, locked)
VALUES (
  DEFAULT,
  16,
  current_date,
  NULL,
  'This one has current_date',
  NULL,
  TRUE
);

INSERT INTO message (id, view_times, lifetime, email, text, title, locked)
VALUES (
  DEFAULT,
  26,
  '2017-01-01',
  'email@random.com',
  'This one has all',
  'The title, wow!',
  TRUE
);

INSERT INTO message (id, view_times, lifetime, email, text, title, locked)
VALUES (
  DEFAULT,
  10,
  '2019-02-01',
  NULL,
  'This one has title',
  'My title!',
  TRUE
);

SELECT *
FROM message;