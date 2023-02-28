
<?php

//search for both src and dest
if (isset($_POST["sourceLocation"]) && isset($_POST["destinationLocation"]) && isset($_POST["nbPassengers"]) 
 && isset($_POST["date"])) {
    require_once "config.php";

  $stmt = mysqli_stmt_init($conn);
  $param1 = $_POST["sourceLocation"];
  $param2 = $_POST["destinationLocation"];


if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName 
FROM rides,users,locations A,locations B,statuses
 WHERE rides.driverID = users.userID AND rides.statusID = statuses.statusID
 AND statuses.statusName = 'Starting Soon'AND rides.sourceID = A.locationID 
 AND A.locationName=? AND rides.destinationID = b.locationID AND B.locationName=?")) 
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
}



//search for just dest
if (isset($_POST["destinationLocation"]) && isset($_POST["nbPassengers"]) 
 && isset($_POST["date"])) {

  require_once "config.php";
  $stmt = mysqli_stmt_init($conn);
 
  $param2 = $_POST["destinationLocation"];

 

if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName
FROM rides,users,locations A,locations B,statuses 
WHERE rides.driverID = users.userID AND rides.statusID = statuses.statusID 
AND statuses.statusName = 'Starting Soon'AND rides.destinationID = B.locationID 
AND B.locationName=? AND rides.destinationID = A.locationID ")) 
{
  mysqli_stmt_bind_param($stmt,"s",$param2);
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
}


//search for just src
if (isset($_POST["sourceLocation"]) && isset($_POST["nbPassengers"]) 
 && isset($_POST["date"])) {
  require_once "config.php";
  $stmt = mysqli_stmt_init($conn);
  $param1 = $_POST["sourceLocation"];



if ( mysqli_stmt_prepare($stmt,"SELECT rides.rideID,users.userName,rides.startDate,A.locationName,B.locationName 
FROM rides,users,locations A,locations B,statuses WHERE rides.driverID = users.userID 
AND rides.statusID = statuses.statusID AND statuses.statusName = 'Starting Soon'AND 
rides.sourceID = A.locationID AND A.locationName=? AND rides.destinationID = b.locationID")) 
{
    mysqli_stmt_bind_param($stmt,"s",$param1);    // Execute query
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
}

?>


<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Carpoolify</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <link rel="icon" type="image/png" href="images/icons/favicon.ico" />

    <!-- Google Fonts -->
    <link
      href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Roboto:300,300i,400,400i,500,500i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i"
      rel="stylesheet"
    />
    <link
      href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet"
    />

    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD"
      crossorigin="anonymous"
    />

    <!-- <link
      rel="stylesheet"
      type="text/css"
      href="vendor/bootstrap/css/bootstrap.min.css"
    /> -->

    <link
      rel="stylesheet"
      type="text/css"
      href="fonts/font-awesome-4.7.0/css/font-awesome.min.css"
    />

    <link
      rel="stylesheet"
      type="text/css"
      href="fonts/iconic/css/material-design-iconic-font.min.css"
    />

    <link rel="stylesheet" type="text/css" href="vendor/animate/animate.css" />

    <link
      rel="stylesheet"
      type="text/css"
      href="vendor/css-hamburgers/hamburgers.min.css"
    />

    <link
      rel="stylesheet"
      type="text/css"
      href="vendor/animsition/css/animsition.min.css"
    />

    <link
      rel="stylesheet"
      type="text/css"
      href="vendor/select2/select2.min.css"
    />

    <link
      rel="stylesheet"
      type="text/css"
      href="vendor/daterangepicker/daterangepicker.css"
    />

    <link rel="stylesheet" type="text/css" href="css/util.css" />
    <link rel="stylesheet" type="text/css" href="css/main.css" />
  </head>


  <body>
  <div class="container">

    <div class="row">
      <div class="col-md-12 text-center">
        <h1 class="display-4" style="background-color: #007bff; color: #fff; padding: 20px;
       border-radius: 10px; box-shadow: 0px 5px 10px rgba(0,0,0,0.3); transition: all 0.3s;">Search Results</h1>
      </div>
    </div>

    <div class="row mt-5">
      <div class="col-md-12">
        <table class="table table-striped table-hover">
          <thead class="bg-primary text-white">
            <tr>
              <th>Driver Name</th>
              <th>Start Date</th>
              <th>Source Location</th>
              <th>Destination Location</th>
              <th>REQUEST HERE!</th>
            </tr>
          </thead>
          <tbody>
            <?php foreach ($ride as $result) { ?>
              <tr>
                <td><?php echo $result['name']; ?></td>
                <td><?php echo $result['date']; ?></td>
                <td><?php echo $result['src']; ?></td>
                <td><?php echo $result['dest']; ?></td>
                <td>
                  <a href="requestAvailableRide.php">
                    <button class="btn btn-primary">Request Ride</button>
                  </a>
                </td>
              </tr>
            <?php } ?>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</body>


</html>
