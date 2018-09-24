import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.sql.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class populateCheckin {
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
	public void readCheckinJsonFile(String checkinFile) throws SQLException{
		System.out.println("File:"+checkinFile);
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String query = "INSERT INTO CHECKIN" 
					+ "(BUSINESS_ID, WORK_DAY, FROM_HR, TO_HR, NUMBER_OF_CHECKINS) VALUES" 
					+ "(?,?,?,?,?)";	
		JSONParser parser = new JSONParser();
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(query);
			FileReader filereader = new FileReader(checkinFile);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			int i=0;
			while ((line = bufferedReader.readLine()) != null){				
				System.out.println(i);
				i++;
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				
				String business_id = (String) jsonObject.get("business_id");
				preparedStatement.setString(1, business_id);
				
				JSONObject jsonObject2 = (JSONObject) jsonObject.get("checkin_info");
				int day;
				int from_hour;
				int to_hour;
				int checkin_count;
				
				for (Object key : jsonObject2.keySet()) 
				{
			        String keyString = (String)key;
			        Object keyValue = jsonObject2.get(keyString);
			        day = convert_day(keyString);
			        from_hour = convert_hour(keyString);
			        to_hour = from_hour+1;
			        checkin_count = ((Long) keyValue).intValue();
			        
			        preparedStatement.setInt(2, day);
			        preparedStatement.setInt(3, from_hour);
			        preparedStatement.setInt(4, to_hour);
			        preparedStatement.setInt(5, checkin_count);
			        preparedStatement.executeUpdate();
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
			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}
	}
	public static int convert_day(String a) {
		String[] b = a.split("-");
		return Integer.parseInt(b[1]);
		/*String c = "WRONG VALUE";
		if(b[1].equals("0")) {
			c = "SUNDAY";
		}
		if(b[1].equals("1")) {
			c = "MONDAY";
		}
		if(b[1].equals("2")) {
			c = "TUESDAY";
		}
		if(b[1].equals("3")) {
			c = "WEDNESDAY";
		}
		if(b[1].equals("4")){
			c = "THURSDAY";
		}
		if(b[1].equals("5")) {
			c = "FRIDAY";
		}
		if(b[1].equals("6")) {
			c = "SATURDAY";
		}
		return c;	*/	
	}
	public static int convert_hour(String a) {
		String[] b = a.split("-");
		return Integer.parseInt(b[0]);		
	}
				
}
