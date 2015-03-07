<?
include_once "conn.php";
$SQL="SELECT * FROM tilestable";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);
print '<input type="hidden" value='.$num.' name="num" />';
print '<table><tr><td>Survey Id</td><td>Company Name</td><td>Enable/Disable</td></tr>';
for($i=0;$i<$num;$i++)
{
	$res[$i]=mysql_fetch_array($result);
	if($res[$i]['sts']==0)
	{
	print '<tr><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['compNm'].'</td><td><a href="disable_survi.php?id='.$res[$i]['id'].'&sts=-1" >Disable</a></td></tr>';
	}
	else
	{
	print '<tr><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['compNm'].'</td><td><a href="disable_survi.php?id='.$res[$i]['id'].'&sts=0" >Enable</a></td></tr>';
	}
}
print '</table>';
?>