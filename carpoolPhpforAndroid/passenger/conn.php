<?php
$server = "localhost";
$username = "root";
$password = "";
$database = "carpool"; 
$conn =  new mysqli($server,$username,$password,$database);
if ($conn->connect_error) {
    die("connection failed".$conn->error);
}


?>