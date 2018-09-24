import java.sql.*;
public class clearTable {
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
	public void deleteContents() throws SQLException{
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;		
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		PreparedStatement preparedStatement7 = null;
		PreparedStatement preparedStatement8 = null;
		PreparedStatement preparedStatement9 = null;
		PreparedStatement preparedStatement10 = null;
		
		try
		{
			dbConnection = getDBConnection(); 
			dbConnection.setAutoCommit(false);
			String query = "delete from checkin";
			preparedStatement = dbConnection.prepareStatement(query);			
			preparedStatement.executeUpdate();
			System.out.println("Deleted contents of checkin table");
			String query2 = "delete from reviews";
			preparedStatement2 = dbConnection.prepareStatement(query2);			
			preparedStatement2.executeUpdate();
			dbConnection.commit();
			System.out.println("Deleted contents of reviews table");
			String query3 = "delete from business_attributes";
			preparedStatement3 = dbConnection.prepareStatement(query3);
			preparedStatement3.executeUpdate();
			System.out.println("Deleted contents of business_attributes table");
			String query4 = "delete from business_sub_category";
			preparedStatement4 = dbConnection.prepareStatement(query4);
			preparedStatement4.executeUpdate();
			System.out.println("Deleted contents of business_sub_category table");
			String query5 = "delete from business_category";
			preparedStatement5 = dbConnection.prepareStatement(query5);
			preparedStatement5.executeUpdate();
			System.out.println("Deleted contents of business_category table");
			String query6 = "delete from business_hours";
			preparedStatement6 = dbConnection.prepareStatement(query6);
			preparedStatement6.executeUpdate();
			System.out.println("Deleted contents of business_hours table");
			String query7 = "delete from business";
			preparedStatement7 = dbConnection.prepareStatement(query7);
			preparedStatement7.executeUpdate();
			System.out.println("Deleted contents of business table");
			String query8 = "delete from bus_maincategory";
			preparedStatement8 = dbConnection.prepareStatement(query8);
			preparedStatement8.executeUpdate();
			System.out.println("Deleted contents of bus_maincategory table");
			String query9 = "delete from friends";
			preparedStatement9 = dbConnection.prepareStatement(query9);
			preparedStatement9.executeUpdate();
			System.out.println("Deleted contents of friends table");
			String query10 = "delete from yelp_user";
			preparedStatement10 = dbConnection.prepareStatement(query10);
			preparedStatement10.executeUpdate();
			System.out.println("Deleted contents of yelp_user table");
			dbConnection.commit();
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
			dbConnection.setAutoCommit(true); 
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
			if (preparedStatement7 != null) 
			{
				preparedStatement7.close();
			}
			if (preparedStatement8 != null) 
			{
				preparedStatement8.close();
			}
			if (preparedStatement9 != null) 
			{
				preparedStatement9.close();
			}
			if (preparedStatement10 != null) 
			{
				preparedStatement10.close();
			}						
			if (dbConnection != null) 
			{
				dbConnection.close();
			}			
		}
	}
}
