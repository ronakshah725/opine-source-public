<?
include_once "conn.php";
include_once 'GCmSend.php';
include_once "retPts.php";
function regids($usrnm)
{
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
	return $aio;
}
function givPtsGCM($usrnm)
{
	return array("gcmid" => 2, "pts" => retPts($usrnm));
}
function updatepts($usrnm)
{
	SendGCm(regids($usrnm),givPtsGCM($usrnm));
}
?>