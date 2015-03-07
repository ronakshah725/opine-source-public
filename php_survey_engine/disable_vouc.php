<?
include_once "conn.php";
$SQL="SELECT * FROM voucher";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);
print '<input type="hidden" value='.$num.' name="num" />';
print '<table><tr><td>Voucher Id</td><td>Voucher Name</td><td>Enable/Disable</td></tr>';
for($i=0;$i<$num;$i++)
{
	$res[$i]=mysql_fetch_array($result);
	if($res[$i]['sts']==0)
	{
	print '<tr><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['vcrnm'].'</td><td><a href="disable_vouci.php?id='.$res[$i]['id'].'&sts=1" >Enable</a></td></tr>';
	}
	else
	{
	print '<tr><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['vcrnm'].'</td><td><a href="disable_vouci.php?id='.$res[$i]['id'].'&sts=0" >Disable</a></td></tr>';
	}
}
print '</table>';
?>