package vargovcik.rss.reader.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import vargovcik.rss.reader.dto.RSSEntryCategory;
import vargovcik.rss.reader.dto.RSSFeed;
import vargovcik.rss.reader.dto.RSSFeedEntry;
import vargovcik.rss.reader.dto.RSSFixture;
import vargovcik.rss.reader.dto.RSSLink;
import vargovcik.rss.reader.dto.RSSLotto;
import vargovcik.rss.reader.dto.RSSMessage;
import vargovcik.rss.reader.dto.RSSNews;
import vargovcik.rss.reader.dto.RSSResult;

public class RSSFeedParserV2 {
    private static final String FEED = "feed";
    private static final String UPDATED = "updated";
    private static final String ID = "id";
	private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String GENERATOR = "generator";
    private static final String START_INDEX = "startIndex";
    private static final String ENTRY = "entry";

    private static final String ENTRY_ID = "id";
    private static final String ENTRY_PUBLISHED = "published";
    private static final String ENTRY_UPDATED = "updated";
    private static final String ENTRY_EDITED = "edited";
    private static final String ENTRY_CATEGORY = "category";
    private static final String ENTRY_TITLE = "title";
    private static final String ENTRY_CONTENT = "content";
    private static final String ENTRY_AUTHOR = "author";
    private static final String ENTRY_PAGE_NAME = "pageName";
    private static final String ENTRY_REVISION = "revision";
    
    private static final String CLUB_NAME = "Caulry";
    
	private final URL url;

	public RSSFeedParserV2(String feedUrl) {
		 try {
	            this.url = new URL(feedUrl);
	        } catch (MalformedURLException e) {
	            throw new RuntimeException(e);
	        }
	}

	public RSSFeed readFeed()  {
		
		RSSFeed feed = new RSSFeed();
		
        try {
    	SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		
		 Element root = document.getRootElement();

		    // iterate through child elements of root
		    for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
		        Element element = it.next();
		        
		        switch (element.getName()) {
				case ID:
					feed.setId(element.getStringValue());
					break;
				case UPDATED:
					feed.setUpdated(Instant.parse(element.getStringValue()));
					break;
				case TITLE:
					feed.setTitle(element.getStringValue());
					break;
				case LINK:
					feed.getLinks().add(parseLink(element));
					break;
				case GENERATOR:
					feed.setGenerator(element.getStringValue());
					break;
				case START_INDEX:
					feed.setStartIndex(Integer.parseInt(element.getStringValue()));
					break;
				case ENTRY:
					feed.getEntries().add(parseEntry(element));
					break;

				default:
					break;
				}	
		    }		
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return feed;
	}

	private RSSFeedEntry parseEntry(Element parentElement) {
		RSSFeedEntry feedEntry = new RSSFeedEntry();

		for (Iterator<Element> it = parentElement.elementIterator(); it.hasNext();) {
	        Element element = it.next();
	        
	        switch (element.getName()) {
			case ENTRY_ID:
				feedEntry.setId(element.getStringValue());
				break;
			case ENTRY_PUBLISHED:
				feedEntry.setPublished(Instant.parse(element.getStringValue()));
				break;
			case ENTRY_UPDATED:
				feedEntry.setUpdated(Instant.parse(element.getStringValue()));
				break;
			case ENTRY_EDITED:
				feedEntry.setEdited(Instant.parse(element.getStringValue()));
				break;
			case ENTRY_CATEGORY:
				feedEntry.setCategory(parseEntryCategory(element));
				break;
			case ENTRY_TITLE:
				feedEntry.setTitle(element.getStringValue());
				break;
			case ENTRY_CONTENT:
				feedEntry.setEntrys(parseContent(element, LocalDateTime.ofInstant(feedEntry.getUpdated(), ZoneOffset.UTC)));
				break;
			case ENTRY_AUTHOR:
				feedEntry.setAuthorName(element.element("name").getStringValue());
				feedEntry.setAuthorEmail(element.element("email").getStringValue());
				break;
			case LINK:
				feedEntry.getLinks().add(parseLink(element));
				break;
			case ENTRY_PAGE_NAME:
				feedEntry.setPageName(element.getStringValue());
				break;
			case ENTRY_REVISION:
				feedEntry.setRevision(Integer.parseInt(element.getStringValue()));
				break;
			default:
				break;
			}	        
		}	
		
		return feedEntry;
	}

	private RSSLink parseLink(Element element) {
		return new RSSLink(
				element.attribute("rel").getStringValue(),
				element.attribute("type").getStringValue(),
				element.attribute("href").getStringValue());
	}

