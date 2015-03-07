<?
include_once "conn.php";
include_once 'retPts.php';
//$tok="";
$reg="";
$usrnm="";
$bday="";
$quesnos="";
$questyp="";
$fname="";
$lname="";
$gen=0;
$dob="";
$salary=0;
//if(isset($_POST['token123']))
//$tok=$_POST['token123'];
if(isset($_POST['regid123']))
$reg=$_POST['regid123'];
if(isset($_POST['usrnm123']))
$usrnm=$_POST['usrnm123'];
if(isset($_POST['fname123']))
$fname=$_POST['fname123'];
if(isset($_POST['lname123']))
$lname=$_POST['lname123'];
if(isset($_POST['gen123']))
$gen=intval($_POST['gen123']);
if(isset($_POST['dob123']))
$dob=$_POST['dob123'];
if(isset($_POST['salary123']))
$salary=intval($_POST['salary123']);
$SQL="SELECT * FROM user_reg WHERE regid='$reg' AND usrnm='$usrnm'";
$result=mysql_query($SQL);
if(mysql_num_rows($result)==0)
{
	date_default_timezone_set('Asia/Kolkata');
	$dt=date("d-M-y H:i:s");
	$SQL="INSERT INTO user_reg VALUES('$usrnm','$reg','$dt')";
	mysql_query($SQL);
}
$SQL="SELECT * FROM user_details WHERE em='$usrnm'";
$result=mysql_query($SQL);
if(mysql_num_rows($result)==0)
{
	//$response=json_decode(file_get_contents("https://www.googleapis.com/oauth2/v1/userinfo?access_token=".$tok.""),true);
	//if(isset($response))
	//{
	//	$fname=$response['given_name'];
		//$lname=$response['family_name'];
		//$gen=$response['gender'];
		/*if($gen=='female')
		{
			$gen=1;
		}
		else
		{
			$gen=2;			
		}*/
		//$bday=$response['birthday'];	
		$SQL="INSERT INTO user_details VALUES('$usrnm','$fname','$lname','$dob',$gen,$salary)";
		mysql_query($SQL);			
	//}
}
$d1=strtotime($dob);/*
if($d1=="")
{
	$agegr=0;
}
else
{*/
date_default_timezone_set("Kolkata/Asia");
$d2=strtotime(date("Y-m-d"));
$diff=($d2-$d1)/(60*60*24*365);
/*if($diff>300)
$agegr=0;
else*/
if($diff>=61)
$agegr=1;
elseif($diff>=41)
$agegr=2;
elseif($diff>=26)
$agegr=3;
elseif($diff>=16)
$agegr=4;
elseif($diff>=8)
$agegr=5;
//}
/*$SQL="SELECT fname FROM user_details WHERE em='$usrnm'";
$result=mysql_query($SQL);
$arr=mysql_fetch_array($result);
$fnm=$arr['fname'];*/
$pts=retPts($usrnm);
/*if($agegr==0 && $gen==0)
{
	$SQL="SELECT * FROM tilestable WHERE sts=0 AND (id NOT IN (SELECT id FROM user_income WHERE usrnm='$usrnm')) AND ((isspcl=1 AND id IN (SELECT id FROM subspcl WHERE usrnm='$usrnm')) OR ((isspcl=0)))";
}
else if($agegr==0)
{
	$SQL="SELECT * FROM tilestable WHERE sts=0 AND gen=$gen AND (id NOT IN (SELECT id FROM user_income WHERE usrnm='$usrnm')) AND ((isspcl=1 AND id IN (SELECT id FROM subspcl WHERE usrnm='$usrnm')) OR ((isspcl=0)))";
}
else if($gen==0)
{
	$SQL="SELECT * FROM tilestable WHERE sts=0 AND agegr=$agegr AND (id NOT IN (SELECT id FROM user_income WHERE usrnm='$usrnm')) AND ((isspcl=1 AND id IN (SELECT id FROM subspcl WHERE usrnm='$usrnm')) OR ((isspcl=0)))";
}
else
{*/
	$SQL="SELECT * FROM tilestable WHERE ((sts=0) AND (gen LIKE (CONCAT('%',$gen,'%'))) AND (agegr LIKE (CONCAT('%',$agegr,'%'))) AND (salgrp LIKE (CONCAT('%',$salary,'%'))) AND (id NOT IN (SELECT id FROM user_income WHERE usrnm='$usrnm')) AND ((isspcl=1 AND id IN (SELECT id FROM subspcl WHERE usrnm='$usrnm')) OR ((isspcl=0))))";
