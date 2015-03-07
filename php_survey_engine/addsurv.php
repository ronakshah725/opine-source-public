<?
include_once "conn.php";
include_once 'GCmSend.php';
if(isset($_POST['sub']))
{
    $id=$_POST['survid'];
	$cmp=$_POST['compnm'];
	$tot_pts=0;
	$quesnos="";
	$questyp="";
	$num=$_POST['num'];
	$tar=$_POST['tar'];		
	if(isset($_POST['isspcl']))
	$isspcl=1;
	else
	$isspcl=0;
	for($i=1;$i<=$num;$i++)
	{
		
        $qn=$_POST["n".$i];
		$quest=$_POST["q".$i];
		$typ=$_POST["t".$i];
		$pnts=$_POST["p".$i];
		$tot_pts=$tot_pts+$pnts;	
		$nop=$_POST["h".$i];
		$spm=-1;
		if(isset($_POST["isop".$i]))
		$isop=1;
		else
		$isop=0;
		$ops="";		
		if($typ==2)
		{
			for($j=1;$j<=$nop;$j++)
			{				
				$ops=$ops."~#%&".$_POST[$i."op".$j];
				if(isset($_POST[$i."ops".$j]))
				{
					$spm=$j;
				}
			}
			$ops=ltrim($ops,"~#%&");
			$SQL="INSERT INTO options_radio values('$id','$qn','$ops')";
			mysql_query($SQL);			
		}
		
		elseif($typ==6)
		{
			for($j=1;$j<=$nop;$j++)
			{				
				$ops=$ops."~#%&".$_POST[$i."op".$j];
				if(isset($_POST[$i."ops".$j]))
				{
					$spm=$j;
				}
			}
			$ops=ltrim($ops,"~#%&");
			$SQL="INSERT INTO options_radioc values('$id','$qn','$ops')";
			mysql_query($SQL);		
		}
		elseif($typ==3)
		{
			for($j=1;$j<=$nop;$j++)
			{				
				$ops=$ops."~#%&".$_POST[$i."op".$j];
				if(isset($_POST[$i."ops".$j]))
				{
					$spm=$j;
				}
			}
			$ops=ltrim($ops,"~#%&");
			$SQL="INSERT INTO options_checkbox values('$id','$qn','$ops')";
			mysql_query($SQL);
		}
		$at=0;
		if($isop==0)
		{$quesnos=$quesnos.",".$qn;
		$questyp=$questyp.",".$typ;
		}
		$SQL="INSERT INTO questions values('$id','$qn','$quest','$typ',0,$isop,'$pnts',$spm)";
		mysql_query($SQL);
		$at=array('id' => $id, 'qno' => $qn, 'quest' => $quest, 'typ' => $typ, 'g' => 0, 'isOp' => $isop, 'pts' => $pnts, 'op' => $ops,'spm' => $spm);
		$at2[$i-1]=$at;		
	}
    $r1="";
    if(isset($_POST['r1b']))
	$r1=$r1.",".$_POST['r1b'];
    if(isset($_POST['r1c']))
    $r1=$r1.",".$_POST['r1c'];
    $r1a="";
    /*
    for($i=0;$i<count($r1);$i++){
        $r1a = $r1a.",".$r1[$i];
    }
    */
    $r1a=substr($r1,1);
    $r2="";
    if(isset($_POST['r2b']))
    $r2=$r2.",".$_POST['r2b'];
    if(isset($_POST['r2c']))
    $r2=$r2.",".$_POST['r2c'];
    if(isset($_POST['r2d']))
    $r2=$r2.",".$_POST['r2d'];
    if(isset($_POST['r2e']))
    $r2=$r2.",".$_POST['r2e'];
    if(isset($_POST['r2f']))
    $r2=$r2.",".$_POST['r2f'];
    $r2a="";
    /*
    for($i=0;$i<count($r2);$i++){
        $r2a = $r2a.",".$r2[$i];
    }*/
    $r2a=substr($r2,1);
    $r3="";
    if(isset($_POST['r3b']))
    $r3=$r3.",".$_POST['r3b'];
    if(isset($_POST['r3c']))
    $r3=$r3.",".$_POST['r3c'];
    if(isset($_POST['r3d']))
    $r3=$r3.",".$_POST['r3d'];
    if(isset($_POST['r3e']))
    $r3=$r3.",".$_POST['r3e'];
    if(isset($_POST['r3f']))
    $r3=$r3.",".$_POST['r3f'];
    if(isset($_POST['r3g']))
    $r3=$r3.",".$_POST['r3g'];
    $r3a="";
    /*
    for($i=0;$i<count($r3);$i++){
        $r3a = $r3a.",".$r3[$i];
    }*/
    $r3a=substr($r3,1);
	$quesnos=trim($quesnos,",");
	$questyp=trim($questyp,",");
	$quesnos=$quesnos."~#%&".$questyp;
	$SQL="UPDATE tilestable SET compNm='$cmp', nodl=$tar, pts=$tot_pts, sts=0, no=$num, isspcl=$isspcl, gen='$r1a', agegr='$r2a', salgrp='$r3a', conven='$quesnos' WHERE id='$id'";
	mysql_query($SQL);
    $ax="";
	$ax=array('gcmid' => 1, 'id' => $id, 'compNm' => $cmp, 'nodl' => $tar, 'pts' => $tot_pts, 'sts' => 0, 'no' =>$num, 'conven' =>$quesnos, 'questions' => $at2 );	
	if($isspcl==0)
	{
    $r1a="'%".$r1a."%'";
    $r3a="'%".$r3a."%'";
	$SQL1="SELECT regid from user_reg WHERE usrnm IN (SELECT em FROM user_details WHERE (((DATEDIFF(CURDATE( ),bday)/365)>=61) OR (gen LIKE $r1a) OR (sal LIKE $r3a)))";
    $SQL2="SELECT regid from user_reg WHERE usrnm IN (SELECT em FROM user_details WHERE (((((DATEDIFF(CURDATE( ),bday)/365)>=41) AND (DATEDIFF(CURDATE( ),bday)/365)<=60)) OR (gen LIKE $r1a) OR (sal LIKE $r3a)))";
    $SQL3="SELECT regid from user_reg WHERE usrnm IN (SELECT em FROM user_details WHERE (((((DATEDIFF(CURDATE( ),bday)/365)>=26) AND (DATEDIFF(CURDATE( ),bday)/365)<=40)) OR (gen LIKE $r1a) OR (sal LIKE $r3a)))";
    $SQL4="SELECT regid from user_reg WHERE usrnm IN (SELECT em FROM user_details WHERE (((((DATEDIFF(CURDATE( ),bday)/365)>=16) AND (DATEDIFF(CURDATE( ),bday)/365)<=25)) OR (gen LIKE $r1a) OR (sal LIKE $r3a)))";
    $SQL5="SELECT regid from user_reg WHERE usrnm IN (SELECT em FROM user_details WHERE (((DATEDIFF(CURDATE( ),bday)/365)<=15) OR (gen LIKE $r1a) OR (sal LIKE $r3a)))";
    $exploded = explode(",",$r2a);
    $SQL="";
    if(in_array("1",$exploded)){$SQL.=$SQL1." UNION ";}
    if(in_array("2",$exploded)){$SQL.=$SQL2." UNION ";}
    if(in_array("3",$exploded)){$SQL.=$SQL3." UNION ";}
    if(in_array("4",$exploded)){$SQL.=$SQL4." UNION ";}
    if(in_array("5",$exploded)){$SQL.=$SQL5." UNION ";}
    $SQL = substr($SQL,0,(strlen($SQL)-7));
    echo $SQL;
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
		   SendGCm($arr,$ax);//uncomment
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
		SendGCm($arr,$ax);//uncomment
	}
}
}
else{
$SQL="SELECT MAX(aid) as m FROM tilestable";
$result2=mysql_query($SQL);
$result3=mysql_fetch_array($result2);
$aid=$result3['m']+1;
date_default_timezone_set('Asia/Kolkata');
$y=date("y");
$m=date("m");
$id="id".$y.$m.$aid;
$SQL="INSERT INTO tilestable (aid, id) VALUES($aid,'$id')";
mysql_query($SQL);
?>
<form action="upldImg.php" method="post"
enctype="multipart/form-data">
<input type="hidden" name="id1" value=<?php echo $id; ?> />
<table><tr><td>Upload Logo: </td><td>
<input type="file" name="file" id="file" /></td><td>
<input type="submit" name="upld" value="Upload" /></td></tr></table>
</form>
<form action="addsurv.php" method="post" >
<table ><tr><td><input type="text" name="survid" readonly="readonly" value=<?php echo $id; ?> /></td><td><input type="text" name="compnm" width="400" /></td></tr>
<tr><td>Make a Special Survey:</td><td><input type="checkbox" name="isspcl" /></td></tr>
<tr><td>Targetted Number: </td><td><input type="number" min='1' max='10000000' step='1' value='10' name="tar" /></td></tr>
<tr><td>Gender:</td><td><div><input type="checkbox" name="r1b" value="2" checked="checked" /> Male<br/><input type="checkbox" name="r1c" value="1" checked="checked" /> Female<br/></div></td></tr>
<tr><td>Targetted Age Group:</td><td><div><input type="checkbox" name="r2b" value="1" checked="checked" /> 61 and above<br/><input type="checkbox" name="r2c" value="2" checked="checked" /> 41-60<br/><input type="checkbox" name="r2d" value="3" checked="checked" /> 26-40<br/><input type="checkbox" name="r2e" value="4" checked="checked" /> 16-25<br/><input type="checkbox" name="r2f" value="5" checked="checked" /> 8-15<br/></div></td></tr>
<tr><td>Targetted Salary Group:</td><td><div><input type="checkbox" name="r3b" value="0" checked="checked" /> Nil<br/><input type="checkbox" name="r3c" value="1" checked="checked" /> Upto Rs. 2,00,000<br/><input type="checkbox" name="r3d" value="2" checked="checked" /> Rs 2,00,001 to Rs 5,00,000<br/><input type="checkbox" name="r3e" value="3" checked="checked" /> Rs 5,00,001 to Rs 10,00,000<br/><input type="checkbox" name="r3f" value="4" checked="checked" /> Rs 10,00,001 to Rs 20,00,000<br/><input type="checkbox" name="r3g" value="5" checked="checked" /> Rs 20,00,001 and above<br/></div></td></tr>
</table>
<input type="button" value="Add" onClick='javascript:addTable()' name="addsurv" >
<table id="addtable"></table>
<input type="hidden" name="num" id="num" value="1" />
<input type="submit" value="Add Survey" name="sub" />
</form>
<?
}
?>
<script type="text/javascript">
var n=1;
function sh(x){

if(document.getElementById("t"+x).selectedIndex==1||document.getElementById("t"+x).selectedIndex==2||document.getElementById("t"+x).selectedIndex==5)
	{
		document.getElementById("d"+x).style.display="block";		
	}
	else
	{
	    document.getElementById("d"+x).style.display="none";		
	}
}

