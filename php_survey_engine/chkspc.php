<?
include_once "conn.php";
include_once 'GCmSend.php';
if(isset($_POST['usrnm']))
$usrnm=$_POST['usrnm'];
if(isset($_POST['id']))
$id=$_POST['id'];

if(isset($usrnm)&&isset($id))
{
	$res=mysql_num_rows(mysql_query("SELECT * FROM subspcl WHERE usrnm='$usrnm' AND id='$id'"));
	if($res==0)
	{
	$SQL="SELECT * FROM tilestable WHERE id='$id' AND isspcl=1";
	mysql_query("INSERT INTO subspcl values('$usrnm','$id')");
	
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
			if($ty==6)
			{
			$SQL="SELECT * FROM options_radioc WHERE id='$cmp' AND qno='$qn'";
			$result2=mysql_query($SQL);
			if(mysql_num_rows($result2)!=0)
				{   $aw=mysql_fetch_array($result2);
				$opsi=$aw['op'];
				}
			}
			$at="";
			$at=array('id' => $row1['id'], 'qno' => $row1['qno'], 'quest' => $row1['quest'], 'typ' => $row1['typ'], 'g' => $row1['g'], 'isOp' => $row1['isOp'], 'pts' => $row1['pts'], 'op' => $opsi, 'spm' => $row1['spm']);
			$at2[$j]=$at;
			$j++;
		}
		$ax="";
		$nodl=$row['nodl']-$cnt['c'];
		$ax=array("gcmid" => 1,'id' => $id, 'compNm' => $row['compNm'], 'nodl' => $nodl, 'pts' => $row['pts'], 'sts' => $row['sts'], 'no' =>$row['no'], 'questions' => $at2, 'conven' =>$row['conven'] );	
	$SQL="SELECT regid FROM user_reg WHERE usrnm='$usrnm'";
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
	}
	SendGCm($arr,$ax);
	}
}

?>