<?
include_once "conn.php";
if(isset($_POST['qid']))
{
	$qid=$_POST['qid'];
}
$SQL="SELECT ops FROM pollques WHERE qid = '$qid'";
$res=mysql_query($SQL);
$ops=mysql_fetch_array($res);
$arr = array('ops' => $ops['ops']);	
echo json_encode($arr);
?>