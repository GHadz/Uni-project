<?php
session_start();
require 'config.php';

if(!empty($_SESSION['userID'])) {
  if(isset($_POST['source']) && isset($_POST['destination']) && isset($_POST['date'])
    && isset($_POST['time']) && isset($_POST['seats'])) {
      
    // Get values from form
    $source = $_POST['source'];
    $destination = $_POST['destination'];
    $date = $_POST['date'];
    $time = $_POST['time'];
    $seats = $_POST['seats'];
    $notes = $_POST['notes'];
    
    // Get user info from SESSION
    $userID = $_SESSION['userID'];
    $username = $_SESSION['username'];
    
    if(!empty($source) && !empty($destination) && !empty($date) && !empty($seats)) {    
      // Prepare and execute statement
      $stmt = $conn->prepare("INSERT INTO rides (sourceID, destinationID, StartDate, maxCapacity)
        VALUES (?, ?, ?, ?)");
      $stmt->bind_param("sssi", $source, $destination, $date, $seats);
      
      if($stmt->execute()) {
        echo "Ride posted successfully!";
      } else {
        echo "Error: " . $stmt->error;
      }
      
      $stmt->close();
    } 
  } else {
    header('Location: index.php');
  }
} else {
  header('Location: index.php');
}
?>
