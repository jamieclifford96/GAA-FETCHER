package vargovcik.rss.reader.dto;

import java.time.LocalDateTime;

public class RSSNews extends RSSMessage {
	String title, newsMessage;
	private LocalDateTime dateTime;
	
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getNewsMessage() {
		return newsMessage;
	}


	public void setNewsMessage(String newsMessage) {
		this.newsMessage = newsMessage;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}


	@Override
	public String toString() {
		return "RSSNews [title=" + title + ", newsMessage=" + newsMessage + ", dateTime=" + dateTime + "]";
	}


	@Override
	public MessageType getMessageType() {
		return MessageType.NEWS;
	}

}
