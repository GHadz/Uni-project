<?php
if(isset($_POST["condId"]) &&isset($_POST["condBool"])){
    require_once "conn.php";
     //query for both source and location
    if (isset($_POST["src"]) &&isset($_POST["dest"])) {
    $sql =    "SELECT rides.rideID FROM conditions,ridecondition,rides,locations A,locations B,statuses WHERE rides.rideID = ridecondition.rideID AND ridecondition.conditionID =conditions.conditionID AND conditions.conditionID =? AND ridecondition.conditionAllowed = ? AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = B.locationID AND B.locationName=? AND rides.destinationID = A.locationID AND A.locationName=?;";
    $dest = $_POST["dest"];
    $src = $_POST["src"];
    }
    elseif (isset($_POST["src"])) { //just for source
        $src = $_POST["src"];
        $sql =    "SELECT rides.rideID FROM conditions,ridecondition,rides,locations B,statuses WHERE rides.rideID = ridecondition.rideID AND ridecondition.conditionID =conditions.conditionID AND conditions.conditionID =? AND ridecondition.conditionAllowed = ? AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = B.locationID AND B.locationName=?;";
    }
    elseif (isset($_POST["dest"])) { //just for destination
        $dest = $_POST["dest"];
        $sql =    "SELECT rides.rideID FROM conditions,ridecondition,rides,locations A,statuses WHERE rides.rideID = ridecondition.rideID AND ridecondition.conditionID =conditions.conditionID AND conditions.conditionID =? AND ridecondition.conditionAllowed = ? AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.destinationID = A.locationID AND A.locationName=?;";
    }
    else {return;}
    $sql2 = "SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName FROM rides,users,locations A,locations B,statuses WHERE rides.rideID IN (?) AND rides.driverID = users.userID AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND rides.sourceID = A.locationID AND rides.destinationID = b.locationID";
    $condId =  $_POST["condId"];
    $condBool =$_POST["condBool"];
    $var =explode(",",$condId);
    $var1 =explode(",",$condBool);
    $ids = array();
    $first = true;


    for ($i=0; $i < count($var) ; $i++) { 
    $stmt = mysqli_stmt_init($conn);
    if ( mysqli_stmt_prepare($stmt,$sql)) {
    
            if (isset($_POST["src"]) &&isset($_POST["dest"])) { //add params for both source and location
                mysqli_stmt_bind_param($stmt,"ssss",$var[$i],$var1[$i],$src,$dest);
            }
            elseif (isset($_POST["src"])) { //params for just source
                mysqli_stmt_bind_param($stmt,"sss",$var[$i],$var1[$i],$src);
            }
            elseif (isset($_POST["dest"])) { //params for just destination
                mysqli_stmt_bind_param($stmt,"sss",$var[$i],$var1[$i],$dest);
            }
         mysqli_stmt_execute($stmt);
        mysqli_stmt_bind_result($stmt,$id);

        /*
        we keep redoing the query for each ridecondition id and finding the ride id
        after every loop we store the data in a tmp ids and clear ids
        then we check if the next ride ids we find satisfied the previous conditions by checking if they are in tmp ids
        if they're not we skip them, if they are we push into id
        after the loop we create tmp id again and clear ids
        */
        $tmpIds = $ids;
        $ids = array();
        while(mysqli_stmt_fetch($stmt)){
            $tmp = array(); 
            $tmp['id'] = $id;
            if($first) {array_push($ids,$tmp);$first = false;}
        elseif(in_array($tmp,$tmpIds,false)) {array_push($ids,$tmp);}
        }   
    }    
}


if (empty($ids)) {
    echo "empty"; return; 
}
else {

$s="";
//take all the elements and separate them with ',' and make it a string so we can put in in query
    foreach ($ids as &$value) {
        foreach ($value as &$v) {
            $s.="$v,";
        }
    }
$s = substr($s, 0, -1);

if ( mysqli_stmt_prepare($stmt,$sql2)) { //query 2 finds the details for the ids we found

    mysqli_stmt_bind_param($stmt,"s",$s); 
     mysqli_stmt_execute($stmt);

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
}

else echo "failure";

?>
