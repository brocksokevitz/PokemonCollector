package com.revature.pokemonv2.dao;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.pokemonv2.model.Pokemon;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.pokemonv2.model.Trainer;
import com.revature.pokemonv2.model.TrainerFactory;
import com.revature.pokemonv2.service.TokenService;
import com.revature.pokemonv2.utilities.ConnectionUtility;

import oracle.jdbc.OracleTypes;

/**
 * The TrainerDAOImp class contains methods that deal with the selection,
 * insertion, and updating of trainers.
 *
 */
public class TrainerDAOImp implements TrainerDAO {

	private static final Logger LOGGER = Logger.getLogger(TrainerDAOImp.class);
	private static ObjectMapper mapper = new ObjectMapper();
	private static final TokenService tokenService = TokenService.getInstance();
	private static TrainerDAOImp trainer = null;

	/**
	 * Gets the instance of the class.
	 */
	public static TrainerDAOImp getTrainerDAO() {
		if (trainer == null) {
			trainer = new TrainerDAOImp();
		}
		return trainer;
	}

	@Override
	public boolean createTrainer(String username, String password, String email, String firstName, String lastName,
			int credit, int score) {
		try (Connection conn = ConnectionUtility.getInstance().getConnection()) {
			try (CallableStatement cs = TrainerDAOStatements.createTrainerStatement(conn, username, password, email,
					firstName, lastName, credit, score)) {
				cs.execute();
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public List<Pokemon> getDuplicates(int trainer_id) {
		//Create a temporary list for pokemon.
		ArrayList<Pokemon> duplicateList = null;
		//Try with resources to connect to database.
		try (Connection conn = ConnectionUtility.getInstance().getConnection()) {

			// Call stored procedure
			String sql = "CALL get_all_duplicates(?, ?)";
			// Setup callableStatment
			try (CallableStatement cs = conn.prepareCall(sql)) {
				// Set the trainer id in the callable statement
				cs.setInt(1, trainer_id);
				cs.registerOutParameter(2, OracleTypes.CURSOR);
				cs.execute(); // Prepare the resultset
				try (ResultSet rs = (ResultSet) cs.getObject(2)) {
					// While the result set has another object create a pokemon objet and push it to
					// the duplicatePokemon array.
					// Check and see if there are any duplicates if not retunr null

					while (rs.next()) {

						Pokemon temp = new Pokemon(rs.getInt("pokemon_id"), rs.getInt("count"));
						// System.out.println(temp.toString());
						duplicateList.add(temp);
						
					}

				}

				return duplicateList;

			}
		}

		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public String loginAuthentication(HttpServletRequest request, HttpServletResponse response) {
		// Verifies if the user is valid
		String token = "";
		Trainer login = verifyLogin(request.getParameter("USERNAME"), request.getParameter("PASSWORD"));
		if (login != null) {
			// Generate a token for the user
			token = tokenService.generateToken(login);
			response.addHeader("Authorization", "Bearer " + token);
			try {
				response.getWriter().write(mapper.writeValueAsString(login));
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return token;
	}

	@Override
	public boolean purchasePokemon(String username, int cost) {
		try (Connection conn = ConnectionUtility.getInstance().getConnection()) {
			try (CallableStatement cs = TrainerDAOStatements.purchasePokemonStatement(conn, username, cost)) {
				cs.execute();
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public int[] redeemAll(int trainer_id) {
		int [] out = new int[2]; //return array
		try (Connection conn = ConnectionUtility.getInstance().getConnection()) { //create connection
			String sql = "CALL redeem_all_duplicates(?,?,?)"; //Procedure string
			//Setup callableStatment
			try(CallableStatement cs = conn.prepareCall(sql)){
				cs.setInt(1, trainer_id);//Set the trainer id in the callable statement
				cs.registerOutParameter(2, Types.INTEGER); //Out param for added credits
				cs.registerOutParameter(3, Types.INTEGER);//out param for new total
				cs.execute();				//Prepare the resultset

				out[0] = cs.getInt(2); //set return value
				out[1] = cs.getInt(3);//set return value
			}
			return out; // return array of values

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int[] redeemSpecific(int trainerId, int pokeId)
	{
		int [] out = new int[2]; //return array
		try (Connection conn = ConnectionUtility.getInstance().getConnection()) { //create connection
			String sql = "CALL redeem_duplicate(?,?, ?, ?)"; //Procedure string
			//Setup callableStatment
			try(CallableStatement cs = TrainerDAOStatements.redeemSpecificStatement(conn, trainerId, pokeId)){
				cs.execute();
				out[0] = cs.getInt(3); //set return value
				out[1] = cs.getInt(4);//set return value
			}
			return out; // return array of values

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}

	}


	@Override
	public Trainer verifyLogin(String username, String password) {
		// Try with resources on the instance of ConnectionUtility
		try (Connection conn = ConnectionUtility.getInstance().getConnection()) {
			// Try with resources on the PreparedStatement
			try (CallableStatement cs = TrainerDAOStatements.verifyLoginStatement(conn, username, password)) {
				cs.execute();
				// Executing out parameters
				try (ResultSet rs = (ResultSet) cs.getObject(3)) {
					if (rs.next()) {
						return TrainerFactory.createFromResult(rs);
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}
}
