<?php
if (isset($_POST["id"])) {

require_once "conn.php";
$idS =$_POST["id"];
$stmt = mysqli_stmt_init($conn);
$sql = "SELECT reviews.description,reviews.reviewType,reviews.rating,users.userName FROM users,reviews WHERE reviews.reviewedUserID =? AND users.userID = reviews.reviewerID;";
if ( mysqli_stmt_prepare($stmt,$sql)) {
    // Bind parameters
    mysqli_stmt_bind_param($stmt,"s", $idS);
    // Execute query
     mysqli_stmt_execute($stmt);
    // Bind result variables
    mysqli_stmt_bind_result($stmt,$details,$type,$rating,$name);
    $requests = array();

    while(mysqli_stmt_fetch($stmt)){
        $tmp = array(); 
        $tmp['details'] = $details;
        $tmp['name'] = $name;
        $tmp['rating'] = $rating;
        $tmp ['type'] = $type;
        array_push($requests,$tmp);
    }
    echo json_encode($requests);
  }
}
?>