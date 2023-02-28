<?php

$servername = "localhost";
$username = "elio";
$password = "";
$dbname = "gitmidproject";

$conn = mysqli_connect($servername, $username, $password, $dbname);


if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}


?>