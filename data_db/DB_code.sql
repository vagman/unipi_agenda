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
  `date` datetime NOT NULL,
  `duration` float NOT NULL,
  `admin` varchar(30) NOT NULL,
  PRIMARY KEY (`id_meeting`),
  KEY `admin` (`admin`),
  CONSTRAINT `meeting_ibfk_1` FOREIGN KEY (`admin`) REFERENCES `users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
INSERT INTO `meeting` VALUES (1,'meeting test','2021-05-24 19:53:00',12.6,'Brinel'),(2,'meeting test','2021-09-10 09:00:00',12.6,'Brinel'),(3,'meeting test1','2021-09-10 09:00:00',12.6,'prekwood'),(4,'meeting test2','2021-09-11 10:00:00',12.6,'Brinel'),(5,'meeting test3','2021-09-12 09:30:00',12.6,'vagman'),(6,'meeting test4','2021-09-13 11:00:00',12.6,'Brinel'),(7,'meeting test5','2021-09-14 12:45:00',12.6,'prekwood'),(8,'meeting test6','2021-09-15 09:00:00',12.6,'Brinel'),(9,'meeting test7','2021-09-16 09:00:00',12.6,'vagman'),(10,'meeting test  111','2100-09-26 10:00:00',12.5,'Brinel'),(11,'meeting test 110','2100-09-26 10:00:00',12.5,'Brinel'),(12,'meeting test 120','2100-09-26 10:00:00',12.5,'Brinel'),(13,'meeting test 121','2100-09-26 10:00:00',12.5,'Brinel'),(14,'meeting test 122','2100-09-26 10:00:00',12.5,'Brinel'),(15,'meeting test 132','2100-09-26 10:00:00',12.5,'Brinel'),(16,'meeting test 132','2100-09-26 10:00:00',12.5,'Brinel');
/*!40000 ALTER TABLE `meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting_comments`
--

DROP TABLE IF EXISTS `meeting_comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting_comments` (
  `id_meeting` int NOT NULL,
  `id_user` int NOT NULL,
  `date` datetime NOT NULL,
  `msg` text,
  PRIMARY KEY (`id_meeting`,`id_user`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting_comments`
--

LOCK TABLES `meeting_comments` WRITE;
/*!40000 ALTER TABLE `meeting_comments` DISABLE KEYS */;
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
  PRIMARY KEY (`id_meeting`,`username`),
  CONSTRAINT `meeting_participants_chk_1` CHECK ((`invitation_status` in (_utf8mb4'open',_utf8mb4'approved',_utf8mb4'declined')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting_participants`
--

LOCK TABLES `meeting_participants` WRITE;
/*!40000 ALTER TABLE `meeting_participants` DISABLE KEYS */;
INSERT INTO `meeting_participants` VALUES (3,'Brinel','approved'),(3,'vagman','approved'),(9,'Brinel','approved'),(10,'prekwood','open'),(10,'vagman','open'),(11,'prekwood','open'),(11,'vagman','open'),(12,'prekwood','open'),(12,'vagman','open'),(13,'prekwood','open'),(13,'vagman','open'),(14,'prekwood','open'),(14,'vagman','open'),(15,'prekwood','open'),(15,'vagman','open');
/*!40000 ALTER TABLE `meeting_participants` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_notification`
--

LOCK TABLES `user_notification` WRITE;
/*!40000 ALTER TABLE `user_notification` DISABLE KEYS */;
INSERT INTO `user_notification` VALUES (1,'Brinel','hello','2022-10-07 08:08:08',0),(2,'Brinel','hello2','2022-11-07 08:08:08',0),(3,'Brinel','hello2','2021-06-04 08:08:08',0);
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-04 19:13:41
