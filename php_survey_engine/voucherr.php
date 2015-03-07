<?
include_once "conn.php";
include_once "curpts.php";
if(isset($_POST['sub']))
{
	for($i=0;$i<$_POST['num'];$i++)
	{
		$usrnm=$_POST["uid".$i];
			$vid=$_POST["vid".$i];
			$vdt=$_POST["vdt".$i];
		if(isset($_POST["a".$i]))
		{
			if($_POST["a".$i]=="accept")
			{
			$SQL="UPDATE voucherr SET mila=1 WHERE usrnm='".$usrnm."' AND id='".$vid."' AND vdt='".$vdt."'";
			mysql_query($SQL);	
				//send mail	
		}
		else
		{	
			$SQL="UPDATE voucherr SET mila=2 WHERE usrnm='".$usrnm."' AND id='".$vid."' AND vdt='".$vdt."'";			
			mysql_query($SQL);
			$SQL="SELECT regid FROM user_reg WHERE usrnm='".$usrnm."'";
			$result=mysql_query($SQL);
			$no=mysql_num_rows($result);
			for($j=0;$j<$no;$j++)
			{
				$regid[$j]=mysql_fetch_array($result);
			}
			for($j=0;$j<$no;$j++)
			{
				$aio[$j]=$regid[$j]['regid'];				
			}
			updatepts($usrnm);
			//send mail	
		}
		}	
	}
}
$SQL="SELECT * FROM voucherr WHERE mila=0";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);
print '<form method="post" action="voucherr.php" >';
print '<input type="hidden" value='.$num.' name="num" />';
print '<table><tr><td>Usrnm</td><td>Voucher id</td><td>Requested At</td><td>Accept</td><td>Reject</td></tr>';
for($i=0;$i<$num;$i++){
$res[$i]=mysql_fetch_array($result);
print '<tr><td>'.$res[$i]['usrnm'].'</td><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['vdt'].'</td><td><input type="radio" name="a'.$i.'" value="accept" /></td><td><input type="radio" name="a'.$i.'" value="reject" /></td><td><input type="hidden" name="vid'.$i.'" value="'.$res[$i]['id'].'" /></td><td><input type="hidden" name="uid'.$i.'" value="'.$res[$i]['usrnm'].'" /></td><td><input type="hidden" name="vdt'.$i.'" value="'.$res[$i]['vdt'].'" /></td></tr>';
}
print '</table><input type="submit" name="sub" value="Submit" ></form>';
?>