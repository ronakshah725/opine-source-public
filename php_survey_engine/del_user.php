<?
include_once "conn.php";
if(isset($_POST['del']))
{
	for($i=0;$i<$_POST['num'];$i++)
	{
		if(isset($_POST["ch".$i]))
		{
			echo $_POST["ch".$i];
			$SQL="DELETE FROM user_details WHERE em='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM user_reg WHERE usrnm='".$_POST["ch".$i]."'";
			mysql_query($SQL);	
			$SQL="DELETE FROM user_income WHERE usrnm='".$_POST["ch".$i]."'";
			mysql_query($SQL);			
		}
	}
}
$SQL="SELECT * FROM user_details";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);

print '<form method="post" action="del_user.php" >';
print '<input type="hidden" value='.$num.' name="num" />';
print '<table><tr><td></td><td>Email Id</td><td>User\'s Name</td></tr>';
for($i=0;$i<$num;$i++)
{
	$res[$i]=mysql_fetch_array($result);
	print '<tr><td><input type="checkbox" value="'.$res[$i]['em'].'" name="ch'.$i.'" /></td><td>'.$res[$i]['em'].'</td><td>'.$res[$i]['fname']." ".$res[$i]['lname'].'</td></tr>';
}
print '</table><input type="submit" name="del" value="Delete" ></form>';
?>