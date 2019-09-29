lient-side-secure-deduplication-of-images
Client side secure deduplication of images using DICE protocol
Create  MySQL database wit folwing tables

desc imagesize;
+--------+-------------+------+-----+---------+-------+
| Field  | Type        | Null | Key | Default | Extra |
+--------+-------------+------+-----+---------+-------+
| name   | varchar(20) | YES  |     | NULL    |       |
| width  | int(100)    | YES  |     | NULL    |       |
| height | int(100)    | YES  |     | NULL    |       |
+--------+-------------+------+-----+---------+-------+



 desc user
+----------+-------------+------+-----+---------+-------+
| Field    | Type        | Null | Key | Default | Extra |
+----------+-------------+------+-----+---------+-------+
| name     | varchar(20) | YES  |     | NULL    |       |
| email    | varchar(20) | NO   | PRI | NULL    |       |
| password | varchar(20) | YES  |     | NULL    |       |
+----------+-------------+------+-----+---------+-------+


 desc server;
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| ID    | int(11)     | NO   | PRI | NULL    | auto_increment |
| email | varchar(20) | YES  |     | NULL    |                |
| fname | varchar(20) | YES  |     | NULL    |                |
| file  | blob        | YES  |     | NULL    |                |
| ckey  | blob        | YES  |     | NULL    |                |
| Id1   | int(11)     | YES  |     | NULL    |                |
+-------+-------------+------+-----+---------+----------------+

Import Project
initialize Tomcat
run index.jsp file

#Note Do necessary changes in code as per DB credentials.