	private RSSEntryCategory parseEntryCategory(Element element) {
		return new RSSEntryCategory(
				element.attribute("scheme").getStringValue(),
				element.attribute("term").getStringValue(),
				element.attribute("label").getStringValue());
	}

	private List<RSSMessage> parseContent(Element element, LocalDateTime date) {
		List<RSSMessage> messages = new ArrayList<>();
		
		Element messagesElement = element
				.element("div")
				.element("table")
				.element("tbody")
				.element("tr")
				.element("td")
				.element("div")
				.elements()
				.get(1);
		
		for (Iterator<Element> it = messagesElement.elementIterator(); it.hasNext();) {
	        Element el = it.next();
	        
	        if(el.element("font") == null) {
	        	 System.err.println("Error in the feed. the Feed is not strucured as ususall : "+el.getStringValue());
	        	 continue;
        	 }
	        
	        String title = el.element("font").getStringValue();
	        
	        switch (title) {
			case "Club lotto":
				messages.add(parseClubLotto(el,date));
				break;
			case "Fixtures":
				messages.addAll(parseFixtures(el,date));
				break;
			default:
				messages.addAll(parseNewsAndResults(el,date));
				break;
			}
	        
	        
		}
		
		return messages;
	}

	private String[] getParagraphsFromElement(Element element) {
		List<String> paragraphs = new ArrayList<>();
		boolean supElFound = false;
		
		for (Iterator<Element> it = element.elementIterator(); it.hasNext();) {
	        Element el = it.next();
	        String rowString = el.getStringValue().replaceAll("\n|\t", " ");
	        String placebo = "";
	        Pattern pattern = Pattern.compile("(pm|am)\\w", Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(rowString);
	        if(matcher.find()) {
	        	placebo = matcher.group();
	        	placebo = placebo.substring(0,placebo.length()-1) + "\n" +placebo.substring(placebo.length()-1);
	        }
	        
	        if(rowString.length() == 0)
	        	continue;
	        
	        if(el.getName().equals("sup") || rowString.charAt(0) == ' ') {
	        	int lastIndex = paragraphs.size()-1;
	        	String lastEl = paragraphs.get(lastIndex);
	        	lastEl = lastEl + rowString;
	        	paragraphs.set(lastIndex, lastEl);
	        }
	        else {
	        	paragraphs.addAll(Arrays.asList(rowString.replaceAll("(pm|am)\\w", placebo).split("\n")));
	        }
	        	        
		}
		
		String[] parArray = new String[paragraphs.size()];
		parArray = paragraphs.toArray(parArray);
		return parArray;
	}

	private RSSLotto parseClubLotto(Element element, LocalDateTime dateTime) {	
		RSSLotto lotto = new RSSLotto();		
		StringBuilder sb = new StringBuilder();
		Pattern lottonumberPattern = Pattern.compile("\\d{1,2}\\,\\s\\d{1,2}\\,\\s\\d{1,2}\\,\\s\\d{1,2}", Pattern.CASE_INSENSITIVE);
		Pattern datePattern = Pattern.compile("\\d{1,2}(nd|rd|th)\\s{1,2}of\\s(January|February|March|April|May|June|July|August|September|October|November|December)", Pattern.CASE_INSENSITIVE); // \\sat\\s\\d{1,2}(\\:\\d{1,2}|)(pm|am)

		
		String[] paragraphs = getParagraphsFromElement(element);
		
		String row = String.join(" ", paragraphs);
		
		Matcher matcher = lottonumberPattern.matcher(row);
		Matcher dateMatcher = datePattern.matcher(row);
		
		if(matcher.find()) {
			String[] numbers = matcher.group().split(",");
			int[] lottoNumbers = new int[4];
			for (int i = 0; i < lottoNumbers.length; i++) {
				lottoNumbers[i] = Integer.parseInt(numbers[i].trim());
			}

			lotto.setDraw(lottoNumbers);
		}
		
		if(dateMatcher.find()) {
			String[] dateArray = dateMatcher
					.group()
					.replaceAll("(nd|rd|th)", "")
					.trim()
					.split("of");
			
			int day = Integer.parseInt(dateArray[0].trim());
			Month month = Month.valueOf(dateArray[1].toUpperCase().trim());				
			lotto.setDrawDate(LocalDate.of(dateTime.getYear(), month, day));
			
		}
		else {
			System.err.println("Wait What????");
			return null;
		}
		
		lotto.setMessage(row);

		return lotto;
	}
	
	private List<RSSFixture> parseFixtures(Element element, LocalDateTime dateTime) {
		int dayOfTheWeek = dateTime.getDayOfWeek().getValue();
		LocalDate date = dateTime.toLocalDate().minusDays(dayOfTheWeek);
		
		Pattern datePatternCurrentWeek = Pattern.compile("(on|)\\s(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)", Pattern.CASE_INSENSITIVE); // \\sa(t|s)\\s\\d{1,2}(\\:\\d{1,2}|)(pm|am)
		Pattern datePattern = Pattern.compile("(January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{1,2}th", Pattern.CASE_INSENSITIVE); // \\sat\\s\\d{1,2}(\\:\\d{1,2}|)(pm|am)
		Pattern divisionPattern = Pattern.compile("^(\\w+\\s){1,4}(at|away|vs)", Pattern.CASE_INSENSITIVE);
		Pattern dayOfWeekPattern = Pattern.compile("(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)", Pattern.CASE_INSENSITIVE);
		Pattern monthOfYearPattern = Pattern.compile("(January|February|March|April|May|June|July|August|September|October|November|December)", Pattern.CASE_INSENSITIVE);
		Pattern timePattern = Pattern.compile("\\d{1,2}(\\:\\d{1,2}|)(pm|am)", Pattern.CASE_INSENSITIVE);
		Pattern dayPattern = Pattern.compile("\\d{1,2}th", Pattern.CASE_INSENSITIVE);
		Pattern opponentPattern = Pattern.compile("(vs.|to)\\s((\\w|\\/|\\’)+\\s){1,2}(on|M|T|W|F|S)", Pattern.CASE_INSENSITIVE);
		Pattern venuePattern = Pattern.compile("in\\s(\\w+\\s){1,2}(in|on|M|T|W|F|S)", Pattern.CASE_INSENSITIVE);
		
		List<RSSFixture> fixtures = new ArrayList<>();
		Queue<String> incompleteQueue = new LinkedList<>();
		String[] paragraphs = getParagraphsFromElement(element);
		for(String row : paragraphs) {
			if(row.equals("Fixtures") || row.length() == 0) 
				continue;
			
			for (String missplaced; (missplaced = incompleteQueue.poll()) != null;)
				row = missplaced + row;
			
			// clean string from special characters
			StringBuilder rowSB = new StringBuilder();
			row.chars().forEach((ch) -> rowSB.append((char) (((int) ch) > 127 ? (int)' ' : (int) ch)));			
			row = rowSB.toString();
			
			RSSFixture fixture = new RSSFixture();
			
			Matcher dateInCurrentWeekMatcher = datePatternCurrentWeek.matcher(row);
			Matcher dateMatcher = datePattern.matcher(row);
			Matcher divisionMatcher = divisionPattern.matcher(row);
			Matcher timeMatcher = timePattern.matcher(row);
			Matcher opponentMatcher = opponentPattern.matcher(row);
			Matcher venueMatcher = venuePattern.matcher(row);
			
			boolean playedHome = row.contains("home");
			boolean venueFound = venueMatcher.find();
			boolean timeFound = timeMatcher.find();
			boolean dayIncurrentWeekFound = dateInCurrentWeekMatcher.find();
			boolean dateFound = dateMatcher.find();
			boolean divisionFound = divisionMatcher.find();
			boolean opponentFound = opponentMatcher.find();
			
			if(!(timeFound && (dayIncurrentWeekFound || dateFound) && divisionFound && opponentFound)) {
				System.err.println("Oh no.. Probably incopmete Row, deployng queue.");
				incompleteQueue.add(row);
				continue;
			}
			
			if(!(timeFound && (dayIncurrentWeekFound || dateFound))){
				System.err.println("Oh no.. No date found !!!.");
			}
			
			String venue = "";
			String dateRegex = "";
			String opponent = "";
			int hours = 0,minutes = 0;
			
			if(venueFound)
				venue = venueMatcher.group();
			
			fixture.setPlayedHome(playedHome);			
			
			if(timeFound) {
				String[] hourArray = timeMatcher.group().split(":");
				
				hours = Integer.parseInt(hourArray[0].replaceAll("(pm|am)", "").trim());
				hours = (timeMatcher.group().contains("pm") && hours != 12) ? hours +12 : hours;
				
				if(hourArray.length == 2)
					minutes = Integer.parseInt(hourArray[1].replaceAll("(pm|am)", "").trim()); 
			}				
			
			if(dayIncurrentWeekFound) {
				dateRegex = dateInCurrentWeekMatcher.group();
				Matcher dayMatcher = dayOfWeekPattern.matcher(dateRegex);
				
				if(!dayMatcher.find() ) 
					System.err.println("Oh Crap dayMatcher failed to match. Program will crash");		

				DayOfWeek dayOfWeek = DayOfWeek.valueOf(dayMatcher.group().toUpperCase());
				
				fixture.setDateTime(date
						.atStartOfDay()
						.plusDays(dayOfWeek.getValue())
						.plusHours(hours)
						.plusMinutes(minutes));
			}
			
			if(dateFound) {
				dateRegex = dateMatcher.group();
				Matcher dMatcher = dayPattern.matcher(dateRegex);
				Matcher monthMatcher = monthOfYearPattern.matcher(dateRegex);
				
				if(!dMatcher.find() && !monthMatcher.find()) {
					System.err.println("Oh Crap dMatcher or monthMatcher failed to match. Program will crash");
				
				int day = Integer.parseInt(dMatcher.group().replaceAll("th", "").trim());
				
				String monthString = monthMatcher.group();
				Month month = Month.valueOf(monthString);				
				fixture.setDateTime(LocalDateTime.of(LocalDate.of(date.getYear(), month, day),LocalTime.of(hours, minutes)));}
				
								
			}
			
			if(divisionFound) {
				String division = divisionMatcher
						.group()
						.replaceAll("(at|away|vs)", "")
						.trim();
				
				fixture.setDivision(division);	
			}
			
			if(opponentFound) {
				opponent = opponentMatcher
						.group()
						.replaceAll("(vs.|to)\\s", "")
						.replaceAll("\\s(on|M|T|W|F|S)", "")
						.trim();
				
				if(playedHome) {
					fixture.setHome(CLUB_NAME);
					fixture.setAway(opponent);
				}
				else{
					fixture.setAway(CLUB_NAME);
					fixture.setHome(opponent);
				}				
			}
			
			if(opponent.length() == 0)
				System.err.println("Opponent is not set, something went wrong");
			
			if(venueFound)
				fixture.setVenue(venue);
			else if(playedHome)
				fixture.setVenue(CLUB_NAME);
			else
				fixture.setVenue(opponent);
			
			fixtures.add(fixture);
		}
		
		return fixtures;
	}	

	private List<RSSMessage> parseNewsAndResults(Element element, LocalDateTime dateTime) {
		List<RSSMessage> list = new ArrayList<>();
		String[] paragraphs = getParagraphsFromElement(element);
		
		try {
			RSSResult result = new RSSResult();
			
			result.setDivision(paragraphs[0]);
			
			String[] homeAndResult = paragraphs[1].split(" ");
			String[] awayAndResult = paragraphs[2].split(" ");
			
			StringBuilder homeSb = new StringBuilder();
			for (int i = 0; i < homeAndResult.length -1; i++)
				homeSb.append(homeAndResult[i]).append(" ");
			
			StringBuilder awaySb = new StringBuilder();
			for (int i = 0; i < awayAndResult.length -1; i++)
				awaySb.append(awayAndResult[i]).append(" ");
					
			result.setHome(homeSb.toString().trim());
			result.setHomeScore(homeAndResult[homeAndResult.length -1]);
			
			result.setAway(awaySb.toString().trim());
			result.setAwayScore(awayAndResult[awayAndResult.length-1]);
			
			
			result.setDateTime(dateTime);
			
			Pattern scorePattern = Pattern.compile("\\d{1,3}-\\d{1,3}", Pattern.CASE_INSENSITIVE);
			Matcher homeMatcher = scorePattern.matcher(homeAndResult[homeAndResult.length -1]);
			Matcher awayMatcher = scorePattern.matcher(awayAndResult[awayAndResult.length -1]);
			
			if(!(homeMatcher.find() || awayMatcher.find()))
				throw new Exception("Not match result.");
			
			list.add(result);
			
			StringBuilder sb = new StringBuilder();
			
			for (int i = 3; i < paragraphs.length; i++)
				sb.append(paragraphs[i]).append("\n");

			RSSNews news = new RSSNews();
			
			news.setTitle(paragraphs[0] + " "+ homeAndResult[0] + " vs. " + awayAndResult[0]);
			news.setNewsMessage(sb.toString());
			
			
			news.setDateTime(dateTime);
			
			list.add(news);
			
			return list;
			
		} catch (Exception e) {
			// special case. small announcement
			RSSNews news = new RSSNews();
			String firstElString = paragraphs[0];
			
			if(firstElString.length() > 50) {
				String allTogether = String.join(" ", paragraphs);
				news.setTitle(allTogether.substring(0, 50) + "...");
				news.setNewsMessage(allTogether);
				
			}
			else {
				StringBuilder sb = new StringBuilder();
				
				for (int i = 1; i < paragraphs.length; i++)
					sb.append(paragraphs[i]).append("\n");
				
				news.setTitle(firstElString);
				news.setNewsMessage(sb.toString());
			}	
			
			news.setDateTime(dateTime);
			
			list.add(news);
			
			return list;
		}
	}

}