function addOption(x){
	var c, r, t;
    t = document.getElementById("ta"+x);	
    r = t.insertRow(0); 
    c = r.insertCell(0);
	var nop=document.getElementById("h"+x).value;
	nop++;
	c.innerHTML = "<input type='text' value="+nop+" />";
	c = r.insertCell(1);
	var nm=""+x+"op"+nop;
	c.innerHTML = "<input type='text' name='"+nm+"' id='"+nm+"' />";
	c=r.insertCell(2);
	var nms=""+x+"ops"+nop;
	c.innerHTML = "<input type='checkbox' name='"+nms+"' id='"+nms+"' />";	
	document.getElementById("h"+x).value=nop;
	document.getElementById("ta"+x).appendChild(t);
}
function addTable() {
    var c, r, t;
    t = document.getElementById("addtable");
    r = t.insertRow(0); 
    c = r.insertCell(0);
	var i="n"+n;
    c.innerHTML = "<input type='text' readonly='readonly' value="+n+" name='"+i+"' />";
    c = r.insertCell(1);
	var i="q"+n;
    c.innerHTML = "<input type='text' name='"+i+"' width=200 />";
	c = r.insertCell(2);
	var i="p"+n;
    c.innerHTML = "<input type='number' min='1' max='10' step='1' value='1' name='"+i+"' />";
	c = r.insertCell(3);
	var i="t"+n;
    c.innerHTML = "<select name='"+i+"' id='"+i+"' onChange='javascript:sh("+n+")' ><option value='1' >Y/N</ option><option value='2' >Radio Choices</ option><option value='3' >Checkbox Choices</ option><option value='4' >Rating</ option><option value='5' >Feedback</ option><option value='6' >Radio Choices with Comment</ option><option value='7' >DatePicker</ option></ select>";
	c = r.insertCell(4);
	var i="isop"+n;
	c.innerHTML = "<input type='checkbox' name='"+i+"' value='ch' />";	
	c = r.insertCell(5);
	var nm=""+n+"op"+"1";
	var nms=""+n+"ops"+"1";
	c.innerHTML = "<div name='d"+n+"' id='d"+n+"' style='display:none' ><input type='hidden' name='h"+n+"' id='h"+n+"' value=1 /><input type='button' name='b"+n+"' id='b"+n+"'on onClick='javascript:addOption("+n+")' value='Add Option' /><table name='ta"+n+"' id='ta"+n+"' ><tr><td>1</td><td><input type='text' name='"+nm+"' id='"+nm+"' /></td><td><input type='checkbox' name='"+nms+"' id='"+nms+"' /></td></tr></table></div>";	
	document.getElementById("num").value=""+n;
	n++;
    document.getElementById("addtable").appendChild(t);	
}
</script>