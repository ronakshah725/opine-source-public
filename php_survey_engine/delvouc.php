<?
include_once "conn.php";
include_once 'GCmSend.php';
if(isset($_POST['del']))
{
	for($i=0;$i<$_POST['num'];$i++)
	{
		if(isset($_POST["ch".$i]))
		{
			echo $_POST["ch".$i];
			$SQL="DELETE FROM voucher WHERE id='".$_POST["ch".$i]."'";		
			mysql_query($SQL);
			unlink('VoucImages/'.$_POST["ch".$i].'.jpg');			
		}
	}
}
$SQL="SELECT * FROM voucher";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);

print '<form method="post" action="delvouc.php" >';
print '<input type="hidden" value='.$num.' name="num" />';
print '<table><tr><td></td><td>Voucher Id</td><td>Voucher Name</td></tr>';
for($i=0;$i<$num;$i++)
{
	$res[$i]=mysql_fetch_array($result);
	print '<tr><td><input type="checkbox" value="'.$res[$i]['id'].'" name="ch'.$i.'" /></td><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['vcrnm'].'</td></tr>';
}
print '</table><input type="submit" name="del" value="Delete" ></form>';
?>