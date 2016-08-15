-- MySQL dump 10.13  Distrib 5.7.10, for Win64 (x86_64)
--
-- Host: localhost    Database: micropos
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,NULL,'\0',NULL,'Appetizers','A',0),(2,NULL,'\0',NULL,'Soups','T',0),(3,NULL,'\0',NULL,'Beverages','D',0),(4,NULL,'\0',NULL,'Chefs Specialties','H',0),(5,NULL,'\0',NULL,'Kids Menu','KID',0),(6,NULL,'\0',NULL,'Poultry','C',0),(7,NULL,'\0',NULL,'Seafood','S',0),(8,NULL,'\0',NULL,'Vegetables','V',0),(9,NULL,'\0',NULL,'Chop Suey','CS',0),(10,NULL,'\0',NULL,'Egg Foo Young','E',0),(11,NULL,'\0',NULL,'Fried Rice','FR',0),(12,NULL,'\0',NULL,'Chow Mein','CM',0),(13,NULL,'\0',NULL,'Lo Mein','LM',0),(14,NULL,'\0',NULL,'Pork','P',0),(15,NULL,'\0',NULL,'Beef','B',0),(16,NULL,'\0',NULL,'Luncheon Special','L',0),(17,NULL,'\0',NULL,'Sushi Appetizers','AP',0),(18,NULL,'\0',NULL,'Sashimi Combo Tray','SCT',0),(19,NULL,'\0',NULL,'Nigiri','N',0),(20,NULL,'\0',NULL,'Lunch Special Sushi','LSS',0),(21,NULL,'\0',NULL,'Lunch Box','LB',0),(22,NULL,'\0',NULL,'Sushi Roll','SR',0),(23,NULL,'\0','2016-08-13 15:53:46.985000','Mix Drinks','MIX',0),(24,NULL,'\0','2016-08-13 16:12:41.902000','Sauce','SAU',0),(25,NULL,'\0','2016-08-13 16:32:17.880000','Dinner Special','DS',0),(26,NULL,'\0','2016-08-13 17:01:13.071000','Combination Dinner','CD',0),(27,NULL,'\0','2016-08-13 17:21:27.452000','Sushi Special Roll','SP',0),(28,NULL,'\0','2016-08-13 17:24:14.473000','Spring Rolls','SR',0);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charge`
--

DROP TABLE IF EXISTS `charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `charge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT NULL,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge`
--

