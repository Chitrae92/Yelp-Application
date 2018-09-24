import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.sql.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
public class populateBusiness {
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
	public void readBusinessJsonFile(String businessFile) throws SQLException{		
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		String query = "INSERT INTO BUSINESS" 
						+ "(BUSINESS_ID, ADDRESS, OPEN, CITY, STATE, LATITUDE, LONGITUDE, REVIEW_COUNT, BUSINESS_NAME, STARS) VALUES" 
						+ "(?,?,?,?,?,?,?,?,?,?)";
		String query2 = "INSERT INTO BUSINESS_HOURS" 
						+ "(BUSINESS_ID, WORK_DAY, CLOSE_TIME, OPEN_TIME) VALUES" 
						+ "(?,?,?,?)";
		String query3 = "INSERT INTO BUS_MAINCATEGORY" 
						+ "(CATEGORY_NAME) VALUES" 
						+ "(?)";
		String query4 = "INSERT INTO BUSINESS_CATEGORY" 
						+ "(CATEGORY_NAME, BUSINESS_ID) VALUES" 
						+ "(?,?)";
		String query5 = "INSERT INTO BUSINESS_SUB_CATEGORY" 
						+ "(CATEGORY_NAME, BUSINESS_ID) VALUES" 
						+ "(?,?)";
		String query6 = "INSERT INTO BUSINESS_ATTRIBUTES" 
						+ "(ATTRIBUTE_NAME, BUSINESS_ID) VALUES" 
						+ "(?,?)";
		JSONParser parser = new JSONParser();
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(query);
			preparedStatement2 = dbConnection.prepareStatement(query2);
			preparedStatement3 = dbConnection.prepareStatement(query3);
			preparedStatement4 = dbConnection.prepareStatement(query4);
			preparedStatement5 = dbConnection.prepareStatement(query5);
			preparedStatement6 = dbConnection.prepareStatement(query6);
			HashSet<String> set = new HashSet<String>();
			set.add("Active Life");				
	        set.add("Arts & Entertainment");
	        set.add("Automotive");
	        set.add("Car Rental");
	        set.add("Cafes");
	        set.add("Beauty & Spas");
	        set.add("Convenience Stores");
	        set.add("Dentists");
	        set.add("Doctors");
	        set.add("Drugstores");
	        set.add("Department Stores");
	        set.add("Education");
	        set.add("Event Planning & Services");
	        set.add("Flowers & Gifts");
	        set.add("Food");
	        set.add("Health & Medical");
	        set.add("Home Services");
	        set.add("Home & Garden");
	        set.add("Hospitals");
	        set.add("Hotels & Travel");
	        set.add("Hardware Stores");
	        set.add("Grocery");
	        set.add("Medical Centers");
	        set.add("Nurseries & Gardening");
	        set.add("Nightlife");
	        set.add("Restaurants");
	        set.add("Shopping");
	        set.add("Transportation");
	        
	        // Insert into Bus_MainCategory table
	        for(String temp:set){
	           preparedStatement3.setString(1, temp);
	           preparedStatement3.executeUpdate();
	        }			
			FileReader filereader = new FileReader(businessFile);
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			while ((line = bufferedReader.readLine()) != null){
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;	
				
				// Insert into Business table
				String business_id = (String) jsonObject.get("business_id");				
				preparedStatement.setString(1, business_id);
				
				String full_address = (String) jsonObject.get("full_address");
				preparedStatement.setString(2, full_address);
				
				boolean openStatus = (Boolean) jsonObject.get("open");
				int open;
				if (openStatus) {
					open = 1;
				}
				else{				
					open = 0;
				}
				preparedStatement.setInt(3, open);
				
				String city = (String) jsonObject.get("city");
				preparedStatement.setString(4, city);
				
				String state = (String) jsonObject.get("state");
				preparedStatement.setString(5, state);
				
				float latitude = ((Double) jsonObject.get("latitude")).floatValue();
				preparedStatement.setFloat(6, latitude);
				
				float longitude = ((Double) jsonObject.get("longitude")).floatValue();
				preparedStatement.setFloat(7, longitude);
				
				int review_count = ((Long) jsonObject.get("review_count")).intValue();
				preparedStatement.setInt(8, review_count);
				
				String business_name = (String) jsonObject.get("name");
				preparedStatement.setString(9, business_name);
				
				float stars = ((Double) jsonObject.get("stars")).floatValue();
				preparedStatement.setFloat(10, stars);
				
				preparedStatement.executeUpdate();	
				
				// Insert into Business_hours table
				if(jsonObject.get("hours")!=null)
				{
					JSONObject jsonObject2 = (JSONObject) jsonObject.get("hours");
					for (Object key : jsonObject2.keySet()) 
					{
				        String day = (String)key;				        
				        JSONObject jsonObject3 = (JSONObject) jsonObject2.get(day);
				        String open_time = (String) jsonObject3.get("open");
				        Float o_t = convert_hour(open_time);
				        String close_time = (String) jsonObject3.get("close");
				        Float c_t = convert_hour(close_time);
				        preparedStatement2.setString(1, business_id);
				        preparedStatement2.setString(2, day);
				        preparedStatement2.setFloat(3, c_t);
					    preparedStatement2.setFloat(4, o_t);
				        preparedStatement2.executeUpdate();				       				    
				    }
				}	
			
				// Insert into Business_category and Business_Sub_Category tables
				JSONArray catArray = (JSONArray) jsonObject.get("categories");
				Iterator<String> iterator = catArray.iterator();
				String category;		
				while(iterator.hasNext())
				{
					category = iterator.next();
					if(set.contains(category)){
						preparedStatement4.setString(1, category);
						preparedStatement4.setString(2, business_id);
						preparedStatement4.executeUpdate();
					}
					else{
						preparedStatement5.setString(1, category);
						preparedStatement5.setString(2, business_id);
						preparedStatement5.executeUpdate();
					}
					
				}
				
				//Insert into Business_Atrributes table
				if(jsonObject.get("attributes")!=null)
				{
					JSONObject jsonObject4 = (JSONObject) jsonObject.get("attributes");
					for(Object key:jsonObject4.keySet()) {
						String keyString = (String)key;
						Object keyValue = jsonObject4.get(keyString);
						if(keyValue instanceof JSONObject) {
							JSONObject jsonObject5 = (JSONObject) jsonObject4.get(key);
							for (Object key2 : jsonObject5.keySet())
				        	{
				        		String keyString2 = (String)key2;
				        		Object keyValue2 = jsonObject5.get(keyString2);
				        		if (keyValue2 instanceof Integer)
				        		{
				        			String a_value = ((Long) jsonObject5.get(keyString2)).toString();
				        			keyString2 = keyString2 + ":" + a_value;
									preparedStatement6.setString(1, keyString2);
									preparedStatement6.setString(2, business_id);
									preparedStatement6.executeUpdate();
				        		}
				        		else if (keyValue2 instanceof String)
				        		{
				        			String a_value = (String) jsonObject5.get(keyString2);
				        			keyString2 = keyString2 + ":" + a_value;
									preparedStatement6.setString(1, keyString2);
									preparedStatement6.setString(2, business_id);
									preparedStatement6.executeUpdate();
				        		}
				        		else if (keyValue2 instanceof Boolean)
				        		{
				        			boolean a = (Boolean) jsonObject5.get(keyString2);
				        			String a_value = String.valueOf(a);
				        			keyString2 = keyString2 + ":" + a_value;
									preparedStatement6.setString(1, keyString2);
									preparedStatement6.setString(2, business_id);
									preparedStatement6.executeUpdate();
				        		}
				        	}
				        }
						else {
							if (keyValue instanceof Integer)
			        		{
			        			String a_value = ((Long) jsonObject4.get(keyString)).toString();
			        			keyString = keyString + ":" + a_value;
								preparedStatement6.setString(1, keyString);
								preparedStatement6.setString(2, business_id);
								preparedStatement6.executeUpdate();
			        		}
			        		else if (keyValue instanceof String)
			        		{
			        			String a_value = (String) jsonObject4.get(keyString);
			        			keyString = keyString + ":" + a_value;
								preparedStatement6.setString(1, keyString);
								preparedStatement6.setString(2, business_id);
								preparedStatement6.executeUpdate();
			        		}
			        		else if (keyValue instanceof Boolean)
			        		{
			        			boolean a = (Boolean) jsonObject4.get(keyString);
			        			String a_value = String.valueOf(a);
			        			keyString = keyString + ":" + a_value;
								preparedStatement6.setString(1, keyString);
								preparedStatement6.setString(2, business_id);
								preparedStatement6.executeUpdate();
			        		}
						}
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
			if (preparedStatement3 != null) 
			{
				preparedStatement3.close();
			}
			if (preparedStatement4 != null) 
			{
				preparedStatement4.close();
			}
			if (preparedStatement5 != null) 
			{
				preparedStatement5.close();
			}
			if (preparedStatement6 != null) 
			{
				preparedStatement6.close();
			}
			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}
	}
	public static Float convert_hour(String a)
	{
		String[] b = a.split(":");
		float c = Float.parseFloat(b[0]);
		float d = Float.parseFloat(b[1]);
		d = d / 100;
		c = c + d;
		return c;
	}
}
