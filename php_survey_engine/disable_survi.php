<?
include_once "conn.php";
include_once 'GCmSend.php';
if(isset($_GET['id']))
{
	$id=$_GET['id'];
	$sts=$_GET['sts'];
	echo "1.".$id." ".$sts."<br />";
	$SQL="UPDATE tilestable SET sts=".$sts." WHERE id='".$id."'";
	mysql_query($SQL);
	if($sts==0)
	{
		echo "2."."<br />";
		$SQL="SELECT * FROM tilestable WHERE id='$id'";
		$row=mysql_fetch_array(mysql_query($SQL));
		$cmp=$row['id'];
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
			$ops=0;
			echo "3. ".$ty."<br />";
			unset($opsi);
			$opsi="";
			if($ty==2)
			{
				$SQL="SELECT * FROM options_radio WHERE id='$cmp' AND qno='$qn'";
				$result2=mysql_query($SQL);
				if(mysql_num_rows($result2)!=0)
				{
					$aw=mysql_fetch_array($result2);
					$opsi=$aw['op'];
				}
			}
			if($ty==3)
			{
				$SQL="SELECT * FROM options_checkbox WHERE id='$cmp' AND qno='$qn'";
				$result2=mysql_query($SQL);
				if(mysql_num_rows($result2)!=0)
				{
					$aw=mysql_fetch_array($result2);
					$opsi=$aw['op'];
				}
			}
			$at="";
			$at=array('id' => $row1['id'], 'qno' => $row1['qno'], 'quest' => $row1['quest'], 'typ' => $row1['typ'], 'g' => $row1['g'], 'isOp' => $row1['isOp'], 'pts' => $row1['pts'], 'op' => $opsi);
			$at2[$j]=$at;
			$j++;
		}
		$ax="";
		$ax=array("gcmid" => 1,'id' => $id, 'compNm' => $row['compNm'], 'nodl' => $row['nodl'], 'pts' => $row['pts'], 'sts' => $row['sts'], 'no' =>$row['no'], 'questions' => $at2 );
		echo "4. ".json_encode($ax)."<br />";
	}
	else
	{
		$ax=array("gcmid" => 6, "id" => $id);

	}
	$SQL="SELECT regid FROM user_reg";
	$result1=mysql_query($SQL);
	$ppl_cnt=mysql_num_rows($result1);

	for($i=0;$i<$ppl_cnt;$i++)
	{
		$result[$i]=mysql_fetch_array($result1);
	}
	$j=0;
	for($i=0;$i<$ppl_cnt;$i++)
	{
		$arr[$j]=$result[$i]['regid'];
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
		$arr[$j]=$result[$i]['regid'];
		$j++;
		$i--;
	}
	if($gn)
	{
		SendGCm($arr,$ax);
	}
	
	//header("Location:disable_surv.php");
}
?>