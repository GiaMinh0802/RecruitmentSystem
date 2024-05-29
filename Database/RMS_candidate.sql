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
-- Table structure for table `candidate`
--

DROP TABLE IF EXISTS `candidate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `candidate` (
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
  UNIQUE KEY `UK_r09ojuqppptb5tf8f640kim17` (`account_id`),
  KEY `FK5q1ksqleh983c18yssyo2dn8l` (`address_id`),
  CONSTRAINT `FK5q1ksqleh983c18yssyo2dn8l` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FKj4889h0mbv3h6rbbxuuyoyame` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `candidate`
--

LOCK TABLES `candidate` WRITE;
/*!40000 ALTER TABLE `candidate` DISABLE KEYS */;
INSERT INTO `candidate` VALUES (1,NULL,'Candidate','QwO7ycPoJt','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,4,NULL),(2,NULL,'Lê','Minh','https://storage.googleapis.com/recruitment-cv-375eb.appspot.com/faWOnNgn-263498346_3200541133554779_8238027386307341084_n.jpg','',1,5,1),(3,NULL,'Candidate','LneqbF7bRP','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,6,NULL),(4,NULL,'Candidate','mLRzWywVde','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,7,NULL),(5,NULL,'Candidate','zoV3buZdu6','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,8,NULL),(6,NULL,'Candidate','stGpqQjcj6','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,9,NULL),(7,NULL,'Candidate','dQX0kHsDl2','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,10,NULL),(8,NULL,'Candidate','GioeXWttWD','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,12,NULL),(9,NULL,'Candidate','iYQ3vX4NzF','https://storage.googleapis.com/recruitment-cv-375eb.appspot.com/FehZy5GV-thay-giao-ba-to-chuc-giai-all-star-vi-mien-trung-1603172979.jpg',NULL,NULL,14,NULL),(10,NULL,'Candidate','BulCqcsHko','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,15,NULL),(11,NULL,'Candidate','h7pVLAAwCl','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,17,NULL),(12,NULL,'Candidate','dzfVkydUxc','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,18,NULL),(13,NULL,'Candidate','MbocS8gMFJ','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,19,NULL),(14,NULL,'Candidate','U5IN59TBIB','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,20,NULL),(15,NULL,'Candidate','bRk3xu1GVn','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,21,NULL),(16,NULL,'Candidate','V3halP1N5t','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,23,NULL),(17,NULL,'Candidate','EnQ7t5BKS9','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,24,NULL),(18,NULL,'Candidate','ZmyTrl9bWI','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,25,NULL),(19,NULL,'Candidate','F8XISwGJPC','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,26,NULL),(20,NULL,'Candidate','h1Xlgj2j1w','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,27,NULL),(21,NULL,'Candidate','VkHCwPAyHA','https://gamek.mediacdn.vn/133514250583805952/2021/6/24/photo-1-16245000005922003764148.jpg',NULL,NULL,28,NULL);
/*!40000 ALTER TABLE `candidate` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06  8:55:39
