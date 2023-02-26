-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 20, 2023 at 02:24 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `carpool`
--

-- --------------------------------------------------------

--
-- Table structure for table `conditions`
--

CREATE TABLE `conditions` (
  `conditionID` int(11) NOT NULL,
  `conditionName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `conditions`
--

INSERT INTO `conditions` (`conditionID`, `conditionName`) VALUES
(1, 'Pets Allowed'),
(2, 'Eating Allowed'),
(3, 'Smoking Allowed'),
(4, 'A.C On');

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE `locations` (
  `locationID` int(11) NOT NULL,
  `locationName` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `locations`
--

INSERT INTO `locations` (`locationID`, `locationName`) VALUES
(1, 'Fanar'),
(2, 'Zouk Mosbeh'),
(3, 'Jal el Dib'),
(4, 'Beirut'),
(5, 'Antelias'),
(6, 'Jdeide'),
(7, 'Jbeil');

-- --------------------------------------------------------

--
-- Table structure for table `requestride`
--

CREATE TABLE `requestride` (
  `requestID` int(11) NOT NULL,
  `requestDetails` varchar(1000) NOT NULL,
  `nbPassengers` int(11) NOT NULL,
  `passengerID` int(11) NOT NULL,
  `rideID` int(11) NOT NULL,
  `statusID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `reviewID` int(11) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `reviewType` varchar(20) NOT NULL,
  `rating` int(11) NOT NULL,
  `reviewedUserID` int(11) NOT NULL,
  `rideID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ridecondition`
--

CREATE TABLE `ridecondition` (
  `rideID` int(11) NOT NULL,
  `conditionID` int(11) NOT NULL,
  `conditionAllowed` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ridecondition`
--


-- --------------------------------------------------------

--
-- Table structure for table `rides`
--

CREATE TABLE `rides` (
  `rideID` int(11) NOT NULL,
  `startDate` datetime NOT NULL,
  `maxCapacity` int(11) NOT NULL,
  `details` varchar(1000) NOT NULL,
  `nbPassengers` int(11) NOT NULL,
  `driverID` int(11) NOT NULL,
  `sourceID` int(11) NOT NULL,
  `destinationID` int(11) NOT NULL,
  `statusID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


--



-- --------------------------------------------------------

--
-- Table structure for table `statuses`
--

CREATE TABLE `statuses` (
  `statusID` int(11) NOT NULL,
  `statusName` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `statuses`
--

INSERT INTO `statuses` (`statusID`, `statusName`) VALUES
(1, 'Starting soon'),
(2, 'Ongoing'),
(3, 'Ended'),
(4, 'Cancelled'),
(5, 'Accepted'),
(6, 'Rejected'),
(7, 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userID` int(11) NOT NULL,
  `userFirstName` varchar(30) NOT NULL,
  `userLastName` varchar(30) NOT NULL,
  `userName` varchar(30) NOT NULL,
  `userEmail` varchar(30) NOT NULL,
  `userPassword` varchar(200) NOT NULL,
  `userPhone` varchar(30) NOT NULL,
  `userDriverRating` float NOT NULL DEFAULT 0,
  `userPassengerRating` float NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--


--
-- Indexes for dumped tables
--

--
-- Indexes for table `conditions`
--
ALTER TABLE `conditions`
  ADD PRIMARY KEY (`conditionID`);

--
-- Indexes for table `locations`
--
ALTER TABLE `locations`
  ADD PRIMARY KEY (`locationID`);

--
-- Indexes for table `requestride`
--
ALTER TABLE `requestride`
  ADD PRIMARY KEY (`requestID`),
  ADD KEY `passengerID` (`passengerID`),
  ADD KEY `rideID` (`rideID`),
  ADD KEY `statusID` (`statusID`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`reviewID`),
  ADD KEY `reviewedUserID` (`reviewedUserID`),
  ADD KEY `rideID` (`rideID`);

--
-- Indexes for table `ridecondition`
--
ALTER TABLE `ridecondition`
  ADD KEY `rideID` (`rideID`),
  ADD KEY `conditionID` (`conditionID`);

--
-- Indexes for table `rides`
--
ALTER TABLE `rides`
  ADD PRIMARY KEY (`rideID`),
  ADD KEY `driverID` (`driverID`),
  ADD KEY `sourceID` (`sourceID`),
  ADD KEY `destinationID` (`destinationID`),
  ADD KEY `statusID` (`statusID`);

--
-- Indexes for table `statuses`
--
ALTER TABLE `statuses`
  ADD PRIMARY KEY (`statusID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `conditions`
--
ALTER TABLE `conditions`
  MODIFY `conditionID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `locations`
--
ALTER TABLE `locations`
  MODIFY `locationID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `requestride`
--
ALTER TABLE `requestride`
  MODIFY `requestID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `reviewID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rides`
--
ALTER TABLE `rides`
  MODIFY `rideID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `statuses`
--
ALTER TABLE `statuses`
  MODIFY `statusID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `requestride`
--
ALTER TABLE `requestride`
  ADD CONSTRAINT `fk_requestRide_rides_rideID` FOREIGN KEY (`rideID`) REFERENCES `rides` (`rideID`),
  ADD CONSTRAINT `fk_requestRide_statuses_statusID` FOREIGN KEY (`statusID`) REFERENCES `statuses` (`statusID`),
  ADD CONSTRAINT `fk_requestRide_users_passengerID` FOREIGN KEY (`passengerID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `fk_reviews_rides_rideID` FOREIGN KEY (`rideID`) REFERENCES `rides` (`rideID`),
  ADD CONSTRAINT `fk_reviews_users_reviewedUserID` FOREIGN KEY (`reviewedUserID`) REFERENCES `users` (`userID`);

--
-- Constraints for table `ridecondition`
--
ALTER TABLE `ridecondition`
  ADD CONSTRAINT `fk_rideCondition_conditions_conditionID` FOREIGN KEY (`conditionID`) REFERENCES `conditions` (`conditionID`),
  ADD CONSTRAINT `fk_rideCondition_rides_rideID` FOREIGN KEY (`rideID`) REFERENCES `rides` (`rideID`);

--
-- Constraints for table `rides`
--
ALTER TABLE `rides`
  ADD CONSTRAINT `fk_rides_locations_destinationID` FOREIGN KEY (`destinationID`) REFERENCES `locations` (`locationID`),
  ADD CONSTRAINT `fk_rides_locations_sourceID` FOREIGN KEY (`sourceID`) REFERENCES `locations` (`locationID`),
  ADD CONSTRAINT `fk_rides_statuses_statusID` FOREIGN KEY (`statusID`) REFERENCES `statuses` (`statusID`),
  ADD CONSTRAINT `fk_rides_users_driverID` FOREIGN KEY (`driverID`) REFERENCES `users` (`userID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

