-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: containers-us-west-106.railway.app    Database: RMS
-- ------------------------------------------------------
-- Server version	8.1.0

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
-- Table structure for table `candidate_vacancy`
--

DROP TABLE IF EXISTS `candidate_vacancy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidate_vacancy` (
  `candidate_id` int NOT NULL,
  `vacancy_id` int NOT NULL,
  `apply_date` date DEFAULT NULL,
  `cv_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`candidate_id`,`vacancy_id`),
  KEY `FK5d4ltqq47eoof7a73l9n5gh06` (`vacancy_id`),
  CONSTRAINT `FK5d4ltqq47eoof7a73l9n5gh06` FOREIGN KEY (`vacancy_id`) REFERENCES `vacancy` (`id`),
  CONSTRAINT `FK8d9t5pxqxu51ilwtnah5h0qjl` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidate_vacancy`
--

LOCK TABLES `candidate_vacancy` WRITE;
/*!40000 ALTER TABLE `candidate_vacancy` DISABLE KEYS */;
INSERT INTO `candidate_vacancy` VALUES (2,1,'2023-08-04',12,'PENDING'),(7,1,'2023-08-04',30,'PENDING'),(7,2,'2023-08-04',30,'PENDING'),(7,3,'2023-08-05',30,'PENDING'),(7,11,'2023-08-05',30,'PENDING'),(7,12,'2023-08-05',30,'PENDING'),(9,3,'2023-08-05',60,'PENDING'),(9,9,'2023-08-05',60,'PENDING'),(9,11,'2023-08-05',60,'PENDING'),(10,3,'2023-08-05',61,'PENDING'),(11,3,'2023-08-05',73,'PENDING'),(12,3,'2023-08-05',74,'PENDING'),(13,3,'2023-08-05',75,'PENDING'),(13,9,'2023-08-05',75,'PENDING'),(13,11,'2023-08-05',75,'PENDING'),(13,12,'2023-08-05',75,'PENDING'),(14,3,'2023-08-05',76,'PENDING');
/*!40000 ALTER TABLE `candidate_vacancy` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06  8:56:23
