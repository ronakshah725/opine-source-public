<?
include_once "conn.php";
include_once "curpts.php";
include_once 'GCmSend.php';
$usrnm="";
$id="";
$qno=0;
$typ=0;
$resS="";
$resI=0;
$pts=0;
$tot_pts=0;
$arr1 = array('state' => 0);
if(isset($_POST['res']))
{
	$res=json_decode(stripslashes($_POST['res']),true);
	$usrnm=$res['usrnm'];
	$id=$res['id'];
	$no=$res['no'];
	$responses=$res['responses'];
	$SQL="SELECT * FROM user_income WHERE usrnm='$usrnm' AND id='$id'";
	$result=mysql_query($SQL);
	if(mysql_num_rows($result)==0)
	{
		$SQL="INSERT INTO user_income values('$usrnm','$id',0,0)";
		mysql_query($SQL);
	}
	for($i=0;$i<$no;$i++)
	{
		$ans=$responses[$i];
		$qno=$ans['qno'];
		$typ=$ans['typ'];
		$SQL="SELECT pts FROM questions WHERE id='$id' AND qno=$qno";
		$result1=mysql_query($SQL);
		$pt=mysql_fetch_array($result1);
		if($typ==1)
		{
			$SQL="SELECT * FROM response_ynr WHERE id='$id' AND usrnm='$usrnm' AND qno=$qno";
			$result=mysql_query($SQL);
			if(mysql_num_rows($result)==0)
			{
				$resI=$ans['res'];
				if($resI!=0)
				{
					$SQL="INSERT INTO response_ynr VALUES('$usrnm','$id',$qno,$resI)";
					mysql_query($SQL);
					$tot_pts=$tot_pts+$pt['pts'];
				}
			}
		}
		elseif($typ==5)
		{
			$SQL="SELECT * FROM response_feedbackr WHERE usrnm='$usrnm' AND id='$id' AND qno=$qno";
			$result=mysql_query($SQL);
			$resS=$ans['res'];
			if(mysql_num_rows($result)==0)
			{
				$resS=$ans['res'];
				if($resS!="")
				{
					$SQL="INSERT INTO response_feedbackr values('$usrnm','$id',$qno,'$resS')";
					mysql_query($SQL);
					$tot_pts=$tot_pts+$pt['pts'];
				}
			}
		}
		elseif($typ==2)
		{
			$SQL="SELECT * FROM options_radior WHERE usrnm='$usrnm' AND id='$id' AND qno=$qno";
			$result=mysql_query($SQL);
			$resI=$ans['res'];
			if(mysql_num_rows($result)==0)
			{
				$resI=$ans['res'];
				if($resI!=0)
				{
					$SQL="INSERT INTO options_radior VALUES('$usrnm','$id',$qno,$resI)";
					mysql_query($SQL);
					$tot_pts=$tot_pts+$pt['pts'];
				}
			}
		}
		elseif($typ==7)
		{
			$SQL="SELECT * FROM dateans WHERE usrnm='$usrnm' AND id='$id' AND qno=$qno";
			$result=mysql_query($SQL);
			if(mysql_num_rows($result)==0)
			{
				$resS=$ans['res'];
				if($resS!="")
				{
					$SQL="INSERT INTO dateans VALUES('$usrnm','$id',$qno,'$resS')";
					mysql_query($SQL);
					$tot_pts=$tot_pts+$pt['pts'];
				}
			}
		}
		elseif($typ==6)
		{
			$SQL="SELECT * FROM options_radiocr WHERE usrnm='$usrnm' AND id='$id' AND qno=$qno";
			$result=mysql_query($SQL);
			$resI=$ans['res'];
			if(mysql_num_rows($result)==0)
			{
				$resI=$ans['res'];
				if($resI!=0)
				{
						
					$res1="";
					if(isset($ans['res1']))
					{
						$res1=$ans['res1'];
						$ops=mysql_fetch_array(mysql_query("SELECT op FROM options_radioc WHERE id='$id' AND qno=$qno"));
						$len=count(preg_split("/~#%&/",$ops['op']));
						if($res1!=""&&$resI==$len)
						{
							$SQL="INSERT INTO options_radiocr VALUES('$usrnm','$id',$qno,$resI)";
							mysql_query($SQL);
							$SQL="INSERT INTO otherans VALUES('$usrnm','$id',$qno,'$res1')";
							mysql_query($SQL);
							$tot_pts=$tot_pts+$pt['pts'];
						}
						if($resI!=$len)
						{
							$SQL="INSERT INTO options_radiocr VALUES('$usrnm','$id',$qno,$resI)";
							mysql_query($SQL);
							$tot_pts=$tot_pts+$pt['pts'];
						}
					}
				}
			}
		}
		elseif($typ==3)
		{
			$SQL="SELECT * FROM options_checkboxr WHERE usrnm='$usrnm' AND id='$id' AND qno=$qno";
			$result=mysql_query($SQL);		
			if(mysql_num_rows($result)==0)
			{
				$resS=$ans['res'];
				$resS=ltrim($resS,"[");
				$resS=rtrim($resS,"]");
				$resS1=explode(",",$resS);
				$sum=0;
				for($p=0;$p<count($resS1);$p++)
				{
				$sum=$sum+$resS1[$p];	
				}
				if($sum>0)
				{
					$SQL="INSERT INTO options_checkboxr VALUES('$usrnm','$id',$qno,'$resS')";
					mysql_query($SQL);
					$tot_pts=$tot_pts+$pt['pts'];
				}
			}
		}
		elseif($typ==4)
		{
			$SQL="SELECT * FROM response_ratingr WHERE usrnm='$usrnm' AND id='$id' AND qno=$qno";
			$result=mysql_query($SQL);
			if(mysql_num_rows($result)==0)
			{
				$resI=$ans['res'];
				if($resI!=0)
				{
					$SQL="INSERT INTO response_ratingr VALUES('$usrnm','$id',$qno,$resI)";
					mysql_query($SQL);
					$tot_pts=$tot_pts+$pt['pts'];
				}
			}
		}
	}
	$SQL="UPDATE user_income SET sts=1, pts=$tot_pts WHERE usrnm='$usrnm' AND id='$id'";
	mysql_query($SQL);
	SendGCm(regids($usrnm),array("gcmid" => 3, "srvid" => $id));
	$SQL="SELECT regid FROM user_reg";
	$result1=mysql_query($SQL);
	$ppl_cnt=mysql_num_rows($result1);
	$SQL="SELECT nodl FROM tilestable WHERE id='$id'";
	$nodl1=mysql_query($SQL);
	$nodls1=mysql_fetch_array($nodl1);
	$SQL="SELECT COUNT(usrnm) AS 'cnt' FROM user_income GROUP BY id HAVING id='$id'";
	$nodl2=mysql_query($SQL);
	$nodls2=mysql_fetch_array($nodl2);
	$nodl=$nodls1['nodl']-$nodls2['cnt'];
	$ax=array('gcmid' => 4, 'id' => $id, 'nodl' => $nodl);
	for($i=0;$i<$ppl_cnt;$i++)
	{
		$result2[$i]=mysql_fetch_array($result1);
	}
	$j=0;
	for($i=0;$i<$ppl_cnt;$i++)
	{
		$arr[$j]=$result2[$i]['regid'];
		$j=$j+1;
		if($i%900==0)
		{
			SendGCm($arr,$ax);
			$j=0;
		}
	}
	$i=$i-1;
	$j=0;
	$gn=false;
	unset($arr);
	while(!($i%900==0))
	{
		$gn=true;
		$arr[$j]=$result2[$i]['regid'];
		$j++;
		$i--;
	}
	if($gn)
	{
		SendGCm($arr,$ax);
	}
	updatepts($usrnm);
	$arr1 = array('state' => 1);
}
echo json_encode($arr1);
?>