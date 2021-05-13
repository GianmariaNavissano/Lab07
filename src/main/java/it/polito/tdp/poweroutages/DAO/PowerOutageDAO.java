package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<Nerc>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<PowerOutage> getPowerOutagesByNerc(Nerc nerc){
		List<PowerOutage> powerOutages = new LinkedList<PowerOutage>();
		String sql ="SELECT id, nerc_id, customers_affected, date_event_began, date_event_finished "
				+ "FROM PowerOutages "
				+ "WHERE nerc_id=?";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				PowerOutage po = new PowerOutage(rs.getInt("id"), rs.getInt("nerc_id"), rs.getInt("customers_affected"), rs.getTimestamp("date_event_began").toLocalDateTime(), rs.getTimestamp("date_event_finished").toLocalDateTime());
				powerOutages.add(po);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore estrazione blackouts by nerc", e);
		}
		return powerOutages;
	}
	

}
