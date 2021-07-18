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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (3,'lalala3',NULL,'2022-09-10 09:00:00','13.5','prekwood'),(5,'meeting test3',NULL,'2022-09-12 09:30:00','12.6','vagman'),(7,'meeting test5',NULL,'2022-09-14 12:45:00','12.6','prekwood'),(9,'meeting test7',NULL,'2021-09-16 09:00:00','12.6','vagman'),(10,'meeting test  111',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(11,'la la la 15',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(12,'meeting test 120',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(13,'meeting test 121',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(14,'meeting test 122',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(15,'meeting test 132',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(16,'meeting test 132',NULL,'2100-09-26 10:00:00','12.5','Brinel'),(21,'test123','lala','2021-05-07 00:00:00','30 min','Brinel'),(22,'Τρεξιμο','μπας και γινω καλυτερα','2022-08-07 00:00:00','45 min','Brinel'),(23,'Test','123','2021-07-17 04:34:19','30 min','prekwood');
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `meeting_changed_trigger` AFTER UPDATE ON `meeting` FOR EACH ROW INSERT INTO user_notification(username, msg, date,viewed)

                                                                                                                                              (SELECT meeting_participants.username ,concat(OLD.name ,' meeting is updated'), now() , false

                                                                                                                                               FROM (meeting_participants)

                                                                                                                                               WHERE (OLD.id_meeting = meeting_participants.id_meeting)) */;;
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
INSERT INTO `meeting_comments` VALUES (3,'Brinel','2021-07-16 23:34:41','asdasdasdasd'),(22,'Brinel','2021-07-16 23:52:48','Γεια τι γινεται');
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
INSERT INTO `meeting_participants` VALUES (0,'','open','2021-07-16 12:16:19'),(3,'vagman','approved',NULL),(10,'prekwood','open',NULL),(10,'vagman','open',NULL),(11,'prekwood','open',NULL),(11,'vagman','open',NULL),(12,'prekwood','open',NULL),(12,'vagman','open',NULL),(13,'prekwood','open',NULL),(13,'vagman','open',NULL),(14,'prekwood','open',NULL),(14,'vagman','open',NULL),(15,'prekwood','open',NULL),(15,'vagman','open',NULL),(22,'prekwood','open','2021-07-16 23:52:36'),(23,'Brinel','open','2021-07-17 16:34:19');
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
                                     `username` varchar(30) NOT NULL,
                                     `msg` text,
                                     `date` datetime DEFAULT NULL,
                                     `viewed` tinyint(1) DEFAULT NULL,
                                     PRIMARY KEY (`id_notification`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notification`
--

LOCK TABLES `user_notification` WRITE;
/*!40000 ALTER TABLE `user_notification` DISABLE KEYS */;
INSERT INTO `user_notification` VALUES (1,'Brinel','hello','2022-10-07 08:08:08',0),(2,'Brinel','hello2','2022-11-07 08:08:08',0),(3,'Brinel','hello2','2021-06-04 08:08:08',0),(4,'prekwood','lalala3 is updated','2021-07-07 18:34:40',0),(5,'vagman','lalala3 is updated','2021-07-07 18:34:40',0),(7,'prekwood','la la la 4 is updated','2021-07-07 18:38:06',0),(8,'vagman','la la la 4 is updated','2021-07-07 18:38:06',0),(9,'vagman','Brinel sends you a invitation for themeeting test','2021-07-08 16:07:14',0),(10,'Brinel','lalala3 meeting is updated','2021-07-08 17:58:22',0),(11,'vagman','lalala3 meeting is updated','2021-07-08 17:58:22',0),(13,'vagman','meeting test meeting is updated','2021-07-08 17:58:22',0),(14,'vagman','meeting test meeting is updated','2021-07-16 11:17:50',0),(15,'vagman','meeting test meeting is updated','2021-07-16 11:22:40',0);
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

-- Dump completed on 2021-07-18 20:32:30
