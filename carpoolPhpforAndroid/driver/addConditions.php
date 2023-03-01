<?php 
if(isset($_POST["condId"]) &&isset($_POST["condBool"])&&isset($_POST["rideId"])){
    require_once "conn.php";
    $condId =  $_POST["condId"];
    $condBool =$_POST["condBool"];
    $rideId =$_POST["rideId"];
    $var =explode(",",$condId);
    $var1 =explode(",",$condBool);
    $stmt = mysqli_stmt_init($conn);
    $sql = "INSERT INTO ridecondition (`rideID`, `conditionID`, `conditionAllowed`) VALUES (?, ?, ?)";
    for ($i=0; $i < count($var) ; $i++) { 
    if(mysqli_stmt_prepare($stmt,$sql))//insert query prepare
    {
        mysqli_stmt_bind_param($stmt,"sss",$rideId,$var[$i],$var1[$i]); //bind variables
$stmt->execute();
  $result = mysqli_stmt_get_result($stmt);
}           
}
  //check if the query was successful
  if(mysqli_stmt_affected_rows($stmt)>0){
      echo "success";
  }else{
    echo "failure";
  }
    
}
 ?>
