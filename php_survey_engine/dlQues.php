<?
include_once "conn.php";
$num=0;
if(isset($_POST['usrnm']))
{
	$usrnm=$_POST['usrnm'];
}
if(isset($_POST['catnm']))
{
	$catnm=$_POST['catnm'];
}
if(isset($_POST['mult']))
{
	$mult=(intval($_POST['mult'])-1)*15;
}
/*if(isset($_POST['ihave']))
{
	$ihav=trim($_POST['ihave'],"\"");
	$ex=explode(",",$ihav);
	$ihae="";
	for($i=0;$i<count($ex);$i++)
	{
		$ihae=$ihae.",'".$ex[$i]."'";
	}
	$ihave=trim($ihae,",");
}*/
$SQL="SELECT * FROM pollques WHERE id = '$catnm' AND sts=1 AND qid NOT IN (SELECT qid FROM pollsub WHERE usrnm = '$usrnm') ORDER BY aid DESC LIMIT $mult, 15";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);
if($num==0)
$nomore=1;
else
$nomore=0;
for($i=0;$i<$num;$i++)
{
$res[$i]=mysql_fetch_array($result);
}
$at2="";
for($i=0;$i<$num;$i++)
{
	$id=$res[$i]['id'];
	$qid=$res[$i]['qid'];
	$ques=$res[$i]['ques'];
	$SQL="SELECT COUNT(usrnm) as c FROM pollres WHERE qid='$qid'";
	$resp=mysql_query($SQL);
	$c=mysql_fetch_array($resp);
	$at="";
	$at2[$i]=array("id" => $id, "qid" => $qid, "ques" => $ques, "nrpl" => $c['c']);	}
$arr="";
$arr=array("quesall"=>$at2,"nomore" => $nomore);
echo str_replace("\\","",json_encode($arr));
?>