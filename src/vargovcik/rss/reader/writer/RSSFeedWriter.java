package vargovcik.rss.reader.writer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vargovcik.rss.reader.dto.RSSFixture;
import vargovcik.rss.reader.dto.RSSLotto;
import vargovcik.rss.reader.dto.RSSNews;
import vargovcik.rss.reader.dto.RSSResult;

public class RSSFeedWriter {
	
	private Properties propObj = null;
	private String dbURL;
	
	public RSSFeedWriter(String url, String user, String password) {
		this.dbURL = url;
		propObj = new Properties();
		propObj.setProperty("user", user);
		propObj.setProperty("password", password);
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveFixtures(List<RSSFixture> fixtures) {
		
		String sqlQuery = "call insertFixtures(?,?,?,?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
				CallableStatement callable = connection.prepareCall(sqlQuery); ) {
			
	        int i = 0;

	        for (RSSFixture entity : fixtures) {
	        	
	        	if(entity == null) {
	        		 i++;
	        		continue;
	        	}
	        	
	        	callable.setString(1, entity.getVenue());
	        	callable.setString(2, entity.getHome());
	        	callable.setString(3, entity.getAway());
	        	callable.setString(4, null);
	        	callable.setString(5, entity.getDivision());
	        	callable.setTimestamp(6, toSqlDateTime(entity.getDateTime()));
	        	
	            callable.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == fixtures.size()) {
	            	int[] updateCounts = callable.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void savelotto(List<RSSLotto> lotto) {
		
		String sqlQuery = "call insertlotto(?,?,?,?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
				CallableStatement callable = connection.prepareCall(sqlQuery); ) {
			
	        int i = 0;

        	for (RSSLotto entity : lotto) {
	        	
	        	if(entity == null)
	        		continue;
	        	

	            callable.setDate(1, toSqlDateTime(entity.getDrawDate()));
	        	callable.setInt(2, entity.getDraw()[0]);
	        	callable.setInt(3, entity.getDraw()[1]);
	            callable.setInt(4, entity.getDraw()[2]);
	            callable.setInt(5, entity.getDraw()[3]);
	            callable.setString(6, entity.getMessage());
	        	
	            callable.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == lotto.size()) {
	            	int[] updateCounts = callable.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	public void saveNews(List<RSSNews> news) {


		String sqlQuery = "call insertMessage(?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
				CallableStatement callable = connection.prepareCall(sqlQuery); ) {
			
	        int i = 0;

	        for (RSSNews entity : news) {
	        	
	        	if(entity == null) {
	        		 i++;
	        		continue;
	        	}
	        	
	        	callable.setString(1, entity.getTitle());
	        	callable.setString(2, entity.getNewsMessage());
	        	callable.setTimestamp(3, toSqlDateTime(entity.getDateTime()));
	        	
	            callable.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == news.size()) {
	            	int[] updateCounts = callable.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void saveResults(List<RSSResult> results) {

		String sqlQuery = "call insertResults(?,?,?,?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
				CallableStatement callable = connection.prepareCall(sqlQuery); ) {
			
	        int i = 0;

        	for (RSSResult entity : results) {
	        	
	        	if(entity == null)
	        		continue;
	        	
	        	
	        	callable.setTimestamp(1, toSqlDateTime(entity.getDateTime()));
	        	callable.setString(2, entity.getHome());
	        	callable.setString(3, entity.getAway());
	        	callable.setString(4, entity.getHomeScore());
	        	callable.setString(5, entity.getAwayScore());
	        	callable.setString(6, entity.getDivision());

	        	callable.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == results.size()) {
	            	callable.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	private Timestamp toSqlDateTime(LocalDateTime dateTime) {
		ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());		
		return new Timestamp(zdt.toInstant().toEpochMilli());
	}

	private Date toSqlDateTime(LocalDate date) {
		return Date.valueOf(date);
	}

}
