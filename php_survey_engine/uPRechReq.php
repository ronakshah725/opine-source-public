<?
include_once "conn.php";
include_once "getFact.php";
include_once "curpts.php";
include_once "rechreq.php";
$usrnm="";
$amt=0;
$serv="";
$ts="";
$loc="";
if(isset($_POST['usrnm']))
{
	$usrnm=$_POST['usrnm'];
}
if(isset($_POST['amt']))
{
	$amt=$_POST['amt'];
}
if(isset($_POST['serv']))
{
	$serv=$_POST['serv'];
}
if(isset($_POST['phno']))
{
	$phno=$_POST['phno'];
}
if(isset($_POST['ts']))
{
	$ts=$_POST['ts'];
}
if(isset($_POST['loc']))
{
	$loc=$_POST['loc'];
}
$tot=$amt*getFact();
if($tot<=retPts($usrnm))
{
$SQL="SELECT * FROM recharger WHERE usrnm='$usrnm' AND ts='$ts'";
$result=mysql_query($SQL);
if(mysql_num_rows($result)==0)
{
$servref=md5($usrnm.''.$amt.''.$ts.''.$phno.''.$serv);
$SQL="INSERT INTO recharger VALUES('$usrnm',$amt,'$serv','$phno','$ts',0,'','$loc','$servref')";
mysql_query($SQL);
$RechRpl = sendreq($phno, $serv, $loc, $amt, $servref);
$xml = simplexml_load_string($RechRpl);
$transRef = "";
$stat = "";
$transRef = $xml->TransactionReference . "";
$stat = $xml->Status . "";
$stat1 = substr($stat, 0, 1);
if($stat1 == '1')
$mila = 4;
elseif($stat1 == '0')
$mila = 1;
else
$mila = 5;
$SQL = "UPDATE recharger SET TransactionReference = '$transRef', mila = $mila WHERE servref = '$servref'";
mysql_query($SQL);
$arr = array('state' => 1);
}
else
{
$arr = array('state' => 1); //Already recieved recharge request
}
}
else
{
$servref=md5($usrnm.''.$amt.''.$ts.''.$phno.''.$serv);
$SQL="INSERT INTO recharger VALUES('$usrnm',$amt,'$serv','$phno','$ts',3,'','$loc','$servref')";
echo $SQL;
mysql_query($SQL);
//Cheat Mail
$arr = array('state' => 1);	
}
updatepts($usrnm);
echo json_encode($arr);
?>