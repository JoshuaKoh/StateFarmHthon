package com.statefarm.codingcomp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    		String mailDay = m.getSent().toString().split("T")[0];
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
    	List<Email> smallerEmails = emails;
    	
    	for (User u : users) {
    		for (int emailIndex = 0; emailIndex < smallerEmails.size(); ) {
    			Email e = smallerEmails.get(emailIndex);
    			if (e.getFrom().equals(u.getEmail())) {
    				if (emailsFrom.containsKey(u)) {
    					emailsFrom.get(u).add(e);
    				} else {
    					List<Email> userMail = new ArrayList<Email>();
    					userMail.add(e);
    					emailsFrom.put(u, userMail);
    				}
    				smallerEmails.remove(emailIndex);
    			} else {
    				emailIndex++;
    			}
    		}
    	}
        return emailsFrom;
    }

    @Override
    public Map<User, List<Email>> emailsToOurUsers() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Email> emailsToUserFromUser() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> emailAddressesByDegrees( String email, int degrees ) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int degreesBetween( String emailAddressOne, String emailAddressTwo ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String mostConnected() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String mostActiveSender() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String mostActiveReceiver() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String mostSingularSender() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String mostSelfEmails() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int mostPopularHour() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int mostPopularHour( String email ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int leastPopularHour() throws Exception {
        // TODO Auto-generated method stub
        return 0;
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

    // TODO Terrible implementation
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
