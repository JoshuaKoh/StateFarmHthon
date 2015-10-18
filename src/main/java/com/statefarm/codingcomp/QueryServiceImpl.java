package com.statefarm.codingcomp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.statefarm.codingcomp.model.Email;
import com.statefarm.codingcomp.model.User;
import com.statefarm.codingcomp.reader.Reader;

public class QueryServiceImpl implements QueryService {

	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    Reader reader = new Reader();
    List<User> users = convertToUsers();
    List<Email> emails = convertToEmails();

    @Override
    public List<User> usersByDomain( String domain ) throws Exception {
        List<User> matchingUsers = new ArrayList<User>();

        for (User u: users) {
        	String userDomain = u.getEmail().split("@")[1];
        	if (domain.equalsIgnoreCase(userDomain)) {
        		matchingUsers.add(u);
        	}
        }
        return matchingUsers;
    }

    @Override
    public List<Email> emailsInDateRange( Date start, Date end ) throws Exception {
        List<Email> returning = new ArrayList<Email>();
        
        for (Email e : emails) {
        	if (e.getSent().after(start) && e.getSent().before(end)) {
        		returning.add(e);
        	}
        }
        
        return returning;
    }

    @Override
    public Map<String, List<Email>> emailsByDay() throws Exception {
    	Map<String, List<Email>> emailsByDay = new HashMap<String, List<Email>>();
    	
    	
    	for(Email m :emails) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(m.getSent());
    		String mailDay = "";
    		mailDay += cal.get(Calendar.YEAR) + "-";
    		int month = cal.get(Calendar.MONTH) + 1;
    		if (month<10) {
    			mailDay += "0" + month + "-";
    		} else {
    			mailDay += month + "-";
    		}
    		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
    		if (dayOfMonth < 10) {
    			mailDay += "0" + dayOfMonth;
    		} else {
    			mailDay += dayOfMonth;
    		}
    		if(emailsByDay.containsKey(mailDay)) {
    			emailsByDay.get(mailDay).add(m);
    		} else {
    			emailsByDay.put(mailDay, new ArrayList<Email>());
    			emailsByDay.get(mailDay).add(m);
    		}
    		
    	}
        return emailsByDay;
    }

    @Override
    public int emailsOnDay( Date date ) throws Exception {
        int emailCount = 0;

        for (Email e : emails) {
        	Date eDate = e.getSent();
        	Calendar cal1 = Calendar.getInstance();
        	Calendar cal2 = Calendar.getInstance();
        	cal1.setTime(eDate);
        	cal2.setTime(date);
        	if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && 
        			cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
        		emailCount++;
        	}
        }
        return emailCount;
    }

    @Override
    public Map<User, List<Email>> emailsFromOurUsers() throws Exception {
    	Map<User, List<Email>> emailsFrom = new HashMap<User, List<Email>>();
    	
    	for (User u : users) {
    		for (Email e : emails) {
    			if (e.getFrom().equals(u.getEmail())) {
    				if (emailsFrom.containsKey(u)) {
    					emailsFrom.get(u).add(e);
    				} else {
    					List<Email> userMail = new ArrayList<Email>();
    					userMail.add(e);
    					emailsFrom.put(u, userMail);
    				}
    			} 
    		}
    	}
        return emailsFrom;
    }

    @Override
    public Map<User, List<Email>> emailsToOurUsers() throws Exception {
    	Map<User, List<Email>> emailsTo = new HashMap<User, List<Email>>();
    	
    	for (User u : users) {
    		for (Email e : emails) {
    			if (e.getTo().equals(u.getEmail())) {
    				if (emailsTo.containsKey(u)) {
    					emailsTo.get(u).add(e);
    				} else {
    					List<Email> userMail = new ArrayList<Email>();
    					userMail.add(e);
    					emailsTo.put(u, userMail);
    				}
    			}
    		}
    	}
        return emailsTo;
    }

    @Override
    public List<Email> emailsToUserFromUser() throws Exception {
    	List<Email> fromUserToUser = new ArrayList<Email>();
    	
    	for (Email e : emails) {
    		if (users.contains(new User(e.getFrom())) && users.contains(new User(e.getTo()))) {
    			fromUserToUser.add(e);
    		}
    	}
        return fromUserToUser;
    }

    @Override
    public Set<String> emailAddressesByDegrees( String email, int degrees ) throws Exception {
    	Set<String> returning = new HashSet<String>();
    	returning.add(email);
    	
    	for (int i = degrees; i > 0; i--) {
    		Set<String> emailTemp = new HashSet<String>();
	    		for (String s : returning) {
	    			for (Email e : emails) {
	    				if (s.equals(e.getFrom())) {
	    					emailTemp.add(e.getTo());
	    				}
	    				if (s.equals(e.getTo())) {
	    					emailTemp.add(e.getFrom());
	    				}
	    			}
	    		}
	    		for (String s : emailTemp) {
	    			returning.add(s);
	    		}
    	}
    	return returning;
    }
    
    @Override
    public int degreesBetween( String emailAddressOne, String emailAddressTwo ) throws Exception {
    	int degrees = 1;
    	Set<String> emails = emailAddressesByDegrees(emailAddressOne, degrees);
    	int oldNum = emails.size();
    	int newNum = 0;
    	
    	while (oldNum != newNum) {
    		if (emails.contains(emailAddressTwo)) {
    			return degrees;
    		}
    		degrees++;
    		emails = emailAddressesByDegrees(emailAddressOne, degrees);
    		oldNum = newNum;
    		newNum = emails.size();
    	}
        return -1;
    }

    @Override
    public String mostConnected() throws Exception {

    	Map<String, Integer> emailsSent = new HashMap<String, Integer>();
    	
    	for (Email e : emails) {
			
				String email = e.getTo();
				if (!emailsSent.containsKey(email)) {
	    			Integer count = emailAddressesByDegrees(email, 1).size();
	        		emailsSent.put(email, count);
	    		}
		
				email = e.getFrom();
				if (!emailsSent.containsKey(email)) {
	    			Integer count = emailAddressesByDegrees(email, 1).size();
	        		emailsSent.put(email, count);
	    		}
    	
    	}
    	Entry<String,Integer> maxEntry = null;

    	for(Entry<String,Integer> entry : emailsSent.entrySet()) {
    		
    	    if (maxEntry == null || entry.getValue().intValue() > maxEntry.getValue().intValue()) {
    	        maxEntry = entry;
    	    }
    	}
        return maxEntry.getKey();
    }

    @Override
    public String mostActiveSender() throws Exception {
    	String maxUsr = "";
    	int max = 0;
    	for (Email e : emails) {
    		int userCount = 0;
    		String newUser = e.getFrom();
    		for (Email em : emails)  {
    			if (em.getFrom().equals(newUser)) {
    				userCount++;
    			}
    		}
    		if (userCount > max) {
    			maxUsr = newUser;
    			max = userCount;
    		}
    	}
        return maxUsr;
    }

    @Override
    public String mostActiveReceiver() throws Exception {
    	String maxUsr = "";
    	int max = 0;
    	for (Email e : emails) {
    		int userCount = 0;
    		String newUser = e.getTo();
    		for (Email em : emails)  {
    			if (em.getTo().equals(newUser)) {
    				userCount++;
    			}
    		}
    		if (userCount > max) {
    			maxUsr = newUser;
    			max = userCount;
    		}
    	}
        return maxUsr;
    }

    @Override
    public String mostSingularSender() throws Exception {
    	String maxUsr = "";
    	int max = 0;
    	for (Email e : emails) {
    		int userCount = 0;
    		String newUser = e.getFrom();
    		Map<String, Integer> newUserSent = new HashMap<String, Integer>();
    		for (Email em : emails)  {
    			if (em.getFrom().equals(newUser)) {
    				if (newUserSent.containsKey(em.getTo())) {
    					Integer count = newUserSent.get(em.getTo());
    					count++;
    					newUserSent.put(em.getTo(), count);
    				} else {
    					newUserSent.put(em.getTo(), new Integer(1));
    				}
    			}
    		}
    		for (Integer i : newUserSent.values()) {
    			if (i > max) {
    				maxUsr = newUser;
    				max = userCount;
    			}
    		}
    	}
        return maxUsr;
    }

    @Override
    public String mostSelfEmails() throws Exception {
    	String maxUsr = "";
    	int max = 0;
    	for (Email e : emails) {
    		int userCount = 0;
    		String newUser = e.getTo();
    		for (Email em : emails)  {
    			if (em.getTo().equals(newUser) && em.getFrom().equals(newUser)) {
    				userCount++;
    			}
    		}
    		if (userCount > max) {
    			maxUsr = newUser;
    			max = userCount;
    		}
    	}
        return maxUsr;
    }

    @Override
    public int mostPopularHour() throws Exception {
    	int[] hourCount = new int[24];
    	for (Email e : emails) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(e.getSent());
    		int hour = cal.get(Calendar.HOUR_OF_DAY);
    		hourCount[hour] = hourCount[hour] + 1;
    	}
    	int max = 0;
    	int returning = 0;
    	for (int i = 0; i < hourCount.length; i++) {
    		if (hourCount[i] > max) {
    			max = hourCount[i];
    			returning = i;
    		}
    	}
    	return returning;
    }

    @Override
    public int mostPopularHour( String email ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int leastPopularHour() throws Exception {
    	int[] hourCount = new int[24];
    	for (Email e : emails) {
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(e.getSent());
    		int hour = cal.get(Calendar.HOUR_OF_DAY);
    		hourCount[hour] = hourCount[hour] + 1;
    	}
    	int min = Integer.MAX_VALUE;
    	int returning = 0;
    	for (int i = 0; i < hourCount.length; i++) {
    		if (hourCount[i] < min) {
    			min = hourCount[i];
    			returning = i;
    		}
    	}
    	return returning;
    }

    @Override
    public int leastPopularHour( String email ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String mostPopularDate() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String mostPopularDate( String email ) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String leastPopularDate() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String leastPopularDate( String email ) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Converts users.txt files into a List of User objects
     * @return List of all User objects
     */
    private List<User> convertToUsers() {
		try {
			String[] userStr1 = reader.read( 1, "users.txt");
			String[] userStr2 = reader.read( 2, "users.txt");
			String[] userStr3 = reader.read( 3, "users.txt");
			String[] userStr4 = reader.read( 4, "users.txt");
			List<String> rawUsers = new ArrayList<String>();
			rawUsers.addAll(Arrays.asList(userStr1));
			rawUsers.addAll(Arrays.asList(userStr2));
			rawUsers.addAll(Arrays.asList(userStr3));
			rawUsers.addAll(Arrays.asList(userStr4));
			List<User> users = new ArrayList<User>();
			Scanner sc;
			for (String s : rawUsers) {
				sc = new Scanner(s);
				sc.useDelimiter(",");
				sc.next();
				String name = sc.next();
				String email = sc.next();
				User newUser = new User(name, email);
				users.add(newUser);
			}
			return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * Converts mail.txt files into a List of Email objects
     * @return List of all Email objects
     */
    private List<Email> convertToEmails() {
    	try {
    		
    		String[] emailStr1 = reader.read( 1, "mail.txt");
    		String[] emailStr2 = reader.read( 2, "mail.txt");
    		String[] emailStr3 = reader.read( 3, "mail.txt");
    		String[] emailStr4 = reader.read( 4, "mail.txt");
    		List<String> rawEmails = new ArrayList<String>();
    		rawEmails.addAll(Arrays.asList(emailStr1));
    		rawEmails.addAll(Arrays.asList(emailStr2));
    		rawEmails.addAll(Arrays.asList(emailStr3));
    		rawEmails.addAll(Arrays.asList(emailStr4));
    		List<Email> emails = new ArrayList<Email>();
    		Scanner sc;
    		for (String s : rawEmails) {
    			sc = new Scanner(s);
    			sc.useDelimiter(",");
    			String from = sc.next();
    			String to = sc.next();
    			String dateStr = sc.next();
    			dateStr = dateStr.substring(0, 10) + " " + dateStr.substring(11, 19);
    			Date date = sdf.parse(dateStr);
    			Email newEmail = new Email(from, to, date);
    			emails.add(newEmail);
    		}
    		return emails;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

}
