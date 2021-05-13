package it.polito.tdp.poweroutages.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	private PowerOutageDAO podao;
	private List<PowerOutage> result;
	private List<PowerOutage> powerOutages;
	private int maxCustomers_affected;
	private int maxHours;
	private int maxYears;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<PowerOutage> getWorstCase(Nerc nerc, int hours, int years) {
		this.result = null;
		this.maxCustomers_affected = 0;
		powerOutages = podao.getPowerOutagesByNerc(nerc);
		List<PowerOutage> parziale = new LinkedList<PowerOutage>();
		maxHours = hours;
		maxYears = years;
		cerca(parziale, 0);
		return result;
	}

	private void cerca(List<PowerOutage> parziale, int livello) {
		
		//Per ogni soluzione parziale valida calcolo il numero di customers_affected
			int customers_affected = this.calcolaCustomers_affected(parziale);
			if(customers_affected > maxCustomers_affected) {
				maxCustomers_affected = customers_affected;
			//	System.out.println("Nuovo maxCustomers_affected: "+maxCustomers_affected);
				result = new LinkedList<PowerOutage>(parziale);
			}
		
		//Caso terminale
		if(livello == (powerOutages.size())) {
			return;
		}
		
		
		parziale.add(powerOutages.get(livello));
		if(this.isValida(parziale))
			cerca(parziale, livello+1);
		
		
		parziale.remove(powerOutages.get(livello));
		if(this.isValida(parziale))
			cerca(parziale, livello+1);
		
		
	}

	private boolean isValida(List<PowerOutage> parziale) {
		
		long minutiDisservizio = 0;
		LocalDateTime dataI;
		LocalDateTime dataF;
		LocalDateTime dataMin = LocalDateTime.MAX;
		LocalDateTime dataMax = LocalDateTime.MIN;
		
		
		for(PowerOutage po : parziale) {
			
			dataI = po.getDate_event_began();
			dataF = po.getDate_event_finished();
			minutiDisservizio += Duration.between(dataI, dataF).toMinutes();
			if(dataI.isBefore(dataMin))
				dataMin = dataI;
			if(dataF.isAfter(dataMax))
				dataMax = dataF;
		}
		if(minutiDisservizio>(maxHours*60))
			return false;
		if(Duration.between(dataMin, dataMax).toMinutes()>(this.maxYears*525600))
			return false;
		
		return true;
	}

	private int calcolaCustomers_affected(List<PowerOutage> parziale) {
		int cnt = 0;
		for(PowerOutage po : parziale)
			cnt += po.getCustomers_affected();
		return cnt;
	}

}
