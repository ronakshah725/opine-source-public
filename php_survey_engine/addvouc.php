<?
include_once 'conn.php';
include_once 'GCmSend.php';
$vid=0;
$id="";
$vnm="";
$desc="";
$cost=0;
if(isset($_POST['sub']))
{
	$id=$_POST['id'];
	$vnm=$_POST['vnm'];
	$desc=$_POST['desc'];
	$cost=$_POST['cost'];
	echo $id.$vnm.$desc.$cost;
$SQL="UPDATE voucher SET vcrnm='$vnm', descr='$desc', amt=$cost, sts=1 WHERE id='$id'";
mysql_query($SQL);	
$ax=array('gcmid' => 5, 'id' => $id, 'vcrnm' => $vnm, 'descr' => $desc, 'amt' => $cost);
$SQL="SELECT regid from user_reg";	
$result1=mysql_query($SQL);
$ppl_cnt=mysql_num_rows($result1);
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
echo "<br />Added Successfully";
}
else
{
$SQL="SELECT MAX(vid) as 'm' FROM voucher";
$result=mysql_query($SQL);
$result=mysql_fetch_array($result);
$vid=$result['m']+1;
date_default_timezone_set('Asia/Kolkata');
$y=date("y");
$m=date("m");
$id="v".$y.$m.$vid;
$SQL="INSERT INTO voucher values($vid,'$id','$vnm','$desc',$cost,0)";
mysql_query($SQL);
?>
<form action="upldVImg.php" method="post"
enctype="multipart/form-data">
<input type="hidden" name="id1" value=<?php echo $id; ?> />
<table><tr><td>Upload Logo: </td><td>
<input type="file" name="file" id="file" /></td><td>
<input type="submit" name="upld" value="Upload" /></td></tr></table>
</form>
<form method="post" action="addvouc.php" >
<table><tr><td>Voucher Id</td><td><input type="text" name="id" readonly="readonly" value=<?php echo $id; ?> /></td></tr>
<tr><td>Voucher Name</td><td><input type="text" name="vnm" width="400" /></td></tr>
<tr><td>Description</td><td><textarea name="desc" width=400 height=400 ></textarea></td></tr>
<tr><td>Cost (Rs.)</td><td><input type="number" min='1' step='1' value='10' name="cost" /></td></tr>
<tr><td></td><td><input type="submit" name="sub" Value="Add Voucher" /></td>
</table></form>
<?
}
?>