<?php
$server = "localhost";
$username = "appusers";
$password = "123";
$database = "carpool"; 
$conn =  new mysqli($server,$username,$password,$database);
if ($conn->connect_error) {
    die("connection failed".$conn->error);
}


?>