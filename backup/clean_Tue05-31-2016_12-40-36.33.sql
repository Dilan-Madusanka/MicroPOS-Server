-- MySQL dump 10.13  Distrib 5.7.10, for Win64 (x86_64)
--
-- Host: localhost    Database: micropos
-- ------------------------------------------------------
-- Server version	5.7.10-log

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,NULL,'\0',NULL,'Appetizers','A',0),(2,NULL,'\0',NULL,'Soups','T',0),(3,NULL,'\0',NULL,'Beverages','D',0),(4,NULL,'\0',NULL,'Chefs Specialties','H',0),(5,NULL,'\0',NULL,'Kids Menu','KID',0),(6,NULL,'\0',NULL,'Poultry','C',0),(7,NULL,'\0',NULL,'Seafood','S',0),(8,NULL,'\0',NULL,'Vegetables','V',0),(9,NULL,'\0',NULL,'Chop Suey','CS',0),(10,NULL,'\0',NULL,'Egg Foo Young','E',0),(11,NULL,'\0',NULL,'Fried Rice','FR',0),(12,NULL,'\0',NULL,'Chow Mein','CM',0),(13,NULL,'\0',NULL,'Lo Mein','LM',0),(14,NULL,'\0',NULL,'Pork','P',0),(15,NULL,'\0',NULL,'Beef','B',0),(16,NULL,'\0',NULL,'Luncheon Special','L',0),(17,NULL,'\0',NULL,'Sushi Appetizers','AP',0),(18,NULL,'\0',NULL,'Sashimi Combo Tray','SCT',0),(19,NULL,'\0',NULL,'Nigiri','N',0),(20,NULL,'\0',NULL,'Lunch Special Sushi','LSS',0),(21,NULL,'\0',NULL,'Lunch Box','LB',0),(22,NULL,'\0',NULL,'Sushi Roll','SR',0);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,NULL,'\0',NULL,'WALK-IN','','----------',NULL),(2,NULL,'\0',NULL,'Marcia','Patton','8484970468',NULL),(3,NULL,'\0',NULL,'Blythe','Mcintyre','6071604771',NULL),(4,NULL,'\0',NULL,'Dieter','Anthony','9124508649',NULL),(5,NULL,'\0',NULL,'Dillon','Figueroa','5383005144',NULL),(6,NULL,'\0',NULL,'Regina','Woodward','7892042013',NULL),(7,NULL,'\0',NULL,'Shea','Montgomery','5438051213',NULL),(8,NULL,'\0',NULL,'Larissa','Larsen','1913574698',NULL),(9,NULL,'\0',NULL,'Imani','Gill','2536698995',NULL),(10,NULL,'\0',NULL,'Wesley','Pitts','8892511499',NULL),(11,NULL,'\0',NULL,'Tara','Powell','1418776365',NULL),(12,NULL,'\0',NULL,'Martin','Cruz','7491846596',NULL),(13,NULL,'\0',NULL,'Samuel','Lloyd','6891945178',NULL),(14,NULL,'\0',NULL,'Rigel','Gamble','3714879693',NULL),(15,NULL,'\0',NULL,'Glenna','Molina','6368170878',NULL),(16,NULL,'\0',NULL,'Gareth','Guy','5093213448',NULL),(17,NULL,'\0',NULL,'Jelani','Cross','8406301530',NULL),(18,NULL,'\0',NULL,'Lois','Delacruz','8287980932',NULL),(19,NULL,'\0',NULL,'Inez','Thompson','9553740360',NULL),(20,NULL,'\0',NULL,'Cameron','Shannon','8459002003',NULL),(21,NULL,'\0',NULL,'Cedric','Leon','8542057193',NULL),(22,NULL,'\0',NULL,'Halee','Henry','6278496706',NULL),(23,NULL,'\0',NULL,'Rebecca','Humphrey','5492557383',NULL),(24,NULL,'\0',NULL,'Vernon','Moon','3884389701',NULL),(25,NULL,'\0',NULL,'Jakeem','Mccarty','9391541250',NULL),(26,NULL,'\0',NULL,'Kiona','Davis','2969759640',NULL),(27,NULL,'\0',NULL,'Harlan','Cantrell','5955417274',NULL),(28,NULL,'\0',NULL,'Rigel','Robertson','5247761342',NULL),(29,NULL,'\0',NULL,'Astra','Morales','3991083393',NULL),(30,NULL,'\0',NULL,'William','Wilkins','6112989165',NULL),(31,NULL,'\0',NULL,'Amity','Hopper','1628440512',NULL),(32,NULL,'\0',NULL,'Vladimir','Gay','8034368092',NULL),(33,NULL,'\0',NULL,'Ella','Schroeder','9755178218',NULL),(34,NULL,'\0',NULL,'Ivory','Morales','8647706888',NULL),(35,NULL,'\0',NULL,'Lacey','Moss','1804057091',NULL),(36,NULL,'\0',NULL,'Brandon','Pennington','2675568146',NULL),(37,NULL,'\0',NULL,'Aline','Walls','7007171434',NULL),(38,NULL,'\0',NULL,'Christian','Berry','5625719852',NULL),(39,NULL,'\0',NULL,'Nathaniel','Diaz','2381985054',NULL),(40,NULL,'\0',NULL,'Clare','Leonard','7547954606',NULL),(41,NULL,'\0',NULL,'Cecilia','Vargas','6601538337',NULL),(42,NULL,'\0',NULL,'Nadine','Larson','6757520765',NULL),(43,NULL,'\0',NULL,'Ora','Stanton','5243548457',NULL),(44,NULL,'\0',NULL,'Tyler','Burt','8824231416',NULL),(45,NULL,'\0',NULL,'Dean','Reyes','2475051683',NULL),(46,NULL,'\0',NULL,'Odette','Conrad','1045115766',NULL),(47,NULL,'\0',NULL,'Adara','Sparks','3865893071',NULL),(48,NULL,'\0',NULL,'Chantale','Mathis','1206383006',NULL),(49,NULL,'\0',NULL,'Rowan','Woodard','4289516061',NULL),(50,NULL,'\0',NULL,'Leandra','Hansen','2055188546',NULL),(51,NULL,'\0',NULL,'Britanney','Butler','5576191320',NULL),(52,NULL,'\0',NULL,'Quyn','Kirk','3025073451',NULL),(53,NULL,'\0',NULL,'Hedda','Delacruz','5896752726',NULL),(54,NULL,'\0',NULL,'Carolyn','Osborne','5804103781',NULL),(55,NULL,'\0',NULL,'Cruz','Velazquez','4724410460',NULL),(56,NULL,'\0',NULL,'Cruz','Johnston','3687769718',NULL),(57,NULL,'\0',NULL,'Tamekah','White','4336076301',NULL),(58,NULL,'\0',NULL,'Odessa','Dodson','2088927208',NULL),(59,NULL,'\0',NULL,'Zoe','Schwartz','8224436812',NULL),(60,NULL,'\0',NULL,'Imani','Carney','7544086063',NULL),(61,NULL,'\0',NULL,'Tatum','Good','2061554741',NULL),(62,NULL,'\0',NULL,'Astra','Meadows','6487786780',NULL),(63,NULL,'\0',NULL,'Neve','Neal','9271806059',NULL),(64,NULL,'\0',NULL,'Thor','Gordon','8623289607',NULL),(65,NULL,'\0',NULL,'Jaime','Gutierrez','1791298975',NULL),(66,NULL,'\0',NULL,'Rae','Mathis','8508780947',NULL),(67,NULL,'\0',NULL,'Chandler','Francis','4862094887',NULL),(68,NULL,'\0',NULL,'Lucas','Turner','5536023865',NULL),(69,NULL,'\0',NULL,'Amal','Wright','3388432857',NULL),(70,NULL,'\0',NULL,'Henry','Bridges','7031489314',NULL),(71,NULL,'\0',NULL,'Tasha','Wall','8595124423',NULL),(72,NULL,'\0',NULL,'Aurora','Bell','6978075835',NULL),(73,NULL,'\0',NULL,'Hayfa','Blair','8863395459',NULL),(74,NULL,'\0',NULL,'Aileen','Patton','5951990899',NULL),(75,NULL,'\0',NULL,'Quinn','Owens','8751988400',NULL),(76,NULL,'\0',NULL,'Claudia','Cox','5435567982',NULL),(77,NULL,'\0',NULL,'Candice','Travis','7714852204',NULL),(78,NULL,'\0',NULL,'Moses','Macdonald','8598109594',NULL),(79,NULL,'\0',NULL,'Warren','Gates','9118540815',NULL),(80,NULL,'\0',NULL,'Dacey','Espinoza','4053012768',NULL),(81,NULL,'\0',NULL,'Hadassah','Simmons','6832499560',NULL),(82,NULL,'\0',NULL,'Jerome','Singleton','7161493763',NULL),(83,NULL,'\0',NULL,'Dawn','Williamson','7495665356',NULL),(84,NULL,'\0',NULL,'Rajah','Hooper','3678173582',NULL),(85,NULL,'\0',NULL,'Channing','Bell','9485462372',NULL),(86,NULL,'\0',NULL,'Akeem','Hess','8853124433',NULL),(87,NULL,'\0',NULL,'Troy','Joseph','7484460039',NULL),(88,NULL,'\0',NULL,'Chelsea','Glenn','1341142536',NULL),(89,NULL,'\0',NULL,'Ila','Vance','7161246690',NULL),(90,NULL,'\0',NULL,'Quamar','Vazquez','1898805053',NULL),(91,NULL,'\0',NULL,'Walker','Aguilar','8546696267',NULL),(92,NULL,'\0',NULL,'Garrison','English','5921070209',NULL),(93,NULL,'\0',NULL,'Paki','Roberson','1732657214',NULL),(94,NULL,'\0',NULL,'Brielle','Ballard','8336125811',NULL),(95,NULL,'\0',NULL,'Lee','Rogers','6046120889',NULL),(96,NULL,'\0',NULL,'Tatiana','Bates','3842122463',NULL),(97,NULL,'\0',NULL,'Cathleen','Marshall','2523622242',NULL),(98,NULL,'\0',NULL,'Justin','Emerson','1758123793',NULL),(99,NULL,'\0',NULL,'Blaze','Whitfield','5569776828',NULL),(100,NULL,'\0',NULL,'Brynn','Harris','5356387354',NULL),(101,NULL,'\0',NULL,'weng','','4491665',NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
  `taxed` bit(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `FK_25ty62u8nyfcw1dwu6jj3efn4` (`category_id`),
  CONSTRAINT `FK_25ty62u8nyfcw1dwu6jj3efn4` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_item`
--

LOCK TABLES `menu_item` WRITE;
/*!40000 ALTER TABLE `menu_item` DISABLE KEYS */;
INSERT INTO `menu_item` VALUES (1,NULL,'\0',NULL,'Egg Roll (1)',1.65,'A1',0,1,1),(2,NULL,'\0',NULL,'Fried Won Ton (8)',3.25,'A2',0,1,1),(3,NULL,'\0',NULL,'Fried Crabmeat Won Ton w/ Cream Cheese (4)',4.75,'A3',0,1,1),(4,NULL,'\0',NULL,'Fried Fantail Shrimp (4)',5.50,'A4',0,1,1),(5,NULL,'\0',NULL,'Fried Chicken Wing (6)',5.50,'A5',0,1,1),(6,NULL,'\0',NULL,'Teriyaki Beef (8)',5.75,'A6',0,1,1),(7,NULL,'\0',NULL,'Chicken Lettuce Wrap',8.95,'A7',0,1,1),(8,NULL,'\0',NULL,'Fried Dumpling (6)',5.95,'A8',0,1,1),(9,NULL,'\0',NULL,'Spare Ribs (4)',5.95,'A9',0,1,1),(10,NULL,'\0',NULL,'Bo-Bo Platter',10.95,'A10',0,1,1),(11,NULL,'\0',NULL,'Salad (S)',1.50,'A11',0,1,1),(12,NULL,'\0',NULL,'Salad (L)',3.00,'A11',0,1,1),(13,NULL,'\0',NULL,'Hot & Sour Soup (S)',2.25,'T1',0,2,1),(14,NULL,'\0',NULL,'Hot & Sour Soup (L)',6.25,'T1',0,2,1),(15,NULL,'\0',NULL,'Won Ton Soup',6.25,'T2',0,2,1),(16,NULL,'\0',NULL,'Egg Drop Soup (S)',1.75,'T3',0,2,1),(17,NULL,'\0',NULL,'Egg Drop Soup (L)',4.75,'T3',0,2,1),(18,NULL,'\0',NULL,'Tom Yum Soup',6.95,'T4',0,2,1),(19,NULL,'\0',NULL,'Seafood Soup',7.50,'T5',0,2,1),(20,NULL,'\0',NULL,'House Special Soup',6.95,'T6',0,2,1),(21,NULL,'\0',NULL,'Soda',2.25,'D1',0,3,1),(22,NULL,'\0',NULL,'Coffee',2.25,'D2',0,3,1),(23,NULL,'\0',NULL,'Ice Tea',2.25,'D3',0,3,1),(24,NULL,'\0',NULL,'Hot Tea',2.25,'D4',0,3,1),(25,NULL,'\0',NULL,'Bowl of Lemon',0.90,'D5',0,3,1),(26,NULL,'\0',NULL,'Bowl of Sauce',0.90,'D6',0,3,1),(27,NULL,'\0',NULL,'Orange Beef',13.95,'H1',0,4,1),(28,NULL,'\0',NULL,'House Chicken',11.95,'H2',0,4,1),(29,NULL,'\0',NULL,'Perfect Shrimp w/ Chicken',13.45,'H3',0,4,1),(30,NULL,'\0',NULL,'Three Kinds w/ Spicy Sauce',13.45,'H4',0,4,1),(31,NULL,'\0',NULL,'Happy Family',13.45,'H5',0,4,1),(32,NULL,'\0',NULL,'Seafood Imperial',14.45,'H6',0,4,1),(33,NULL,'\0',NULL,'Jumbo Shrimp w/ Scallop',14.45,'H7',0,4,1),(34,NULL,'\0',NULL,'Rainbow Jumbo Shrimp',13.95,'H8',0,4,1),(35,NULL,'\0',NULL,'Crispy Fried Duck',13.95,'H9',0,4,1),(36,NULL,'\0',NULL,'Peking Duck',32.00,'H10',0,4,1),(37,NULL,'\0',NULL,'Orange Chicken',12.95,'H11',0,4,1),(38,NULL,'\0',NULL,'Chicken Nugget',5.25,'K1',0,5,1),(39,NULL,'\0',NULL,'Fried Chicken Wing (6)',5.75,'K2',0,5,1),(40,NULL,'\0',NULL,'French Fries',3.00,'K3',0,5,1),(41,NULL,'\0',NULL,'Chicken w/ Cashew Nuts',11.45,'C1',0,6,1),(42,NULL,'\0',NULL,'Sizzling Rice Chicken',11.45,'C2',0,6,1),(43,NULL,'\0',NULL,'Chicken w/ Snow Peas',11.45,'C3',0,6,1),(44,NULL,'\0',NULL,'Sesame Chicken',11.45,'C4',0,6,1),(45,NULL,'\0',NULL,'Tiny Spicy Chicken',11.45,'C5',0,6,1),(46,NULL,'\0',NULL,'Kung Bo Chicken',11.45,'C6',0,6,1),(47,NULL,'\0',NULL,'Chicken w/ Garlic Sauce',11.45,'C7',0,6,1),(48,NULL,'\0',NULL,'Mandarin Chicken',11.45,'C8',0,6,1),(49,NULL,'\0',NULL,'Moo Goo Gai Pan',11.45,'C9',0,6,1),(50,NULL,'\0',NULL,'Chicken w/ Almond',11.45,'C10',0,6,1),(51,NULL,'\0',NULL,'Sweet & Sour Chicken',11.45,'C11',0,6,1),(52,NULL,'\0',NULL,'Lemon Chicken',11.45,'C12',0,6,1),(53,NULL,'\0',NULL,'Ginger Chicken',11.45,'C13',0,6,1),(54,NULL,'\0',NULL,'Chicken w/ Fresh Broccoli',11.45,'C14',0,6,1),(55,NULL,'\0',NULL,'Chicken w/ Black Bean Sauce',11.45,'C15',0,6,1),(56,NULL,'\0',NULL,'Fried Chicken w/ Almond',11.45,'C16',0,6,1),(57,NULL,'\0',NULL,'Curry Chicken',11.45,'C17',0,6,1),(58,NULL,'\0',NULL,'Moo Shu Chicken',11.45,'C18',0,6,1),(59,NULL,'\0',NULL,'Shredded Chicken w/ Szechuan Style',11.45,'C19',0,6,1),(60,NULL,'\0',NULL,'Fried Sesame Chicken',11.45,'C20',0,6,1),(61,NULL,'\0',NULL,'Mongolian Chicken',11.45,'C21',0,6,1),(62,NULL,'\0',NULL,'Sweet & Sour Shrimp',11.75,'S1',0,7,1),(63,NULL,'\0',NULL,'Kwoh Ba Shrimp',12.45,'S2',0,7,1),(64,NULL,'\0',NULL,'Snow Peas Shrimp',12.25,'S3',0,7,1),(65,NULL,'\0',NULL,'Kung Bo Shrimp',12.25,'S4',0,7,1),(66,NULL,'\0',NULL,'Wonderful Shrimp',12.75,'S5',0,7,1),(67,NULL,'\0',NULL,'Kan Saw Shrimp',12.75,'S6',0,7,1),(68,NULL,'\0',NULL,'Shrimp w/ Lobster Sauce',12.25,'S7',0,7,1),(69,NULL,'\0',NULL,'Peking Shrimp',12.25,'S8',0,7,1),(70,NULL,'\0',NULL,'Shrimp w/ Cashew Nuts',12.50,'S9',0,7,1),(71,NULL,'\0',NULL,'Imperial Shrimp',12.25,'S10',0,7,1),(72,NULL,'\0',NULL,'Shrimp w/ Black Bean Sauce',12.95,'S11',0,7,1),(73,NULL,'\0',NULL,'Shrimp w/ Garlic Sauce',12.25,'S12',0,7,1),(74,NULL,'\0',NULL,'Seafood Deluxe',13.95,'S13',0,7,1),(75,NULL,'\0',NULL,'Scallop w/ Garlic Sauce',15.75,'S14',0,7,1),(76,NULL,'\0',NULL,'Butterfly Shrimp',13.95,'S15',0,7,1),(77,NULL,'\0',NULL,'Tiny Spicy Shrimp',12.45,'S16',0,7,1),(78,NULL,'\0',NULL,'Moo Shu Vegetables',8.95,'V1',0,8,1),(79,NULL,'\0',NULL,'Vegetables Delight',7.45,'V2',0,8,1),(80,NULL,'\0',NULL,'Bamboo Shoots w/ BlackMushrooms',7.45,'V3',0,8,1),(81,NULL,'\0',NULL,'Broccoli w/ Garlic Sauce',7.45,'V4',0,8,1),(82,NULL,'\0',NULL,'Bean Curd Szechuan Style w/ Pork',8.25,'V5',0,8,1),(83,NULL,'\0',NULL,'Bean Curd w/ Black Mushrooms',9.95,'V6',0,8,1),(84,NULL,'\0',NULL,'Sauteed String Beans',8.45,'V7',0,8,1),(85,NULL,'\0',NULL,'Vegetable w/ Tofu',8.95,'V8',0,8,1),(86,NULL,'\0',NULL,'Eggplant w/ Garlic Sauce',8.25,'V9',0,8,1),(87,NULL,'\0',NULL,'Kung Bo Tofu',8.75,'V10',0,8,1),(88,NULL,'\0',NULL,'Grand Chop Suey',10.45,'CS1',0,9,1),(89,NULL,'\0',NULL,'Chicken Chop Suey',9.95,'CS2',0,9,1),(90,NULL,'\0',NULL,'Beef Chop Suey',9.95,'CS3',0,9,1),(91,NULL,'\0',NULL,'Shrimp Chop Suey',10.65,'CS4',0,9,1),(92,NULL,'\0',NULL,'Chicken Egg Foo Young',9.45,'E1',0,10,1),(93,NULL,'\0',NULL,'Shrimp Egg Foo Young',10.45,'E2',0,10,1),(94,NULL,'\0',NULL,'Roast Port Egg Foo Young',9.45,'E3',0,10,1),(95,NULL,'\0',NULL,'House Special Egg Foo Young',10.45,'E4',0,10,1),(96,NULL,'\0',NULL,'Vegetable Egg Foo Young',9.45,'E5',0,10,1),(97,NULL,'\0',NULL,'Chicken Fried Rice',7.25,'FR1',0,11,1),(98,NULL,'\0',NULL,'Roast Pork Fried Rice',7.25,'FR2',0,11,1),(99,NULL,'\0',NULL,'Beef Fried Rice',7.45,'FR3',0,11,1),(100,NULL,'\0',NULL,'Shrimp Fried Rice',8.85,'FR4',0,11,1),(101,NULL,'\0',NULL,'Vegetables Fried Rice',7.25,'FR5',0,11,1),(102,NULL,'\0',NULL,'House Special Fried Rice',8.45,'FR6',0,11,1),(103,NULL,'\0',NULL,'Yong Chow Fried Rice',8.85,'FR7',0,11,1),(104,NULL,'\0',NULL,'Chicken Chow Mein',7.75,'CM1',0,12,1),(105,NULL,'\0',NULL,'Beef Chow Mein',7.75,'CM2',0,12,1),(106,NULL,'\0',NULL,'Pork Chow Mein',7.25,'CM3',0,12,1),(107,NULL,'\0',NULL,'Shrimp Chow Mein',8.80,'CM4',0,12,1),(108,NULL,'\0',NULL,'House Chow Mein',8.75,'CM5',0,12,1),(109,NULL,'\0',NULL,'Beef Lo Mein',8.25,'LM1',0,13,1),(110,NULL,'\0',NULL,'Chicken Lo Mein',8.25,'LM2',0,13,1),(111,NULL,'\0',NULL,'Shrimp Lo Mein',8.85,'LM3',0,13,1),(112,NULL,'\0',NULL,'Roast Pork Lo Mein',7.75,'LM4',0,13,1),(113,NULL,'\0',NULL,'House Special Lo Mein',8.75,'LM5',0,13,1),(114,NULL,'\0',NULL,'Pan Fried Noodles',14.45,'LM6',0,13,1),(115,NULL,'\0',NULL,'Vegetables Lo Mein',7.75,'LM7',0,13,1),(116,NULL,'\0',NULL,'Plain Lo Mein',5.25,'LM8',0,13,1),(117,NULL,'\0',NULL,'House Special Noodle Soup',8.45,'LM9',0,13,1),(118,NULL,'\0',NULL,'House Special Pan Fried Rice Vermicelli',8.75,'LM10',0,13,1),(119,NULL,'\0',NULL,'Moo Shu Pork',9.95,'P1',0,14,1),(120,NULL,'\0',NULL,'Sweet & Sour Pork',9.95,'P2',0,14,1),(121,NULL,'\0',NULL,'Pork w/ Garlic Sauce',9.95,'P3',0,14,1),(122,NULL,'\0',NULL,'Twice Cooked Pork',9.95,'P4',0,14,1),(123,NULL,'\0',NULL,'Peking Style Pork',9.95,'P5',0,14,1),(124,NULL,'\0',NULL,'Hunan Spicy Pork',9.95,'P6',0,14,1),(125,NULL,'\0',NULL,'Mongolian Pork',9.95,'P7',0,14,1),(126,NULL,'\0',NULL,'Roast Pork w/ Vegetables',9.95,'P8',0,14,1),(127,NULL,'\0',NULL,'Family Style Pork',9.95,'P9',0,14,1),(128,NULL,'\0',NULL,'Moo Shu Beef',10.95,'B1',0,15,1),(129,NULL,'\0',NULL,'Snow Peas Beef',10.95,'B2',0,15,1),(130,NULL,'\0',NULL,'Kung Bo Beef',10.95,'B3',0,15,1),(131,NULL,'\0',NULL,'Yu Shang Beef',10.95,'B4',0,15,1),(132,NULL,'\0',NULL,'Green Pepper Beef',10.95,'B5',0,15,1),(133,NULL,'\0',NULL,'Mongolian Beef',10.95,'B6',0,15,1),(134,NULL,'\0',NULL,'Beef w/ Black Mushrooms',10.95,'B7',0,15,1),(135,NULL,'\0',NULL,'Empoeror\'s Delight',10.95,'B8',0,15,1),(136,NULL,'\0',NULL,'Beef w/ Broccoli',10.95,'B9',0,15,1),(137,NULL,'\0',NULL,'Hunan Beef',10.95,'B10',0,15,1),(138,NULL,'\0',NULL,'Shredded Beef Szechuan Style',10.95,'B11',0,15,1),(139,NULL,'\0',NULL,'Chicken Chow Mein',8.26,'L1',0,16,1),(140,NULL,'\0',NULL,'Moo Goo Gal Pan',8.26,'L2',0,16,1),(141,NULL,'\0',NULL,'Sweet and Sour Chicken',8.26,'L3',0,16,1),(142,NULL,'\0',NULL,'Kong Bo Chicken',8.26,'L4',0,16,1),(143,NULL,'\0',NULL,'Tiny Spicy Chicken',8.26,'L5',0,16,1),(144,NULL,'\0',NULL,'Sweet & Sour Pork',8.26,'L6',0,16,1),(145,NULL,'\0',NULL,'Pork w/ Garlic Sauce',8.26,'L7',0,16,1),(146,NULL,'\0',NULL,'Twice Cooked Pork',8.26,'L8',0,16,1),(147,NULL,'\0',NULL,'Family Style Pork',8.26,'L9',0,16,1),(148,NULL,'\0',NULL,'Beef or Chicken w/ Broccoli',8.26,'L10',0,16,1),(149,NULL,'\0',NULL,'Beef w/ Green Peppers',8.26,'L11',0,16,1),(150,NULL,'\0',NULL,'Mongolian Beef',8.26,'L12',0,16,1),(151,NULL,'\0',NULL,'Shrimp w/ Lobster Sauce',8.26,'L13',0,16,1),(152,NULL,'\0',NULL,'Kung Bo Shrimp',8.26,'L14',0,16,1),(153,NULL,'\0',NULL,'Roast Pork Egg Foo Young',8.26,'L15',0,16,1),(154,NULL,'\0',NULL,'Roast Pork or Chicken Lo Mein',8.26,'L16',0,16,1),(155,NULL,'\0',NULL,'House Special Noodle Soup',8.26,'L17',0,16,1),(156,NULL,'\0',NULL,'Almond Chicken',8.26,'L18',0,16,1),(157,NULL,'\0',NULL,'Vegetable Delight',8.26,'L19',0,16,1),(158,NULL,'\0',NULL,'Shrimp or Chicken Chop Suey',8.26,'L20',0,16,1),(159,NULL,'\0',NULL,'Sweet & Sour Shrimp',8.26,'L21',0,16,1),(160,NULL,'\0',NULL,'Fish w/ Vegetables',8.26,'L22',0,16,1),(161,NULL,'\0',NULL,'Kan Saw Fish',8.26,'L23',0,16,1),(162,NULL,'\0',NULL,'Sauteed Chicken or Beef Szechuan Style',8.26,'L24',0,16,1),(163,NULL,'\0',NULL,'Kung Bo Tofu',8.26,'L25',0,16,1),(164,NULL,'\0',NULL,'Curry Chicken',8.26,'L26',0,16,1),(165,NULL,'\0',NULL,'Sauteed Chicken with String Beans',8.26,'L27',0,16,1),(166,NULL,'\0',NULL,'Edamame',3.95,'AP1',0,17,1),(167,NULL,'\0',NULL,'Seaweed Salad',3.95,'AP2',0,17,1),(168,NULL,'\0',NULL,'Squid Salad',4.95,'AP3',0,17,1),(169,NULL,'\0',NULL,'Crabmeat Salad',5.50,'AP4',0,17,1),(170,NULL,'\0',NULL,'Age Tofu',4.50,'AP5',0,17,1),(171,NULL,'\0',NULL,'Japanese Dumpling',3.95,'AP6',0,17,1),(172,NULL,'\0',NULL,'Seafood Sunomono',5.00,'AP7',0,17,1),(173,NULL,'\0',NULL,'Shrimp Tempura',7.00,'AP8',0,17,1),(174,NULL,'\0',NULL,'Sashimi Red Snapper Tuna & Salmon (7)',12.50,'AP9',0,17,1),(175,NULL,'\0',NULL,'Sashimi Salmon (7)',11.95,'AP10',0,17,1),(176,NULL,'\0',NULL,'Sashimi Tuna (7)',12.95,'AP11',0,17,1),(177,NULL,'\0',NULL,'Sashimi (7) & California Roll (6)',16.50,'SCA',0,18,1),(178,NULL,'\0',NULL,'Sashimi (10) & Spicy Tuna Roll (6)',23.00,'SCB',0,18,1),(179,NULL,'\0',NULL,'Sashimi Deluxe (23)',35.00,'SCC',0,18,1),(180,NULL,'\0',NULL,'Crab Stick (Kani)',3.00,'N2',0,19,1),(181,NULL,'\0',NULL,'Eel (Unagi)',4.00,'N3',0,19,1),(182,NULL,'\0',NULL,'Egg (Tamago)',3.00,'N4',0,19,1),(183,NULL,'\0',NULL,'Octopus (Take)',4.50,'N5',0,19,1),(184,NULL,'\0',NULL,'Squid (lka)',4.00,'N6',0,19,1),(185,NULL,'\0',NULL,'Scallop (Hotategal)',3.50,'N7',0,19,1),(186,NULL,'\0',NULL,'Shrimp (Ebi)',3.00,'N8',0,19,1),(187,NULL,'\0',NULL,'Red Snapper (Tail)',3.00,'N9',0,19,1),(188,NULL,'\0',NULL,'Smoked Salmon',4.00,'N10',0,19,1),(189,NULL,'\0',NULL,'Salmon (Sake)',3.50,'N11',0,19,1),(190,NULL,'\0',NULL,'White Tuna',3.50,'N12',0,19,1),(191,NULL,'\0',NULL,'Tuna (Maguro)',4.00,'N13',0,19,1),(192,NULL,'\0',NULL,'Flying Fish Egg (Massago)',4.00,'N15',0,19,1),(193,NULL,'\0',NULL,'Yellow Tail (Hamachi)',5.50,'N16',0,19,1),(194,NULL,'\0',NULL,'Salmon Skin Handroll',3.50,'HR17',0,19,1),(195,NULL,'\0',NULL,'California Roll & Sashimi (5)',8.95,'LS1',0,20,1),(196,NULL,'\0',NULL,'Futo Maki & Shrimp Tempura Roll',8.95,'LS2',0,20,1),(197,NULL,'\0',NULL,'Lunch Box A',8.95,'LBA',0,21,1),(198,NULL,'\0',NULL,'Lunch Box B',8.95,'LBB',0,21,1),(199,NULL,'\0',NULL,'Lunch Box C',8.95,'LBC',0,21,1),(200,NULL,'\0',NULL,'Tuna Roll',4.50,'SR1',0,22,1),(201,NULL,'\0',NULL,'Salmon Roll',4.25,'SR2',0,22,1),(202,NULL,'\0',NULL,'Eel Roll',4.95,'SR3',0,22,1),(203,NULL,'\0',NULL,'California Roll',3.95,'SR4',0,22,1),(204,NULL,'\0',NULL,'Red Dragon Roll (Tuna)',10.95,'SR5',0,22,1),(205,NULL,'\0',NULL,'Spicy Tuna Roll',5.95,'SR6',0,22,1),(206,NULL,'\0',NULL,'Spicy Salmon Roll',5.25,'SR7',0,22,1),(207,NULL,'\0',NULL,'Futo Maki Roll',5.95,'SR8',0,22,1),(208,NULL,'\0',NULL,'Shrimp Tempura Roll',6.50,'SR9',0,22,1),(209,NULL,'\0',NULL,'Spider Roll',7.50,'SR10',0,22,1),(210,NULL,'\0',NULL,'Philadelphia Roll',6.00,'SR11',0,22,1),(211,NULL,'\0',NULL,'Crunch Roll',8.75,'SR12',0,22,1),(212,NULL,'\0',NULL,'Fuji Maki',9.75,'SR13',0,22,1),(213,NULL,'\0',NULL,'Fan Ka Zan Roll (Volcano Roll)',9.75,'SR14',0,22,1),(214,NULL,'\0',NULL,'Rainbow Roll',9.95,'SR15',0,22,1),(215,NULL,'\0',NULL,'Black Dragon Roll',12.95,'SR16',0,22,1),(216,NULL,'\0',NULL,'Soybean Rock \'n Roll',13.95,'SR17',0,22,1),(217,NULL,'\0',NULL,'Yama Roll',13.95,'SR18',0,22,1),(218,NULL,'\0',NULL,'Smoked Salmon Roll',5.50,'SR19',0,22,1),(219,NULL,'\0',NULL,'Chef Special Roll',13.95,'SR20',0,22,1),(220,NULL,'\0',NULL,'Snow Crab Roll',6.00,'SR21',0,22,1),(221,NULL,'\0',NULL,'Spicy Cucumber Wrap Roll',10.95,'SR22',0,22,1),(222,NULL,'\0',NULL,'Wok Special Roll',12.95,'SR23',0,22,1),(223,NULL,'\0',NULL,'Tiger Eye Roll',12.95,'SR24',0,22,1),(224,NULL,'\0',NULL,'Hawaiian Roll',12.95,'SR25',0,22,1),(225,NULL,'\0',NULL,'Tsunami Roll',12.95,'SR26',0,22,1),(226,NULL,'\0',NULL,'Crater Roll',12.95,'SR27',0,22,1),(227,NULL,'\0',NULL,'Golden Dragon Roll',9.95,'SR28',0,22,1),(228,NULL,'\0',NULL,'Cucumber Roll',3.95,'SR29',0,22,1);
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
INSERT INTO `menu_item_printers` VALUES (48,'df'),(48,'c'),(48,'@#tg'),(58,'df'),(58,'c'),(58,'@#tg'),(76,'df'),(76,'c'),(76,'@#tg'),(78,'df'),(78,'c'),(78,'@#tg'),(128,'df'),(128,'c'),(128,'@#tg'),(166,'s'),(167,'s'),(168,'s'),(169,'s'),(172,'s'),(173,'s'),(174,'s'),(175,'s'),(176,'s'),(177,'s'),(178,'s'),(179,'s'),(180,'s'),(181,'s'),(182,'s'),(183,'s'),(184,'s'),(185,'s'),(186,'s'),(187,'s'),(188,'s'),(189,'s'),(190,'s'),(191,'s'),(192,'s'),(193,'s'),(194,'s'),(195,'s'),(196,'s'),(197,'s'),(198,'s'),(199,'s'),(200,'s'),(201,'s'),(202,'s'),(203,'s'),(204,'s'),(205,'s'),(206,'s'),(207,'s'),(208,'s'),(209,'s'),(210,'s'),(211,'s'),(212,'s'),(213,'s'),(214,'s'),(215,'s'),(216,'s'),(217,'s'),(218,'s'),(219,'s'),(220,'s'),(221,'s'),(222,'s'),(223,'s'),(224,'s'),(225,'s'),(226,'s'),(227,'s'),(228,'s'),(11,'@#tg'),(12,'@#tg'),(13,'@#tg'),(14,'@#tg'),(15,'c'),(15,'@#tg'),(16,'@#tg'),(17,'@#tg'),(18,'c'),(18,'@#tg'),(19,'c'),(19,'@#tg'),(20,'c'),(20,'@#tg'),(21,'@#tg'),(22,'@#tg'),(23,'@#tg'),(24,'@#tg'),(25,'@#tg'),(26,'@#tg'),(27,'c'),(27,'@#tg'),(28,'c'),(28,'@#tg'),(29,'c'),(29,'@#tg'),(30,'c'),(30,'@#tg'),(31,'c'),(31,'@#tg'),(32,'c'),(32,'@#tg'),(33,'c'),(33,'@#tg'),(34,'c'),(34,'@#tg'),(35,'df'),(35,'@#tg'),(36,'df'),(36,'@#tg'),(37,'c'),(37,'@#tg'),(38,'df'),(38,'@#tg'),(39,'df'),(39,'@#tg'),(40,'df'),(40,'@#tg'),(41,'c'),(41,'@#tg'),(42,'c'),(42,'@#tg'),(43,'c'),(43,'@#tg'),(44,'c'),(44,'@#tg'),(45,'df'),(45,'@#tg'),(46,'df'),(46,'@#tg'),(47,'c'),(47,'@#tg'),(49,'c'),(49,'@#tg'),(50,'c'),(50,'@#tg'),(51,'df'),(51,'@#tg'),(52,'df'),(52,'c'),(52,'@#tg'),(53,'c'),(53,'@#tg'),(54,'c'),(54,'@#tg'),(55,'c'),(55,'@#tg'),(56,'df'),(56,'c'),(56,'@#tg'),(57,'c'),(57,'@#tg'),(59,'c'),(59,'@#tg'),(60,'c'),(60,'@#tg'),(61,'c'),(61,'@#tg'),(62,'df'),(62,'@#tg'),(63,'c'),(63,'@#tg'),(64,'c'),(64,'@#tg'),(65,'c'),(65,'@#tg'),(66,'c'),(66,'@#tg'),(67,'c'),(67,'@#tg'),(68,'c'),(68,'@#tg'),(69,'c'),(69,'@#tg'),(70,'c'),(70,'@#tg'),(71,'c'),(71,'@#tg'),(72,'c'),(72,'@#tg'),(73,'c'),(73,'@#tg'),(74,'c'),(74,'@#tg'),(75,'c'),(75,'@#tg'),(77,'df'),(77,'@#tg'),(79,'c'),(79,'@#tg'),(80,'c'),(80,'@#tg'),(81,'c'),(81,'@#tg'),(82,'c'),(82,'@#tg'),(83,'c'),(83,'@#tg'),(84,'c'),(84,'@#tg'),(85,'c'),(85,'@#tg'),(86,'c'),(86,'@#tg'),(87,'c'),(87,'@#tg'),(88,'c'),(88,'@#tg'),(89,'c'),(89,'@#tg'),(90,'c'),(90,'@#tg'),(91,'c'),(91,'@#tg'),(92,'c'),(92,'@#tg'),(93,'c'),(93,'@#tg'),(94,'c'),(94,'@#tg'),(95,'c'),(95,'@#tg'),(96,'c'),(96,'@#tg'),(97,'c'),(97,'@#tg'),(98,'c'),(98,'@#tg'),(99,'c'),(99,'@#tg'),(100,'c'),(100,'@#tg'),(101,'c'),(101,'@#tg'),(102,'c'),(102,'@#tg'),(103,'c'),(103,'@#tg'),(104,'c'),(104,'@#tg'),(105,'c'),(105,'@#tg'),(106,'c'),(106,'@#tg'),(107,'c'),(107,'@#tg'),(108,'c'),(108,'@#tg'),(109,'c'),(109,'@#tg'),(110,'c'),(110,'@#tg'),(111,'c'),(111,'@#tg'),(112,'c'),(112,'@#tg'),(113,'c'),(113,'@#tg'),(114,'c'),(114,'@#tg'),(115,'c'),(115,'@#tg'),(116,'c'),(116,'@#tg'),(117,'c'),(117,'@#tg'),(118,'c'),(118,'@#tg'),(119,'df'),(119,'c'),(119,'@#tg'),(120,'df'),(120,'@#tg'),(121,'c'),(121,'@#tg'),(122,'c'),(122,'@#tg'),(123,'c'),(123,'@#tg'),(124,'c'),(124,'@#tg'),(125,'c'),(125,'@#tg'),(126,'c'),(126,'@#tg'),(127,'c'),(127,'@#tg'),(129,'c'),(129,'@#tg'),(130,'c'),(130,'@#tg'),(131,'c'),(131,'@#tg'),(132,'c'),(132,'@#tg'),(133,'c'),(133,'@#tg'),(134,'c'),(134,'@#tg'),(135,'c'),(135,'@#tg'),(136,'c'),(136,'@#tg'),(137,'c'),(137,'@#tg'),(138,'c'),(138,'@#tg'),(171,'df'),(171,'@#tg'),(139,'c'),(139,'@#tg'),(140,'c'),(140,'@#tg'),(141,'df'),(141,'@#tg'),(142,'c'),(142,'@#tg'),(143,'df'),(143,'@#tg'),(144,'df'),(144,'@#tg'),(145,'c'),(145,'@#tg'),(146,'c'),(146,'@#tg'),(147,'c'),(147,'@#tg'),(148,'c'),(148,'@#tg'),(149,'c'),(149,'@#tg'),(150,'c'),(150,'@#tg'),(151,'c'),(151,'@#tg'),(152,'c'),(152,'@#tg'),(153,'c'),(153,'@#tg'),(154,'c'),(154,'@#tg'),(155,'c'),(155,'@#tg'),(156,'c'),(156,'@#tg'),(157,'c'),(157,'@#tg'),(158,'c'),(158,'@#tg'),(159,'c'),(159,'@#tg'),(160,'c'),(160,'@#tg'),(161,'c'),(161,'@#tg'),(162,'c'),(162,'@#tg'),(163,'c'),(163,'@#tg'),(164,'c'),(164,'@#tg'),(165,'c'),(165,'@#tg'),(3,'df'),(3,'@#tg'),(1,'df'),(1,'@#tg'),(2,'df'),(2,'@#tg'),(4,'df'),(4,'@#tg'),(5,'df'),(5,'@#tg'),(6,'df'),(6,'@#tg'),(7,'c'),(7,'@#tg'),(8,'df'),(8,'@#tg'),(9,'df'),(9,'@#tg'),(10,'df'),(10,'@#tg'),(170,'c'),(170,'@#tg');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modifier`
--

LOCK TABLES `modifier` WRITE;
/*!40000 ALTER TABLE `modifier` DISABLE KEYS */;
INSERT INTO `modifier` VALUES (1,NULL,'\0',NULL,'Beef',0.00,'+M.BEEF',0,0,1),(2,NULL,'\0',NULL,'Chicken',0.00,'+M.CHKN',0,0,1),(3,NULL,'\0',NULL,'Crab Meat',0.00,'+M.CRAB',0,0,1),(4,NULL,'\0',NULL,'Pork',0.00,'+M.PORK',0,0,1),(5,NULL,'\0',NULL,'Scallop',0.00,'+M.SCLP',0,0,1),(6,NULL,'\0',NULL,'Shrimp',0.00,'+M.SHRM',0,0,1),(7,NULL,'\0',NULL,'Beef',0.00,'-M.BEEF',1,0,1),(8,NULL,'\0',NULL,'Chicken',0.00,'-M.CHKN',1,0,1),(9,NULL,'\0',NULL,'Crab Meat',0.00,'-M.CRAB',1,0,1),(10,NULL,'\0',NULL,'Pork',0.00,'-M.PORK',1,0,1),(11,NULL,'\0',NULL,'Scallop',0.00,'-M.SCLP',1,0,1),(12,NULL,'\0',NULL,'Shrimp',0.00,'-M.SHRM',1,0,1),(13,NULL,'\0',NULL,'NO Meat',0.00,'-M.MEAT',1,0,1),(14,NULL,'\0',NULL,'NO Seafood',0.00,'-M.SEAF',1,0,1),(15,NULL,'\0',NULL,'Beef',0.00,'*M.BEEF',2,0,1),(16,NULL,'\0',NULL,'Chicken',0.00,'*M.CHKN',2,0,1),(17,NULL,'\0',NULL,'Crab Meat',0.00,'*M.CRAB',2,0,1),(18,NULL,'\0',NULL,'Pork',0.00,'*M.PORK',2,0,1),(19,NULL,'\0',NULL,'Scallop',0.00,'*M.SCLP',2,0,1),(20,NULL,'\0',NULL,'Shrimp',0.00,'*M.SHRM',2,0,1),(21,NULL,'\0',NULL,'Almond',0.00,'+V.ALMD',0,0,2),(22,NULL,'\0',NULL,'Baby Corn',0.00,'+V.BBCN',0,0,2),(23,NULL,'\0',NULL,'Bamboo',0.00,'+V.BAMB',0,0,2),(24,NULL,'\0',NULL,'Bean Sprout',0.00,'+V.BNSP',0,0,2),(25,NULL,'\0',NULL,'Broccoli',0.00,'+V.BROC',0,0,2),(26,NULL,'\0',NULL,'Cabbage',0.00,'+V.CABB',0,0,2),(27,NULL,'\0',NULL,'Carrot',0.00,'+V.CARR',0,0,2),(28,NULL,'\0',NULL,'Cashew',0.00,'+V.CASH',0,0,2),(29,NULL,'\0',NULL,'Celery',0.00,'+V.CLRY',0,0,2),(30,NULL,'\0',NULL,'Egg',0.00,'+V.EGG',0,0,2),(31,NULL,'\0',NULL,'Eggplant',0.00,'+V.EGGP',0,0,2),(32,NULL,'\0',NULL,'Garlic',0.00,'+V.GRLC',0,0,2),(33,NULL,'\0',NULL,'Ginger',0.00,'+V.GNGR',0,0,2),(34,NULL,'\0',NULL,'Green Peas',0.00,'+V.GPEA',0,0,2),(35,NULL,'\0',NULL,'Green Pepper',0.00,'+V.GPEP',0,0,2),(36,NULL,'\0',NULL,'Mixed Vegetables',0.00,'+V.MIXV',0,0,2),(37,NULL,'\0',NULL,'Black Mushrooms',0.00,'+V.BLMR',0,0,2),(38,NULL,'\0',NULL,'Sliced Mushrooms',0.00,'+V.SLMR',0,0,2),(39,NULL,'\0',NULL,'Straw Mushrooms',0.00,'+V.STMR',0,0,2),(40,NULL,'\0',NULL,'Nappa',0.00,'+V.NAPP',0,0,2),(41,NULL,'\0',NULL,'Green Onion',0.00,'+V.GONN',0,0,2),(42,NULL,'\0',NULL,'White Onion',0.00,'+V.WONN',0,0,2),(43,NULL,'\0',NULL,'Peanut',0.00,'+V.PNUT',0,0,2),(44,NULL,'\0',NULL,'Sesame',0.00,'+V.SSME',0,0,2),(45,NULL,'\0',NULL,'Snow Peas',0.00,'+V.SPEA',0,0,2),(46,NULL,'\0',NULL,'String Bean',0.00,'+V.STBN',0,0,2),(47,NULL,'\0',NULL,'Fried Tofu',0.00,'+V.FTOFU',0,0,2),(48,NULL,'\0',NULL,'Tofu',0.00,'+V.TOFU',0,0,2),(49,NULL,'\0',NULL,'Water Chestnut',0.00,'+V.WCNT',0,0,2),(50,NULL,'\0',NULL,'Zucchini',0.00,'+V.ZUCC',0,0,2),(51,NULL,'\0',NULL,'Almond',0.00,'-V.ALMD',1,0,2),(52,NULL,'\0',NULL,'Baby Corn',0.00,'-V.BBCN',1,0,2),(53,NULL,'\0',NULL,'Bamboo',0.00,'-V.BAMB',1,0,2),(54,NULL,'\0',NULL,'Bean Sprout',0.00,'-V.BNSP',1,0,2),(55,NULL,'\0',NULL,'Broccoli',0.00,'-V.BROC',1,0,2),(56,NULL,'\0',NULL,'Cabbage',0.00,'-V.CABB',1,0,2),(57,NULL,'\0',NULL,'Carrot',0.00,'-V.CARR',1,0,2),(58,NULL,'\0',NULL,'Cashew',0.00,'-V.CASH',1,0,2),(59,NULL,'\0',NULL,'Celery',0.00,'-V.CLRY',1,0,2),(60,NULL,'\0',NULL,'Egg',0.00,'-V.EGG',1,0,2),(61,NULL,'\0',NULL,'Eggplant',0.00,'-V.EGGP',1,0,2),(62,NULL,'\0',NULL,'Garlic',0.00,'-V.GRLC',1,0,2),(63,NULL,'\0',NULL,'Ginger',0.00,'-V.GNGR',1,0,2),(64,NULL,'\0',NULL,'Green Peas',0.00,'-V.GPEA',1,0,2),(65,NULL,'\0',NULL,'Green Pepper',0.00,'-V.GPEP',1,0,2),(66,NULL,'\0',NULL,'Mixed Vegetables',0.00,'-V.MIXV',1,0,2),(67,NULL,'\0',NULL,'Black Mushrooms',0.00,'-V.BLMR',1,0,2),(68,NULL,'\0',NULL,'Sliced Mushrooms',0.00,'-V.SLMR',1,0,2),(69,NULL,'\0',NULL,'Straw Mushrooms',0.00,'-V.STMR',1,0,2),(70,NULL,'\0',NULL,'Nappa',0.00,'-V.NAPP',1,0,2),(71,NULL,'\0',NULL,'Green Onion',0.00,'-V.GONN',1,0,2),(72,NULL,'\0',NULL,'White Onion',0.00,'-V.WONN',1,0,2),(73,NULL,'\0',NULL,'Peanut',0.00,'-V.PNUT',1,0,2),(74,NULL,'\0',NULL,'Sesame',0.00,'-V.SSME',1,0,2),(75,NULL,'\0',NULL,'Snow Peas',0.00,'-V.SPEA',1,0,2),(76,NULL,'\0',NULL,'String Bean',0.00,'-V.STBN',1,0,2),(77,NULL,'\0',NULL,'Fried Tofu',0.00,'-V.FTOFU',1,0,2),(78,NULL,'\0',NULL,'Tofu',0.00,'-V.TOFU',1,0,2),(79,NULL,'\0',NULL,'Water Chestnut',0.00,'-V.WCNT',1,0,2),(80,NULL,'\0',NULL,'Zucchini',0.00,'-V.ZUCC',1,0,2),(81,NULL,'\0',NULL,'Almond',0.00,'*V.ALMD',2,0,2),(82,NULL,'\0',NULL,'Baby Corn',0.00,'*V.BBCN',2,0,2),(83,NULL,'\0',NULL,'Bamboo',0.00,'*V.BAMB',2,0,2),(84,NULL,'\0',NULL,'Bean Sprout',0.00,'*V.BNSP',2,0,2),(85,NULL,'\0',NULL,'Broccoli',0.00,'*V.BROC',2,0,2),(86,NULL,'\0',NULL,'Cabbage',0.00,'*V.CABB',2,0,2),(87,NULL,'\0',NULL,'Carrot',0.00,'*V.CARR',2,0,2),(88,NULL,'\0',NULL,'Cashew',0.00,'*V.CASH',2,0,2),(89,NULL,'\0',NULL,'Celery',0.00,'*V.CLRY',2,0,2),(90,NULL,'\0',NULL,'Egg',0.00,'*V.EGG',2,0,2),(91,NULL,'\0',NULL,'Eggplant',0.00,'*V.EGGP',2,0,2),(92,NULL,'\0',NULL,'Garlic',0.00,'*V.GRLC',2,0,2),(93,NULL,'\0',NULL,'Ginger',0.00,'*V.GNGR',2,0,2),(94,NULL,'\0',NULL,'Green Peas',0.00,'*V.GPEA',2,0,2),(95,NULL,'\0',NULL,'Green Pepper',0.00,'*V.GPEP',2,0,2),(96,NULL,'\0',NULL,'Mixed Vegetables',0.00,'*V.MIXV',2,0,2),(97,NULL,'\0',NULL,'Black Mushrooms',0.00,'*V.BLMR',2,0,2),(98,NULL,'\0',NULL,'Sliced Mushrooms',0.00,'*V.SLMR',2,0,2),(99,NULL,'\0',NULL,'Straw Mushrooms',0.00,'*V.STMR',2,0,2),(100,NULL,'\0',NULL,'Nappa',0.00,'*V.NAPP',2,0,2),(101,NULL,'\0',NULL,'Green Onion',0.00,'*V.GONN',2,0,2),(102,NULL,'\0',NULL,'White Onion',0.00,'*V.WONN',2,0,2),(103,NULL,'\0',NULL,'Peanut',0.00,'*V.PNUT',2,0,2),(104,NULL,'\0',NULL,'Sesame',0.00,'*V.SSME',2,0,2),(105,NULL,'\0',NULL,'Snow Peas',0.00,'*V.SPEA',2,0,2),(106,NULL,'\0',NULL,'String Bean',0.00,'*V.STBN',2,0,2),(107,NULL,'\0',NULL,'Fried Tofu',0.00,'*V.FTOFU',2,0,2),(108,NULL,'\0',NULL,'Tofu',0.00,'*V.TOFU',2,0,2),(109,NULL,'\0',NULL,'Water Chestnut',0.00,'*V.WCNT',2,0,2),(110,NULL,'\0',NULL,'Zucchini',0.00,'*V.ZUCC',2,0,2),(111,NULL,'\0',NULL,'Avocado',0.00,'+I.AVOC',0,0,3),(112,NULL,'\0',NULL,'Crab',0.00,'+I.CRAB',0,0,3),(113,NULL,'\0',NULL,'Cream Cheese',0.00,'+I.CRCH',0,0,3),(114,NULL,'\0',NULL,'Cucumber',0.00,'+I.CCMB',0,0,3),(115,NULL,'\0',NULL,'Eel Sauce',0.00,'+I.EELS',0,0,3),(116,NULL,'\0',NULL,'Eel',0.00,'+I.EEL',0,0,3),(117,NULL,'\0',NULL,'Fish',0.00,'+I.FISH',0,0,3),(118,NULL,'\0',NULL,'Ginger',0.00,'+I.GNGR',0,0,3),(119,NULL,'\0',NULL,'Masago',0.00,'+I.MSGO',0,0,3),(120,NULL,'\0',NULL,'Mayo',0.00,'+I.MAYO',0,0,3),(121,NULL,'\0',NULL,'Octopus',0.00,'+I.OCTP',0,0,3),(122,NULL,'\0',NULL,'Red Snapper',0.00,'+I.RSNP',0,0,3),(123,NULL,'\0',NULL,'Salmon',0.00,'+I.SLMN',0,0,3),(124,NULL,'\0',NULL,'Scallop',0.00,'+I.SCLP',0,0,3),(125,NULL,'\0',NULL,'Shrimp',0.00,'+I.SHRM',0,0,3),(126,NULL,'\0',NULL,'Smoked Salmon',0.00,'+I.SSLMN',0,0,3),(127,NULL,'\0',NULL,'Soy Bean Wrap',0.00,'+I.SBWR',0,0,3),(128,NULL,'\0',NULL,'Spicy Tuna',0.00,'+I.STUNA',0,0,3),(129,NULL,'\0',NULL,'Wasabi',0.00,'+I.WSBI',0,0,3),(130,NULL,'\0',NULL,'White Tuna',0.00,'+I.WTUNA',0,0,3),(131,NULL,'\0',NULL,'Yellow Tail',0.00,'+I.YWTL',0,0,3),(132,NULL,'\0',NULL,'Avocado',0.00,'-I.AVOC',1,0,3),(133,NULL,'\0',NULL,'Crab',0.00,'-I.CRAB',1,0,3),(134,NULL,'\0',NULL,'Cream Cheese',0.00,'-I.CRCH',1,0,3),(135,NULL,'\0',NULL,'Cucumber',0.00,'-I.CCMB',1,0,3),(136,NULL,'\0',NULL,'Eel Sauce',0.00,'-I.EELS',1,0,3),(137,NULL,'\0',NULL,'Eel',0.00,'-I.EEL',1,0,3),(138,NULL,'\0',NULL,'Fish',0.00,'-I.FISH',1,0,3),(139,NULL,'\0',NULL,'Ginger',0.00,'-I.GNGR',1,0,3),(140,NULL,'\0',NULL,'Masago',0.00,'-I.MSGO',1,0,3),(141,NULL,'\0',NULL,'Mayo',0.00,'-I.MAYO',1,0,3),(142,NULL,'\0',NULL,'Octopus',0.00,'-I.OCTP',1,0,3),(143,NULL,'\0',NULL,'Red Snapper',0.00,'-I.RSNP',1,0,3),(144,NULL,'\0',NULL,'Salmon',0.00,'-I.SLMN',1,0,3),(145,NULL,'\0',NULL,'Scallop',0.00,'-I.SCLP',1,0,3),(146,NULL,'\0',NULL,'Shrimp',0.00,'-I.SHRM',1,0,3),(147,NULL,'\0',NULL,'Smoked Salmon',0.00,'-I.SSLMN',1,0,3),(148,NULL,'\0',NULL,'Soy Bean Wrap',0.00,'-I.SBWR',1,0,3),(149,NULL,'\0',NULL,'Spicy Tuna',0.00,'-I.STUNA',1,0,3),(150,NULL,'\0',NULL,'Wasabi',0.00,'-I.WSBI',1,0,3),(151,NULL,'\0',NULL,'White Tuna',0.00,'-I.WTUNA',1,0,3),(152,NULL,'\0',NULL,'Yellow Tail',0.00,'-I.YWTL',1,0,3),(153,NULL,'\0',NULL,'Avocado',0.00,'*I.AVOC',2,0,3),(154,NULL,'\0',NULL,'Crab',0.00,'*I.CRAB',2,0,3),(155,NULL,'\0',NULL,'Cream Cheese',0.00,'*I.CRCH',2,0,3),(156,NULL,'\0',NULL,'Cucumber',0.00,'*I.CCMB',2,0,3),(157,NULL,'\0',NULL,'Eel Sauce',0.00,'*I.EELS',2,0,3),(158,NULL,'\0',NULL,'Eel',0.00,'*I.EEL',2,0,3),(159,NULL,'\0',NULL,'Fish',0.00,'*I.FISH',2,0,3),(160,NULL,'\0',NULL,'Ginger',0.00,'*I.GNGR',2,0,3),(161,NULL,'\0',NULL,'Masago',0.00,'*I.MSGO',2,0,3),(162,NULL,'\0',NULL,'Mayo',0.00,'*I.MAYO',2,0,3),(163,NULL,'\0',NULL,'Octopus',0.00,'*I.OCTP',2,0,3),(164,NULL,'\0',NULL,'Red Snapper',0.00,'*I.RSNP',2,0,3),(165,NULL,'\0',NULL,'Salmon',0.00,'*I.SLMN',2,0,3),(166,NULL,'\0',NULL,'Scallop',0.00,'*I.SCLP',2,0,3),(167,NULL,'\0',NULL,'Shrimp',0.00,'*I.SHRM',2,0,3),(168,NULL,'\0',NULL,'Smoked Salmon',0.00,'*I.SSLMN',2,0,3),(169,NULL,'\0',NULL,'Soy Bean Wrap',0.00,'*I.SBWR',2,0,3),(170,NULL,'\0',NULL,'Spicy Tuna',0.00,'*I.STUNA',2,0,3),(171,NULL,'\0',NULL,'Wasabi',0.00,'*I.WSBI',2,0,3),(172,NULL,'\0',NULL,'White Tuna',0.00,'*I.WTUNA',2,0,3),(173,NULL,'\0',NULL,'Yellow Tail',0.00,'*I.YWTL',2,0,3),(174,NULL,'\0',NULL,'Black Bean Sauce',0.00,'+S.BLBN',0,0,4),(175,NULL,'\0',NULL,'Brown Sauce',0.00,'+S.BRWN',0,0,4),(176,NULL,'\0',NULL,'Curry Sauce',0.00,'+S.CURR',0,0,4),(177,NULL,'\0',NULL,'Garlic Sauce',0.00,'+S.GRLC',0,0,4),(178,NULL,'\0',NULL,'Ginger Sauce',0.00,'+S.GNGR',0,0,4),(179,NULL,'\0',NULL,'Hot Mustard',0.00,'+S.HOTM',0,0,4),(180,NULL,'\0',NULL,'Hot Sauce',0.00,'+S.HOT',0,0,4),(181,NULL,'\0',NULL,'Orange Sauce',0.00,'+S.ORNG',0,0,4),(182,NULL,'\0',NULL,'Soy Sauce',0.00,'+S.SOY',0,0,4),(183,NULL,'\0',NULL,'Tiny Sauce',0.00,'+S.TINY',0,0,4),(184,NULL,'\0',NULL,'Duck Sauce',0.00,'+S.DUCK',0,0,4),(185,NULL,'\0',NULL,'Plum Sauce',0.00,'+S.PLUM',0,0,4),(186,NULL,'\0',NULL,'White Sauce',0.00,'+S.WHTE',0,0,4),(187,NULL,'\0',NULL,'Black Bean Sauce',0.00,'-S.BLBN',1,0,4),(188,NULL,'\0',NULL,'Brown Sauce',0.00,'-S.BRWN',1,0,4),(189,NULL,'\0',NULL,'Curry Sauce',0.00,'-S.CURR',1,0,4),(190,NULL,'\0',NULL,'Garlic Sauce',0.00,'-S.GRLC',1,0,4),(191,NULL,'\0',NULL,'Ginger Sauce',0.00,'-S.GNGR',1,0,4),(192,NULL,'\0',NULL,'Hot Mustard',0.00,'-S.HOTM',1,0,4),(193,NULL,'\0',NULL,'Hot Sauce',0.00,'-S.HOT',1,0,4),(194,NULL,'\0',NULL,'Orange Sauce',0.00,'-S.ORNG',1,0,4),(195,NULL,'\0',NULL,'Soy Sauce',0.00,'-S.SOY',1,0,4),(196,NULL,'\0',NULL,'Tiny Sauce',0.00,'-S.TINY',1,0,4),(197,NULL,'\0',NULL,'Duck Sauce',0.00,'-S.DUCK',1,0,4),(198,NULL,'\0',NULL,'Plum Sauce',0.00,'-S.PLUM',1,0,4),(199,NULL,'\0',NULL,'White Sauce',0.00,'-S.WHTE',1,0,4),(200,NULL,'\0',NULL,'Black Bean Sauce',0.00,'*S.BLBN',2,0,4),(201,NULL,'\0',NULL,'Brown Sauce',0.00,'*S.BRWN',2,0,4),(202,NULL,'\0',NULL,'Curry Sauce',0.00,'*S.CURR',2,0,4),(203,NULL,'\0',NULL,'Garlic Sauce',0.00,'*S.GRLC',2,0,4),(204,NULL,'\0',NULL,'Ginger Sauce',0.00,'*S.GNGR',2,0,4),(205,NULL,'\0',NULL,'Hot Mustard',0.00,'*S.HOTM',2,0,4),(206,NULL,'\0',NULL,'Hot Sauce',0.00,'*S.HOT',2,0,4),(207,NULL,'\0',NULL,'Orange Sauce',0.00,'*S.ORNG',2,0,4),(208,NULL,'\0',NULL,'Soy Sauce',0.00,'*S.SOY',2,0,4),(209,NULL,'\0',NULL,'Tiny Sauce',0.00,'*S.TINY',2,0,4),(210,NULL,'\0',NULL,'Duck Sauce',0.00,'*S.DUCK',2,0,4),(211,NULL,'\0',NULL,'Plum Sauce',0.00,'*S.PLUM',2,0,4),(212,NULL,'\0',NULL,'White Sauce',0.00,'*S.WHTE',2,0,4),(213,NULL,'\0',NULL,'MSG',0.00,'+MSG',0,0,5),(214,NULL,'\0',NULL,'Oil',0.00,'+OIL',0,0,5),(215,NULL,'\0',NULL,'Pancake',0.00,'+PCAKE',0,0,5),(216,NULL,'\0',NULL,'Pepper',0.00,'+PEPR',0,0,5),(217,NULL,'\0',NULL,'Salt',0.00,'+SALT',0,0,5),(218,NULL,'\0',NULL,'Sugar',0.00,'+SUGR',0,0,5),(219,NULL,'\0',NULL,'Won Ton',0.00,'+WONTON',0,0,5),(220,NULL,'\0',NULL,'Corn Starch',0.00,'+STRCH',0,0,5),(221,NULL,'\0',NULL,'MSG',0.00,'-MSG',1,0,5),(222,NULL,'\0',NULL,'Oil',0.00,'-OIL',1,0,5),(223,NULL,'\0',NULL,'Pancake',0.00,'-PCAKE',1,0,5),(224,NULL,'\0',NULL,'Pepper',0.00,'-PEPR',1,0,5),(225,NULL,'\0',NULL,'Salt',0.00,'-SALT',1,0,5),(226,NULL,'\0',NULL,'Sugar',0.00,'-SUGR',1,0,5),(227,NULL,'\0',NULL,'Won Ton',0.00,'-WONTON',1,0,5),(228,NULL,'\0',NULL,'Corn Starch',0.00,'-STRCH',1,0,5),(229,NULL,'\0',NULL,'MSG',0.00,'*MSG',2,0,5),(230,NULL,'\0',NULL,'Oil',0.00,'*OIL',2,0,5),(231,NULL,'\0',NULL,'Pancake',0.00,'*PCAKE',2,0,5),(232,NULL,'\0',NULL,'Pepper',0.00,'*PEPR',2,0,5),(233,NULL,'\0',NULL,'Salt',0.00,'*SALT',2,0,5),(234,NULL,'\0',NULL,'Sugar',0.00,'*SUGR',2,0,5),(235,NULL,'\0',NULL,'Won Ton',0.00,'*WONTON',2,0,5),(236,NULL,'\0',NULL,'Corn Starch',0.00,'*STRCH',2,0,5),(237,NULL,'\0',NULL,'Chop Small',0.00,'@SM.CHOP',3,0,5),(238,NULL,'\0',NULL,'Crispy',0.00,'@CRISPY',3,0,5),(239,NULL,'\0',NULL,'Deep Fried',0.00,'@D.FRY',3,0,5),(240,NULL,'\0',NULL,'Sauteed',0.00,'@SAUT',3,0,5),(241,NULL,'\0',NULL,'Spicy',0.00,'@SPICY',3,0,5),(242,NULL,'\0',NULL,'Steamed',0.00,'@STEAM',3,0,5),(243,NULL,'\0',NULL,'Sauce On Side',0.00,'@S.SIDE',3,0,5),(244,NULL,'\0',NULL,'Rare',0.00,'@RARE',3,0,5),(245,NULL,'\0',NULL,'Medium Rare',0.00,'@M.RARE',3,0,5),(246,NULL,'\0',NULL,'Medium',0.00,'@MED',3,0,5),(247,NULL,'\0',NULL,'Medium Well',0.00,'@M.WELL',3,0,5),(248,NULL,'\0',NULL,'Well-Done',0.00,'@WELL',3,0,5),(249,NULL,'\0',NULL,'Brown Rice',0.00,'?BR',4,0,6),(250,NULL,'\0',NULL,'White Rice',0.00,'?WR',4,0,6),(251,NULL,'\0',NULL,'Ice',0.00,'+ICE',0,0,7),(252,NULL,'\0',NULL,'Ice',0.00,'-ICE',1,0,7);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_entry`
--

LOCK TABLES `product_entry` WRITE;
/*!40000 ALTER TABLE `product_entry` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order`
--

LOCK TABLES `sales_order` WRITE;
/*!40000 ALTER TABLE `sales_order` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES (1,NULL,'\0',0,NULL,0,'1',1),(2,NULL,'\0',1,NULL,0,'2',1),(3,NULL,'\0',2,NULL,0,'3',1),(4,NULL,'\0',3,NULL,1,'4',1),(5,NULL,'\0',9,NULL,1,'5',1),(6,NULL,'\0',2,NULL,1,'6',1),(7,NULL,'\0',14,NULL,1,'7',1),(8,NULL,'\0',15,NULL,1,'8',1),(9,NULL,'\0',16,NULL,1,'9',1),(10,NULL,'\0',17,NULL,1,'10',1),(11,NULL,'\0',18,NULL,1,'11',1),(12,NULL,'\0',19,NULL,1,'12',1),(13,NULL,'\0',3,NULL,2,'13',1),(14,NULL,'\0',6,NULL,2,'14',1),(15,NULL,'\0',13,NULL,2,'15',1),(16,NULL,'\0',19,NULL,2,'16',1),(17,NULL,'\0',3,NULL,3,'17',1),(18,NULL,'\0',9,NULL,3,'18',1),(19,NULL,'\0',15,NULL,3,'19',1),(20,NULL,'\0',17,NULL,3,'20',1),(21,NULL,'\0',19,NULL,3,'21',1),(22,NULL,'\0',3,NULL,4,'8',1),(23,NULL,'\0',6,NULL,4,'9',1),(24,NULL,'\0',13,NULL,4,'10',1),(25,NULL,'\0',19,NULL,4,'11',1),(26,NULL,'\0',3,NULL,5,'12',1),(27,NULL,'\0',9,NULL,5,'13',1),(28,NULL,'\0',3,NULL,6,'14',1),(29,NULL,'\0',6,NULL,6,'15',1),(30,NULL,'\0',19,NULL,6,'16',1),(31,NULL,'\0',0,NULL,7,'17',1),(32,NULL,'\0',1,NULL,7,'18',1),(33,NULL,'\0',2,NULL,7,'19',1),(34,NULL,'\0',9,NULL,7,'20',1),(35,NULL,'\0',19,NULL,7,'21',1),(36,NULL,'\0',19,NULL,8,'22',1),(37,NULL,'\0',0,NULL,9,'23',1),(38,NULL,'\0',3,NULL,9,'24',1),(39,NULL,'\0',9,NULL,9,'25',1),(40,NULL,'\0',11,NULL,9,'26',1),(41,NULL,'\0',13,NULL,9,'27',1),(42,NULL,'\0',15,NULL,9,'28',1),(43,NULL,'\0',17,NULL,9,'29',1),(44,NULL,'\0',19,NULL,9,'30',1),(45,NULL,'\0',0,NULL,10,'31',1),(46,NULL,'\0',3,NULL,10,'32',1),(47,NULL,'\0',13,NULL,10,'33',1),(48,NULL,'\0',15,NULL,10,'34',1),(49,NULL,'\0',17,NULL,10,'35',1),(50,NULL,'\0',19,NULL,10,'36',1),(51,NULL,'\0',0,NULL,11,'37',1),(52,NULL,'\0',3,NULL,11,'38',1),(53,NULL,'\0',6,NULL,11,'39',1),(54,NULL,'\0',7,NULL,11,'40',1),(55,NULL,'\0',13,NULL,11,'41',1),(56,NULL,'\0',15,NULL,11,'42',1),(57,NULL,'\0',17,NULL,11,'43',1),(58,NULL,'\0',19,NULL,11,'44',1),(59,NULL,'\0',0,NULL,12,'45',1),(60,NULL,'\0',3,NULL,12,'46',1),(61,NULL,'\0',13,NULL,12,'47',1),(62,NULL,'\0',15,NULL,12,'48',1),(63,NULL,'\0',17,NULL,12,'49',1),(64,NULL,'\0',19,NULL,12,'50',1),(65,NULL,'\0',0,NULL,13,'51',1),(66,NULL,'\0',3,NULL,13,'52',1),(67,NULL,'\0',13,NULL,13,'53',1),(68,NULL,'\0',15,NULL,13,'54',1),(69,NULL,'\0',17,NULL,13,'55',1),(70,NULL,'\0',19,NULL,13,'56',1);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (1,NULL,'\0',20,NULL,'Section I',14,'I',0);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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

-- Dump completed on 2016-02-24 12:40:39
