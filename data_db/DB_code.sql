DROP DATABASE IF EXISTS unipi_agenda;
CREATE DATABASE IF NOT EXISTS unipi_agenda;
-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: unipi_agenda
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `meeting`
--

DROP TABLE IF EXISTS `meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting` (
  `id_meeting` int NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `description` text,
  `date` datetime NOT NULL,
  `duration` varchar(100) NOT NULL,
  `admin` varchar(30) NOT NULL,
  PRIMARY KEY (`id_meeting`),
  KEY `admin` (`admin`),
  CONSTRAINT `meeting_ibfk_1` FOREIGN KEY (`admin`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (3,'lalala3','testettstetsetwe','2021-07-18 21:38:19','13.5','prekwood'),(5,'meeting test3',NULL,'2022-09-12 09:30:00','12.6','vagman'),(7,'meeting test5',NULL,'2022-09-14 12:45:00','12.6','prekwood'),(9,'meeting test7',NULL,'2021-09-16 09:00:00','12.6','vagman'),(10,'meeting test  111',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(11,'la la la 15',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(12,'meeting test 120',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(13,'meeting test 121',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(14,'meeting test 122',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(15,'meeting test 132',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(16,'meeting test 132',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(21,'test123','lala','2021-05-07 00:00:00','30 min','Brinel'),(22,'Τρεξιμο','μπας και γινω καλυτερα test','2021-07-18 21:59:40','45 min','Brinel'),(23,'Test','123','2021-07-17 04:34:19','30 min','prekwood'),(24,'Test','tsetsetset','2021-07-18 09:39:56','1 hours 0 min','prekwood'),(25,'asd','asdasd','0013-08-13 12:00:00','1 hours 15 min','prekwood'),(26,'123123','123123','0014-10-12 12:00:00','1 hours 0 min','Brinel'),(27,'123','123 test asd\nasd asdasd','2022-08-07 12:00:00','30 min','Brinel'),(28,'123123123123123123','123123123','2021-07-18 09:57:27','1 hours 30 min','Brinel'),(29,'TESTESTEST','last test test ','2022-10-07 12:00:00','45 min','Brinel');
/*!40000 ALTER TABLE `meeting` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `meeting_changed_trigger` AFTER UPDATE ON `meeting` FOR EACH ROW BEGIN

    DECLARE msg VARCHAR(100);

    DECLARE count_notifications boolean;

#     check if the meeting has changed more than one time on the last 1 hour

    set count_notifications =

        (SELECT DISTINCT(count(id_notification)>0) as count_notifications

        FROM user_notification

        WHERE user_notification.id_meeting = OLD.id_meeting AND date > NOW() - INTERVAL 1 HOUR

        group by username);

    if count_notifications = 1 THEN

        set msg = ' changed the meeting more than one time in the last hour, check it out please';

        DELETE FROM user_notification

        WHERE user_notification.id_meeting=OLD.id_meeting AND

                date > NOW() - INTERVAL 1 HOUR;

        INSERT INTO user_notification(id_meeting, username, msg, date,viewed)

            (SELECT OLD.id_meeting, meeting_participants.username , concat(OLD.admin, msg), now() , false

             FROM (meeting_participants)

             WHERE (OLD.id_meeting = meeting_participants.id_meeting));

    ELSE

#       Detect the edited field

        IF (OLD.name <> NEW.name) THEN

            SET msg = ' changed the meeting name ';

        ELSEIF (OLD.description <> NEW.description) THEN

            set msg = ' changed the meeting description ';

        ELSEIF (OLD.date <> NEW.date) THEN

            set msg = ' changed the meeting date';

        ELSEIF (OLD.duration <> NEW.duration) THEN

            set msg = ' changed the meeting duration';

        ELSE

            set msg = ' changed the meeting';

        end if;

#         send a notification to all participants

        INSERT INTO user_notification(id_meeting, username, msg, date,viewed)

            (SELECT OLD.id_meeting ,meeting_participants.username , concat(OLD.admin ,msg), now() , false

             FROM (meeting_participants)

             WHERE (OLD.id_meeting = meeting_participants.id_meeting));



    end if;

    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `meeting_comments`
--

DROP TABLE IF EXISTS `meeting_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting_comments` (
  `id_meeting` int NOT NULL,
  `username` varchar(30) NOT NULL,
  `date` datetime NOT NULL,
  `msg` text,
  PRIMARY KEY (`id_meeting`,`username`,`date`),
  KEY `username_idx` (`username`),
  CONSTRAINT `username` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting_comments`
--

LOCK TABLES `meeting_comments` WRITE;
/*!40000 ALTER TABLE `meeting_comments` DISABLE KEYS */;
INSERT INTO `meeting_comments` VALUES (3,'Brinel','2021-07-16 23:34:41','asdasdasdasd'),(3,'prekwood','2021-07-18 21:37:59','hahah'),(22,'Brinel','2021-07-16 23:52:48','Γεια τι γινεται');
/*!40000 ALTER TABLE `meeting_comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting_participants`
--

DROP TABLE IF EXISTS `meeting_participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting_participants` (
  `id_meeting` int NOT NULL,
  `username` varchar(30) NOT NULL,
  `invitation_status` varchar(10) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id_meeting`,`username`),
  CONSTRAINT `meeting_participants_chk_1` CHECK ((`invitation_status` in (_utf8mb4'open',_utf8mb4'approved',_utf8mb4'declined')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting_participants`
--

LOCK TABLES `meeting_participants` WRITE;
/*!40000 ALTER TABLE `meeting_participants` DISABLE KEYS */;
INSERT INTO `meeting_participants` VALUES (0,'','open','2021-07-16 12:16:19'),(10,'prekwood','open',NULL),(10,'vagman','open',NULL),(11,'prekwood','open',NULL),(11,'vagman','open',NULL),(12,'prekwood','open',NULL),(12,'vagman','open',NULL),(13,'prekwood','open',NULL),(13,'vagman','open',NULL),(14,'vagman','open',NULL),(15,'prekwood','open',NULL),(15,'vagman','open',NULL),(22,'Brinel','declined','2021-07-17 16:34:19'),(24,'vagman','open','2021-07-18 21:39:56'),(25,'vagman','open','2021-07-18 21:40:52'),(27,'vagman','approved',NULL),(29,'prekwood','approved','2021-07-19 12:30:52'),(29,'vagman','approved','2021-07-19 12:30:52');
/*!40000 ALTER TABLE `meeting_participants` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `meeting_invitations_trigger` AFTER INSERT ON `meeting_participants` FOR EACH ROW INSERT INTO user_notification(username, msg, date,viewed)

        (SELECT meeting_participants.username,

                concat(meeting.admin,' sends you a invitation for the ',meeting.name),NOW(),false

            FROM meeting natural join meeting_participants

            WHERE id_meeting = NEW.id_meeting) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_notification`
--

DROP TABLE IF EXISTS `user_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_notification` (
  `id_notification` int NOT NULL AUTO_INCREMENT,
  `id_meeting` int DEFAULT NULL,
  `username` varchar(30) NOT NULL,
  `msg` text,
  `date` datetime DEFAULT NULL,
  `viewed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id_notification`),
  KEY `id_meeting` (`id_meeting`),
  CONSTRAINT `user_notification_ibfk_1` FOREIGN KEY (`id_meeting`) REFERENCES `meeting` (`id_meeting`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notification`
--

LOCK TABLES `user_notification` WRITE;
/*!40000 ALTER TABLE `user_notification` DISABLE KEYS */;
INSERT INTO `user_notification` VALUES (54,29,'prekwood','Brinel changed the meeting more than one time in the last hour, check it out please','2021-07-19 12:54:12',0),(55,29,'vagman','Brinel changed the meeting more than one time in the last hour, check it out please','2021-07-19 12:54:12',0);
/*!40000 ALTER TABLE `user_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(30) NOT NULL,
  `password` varchar(128) NOT NULL,
  `password_salt` varchar(11) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `color` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('Brinel','113bec15a0ed6309b845d420e0fc0db7','[B@5f7eaac9','Ilias','Brinias',NULL),('prekwood','113bec15a0ed6309b845d420e0fc0db7','[B@5f7eaac9','Nikos','Prekas',NULL),('vagman','113bec15a0ed6309b845d420e0fc0db7','[B@5f7eaac9','Vaggelis','Manousakis',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'unipi_agenda'
--

--
-- Dumping routines for database 'unipi_agenda'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-19 13:51:13
