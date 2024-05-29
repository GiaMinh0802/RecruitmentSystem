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
-- Table structure for table `recruiter`
--

DROP TABLE IF EXISTS `recruiter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruiter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `birthday` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `link_avt` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `sex` int DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  `address_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hekwvdsbctq9xp975oupoy8h5` (`account_id`),
  KEY `FKgjc9e154olbmesrp7j52dw3n1` (`address_id`),
  CONSTRAINT `FKe0mecocgy2k9cewub2mo5xcf0` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKgjc9e154olbmesrp7j52dw3n1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruiter`
--

LOCK TABLES `recruiter` WRITE;
/*!40000 ALTER TABLE `recruiter` DISABLE KEYS */;
INSERT INTO `recruiter` VALUES (1,NULL,'General','Reccuiter','https://storage.googleapis.com/recruitment-cv-375eb.appspot.com/ygIAIKou-263498346_3200541133554779_8238027386307341084_n.jpg','',1,2,1),(2,NULL,'Recruiter','ydJq73cwGr','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,11,NULL),(3,NULL,'Recruiter','9hCt67ckhC','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,13,NULL),(4,NULL,'Recruiter','fPfLx6ewGD','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg','0964643875',NULL,16,1),(5,NULL,'Recruiter','XJdMOF6jWD','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,22,NULL);
/*!40000 ALTER TABLE `recruiter` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06  8:56:34
