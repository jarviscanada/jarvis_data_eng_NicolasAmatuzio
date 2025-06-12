-- Show Table Schema
\d+ retail;

-- Show First 10 rows 
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) FROM retail;

-- Number of Client (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) FROM retail;

-- Invoice range
SELECT MAX(invoice_date) AS "Max", MIN(invoice_date) AS "Min" FROM retail;

-- Unique stock codes
SELECT COUNT(DISTINCT stock_code) FROM retail;

-- Avg invoice amount
SELECT AVG(quantity) FROM retail
GROUP BY quantity
HAVING (quantity > 0);

SELECT AVG(invoice_total) AS avg_invoice_amount
FROM (
         SELECT invoice_no, SUM(quantity * unit_price) AS invoice_total
         FROM retail
         GROUP BY invoice_no
         HAVING SUM(quantity * unit_price) > 0
) AS valid_invoices;

-- Total Revenue
SELECT SUM(unit_price * quantity) FROM retail;

-- Total Revenue by YYYYMM
SELECT DISTINCT "yyyy" AS "yyyymm", "sum"
FROM (
        SELECT CAST(EXTRACT(YEAR FROM invoice_date) * 100 + (EXTRACT(MONTH FROM invoice_date)) AS int) AS "yyyy", SUM(unit_price * quantity) AS "sum"
        FROM retail
        GROUP BY "yyyy"
        ORDER BY "yyyy"
) AS totals
GROUP BY "yyyymm", "sum";


