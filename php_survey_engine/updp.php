<?
include_once "conn.php";
$qid=$_POST['qid'];
$SQL="UPDATE pollques SET sts=3 WHERE qid='$qid'";
mysql_query($SQL);
$arr = array('state' => 1);	
echo json_encode($arr);
?>