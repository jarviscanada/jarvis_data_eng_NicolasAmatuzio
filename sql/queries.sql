INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);


INSERT INTO cd.facilities (
    facid, name, membercost,
    guestcost, initialoutlay, monthlymaintenance
) VALUES
    ((SELECT COUNT(*) FROM cd.facilities), 'Spa', 20, 30, 100000, 800);