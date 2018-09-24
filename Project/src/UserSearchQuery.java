public class UserSearchQuery {
	public String Query_Yelpuser(String yelping_since,String rev_count_cond,String review_count,String frnd_count_cond,String no_of_friends,String avg_stars_cond,String avg_stars,String or_and) {
		String result = "";
		boolean and = false;
		result = "SELECT distinct user_id,name,yelping_since,total_votes,review_count,friends_count,fans,average_stars FROM YELP_USER a WHERE";
		if(review_count != null) {
			result += " a.review_count"+ rev_count_cond + review_count;
			and = true;
		} 
		if(avg_stars != null) {
			if(and) {
				result += " "+ or_and;
			}
			result += " a.AVERAGE_STARS" + avg_stars_cond + avg_stars;
			and = true;
		}
		if(yelping_since != null) {
			if(and) {
				result += " "+ or_and;
			}
			result += " a.YELPING_SINCE >= to_date('"+ yelping_since +"','YYYY-MM-DD')";
			and = true;
		}	
		if(no_of_friends!=null) { 
			if(and) {
				result += " "+ or_and;
			}
			result += " a.FRIENDS_COUNT" + frnd_count_cond + no_of_friends;
		}		
		return result;
	}	
	
}
