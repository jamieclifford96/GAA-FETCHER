package vargovcik.rss.reader.dto;

public abstract class RSSMessage {
	
	public enum MessageType {
	    LOTTO, RESULT, FIXTURE, NEWS
	}
	
	public abstract MessageType getMessageType();

}
