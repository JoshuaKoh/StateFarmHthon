package com.statefarm.codingcomp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public List<User> usersByDomain( String domain ) throws Exception {
    	String[] st = reader.read(1, "mail.txt");
//    	for (String s : st) {
//    		System.out.println(s);
//    	}
    	List<Email> em = convertToEmails(st);
    	for (Email e : em) {
    		System.out.println(e.getFrom());
    	}
    	return null;
    }

    @Override
    public List<Email> emailsInDateRange( Date start, Date end ) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, List<Email>> emailsByDay() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int emailsOnDay( Date date ) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Map<User, List<Email>> emailsFromOurUsers() throws Exception {
        // TODO Auto-generated method stub
        return null;
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
    
    private List<User> convertToUsers(String[] userStr) {
    	List<User> users = new ArrayList<User>();
    	Scanner sc;
    	for (String s : userStr) {
    		sc = new Scanner(s);
    		sc.useDelimiter(",");
    		sc.next();
    		String name = sc.next();
    		String email = sc.next();
    		User newUser = new User(name, email);
    		users.add(newUser);
    	}
		return users;
    }
    
    // TODO Terrible implementation
    private List<Email> convertToEmails(String[] emailStr) throws ParseException {
    	List<Email> emails = new ArrayList<Email>();
    	Scanner sc;
    	for (String s : emailStr) {
    		sc = new Scanner(s);
    		sc.useDelimiter(",");
    		String from = sc.next();
    		String to = sc.next();
    		String dateStr = sc.next();
    		dateStr = dateStr.substring(0, 10) + " " + dateStr.substring(12, 19);
    		Date date = sdf.parse(dateStr);
    		Email newEmail = new Email(from, to, date);
    		emails.add(newEmail);
    	}
    	return emails;
    }
    
}
