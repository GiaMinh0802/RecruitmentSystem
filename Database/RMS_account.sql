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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd4vb66o896tay3yy52oqxr9w0` (`role_id`),
  CONSTRAINT `FKd4vb66o896tay3yy52oqxr9w0` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin@gmail.com',_binary '','$2a$10$U8F31NZqX72tdyBZH57c2.qlhTno97zeBEXrMaR4aFtAEZebVro/m',1),(2,'recruiter@gmail.com',_binary '','$2a$10$U8F31NZqX72tdyBZH57c2.qlhTno97zeBEXrMaR4aFtAEZebVro/m',2),(3,'interviewer@gmail.com',_binary '','$2a$10$U8F31NZqX72tdyBZH57c2.qlhTno97zeBEXrMaR4aFtAEZebVro/m',3),(4,'chepurnoy7777@ovmail.net',_binary '','$2a$10$ShK/8e2ZMNojmTmkiiCQ8O.fW26bXQnfoLsoGmY3E7PU17siC7Ceq',4),(5,'minhledang.hcm@gmail.com',_binary '','$2a$10$gmBSt80tmHW1NDoGWw.ENufChazVCGEx88iXfKAPSq9yyIvnNIUsK',4),(6,'khai482002gl@gmail.com',_binary '','$2a$10$EeEPU78j3a5pjI./G.kAm.ba0fUBlUTlxzKRwRFrSJ6HNUzOgSFx2',4),(7,'phamlinh141975@gmail.com',_binary '\0','$2a$10$OoCcOKT2XfGE2Jm0S9D2zu0uEmrw6doJHZPKXJqFr9oguBWhzrcUC',4),(8,'khai482002@gmail.com',_binary '\0','$2a$10$7DmXwMRQuwYDmrZWWe6iZekqQh33lXUe1MNX/8YEs817icFirsyuy',4),(9,'hoang.le2569@gmail.com',_binary '\0','$2a$10$O2Eg/V1iPe1h01aVPDDsheCSDmg80M2PYMaW2gv32L.Ob4j6UCuM6',4),(10,'hoang.le25699@gmail.com',_binary '','$2a$10$2YPb3eHSx0j2aMjwd4QXS.hDLJY/KJofBQmA2DyPvBokj4qw0hjve',4),(11,'trungnghia9a5@gmail.com',_binary '','$2a$10$Mp.7Kp2GHiptr69uab2iMOmc02Q/ahev/ljT8ihANkP4r17DhkHD.',2),(12,'trol088@sqmail.xyz',_binary '','$2a$10$ojMrIyNwuVsV5QXgBjfDPeNvdNhblx3qVu3aPe4GZr/1DtZL6rjcG',4),(13,'hoang.le99256@gmail.com',_binary '','$2a$10$kO6aBEamgmzn8W4dCGIVpu2iA50w63aumSqOLzlfYWu3P15RXzy4u',2),(14,'hoang.le2506.99@hcmut.edu.vn',_binary '','$2a$10$ZRxmOiV/o1PiLQV1wG88buEZ513qg2lplxz/eawwCXZhzJeySzMCC',4),(15,'fotexi2341@quipas.com',_binary '','$2a$10$ZI9WDwdcHQ/a1salUfKwXeAGP0XF58xYaPY2L/9TQaiRzcjchEH2m',4),(16,'htkiet1805@gmail.com',_binary '','$2a$10$6Hur2Y8qclUJ4WWXO8qEFudj2zAaxaYWw/IKq9uRIgaKGZnmcSlPG',2),(17,'riwone9356@naymedia.com',_binary '','$2a$10$l1FWvIbXI7f3Pjm0AtV6lO7w8DDvUMlB8YQm261XnCAa8gSQAKvp6',4),(18,'kelyhuve@tutuapp.bid',_binary '','$2a$10$ElrBZUhRqbTKfh72uGeveeIF8ky5CeTNHyt7vL9Vb3x4drdRRpJLG',4),(19,'z78grsrv1r@superblohey.com',_binary '','$2a$10$pNPYSUVHvVf9shfIxFifuuehGUwoUp5xcIme1/nPz6YgpFOgHKDpu',4),(20,'oxbun@mailto.plus',_binary '','$2a$10$jYrAdqlQ69RTvYnOMo4c7unaHqUQjJBDLP0QxHVYYnXxQMbxuTqNm',4),(21,'camnguyentqnk@gmail.com',_binary '\0','$2a$10$L.ii2lnRwzIE66P/sIrt8.gqlVmOTmLoaYaQgLk1I9NbjxQU9YepW',4),(22,'okehetnha@gmail.com',_binary '','$2a$10$43lrNQgdHTQwTgcGw4Z/leMirKoMXUZG7gL8Olwtpk7EArQSbYGpO',2),(23,'vogiaminh0802@gmail.com',_binary '','$2a$10$ZDpEsHu5wxKjdgv5o1DIy..wuxXxwXxtVlKo1GPS3gdysJQz49TSm',4),(24,'htkiet180502@gmail.com',_binary '\0','$2a$10$zUXznn9bV0gl9kIkDngZD.VknMISKnoYS3kqeRFCHUTyqe74.3RIa',4),(25,'20133041@student.hcmute.edu.vn',_binary '\0','$2a$10$zFdg0PClyxs230A7xJnuKuN0RVpDIywdjWyV1tXy2ICt1pAjqkjx6',4),(26,'nguyenhien212001baulam@gmail.com',_binary '\0','$2a$10$LlT0b.MVyE/ecV/R0WcKDuBHQKMqVlJDyMkqlTps9tsju8MECB6OO',4),(27,'trandinhnham0988929202@gmail.com',_binary '\0','$2a$10$VOePNh7s9vm2OMCWv0CcrOTHfy6xkj2O68JWoCGXt.8JX/N9zkte6',4),(28,'nguyenhien212001bl@gmail.com',_binary '\0','$2a$10$Jag8Ly9qb1V06xlkcwLbf.idiK2KMdH6uBUs8cQ5Jt4dfPYwQuv6u',4);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06  8:56:06
