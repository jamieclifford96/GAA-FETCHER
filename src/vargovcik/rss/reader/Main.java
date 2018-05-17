package vargovcik.rss.reader;

import java.util.ArrayList;
import java.util.List;

import vargovcik.rss.reader.dto.RSSFeed;
import vargovcik.rss.reader.parser.RSSFeedParserV2;
import vargovcik.rss.reader.writer.RSSFeedWriter;
import vargovcik.rss.reader.dto.RSSFixture;
import vargovcik.rss.reader.dto.RSSLotto;
import vargovcik.rss.reader.dto.RSSNews;
import vargovcik.rss.reader.dto.RSSResult;

public class Main {
	
	public static void main(String[] args) {
		
		RSSFeedParserV2 parser = new RSSFeedParserV2("http://caulry.gaa.ie/club-news-1/posts.xml");
	    
		RSSFeed feed = parser.readFeed();
	    
		List<RSSFixture> fixtures = new ArrayList<>();
		List<RSSLotto> lotto = new ArrayList<>();
		List<RSSNews> news = new ArrayList<>();
		List<RSSResult> results = new ArrayList<>();
		
		feed.getEntries().stream().forEach((entry)-> entry.getEntrys().stream().forEach((message)-> {
			if(message == null)
				return;
			
			switch (message.getMessageType()) {
			case FIXTURE:
				fixtures.add((RSSFixture) message);
				break;
			case LOTTO:
				lotto.add((RSSLotto) message);
				break;
			case NEWS:
				news.add((RSSNews) message);
				break;
			case RESULT:
				results.add((RSSResult) message);
				break;

			default:
				break;
			}
		}));
		
		
		String url = "jdbc:mysql://localhost:3306/gaa_club";
		String user = "gaaWorker";
		String password = "password";	

		RSSFeedWriter writer = new RSSFeedWriter(url, user, password);
		
		writer.saveFixtures(fixtures);
		writer.savelotto(lotto);
		writer.saveNews(news);
		writer.saveResults(results);

}

	

}
