<?
include_once "conn.php";
$num=0;
if(isset($_POST['usrnm123']))
{ $usrnm=$_POST['usrnm123'];
$SQL="SELECT * FROM voucher WHERE sts=1";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);
for($i=0;$i<$num;$i++)
{
$res[$i]=mysql_fetch_array($result);
}
$at2="";
for($i=0;$i<$num;$i++)
{
	$id=$res[$i]['id'];
	$vcrnm=$res[$i]['vcrnm'];
	$descr=$res[$i]['descr'];
	$amt=$res[$i]['amt'];
	$at="";
	$at2[$i]=array("id" => $id, "vcrnm" => $vcrnm, "descr" => $descr, "amt" => $amt);		
}
}
$arr="";
$arr=array("no"=>$num,"vouchers"=>$at2);
echo str_replace("\\","",json_encode($arr));
?>