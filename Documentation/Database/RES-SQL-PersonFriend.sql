/*
 Creating association between Person and Friendship
 */
CREATE TABLE person(
	id SERIAL NOT NULL
	CONSTRAINT person_pkey
	PRIMARY KEY,
	name VARCHAR(5)
);

CREATE UNIQUE INDEX person_id_uindex
	ON person (id)
;


CREATE TABLE friendship(
	id SERIAL NOT NULL
	CONSTRAINT friendship_pkey
	PRIMARY KEY,
	person_id INTEGER NOT NULL,
	friend_id INTEGER NOT NULL
)
;

-- Unique Index for avoiding inserts for (1, 2) and (2, 1)
CREATE UNIQUE INDEX friendship_idx01
	ON friendship ((LEAST(person_id, friend_id)), (GREATEST(person_id, friend_id)))
;

CREATE UNIQUE INDEX friendship_id_uindex
	ON friendship (id)
;


-- Getting all people relationship
SELECT p1.name, p2.name FROM friendship f
  JOIN person p1 ON (f.person_id = p1.id)
  JOIN person p2 ON (f.friend_id = p2.id)
;


-- Finding all unique combinations like (1, 2) and (2, 1)
SELECT p1.name AS person, p2.name AS friend FROM friendship f
  JOIN person p1 ON (f.person_id = p1.id)
  JOIN person p2 ON (f.friend_id = p2.id)
UNION -- No duplication, in case of wanting duplications use UNION ALL
SELECT p2.name AS person, p1.name AS friend FROM friendship f
  JOIN person p1 ON (f.person_id = p1.id)
  JOIN person p2 ON (f.friend_id = p2.id)
ORDER BY person, friend
;

-- Finding all relationships that are physically existing like (1, 2) and (2, 1)
SELECT p1.name AS person, p2.name AS friend FROM friendship f
  JOIN person p1 ON (f.person_id = p1.id)
  JOIN person p2 ON (f.friend_id = p2.id)
INTERSECT -- In case inserts of (1, 2) and (2, 1) are possible, then Intersect will show what is in common
SELECT p2.name AS person, p1.name AS friend FROM friendship f
  JOIN person p1 ON (f.person_id = p1.id)
  JOIN person p2 ON (f.friend_id = p2.id)
ORDER BY person, friend
;


-- All friends of person
SELECT
  p1.name AS person,
  ARRAY_AGG(p2.name ORDER BY p2.name) AS friends
  FROM friendship f
      JOIN person p1 ON (f.person_id = p1.id)
      JOIN person p2 ON (f.friend_id = p2.id)
  GROUP BY p1.name
;