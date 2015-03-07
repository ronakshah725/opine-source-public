<?
include_once "conn.php";
include_once "getFact.php";
include_once "curpts.php";
$usrnm="";
$s="";
$ts="";
if(isset($_POST['usrnm']))
{
	$usrnm=$_POST['usrnm'];
}
if(isset($_POST['s']))
{
	$s=$_POST['s'];
	$sa=explode(",",$s);
}
if(isset($_POST['ts']))
{
	$ts=$_POST['ts'];
}
$tot=0;
for($i=0;$i<count($sa);$i++)
{
	$x=$sa[$i];
	$SQL="SELECT amt FROM voucher WHERE id='$x'";
	$result=mysql_query($SQL);
	$ar=mysql_fetch_array($result);
	$tot=$tot+$ar['amt'];
}
$tot=$tot*getFact();
if($tot<=retPts($usrnm))
{
	$j=0;
	for($i=0;$i<count($sa);$i++)
	{
		$x=$sa[$i];
		$SQL="SELECT * FROM voucherr WHERE usrnm='$usrnm' AND id='$x' AND vdt='$ts'";
		$result=mysql_query($SQL);
		if(mysql_num_rows($result)==0)
		{
			$SQL="SELECT amt FROM voucher WHERE id='$x'";
			$result1=mysql_query($SQL);
			$result2=mysql_fetch_array($result1);
			$cost=$result2['amt'];
			$SQL="INSERT INTO voucherr VALUES('$usrnm','$x','$ts',0,$cost)";
			mysql_query($SQL);
			$reg[$j]=$x;
			$j++;
			$arr = array('state' => 1);
		}
		else{
			$arr = array('state' => 1);//state0 already requested voucher
		}
	}
	//reg mail for reg['j'];
}
else{
	for($i=0;$i<count($sa);$i++)
	{
		$x=$sa[$i];
		$SQL="SELECT amt FROM voucher WHERE id='$x'";
		$result2=mysql_fetch_array(mysql_query($SQL));
		$cost=$result2['amt'];
		$SQL="INSERT INTO voucherr VALUES('$usrnm','$x','$ts',3,$cost)";
		mysql_query($SQL);
	}
	//cheat mail	
	$arr = array('state' => 1);//state3 cheating
}
updatepts($usrnm);
echo json_encode($arr);
?>