<?
include_once "conn.php";
include_once 'GCmSend.php';
if(isset($_POST['qid']))
{
	$qid=$_POST['qid'];
}
if(isset($_POST['usrnm']))
{
	$usrnm=$_POST['usrnm'];
}
if(isset($_POST['res']))
{
	$res=$_POST['res'];
}
$SQL="SELECT * FROM pollres WHERE qid='$qid' AND usrnm='$usrnm'";
$resu=mysql_query($SQL);
if(mysql_num_rows($resu)==0)
{
$SQL="INSERT INTO pollres VALUES('$qid','$usrnm','$res')";
$res=mysql_query($SQL);
$resul=mysql_query("SELECT * FROM pollsub WHERE qid='$qid' AND usrnm='$usrnm'");
if(mysql_num_rows($resul)==0)
{
mysql_query("INSERT INTO pollsub VALUES('$qid','$usrnm')");
$ops=mysql_fetch_array(mysql_query("SELECT ops FROM pollques WHERE qid='$qid'"));
$no=preg_split("/~#%&/",$ops['ops']);
$num=count($no);
$rp="";
$cnt=0;
for($i=0;$i<$num;$i++)
{
	$c=mysql_fetch_array(mysql_query("SELECT COUNT(usrnm) AS c FROM pollres WHERE qid='$qid' AND res=$i"));
	$rp=$rp.",".$c['c'];	
	$cnt=$cnt+$c['c'];
}
$rp1=trim($rp,",");
$ax="";
$ax=array('gcmid' => 8, 'rp' => $rp1, 'qid' => $qid, 'nrpl' => $cnt);
$SQL="SELECT usrnm FROM pollsub WHERE qid='$qid'";
$r=mysql_query($SQL);
$ppl_cnt=mysql_num_rows($r);
for($i=0;$i<$ppl_cnt;$i++)
{
$re[$i]=mysql_fetch_array($r);	
}
$j=0;
for($i=0;$i<$ppl_cnt;$i++)
{
	$gh=$re[$i]['usrnm'];
	$SQL="SELECT regid FROM user_reg WHERE usrnm = '".$gh."'";
	$r12=mysql_query($SQL);
	$r1=mysql_fetch_array($r12);
	$arr[$j]=$r1['regid'];
	$j=$j+1;
   if($i%900==0)
   {
	  SendGCm($arr,$ax);
	   $j=0;		   
   }   	   
}
$i=$i-1;
$j=0;
$gn=false;
unset($arr);
while(!($i%900==0))
{
	$gh=$re[$i]['usrnm'];
	$SQL="SELECT regid FROM user_reg WHERE usrnm = '".$gh."'";
	$r12=mysql_query($SQL);
	$r1=mysql_fetch_array($r12);
	$gn=true;
	$arr[$j]=$r1['regid'];
	$j++;
	$i--;		
}
if($gn)
{
	SendGCm($arr,$ax);
}
$arr1 = array('state' => 1);	
}
else
{
$arr1 = array('state' => 0);	
}
}
else
{	
$arr1 = array('state' => 0);	
}
echo json_encode($arr1);
?>