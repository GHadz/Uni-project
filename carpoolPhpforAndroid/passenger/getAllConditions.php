<?php

    require_once "conn.php";

    $stmt = mysqli_stmt_init($conn);
    if ( mysqli_stmt_prepare($stmt,"SELECT  conditions.conditionID,conditions.conditionName FROM conditions")) {


         mysqli_stmt_execute($stmt);

        mysqli_stmt_bind_result($stmt,$id,$name);
        $conditions = array();
        while(mysqli_stmt_fetch($stmt)){
            $tmp = array(); 
            $tmp['id'] = $id;
            $tmp['name'] = $name;
    
            array_push($conditions,$tmp);
        }
        // Close statement
        $stmt -> close();
      }
    
    
    echo json_encode($conditions);



?>