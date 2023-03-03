
<?php
//search for both src and dest
if (isset($_POST["src"]) &&isset($_POST["dest"])&&isset($_POST["id"])) {

  require_once "conn.php";
  $stmt = mysqli_stmt_init($conn);
  $param1 = $_POST["src"];
  $param2 = $_POST["dest"];
  $param3 = $_POST["id"];
if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.driverID !=? AND rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = A.locationID AND A.locationName=? AND rides.destinationID = B.locationID AND B.locationName=?")) 
{
  mysqli_stmt_bind_param($stmt,"sss",$param3, $param1,$param2);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$name,$date,$source,$destination);
    $ride = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['name'] = $name;
        $tmp['date'] = $date;
        $tmp['src'] = $source;
        $tmp['dest'] = $destination;
        array_push($ride,$tmp);
    }
    // Close statement
    $stmt -> close();
  }
echo json_encode($ride);
}



//search for just dest
else if (isset($_POST["dest"])) {

  require_once "conn.php";
  $stmt = mysqli_stmt_init($conn);
 
  $param1 = $_POST["dest"];
  $param3 = $_POST["id"];
if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.driverID !=? AND rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.destinationID = B.locationID AND B.locationName=? AND rides.sourceID = A.locationID ")) 
{
  mysqli_stmt_bind_param($stmt,"ss",$param3 ,$param1);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$name,$date,$source,$destination);
    $ride = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['name'] = $name;
        $tmp['date'] = $date;
        $tmp['src'] = $source;
        $tmp['dest'] = $destination;
        array_push($ride,$tmp);
    }
    // Close statement
    $stmt -> close();
  }
echo json_encode($ride);
}


//search for just src
else if (isset($_POST["src"])) {

  require_once "conn.php";
  $stmt = mysqli_stmt_init($conn);
  $param1 = $_POST["src"];
  $param3 = $_POST["id"];

if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.driverID !=? AND rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = A.locationID AND A.locationName=? AND rides.destinationID = B.locationID")) 
{
  mysqli_stmt_bind_param($stmt,"ss",$param3, $param1);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$id,$name,$date,$source,$destination);
    $ride = array();
    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['id'] = $id;
        $tmp['name'] = $name;
        $tmp['date'] = $date;
        $tmp['src'] = $source;
        $tmp['dest'] = $destination;
        array_push($ride,$tmp);
    }
    // Close statement
    $stmt -> close();
  }
echo json_encode($ride);
}

?>