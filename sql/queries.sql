INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);

INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (
        (
            SELECT
                COUNT(*)
            FROM
                cd.facilities
        ),
        'Spa',
        20,
        30,
        100000,
        800
    );

UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
    (facid = 1);

UPDATE
    cd.facilities
SET
    guestcost = (
        (
            SELECT
                guestcost
            FROM
                cd.facilities
            WHERE
                facid = 0
        ) * 1.1
        ),
    membercost = (
        (
            SELECT
                membercost
            FROM
                cd.facilities
            WHERE
                facid = 0
        ) * 1.1
        )
WHERE
    (facid = 1);

DELETE FROM cd.bookings *;

DELETE FROM
    cd.members
WHERE
    memid = 37;

SELECT
    facid,
    name,
    membercost,
    monthlymaintenance
FROM
    cd.facilities
WHERE
    (
        (membercost / monthlymaintenance) < (1.0 / 50.0)
        )
  and membercost > 0;

SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';

SELECT
    *
FROM
    cd.facilities
WHERE
    facid = 1
   OR facid = 5;

SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-01';

SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities;

SELECT
    bks.starttime
FROM
    cd.bookings bks
        INNER JOIN cd.members mems ON mems.memid = bks.memid
WHERE
    mems.firstname = 'David'
  AND mems.surname = 'Farrell';

SELECT
    bks.starttime AS start,
    fcs.name AS name
FROM
    cd.facilities fcs
        INNER JOIN cd.bookings bks ON fcs.facid = bks.facid
WHERE
    fcs.name LIKE '%Tennis Court%'
  AND bks.starttime >= '2012-09-21'
  AND bks.starttime < '2012-09-22'
ORDER BY
    bks.starttime;

select
    mem.firstname AS memfname,
    mem.surname AS memsname,
    recs.firstname AS recfname,
    recs.surname AS recsname
FROM
    cd.members mem
        LEFT OUTER JOIN cd.members recs ON recs.memid = mem.recommendedby
ORDER BY
    memsname,
    memfname;

SELECT
    DISTINCT mem.firstname,
             mem.surname
FROM
    cd.members mem
        INNER JOIN cd.members rec ON mem.memid = rec.recommendedby
ORDER BY
    mem.surname,
    mem.firstname;

SELECT
    DISTINCT CONCAT(mem.firstname, ' ', mem.surname) as member,
    (
         SELECT
             CONCAT(rec.firstname, ' ', rec.surname) as recommender
         FROM
             cd.members rec
         WHERE
             rec.memid = mem.recommendedby
    )
FROM
    cd.members mem
order by
    member;

select
    recommendedby,
    count(*)
FROM
    cd.members
WHERE
    recommendedby is not null
GROUP BY
    recommendedby
order by
    recommendedby;

SELECT
    DISTINCT facid,
             sum(slots) "Total Slots"
FROM
    cd.bookings
GROUP BY
    facid
order by
    facid;

SELECT
    facid,
    sum(slots) as "Total Slots"
FROM
    cd.bookings
WHERE
    starttime < '2012-10-01'
  AND starttime >= '2012-09-01'
GROUP BY
    facid
ORDER BY
    "Total Slots";

SELECT
    facid,
    EXTRACT(
            MONTH
            FROM
            starttime
    ) as month,
  SUM(slots) as "Total Slots"
FROM
    cd.bookings
WHERE
    starttime >= '2012-01-01'
  AND starttime < '2013-01-01'
GROUP BY
    facid,
    month
ORDER BY
    facid,
    month;

SELECT
    DISTINCT COUNT(DISTINCT memid) as "count"
from
    cd.bookings;

SELECT
    mem.surname,
    mem.firstname,
    mem.memid,
    min(bks.starttime) as starttime
from
    cd.bookings bks
        INNER JOIN cd.members mem on mem.memid = bks.memid
WHERE
    starttime >= '2012-09-01'
GROUP BY
    mem.surname,
    mem.firstname,
    mem.memid
ORDER BY
    mem.memid;

SELECT
    COUNT(memid) OVER (),
        firstname,
    surname
from
    cd.members;

SELECT
    ROW_NUMBER() OVER () as row_number,
        firstname,
    surname
from
    cd.members
ORDER BY
    memid;

SELECT
    DISTINCT facid,
             total
from
    (
        SELECT
            facid,
            SUM(slots) total,
            rank() over (
        order by
          sum(slots) desc
      ) rank
        from
            cd.bookings
        group by
            facid
    ) as ranked
where
    rank = 1;

SELECT
    CONCAT(surname, ', ', firstname)
FROM
    cd.members;

SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone LIKE '(%';

SELECT
    SUBSTR(mem.surname, 1, 1) as letter,
    COUNT(*) as count
FROM
    cd.members mem
GROUP BY
    letter
ORDER BY
    letter;

