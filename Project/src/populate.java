import java.sql.SQLException;
public class populate 
{
	public static void main(String[] args) throws SQLException 
	{
		String businessFile = args[0];
    	String reviewFile = args[1];
    	String checkinFile = args[2];
    	String userFile = args[3];  
    	clearTable x = new clearTable();
    	System.out.println("DELETING CONTENTS OF THE TABLES");
    	x.deleteContents();
    	populateUser a = new populateUser();
    	System.out.println("PARSING USER FILE");
        a.readUserJsonFile(userFile);
        populateBusiness b = new populateBusiness();
    	System.out.println("PARSING BUSINESS FILE");
    	b.readBusinessJsonFile(businessFile);
    	populateReview c = new populateReview();
    	System.out.println("PARSING REVIEW FILE");
        c.readReviewJsonFile(reviewFile);
        populateCheckin d = new populateCheckin();
        System.out.println("PARSING CHECKIN FILE");
        d.readCheckinJsonFile(checkinFile);     
	}
}