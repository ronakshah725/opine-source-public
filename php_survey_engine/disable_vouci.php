<?
include_once "conn.php";
include_once 'GCmSend.php';
if(isset($_GET['id']))
{
	$id=$_GET['id'];
	$sts=$_GET['sts'];
	$SQL="UPDATE voucher SET sts=".$sts." WHERE id='".$id."'";
	mysql_query($SQL);
	$SQL="SELECT regid FROM user_reg";
	$result1=mysql_query($SQL);
	$ppl_cnt=mysql_num_rows($result1);	
	if($sts==0)
	{
	$ax=array("gcmid" => 7, "id" => $id);
	}
	else
	{
	$res=mysql_fetch_array(mysql_query("SELECT * FROM voucher WHERE id='$id'"));
	$ax=array('gcmid' => 5, 'id' => $id, 'vcrnm' => $res['vcrnm'], 'descr' => $res['descr'], 'amt' => $res['amt']);
	}
	for($i=0;$i<$ppl_cnt;$i++)
	{
	$result[$i]=mysql_fetch_array($result1);	
	}
	$j=0;
	for($i=0;$i<$ppl_cnt;$i++)
	{
	   $arr[$j]=$result[$i]['regid'];
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
		$gn=true;
		$arr[$j]=$result[$i]['regid'];
		$j++;
		$i--;		
	}
	if($gn)
	{
		SendGCm($arr,$ax);
	}	
	header("Location:disable_vouc.php");
}
?>