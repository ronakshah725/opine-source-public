<?
include_once "conn.php";
$qid=$_POST['qid'];
$usrnm=$_POST['usrnm'];
$SQL="SELECT * FROM pollspm WHERE qid='$qid' AND usrnm='$usrnm'";
$re=mysql_query($SQL);
$c=mysql_num_rows($re);
if($c==0)
{
	mysql_query("INSERT INTO pollspm VALUES('$qid','$usrnm')");
}
$res=mysql_fetch_array(mysql_query("SELECT COUNT(usrnm) AS c FROM pollspm WHERE qid='$qid'"));
$num=$res['c'];
if($num==5)
{
$SQL="UPDATE pollques SET sts=2 WHERE qid='$qid'";
mysql_query($SQL);
}
$arr = array('state' => 1);	
echo json_encode($arr);
?>