<?php
define('SQL_HOST','203.124.112.190');
define('SQL_USER','Appsathidb');
define('SQL_PASS','h@ySAPDWS#5Hk');
define('SQL_DB','Appsathidb');
$conn = mysql_connect(SQL_HOST, SQL_USER, SQL_PASS)
 or die('Could not connect to the database; ' . mysql_error());
mysql_select_db(SQL_DB, $conn)
 or die('Could not select database; ' . mysql_error()); 
?>