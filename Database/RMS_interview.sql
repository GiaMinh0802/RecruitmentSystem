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
-- Table structure for table `interview`
--

DROP TABLE IF EXISTS `interview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interview` (
  `id` int NOT NULL AUTO_INCREMENT,
  `interview_datetime` datetime(6) DEFAULT NULL,
  `interviewer_score` float DEFAULT NULL,
  `interviewer_status` bit(1) DEFAULT NULL,
  `language_skill_score` float DEFAULT NULL,
  `link_meet` varchar(255) DEFAULT NULL,
  `recruiter_status` bit(1) DEFAULT NULL,
  `soft_skill_score` float DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `total_score` float DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  `candidate_id` int DEFAULT NULL,
  `vacancy_id` int DEFAULT NULL,
  `interviewer_id` int DEFAULT NULL,
  `recruiter_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1dbbnw2d2fnmk6fublcjkwjr6` (`candidate_id`,`vacancy_id`),
  KEY `FK5amdvskvlsj31qxv5aceawoye` (`interviewer_id`),
  KEY `FKaehscqyahewafrvlq1hpqfa5o` (`recruiter_id`),
  CONSTRAINT `FK5amdvskvlsj31qxv5aceawoye` FOREIGN KEY (`interviewer_id`) REFERENCES `interviewer` (`id`),
  CONSTRAINT `FKaehscqyahewafrvlq1hpqfa5o` FOREIGN KEY (`recruiter_id`) REFERENCES `recruiter` (`id`),
  CONSTRAINT `FKb6duos8i9c9d1i70xvhkkupth` FOREIGN KEY (`candidate_id`, `vacancy_id`) REFERENCES `candidate_vacancy` (`candidate_id`, `vacancy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interview`
--

LOCK TABLES `interview` WRITE;
/*!40000 ALTER TABLE `interview` DISABLE KEYS */;
INSERT INTO `interview` VALUES (1,'2023-08-06 15:00:00.000000',4.5,_binary '',7,NULL,_binary '',4,1,NULL,0,NULL,2,1,1,1),(2,'2023-08-30 11:12:00.000000',0,_binary '\0',10,NULL,_binary '',7,0,'Hú hú',0,NULL,7,2,1,1),(3,'2023-08-05 16:00:00.000000',7,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,7,3,1,3),(4,'2023-09-02 16:53:00.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,9,3,1,3),(5,'2023-09-02 23:59:00.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,10,3,1,3),(6,'2023-08-06 10:00:00.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,11,3,1,3),(7,'2023-08-06 10:00:00.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,12,3,1,3),(8,'2023-08-06 14:11:07.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,13,3,1,3),(10,'2023-09-05 14:37:34.000000',7.5,_binary '',0,NULL,_binary '\0',0,0,NULL,0,NULL,14,3,1,3),(11,'2023-08-07 08:54:20.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,9,11,1,5),(12,'2023-08-07 08:54:29.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,7,11,1,5),(13,'2023-08-09 21:51:54.000000',0,_binary '\0',10,NULL,_binary '',9,0,'very good !!',0,NULL,7,1,1,1),(14,'2023-08-30 11:00:00.000000',0,_binary '\0',0,NULL,_binary '\0',0,0,NULL,0,NULL,13,11,NULL,5);
/*!40000 ALTER TABLE `interview` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06  8:56:12
