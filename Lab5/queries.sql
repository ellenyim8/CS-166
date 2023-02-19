--- Query 1 --- 
Select p.pid FROM Catalog c, Parts p
Where p.pid=c.pid AND c.cost < 10;

--- Query 2 ---
Select p.pname FROM Catalog c, Parts p
Where p.pid=c.pid AND c.cost < 10;

--- Query 3 ---
Select s.address From Suppliers s, Parts p, Catalog c
Where s.sid=c.sid AND p.pid=c.pid AND p.pname='Fire Hydrant Cap';

--- Query 4 ---
Select s.sname From Suppliers s, Parts p, Catalog c
Where s.sid=c.sid AND p.pid=c.pid AND p.color='Green';

--- Query 5 ---
Select s.sname, p.pname From Suppliers s, Parts p, Catalog c
Where s.sid=c.sid AND p.pid=c.pid;
