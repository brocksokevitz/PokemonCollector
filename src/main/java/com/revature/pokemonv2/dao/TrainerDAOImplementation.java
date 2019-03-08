package com.revature.pokemonv2.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.revature.pokemonv2.model.Trainer;
import com.revature.pokemonv2.utilities.ConnectionUtility;

public class TrainerDAOImplementation implements TrainerDAO{
	private static TrainerDAOImplementation singleton = new TrainerDAOImplementation();
	private TrainerDAOImplementation() { }
	
	public static TrainerDAOImplementation getDAO() {
		return singleton;
	}

	@Override
	//By CameronT
	public boolean create_trainer(String username, String password, String email, String f_name, 
			String l_name, int credit, int score) {
		
		Connection conn = ConnectionUtility.getConnection();
		System.out.println(1);
		//For the connection pool that we will add later
	//	conn = pool.getConnection();
		System.out.println(2);
		try (CallableStatement cs = conn.prepareCall("{call create_trainer(?,?,?,?,?,?,?)}");) {
			cs.setString(1, username);
			cs.setString(2, password);
			cs.setString(3, email);
			cs.setString(4, f_name);
			cs.setString(5, l_name);
			cs.setInt(6, credit);
			cs.setInt(7, score);
			cs.execute();

			return true;
			
		} catch (SQLException e) {
			
		//	log.error(e.getMessage());
		}finally {
			ConnectionUtility.freeConnection(conn);
		}
	return false;
	}

	@Override
	public Trainer read_trainer(String username, String password) {
		
		return null;
	}
}