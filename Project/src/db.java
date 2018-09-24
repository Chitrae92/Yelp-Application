import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class db {		
	//function for database connection
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
	
	//Function for generating result set based on type - user,business
	public void func(String query,String type,JScrollPane scrollPane) {
		try{  
			Connection dbConnection = getDBConnection();			 
			Statement stmt=dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				    ResultSet.CONCUR_READ_ONLY);
			ResultSet rs= stmt.executeQuery(query); 
			rs.last();
	        String[][] data;
			ResultSetMetaData rsmd = rs.getMetaData();
            int rowCount = rs.getRow();
            int columnCount = rsmd.getColumnCount();
            data = new String[rowCount][columnCount];
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }            
            rs.beforeFirst();
            for (int i = 0; i < rowCount; i++) {
                if (rs.next()) {
                    for (int j = 1; j <= columnCount; j++) {
                        data[i][j - 1] = rs.getString(j);
                    }
                }
            }
            rs.close();
            stmt.close();
            JTable table = new JTable();
    		scrollPane.setViewportView(table);
            DefaultTableModel defaultTableModel= new DefaultTableModel(data, columnNames);
            table.setModel(defaultTableModel);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);            
            table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) {
                        JTable target = (JTable)e.getSource();
                        int row = target.getSelectedRow();
                        String id = table.getModel().getValueAt(row, 0).toString();
                        showReview(id,type);
                    }
                }
            });
            dbConnection.close(); 
			}
		catch(Exception e){ System.out.println(e);}  
	}
	public List<String> getCategories(){
		List<String> mainCategories = new ArrayList<String>();
		try{  
			Connection dbConnection = getDBConnection();			 
			Statement stmt=dbConnection.createStatement();
			String query = "SELECT distinct CATEGORY_NAME from BUS_MAINCATEGORY order by CATEGORY_NAME";
			ResultSet rs= stmt.executeQuery(query);	
			while(rs.next()) {
				mainCategories.add(rs.getString(1));
            }
            dbConnection.close();             
			}
		catch(Exception e){ System.out.println(e);}
		return mainCategories;
	}
	public List<String> getSubCategories(String categoryList){
		List<String> subCategories = new ArrayList<String>();
		try{  
			Connection dbConnection = getDBConnection();			 
			Statement stmt=dbConnection.createStatement();
			StringBuilder query = new StringBuilder();
			query.append("SELECT distinct b.CATEGORY_NAME from BUSINESS_CATEGORY a,BUSINESS_SUB_CATEGORY b where a.business_id = b.business_id and a.category_name in");
			query.append(categoryList);
			ResultSet rs= stmt.executeQuery(query.toString());	
			while(rs.next()) {
				subCategories.add(rs.getString(1));
            }
            dbConnection.close();             
			}
		catch(Exception e){ System.out.println(e);}
		return subCategories;
	}
	
	public void showReview(String id,String type) {	
		JFrame reviewFrame = new JFrame("Reviews");
		reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reviewFrame.setSize(600, 600);
        reviewFrame.setLayout(new GridLayout(1, 1));
        reviewFrame.setVisible(true);
        DefaultTableModel reviewTableModel= new DefaultTableModel();
        JTable reviewTable = new JTable(reviewTableModel);
		JScrollPane scrollpane = new JScrollPane(reviewTable);
		try{  
			Connection dbConnection = getDBConnection();			 
			Statement stmt=dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				    ResultSet.CONCUR_READ_ONLY);
			StringBuilder query = new StringBuilder();
			if(type.equals("user")) {
				query.append("select b.name,a.user_id,a.total_votes,a.stars,a.review_date from reviews a,yelp_user b ");
				query.append("where a.user_id = b.user_id ");
				query.append("and a.user_id = '");
				query.append(id);
				query.append("'");
			}
			if(type.equals("business")) {
				query.append("select b.name,a.user_id,a.total_votes,a.stars,a.review_date from reviews a,yelp_user b ");
				query.append("where a.user_id = b.user_id ");
				query.append("and a.business_id = '");
				query.append(id);
				query.append("'");
			}
			ResultSet rs= stmt.executeQuery(query.toString()); 
			rs.last();
	        String[][] data;
			ResultSetMetaData rsmd = rs.getMetaData();
            int rowCount = rs.getRow();
            int columnCount = rsmd.getColumnCount();
            data = new String[rowCount][columnCount];
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }            
            rs.beforeFirst();
            for (int i = 0; i < rowCount; i++) {
                if (rs.next()) {
                    for (int j = 1; j <= columnCount; j++) {
                        data[i][j - 1] = rs.getString(j);
                    }
                }
            }
            rs.close();
            stmt.close();
            dbConnection.close(); 
            DefaultTableModel defaultTableModel= new DefaultTableModel(data, columnNames);
            reviewTable.setModel(defaultTableModel);
            reviewFrame.add(scrollpane);
            reviewFrame.setVisible(true);
            
			}
		catch(Exception e){ System.out.println(e);}  
	}

}
