--query 1--
SELECT COUNT(*)
FROM part_nyc n
WHERE n.on_hand > 70;

--query 2--
SELECT SUM 
( 
 (SELECT SUM(n.on_hand) FROM part_nyc n, color c
WHERE n.color=c.color_id AND c.color_name='Red')
+ 
(SELECT SUM(f.on_hand) FROM part_sfo f, color c
WHERE f.color=c.color_id AND c.color_name='Red')
) AS total_parts_red_nyc_sfo;

--query 3--
SELECT s.supplier_id, s.supplier_name FROM supplier s 
WHERE ( SELECT SUM(n.on_hand) FROM part_nyc n WHERE n.supplier=s.supplier_id) > 
( SELECT SUM(f.on_hand) FROM part_sfo f WHERE f.supplier=s.supplier_id) 
ORDER BY s.supplier_id;


--query 4--
SELECT DISTINCT s.supplier_id, s.supplier_name FROM supplier s, part_nyc n
WHERE s.supplier_id=n.supplier AND n.part_number IN 
( SELECT n2.part_number FROM supplier s, part_nyc n2
WHERE s.supplier_id=n2.supplier 
EXCEPT 
SELECT sf.part_number FROM supplier s, part_sfo sf
WHERE s.supplier_id=sf.supplier 
) ORDER BY s.supplier_id;


--query 5--
UPDATE part_nyc
SET on_hand=on_hand-10
WHERE on_hand >= 10;

--query 6-- 
DELETE FROM part_nyc
WHERE on_hand < 30;