LOCK TABLES `charge` WRITE;
/*!40000 ALTER TABLE `charge` DISABLE KEYS */;
INSERT INTO `charge` VALUES (1,-0.10,NULL,'\0',NULL,'10% Off','-10%',0,0),(2,-0.20,NULL,'\0',NULL,'20% Off','-20%',0,0),(3,-0.30,NULL,'\0',NULL,'30% Off','-30%',0,0),(4,-0.40,NULL,'\0',NULL,'40% Off','-40%',0,0),(5,-0.50,NULL,'\0',NULL,'50% Off','-50%',0,0),(6,-50.00,NULL,'\0',NULL,'$50 Off','-$50',1,0),(7,-50.00,NULL,'\0',NULL,'$100 Off','-$50',1,0);
/*!40000 ALTER TABLE `charge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charge_entry`
--

DROP TABLE IF EXISTS `charge_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `charge_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `charge_id` bigint(20) DEFAULT NULL,
  `sales_order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ng2smai0sgdgkctfr24kou1s6` (`charge_id`),
  KEY `FK_66py7smny94v4m5fieodcrcwd` (`sales_order_id`),
  CONSTRAINT `FK_66py7smny94v4m5fieodcrcwd` FOREIGN KEY (`sales_order_id`) REFERENCES `sales_order` (`id`),
  CONSTRAINT `FK_ng2smai0sgdgkctfr24kou1s6` FOREIGN KEY (`charge_id`) REFERENCES `charge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge_entry`
--

LOCK TABLES `charge_entry` WRITE;
/*!40000 ALTER TABLE `charge_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `charge_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `charge_entry_record`
--

DROP TABLE IF EXISTS `charge_entry_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `charge_entry_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `charge_id` bigint(20) DEFAULT NULL,
  `sales_order_record_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_40lcn9b71ff2hbkgd8afa7f5t` (`charge_id`),
  KEY `FK_l9o0w8ptrvi5rt5hbdecyfvfy` (`sales_order_record_id`),
  CONSTRAINT `FK_40lcn9b71ff2hbkgd8afa7f5t` FOREIGN KEY (`charge_id`) REFERENCES `charge` (`id`),
  CONSTRAINT `FK_l9o0w8ptrvi5rt5hbdecyfvfy` FOREIGN KEY (`sales_order_record_id`) REFERENCES `sales_order_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge_entry_record`
--

LOCK TABLES `charge_entry_record` WRITE;
/*!40000 ALTER TABLE `charge_entry_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `charge_entry_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `previous_order` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,NULL,'\0',NULL,'WALK-IN','','----------','1 Tiny Sauce\n'),(2,NULL,'\0',NULL,'Marcia','Patton','8484970468',NULL),(3,NULL,'\0',NULL,'Blythe','Mcintyre','6071604771',NULL),(4,NULL,'\0',NULL,'Dieter','Anthony','9124508649',NULL),(5,NULL,'\0',NULL,'Dillon','Figueroa','5383005144',NULL),(6,NULL,'\0',NULL,'Regina','Woodward','7892042013',NULL),(7,NULL,'\0',NULL,'Shea','Montgomery','5438051213',NULL),(8,NULL,'\0',NULL,'Larissa','Larsen','1913574698',NULL),(9,NULL,'\0',NULL,'Imani','Gill','2536698995',NULL),(10,NULL,'\0',NULL,'Wesley','Pitts','8892511499',NULL),(11,NULL,'\0',NULL,'Tara','Powell','1418776365',NULL),(12,NULL,'\0',NULL,'Martin','Cruz','7491846596',NULL),(13,NULL,'\0',NULL,'Samuel','Lloyd','6891945178',NULL),(14,NULL,'\0',NULL,'Rigel','Gamble','3714879693',NULL),(15,NULL,'\0',NULL,'Glenna','Molina','6368170878',NULL),(16,NULL,'\0',NULL,'Gareth','Guy','5093213448',NULL),(17,NULL,'\0',NULL,'Jelani','Cross','8406301530',NULL),(18,NULL,'\0',NULL,'Lois','Delacruz','8287980932',NULL),(19,NULL,'\0',NULL,'Inez','Thompson','9553740360',NULL),(20,NULL,'\0',NULL,'Cameron','Shannon','8459002003',NULL),(21,NULL,'\0',NULL,'Cedric','Leon','8542057193',NULL),(22,NULL,'\0',NULL,'Halee','Henry','6278496706',NULL),(23,NULL,'\0',NULL,'Rebecca','Humphrey','5492557383',NULL),(24,NULL,'\0',NULL,'Vernon','Moon','3884389701',NULL),(25,NULL,'\0',NULL,'Jakeem','Mccarty','9391541250',NULL),(26,NULL,'\0',NULL,'Kiona','Davis','2969759640',NULL),(27,NULL,'\0',NULL,'Harlan','Cantrell','5955417274',NULL),(28,NULL,'\0',NULL,'Rigel','Robertson','5247761342',NULL),(29,NULL,'\0',NULL,'Astra','Morales','3991083393',NULL),(30,NULL,'\0',NULL,'William','Wilkins','6112989165',NULL),(31,NULL,'\0',NULL,'Amity','Hopper','1628440512',NULL),(32,NULL,'\0',NULL,'Vladimir','Gay','8034368092',NULL),(33,NULL,'\0',NULL,'Ella','Schroeder','9755178218',NULL),(34,NULL,'\0',NULL,'Ivory','Morales','8647706888',NULL),(35,NULL,'\0',NULL,'Lacey','Moss','1804057091',NULL),(36,NULL,'\0',NULL,'Brandon','Pennington','2675568146',NULL),(37,NULL,'\0',NULL,'Aline','Walls','7007171434',NULL),(38,NULL,'\0',NULL,'Christian','Berry','5625719852',NULL),(39,NULL,'\0',NULL,'Nathaniel','Diaz','2381985054',NULL),(40,NULL,'\0',NULL,'Clare','Leonard','7547954606',NULL),(41,NULL,'\0',NULL,'Cecilia','Vargas','6601538337',NULL),(42,NULL,'\0',NULL,'Nadine','Larson','6757520765',NULL),(43,NULL,'\0',NULL,'Ora','Stanton','5243548457',NULL),(44,NULL,'\0',NULL,'Tyler','Burt','8824231416',NULL),(45,NULL,'\0',NULL,'Dean','Reyes','2475051683',NULL),(46,NULL,'\0',NULL,'Odette','Conrad','1045115766',NULL),(47,NULL,'\0',NULL,'Adara','Sparks','3865893071',NULL),(48,NULL,'\0',NULL,'Chantale','Mathis','1206383006',NULL),(49,NULL,'\0',NULL,'Rowan','Woodard','4289516061',NULL),(50,NULL,'\0',NULL,'Leandra','Hansen','2055188546',NULL),(51,NULL,'\0',NULL,'Britanney','Butler','5576191320',NULL),(52,NULL,'\0',NULL,'Quyn','Kirk','3025073451',NULL),(53,NULL,'\0',NULL,'Hedda','Delacruz','5896752726',NULL),(54,NULL,'\0',NULL,'Carolyn','Osborne','5804103781',NULL),(55,NULL,'\0',NULL,'Cruz','Velazquez','4724410460',NULL),(56,NULL,'\0',NULL,'Cruz','Johnston','3687769718',NULL),(57,NULL,'\0',NULL,'Tamekah','White','4336076301',NULL),(58,NULL,'\0',NULL,'Odessa','Dodson','2088927208',NULL),(59,NULL,'\0',NULL,'Zoe','Schwartz','8224436812',NULL),(60,NULL,'\0',NULL,'Imani','Carney','7544086063',NULL),(61,NULL,'\0',NULL,'Tatum','Good','2061554741',NULL),(62,NULL,'\0',NULL,'Astra','Meadows','6487786780',NULL),(63,NULL,'\0',NULL,'Neve','Neal','9271806059',NULL),(64,NULL,'\0',NULL,'Thor','Gordon','8623289607',NULL),(65,NULL,'\0',NULL,'Jaime','Gutierrez','1791298975',NULL),(66,NULL,'\0',NULL,'Rae','Mathis','8508780947',NULL),(67,NULL,'\0',NULL,'Chandler','Francis','4862094887',NULL),(68,NULL,'\0',NULL,'Lucas','Turner','5536023865',NULL),(69,NULL,'\0',NULL,'Amal','Wright','3388432857',NULL),(70,NULL,'\0',NULL,'Henry','Bridges','7031489314',NULL),(71,NULL,'\0',NULL,'Tasha','Wall','8595124423',NULL),(72,NULL,'\0',NULL,'Aurora','Bell','6978075835',NULL),(73,NULL,'\0',NULL,'Hayfa','Blair','8863395459',NULL),(74,NULL,'\0',NULL,'Aileen','Patton','5951990899',NULL),(75,NULL,'\0',NULL,'Quinn','Owens','8751988400',NULL),(76,NULL,'\0',NULL,'Claudia','Cox','5435567982',NULL),(77,NULL,'\0',NULL,'Candice','Travis','7714852204',NULL),(78,NULL,'\0',NULL,'Moses','Macdonald','8598109594',NULL),(79,NULL,'\0',NULL,'Warren','Gates','9118540815',NULL),(80,NULL,'\0',NULL,'Dacey','Espinoza','4053012768',NULL),(81,NULL,'\0',NULL,'Hadassah','Simmons','6832499560',NULL),(82,NULL,'\0',NULL,'Jerome','Singleton','7161493763',NULL),(83,NULL,'\0',NULL,'Dawn','Williamson','7495665356',NULL),(84,NULL,'\0',NULL,'Rajah','Hooper','3678173582',NULL),(85,NULL,'\0',NULL,'Channing','Bell','9485462372',NULL),(86,NULL,'\0',NULL,'Akeem','Hess','8853124433',NULL),(87,NULL,'\0',NULL,'Troy','Joseph','7484460039',NULL),(88,NULL,'\0',NULL,'Chelsea','Glenn','1341142536',NULL),(89,NULL,'\0',NULL,'Ila','Vance','7161246690',NULL),(90,NULL,'\0',NULL,'Quamar','Vazquez','1898805053',NULL),(91,NULL,'\0',NULL,'Walker','Aguilar','8546696267',NULL),(92,NULL,'\0',NULL,'Garrison','English','5921070209',NULL),(93,NULL,'\0',NULL,'Paki','Roberson','1732657214',NULL),(94,NULL,'\0',NULL,'Brielle','Ballard','8336125811',NULL),(95,NULL,'\0',NULL,'Lee','Rogers','6046120889',NULL),(96,NULL,'\0',NULL,'Tatiana','Bates','3842122463',NULL),(97,NULL,'\0',NULL,'Cathleen','Marshall','2523622242',NULL),(98,NULL,'\0',NULL,'Justin','Emerson','1758123793',NULL),(99,NULL,'\0',NULL,'Blaze','Whitfield','5569776828',NULL),(100,NULL,'\0',NULL,'Brynn','Harris','5356387354',NULL),(101,NULL,'\0',NULL,'weng','','4491665',NULL),(102,NULL,'\0',NULL,'terry','tsai','3182908858','2 Kung Bo Beef\n'),(103,NULL,'\0',NULL,'terry','','','1 Soda\n'),(104,NULL,'\0',NULL,'','','3187873311','1 Vegetables Lo Mein\n'),(105,NULL,'\0',NULL,'','','3182908858','2 Egg Roll (1)\n1 Fried Won Ton (8)\n'),(106,NULL,'\0',NULL,'','','3182018889','4 Egg Roll (1)\n'),(107,NULL,'\0',NULL,'','','7873311','1 Roast Port Egg Foo Young\n'),(108,NULL,'\0',NULL,'jim','','',NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `pin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,NULL,'\0',NULL,'Guest','Guest','0000'),(2,NULL,'\0',NULL,'Terry','Tsai','1'),(3,NULL,'\0',NULL,'Chiao','Weng','2'),(4,NULL,'\0',NULL,'Ben','Wong','3'),(5,NULL,'\0',NULL,'Kun','Tsai','4'),(6,NULL,'\0',NULL,'Evelyn','Powers','5'),(7,NULL,'\0',NULL,'Serena','Randall','6'),(8,NULL,'\0',NULL,'Dominique','Contreras','7'),(9,NULL,'\0',NULL,'Leila','Walters','1981'),(10,NULL,'\0',NULL,'Ferdinand','Dorsey','2667'),(11,NULL,'\0',NULL,'Kuame','Clemons','8694'),(12,NULL,'\0',NULL,'Allegra','Cotton','4580'),(13,NULL,'\0',NULL,'Graiden','George','9826'),(14,NULL,'\0',NULL,'Stacy','Brock','6006'),(15,NULL,'\0',NULL,'Calvin','Fischer','8616'),(16,NULL,'\0',NULL,'Elliott','Hester','4294'),(17,NULL,'\0',NULL,'Alyssa','Barron','3098'),(18,NULL,'\0',NULL,'Hammett','Rosa','3824'),(19,NULL,'\0',NULL,'Megan','Sargent','3079'),(20,NULL,'\0',NULL,'Mallory','Gilmore','6190'),(21,NULL,'\0',NULL,'Jenette','Santos','5733'),(22,NULL,'\0',NULL,'Ezekiel','Avery','2042'),(23,NULL,'\0',NULL,'Lacy','Fernandez','6019'),(24,NULL,'\0',NULL,'Lillith','Morris','6970'),(25,NULL,'\0',NULL,'Maisie','Sherman','6690'),(26,NULL,'\0',NULL,'Jenette','Boone','5654'),(27,NULL,'\0',NULL,'Charity','Santos','2413'),(28,NULL,'\0',NULL,'Fitzgerald','Hammond','3018'),(29,NULL,'\0',NULL,'Alan','Riley','1574'),(30,NULL,'\0',NULL,'Hedy','Ross','7593'),(31,NULL,'\0',NULL,'Phyllis','Bright','2809'),(32,NULL,'\0',NULL,'Willa','Workman','2744'),(33,NULL,'\0',NULL,'Dolan','Kent','2099'),(34,NULL,'\0',NULL,'Otto','Reeves','9970'),(35,NULL,'\0',NULL,'Amela','Collier','7409'),(36,NULL,'\0',NULL,'Burke','Galloway','2791'),(37,NULL,'\0',NULL,'Pamela','Lowery','1879'),(38,NULL,'\0',NULL,'Sydney','Combs','2560'),(39,NULL,'\0',NULL,'Lana','Hendrix','6001'),(40,NULL,'\0',NULL,'Elmo','Morin','7360'),(41,NULL,'\0',NULL,'Sydnee','Lowe','4252'),(42,NULL,'\0',NULL,'Vivien','Parks','8077'),(43,NULL,'\0',NULL,'Mari','Conrad','6331'),(44,NULL,'\0',NULL,'Hayes','Whitley','3941'),(45,NULL,'\0',NULL,'Valentine','Bonner','4082'),(46,NULL,'\0',NULL,'Kalia','Sutton','9874'),(47,NULL,'\0',NULL,'Mariko','Andrews','3428'),(48,NULL,'\0',NULL,'Marvin','Whitley','8318'),(49,NULL,'\0',NULL,'Myra','Solis','9480'),(50,NULL,'\0',NULL,'Petra','Riley','4997'),(51,NULL,'\0',NULL,'Desiree','Mckay','7000'),(52,NULL,'\0',NULL,'Judith','Forbes','7364'),(53,NULL,'\0',NULL,'Madaline','Herrera','3297'),(54,NULL,'\0',NULL,'Nehru','Chavez','2994'),(55,NULL,'\0',NULL,'Yoshio','Bailey','2894'),(56,NULL,'\0',NULL,'Georgia','Hayden','7002'),(57,NULL,'\0',NULL,'Holmes','Dudley','3578'),(58,NULL,'\0',NULL,'Hedwig','Compton','3647'),(59,NULL,'\0',NULL,'Joseph','Albert','6933'),(60,NULL,'\0',NULL,'Ashely','Mcfarland','8179'),(61,NULL,'\0',NULL,'Cameran','Haney','1523'),(62,NULL,'\0',NULL,'Charde','Solomon','4478'),(63,NULL,'\0',NULL,'Blythe','Foster','4132'),(64,NULL,'\0',NULL,'Destiny','Rivas','2048'),(65,NULL,'\0',NULL,'Brynn','York','2136'),(66,NULL,'\0',NULL,'Reed','Cotton','3968'),(67,NULL,'\0',NULL,'David','Dixon','9361'),(68,NULL,'\0',NULL,'Garrett','Oneil','9850'),(69,NULL,'\0',NULL,'Ciaran','Wells','4037'),(70,NULL,'\0',NULL,'Zelda','Puckett','6245'),(71,NULL,'\0',NULL,'Gisela','Snider','5615'),(72,NULL,'\0',NULL,'Jaime','Deleon','3945'),(73,NULL,'\0',NULL,'India','Puckett','2094'),(74,NULL,'\0',NULL,'Ivan','Hyde','9095'),(75,NULL,'\0',NULL,'Francesca','Santana','7853'),(76,NULL,'\0',NULL,'Charity','Conner','3058'),(77,NULL,'\0',NULL,'Kieran','Oneill','6746'),(78,NULL,'\0',NULL,'Simon','Gibson','9973'),(79,NULL,'\0',NULL,'Dai','Nixon','6123'),(80,NULL,'\0',NULL,'Cain','Mckay','3944'),(81,NULL,'\0',NULL,'Keefe','Hoover','8496'),(82,NULL,'\0',NULL,'Samantha','Haynes','1533'),(83,NULL,'\0',NULL,'Whilemina','Irwin','5714'),(84,NULL,'\0',NULL,'Prescott','Torres','5995'),(85,NULL,'\0',NULL,'Tyrone','Bolton','4524'),(86,NULL,'\0',NULL,'Kai','Tran','4462'),(87,NULL,'\0',NULL,'Beatrice','Wong','3420'),(88,NULL,'\0',NULL,'Ima','Odonnell','6030'),(89,NULL,'\0',NULL,'Cole','Mercado','8782'),(90,NULL,'\0',NULL,'Cyrus','Maynard','6767'),(91,NULL,'\0',NULL,'Nasim','Barton','6619'),(92,NULL,'\0',NULL,'Len','Hopper','7604'),(93,NULL,'\0',NULL,'Eric','James','3778'),(94,NULL,'\0',NULL,'Christian','Rasmussen','3940'),(95,NULL,'\0',NULL,'Ferris','Stuart','1013'),(96,NULL,'\0',NULL,'Shafira','Chandler','9358'),(97,NULL,'\0',NULL,'Quemby','Yates','9578'),(98,NULL,'\0',NULL,'Daphne','Rosario','6278'),(99,NULL,'\0',NULL,'Geoffrey','Joyce','2969'),(100,NULL,'\0',NULL,'Lisandra','Gonzalez','1874');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_positions`
--

DROP TABLE IF EXISTS `employee_positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_positions` (
  `employees_id` bigint(20) NOT NULL,
  `positions_id` bigint(20) NOT NULL,
  KEY `FK_72095k598ua3dfmwsl71fx4x4` (`positions_id`),
  KEY `FK_ba4evmaos47im1dt4a3hpal2c` (`employees_id`),
  CONSTRAINT `FK_72095k598ua3dfmwsl71fx4x4` FOREIGN KEY (`positions_id`) REFERENCES `position` (`id`),
  CONSTRAINT `FK_ba4evmaos47im1dt4a3hpal2c` FOREIGN KEY (`employees_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_positions`
--

LOCK TABLES `employee_positions` WRITE;
/*!40000 ALTER TABLE `employee_positions` DISABLE KEYS */;
INSERT INTO `employee_positions` VALUES (2,1),(3,2),(4,2),(5,2),(6,3),(7,4),(8,5),(9,5),(10,5),(11,5),(12,5),(13,5),(14,5),(15,5),(16,5),(17,5),(18,5),(19,5),(20,5),(21,5),(22,5),(23,5),(24,5),(25,5),(26,5),(27,5),(28,5),(29,5),(30,5),(31,5),(32,5),(33,5),(34,5),(35,5),(36,5),(37,5),(38,5),(39,5),(40,5),(41,5),(42,5),(43,5),(44,5),(45,5),(46,5),(47,5),(48,5),(49,5),(50,5),(51,5),(52,5),(53,5),(54,5),(55,5),(56,5),(57,5),(58,5),(59,5),(60,5),(61,5),(62,5),(63,5),(64,5),(65,5),(66,5),(67,5),(68,5),(69,5),(70,5),(71,5),(72,5),(73,5),(74,5),(75,5),(76,5),(77,5),(78,5),(79,5),(80,5),(81,5),(82,5),(83,5),(84,5),(85,5),(86,5),(87,5),(88,5),(89,5),(90,5),(91,5),(92,5),(93,5),(94,5),(95,5),(96,5),(97,5),(98,5),(99,5),(100,5);
/*!40000 ALTER TABLE `employee_positions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_item`
--

DROP TABLE IF EXISTS `menu_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `taxed` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `FK_25ty62u8nyfcw1dwu6jj3efn4` (`category_id`),
  CONSTRAINT `FK_25ty62u8nyfcw1dwu6jj3efn4` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_item`
--

LOCK TABLES `menu_item` WRITE;
/*!40000 ALTER TABLE `menu_item` DISABLE KEYS */;
INSERT INTO `menu_item` VALUES (1,NULL,'\0',NULL,'Egg Roll (1)',1.65,'A1',0,1,''),(2,NULL,'\0',NULL,'Fried Won Ton (8)',3.25,'A2',0,1,''),(3,NULL,'\0',NULL,'Fried Crabmeat Won Ton w/ Cream Cheese (4)',4.75,'A3',0,1,''),(4,NULL,'\0',NULL,'Szechuan Spicy Won Ton',4.75,'A4',0,1,''),(5,NULL,'\0',NULL,'Fried Chicken Wing (6)',5.50,'A5',0,1,''),(6,NULL,'\0',NULL,'Teriyaki Beef (8)',5.75,'A6',0,1,''),(7,NULL,'\0',NULL,'Chicken Lettuce Wrap',8.95,'A7',0,1,''),(8,NULL,'\0',NULL,'Fried Dumpling (6)',5.95,'A10',0,1,''),(9,NULL,'\0',NULL,'Shrimp Toast (4)',5.50,'A9',0,1,''),(10,NULL,'\0',NULL,'Bo-Bo Platter',10.95,'A11',0,1,''),(11,NULL,'\0',NULL,'Salad (S)',1.50,'A12',0,1,''),(12,NULL,'\0',NULL,'Salad (L)',3.00,'A12',0,1,''),(13,NULL,'\0',NULL,'Hot & Sour Soup (S)',2.25,'T1',0,2,''),(14,NULL,'\0',NULL,'Hot & Sour Soup (L)',6.25,'T1',0,2,''),(15,NULL,'\0',NULL,'Won Ton Soup',6.25,'T2',0,2,''),(16,NULL,'\0',NULL,'Egg Drop Soup (S)',1.75,'T3',0,2,''),(17,NULL,'\0',NULL,'Egg Drop Soup (L)',4.75,'T3',0,2,''),(18,NULL,'\0',NULL,'Tom Yum Soup',6.95,'T4',0,2,''),(19,NULL,'\0',NULL,'Seafood Soup',7.50,'T5',0,2,''),(20,NULL,'\0',NULL,'House Special Soup',6.95,'T6',0,2,''),(21,NULL,'\0',NULL,'Soda',2.50,'D1',0,3,''),(22,NULL,'\0',NULL,'Coffee',2.50,'D2',0,3,''),(23,NULL,'\0',NULL,'Ice Tea',2.50,'D3',0,3,''),(24,NULL,'\0',NULL,'Hot Tea',2.50,'D4',0,3,''),(25,NULL,'\0',NULL,'Bowl of Lemon',0.90,'D5',0,3,''),(26,NULL,'\0',NULL,'Bowl of Sauce',0.90,'D6',0,3,''),(27,NULL,'\0',NULL,'Orange Beef',13.95,'H1',0,4,''),(28,NULL,'\0',NULL,'House Chicken',11.95,'H2',0,4,''),(29,NULL,'\0',NULL,'Perfect Shrimp w/ Chicken',13.95,'H3',0,4,''),(30,NULL,'\0',NULL,'Three Kinds w/ Spicy Sauce',13.95,'H4',0,4,''),(31,NULL,'\0',NULL,'Happy Family',13.95,'H5',0,4,''),(32,NULL,'\0',NULL,'Seafood Imperial',14.95,'H6',0,4,''),(33,NULL,'\0',NULL,'Jumbo Shrimp w/ Scallop',14.95,'H7',0,4,''),(34,NULL,'\0',NULL,'Rainbow Jumbo Shrimp',14.45,'H8',0,4,''),(35,NULL,'\0',NULL,'Crispy Fried Duck',13.95,'H9',0,4,''),(36,NULL,'\0',NULL,'Peking Duck',32.00,'H10',0,4,''),(37,NULL,'\0',NULL,'Orange Chicken',12.95,'H11',0,4,''),(38,NULL,'\0',NULL,'Chicken Nugget',5.25,'K1',0,5,''),(39,NULL,'\0',NULL,'Fried Chicken Wing (6)',5.75,'K2',0,5,''),(40,NULL,'\0',NULL,'French Fries',3.00,'K3',0,5,''),(41,NULL,'\0',NULL,'Chicken w/ Cashew Nuts',11.45,'C1',0,6,''),(42,NULL,'\0',NULL,'La Chi Chicken',11.45,'C2',0,6,''),(43,NULL,'\0',NULL,'Chicken w/ Asparagus',11.45,'C3',0,6,''),(44,NULL,'\0',NULL,'Sesame Chicken',11.45,'C4',0,6,''),(45,NULL,'\0',NULL,'Tiny Spicy Chicken',11.45,'C5',0,6,''),(46,NULL,'\0',NULL,'Kung Bo Chicken',11.45,'C6',0,6,''),(47,NULL,'\0',NULL,'Chicken w/ Garlic Sauce',11.45,'C7',0,6,''),(48,NULL,'\0',NULL,'Mandarin Chicken',11.45,'C8',0,6,''),(49,NULL,'\0',NULL,'Moo Goo Gai Pan',11.45,'C9',0,6,''),(50,NULL,'\0',NULL,'Chicken w/ Almond',11.45,'C10',0,6,''),(51,NULL,'\0',NULL,'Sweet & Sour Chicken',11.45,'C11',0,6,''),(52,NULL,'\0',NULL,'Lemon Chicken',11.45,'C12',0,6,''),(53,NULL,'\0',NULL,'Ginger Chicken',11.45,'C13',0,6,''),(54,NULL,'\0',NULL,'Chicken w/ Fresh Broccoli',11.45,'C14',0,6,''),(55,NULL,'\0',NULL,'Chicken w/ Black Bean Sauce',11.45,'C15',0,6,''),(56,NULL,'\0',NULL,'Fried Chicken w/ Almond',11.45,'C16',0,6,''),(57,NULL,'\0',NULL,'Curry Chicken',11.45,'C17',0,6,''),(58,NULL,'\0',NULL,'Moo Shu Chicken',11.45,'C18',0,6,''),(59,NULL,'\0',NULL,'Shredded Chicken w/ Szechuan Style',11.45,'C19',0,6,''),(60,NULL,'\0',NULL,'Fried Sesame Chicken',11.45,'C20',0,6,''),(61,NULL,'\0',NULL,'Mongolian Chicken',11.45,'C21',0,6,''),(62,NULL,'\0',NULL,'Sweet & Sour Shrimp',12.75,'S1',0,7,''),(63,NULL,'\0',NULL,'Kwoh Ba Shrimp',13.45,'S2',0,7,''),(64,NULL,'\0',NULL,'Snow Peas Shrimp',13.25,'S3',0,7,''),(65,NULL,'\0',NULL,'Kung Bo Shrimp',13.25,'S4',0,7,''),(66,NULL,'\0',NULL,'Wonderful Shrimp',13.75,'S5',0,7,''),(67,NULL,'\0',NULL,'Kan Saw Shrimp',13.75,'S6',0,7,''),(68,NULL,'\0',NULL,'Shrimp w/ Lobster Sauce',13.25,'S7',0,7,''),(69,NULL,'\0',NULL,'Peking Shrimp',13.25,'S8',0,7,''),(70,NULL,'\0',NULL,'Shrimp w/ Cashew Nuts',13.50,'S9',0,7,''),(71,NULL,'\0',NULL,'Imperial Shrimp',13.25,'S10',0,7,''),(72,NULL,'\0',NULL,'Shrimp w/ Black Bean Sauce',13.95,'S11',0,7,''),(73,NULL,'\0',NULL,'Shrimp w/ Garlic Sauce',13.25,'S12',0,7,''),(74,NULL,'\0',NULL,'Seafood Deluxe',14.95,'S13',0,7,''),(75,NULL,'\0',NULL,'Scallop w/ Garlic Sauce',15.75,'S14',0,7,''),(76,NULL,'\0',NULL,'Butterfly Shrimp',14.95,'S15',0,7,''),(77,NULL,'\0',NULL,'Tiny Spicy Shrimp',13.45,'S16',0,7,''),(78,NULL,'\0',NULL,'Moo Shu Vegetables',8.95,'V1',0,8,''),(79,NULL,'\0',NULL,'Vegetables Delight',7.45,'V2',0,8,''),(80,NULL,'\0',NULL,'Bamboo Shoots w/ BlackMushrooms',7.45,'V3',0,8,''),(81,NULL,'\0',NULL,'Broccoli w/ Garlic Sauce',7.45,'V4',0,8,''),(82,NULL,'\0',NULL,'Bean Curd Szechuan Style w/ Pork',8.25,'V5',0,8,''),(83,NULL,'\0',NULL,'Bean Curd w/ Black Mushrooms',9.95,'V6',0,8,''),(84,NULL,'\0',NULL,'Sauteed String Beans',8.45,'V7',0,8,''),(85,NULL,'\0',NULL,'Vegetable w/ Tofu',8.95,'V8',0,8,''),(86,NULL,'\0',NULL,'Eggplant w/ Garlic Sauce',8.25,'V9',0,8,''),(87,NULL,'\0',NULL,'Kung Bo Tofu',8.75,'V10',0,8,''),(88,NULL,'\0',NULL,'Grand Chop Suey',10.95,'CS1',0,9,''),(89,NULL,'\0',NULL,'Chicken Chop Suey',9.95,'CS2',0,9,''),(90,NULL,'\0',NULL,'Beef Chop Suey',9.95,'CS3',0,9,''),(91,NULL,'\0',NULL,'Shrimp Chop Suey',11.65,'CS4',0,9,''),(92,NULL,'\0',NULL,'Chicken Egg Foo Young',9.45,'E1',0,10,''),(93,NULL,'\0',NULL,'Shrimp Egg Foo Young',11.45,'E2',0,10,''),(94,NULL,'\0',NULL,'Roast Port Egg Foo Young',9.45,'E3',0,10,''),(95,NULL,'\0',NULL,'House Special Egg Foo Young',10.95,'E4',0,10,''),(96,NULL,'\0',NULL,'Vegetable Egg Foo Young',9.45,'E5',0,10,''),(97,NULL,'\0',NULL,'Chicken Fried Rice',7.25,'FR1',0,11,''),(98,NULL,'\0',NULL,'Roast Pork Fried Rice',7.25,'FR2',0,11,''),(99,NULL,'\0',NULL,'Beef Fried Rice',7.45,'FR3',0,11,''),(100,NULL,'\0',NULL,'Shrimp Fried Rice',9.35,'FR4',0,11,''),(101,NULL,'\0',NULL,'Vegetables Fried Rice',7.25,'FR5',0,11,''),(102,NULL,'\0',NULL,'House Special Fried Rice',9.35,'FR6',0,11,''),(103,NULL,'\0',NULL,'Yong Chow Fried Rice',9.35,'FR7',0,11,''),(104,NULL,'\0',NULL,'Chicken Chow Mein',7.75,'CM1',0,12,''),(105,NULL,'\0',NULL,'Beef Chow Mein',7.75,'CM2',0,12,''),(106,NULL,'\0',NULL,'Pork Chow Mein',7.25,'CM3',0,12,''),(107,NULL,'\0',NULL,'Shrimp Chow Mein',9.80,'CM4',0,12,''),(108,NULL,'\0',NULL,'House Chow Mein',9.25,'CM5',0,12,''),(109,NULL,'\0',NULL,'Beef Lo Mein',8.25,'LM1',0,13,''),(110,NULL,'\0',NULL,'Chicken Lo Mein',8.25,'LM2',0,13,''),(111,NULL,'\0',NULL,'Shrimp Lo Mein',9.85,'LM3',0,13,''),(112,NULL,'\0',NULL,'Roast Pork Lo Mein',7.75,'LM4',0,13,''),(113,NULL,'\0',NULL,'House Special Lo Mein',9.25,'LM5',0,13,''),(114,NULL,'\0',NULL,'Pan Fried Noodles',14.95,'LM6',0,13,''),(115,NULL,'\0',NULL,'Vegetables Lo Mein',7.75,'LM7',0,13,''),(116,NULL,'\0',NULL,'Plain Lo Mein',5.25,'LM8',0,13,''),(117,NULL,'\0',NULL,'House Special Noodle Soup',8.95,'LM9',0,13,''),(118,NULL,'\0',NULL,'House Special Pan Fried Rice Vermicelli',9.25,'LM10',0,13,''),(119,NULL,'\0',NULL,'Moo Shu Pork',9.95,'P1',0,14,''),(120,NULL,'\0',NULL,'Sweet & Sour Pork',9.95,'P2',0,14,''),(121,NULL,'\0',NULL,'Pork w/ Garlic Sauce',9.95,'P3',0,14,''),(122,NULL,'\0',NULL,'Twice Cooked Pork',9.95,'P4',0,14,''),(123,NULL,'\0',NULL,'Peking Style Pork',9.95,'P5',0,14,''),(124,NULL,'\0',NULL,'Hunan Spicy Pork',9.95,'P6',0,14,''),(125,NULL,'\0',NULL,'Mongolian Pork',9.95,'P7',0,14,''),(126,NULL,'\0',NULL,'Roast Pork w/ Vegetables',9.95,'P8',0,14,''),(127,NULL,'\0',NULL,'Family Style Pork',9.95,'P9',0,14,''),(128,NULL,'\0',NULL,'Moo Shu Beef',10.95,'B1',0,15,''),(129,NULL,'\0',NULL,'Snow Peas Beef',10.95,'B2',0,15,''),(130,NULL,'\0',NULL,'Kung Bo Beef',10.95,'B3',0,15,''),(131,NULL,'\0',NULL,'Yu Shang Beef',10.95,'B4',0,15,''),(132,NULL,'\0',NULL,'Green Pepper Beef',10.95,'B5',0,15,''),(133,NULL,'\0',NULL,'Mongolian Beef',10.95,'B6',0,15,''),(134,NULL,'\0',NULL,'Beef w/ Black Mushrooms',10.95,'B7',0,15,''),(135,NULL,'\0',NULL,'Emperor\'s Delight',10.95,'B8',0,15,''),(136,NULL,'\0',NULL,'Beef w/ Broccoli',10.95,'B9',0,15,''),(137,NULL,'\0',NULL,'Hunan Beef',10.95,'B10',0,15,''),(138,NULL,'\0',NULL,'Shredded Beef Szechuan Style',10.95,'B11',0,15,''),(139,NULL,'\0',NULL,'Chicken Chow Mein',8.26,'L1',0,16,''),(140,NULL,'\0',NULL,'Moo Goo Gai Pan',8.26,'L2',0,16,''),(141,NULL,'\0',NULL,'Sweet & Sour Chicken',8.26,'L3',0,16,''),(142,NULL,'\0',NULL,'Kung Bo Chicken',8.26,'L4',0,16,''),(143,NULL,'\0',NULL,'Tiny Spicy Chicken',8.26,'L5',0,16,''),(144,NULL,'\0',NULL,'Sweet & Sour Pork',8.26,'L6',0,16,''),(145,NULL,'\0',NULL,'Pork w/ Garlic Sauce',8.26,'L7',0,16,''),(146,NULL,'\0',NULL,'Twice Cooked Pork',8.26,'L8',0,16,''),(147,NULL,'\0',NULL,'Family Style Pork',8.26,'L9',0,16,''),(148,NULL,'\0',NULL,'Beef or Chicken w/ Broccoli',8.26,'L10',0,16,''),(149,NULL,'\0',NULL,'Beef w/ Green Peppers',8.26,'L11',0,16,''),(150,NULL,'\0',NULL,'Mongolian Beef',8.26,'L12',0,16,''),(151,NULL,'\0',NULL,'Shrimp w/ Lobster Sauce',8.26,'L13',0,16,''),(152,NULL,'\0',NULL,'Kung Bo Shrimp',8.26,'L14',0,16,''),(153,NULL,'\0',NULL,'Roast Pork Egg Foo Young',8.26,'L15',0,16,''),(154,NULL,'\0',NULL,'Roast Pork or Chicken Lo Mein',8.26,'L16',0,16,''),(155,NULL,'\0',NULL,'House Special Noodle Soup',8.26,'L17',0,16,''),(156,NULL,'\0',NULL,'Almond Chicken',8.26,'L18',0,16,''),(157,NULL,'\0',NULL,'Vegetable Delight',8.26,'L19',0,16,''),(158,NULL,'\0',NULL,'Shrimp or Chicken Chop Suey',8.26,'L20',0,16,''),(159,NULL,'\0',NULL,'Sweet & Sour Shrimp',8.26,'L21',0,16,''),(160,NULL,'\0',NULL,'Fish w/ Vegetables',8.26,'L22',0,16,''),(161,NULL,'\0',NULL,'Kan Saw Fish',8.26,'L23',0,16,''),(162,NULL,'\0',NULL,'Sauteed Chicken or Beef Szechuan Style',8.26,'L24',0,16,''),(163,NULL,'\0',NULL,'Kung Bo Tofu',8.26,'L25',0,16,''),(164,NULL,'\0',NULL,'Curry Chicken',8.26,'L26',0,16,''),(165,NULL,'\0',NULL,'Sauteed Chicken with String Beans',8.26,'L27',0,16,''),(166,NULL,'\0',NULL,'Edamame',3.95,'AP1',0,17,''),(167,NULL,'\0',NULL,'Seaweed Salad',3.95,'AP2',0,17,''),(168,NULL,'\0',NULL,'Squid Salad',4.95,'AP3',0,17,''),(169,NULL,'\0',NULL,'Crabmeat Salad',5.50,'AP4',0,17,''),(170,NULL,'\0',NULL,'Crabmeat Cucumber Salad',4.50,'AP5',0,17,''),(171,NULL,'\0',NULL,'Japanese Dumpling',3.95,'AP6',0,17,''),(172,NULL,'\0',NULL,'Seafood Sunomono',5.00,'AP7',0,17,''),(173,NULL,'\0',NULL,'Shrimp Tempura',7.00,'AP8',0,17,''),(174,NULL,'\0',NULL,'Sashimi Red Snapper Tuna & Salmon (7)',12.50,'AP9',0,17,''),(175,NULL,'\0',NULL,'Sashimi Salmon (7)',11.95,'AP10',0,17,''),(176,NULL,'\0',NULL,'Sashimi Tuna (7)',14.95,'AP11',0,17,''),(177,NULL,'\0',NULL,'Sashimi (7) & California Roll (6)',16.50,'SCA',0,18,''),(178,NULL,'\0',NULL,'Sashimi (10) & Spicy Tuna Roll (6)',23.00,'SCB',0,18,''),(179,NULL,'\0',NULL,'Sashimi Deluxe (23)',38.00,'SCC',0,18,''),(180,NULL,'\0',NULL,'Crab Stick (Kani)',3.00,'N2',0,19,''),(181,NULL,'\0',NULL,'Eel (Unagi)',4.50,'N3',0,19,''),(182,NULL,'\0',NULL,'Egg (Tamago)',3.00,'N4',0,19,''),(183,NULL,'\0',NULL,'Octopus (Take)',4.50,'N5',0,19,''),(184,NULL,'\0',NULL,'Squid (lka)',4.00,'N6',0,19,''),(185,NULL,'\0',NULL,'Scallop (Hotategal)',3.50,'N7',0,19,''),(186,NULL,'\0',NULL,'Shrimp (Ebi)',3.00,'N8',0,19,''),(187,NULL,'\0',NULL,'Red Snapper (Tail)',3.00,'N9',0,19,''),(188,NULL,'\0',NULL,'Smoked Salmon',4.00,'N10',0,19,''),(189,NULL,'\0',NULL,'Salmon (Sake)',3.50,'N11',0,19,''),(190,NULL,'\0',NULL,'White Tuna',3.50,'N12',0,19,''),(191,NULL,'\0',NULL,'Tuna (Maguro)',5.50,'N13',0,19,''),(192,NULL,'\0',NULL,'Flying Fish Egg (Massago)',4.00,'N15',0,19,''),(193,NULL,'\0',NULL,'Yellow Tail (Hamachi)',5.50,'N16',0,19,''),(194,NULL,'\0',NULL,'Salmon Skin Handroll',3.50,'HR17',0,19,''),(195,NULL,'\0',NULL,'California Roll & Sashimi (5)',8.95,'LS1',0,20,''),(196,NULL,'\0',NULL,'Futo Maki & Shrimp Tempura Roll',8.95,'LS2',0,20,''),(197,NULL,'\0',NULL,'Lunch Box A',8.95,'LBA',0,21,''),(198,NULL,'\0',NULL,'Lunch Box B',8.95,'LBB',0,21,''),(199,NULL,'\0',NULL,'Lunch Box C',8.95,'LBC',0,21,''),(200,NULL,'\0',NULL,'Tuna Roll',4.50,'SR1',0,22,''),(201,NULL,'\0',NULL,'Salmon Roll',4.25,'SR2',0,22,''),(202,NULL,'\0',NULL,'Eel Roll',5.95,'SR3',0,22,''),(203,NULL,'\0',NULL,'California Roll',3.95,'SR4',0,22,''),(204,NULL,'\0',NULL,'Red Dragon Roll (Tuna)',13.95,'SR5',0,22,''),(205,NULL,'\0',NULL,'Spicy Tuna Roll',5.95,'SR6',0,22,''),(206,NULL,'\0',NULL,'Spicy Salmon Roll',5.25,'SR7',0,22,''),(207,NULL,'\0',NULL,'Futo Maki Roll',5.95,'SR8',0,22,''),(208,NULL,'\0',NULL,'Shrimp Tempura Roll',6.50,'SR9',0,22,''),(209,NULL,'\0',NULL,'Spider Roll',7.50,'SR10',0,22,''),(210,NULL,'\0',NULL,'Philadelphia Roll',6.00,'SR11',0,22,''),(211,NULL,'\0',NULL,'Crunch Roll',8.75,'SR12',0,22,''),(212,NULL,'\0',NULL,'Fuji Maki',9.75,'SR13',0,22,''),(213,NULL,'\0',NULL,'Fan Ka Zan Roll (Volcano Roll)',9.75,'SR14',0,22,''),(214,NULL,'\0',NULL,'Rainbow Roll',11.95,'SR15',0,22,''),(215,NULL,'\0',NULL,'Black Dragon Roll',14.50,'SR16',0,22,''),(216,NULL,'\0',NULL,'Soybean Rock \'n Roll',13.95,'SR17',0,22,''),(217,NULL,'\0',NULL,'Yama Roll',13.95,'SR18',0,22,''),(218,NULL,'\0',NULL,'Smoked Salmon Roll',5.50,'SR19',0,22,''),(219,NULL,'\0',NULL,'Chef Special Roll',13.95,'SR20',0,22,''),(220,NULL,'\0',NULL,'Snow Crab Roll',6.00,'SR21',0,22,''),(221,NULL,'\0',NULL,'Spicy Cucumber Wrap Roll',10.95,'SR22',0,22,''),(222,NULL,'\0',NULL,'Wok Special Roll',12.95,'SR23',0,22,''),(223,NULL,'\0',NULL,'Tiger Eye Roll',14.50,'SR24',0,22,''),(224,NULL,'\0',NULL,'Hawaiian Roll',12.95,'SR25',0,22,''),(225,NULL,'\0',NULL,'Tsunami Roll',12.95,'SR26',0,22,''),(226,NULL,'\0',NULL,'Crater Roll',12.95,'SR27',0,22,''),(227,NULL,'\0',NULL,'Golden Dragon Roll',11.95,'SR28',0,22,''),(228,NULL,'\0',NULL,'Cucumber Roll',3.95,'SR29',0,22,''),(229,NULL,'\0','2016-05-31 13:50:24.483000','Gift Card $10',10.00,'GC10',0,1,'\0'),(230,NULL,'\0',NULL,'Pineapple Plantation',7.75,'MIX1',0,23,''),(231,NULL,'\0','2016-08-13 16:01:04.700000','Headhunter',8.75,'MIX2',0,23,''),(232,NULL,'\0','2016-08-13 16:01:25.217000','Suffering Bastard',7.75,'MIX3',0,23,''),(233,NULL,'\0','2016-08-13 16:01:41.646000','Pina Colada',7.75,'MIX4',0,23,''),(234,NULL,'\0','2016-08-13 16:01:52.528000','Scorpion',7.75,'MIX5',0,23,''),(235,NULL,'\0','2016-08-13 16:02:03.746000','Blue Hawaii',7.75,'MIX6',0,23,''),(236,NULL,'\0','2016-08-13 16:02:20.932000','Mai Tai',7.75,'MIX7',0,23,''),(237,NULL,'\0','2016-08-13 16:02:32.279000','Planter\'s Punch',7.75,'MIX8',0,23,''),(238,NULL,'\0','2016-08-13 16:02:52.854000','Frozen Daiquiri',7.75,'MIX9',0,23,''),(239,NULL,'\0','2016-08-13 16:03:14.470000','Navy Grog',7.75,'MIX10',0,23,''),(240,NULL,'\0','2016-08-13 16:03:28.387000','Fog Cutter',7.75,'MIX11',0,23,''),(241,NULL,'\0','2016-08-13 16:03:39.975000','Zombie',7.75,'MIX12',0,23,''),(242,NULL,'\0','2016-08-13 16:03:51.898000','Flaming Volcano',11.50,'MIX13',0,23,''),(243,NULL,'\0','2016-08-13 16:04:03.561000','Singapore Sling',7.75,'MIX14',0,23,''),(244,NULL,'\0','2016-08-13 16:04:18.538000','Bar Drink',4.95,'MIX15',0,23,''),(245,NULL,'\0','2016-08-13 16:04:31.721000','Call Drink',6.25,'MIX16',0,23,''),(246,NULL,'\0','2016-08-13 16:04:43.517000','Sake',6.75,'MIX17',0,23,''),(247,NULL,'\0','2016-08-13 16:05:59.083000','Wine',7.75,'MIX18',0,23,''),(248,NULL,'\0','2016-08-13 16:06:16.257000','Plum Wine',8.75,'MIX19',0,23,''),(249,NULL,'\0',NULL,'Strawberry Punch',4.25,'D7',0,3,''),(250,NULL,'\0',NULL,'Pineapple Punch',4.25,'D8',0,3,''),(251,NULL,'\0',NULL,'Coconut Punch',4.25,'D9',0,3,''),(252,NULL,'\0',NULL,'Hawaiian Punch',4.25,'D10',0,3,''),(253,NULL,'\0',NULL,'Tiny Sauce',1.00,'SAU1',0,24,''),(254,NULL,'\0',NULL,'Orange Sauce',1.00,'SAU2',0,24,''),(255,NULL,'\0',NULL,'Brown Sauce',1.00,'SAU3',0,24,''),(256,NULL,'\0',NULL,'Garlic Sauce',1.00,'SAU4',0,24,''),(257,NULL,'\0',NULL,'Sweet & Sour Sauce',1.00,'SAU5',0,24,''),(258,NULL,'\0','2016-08-13 16:21:54.589000','Hunan Sauce',1.00,'SAU6',0,24,''),(259,NULL,'\0','2016-08-13 16:22:12.194000','Hot Sauce',1.00,'SAU7',0,24,''),(260,NULL,'\0','2016-08-13 16:22:34.236000','Tartar Sauce',1.00,'SAU8',0,24,''),(261,NULL,'\0','2016-08-13 16:22:47.164000','Hot Mustard',1.00,'SAU9',0,24,''),(262,NULL,'\0','2016-08-13 16:22:59.860000','Salad Dressing',1.00,'SAU10',0,24,''),(263,NULL,'\0','2016-08-13 16:23:10.651000','Mayo',1.00,'SAU11',0,24,''),(264,NULL,'\0','2016-08-13 16:23:22.170000','Eel Sauce',1.00,'SAU12',0,24,''),(265,NULL,'\0','2016-08-13 16:23:37.254000','Chef Roll Sauce',1.00,'SAU13',0,24,''),(266,NULL,'\0','2016-08-13 16:24:01.592000','Soy Bean Wrap',1.00,'SAU14',0,24,''),(267,NULL,'\0','2016-08-13 16:24:15.181000','Cream Cheese',1.00,'SAU15',0,24,''),(268,NULL,'\0','2016-08-13 16:27:47.332000','Shrimp Lettuce Wrap',9.50,'A8',0,1,''),(269,NULL,'\0','2016-08-13 16:30:47.409000','Crabmeat Asparagus Soup',7.50,'T7',0,2,''),(270,NULL,'\0','2016-08-13 16:35:08.353000','Moo Goo Gai Pan',13.95,'DS1',0,25,''),(271,NULL,'\0','2016-08-13 16:35:33.179000','Sweet & Sour Pork',13.95,'DS2',0,25,''),(272,NULL,'\0','2016-08-13 16:35:55.865000','Beef with Green Pepper',13.95,'DS3',0,25,''),(273,NULL,'\0','2016-08-13 16:36:19.064000','Shrimp with Lobster Sauce',13.95,'DS4',0,25,''),(274,NULL,'\0','2016-08-13 16:36:39.201000','Shrimp with Garlic Sauce',13.95,'DS5',0,25,''),(275,NULL,'\0','2016-08-13 16:36:57.961000','Tiny Spicy Chicken',13.95,'DS6',0,25,''),(276,NULL,'\0','2016-08-13 16:37:12.615000','Hunan Beef',13.95,'DS7',0,25,''),(277,NULL,'\0','2016-08-13 16:37:47.941000','Shredded Beef Szechuan Style',13.95,'DS8',0,25,''),(278,NULL,'\0','2016-08-13 16:38:08.431000','Fried Chicken with Almond',13.95,'DS9',0,25,''),(279,NULL,'\0','2016-08-13 16:38:45.872000','Mongolian Pork',13.95,'DS10',0,25,''),(280,NULL,'\0','2016-08-13 16:42:15.368000','Hunan Spicy Fish',13.25,'H12',0,4,''),(281,NULL,'\0','2016-08-13 16:42:53.518000','Blacken Steak Sizzling',14.95,'H13',0,4,''),(282,NULL,'\0','2016-08-13 16:43:11.268000','Salt Toast Scallop',15.75,'H14',0,4,''),(283,NULL,'\0','2016-08-13 16:43:37.029000','Seafood Bird Nest',15.95,'H15',0,4,''),(284,NULL,'\0','2016-08-13 16:46:14.152000','Chicken w/ String Beans',11.45,'C22',0,6,''),(285,NULL,'\0','2016-08-13 16:48:13.737000','Beef w/ Asparagus',10.95,'B12',0,15,''),(286,NULL,'\0','2016-08-13 16:50:36.288000','Shrimp w/ Walnut',13.95,'S17',0,7,''),(287,NULL,'\0','2016-08-13 16:55:08.245000','Salmon Fried Rice',9.35,'FR8',0,11,''),(288,NULL,'\0','2016-08-13 16:55:29.438000','Plain Fried Rice',4.50,'FR9',0,11,''),(289,NULL,'\0','2016-08-13 16:55:46.469000','Egg Fried Rice',5.00,'FR10',0,11,''),(290,NULL,'\0','2016-08-13 16:58:01.279000','Singapore Style Vermicelli',9.25,'LM11',0,13,''),(291,NULL,'\0','2016-08-13 16:58:26.785000','Seafood Pan Fried Egg Noodle',14.95,'LM12',0,13,''),(292,NULL,'\0','2016-08-13 16:58:50.938000','Pad Thai',9.25,'LM13',0,13,''),(293,NULL,'\0','2016-08-13 16:59:08.782000','Wonton Noodle Soup',8.95,'LM14',0,13,''),(294,NULL,'\0','2016-08-13 16:59:29.099000','Shrimp Tempura Udon Soup',8.95,'LM15',0,13,''),(295,NULL,'\0',NULL,'Combination A',9.75,'CDA',0,26,''),(296,NULL,'\0',NULL,'Combination B',10.75,'CDB',0,26,''),(297,NULL,'\0',NULL,'Combination C',11.75,'CDC',0,26,''),(298,NULL,'\0','2016-08-13 17:05:37.637000','Hawaii Salmon',8.26,'L28',0,16,''),(299,NULL,'\0',NULL,'Domestic Beer',3.00,'D11',0,3,''),(301,NULL,'\0',NULL,'Imported Beer',3.75,'D12',0,3,''),(302,NULL,'\0',NULL,'Japanese Imported Beer',3.75,'D13',0,3,''),(303,NULL,'\0','2016-08-13 17:12:27.973000','Age Tofu',4.50,'AP12',0,17,''),(304,NULL,'\0','2016-08-13 17:13:27.639000','Spicy Tuna & Shrimp Chips',10.95,'AP13',0,17,''),(305,NULL,'\0','2016-08-13 17:18:51.309000','Salmon Skin Handroll',3.50,'HR17',0,19,''),(306,NULL,'\0','2016-08-13 17:19:20.345000','Cup of Ginger',3.50,'HR18',0,19,''),(307,NULL,'\0','2016-08-13 17:22:00.136000','Zomi Soy Bean Roll',14.50,'SP1',0,27,''),(308,NULL,'\0','2016-08-13 17:22:16.317000','Living Color Roll',14.25,'SP2',0,27,''),(309,NULL,'\0','2016-08-13 17:22:37.981000','Zolia Rock\'n Roll',14.50,'SP3',0,27,''),(310,NULL,'\0','2016-08-13 17:22:57.256000','John Bawl Special Roll',14.50,'SP4',0,27,''),(311,NULL,'\0','2016-08-13 17:23:16.509000','Zotoang Mango Roll',14.50,'SP5',0,27,''),(312,NULL,'\0','2016-08-13 17:23:35.177000','Dynamite Roll',11.25,'SP6',0,27,''),(313,NULL,'\0','2016-08-13 17:23:56.334000','Zomi Special Roll',14.50,'SP7',0,27,''),(314,NULL,'\0','2016-08-13 17:24:50.717000','Shrimp Spring Roll',3.99,'SRA',0,28,''),(315,NULL,'\0','2016-08-13 17:25:17.171000','Zomi Spring Roll',3.99,'SRB',0,28,''),(316,NULL,'\0','2016-08-13 17:25:35.709000','Veggie Spring Roll',3.99,'SRC',0,28,'');
/*!40000 ALTER TABLE `menu_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_item_printers`
--

DROP TABLE IF EXISTS `menu_item_printers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_item_printers` (
  `menu_item_id` bigint(20) NOT NULL,
  `printers` varchar(255) DEFAULT NULL,
  KEY `FK_srapw1oaeoa6jwkajck6jjq98` (`menu_item_id`),
  CONSTRAINT `FK_srapw1oaeoa6jwkajck6jjq98` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_item_printers`
--

LOCK TABLES `menu_item_printers` WRITE;
/*!40000 ALTER TABLE `menu_item_printers` DISABLE KEYS */;
INSERT INTO `menu_item_printers` VALUES (48,'df'),(48,'c'),(48,'@#tg'),(58,'df'),(58,'c'),(58,'@#tg'),(78,'df'),(78,'c'),(78,'@#tg'),(128,'df'),(128,'c'),(128,'@#tg'),(13,'@#tg'),(14,'@#tg'),(15,'c'),(15,'@#tg'),(16,'@#tg'),(17,'@#tg'),(18,'c'),(18,'@#tg'),(19,'c'),(19,'@#tg'),(20,'c'),(20,'@#tg'),(25,'@#tg'),(26,'@#tg'),(27,'c'),(27,'@#tg'),(28,'c'),(28,'@#tg'),(35,'df'),(35,'@#tg'),(36,'df'),(36,'@#tg'),(37,'c'),(37,'@#tg'),(38,'df'),(38,'@#tg'),(39,'df'),(39,'@#tg'),(40,'df'),(40,'@#tg'),(41,'c'),(41,'@#tg'),(44,'c'),(44,'@#tg'),(45,'df'),(45,'@#tg'),(46,'df'),(46,'@#tg'),(47,'c'),(47,'@#tg'),(49,'c'),(49,'@#tg'),(50,'c'),(50,'@#tg'),(51,'df'),(51,'@#tg'),(52,'df'),(52,'c'),(52,'@#tg'),(53,'c'),(53,'@#tg'),(54,'c'),(54,'@#tg'),(55,'c'),(55,'@#tg'),(56,'df'),(56,'c'),(56,'@#tg'),(57,'c'),(57,'@#tg'),(59,'c'),(59,'@#tg'),(60,'c'),(60,'@#tg'),(61,'c'),(61,'@#tg'),(75,'c'),(75,'@#tg'),(79,'c'),(79,'@#tg'),(80,'c'),(80,'@#tg'),(81,'c'),(81,'@#tg'),(82,'c'),(82,'@#tg'),(83,'c'),(83,'@#tg'),(84,'c'),(84,'@#tg'),(85,'c'),(85,'@#tg'),(86,'c'),(86,'@#tg'),(87,'c'),(87,'@#tg'),(89,'c'),(89,'@#tg'),(90,'c'),(90,'@#tg'),(92,'c'),(92,'@#tg'),(94,'c'),(94,'@#tg'),(96,'c'),(96,'@#tg'),(97,'c'),(97,'@#tg'),(98,'c'),(98,'@#tg'),(99,'c'),(99,'@#tg'),(101,'c'),(101,'@#tg'),(104,'c'),(104,'@#tg'),(105,'c'),(105,'@#tg'),(106,'c'),(106,'@#tg'),(109,'c'),(109,'@#tg'),(110,'c'),(110,'@#tg'),(112,'c'),(112,'@#tg'),(115,'c'),(115,'@#tg'),(116,'c'),(116,'@#tg'),(119,'df'),(119,'c'),(119,'@#tg'),(120,'df'),(120,'@#tg'),(121,'c'),(121,'@#tg'),(122,'c'),(122,'@#tg'),(123,'c'),(123,'@#tg'),(124,'c'),(124,'@#tg'),(125,'c'),(125,'@#tg'),(126,'c'),(126,'@#tg'),(127,'c'),(127,'@#tg'),(129,'c'),(129,'@#tg'),(130,'c'),(130,'@#tg'),(131,'c'),(131,'@#tg'),(132,'c'),(132,'@#tg'),(133,'c'),(133,'@#tg'),(134,'c'),(134,'@#tg'),(136,'c'),(136,'@#tg'),(137,'c'),(137,'@#tg'),(138,'c'),(138,'@#tg'),(171,'df'),(171,'@#tg'),(139,'c'),(139,'@#tg'),(143,'df'),(143,'@#tg'),(144,'df'),(144,'@#tg'),(145,'c'),(145,'@#tg'),(146,'c'),(146,'@#tg'),(147,'c'),(147,'@#tg'),(148,'c'),(148,'@#tg'),(149,'c'),(149,'@#tg'),(150,'c'),(150,'@#tg'),(151,'c'),(151,'@#tg'),(152,'c'),(152,'@#tg'),(153,'c'),(153,'@#tg'),(154,'c'),(154,'@#tg'),(155,'c'),(155,'@#tg'),(156,'c'),(156,'@#tg'),(157,'c'),(157,'@#tg'),(158,'c'),(158,'@#tg'),(159,'c'),(159,'@#tg'),(160,'c'),(160,'@#tg'),(161,'c'),(161,'@#tg'),(162,'c'),(162,'@#tg'),(163,'c'),(163,'@#tg'),(164,'c'),(164,'@#tg'),(165,'c'),(165,'@#tg'),(3,'df'),(3,'@#tg'),(1,'df'),(1,'@#tg'),(2,'df'),(2,'@#tg'),(5,'df'),(5,'@#tg'),(6,'df'),(6,'@#tg'),(7,'c'),(7,'@#tg'),(166,'s'),(166,'@#tg'),(167,'s'),(167,'@#tg'),(168,'s'),(168,'@#tg'),(169,'s'),(169,'@#tg'),(172,'s'),(172,'@#tg'),(173,'s'),(173,'@#tg'),(174,'s'),(174,'@#tg'),(175,'s'),(175,'@#tg'),(177,'s'),(177,'@#tg'),(178,'s'),(178,'@#tg'),(180,'s'),(180,'@#tg'),(182,'s'),(182,'@#tg'),(183,'s'),(183,'@#tg'),(184,'s'),(184,'@#tg'),(185,'s'),(185,'@#tg'),(186,'s'),(186,'@#tg'),(187,'s'),(187,'@#tg'),(188,'s'),(188,'@#tg'),(189,'s'),(189,'@#tg'),(190,'s'),(190,'@#tg'),(192,'s'),(192,'@#tg'),(193,'s'),(193,'@#tg'),(194,'s'),(194,'@#tg'),(195,'s'),(195,'@#tg'),(196,'s'),(196,'@#tg'),(197,'s'),(197,'@#tg'),(198,'s'),(198,'@#tg'),(199,'s'),(199,'@#tg'),(200,'s'),(200,'@#tg'),(201,'s'),(201,'@#tg'),(203,'s'),(203,'@#tg'),(205,'s'),(205,'@#tg'),(206,'s'),(206,'@#tg'),(207,'s'),(207,'@#tg'),(208,'s'),(208,'@#tg'),(209,'s'),(209,'@#tg'),(210,'s'),(210,'@#tg'),(211,'s'),(211,'@#tg'),(212,'s'),(212,'@#tg'),(213,'s'),(213,'@#tg'),(216,'s'),(216,'@#tg'),(217,'s'),(217,'@#tg'),(218,'s'),(218,'@#tg'),(219,'s'),(219,'@#tg'),(220,'s'),(220,'@#tg'),(221,'s'),(221,'@#tg'),(222,'s'),(222,'@#tg'),(224,'s'),(224,'@#tg'),(225,'s'),(225,'@#tg'),(226,'s'),(226,'@#tg'),(228,'s'),(228,'@#tg'),(21,'@#tg'),(22,'@#tg'),(23,'@#tg'),(24,'@#tg'),(253,'@#tg'),(254,'@#tg'),(255,'@#tg'),(256,'@#tg'),(257,'@#tg'),(258,'@#tg'),(259,'@#tg'),(260,'@#tg'),(261,'@#tg'),(262,'@#tg'),(263,'@#tg'),(264,'@#tg'),(265,'@#tg'),(266,'@#tg'),(267,'@#tg'),(4,'df'),(4,'@#tg'),(8,'df'),(8,'@#tg'),(268,'c'),(268,'@#tg'),(9,'df'),(9,'@#tg'),(10,'df'),(10,'@#tg'),(11,'@#tg'),(12,'@#tg'),(269,'c'),(269,'@#tg'),(270,'c'),(270,'@#tg'),(271,'df'),(271,'@#tg'),(272,'c'),(272,'@#tg'),(273,'c'),(273,'@#tg'),(274,'c'),(274,'@#tg'),(275,'df'),(275,'@#tg'),(276,'c'),(276,'@#tg'),(277,'c'),(277,'@#tg'),(278,'df'),(278,'@#tg'),(279,'c'),(279,'@#tg'),(29,'c'),(29,'@#tg'),(30,'c'),(30,'@#tg'),(31,'c'),(31,'@#tg'),(32,'c'),(32,'@#tg'),(33,'c'),(33,'@#tg'),(34,'c'),(34,'@#tg'),(280,'c'),(280,'@#tg'),(281,'c'),(281,'@#tg'),(282,'c'),(282,'@#tg'),(283,'c'),(283,'@#tg'),(42,'c'),(42,'@#tg'),(43,'c'),(43,'@#tg'),(284,'c'),(284,'@#tg'),(135,'c'),(135,'@#tg'),(285,'c'),(285,'@#tg'),(62,'df'),(62,'@#tg'),(63,'c'),(63,'@#tg'),(64,'c'),(64,'@#tg'),(65,'c'),(65,'@#tg'),(66,'c'),(66,'@#tg'),(67,'c'),(67,'@#tg'),(68,'c'),(68,'@#tg'),(69,'c'),(69,'@#tg'),(70,'c'),(70,'@#tg'),(71,'c'),(71,'@#tg'),(72,'c'),(72,'@#tg'),(73,'c'),(73,'@#tg'),(74,'c'),(74,'@#tg'),(76,'df'),(76,'c'),(76,'@#tg'),(77,'df'),(77,'@#tg'),(286,'c'),(286,'@#tg'),(88,'c'),(88,'@#tg'),(91,'c'),(91,'@#tg'),(93,'c'),(93,'@#tg'),(95,'c'),(95,'@#tg'),(100,'c'),(100,'@#tg'),(102,'c'),(102,'@#tg'),(103,'c'),(103,'@#tg'),(287,'c'),(287,'@#tg'),(288,'c'),(288,'@#tg'),(289,'c'),(289,'@#tg'),(107,'c'),(107,'@#tg'),(108,'c'),(108,'@#tg'),(111,'c'),(111,'@#tg'),(113,'c'),(113,'@#tg'),(114,'c'),(114,'@#tg'),(117,'c'),(117,'@#tg'),(118,'c'),(118,'@#tg'),(290,'c'),(290,'@#tg'),(291,'c'),(291,'@#tg'),(292,'c'),(292,'@#tg'),(293,'c'),(293,'@#tg'),(294,'c'),(294,'@#tg'),(140,'c'),(140,'@#tg'),(142,'c'),(142,'@#tg'),(141,'df'),(141,'@#tg'),(298,'c'),(298,'@#tg'),(249,'@#tg'),(299,'@#tg'),(301,'@#tg'),(302,'@#tg'),(250,'@#tg'),(251,'@#tg'),(252,'@#tg'),(170,'c'),(170,'@#tg'),(176,'s'),(176,'@#tg'),(303,'c'),(303,'@#tg'),(304,'s'),(304,'@#tg'),(202,'s'),(202,'@#tg'),(204,'s'),(204,'@#tg'),(214,'s'),(214,'@#tg'),(215,'s'),(215,'@#tg'),(223,'s'),(223,'@#tg'),(227,'s'),(227,'@#tg'),(181,'s'),(181,'@#tg'),(191,'s'),(191,'@#tg'),(305,'s'),(305,'@#tg'),(306,'s'),(306,'@#tg'),(179,'s'),(179,'@#tg'),(295,'df'),(295,'c'),(295,'@#tg'),(296,'df'),(296,'c'),(296,'@#tg'),(297,'df'),(297,'c'),(297,'@#tg'),(307,'s'),(307,'@#tg'),(308,'s'),(308,'@#tg'),(309,'s'),(309,'@#tg'),(310,'s'),(310,'@#tg'),(311,'s'),(311,'@#tg'),(312,'s'),(312,'@#tg'),(313,'s'),(313,'@#tg'),(314,'s'),(314,'@#tg'),(315,'s'),(315,'@#tg'),(316,'s'),(316,'@#tg');
/*!40000 ALTER TABLE `menu_item_printers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modifier`
--

DROP TABLE IF EXISTS `modifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modifier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  `modifier_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2aw69fx3gqyuhkgb2ig9yv7ry` (`modifier_group_id`),
  CONSTRAINT `FK_2aw69fx3gqyuhkgb2ig9yv7ry` FOREIGN KEY (`modifier_group_id`) REFERENCES `modifier_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modifier`
--

LOCK TABLES `modifier` WRITE;
/*!40000 ALTER TABLE `modifier` DISABLE KEYS */;
INSERT INTO `modifier` VALUES (1,NULL,'\0',NULL,'Beef',0.00,'+',0,0,1),(2,NULL,'\0',NULL,'Chicken',0.00,'+',0,0,1),(3,NULL,'\0',NULL,'Crab Meat',0.00,'+',0,0,1),(4,NULL,'\0',NULL,'Pork',0.00,'+',0,0,1),(5,NULL,'\0',NULL,'Scallop',0.00,'+',0,0,1),(6,NULL,'\0',NULL,'Shrimp',0.00,'+',0,0,1),(7,NULL,'\0',NULL,'Beef',0.00,'-',1,0,1),(8,NULL,'\0',NULL,'Chicken',0.00,'-',1,0,1),(9,NULL,'\0',NULL,'Crab Meat',0.00,'-',1,0,1),(10,NULL,'\0',NULL,'Pork',0.00,'-',1,0,1),(11,NULL,'\0',NULL,'Scallop',0.00,'-',1,0,1),(12,NULL,'\0',NULL,'Shrimp',0.00,'-',1,0,1),(13,NULL,'\0',NULL,'NO Meat',0.00,'-',1,0,1),(14,NULL,'\0',NULL,'NO Seafood',0.00,'-',1,0,1),(15,NULL,'\0',NULL,'Beef',0.00,'SUB',2,0,1),(16,NULL,'\0',NULL,'Chicken',0.00,'SUB',2,0,1),(17,NULL,'\0',NULL,'Crab Meat',0.00,'SUB',2,0,1),(18,NULL,'\0',NULL,'Pork',0.00,'SUB',2,0,1),(19,NULL,'\0',NULL,'Scallop',0.00,'SUB',2,0,1),(20,NULL,'\0',NULL,'Shrimp',0.00,'SUB',2,0,1),(21,NULL,'\0',NULL,'Almond',0.00,'+',0,0,2),(22,NULL,'\0',NULL,'Baby Corn',0.00,'+',0,0,2),(23,NULL,'\0',NULL,'Bamboo',0.00,'+',0,0,2),(24,NULL,'\0',NULL,'Bean Sprout',0.00,'+',0,0,2),(25,NULL,'\0',NULL,'Broccoli',0.00,'+',0,0,2),(26,NULL,'\0',NULL,'Cabbage',0.00,'+',0,0,2),(27,NULL,'\0',NULL,'Carrot',0.00,'+',0,0,2),(28,NULL,'\0',NULL,'Cashew',0.00,'+',0,0,2),(29,NULL,'\0',NULL,'Celery',0.00,'+',0,0,2),(30,NULL,'\0',NULL,'Egg',0.00,'+',0,0,2),(31,NULL,'\0',NULL,'Eggplant',0.00,'+',0,0,2),(32,NULL,'\0',NULL,'Garlic',0.00,'+',0,0,2),(33,NULL,'\0',NULL,'Ginger',0.00,'+',0,0,2),(34,NULL,'\0',NULL,'Green Peas',0.00,'+',0,0,2),(35,NULL,'\0',NULL,'Green Pepper',0.00,'+',0,0,2),(36,NULL,'\0',NULL,'Mixed Vegetables',0.00,'+',0,0,2),(37,NULL,'\0',NULL,'Black Mushrooms',0.00,'+',0,0,2),(38,NULL,'\0',NULL,'Sliced Mushrooms',0.00,'+',0,0,2),(39,NULL,'\0',NULL,'Straw Mushrooms',0.00,'+',0,0,2),(40,NULL,'\0',NULL,'Nappa',0.00,'+',0,0,2),(41,NULL,'\0',NULL,'Green Onion',0.00,'+',0,0,2),(42,NULL,'\0',NULL,'White Onion',0.00,'+',0,0,2),(43,NULL,'\0',NULL,'Peanut',0.00,'+',0,0,2),(44,NULL,'\0',NULL,'Sesame',0.00,'+',0,0,2),(45,NULL,'\0',NULL,'Snow Peas',0.00,'+',0,0,2),(46,NULL,'\0',NULL,'String Bean',0.00,'+',0,0,2),(47,NULL,'\0',NULL,'Fried Tofu',0.00,'+',0,0,2),(48,NULL,'\0',NULL,'Tofu',0.00,'+',0,0,2),(49,NULL,'\0',NULL,'Water Chestnut',0.00,'+',0,0,2),(50,NULL,'\0',NULL,'Zucchini',0.00,'+',0,0,2),(51,NULL,'\0',NULL,'Almond',0.00,'-',1,0,2),(52,NULL,'\0',NULL,'Baby Corn',0.00,'-',1,0,2),(53,NULL,'\0',NULL,'Bamboo',0.00,'-',1,0,2),(54,NULL,'\0',NULL,'Bean Sprout',0.00,'-',1,0,2),(55,NULL,'\0',NULL,'Broccoli',0.00,'-',1,0,2),(56,NULL,'\0',NULL,'Cabbage',0.00,'-',1,0,2),(57,NULL,'\0',NULL,'Carrot',0.00,'-',1,0,2),(58,NULL,'\0',NULL,'Cashew',0.00,'-',1,0,2),(59,NULL,'\0',NULL,'Celery',0.00,'-',1,0,2),(60,NULL,'\0',NULL,'Egg',0.00,'-',1,0,2),(61,NULL,'\0',NULL,'Eggplant',0.00,'-',1,0,2),(62,NULL,'\0',NULL,'Garlic',0.00,'-',1,0,2),(63,NULL,'\0',NULL,'Ginger',0.00,'-',1,0,2),(64,NULL,'\0',NULL,'Green Peas',0.00,'-',1,0,2),(65,NULL,'\0',NULL,'Green Pepper',0.00,'-',1,0,2),(66,NULL,'\0',NULL,'Mixed Vegetables',0.00,'-',1,0,2),(67,NULL,'\0',NULL,'Black Mushrooms',0.00,'-',1,0,2),(68,NULL,'\0',NULL,'Sliced Mushrooms',0.00,'-',1,0,2),(69,NULL,'\0',NULL,'Straw Mushrooms',0.00,'-',1,0,2),(70,NULL,'\0',NULL,'Nappa',0.00,'-',1,0,2),(71,NULL,'\0',NULL,'Green Onion',0.00,'-',1,0,2),(72,NULL,'\0',NULL,'White Onion',0.00,'-',1,0,2),(73,NULL,'\0',NULL,'Peanut',0.00,'-',1,0,2),(74,NULL,'\0',NULL,'Sesame',0.00,'-',1,0,2),(75,NULL,'\0',NULL,'Snow Peas',0.00,'-',1,0,2),(76,NULL,'\0',NULL,'String Bean',0.00,'-',1,0,2),(77,NULL,'\0',NULL,'Fried Tofu',0.00,'-',1,0,2),(78,NULL,'\0',NULL,'Tofu',0.00,'-',1,0,2),(79,NULL,'\0',NULL,'Water Chestnut',0.00,'-',1,0,2),(80,NULL,'\0',NULL,'Zucchini',0.00,'-',1,0,2),(81,NULL,'\0',NULL,'Almond',0.00,'SUB',2,0,2),(82,NULL,'\0',NULL,'Baby Corn',0.00,'SUB',2,0,2),(83,NULL,'\0',NULL,'Bamboo',0.00,'SUB',2,0,2),(84,NULL,'\0',NULL,'Bean Sprout',0.00,'SUB',2,0,2),(85,NULL,'\0',NULL,'Broccoli',0.00,'SUB',2,0,2),(86,NULL,'\0',NULL,'Cabbage',0.00,'SUB',2,0,2),(87,NULL,'\0',NULL,'Carrot',0.00,'SUB',2,0,2),(88,NULL,'\0',NULL,'Cashew',0.00,'SUB',2,0,2),(89,NULL,'\0',NULL,'Celery',0.00,'SUB',2,0,2),(90,NULL,'\0',NULL,'Egg',0.00,'SUB',2,0,2),(91,NULL,'\0',NULL,'Eggplant',0.00,'SUB',2,0,2),(92,NULL,'\0',NULL,'Garlic',0.00,'SUB',2,0,2),(93,NULL,'\0',NULL,'Ginger',0.00,'SUB',2,0,2),(94,NULL,'\0',NULL,'Green Peas',0.00,'SUB',2,0,2),(95,NULL,'\0',NULL,'Green Pepper',0.00,'SUB',2,0,2),(96,NULL,'\0',NULL,'Mixed Vegetables',0.00,'SUB',2,0,2),(97,NULL,'\0',NULL,'Black Mushrooms',0.00,'SUB',2,0,2),(98,NULL,'\0',NULL,'Sliced Mushrooms',0.00,'SUB',2,0,2),(99,NULL,'\0',NULL,'Straw Mushrooms',0.00,'SUB',2,0,2),(100,NULL,'\0',NULL,'Nappa',0.00,'SUB',2,0,2),(101,NULL,'\0',NULL,'Green Onion',0.00,'SUB',2,0,2),(102,NULL,'\0',NULL,'White Onion',0.00,'SUB',2,0,2),(103,NULL,'\0',NULL,'Peanut',0.00,'SUB',2,0,2),(104,NULL,'\0',NULL,'Sesame',0.00,'SUB',2,0,2),(105,NULL,'\0',NULL,'Snow Peas',0.00,'SUB',2,0,2),(106,NULL,'\0',NULL,'String Bean',0.00,'SUB',2,0,2),(107,NULL,'\0',NULL,'Fried Tofu',0.00,'SUB',2,0,2),(108,NULL,'\0',NULL,'Tofu',0.00,'SUB',2,0,2),(109,NULL,'\0',NULL,'Water Chestnut',0.00,'SUB',2,0,2),(110,NULL,'\0',NULL,'Zucchini',0.00,'SUB',2,0,2),(111,NULL,'\0',NULL,'Avocado',0.00,'+',0,0,3),(112,NULL,'\0',NULL,'Crab',0.00,'+',0,0,3),(113,NULL,'\0',NULL,'Cream Cheese',0.00,'+',0,0,3),(114,NULL,'\0',NULL,'Cucumber',0.00,'+',0,0,3),(115,NULL,'\0',NULL,'Eel Sauce',0.00,'+',0,0,3),(116,NULL,'\0',NULL,'Eel',0.00,'+',0,0,3),(117,NULL,'\0',NULL,'Fish',0.00,'+',0,0,3),(118,NULL,'\0',NULL,'Ginger',0.00,'+',0,0,3),(119,NULL,'\0',NULL,'Masago',0.00,'+',0,0,3),(120,NULL,'\0',NULL,'Mayo',0.00,'+',0,0,3),(121,NULL,'\0',NULL,'Octopus',0.00,'+',0,0,3),(122,NULL,'\0',NULL,'Red Snapper',0.00,'+',0,0,3),(123,NULL,'\0',NULL,'Salmon',0.00,'+',0,0,3),(124,NULL,'\0',NULL,'Scallop',0.00,'+',0,0,3),(125,NULL,'\0',NULL,'Shrimp',0.00,'+',0,0,3),(126,NULL,'\0',NULL,'Smoked Salmon',0.00,'+',0,0,3),(127,NULL,'\0',NULL,'Soy Bean Wrap',0.00,'+',0,0,3),(128,NULL,'\0',NULL,'Spicy Tuna',0.00,'+',0,0,3),(129,NULL,'\0',NULL,'Wasabi',0.00,'+',0,0,3),(130,NULL,'\0',NULL,'White Tuna',0.00,'+',0,0,3),(131,NULL,'\0',NULL,'Yellow Tail',0.00,'+',0,0,3),(132,NULL,'\0',NULL,'Avocado',0.00,'-',1,0,3),(133,NULL,'\0',NULL,'Crab',0.00,'-',1,0,3),(134,NULL,'\0',NULL,'Cream Cheese',0.00,'-',1,0,3),(135,NULL,'\0',NULL,'Cucumber',0.00,'-',1,0,3),(136,NULL,'\0',NULL,'Eel Sauce',0.00,'-',1,0,3),(137,NULL,'\0',NULL,'Eel',0.00,'-',1,0,3),(138,NULL,'\0',NULL,'Fish',0.00,'-',1,0,3),(139,NULL,'\0',NULL,'Ginger',0.00,'-',1,0,3),(140,NULL,'\0',NULL,'Masago',0.00,'-',1,0,3),(141,NULL,'\0',NULL,'Mayo',0.00,'-',1,0,3),(142,NULL,'\0',NULL,'Octopus',0.00,'-',1,0,3),(143,NULL,'\0',NULL,'Red Snapper',0.00,'-',1,0,3),(144,NULL,'\0',NULL,'Salmon',0.00,'-',1,0,3),(145,NULL,'\0',NULL,'Scallop',0.00,'-',1,0,3),(146,NULL,'\0',NULL,'Shrimp',0.00,'-',1,0,3),(147,NULL,'\0',NULL,'Smoked Salmon',0.00,'-',1,0,3),(148,NULL,'\0',NULL,'Soy Bean Wrap',0.00,'-',1,0,3),(149,NULL,'\0',NULL,'Spicy Tuna',0.00,'-',1,0,3),(150,NULL,'\0',NULL,'Wasabi',0.00,'-',1,0,3),(151,NULL,'\0',NULL,'White Tuna',0.00,'-',1,0,3),(152,NULL,'\0',NULL,'Yellow Tail',0.00,'-',1,0,3),(153,NULL,'\0',NULL,'Avocado',0.00,'SUB',2,0,3),(154,NULL,'\0',NULL,'Crab',0.00,'SUB',2,0,3),(155,NULL,'\0',NULL,'Cream Cheese',0.00,'SUB',2,0,3),(156,NULL,'\0',NULL,'Cucumber',0.00,'SUB',2,0,3),(157,NULL,'\0',NULL,'Eel Sauce',0.00,'SUB',2,0,3),(158,NULL,'\0',NULL,'Eel',0.00,'SUB',2,0,3),(159,NULL,'\0',NULL,'Fish',0.00,'SUB',2,0,3),(160,NULL,'\0',NULL,'Ginger',0.00,'SUB',2,0,3),(161,NULL,'\0',NULL,'Masago',0.00,'SUB',2,0,3),(162,NULL,'\0',NULL,'Mayo',0.00,'SUB',2,0,3),(163,NULL,'\0',NULL,'Octopus',0.00,'SUB',2,0,3),(164,NULL,'\0',NULL,'Red Snapper',0.00,'SUB',2,0,3),(165,NULL,'\0',NULL,'Salmon',0.00,'SUB',2,0,3),(166,NULL,'\0',NULL,'Scallop',0.00,'SUB',2,0,3),(167,NULL,'\0',NULL,'Shrimp',0.00,'SUB',2,0,3),(168,NULL,'\0',NULL,'Smoked Salmon',0.00,'SUB',2,0,3),(169,NULL,'\0',NULL,'Soy Bean Wrap',0.00,'SUB',2,0,3),(170,NULL,'\0',NULL,'Spicy Tuna',0.00,'SUB',2,0,3),(171,NULL,'\0',NULL,'Wasabi',0.00,'SUB',2,0,3),(172,NULL,'\0',NULL,'White Tuna',0.00,'SUB',2,0,3),(173,NULL,'\0',NULL,'Yellow Tail',0.00,'SUB',2,0,3),(174,NULL,'\0',NULL,'Black Bean Sauce',0.00,'+',0,0,4),(175,NULL,'\0',NULL,'Brown Sauce',0.00,'+',0,0,4),(176,NULL,'\0',NULL,'Curry Sauce',0.00,'+',0,0,4),(177,NULL,'\0',NULL,'Garlic Sauce',0.00,'+',0,0,4),(178,NULL,'\0',NULL,'Ginger Sauce',0.00,'+',0,0,4),(179,NULL,'\0',NULL,'Hot Mustard',0.00,'+',0,0,4),(180,NULL,'\0',NULL,'Hot Sauce',0.00,'+',0,0,4),(181,NULL,'\0',NULL,'Orange Sauce',0.00,'+',0,0,4),(182,NULL,'\0',NULL,'Soy Sauce',0.00,'+',0,0,4),(183,NULL,'\0',NULL,'Tiny Sauce',0.00,'+',0,0,4),(184,NULL,'\0',NULL,'Duck Sauce',0.00,'+',0,0,4),(185,NULL,'\0',NULL,'Plum Sauce',0.00,'+',0,0,4),(186,NULL,'\0',NULL,'White Sauce',0.00,'+',0,0,4),(187,NULL,'\0',NULL,'Black Bean Sauce',0.00,'-',1,0,4),(188,NULL,'\0',NULL,'Brown Sauce',0.00,'-',1,0,4),(189,NULL,'\0',NULL,'Curry Sauce',0.00,'-',1,0,4),(190,NULL,'\0',NULL,'Garlic Sauce',0.00,'-',1,0,4),(191,NULL,'\0',NULL,'Ginger Sauce',0.00,'-',1,0,4),(192,NULL,'\0',NULL,'Hot Mustard',0.00,'-',1,0,4),(193,NULL,'\0',NULL,'Hot Sauce',0.00,'-',1,0,4),(194,NULL,'\0',NULL,'Orange Sauce',0.00,'-',1,0,4),(195,NULL,'\0',NULL,'Soy Sauce',0.00,'-',1,0,4),(196,NULL,'\0',NULL,'Tiny Sauce',0.00,'-',1,0,4),(197,NULL,'\0',NULL,'Duck Sauce',0.00,'-',1,0,4),(198,NULL,'\0',NULL,'Plum Sauce',0.00,'-',1,0,4),(199,NULL,'\0',NULL,'White Sauce',0.00,'-',1,0,4),(200,NULL,'\0',NULL,'Black Bean Sauce',0.00,'SUB',2,0,4),(201,NULL,'\0',NULL,'Brown Sauce',0.00,'SUB',2,0,4),(202,NULL,'\0',NULL,'Curry Sauce',0.00,'SUB',2,0,4),(203,NULL,'\0',NULL,'Garlic Sauce',0.00,'SUB',2,0,4),(204,NULL,'\0',NULL,'Ginger Sauce',0.00,'SUB',2,0,4),(205,NULL,'\0',NULL,'Hot Mustard',0.00,'SUB',2,0,4),(206,NULL,'\0',NULL,'Hot Sauce',0.00,'SUB',2,0,4),(207,NULL,'\0',NULL,'Orange Sauce',0.00,'SUB',2,0,4),(208,NULL,'\0',NULL,'Soy Sauce',0.00,'SUB',2,0,4),(209,NULL,'\0',NULL,'Tiny Sauce',0.00,'SUB',2,0,4),(210,NULL,'\0',NULL,'Duck Sauce',0.00,'SUB',2,0,4),(211,NULL,'\0',NULL,'Plum Sauce',0.00,'SUB',2,0,4),(212,NULL,'\0',NULL,'White Sauce',0.00,'SUB',2,0,4),(213,NULL,'\0',NULL,'MSG',0.00,'+',0,0,5),(214,NULL,'\0',NULL,'Oil',0.00,'+',0,0,5),(215,NULL,'\0',NULL,'Pancake',0.00,'+',0,0,5),(216,NULL,'\0',NULL,'Pepper',0.00,'+',0,0,5),(217,NULL,'\0',NULL,'Salt',0.00,'+',0,0,5),(218,NULL,'\0',NULL,'Sugar',0.00,'+',0,0,5),(219,NULL,'\0',NULL,'Won Ton',0.00,'+',0,0,5),(220,NULL,'\0',NULL,'Corn Starch',0.00,'+',0,0,5),(221,NULL,'\0',NULL,'MSG',0.00,'-',1,0,5),(222,NULL,'\0',NULL,'Oil',0.00,'-',1,0,5),(223,NULL,'\0',NULL,'Pancake',0.00,'-',1,0,5),(224,NULL,'\0',NULL,'Pepper',0.00,'-',1,0,5),(225,NULL,'\0',NULL,'Salt',0.00,'-',1,0,5),(226,NULL,'\0',NULL,'Sugar',0.00,'-',1,0,5),(227,NULL,'\0',NULL,'Won Ton',0.00,'-',1,0,5),(228,NULL,'\0',NULL,'Corn Starch',0.00,'-',1,0,5),(229,NULL,'\0',NULL,'MSG',0.00,'SUB',2,0,5),(230,NULL,'\0',NULL,'Oil',0.00,'SUB',2,0,5),(231,NULL,'\0',NULL,'Pancake',0.00,'SUB',2,0,5),(232,NULL,'\0',NULL,'Pepper',0.00,'SUB',2,0,5),(233,NULL,'\0',NULL,'Salt',0.00,'SUB',2,0,5),(234,NULL,'\0',NULL,'Sugar',0.00,'SUB',2,0,5),(235,NULL,'\0',NULL,'Won Ton',0.00,'SUB',2,0,5),(236,NULL,'\0',NULL,'Corn Starch',0.00,'SUB',2,0,5),(237,NULL,'\0',NULL,'Chop Small',0.00,'+',0,0,5),(238,NULL,'\0',NULL,'Crispy',0.00,'+',0,0,5),(239,NULL,'\0',NULL,'Deep Fried',0.00,'+',0,0,5),(240,NULL,'\0',NULL,'Sauteed',0.00,'+',0,0,5),(241,NULL,'\0',NULL,'Spicy',0.00,'+',0,0,5),(242,NULL,'\0',NULL,'Steamed',0.00,'+',0,0,5),(243,NULL,'\0',NULL,'Sauce On Side',0.00,'+',0,0,5),(244,NULL,'\0',NULL,'Rare',0.00,'+',0,0,5),(245,NULL,'\0',NULL,'Medium Rare',0.00,'+',0,0,5),(246,NULL,'\0',NULL,'Medium',0.00,'+',0,0,5),(247,NULL,'\0',NULL,'Medium Well',0.00,'+',0,0,5),(248,NULL,'\0',NULL,'Well-Done',0.00,'+',0,0,5),(249,NULL,'\0',NULL,'Brown Rice',0.00,'+',0,0,6),(250,NULL,'\0',NULL,'White Rice',0.00,'+',0,0,6),(251,NULL,'\0',NULL,'Ice',0.00,'+',0,0,7),(252,NULL,'\0',NULL,'Ice',0.00,'-',1,0,7),(253,NULL,'\0','2016-08-13 15:44:50.269000','White',0.00,'+',0,0,1),(254,NULL,'\0','2016-08-13 15:45:10.080000','White',0.00,'-',1,0,1),(255,NULL,'\0','2016-08-13 15:45:25.592000','White',0.00,'SUB',2,0,1),(256,NULL,'\0','2016-08-13 15:46:16.364000','Asparagus',0.00,'+',0,0,2),(257,NULL,'\0','2016-08-13 15:46:32.995000','Asparagus',0.00,'-',1,0,2),(258,NULL,'\0','2016-08-13 15:46:48.992000','Asparagus',0.00,'SUB',2,0,2),(259,NULL,'\0','2016-08-13 15:51:22.874000','Easy Cook',0.00,'+',0,0,5),(260,NULL,'\0','2016-08-13 15:51:37.409000','All Spicy',0.00,'+',0,0,5);
/*!40000 ALTER TABLE `modifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modifier_group`
--

DROP TABLE IF EXISTS `modifier_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modifier_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modifier_group`
--

LOCK TABLES `modifier_group` WRITE;
/*!40000 ALTER TABLE `modifier_group` DISABLE KEYS */;
INSERT INTO `modifier_group` VALUES (1,NULL,'\0',NULL,'Meat','M',0),(2,NULL,'\0',NULL,'Vegetables','V',0),(3,NULL,'\0',NULL,'Sushi','I',0),(4,NULL,'\0',NULL,'Sauce','S',0),(5,NULL,'\0',NULL,'Cook','C',0),(6,NULL,'\0',NULL,'Rice','R',0),(7,NULL,'\0',NULL,'Drink','D',0);
/*!40000 ALTER TABLE `modifier_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_entry`
--

DROP TABLE IF EXISTS `payment_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `sales_order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ko8djsr334xrpi946ac2qhfmq` (`sales_order_id`),
  CONSTRAINT `FK_ko8djsr334xrpi946ac2qhfmq` FOREIGN KEY (`sales_order_id`) REFERENCES `sales_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_entry`
--

LOCK TABLES `payment_entry` WRITE;
/*!40000 ALTER TABLE `payment_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_entry_record`
--

DROP TABLE IF EXISTS `payment_entry_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_entry_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `sales_order_record_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gstuokcba39nbqawuay02pv9y` (`sales_order_record_id`),
  CONSTRAINT `FK_gstuokcba39nbqawuay02pv9y` FOREIGN KEY (`sales_order_record_id`) REFERENCES `sales_order_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_entry_record`
--

LOCK TABLES `payment_entry_record` WRITE;
/*!40000 ALTER TABLE `payment_entry_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment_entry_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
INSERT INTO `position` VALUES (1,NULL,'\0',NULL,'admin'),(2,NULL,'\0',NULL,'manager'),(3,NULL,'\0',NULL,'supervisor'),(4,NULL,'\0',NULL,'cashier'),(5,NULL,'\0',NULL,'server'),(6,NULL,'\0',NULL,'general');
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_permissions`
--

DROP TABLE IF EXISTS `position_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position_permissions` (
  `position_id` bigint(20) NOT NULL,
  `permissions` int(11) DEFAULT NULL,
  KEY `FK_j5an1u7ov25976eftwsg9mflg` (`position_id`),
  CONSTRAINT `FK_j5an1u7ov25976eftwsg9mflg` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_permissions`
--

LOCK TABLES `position_permissions` WRITE;
/*!40000 ALTER TABLE `position_permissions` DISABLE KEYS */;
INSERT INTO `position_permissions` VALUES (1,0),(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39),(1,40),(1,41),(1,42),(1,43),(1,44),(1,45),(2,0),(2,1),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(2,10),(2,11),(2,12),(2,13),(2,14),(2,15),(2,16),(2,17),(2,18),(2,19),(2,20),(2,21),(2,22),(2,23),(2,24),(2,25),(2,26),(2,27),(2,28),(2,29),(2,30),(2,31),(2,32),(2,33),(2,35),(2,36),(2,37),(2,38),(2,39),(2,40),(2,41),(2,42),(2,43),(2,44),(2,45),(3,1),(3,3),(3,4),(3,5),(3,6),(3,7),(3,8),(3,9),(3,10),(3,11),(3,12),(3,13),(3,14),(3,15),(3,16),(3,17),(3,18),(3,19),(3,20),(3,21),(3,22),(3,23),(3,24),(3,29),(3,30),(3,31),(3,32),(3,35),(3,36),(3,37),(3,38),(3,40),(3,41),(3,42),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),(4,10),(4,12),(4,14),(4,16),(4,17),(4,18),(4,20),(4,21),(4,22),(4,23),(4,24),(4,40),(4,41),(4,42),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(5,10),(5,12),(5,14),(5,16),(5,17),(5,18),(5,20),(5,22),(5,23),(5,24),(5,40),(5,41),(5,42),(6,40),(6,41),(6,42);
/*!40000 ALTER TABLE `position_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_entry`
--

DROP TABLE IF EXISTS `product_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `quantity` decimal(19,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `menu_item_id` bigint(20) DEFAULT NULL,
  `sales_order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8leso45ejw2cpil3flw1rwsw4` (`menu_item_id`),
  KEY `FK_2b61lwv7ea57w8ro4fdmrvnb2` (`sales_order_id`),
  CONSTRAINT `FK_2b61lwv7ea57w8ro4fdmrvnb2` FOREIGN KEY (`sales_order_id`) REFERENCES `sales_order` (`id`),
  CONSTRAINT `FK_8leso45ejw2cpil3flw1rwsw4` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_item` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_entry`
--

LOCK TABLES `product_entry` WRITE;
/*!40000 ALTER TABLE `product_entry` DISABLE KEYS */;
INSERT INTO `product_entry` VALUES (80,NULL,1.00,0,248,36),(81,NULL,1.00,0,253,37),(82,NULL,1.00,0,253,38);
/*!40000 ALTER TABLE `product_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_entry_modifiers`
--

DROP TABLE IF EXISTS `product_entry_modifiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_entry_modifiers` (
  `product_entries_id` bigint(20) NOT NULL,
  `modifiers_id` bigint(20) NOT NULL,
  KEY `FK_i8gx68pa13y63ueuu9hv76lxi` (`modifiers_id`),
  KEY `FK_50yhgnop07o9drct09fx24b71` (`product_entries_id`),
  CONSTRAINT `FK_50yhgnop07o9drct09fx24b71` FOREIGN KEY (`product_entries_id`) REFERENCES `product_entry` (`id`),
  CONSTRAINT `FK_i8gx68pa13y63ueuu9hv76lxi` FOREIGN KEY (`modifiers_id`) REFERENCES `modifier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_entry_modifiers`
--

LOCK TABLES `product_entry_modifiers` WRITE;
/*!40000 ALTER TABLE `product_entry_modifiers` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_entry_modifiers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_entry_record`
--

DROP TABLE IF EXISTS `product_entry_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_entry_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `quantity` decimal(19,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `menu_item_id` bigint(20) DEFAULT NULL,
  `sales_order_record_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_o25c6464y1cl6w5k9ct3hmvpr` (`menu_item_id`),
  KEY `FK_7tahtgsldvsbwxng7u518mhg2` (`sales_order_record_id`),
  CONSTRAINT `FK_7tahtgsldvsbwxng7u518mhg2` FOREIGN KEY (`sales_order_record_id`) REFERENCES `sales_order_record` (`id`),
  CONSTRAINT `FK_o25c6464y1cl6w5k9ct3hmvpr` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_item` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_entry_record`
--

LOCK TABLES `product_entry_record` WRITE;
/*!40000 ALTER TABLE `product_entry_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_entry_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_entry_record_modifiers`
--

DROP TABLE IF EXISTS `product_entry_record_modifiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_entry_record_modifiers` (
  `product_entry_records_id` bigint(20) NOT NULL,
  `modifiers_id` bigint(20) NOT NULL,
  KEY `FK_brv2x2dmppb9h43mj8usm2y6` (`modifiers_id`),
  KEY `FK_m5m026mhkcdxuek9t3kym7ebv` (`product_entry_records_id`),
  CONSTRAINT `FK_brv2x2dmppb9h43mj8usm2y6` FOREIGN KEY (`modifiers_id`) REFERENCES `modifier` (`id`),
  CONSTRAINT `FK_m5m026mhkcdxuek9t3kym7ebv` FOREIGN KEY (`product_entry_records_id`) REFERENCES `product_entry_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_entry_record_modifiers`
--

LOCK TABLES `product_entry_record_modifiers` WRITE;
/*!40000 ALTER TABLE `product_entry_record_modifiers` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_entry_record_modifiers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order`
--

DROP TABLE IF EXISTS `sales_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cook_time` datetime(6) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `gratuity_percent` decimal(19,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tax_percent` decimal(19,2) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  `seat_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tgmt6ny9xrqqht5f3b2c9w8vl` (`customer_id`),
  KEY `FK_8uhx884p03wwm8m2g29mqt9g9` (`employee_id`),
  KEY `FK_k5vndk48pduacth887eyd4d1n` (`seat_id`),
  CONSTRAINT `FK_8uhx884p03wwm8m2g29mqt9g9` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK_k5vndk48pduacth887eyd4d1n` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`),
  CONSTRAINT `FK_tgmt6ny9xrqqht5f3b2c9w8vl` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order`
--

LOCK TABLES `sales_order` WRITE;
/*!40000 ALTER TABLE `sales_order` DISABLE KEYS */;
INSERT INTO `sales_order` VALUES (36,NULL,'2016-08-13 16:06:02.374000',0.00,0,0.09,0,NULL,2,3),(37,NULL,'2016-08-13 16:18:45.763000',0.00,0,0.09,1,1,2,NULL),(38,NULL,'2016-08-13 16:18:58.408000',0.00,0,0.09,0,NULL,2,5);
/*!40000 ALTER TABLE `sales_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order_record`
--

DROP TABLE IF EXISTS `sales_order_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_order_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idx` bigint(20) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `gratuity_percent` decimal(19,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tax_percent` decimal(19,2) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  `seat_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2iyl6pidgmli7i09yr57a7cuq` (`customer_id`),
  KEY `FK_5cidy2wnt2m7e715rvvwmu5ho` (`employee_id`),
  KEY `FK_ry4ocr921cw2vcc1nwjthoipq` (`seat_id`),
  CONSTRAINT `FK_2iyl6pidgmli7i09yr57a7cuq` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_5cidy2wnt2m7e715rvvwmu5ho` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK_ry4ocr921cw2vcc1nwjthoipq` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order_record`
--

LOCK TABLES `sales_order_record` WRITE;
/*!40000 ALTER TABLE `sales_order_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_order_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `col` tinyint(4) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `row` tinyint(4) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `section_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_edcsxq1dxvwrtgt8qlbcreo9d` (`section_id`),
  CONSTRAINT `FK_edcsxq1dxvwrtgt8qlbcreo9d` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES (1,NULL,'\0',3,NULL,0,'B1',1),(2,NULL,'\0',14,NULL,0,'B14',1),(3,NULL,'\0',3,NULL,1,'B2',1),(4,NULL,'\0',14,NULL,1,'B13',1),(5,NULL,'\0',4,NULL,2,'B3',1),(6,NULL,'\0',5,NULL,2,'B4',1),(7,NULL,'\0',6,NULL,2,'B5',1),(8,NULL,'\0',7,NULL,2,'B6',1),(9,NULL,'\0',8,NULL,2,'B7',1),(10,NULL,'\0',9,NULL,2,'B8',1),(11,NULL,'\0',10,NULL,2,'B9',1),(12,NULL,'\0',11,NULL,2,'B10',1),(13,NULL,'\0',12,NULL,2,'B11',1),(14,NULL,'\0',13,NULL,2,'B12',1),(15,NULL,'\0',0,NULL,4,'1',1),(16,NULL,'\0',2,NULL,4,'6',1),(17,NULL,'\0',3,NULL,4,'10',1),(18,NULL,'\0',5,NULL,4,'14',1),(19,NULL,'\0',6,NULL,5,'20',1),(20,NULL,'\0',0,NULL,6,'2',1),(21,NULL,'\0',2,NULL,6,'7',1),(22,NULL,'\0',3,NULL,6,'11',1),(23,NULL,'\0',5,NULL,6,'15',1),(24,NULL,'\0',6,NULL,6,'21',1),(25,NULL,'\0',15,NULL,6,'40',1),(26,NULL,'\0',16,NULL,6,'41',1),(27,NULL,'\0',17,NULL,6,'42',1),(28,NULL,'\0',6,NULL,7,'22',1),(29,NULL,'\0',8,NULL,7,'30',1),(30,NULL,'\0',10,NULL,7,'31',1),(31,NULL,'\0',12,NULL,7,'32',1),(32,NULL,'\0',13,NULL,7,'33',1),(33,NULL,'\0',0,NULL,8,'3',1),(34,NULL,'\0',2,NULL,8,'8',1),(35,NULL,'\0',3,NULL,8,'12',1),(36,NULL,'\0',5,NULL,8,'16',1),(37,NULL,'\0',6,NULL,8,'23',1),(38,NULL,'\0',15,NULL,8,'46',1),(39,NULL,'\0',16,NULL,8,'45',1),(40,NULL,'\0',17,NULL,8,'44',1),(41,NULL,'\0',6,NULL,9,'24',1),(42,NULL,'\0',0,NULL,10,'4',1),(43,NULL,'\0',2,NULL,10,'9',1),(44,NULL,'\0',3,NULL,10,'13',1),(45,NULL,'\0',5,NULL,10,'17',1),(46,NULL,'\0',6,NULL,10,'25',1),(47,NULL,'\0',8,NULL,9,'34',1),(48,NULL,'\0',10,NULL,9,'35',1),(49,NULL,'\0',15,NULL,10,'50',1),(51,NULL,'\0',17,NULL,10,'51',1),(52,NULL,'\0',0,NULL,12,'5',1),(53,NULL,'\0',3,NULL,12,'29',1),(54,NULL,'\0',4,NULL,12,'28',1),(55,NULL,'\0',5,NULL,12,'27',1),(56,NULL,'\0',6,NULL,12,'26',1),(57,NULL,'\0',15,NULL,12,'52',1),(59,NULL,'\0',17,NULL,12,'53',1),(74,NULL,'\0',17,'2016-08-13 11:06:46.717000',7,'43',1);
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `cols` tinyint(4) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rows` tinyint(4) NOT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (1,NULL,'\0',18,NULL,'Section I',13,'I',0);
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_card_entry`
--

DROP TABLE IF EXISTS `time_card_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_card_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `archive_date` datetime(6) DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `clockin` bit(1) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  `verifier_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_15h4y7jfn9qjs8mp5ay2fbboj` (`employee_id`),
  KEY `FK_i5f57f9rptdsgvryndghsf51v` (`verifier_id`),
  CONSTRAINT `FK_15h4y7jfn9qjs8mp5ay2fbboj` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `FK_i5f57f9rptdsgvryndghsf51v` FOREIGN KEY (`verifier_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_card_entry`
--

LOCK TABLES `time_card_entry` WRITE;
/*!40000 ALTER TABLE `time_card_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `time_card_entry` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-13 17:26:53
