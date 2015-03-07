<?
include_once "conn.php";
$usrnm=$_POST['usrnm'];
$zip=$_POST['zip'];
$mar=$_POST['mar'];
$edu=$_POST['edu'];
$occ=$_POST['occ'];
$sal=$_POST['sal'];
$SQL="SELECT * FROM savedemo WHERE usrnm='$usrnm'";
$re=mysql_query($SQL);
$c=mysql_num_rows($re);
if($c==0)
{
	mysql_query("INSERT INTO savedemo VALUES('$usrnm','$zip','$mar','$edu','$occ','$sal')");
}
else
{
$SQL="UPDATE savedemo SET zip='$zip', mar='$mar', edu='$edu', occ='$occ', sal='$sal' WHERE usrnm='$usrnm'";
mysql_query($SQL);
}
$arr = array('state' => 1);	
echo json_encode($arr);
?>