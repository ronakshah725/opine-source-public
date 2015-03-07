<?
include_once 'conn.php';
include_once 'getFact.php';
function retPts($usrnm){
	$SQL="SELECT SUM(pts) AS 'tot' FROM user_income WHERE sts = 1 GROUP BY usrnm HAVING usrnm='$usrnm'";
	$result=mysql_query($SQL);
	$arr1=mysql_fetch_array($result);
	$pts=$arr1['tot'];
	$SQL="SELECT SUM(amt) AS 'tot' FROM voucherr WHERE mila < 2 GROUP BY usrnm HAVING usrnm='$usrnm'";
	$result=mysql_query($SQL);
	$arr2=mysql_fetch_array($result);
	$pts1=$arr2['tot'];
	$SQL="SELECT SUM(amt) AS 'tot' FROM recharger WHERE mila < 2 GROUP BY usrnm HAVING usrnm='$usrnm'";
	$result=mysql_query($SQL);
	$arr3=mysql_fetch_array($result);
	$pts2=$arr3['tot'];
	$pts3=($pts1+$pts2)*getFact();
	$pts=$pts-$pts3;
	return $pts;
}
?>