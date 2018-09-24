import java.util.HashSet;
public class BusinessSearchQuery {
	public String generateCategoriesINFormat(HashSet<String> categories) {
		StringBuilder sb = new StringBuilder();
    	sb.append("(");
		for(String temp:categories) {
			sb.append("'");
			sb.append(temp);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append (")");
		return sb.toString();
    }	
	public String queryBusiness(HashSet<String> mainCategory,HashSet<String> subCategory,String checkinQuery,String reviewQuery) {
		StringBuilder result = new StringBuilder(); 
		//MainCategory
		result.append("select distinct a.business_id,a.business_name,a.city,a.state,a.review_count,a.stars \n");
		result.append("from business a,business_category b \n");
		result.append("where a.business_id = b.business_id and\n b.category_name IN " );
		String mc = generateCategoriesINFormat(mainCategory);
		result.append(mc);
		//Sub Category
		if(subCategory.size()!=0) {
			result.append("and  a.business_id in (\n" );
			result.append("select distinct business_id from business_sub_category sc \nWHERE sc.category_name IN" );
			String sc = generateCategoriesINFormat(subCategory);
			result.append(sc);
			result.append(")\n");			
		}
		//Checkin
		if(checkinQuery.length()!=0) {
			result.append("and  a.business_id in (\n" );
			result.append(checkinQuery);
			result.append(")\n");
		}
		//Review
		if(reviewQuery.length()!=0) {
			result.append("and  a.business_id in (\n" );
			result.append(reviewQuery);
			result.append(")\n");
		}
		return result.toString();	    
	}
	public String queryCheckin(int from_day,String from_hr,int to_day,String to_hr,String no_of_checkins,String checkin_cond) {
		StringBuilder result = new StringBuilder();
		boolean test = false;
		result.append("select business_id from checkin ");	
		if(from_day!=-1 && to_day !=-1) {
			result.append("\nwhere ((work_day = ");
			result.append(from_day);
			result.append(" and from_hr >= ");
			result.append(from_hr);
			result.append(" )");
			result.append("or (work_day = ");
			result.append(to_day);
			result.append(" and to_hr <= ");
			result.append(to_hr);
			result.append(" )");
			test = true;
		}
			if(from_day+1 <= to_day-1 && (from_day!=-1 && to_day !=-1)) {
				result.append("or (work_day between ");
				result.append(from_day+1);
				result.append(" and ");
				result.append(to_day-1);
				result.append(" )");
			}
			else if(from_day > to_day && (from_day!=-1 && to_day !=-1)) {
				result.append("or (work_day not between ");
				result.append(to_day);				
				result.append(" and ");
				result.append(from_day);
				result.append(" )");
			}
			if(test) result.append(" )");
		if(no_of_checkins != null) {
			result.append("\ngroup by business_id");
			result.append("\nhaving sum(number_of_checkins) "); 
			result.append(checkin_cond);
			result.append(no_of_checkins);
		}
		return result.toString();
	}	
	
	public String queryReview(String fromDate,String toDate,String starCond,String stars,String votesCond,String total_votes) {
		StringBuilder result = new StringBuilder();
		result.append("select business_id from reviews b where ");	
		boolean test = false;
		if(fromDate != null) {
			result.append("b.REVIEW_DATE >= to_date('");
			result.append(fromDate);
			result.append("','YYYY-MM-DD')");
			test = true;
		}
		if(toDate != null) {
			if(test) {
				result.append(" and");
			}
			result.append(" b.REVIEW_DATE <= to_date('");
			result.append(toDate);
			result.append("','YYYY-MM-DD')");
			test = true;
		}
		if(stars != null) {
			if(test) {
				result.append(" and");
			}
			result.append(" b.STARS");  
			result.append(starCond);
			result.append(stars);
			test = true;
		}
		if(total_votes != null) {
			if(test) {
				result.append(" and");
			}
			result.append(" b.TOTAL_VOTES");  
			result.append(votesCond);
			result.append(total_votes);
		}
		return result.toString();
	}		
	
}
