import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.sql.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class populateReview {
	public static Connection getDBConnection() 
	{
		Connection dbConnection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:xe", "system", "password");
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
	public void readReviewJsonFile(String reviewFile) throws SQLException{
		System.out.println("File:"+reviewFile);
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String query = "INSERT INTO REVIEWS" 
					+ "(FUNNY_VOTES, USEFUL_VOTES, COOL_VOTES, TOTAL_VOTES, USER_ID, REVIEW_ID, STARS, REVIEW_DATE, TEXT, BUSINESS_ID) VALUES" 
					+ "(?,?,?,?,?,?,?,?,?,?)";	
		JSONParser parser = new JSONParser();
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(query);
			FileReader filereader = new FileReader(reviewFile);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			int i=0;
			while ((line = bufferedReader.readLine()) != null){				
				System.out.println(i);
				i++;
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				
				JSONObject votes = (JSONObject) jsonObject.get("votes");
				int  funny_votes = ((Long) votes.get("funny")).intValue();
				int  useful_votes = ((Long) votes.get("useful")).intValue();
				int  cool_votes = ((Long) votes.get("cool")).intValue();
				int  total_votes = funny_votes+useful_votes+cool_votes;
				
				preparedStatement.setInt(1, funny_votes);
				preparedStatement.setInt(2, useful_votes);
				preparedStatement.setInt(3, cool_votes);
				preparedStatement.setInt(4, total_votes);
				
				String user_id = (String) jsonObject.get("user_id");
				preparedStatement.setString(5, user_id);
				
				String review_id = (String) jsonObject.get("review_id");
				preparedStatement.setString(6, review_id);
				
				int stars = ((Long) jsonObject.get("stars")).intValue();
				preparedStatement.setInt(7, stars);
				
				String review_date = (String) jsonObject.get("date");
				preparedStatement.setDate(8, java.sql.Date.valueOf(review_date));
				
				String text = (String) jsonObject.get("text");
				preparedStatement.setString(9, text);
				
				String business_id = (String) jsonObject.get("business_id");
				preparedStatement.setString(10, business_id);
				
				preparedStatement.executeUpdate();
			}
			filereader.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{

			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if (preparedStatement != null) 
			{
				preparedStatement.close();
			}			
			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}
	}
		
}
