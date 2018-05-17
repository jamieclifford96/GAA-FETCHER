package vargovcik.rss.reader.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RSSFeed {
	private String id, title, generator;
	private int startIndex;
	private Instant updated;
    final List<RSSLink> links = new ArrayList<RSSLink>();
    final List<RSSFeedEntry> entries = new ArrayList<RSSFeedEntry>();
    
    public RSSFeed() {
    	
    }
    
	public RSSFeed(String id, String title, Instant updated) {
		super();
		this.id = id;
		this.title = title;
		this.updated = updated;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Instant getUpdated() {
		return updated;
	}

	public List<RSSLink> getLinks() {
		return links;
	}

	public List<RSSFeedEntry> getEntries() {
		return entries;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUpdated(Instant updated) {
		this.updated = updated;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public String toString() {
		return "Feed [id=" + id + ", title=" + title + ", updated=" + updated + ", generator=" + generator
				+ ", startIndex=" + startIndex + ", links=" + links + ", entries=" + entries + "]";
	}  

}
