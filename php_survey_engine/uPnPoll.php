<?
include_once "conn.php";
include_once 'GCmSend.php';
$usrnm="";
$ques="";
$ops="";
if(isset($_POST['usrnm']))
{
	$usrnm=$_POST['usrnm'];
}
if(isset($_POST['catnm']))
{
	$catnm=$_POST['catnm'];
}
if(isset($_POST['ques']))
{
	$ques=$_POST['ques'];
}
if(isset($_POST['ops']))
{
	$ops=$_POST['ops'];
}
$ts=date("d-m-Y H:i:s");
$SQL="SELECT MAX(aid) AS m FROM pollques";
$res=mysql_query($SQL);
$max=mysql_fetch_array($res);
$aid=$max['m']+1;
$qid="q".$aid;
mysql_query("INSERT INTO pollques VALUES($aid,'$catnm','$qid','$ques','$ops','$ts',1,'$usrnm')");
mysql_query("INSERT INTO pollsub VALUES('$qid','$usrnm')");
$SQL="SELECT regid FROM user_reg WHERE usrnm = '$usrnm'";
$resu=mysql_query($SQL);
$num=mysql_num_rows($resu);
$regs="";
if($num>1)
{
	for($i=0;$i<$num;$i++)
	{
		$regs[$i]=mysql_fetch_array($resu);
	}
$no=preg_split("/~#%&/",$ops);
$nm=count($no);
$rp="";
$cnt=0;
for($i=0;$i<$nm;$i++)
{
	$c=mysql_fetch_array(mysql_query("SELECT COUNT(usrnm) AS c FROM pollres WHERE qid='$qid' AND res=$i"));
	$rp=$rp.",".$c['c'];	
	$cnt=$cnt+$c['c'];
}
	$ax=array("gcmid" => 9, "id" => $catnm, "qid" => $qid, "ques" => $ques, "nrpl" => $cnt,"ops" => $ops, "res" =>$rp);
	SendGCm($regs,$ax);
}
$arr = array('state' => 1,'qid'=>$qid);	
echo json_encode($arr);
?>