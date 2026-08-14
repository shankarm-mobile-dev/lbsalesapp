
PRAGMA foreign_keys=OFF;
----------------------------------------------------------
CREATE TABLE IF NOT EXISTS "lblogin"
(
 "USERID"                VARCHAR,
 "USERNAME"                VARCHAR,
 "Pass_word"             VARCHAR,
 "TKEY"      VARCHAR,
 "USERDES"            VARCHAR
);

----------------------------------------------------------
CREATE TABLE IF NOT EXISTS "Outstanding"
(
 "NAME"                VARCHAR,
 "PRICE"                VARCHAR,
 "FOLLOWING"             VARCHAR,
 "CODE"      VARCHAR,
 "DRILLDOWN"            VARCHAR
);

----------------------------------------------------------

CREATE TABLE IF NOT EXISTS "OrderSales"
(
 "NAME"                VARCHAR,
 "CODE"                VARCHAR,
 "TODAY"             VARCHAR,
 "LASTWK"      VARCHAR,
 "LAST5WK"            VARCHAR,
  "DRILLDOWN"                VARCHAR,
  "TODAYAVG"                VARCHAR,
  "LASTWKAVG"             VARCHAR,
  "LAST5WKAVG"      VARCHAR,
  "BRANCHNAME"            VARCHAR

);
----------------------------------------------------------
