<?
include_once "conn.php";
include_once "mainGCM.php";
function SendGCm($SendToArr, $SendMsgArr)
{
	$result = mainGCM($SendToArr, $SendMsgArr);
	$gcmj = json_decode($result);
	for ($i = 0; $i < count($gcmj->results); $i++) {
		$eror = $gcmj->results[$i]->error;
		if($eror == "InvalidRegistration" OR $eror == "NotRegistered" OR $eror == "MissingRegistration")
		{
			$regiderr = $SendToArr[$i];
			$SQL = "DELETE FROM user_reg WHERE regid = '$regiderr'";
			mysql_query($SQL);
		}
		if(isset($gcmj->results[$i]->registration_id))
		{
			$newreg = $gcmj->results[$i]->registration_id;
			$regiderr = $SendToArr[$i];
			$SQL = "UPDATE user_reg SET regid = '$newreg' WHERE regid = '$regiderr'";
			mysql_query($SQL);
		}
	}
}
?>