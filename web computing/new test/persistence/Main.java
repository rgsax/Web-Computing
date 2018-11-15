package persistence;

import java.util.Calendar;
import java.util.Date;

import model.Corso;
import model.CorsoDiLaurea;
import model.Dipartimento;
import model.Scuola;
import model.Studente;
import persistence.dao.CorsoDao;
import persistence.dao.CorsoDiLaureaDao;
import persistence.dao.DipartimentoDao;
import persistence.dao.ScuolaDao;
import persistence.dao.StudenteDao;
import persistence.mariadb.*;

public class Main {
	public static void main(String args[]) {				
		try {
			//Class.forName("org.postgresql.Driver").newInstance();
			//DataSource dataSource=new DataSource("jdbc:postgresql://localhost:5432/Segreteria2019","postgres","postgres");
			
			Class.forName("org.mariadb.jdbc.Driver").newInstance();
			DataSource dataSource = new DataSource("jdbc:mariadb://localhost:3306/Segreteria2019", "riccardo", "riccardo");

			reinitDatabase(dataSource);
			showDatabase(dataSource);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	private static void showDatabase(DataSource dataSource) {
		CorsoDao corsoDao = new CorsoDaoJDBC(dataSource);
		CorsoDiLaureaDao cdlDao = new CorsoDiLaureaDaoJDBC(dataSource);
		DipartimentoDao dipDao = new DipartimentoDaoJDBC(dataSource);
		ScuolaDao scuolaDao = new ScuolaDaoJDBC(dataSource);
		StudenteDao studDao = new StudenteDaoJDBC(dataSource);
		
		for(Corso corso : corsoDao.findAll())
			System.out.println(corso.toString());
		
		for(CorsoDiLaurea cdl : cdlDao.findAll())
			System.out.println(cdl.toString());
		
		for(Dipartimento dip : dipDao.findAll())
			System.out.println(dip.toString());
		
		for(Scuola scuola : scuolaDao.findAll())
			System.out.println(scuola.toString());
		
		for(Studente stud : studDao.findAll())
			System.out.println(stud.toString());
	}
	
	private static void reinitDatabase(DataSource dataSource) {		
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(1995, Calendar.MARCH, 21); // // 21 marzo 1995
		Date date1 = cal.getTime();
		cal.set(1996, Calendar.APRIL, 12); // 12 aprile 1996
		Date date2 = cal.getTime();
		cal.set(1998, Calendar.OCTOBER, 1);  // 1 ottobre 1998
		Date date3 = cal.getTime();

		//DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		//UtilDao util = factory.getUtilDAO();
		UtilDao util = new UtilDao(dataSource);
		util.dropDatabase();		
		util.createDatabase();		
		
		StudenteDao studenteDao = new StudenteDaoJDBC(dataSource);
		ScuolaDao scuolaDao = new ScuolaDaoJDBC(dataSource);
		CorsoDao corsoDao = new CorsoDaoJDBC(dataSource);

		Studente studente1 = new Studente("00000001","Rossi","Mario",date1);
		
		Studente studente2 = new Studente();
		studente2.setCognome("Verdi");
		studente2.setNome("Anna");
		studente2.setMatricola("00000002");
		studente2.setDataNascita(date2);

		Studente studente3 = new Studente();
		studente3.setCognome("Bianchi");
		studente3.setNome("Antonio");
		studente3.setMatricola("00000003");
		studente3.setDataNascita(date3);

		Scuola scuola1 = new Scuola();
		//l'id del gruppo e' gestito tramite la classe IDBroker
		scuola1.setCodiceMeccanografico("RCISSE45SDF2");
		scuola1.setNome("Istituto Melandri");
		
		Scuola scuola2 = new Scuola();
		//l'id del gruppo e' gestito tramite la classe IDBroker
		scuola2.setCodiceMeccanografico("RCIS673SDF2");
		scuola2.setNome("Istituto Gizzi");	
		
		Scuola scuola3 = new Scuola();
		//l'id del gruppo e' gestito tramite la classe IDBroker
		scuola3.setCodiceMeccanografico("RCA345S5SDF2");
		scuola3.setNome("Istituto Morchi");	
		
		studente1.setScuolaDiDiploma(scuola3);
		studente2.setScuolaDiDiploma(scuola3);
		studente3.setScuolaDiDiploma(scuola1);
				
		Corso corso1 = new Corso();
		corso1.setNome("Web Computing");
		corso1.addStudente(studente1);
		corso1.addStudente(studente2);
		corso1.addStudente(studente3);
		
		Corso corso2 = new Corso();
		corso2.setNome("Ingegneria del Software");
		corso2.addStudente(studente1);		
		corso2.addStudente(studente3);

		//CREATE
		scuolaDao.save(scuola1);
		scuolaDao.save(scuola2);
		scuolaDao.save(scuola3);
		
		studenteDao.save(studente1);
		studenteDao.save(studente2);
		studenteDao.save(studente3);
		
		corsoDao.save(corso1);
		corsoDao.save(corso2);

//		gruppo1.addStudente(studente3);
//		gruppoDao.update(gruppo1);	
	}
}