//}
$result=mysql_query($SQL);
$no=mysql_num_rows($result);
$i=0;
$row=0;
while($row=mysql_fetch_assoc($result))
{	$cmp=$row['id'];
	$SQL="SELECT * FROM questions WHERE id='$cmp'";
	$result1=0;
	$result1=mysql_query($SQL);
	$j=0;
	$row1=0;	
	$at2="";
	while($row1=mysql_fetch_assoc($result1))
	{
		$qn=$row1['qno'];
		$ty=$row1['typ'];
		$spm=$rows1['spm'];
		$ops=0;	
		unset($opsi);
		$opsi="";
		if($ty==2)
		{
			$SQL="SELECT * FROM options_radio WHERE id='$cmp' AND qno='$qn'";
			$result2=mysql_query($SQL);
			if(mysql_num_rows($result2)!=0)
			{   $aw=mysql_fetch_array($result2);
			$opsi=$aw['op'];
			}
		}
		if($ty==6)
		{
			$SQL="SELECT * FROM options_radioc WHERE id='$cmp' AND qno='$qn'";
			$result2=mysql_query($SQL);
			if(mysql_num_rows($result2)!=0)
			{   $aw=mysql_fetch_array($result2);
			$opsi=$aw['op'];
			}
		}
		if($ty==3)
		{	
		    $SQL="SELECT * FROM options_checkbox WHERE id='$cmp' AND qno='$qn'";
			$result2=mysql_query($SQL);
			if(mysql_num_rows($result2)!=0)
			{   $aw=mysql_fetch_array($result2);
			$opsi=$aw['op'];
			}
		}	
		$at="";	
		if($row1['isOp']==0)
		{$quesnos=$quesnos.",".$row1['qno'];
		$questyp=$questyp.",".$row1['typ'];
		}
		$at=array('id' => $row1['id'], 'qno' => $row1['qno'], 'quest' => $row1['quest'], 'typ' => $row1['typ'], 'g' => $row1['g'], 'isOp' => $row1['isOp'], 'pts' => $row1['pts'], 'op' => $opsi,'spm' => $row1['spm']);
		$at2[$j]=$at;
		$j++;
	}
	$ax="";
	$id=$row['id'];
	$cnt=mysql_fetch_array(mysql_query("SELECT COUNT(id) as c FROM user_income WHERE id='$id'"));
	$nodl=$row['nodl']-$cnt['c'];
	if($nodl>0)
	{
	$ax=array('id' => $id, 'compNm' => $row['compNm'], 'nodl' => $nodl, 'pts' => $row['pts'], 'sts' => $row['sts'], 'no' =>$row['no'], 'conven' =>$row['conven'], 'questions' => $at2 );
	$ax2[$i]=$ax;
	$i++;
	}
}
$pc=mysql_query("SELECT * FROM pollcat WHERE sts = 1");
$num=mysql_num_rows($pc);
for($i=0;$i<$num;$i++)
{
$polcats[$i]=mysql_fetch_array($pc);
}
$ax5="";
for($i=0;$i<$num;$i++)
{
$ax="";	
$catid=$polcats[$i]['catnm'];
$sqlrr="";
$sqlrr="SELECT COUNT(qid) AS c FROM pollques WHERE id ='$catid'";
$res=mysql_query("SELECT COUNT(qid) AS c FROM pollques WHERE id ='$catid'");
$que=mysql_fetch_array($res);
$ax=array('id' => $catid, 'catnm' => $polcats[$i]['catnm'], 'nrpl' => $que['c']);
$ax5[$i]=$ax;
}
$resu=mysql_query("SELECT qid FROM pollsub WHERE usrnm='$usrnm'");
$n=mysql_num_rows($resu);
for($i=0;$i<$n;$i++)
{
$gresu[$i]=mysql_fetch_array($resu);
}
$polls="";
$j=0;
for($i=0;$i<$n;$i++)
{
$qid=$gresu[$i]['qid'];	
$SQL="SELECT * FROM pollques WHERE qid='$qid' AND sts=1";
$fl=mysql_query($SQL);
if(mysql_num_rows($fl)==1)
{
$polln="";
$polln=mysql_fetch_array($fl);
if($polln['icreated']==$usrnm)
{
	$icreated=1;
}
else
{
	$icreated=0;
}
$opsx="";
$opsa="";
$opsx=mysql_fetch_array(mysql_query("SELECT ops FROM pollques WHERE qid='$qid'"));
$opsa=preg_split("/~#%&/",$opsx['ops']);
$numc=0;
$numc=count($opsa);
$rp="";
$nrpl=0;
for($k=0;$k<$numc;$k++)
{
$c="";
	$c=mysql_fetch_array(mysql_query("SELECT COUNT(usrnm) AS c FROM pollres WHERE qid='$qid' AND res=$k"));
	$rp=$rp.",".$c['c'];	
	$nrpl=$nrpl+$c['c'];
}
$rp23=substr($rp,1);
$polls[$j]=array("id"=>$polln['id'],"qid"=>$polln['qid'],"ques"=>$polln['ques'],"nrpl"=>$nrpl,"ops"=>$opsx['ops'],"res"=>$rp23,"icreated"=>$icreated);
$j++;
}
}
$ax4="";
$ax4=array('fname' => $fname , 'pts' => $pts , 'no' => $no ,'tiles' => $ax2, 'pollcats' => $ax5, 'polls' => $polls);
echo str_replace("\\","",json_encode($ax4));
?>