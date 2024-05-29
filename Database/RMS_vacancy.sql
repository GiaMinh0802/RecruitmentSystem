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
-- Table structure for table `vacancy`
--

DROP TABLE IF EXISTS `vacancy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vacancy` (
  `id` int NOT NULL AUTO_INCREMENT,
  `benefit` longtext,
  `description` longtext,
  `end_date` date DEFAULT NULL,
  `reference_information` longtext,
  `remaining_needed` int DEFAULT NULL,
  `requirements` longtext,
  `salary` float DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_needed` int DEFAULT NULL,
  `working_location` varchar(255) DEFAULT NULL,
  `position_id` int DEFAULT NULL,
  `recruiter_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK57dh2fsk2okd4j3hvv7w4skgd` (`position_id`),
  KEY `FKl7mbindky0mqh35ovhhyqyrac` (`recruiter_id`),
  CONSTRAINT `FK57dh2fsk2okd4j3hvv7w4skgd` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`),
  CONSTRAINT `FKl7mbindky0mqh35ovhhyqyrac` FOREIGN KEY (`recruiter_id`) REFERENCES `recruiter` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vacancy`
--

LOCK TABLES `vacancy` WRITE;
/*!40000 ALTER TABLE `vacancy` DISABLE KEYS */;
INSERT INTO `vacancy` VALUES (1,'Insurance ：Yes (full salary)\nProbation time: 100% month salary','- Write Frontend code on ReactJS/VueJS framework to interact with API, Back-end.\n- Deploy code on Server Test and Production.\n- System Optimization.\n- Write good, clean, and easily maintainable code.\n- Develop new features that satisfy users.\n- Program and ensure the feasibility of user interface designs.\n- Collect and analyze data, operations, and code snippets to predict and fix problems or issues that need improvement.\n- Participate in source code reviews to set goals and develop features that enhance user experience.','2023-08-30','Nếu bạn quan tâm, vui lòng liên hệ với chúng tôi! Ngoài vị trí này ra, chúng tôi con đề xuất cho bạn nhiều vị trí hấp dẫn, phù hợp khác!',1,'Gender：Any\nAge：24 ~ 32 years old\nEducation level：Graduated Universities\nLanguage：\nNo. of experienced year：At least 1.5 years\nKey experience：\n- 1.5+ years of experience with the full lifecycle of project development for web applications.\n- Experience working with Javascript/typescript and experience with ES6 syntax Solid knowledge of Javascript.\n- Fluent in building layout using HTML/SCSS/CSS.\n- Experience in working with front-end framework like ReactJS/Redux/Next or VueJS/Vuex/Nuxt.\n- Knowledge with Electronjs for building Desktop App.\n- Ability to read and understand English documents.\n- Good communication skills, strong problem-solving skills, team worker.\n- Very comfortable learning new technologies, tools, and platforms.\n- Highly motivated. Initiative and passion.',150000,'2023-08-04','0',1,'Khu công nghệ cao, Quận 9 Thành phố Hồ Chí Minh',2,1),(2,'Insurance ：Yes (full salary)\nProbation time: 100% month salary','- Write Frontend code on ReactJS/VueJS framework to interact with API, Back-end.\n- Deploy code on Server Test and Production.\n- System Optimization.\n- Write good, clean, and easily maintainable code.\n- Develop new features that satisfy users.\n- Program and ensure the feasibility of user interface designs.\n- Collect and analyze data, operations, and code snippets to predict and fix problems or issues that need improvement.\n- Participate in source code reviews to set goals and develop features that enhance user experience.','2023-08-13','Nếu bạn quan tâm, vui lòng liên hệ với chúng tôi! Ngoài vị trí này ra, chúng tôi con đề xuất cho bạn nhiều vị trí hấp dẫn, phù hợp khác!',5,'Gender：Any\nAge：24 ~ 32 years old\nEducation level：Graduated Universities\nLanguage：\nNo. of experienced year：At least 1.5 years\nKey experience：\n- 1.5+ years of experience with the full lifecycle of project development for web applications.\n- Experience working with Javascript/typescript and experience with ES6 syntax Solid knowledge of Javascript.\n- Fluent in building layout using HTML/SCSS/CSS.\n- Experience in working with front-end framework like ReactJS/Redux/Next or VueJS/Vuex/Nuxt.\n- Knowledge with Electronjs for building Desktop App.\n- Ability to read and understand English documents.\n- Good communication skills, strong problem-solving skills, team worker.\n- Very comfortable learning new technologies, tools, and platforms.\n- Highly motivated. Initiative and passion.',150000,'2023-07-24','0',5,'Khu công nghệ cao, Quận 9 Thành phố Hồ Chí Minh',2,1),(3,'Benefit','Description','2023-08-17','Reference',10,'Requirements',1500000,'2023-08-05','1',10,'Ho Chi Minh',5,3),(4,'789','123','2023-08-16','101112',10,'456',100000,'2023-08-15','0',10,'FPT',1,1),(5,'12','12','2023-08-14','12',12,'12',12,'2023-08-14','0',12,'12333',3,4),(6,'321','321','2023-08-08','321',321,'321',321,'2023-08-08','0',321,'321',3,1),(7,'123','123','2023-08-08','123',321,'123',123,'2023-08-17','0',321,'123',2,1),(8,'123','123','2023-08-18','123',321,'123',123,'2023-08-07','0',321,'123',3,1),(9,'Lương Net: 2220 USD - 2497 USD\nPhụ cấp nhà ở, suất ăn\nPhụ cấp giáo dục con cái, phụ cấp chuyên cần\nPhụ cấp đi lại trong - ngoài nước, thu hút dành cho NLĐ nước ngoài\nThưởng lễ tết, sinh nhật\nLương tháng 13\nThưởng bán niên, thưởng nóng, năng suất, ý tưởng\nTăng lương hàng năm ','Phát triển và duy trì các ứng dụng Web\nHoàn thành task và các assignment theo lịch trình hoặc yêu cầu\nTrao đổi, hỗ trợ với các thành viên và team leader về các vấn đề liên quan đến công việc\nCập nhật và báo cáo tiến độ, kết quả làm việc cho team leader, cấp quản lý \nĐóng góp ý kiến để hỗ trợ cho sự phát triển và cải thiện sản phẩm, dự án của Công ty','2023-08-22','1234',12,'Tốt nghiệp cao đẳng, đại học chuyên ngành CNTT\nNắm vững kiến thức nền tảng Javascript\nCó kinh nghiệm làm việc với Nodejs, ReactJs, AngularJS, MongoDB, MySQL\nTư duy logic tốt\nCó kinh nghiệm sử dụng Git, thiết kế hệ thống là một lợi thế\nKiến thức về giao diện người dùng, viết css, microservice là một lợi thế\nKhả năng giao tiếp tiếng anh là một lợi thế\nSẵn sàng, ham học hỏi kiến thức mới ',10000000,'2023-07-22','1',12,'Hà Nội',2,1),(10,'123','123','2023-08-17','123',5161156,'132',1456,'2023-08-07','0',5161156,'123',1,1),(11,'test','test','2023-08-31','test',10,'test',123,'2023-08-20','1',10,'HCM',3,5),(12,'Competitive salary\nOpportunities for professional development and growth\nCollaborative and inclusive work environment\nExciting and challenging projects with cutting-edge technologies','Develop and maintain software/library/API in Script Language (Node.js and others) by writing clean, efficient, and reusable code.\nContribute, maintain and optimize our backend infrastructure and APIs.\nCollaborate with other developers to create high-quality software/ library/ API.\nSupport the entire project lifecycle (inception, design, build, test, deploy, support documentation).\nIdentify and fix software defects and bugs through effective debugging and troubleshooting.','2023-09-12','123',2,'Proven experience as a Software Developer or similar role, working with complex systems.\n3-5 years of experience with Script Language programming.\nExperience with Web Assembly (WASM) and other languages is a plus.\nStrong knowledge of data structures and algorithms.\nEfficient at writing maintainable code.\nExperience in Rust is a plus\nExperience and used to git repos, CI/CD development environment\nExcellent in documentation of your work and your code.\nExperience with AML is a plus\nUnderstanding DevOps is a plus.\nExcellent problem-solving and analytical skills with keen attention to detail.\nMust be able to prioritize assigned development tasks and complete challenging projects on a very tight schedule\nMust be able to work independently with very little supervision with the ability to communicate and coordinate your work with a remote team.\nFluent in English\nBachelor\'s degree in Computer Science, Engineering, or a related field.',14000000,'2023-08-09','1',2,'Quận 1, Hồ Chí Minh',6,1),(13,'Competitive salary\nOpportunities for professional development and growth\nCollaborative and inclusive work environment\nExciting and challenging projects with cutting-edge technologies','Develop and maintain software/library/API in Script Language (Node.js and others) by writing clean, efficient, and reusable code.\nContribute, maintain and optimize our backend infrastructure and APIs.\nCollaborate with other developers to create high-quality software/ library/ API.\nSupport the entire project lifecycle (inception, design, build, test, deploy, support documentation).\nIdentify and fix software defects and bugs through effective debugging and troubleshooting.','2023-09-12','123',2,'Proven experience as a Software Developer or similar role, working with complex systems.\n3-5 years of experience with Script Language programming.\nExperience with Web Assembly (WASM) and other languages is a plus.\nStrong knowledge of data structures and algorithms.\nEfficient at writing maintainable code.\nExperience in Rust is a plus\nExperience and used to git repos, CI/CD development environment\nExcellent in documentation of your work and your code.\nExperience with AML is a plus\nUnderstanding DevOps is a plus.\nExcellent problem-solving and analytical skills with keen attention to detail.\nMust be able to prioritize assigned development tasks and complete challenging projects on a very tight schedule\nMust be able to work independently with very little supervision with the ability to communicate and coordinate your work with a remote team.\nFluent in English\nBachelor\'s degree in Computer Science, Engineering, or a related field.',14000000,'2023-08-09','1',2,'Quận 1, Hồ Chí Minh',6,1);
/*!40000 ALTER TABLE `vacancy` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-06  8:55:50
