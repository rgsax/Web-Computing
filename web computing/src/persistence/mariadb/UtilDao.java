package persistence.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import persistence.DataSource;
import persistence.PersistenceException;

public class UtilDao {

	
private DataSource dataSource;

public UtilDao(DataSource dataSource) {
		this.dataSource=dataSource;
	}

public void dropDatabase(){
	
	Connection connection = dataSource.getConnection();
	try {
		String delete = "drop table if exists iscritto;"
				+ "drop table if exists afferisce;"							
				+ "drop table if exists studente;"
				+ "drop table if exists corso;"
				+ "drop table if exists corsodilaurea;"
				+ "drop table if exists dipartimento;"
				+ "drop table if exists scuola;";
		
		for(String query : delete.split(";")) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.executeUpdate();
		}
		
		System.out.println("Executed drop database");
		
	} catch (SQLException e) {
		
		throw new PersistenceException(e.getMessage());
	} finally {
		try {
			connection.close();
		} catch (SQLException e) {
			
			throw new PersistenceException(e.getMessage());
		}
	}
}

public void createDatabase(){
	
	Connection connection = dataSource.getConnection();
	try {
		
		String create = "CREATE TABLE `scuola` (" + 
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT," + 
				"  `codice_meccanografico` varchar(100) DEFAULT NULL," + 
				"  `nome` varchar(100) NOT NULL," + 
				"  PRIMARY KEY (`id`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" +
				
				"CREATE TABLE `corso` (" + 
				"  `codice` bigint(20) NOT NULL AUTO_INCREMENT," + 
				"  `nome` varchar(100) NOT NULL," + 
				"  PRIMARY KEY (`codice`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				
				"CREATE TABLE `dipartimento` (" + 
				"  `codice` bigint(20) NOT NULL AUTO_INCREMENT," + 
				"  `nome` varchar(100) NOT NULL," + 
				"  PRIMARY KEY (`codice`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" + 
				
				"CREATE TABLE `corsodilaurea` (" + 
				"  `codice` bigint(20) NOT NULL AUTO_INCREMENT," + 
				"  `nome` varchar(100) NOT NULL," + 
				"  `dipartimento_codice` bigint(20) NOT NULL," + 
				"  PRIMARY KEY (`codice`)," + 
				"  KEY `corsodilaurea_dipartimento_FK` (`dipartimento_codice`)," + 
				"  CONSTRAINT `corsodilaurea_dipartimento_FK` FOREIGN KEY (`dipartimento_codice`) REFERENCES `dipartimento` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" +
				
				"CREATE TABLE `afferisce` (" + 
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT," + 
				"  `corso_codice` bigint(20) NOT NULL," + 
				"  `corsodilaurea_codice` bigint(20) NOT NULL," + 
				"  PRIMARY KEY (`id`)," + 
				"  KEY `afferisce_corso_FK` (`corso_codice`)," + 
				"  KEY `afferisce_corsodilaurea_FK` (`corsodilaurea_codice`)," + 
				"  CONSTRAINT `afferisce_corso_FK` FOREIGN KEY (`corso_codice`) REFERENCES `corso` (`codice`)," + 
				"  CONSTRAINT `afferisce_corsodilaurea_FK` FOREIGN KEY (`corsodilaurea_codice`) REFERENCES `corsodilaurea` (`codice`)" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" +
				
				"CREATE TABLE `studente` (" + 
				"  `matricola` char(8) NOT NULL," + 
				"  `nome` varchar(100) NOT NULL," + 
				"  `cognome` varchar(100) NOT NULL," + 
				"  `data_nascita` date NOT NULL," + 
				"  `scuola_id` bigint(20) NOT NULL," + 
				"  `password` varchar(100) DEFAULT NULL," + 
				"  PRIMARY KEY (`matricola`)," + 
				"  KEY `studente_scuola_FK` (`scuola_id`)," + 
				"  CONSTRAINT `studente_scuola_FK` FOREIGN KEY (`scuola_id`) REFERENCES `scuola` (`id`) ON DELETE CASCADE ON UPDATE CASCADE" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;" +
				
				"CREATE TABLE `iscritto` (" + 
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT," + 
				"  `matricola_studente` char(8) NOT NULL," + 
				"  `corso_codice` bigint(20) NOT NULL," + 
				"  PRIMARY KEY (`id`)," + 
				"  KEY `iscritto_corso_FK` (`corso_codice`)," + 
				"  KEY `iscritto_studente_FK` (`matricola_studente`)," + 
				"  CONSTRAINT `iscritto_corso_FK` FOREIGN KEY (`corso_codice`) REFERENCES `corso` (`codice`) ON DELETE CASCADE ON UPDATE CASCADE," + 
				"  CONSTRAINT `iscritto_studente_FK` FOREIGN KEY (`matricola_studente`) REFERENCES `studente` (`matricola`) ON DELETE CASCADE ON UPDATE CASCADE" + 
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		
		for(String query : create.split(";")) {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.executeUpdate();
		}
		System.out.println("Executed create database");
		
	} catch (SQLException e) {
		
		throw new PersistenceException(e.getMessage());
	} finally {
		try {
			connection.close();
		} catch (SQLException e) {
			
			throw new PersistenceException(e.getMessage());
		}
	}
}


public  void resetDatabase() {
		
		Connection connection = dataSource.getConnection();
		try {
			String delete = "delete FROM studente";
			PreparedStatement statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();

			delete = "delete FROM scuola";
			statement = connection.prepareStatement(delete);
			
			delete = "delete FROM corso";
			statement = connection.prepareStatement(delete);
			
			delete = "delete FROM dipartimento";
			statement = connection.prepareStatement(delete);
			
			delete = "delete FROM corsodilaurea";
			statement = connection.prepareStatement(delete);
			
			delete = "delete FROM afferisce";
			statement = connection.prepareStatement(delete);
			
			delete = "delete FROM iscritto";
			statement = connection.prepareStatement(delete);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				
				throw new PersistenceException(e.getMessage());
			}
		}
	}
}
