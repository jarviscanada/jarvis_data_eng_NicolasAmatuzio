# Introduction
This project is primarily for getting familiar with SQL concepts and workflows. 
All table data was acquired from [pgexercises](https://pgexercises.com/).
Testing was done locally through a docker instance running psql and also through the website.

# Table Structure

### cd.members
| Column Name       | Data Type     | Constraint|
| ---               | ---           |---------- |
| `memid`           | `INTEGER`(PK) | NOT NULL  |
| `surname`         | `VARCHAR(200)`| NOT NULL  |
| `firstname`       | `VARCHAR(200)`| NOT NULL  |
| `address`         | `VARCHAR(300)`| NOT NULL  |
| `zipcode`         | `INTEGER`     | NOT NULL  |
| `telephone`       | `VARCHAR(20)` | NOT NULL  |
| `recommendedby`   | `INTEGER`     | NOT NULL  |
| `joindate`        | `TIMESTAMP`   | NOT NULL  |

### cd.facilities
| Column Name           | Data Type     | Constraint|
| ---                   | ---           |---------- |
| `facid`               | `INTEGER`(PK) | NOT NULL  |
| `"name"`              | `VARCHAR(200)`| NOT NULL  |
| `membercost`          | `VARCHAR(200)`| NOT NULL  |
| `guescost`            | `VARCHAR(300)`| NOT NULL  |
| `inistialoutlay`      | `INTEGER`     | NOT NULL  |
| `monthlymaintenance`  | `VARCHAR(20)` | NOT NULL  |

### cd.bookings
| Column Name   | Data Type     | Constraint|
| ---           | ---           |---------- |
| `bookid`      | `INTEGER`(PK) | NOT NULL  |
| `facid`       | `INTEGER`(FK) | NOT NULL  |
| `memid`       | `INTEGER`(FK) | NOT NULL  |
| `starttime`   | `INTEGER`     | NOT NULL  |
| `slots`       | `INTEGER`     | NOT NULL  |

# SQL Queries

###### Table Setup (DDL)

```sql
CREATE TABLE IF NOT EXISTS cd.members
(
    memid               INTEGER NOT NULL,
    surname             VARCHAR(200) NOT NULL,
    firstname           VARCHAR(200) NOT NULL,
    address             VARCHAR(300) NOT NULL,
    zipcode             INTEGER NOT NULL,
    telephone           VARCHAR(20) NOT NULL,
    recommendedby       INTEGER NOT NULL,
    joindate            TIMESTAMP NOT NULL,
    CONSTRAINT members_pk PRIMARY KEY (memid),
    CONSTRAINT fk_members_recommendedby FOREIGN KEY (recommendedby) REFERENCES "cd.members"(memid) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS cd.facilities
(
    facid               INTEGER NOT NULL,
    "name"              VARCHAR(100) NOT NULL,
    membercost          NUMERIC NOT NULL,
    guestcost           NUMERIC NOT NULL,
    initialoutlay       NUMERIC NOT NULL,
    monthlymaintenance  NUMERIC NOT NULL,
    CONSTRAINT facilities_pk PRIMARY KEY (facid)
);

CREATE TABLE IF NOT EXISTS cd.bookings
(
    bookid              INTEGER NOT NULL,
    facid               INTEGER NOT NULL,
    memid               INTEGER NOT NULL,
    starttime           TIMESTAMP NOT NULL,
    slots               INTEGER NOT NULL,
    CONSTRAINT bookings_pk PRIMARY KEY (bookid),
    CONSTRAINT fk_bookings_facid FOREIGN KEY (facid)
        REFERENCES cd.facilities(facid),
    CONSTRAINT fk_bookings_memid FOREIGN KEY (memid)
        REFERENCES cd.members(memid)
);
```

##### Insert a new facility into the cd.facilities table

```sql
INSERT INTO cd.facilities (
facid, name, membercost, guestcost,
initialoutlay, monthlymaintenance
)
VALUES
(9, 'Spa', 20, 30, 100000, 800);
```

##### Insert a new facility into the cd.facilities table with a dynamic id

```sql
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
```

##### Update facility with correct information

```sql
UPDATE 
  cd.facilities 
SET 
  initialoutlay = 10000 
WHERE 
  (facid = 1);
```

##### Update price of facility dynamically

```sql
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
  (facid = 1)
```

##### Delete all bookings from cd.bookings

```sql
DELETE FROM cd.bookings *
```

##### Delete a specific member fromm cd.members

```sql
DELETE FROM 
  cd.members 
WHERE 
  memid = 37
```

##### Select all facilities that charge a fee that is 1/50th of the monthly maintenance cost

```sql
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
  and membercost > 0
```

##### List all facilities with the word 'Tennis' in the name

```sql
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  name LIKE '%Tennis%';
```

##### Retrieve the details of facilities with the ID 1 and 5

```sql
SELECT 
  * 
FROM 
  cd.facilities 
WHERE 
  facid = 1 
  OR facid = 5
```

##### Produce a list of members who joined after the start of September 2012

```sql
SELECT 
  memid, 
  surname, 
  firstname, 
  joindate 
FROM 
  cd.members 
WHERE 
  joindate >= '2012-09-01'
```

##### Return a combined list of all surnames and facility names

```sql
SELECT 
  surname 
FROM 
  cd.members 
UNION 
SELECT 
  name 
FROM 
  cd.facilities
```

##### Produce a list of the start times for bookings by a specific member

```sql
SELECT 
  bks.starttime 
FROM 
  cd.bookings bks 
  INNER JOIN cd.members mems ON mems.memid = bks.memid 
WHERE 
  mems.firstname = 'David' 
  AND mems.surname = 'Farrell';
```

##### Produce a list of all start times of bookings for tennis courts on '2012-09-21'

```sql
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
```

##### Produce a list of all members including those who recommeneded them

```sql
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
```

##### Ouput all members who have been recommended by another member

```sql
SELECT 
  DISTINCT mem.firstname, 
  mem.surname 
FROM 
  cd.members mem 
  INNER JOIN cd.members rec ON mem.memid = rec.recommendedby 
ORDER BY 
  mem.surname, 
  mem.firstname
```

##### Ouput all members including whoever recommended them without using joins

```sql
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
  member
```

##### Produce a count of the recommendations each member has made

```sql
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
  recommendedby
```

##### Produce a list of the total number of slots booked per facility

```sql
SELECT 
  DISTINCT facid, 
  sum(slots) "Total Slots" 
FROM 
  cd.bookings 
GROUP BY 
  facid 
order by 
  facid
```

##### Produce a list of the total number of slots booked in September 2012

```sql
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
  "Total Slots"
```

##### Produce a list of the total number of slots booker per facility per month in the year of 2012

```sql
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
  month
```

##### Find the total number of members who have mamde at least one booking

```sql
SELECT 
  DISTINCT COUNT(DISTINCT memid) as "count" 
from 
  cd.bookings
```

##### Produce a list of each member name, id, and their first booking after September 1st 2012

```sql
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
  mem.memid
```

###### Produce a list of member names, with each row containing count

```sql
SELECT 
  COUNT(memid) OVER (), 
  firstname, 
  surname 
from 
  cd.members
```

###### Produce a monotonically increasing numbered list of members

```sql
SELECT 
  ROW_NUMBER() OVER () as row_number, 
  firstname, 
  surname 
from 
  cd.members 
ORDER BY 
  memid
```

###### Output the facility id that has the highest number of slots booked

```sql
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
  rank = 1
```

##### Output the names of all mmembers formatted as 'Surname, Firstname'

```sql
SELECT 
  CONCAT(surname, ', ', firstname) 
FROM 
  cd.members
```

##### Find member's telephone numbers that contain parentheses

```sql
SELECT 
  memid, 
  telephone 
FROM 
  cd.members 
WHERE 
  telephone LIKE '(%'
```

##### Produce a count of how many members you have whose surnamme starts with each letter of the alphabet

```sql
SELECT 
  SUBSTR(mem.surname, 1, 1) as letter, 
  COUNT(*) as count 
FROM 
  cd.members mem 
GROUP BY 
  letter 
ORDER BY 
  letter
```
