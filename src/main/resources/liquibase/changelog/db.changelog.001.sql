CREATE SEQUENCE reservation.public.guest_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE TABLE reservation.public.guest (
  id                 INTEGER DEFAULT nextval('guest_id_seq') NOT NULL,
  create_date        TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date        TIMESTAMP WITH TIME ZONE                                    NULL,
  first_name         VARCHAR(200)                                                NOT NULL,
  last_name          VARCHAR(200)                                                NOT NULL,
  email              VARCHAR(200)                                                NOT NULL,
  delete_date        TIMESTAMP WITH TIME ZONE                                    NULL,
  is_deleted         BOOLEAN                                                     NOT NULL DEFAULT FALSE,

  PRIMARY KEY (id)
);


CREATE INDEX guest_first_name_idx
  ON reservation.public.guest (first_name);


CREATE INDEX guest_last_name_idx
  ON reservation.public.guest (last_name);


CREATE INDEX guest_email_idx
  ON reservation.public.guest (email);


CREATE INDEX guest_idx01
  ON reservation.public.guest (first_name, last_name);

------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE reservation.public.room_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE TYPE ROOM_STATUS AS ENUM ('UNAVAILABLE', 'AVAILABLE');


CREATE TABLE reservation.public.room (
  id                 INTEGER DEFAULT nextval('room_id_seq') NOT NULL,
  create_date        TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date        TIMESTAMP WITH TIME ZONE                                    NULL,
  title              VARCHAR(200)                                                NOT NULL,
  status             ROOM_STATUS                                                 NOT NULL DEFAULT 'AVAILABLE',
  delete_date        TIMESTAMP WITH TIME ZONE                                    NULL,
  is_deleted         BOOLEAN                                                     NOT NULL DEFAULT FALSE,

  PRIMARY KEY (id)
);


CREATE INDEX room_title_idx
  ON reservation.public.room (title);


CREATE INDEX room_status_idx
  ON reservation.public.room (status);


------------------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE reservation.public.booking_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


CREATE TYPE BOOKING_STATUS AS ENUM ('PENDING', 'ACTIVE', 'CONFIRMED', 'CANCELLED');


CREATE TABLE reservation.public.booking (
  id                 INTEGER DEFAULT nextval('booking_id_seq')                      NOT NULL,
  uuid               VARCHAR(200)                                                NOT NULL,
  create_date        TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_date        TIMESTAMP WITH TIME ZONE                                    NULL,
  guest_id           INTEGER                                                     NOT NULL,
  room_id            INTEGER                                                     NOT NULL,
  status             BOOKING_STATUS                                              NOT NULL DEFAULT 'PENDING',
  start_date         TIMESTAMP WITH TIME ZONE                                    NOT NULL,
  end_date           TIMESTAMP WITH TIME ZONE                                    NOT NULL,
  delete_date        TIMESTAMP WITH TIME ZONE                                    NULL,
  is_deleted         BOOLEAN                                                     NOT NULL DEFAULT FALSE,

  PRIMARY KEY (id)
);


CREATE INDEX booking_uuid_idx
  ON reservation.public.booking (uuid);


CREATE INDEX booking_guest_id_idx
  ON reservation.public.booking (guest_id);


CREATE INDEX booking_room_id_idx
  ON reservation.public.booking (room_id);


CREATE INDEX booking_idx01
  ON reservation.public.booking (start_date, end_date);


CREATE UNIQUE INDEX booking_idx02
  ON reservation.public.booking (uuid)
;