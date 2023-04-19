-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 19, 2023 at 05:02 PM
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

--
-- Dumping data for table `requestride`
--

INSERT INTO `requestride` (`requestID`, `requestDetails`, `nbPassengers`, `passengerID`, `rideID`, `statusID`) VALUES
(14, 'need to meet up on the highway.', 2, 5, 28, 5),
(15, 'can meet up on the highway.', 2, 5, 27, 5);

--
-- Triggers `requestride`
--
DELIMITER $$
CREATE TRIGGER `accepted` AFTER UPDATE ON `requestride` FOR EACH ROW IF (NEW.statusID = (SELECT statusID FROM statuses WHERE statusName = 'accepted')) THEN
    UPDATE rides SET rides.nbPassengers = rides.nbPassengers + 1 WHERE rides.rideID = NEW.rideID;
  END IF
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `rejected` AFTER UPDATE ON `requestride` FOR EACH ROW IF (NEW.statusID = (SELECT statusID FROM statuses WHERE statusName = 'rejected')) THEN
    UPDATE rides SET rides.nbPassengers = rides.nbPassengers - 1 WHERE rides.rideID = NEW.rideID;
  END IF
$$
DELIMITER ;

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
  `rideID` int(11) NOT NULL,
  `reviewerID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`reviewID`, `description`, `reviewType`, `rating`, `reviewedUserID`, `rideID`, `reviewerID`) VALUES
(3, 'Decent driver but takes wrong turns.', 'Driver', 3, 4, 27, 5);

--
-- Triggers `reviews`
--
DELIMITER $$
CREATE TRIGGER `rating` AFTER INSERT ON `reviews` FOR EACH ROW BEGIN
    DECLARE avg_rating FLOAT;
    IF NEW.reviewType = 'Driver' THEN
        SELECT AVG(rating) INTO avg_rating FROM reviews WHERE reviewedUserID = NEW.reviewedUserID AND reviewType = 'Driver';
        UPDATE users SET userDriverRating = avg_rating WHERE userID = NEW.reviewedUserID;
    END IF;
    IF NEW.reviewType = 'Passenger' THEN
        SELECT AVG(rating) INTO avg_rating FROM reviews WHERE reviewedUserID = NEW.reviewedUserID AND reviewType = 'Passenger';
        UPDATE users SET userPassengerRating = avg_rating WHERE userID = NEW.reviewedUserID;
    END IF;
END
$$
DELIMITER ;

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

INSERT INTO `ridecondition` (`rideID`, `conditionID`, `conditionAllowed`) VALUES
(27, 1, 0),
(27, 2, 0),
(27, 3, 1),
(27, 4, 1),
(28, 1, 0),
(28, 2, 1),
(28, 3, 0),
(28, 4, 0),
(29, 1, 0),
(29, 2, 1),
(29, 3, 0),
(29, 4, 1),
(30, 1, 0),
(30, 2, 1),
(30, 3, 1),
(30, 4, 0),
(31, 1, 0),
(31, 2, 0),
(31, 3, 0),
(31, 4, 0),
(32, 1, 0),
(32, 2, 1),
(32, 3, 1),
(32, 4, 0),
(33, 1, 0),
(33, 2, 0),
(33, 3, 0),
(33, 4, 0);

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
-- Dumping data for table `rides`
--

INSERT INTO `rides` (`rideID`, `startDate`, `maxCapacity`, `details`, `nbPassengers`, `driverID`, `sourceID`, `destinationID`, `statusID`) VALUES
(27, '2023-03-23 13:45:00', 4, 'Willing to take detours to pick people up. Make sure you\'re on time.', 1, 4, 6, 1, 3),
(28, '2023-03-19 15:34:00', 6, 'Details of the ride that has already happened', 1, 4, 5, 1, 1),
(29, '2023-04-03 00:00:00', 5, 'Going from jbeil and taking a stop on the way', 0, 6, 7, 1, 1),
(30, '2023-03-20 17:56:00', 3, 'Leaving from zouk, and willing to pick people up around the area.', 0, 7, 2, 1, 4),
(31, '2023-03-19 17:58:00', 4, 'Leaving from jbeil, cannot take detours.', 0, 7, 7, 1, 4),
(32, '2023-03-27 18:00:00', 3, 'Leaving from Zouk willing to pick people up in the area.', 0, 7, 2, 1, 1),
(33, '2023-03-22 12:58:00', 3, 'Leaving from jbeil, won\'t take detours or wait for anyone.', 0, 7, 1, 7, 1);

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
-- Dumping data for table `users`
--

INSERT INTO `users` (`userID`, `userFirstName`, `userLastName`, `userName`, `userEmail`, `userPassword`, `userPhone`, `userDriverRating`, `userPassengerRating`) VALUES
(4, 'Elio', 'Khawand', 'ElioKh', 'elio.khawand@st.ul.edu.lb', '202cb962ac59075b964b07152d234b70', '76600134', 3, 0),
(5, 'Karim', 'Yazbeck', 'KarimY', 'karim.yazbeck@st.ul.edu.lb', '202cb962ac59075b964b07152d234b70', '03235987', 0, 0),
(6, 'ali', 'mor', 'AL', 'a@st.ul.edu.lb', '202cb962ac59075b964b07152d234b70', '03567678', 0, 0),
(7, 'mia', 'haddad', 'miah', 'm@st.ul.edu.lb', '202cb962ac59075b964b07152d234b70', '76333044', 0, 0);

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
  ADD KEY `rideID` (`rideID`),
  ADD KEY `fk_reviews_users_reviewerUserID` (`reviewerID`);

--
-- Indexes for table `ridecondition`
--
ALTER TABLE `ridecondition`
  ADD PRIMARY KEY (`rideID`,`conditionID`),
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
  MODIFY `requestID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `reviewID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `rides`
--
ALTER TABLE `rides`
  MODIFY `rideID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `statuses`
--
ALTER TABLE `statuses`
  MODIFY `statusID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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
  ADD CONSTRAINT `fk_reviews_users_reviewedUserID` FOREIGN KEY (`reviewedUserID`) REFERENCES `users` (`userID`),
  ADD CONSTRAINT `fk_reviews_users_reviewerUserID` FOREIGN KEY (`reviewerID`) REFERENCES `users` (`userID`);

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
