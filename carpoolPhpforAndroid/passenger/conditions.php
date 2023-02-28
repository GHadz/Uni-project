<?php
if(isset($_POST["id"])){
    require_once "conn.php";
    $idS = $_POST["id"];
    $stmt = mysqli_stmt_init($conn);
    //prepare query
    if ( mysqli_stmt_prepare($stmt,"SELECT conditions.conditionName, ridecondition.conditionAllowed FROM conditions,ridecondition WHERE ridecondition.rideID = ? AND ridecondition.conditionID = conditions.conditionID;")) {
        // Bind parameters
        mysqli_stmt_bind_param($stmt,"s", $idS);
        // Execute query
         mysqli_stmt_execute($stmt);
        // Bind result variables
        mysqli_stmt_bind_result($stmt,$name,$bool);
        $idAr = array();
        while(mysqli_stmt_fetch($stmt)){
            $tmp = array(); 
            $tmp['name'] = $name;
            $tmp['bool'] = $bool;
    
            array_push($idAr,$tmp);
        }
        // Close statement
        $stmt -> close();
      }
    
    
    echo json_encode($idAr);
}


?>