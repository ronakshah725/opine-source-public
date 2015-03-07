<?
include_once "getrechapikey.php";
function sendreq($Mob, $Prov, $Loc, $Amt, $SysRef)
{
	$email = EMAIL_ID;
	$pass = PASSWORD;
	$sec_key = SEC_KEY;
	$urlToSend = "http://hansaacademy.org/api/rechapi.php";
	$postdata = http_build_query(
		array(
			'email' 		=> $email,
			'pass'			=> $pass,
			'seckey'		=> $sec_key,
			'mob'			=> $Mob,
			'prov'			=> $Prov,
			'loc'			=> $Loc,
			'amt'			=> $Amt,
			'sysref'		=> $SysRef
	));
	$opts = array('http' =>
		array(
			'method'  => 'POST',
			'header'  => 'Content-type: application/x-www-form-urlencoded',
			'content' => $postdata
	));
	$context  = stream_context_create($opts);
	return file_get_contents($urlToSend, false, $context);
}
?>