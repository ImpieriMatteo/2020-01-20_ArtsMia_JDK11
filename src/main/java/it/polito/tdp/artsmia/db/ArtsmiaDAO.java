package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Connessioni;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getAllRoles() {
		
		String sql = "SELECT DISTINCT a.role "
				+ "FROM authorship a "
				+ "ORDER BY a.role ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("a.role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getVertex(String ruolo, Map<Integer, Artist> idMap) {

		String sql = "SELECT DISTINCT ar.artist_id AS ID, ar.name AS nome "
				+ "FROM authorship au, artists ar "
				+ "WHERE au.artist_id = ar.artist_id AND au.role = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				idMap.put(res.getInt("ID"), new Artist(res.getInt("ID"), res.getString("nome")));
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	public void getEsibizioniArtistaPerRuolo(String ruolo, Map<Integer, Artist> idMap) {
		
		String sql = "SELECT DISTINCT au.artist_id AS ID, ex.exhibition_id AS ex_id "
				+ "FROM authorship au, exhibition_objects ex "
				+ "WHERE au.role = ? AND ex.object_id = au.object_id "
				+ "ORDER BY au.artist_id";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				idMap.get(res.getInt("ID")).addEsibizioniPerRuolo(res.getInt("ex_id"));
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public List<Connessioni> getEdges(String ruolo, Map<Integer, Artist> idMap) {
		
		String sql = "SELECT au.artist_id AS ID1, au2.artist_id AS ID2, COUNT(*) AS peso "
				+ "FROM authorship au, exhibition_objects ex, authorship au2, exhibition_objects ex2 "
				+ "WHERE au.role = ? AND ex.object_id = au.object_id AND "
				+ "	au2.role = ? AND ex2.object_id = au2.object_id AND "
				+ "	ex.exhibition_id = ex2.exhibition_id AND au.artist_id < au2.artist_id "
				+ "GROUP BY au.artist_id, au2.artist_id";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			
			List<Connessioni> result = new ArrayList<>();
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(new Connessioni(idMap.get(res.getInt("ID1")), idMap.get(res.getInt("ID2")), res.getInt("peso")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
