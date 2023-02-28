
<?php
//search for both src and dest
if (isset($_POST["src"]) &&isset($_POST["dest"])) {

  require_once "conn.php";
  $stmt = mysqli_stmt_init($conn);
  $param1 = $_POST["src"];
  $param2 = $_POST["dest"];

if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = A.locationID AND A.locationName=? AND rides.destinationID = b.locationID AND B.locationName=?")) 
{
  mysqli_stmt_bind_param($stmt,"ss", $param1,$param2);
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
if (isset($_POST["dest"])) {

  require_once "conn.php";
  $stmt = mysqli_stmt_init($conn);
 
  $param1 = $_POST["dest"];

if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.destinationID = B.locationID AND B.locationName=? AND rides.destinationID = A.locationID ")) 
{
  mysqli_stmt_bind_param($stmt,"s", $param1);
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
if (isset($_POST["src"])) {

  require_once "conn.php";
  $stmt = mysqli_stmt_init($conn);
  $param1 = $_POST["src"];


if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = A.locationID AND A.locationName=? AND rides.destinationID = b.locationID")) 
{
  mysqli_stmt_bind_param($stmt,"s", $param1);
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