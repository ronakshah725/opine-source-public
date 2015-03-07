<?
include_once "conn.php";
$usrnm="";
$ax4="";
$fname="";
$lname="";
$salary="";
$bday="";
$gen="";
if(isset($_POST['usrnm123']))
$usrnm=$_POST['usrnm123'];
$SQL="SELECT * FROM user_details WHERE em='$usrnm'";
$result=mysql_query($SQL);
if(mysql_num_rows($result)==0)
{
$ax4=array('set' => '0');
}
else
{
$SQL="SELECT * FROM user_details WHERE em='$usrnm'";
$result=mysql_query($SQL);
$arr=mysql_fetch_array($result);
$fname=$arr['fname'];
$lname=$arr['lname'];
$bday=$arr['bday'];
$gen=$arr['gen'];
$salary=$arr['sal'];
/*$SQL="SELECT * FROM savedemo WHERE usrnm='$usrnm'";
$result=mysql_query($SQL);
$arr=mysql_fetch_array($result);
$salary=$arr['sal'];*/
$ax4=array('set' => '1', 'fname' => $fname, 'lname' => $lname, 'bday' => $bday, 'gen' => $gen, 'salary' => $salary);
}
echo str_replace("\\","",json_encode($ax4));
?>