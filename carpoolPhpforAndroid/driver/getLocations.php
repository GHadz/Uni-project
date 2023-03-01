<?php
    require_once "conn.php";
    $stmt = mysqli_stmt_init($conn);
    if ( mysqli_stmt_prepare($stmt,"SELECT locations.locationName,locations.locationID FROM locations;")) {

        // Execute query
         mysqli_stmt_execute($stmt);

         mysqli_stmt_bind_result($stmt,$name,$id);
         $locations= array();
         while(mysqli_stmt_fetch($stmt)){
             $tmp = array(); 

             $tmp['name'] = $name;
             $tmp['id'] = $id;

             array_push($locations,$tmp);
         }
       $stmt -> close();
       echo json_encode($locations);
    }
    

?>