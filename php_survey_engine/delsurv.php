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
			$SQL="DELETE FROM tilestable WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM questions WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM options_checkbox WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM options_radio WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM response_yn WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM response_rating WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			$SQL="DELETE FROM response_feedback WHERE id='".$_POST["ch".$i]."'";
			mysql_query($SQL);
			unlink('SurvImages/'.$_POST["ch".$i].'.jpg');
		}
	}
}
$SQL="SELECT * FROM tilestable";
$result=mysql_query($SQL);
$num=mysql_num_rows($result);

print '<form method="post" action="delsurv.php" >';
print '<input type="hidden" value='.$num.' name="num" />';
print '<table><tr><td></td><td>Survey Id</td><td>Company Name</td></tr>';
for($i=0;$i<$num;$i++)
{
	$res[$i]=mysql_fetch_array($result);
	print '<tr><td><input type="checkbox" value="'.$res[$i]['id'].'" name="ch'.$i.'" /></td><td>'.$res[$i]['id'].'</td><td>'.$res[$i]['compNm'].'</td></tr>';
}
print '</table><input type="submit" name="del" value="Delete" ></form>';
?>