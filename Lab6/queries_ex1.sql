
-- query 1--
SELECT S.sname, COUNT(*)
FROM SUPPLIERS S, PARTS P, CATALOG C
WHERE S.sid=C.sid AND P.pid=C.pid
GROUP BY S.sid;

-- query 2--
SELECT S.sname, COUNT(*)
FROM SUPPLIERS S, PARTS P, CATALOG C
WHERE S.sid=C.sid AND P.pid=C.pid
GROUP BY S.sid
HAVING COUNT(*) >= 3;

-- query 3--
SELECT S.sname, COUNT(*)
FROM SUPPLIERS S, PARTS P, CATALOG C
WHERE S.sid=C.sid AND P.pid=C.pid AND S.sid IN
(
	SELECT S.sid
	FROM SUPPLIERS S, PARTS P, CATALOG C
	WHERE S.sid=C.sid AND P.pid=C.pid AND P.color='Green'
)
GROUP BY S.sid;

-- query 4--
SELECT S.sname, MAX(C.cost)
FROM SUPPLIERS S, PARTS P, CATALOG C
WHERE S.sid=C.sid AND P.pid=C.pid AND S.sid IN
(
	SELECT S.sid
	FROM SUPPLIERS S, PARTS P, CATALOG C
	WHERE S.sid=C.sid AND P.pid=C.pid AND P.color='Green'
	INTERSECT
	SELECT S.sid
	FROM SUPPLIERS S, PARTS P, CATALOG C
	WHERE S.sid=C.sid AND P.pid=C.pid AND P.color='Red'

)
GROUP BY S.sid;

