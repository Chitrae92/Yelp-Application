import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.sql.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
public class populateUser {
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
	public void readUserJsonFile(String userFile) throws SQLException{
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		String query = "INSERT INTO YELP_USER" 
				+ "(YELPING_SINCE, FUNNY_VOTES, USEFUL_VOTES, COOL_VOTES, TOTAL_VOTES, REVIEW_COUNT, NAME, USER_ID, FANS, AVERAGE_STARS,FRIENDS_COUNT) VALUES" 
				+ "(?,?,?,?,?,?,?,?,?,?,?)";
		String query2 = "INSERT INTO FRIENDS" 
				+ "(USER_ID,FRIEND_ID) VALUES" 
				+ "(?,?)";
		JSONParser parser = new JSONParser();
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(query);
			preparedStatement2 = dbConnection.prepareStatement(query2);
			FileReader filereader = new FileReader(userFile);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			int i=0;
			while ((line = bufferedReader.readLine()) != null){				
				System.out.println(i);
				i++;
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;	
				
				//Insert into Yelp_user Table
				String yelping_since = (String) jsonObject.get("yelping_since")+"-01";
				preparedStatement.setDate(1, java.sql.Date.valueOf(yelping_since));
				
				JSONObject votes = (JSONObject) jsonObject.get("votes");
				int  funny_votes = ((Long) votes.get("funny")).intValue();
				int  useful_votes = ((Long) votes.get("useful")).intValue();
				int  cool_votes = ((Long) votes.get("cool")).intValue();
				int  total_votes = funny_votes+useful_votes+cool_votes;				
				
				preparedStatement.setInt(2, funny_votes);
				preparedStatement.setInt(3, useful_votes);
				preparedStatement.setInt(4, cool_votes);
				preparedStatement.setInt(5, total_votes);
				
				int review_count = ((Long) jsonObject.get("review_count")).intValue();
				preparedStatement.setInt(6, review_count);
				
				String user_name = (String) jsonObject.get("name");	
				preparedStatement.setString(7, user_name);
				
				String user_id = (String) jsonObject.get("user_id");
				preparedStatement.setString(8, user_id);
				
				int fans = ((Long) jsonObject.get("fans")).intValue();
				preparedStatement.setInt(9, fans);
		
				float average_stars = ((Double) jsonObject.get("average_stars")).floatValue();
				preparedStatement.setFloat(10, average_stars);
				
				JSONArray friendArray = (JSONArray) jsonObject.get("friends");
				int friends_count = friendArray.size();				
				preparedStatement.setInt(11, friends_count);
				preparedStatement.executeUpdate();
				
				//Insert into Friends Table
				if(jsonObject.get("friends")!=null) {
					JSONArray friendsArray = (JSONArray) jsonObject.get("friends");
					Iterator<String> iterator = friendsArray.iterator();
					String friend_id;		
					while(iterator.hasNext())
					{
						friend_id = iterator.next();
						preparedStatement2.setString(1, user_id);
						preparedStatement2.setString(2, friend_id);
						preparedStatement2.executeUpdate();
					}
				}	
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
				if (preparedStatement2 != null) 
				{
					preparedStatement2.close();
				}
				
				if (dbConnection != null) 
				{
					dbConnection.close();
				}
			}
	}
}
