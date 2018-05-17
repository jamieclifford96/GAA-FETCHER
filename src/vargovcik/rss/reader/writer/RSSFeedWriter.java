package vargovcik.rss.reader.writer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

		String sqlQuery = "INSERT IGNORE INTO `gaa_club`.`fixtures` (`venue`,`home`,`away`,`ref`,`group`,`dateTime`) VALUES (?,?,?,?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
		      PreparedStatement statement = connection.prepareStatement(sqlQuery); ) {
			
	        int i = 0;

	        for (RSSFixture entity : fixtures) {
	        	
	        	if(entity == null)
	        		continue;
	        	
	            statement.setString(1, entity.getVenue());
	            statement.setString(2, entity.getHome());
	            statement.setString(3, entity.getAway());
	            statement.setString(4, null);
	            statement.setString(5, entity.getDivision());
	            statement.setDate(6, toSqlDateTime(entity.getDateTime()));

	            statement.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == fixtures.size()) {
	                statement.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void savelotto(List<RSSLotto> lotto) {

		String sqlQuery = "INSERT IGNORE INTO `gaa_club`.`lotto` (`draw1`,`draw2`,`draw3`,`draw4`,`message`,`drawdate`) VALUES (?,?,?,?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
		      PreparedStatement statement = connection.prepareStatement(sqlQuery); ) {
			
	        int i = 0;

	        for (RSSLotto entity : lotto) {
	        	
	        	if(entity == null)
	        		continue;
	        	
	            statement.setInt(1, entity.getDraw()[0]);
	            statement.setInt(2, entity.getDraw()[1]);
	            statement.setInt(3, entity.getDraw()[2]);
	            statement.setInt(4, entity.getDraw()[3]);
	            statement.setString(5, entity.getMessage());
	            statement.setDate(6, toSqlDateTime(entity.getDrawDate()));

	            statement.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == lotto.size()) {
	                statement.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void saveNews(List<RSSNews> news) {


		String sqlQuery = "INSERT IGNORE INTO `gaa_club`.`news` (`title`,`newsMessage`,`dateTime`) VALUES (?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
		      PreparedStatement statement = connection.prepareStatement(sqlQuery); ) {
			
	        int i = 0;

	        for (RSSNews entity : news) {
	        	
	        	if(entity == null)
	        		continue;
	        	
	            statement.setString(1, entity.getTitle());
	            statement.setString(2, entity.getNewsMessage());
	            statement.setDate(3, toSqlDateTime(entity.getDateTime()));

	            statement.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == news.size()) {
	                statement.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void saveResults(List<RSSResult> results) {

		String sqlQuery = "INSERT IGNORE INTO `gaa_club`.`results` (`home`,`away`,`homeScore`,`awayScore`,`division`,`dateTime`) VALUES (?,?,?,?,?,?);";
		
		try ( Connection connection = DriverManager.getConnection(dbURL,propObj);
		      PreparedStatement statement = connection.prepareStatement(sqlQuery); ) {
			
	        int i = 0;

	        for (RSSResult entity : results) {
	        	
	        	if(entity == null)
	        		continue;
	        	
	            statement.setString(1, entity.getHome());
	            statement.setString(2, entity.getAway());
	            statement.setString(3, entity.getHomeScore());
	            statement.setString(4, entity.getAwayScore());
	            statement.setString(5, entity.getDivision());
	            statement.setDate(6, toSqlDateTime(entity.getDateTime()));

	            statement.addBatch();
	            i++;

	            if (i % 1000 == 0 || i == results.size()) {
	                statement.executeBatch(); // Execute every 1000 items.
	            }
	        }
	    }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	private Date toSqlDateTime(LocalDateTime dateTime) {
		return Date.valueOf(dateTime.toLocalDate());
	}

	private Date toSqlDateTime(LocalDate date) {
		return Date.valueOf(date);
	}

}
