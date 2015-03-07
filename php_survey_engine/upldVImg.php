<?php
  include_once 'ImgRes.php';
   if(isset($_POST['upld']))
   {
	  $imgnm=$_POST['id1'];
$allowedExts = array("jpg", "jpeg","png");
$extension = end(explode(".", $_FILES["file"]["name"]));
if (($_FILES["file"]["type"] == "image/jpeg")||($_FILES["file"]["type"] == "image/jpg")||($_FILES["file"]["type"] == "image/png")||($_FILES["file"]["type"] == "image/gif")
&& ($_FILES["file"]["size"] < 20000)
&& in_array($extension, $allowedExts))
  {
  if ($_FILES["file"]["error"] > 0)
    {
    echo "Return Code: " . $_FILES["file"]["error"] . "<br>";
    }
  else
    {
    echo "Upload: " . $_FILES["file"]["name"] . "<br>";
    echo "Type: " . $_FILES["file"]["type"] . "<br>";
	echo "Img Name: ".$imgnm;
    echo "Size: " . ($_FILES["file"]["size"] / 1024) . " kB<br>";
    echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br>";

    if (file_exists("orgImg/".$imgnm.".".$extension))
      {
      echo $imgnm . " already exists. ";
      }
    else
      {
		echo "Resized";
		move_uploaded_file($_FILES["file"]["tmp_name"],
      "orgImg/" . $imgnm.".".$extension);
     $image = new SimpleImage();
   $image->load('orgImg/'.$imgnm.".".$extension);
   $image->resize_v(600,400);
   $image->save('VoucImages/'.$imgnm.'.jpg');
   unlink('orgImg/'.$imgnm.".".$extension);
   echo "Resized Saved";
      }
    }
  }
else
  {
  echo "Invalid file";
  }  
   }
?>
