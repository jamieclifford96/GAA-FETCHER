package vargovcik.rss.reader.dto;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RSSFeedEntry {
	
	private String id, title, authorName, authorEmail, pageName;
	private int revision;
	private RSSEntryCategory category;
	private Instant published, updated, edited;
    private List<RSSMessage> entrys = new ArrayList<RSSMessage>();
    final List<RSSLink> links = new ArrayList<RSSLink>();
    
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public RSSEntryCategory getCategory() {
		return category;
	}
	
	public void setCategory(RSSEntryCategory category) {
		this.category = category;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Instant getPublished() {
		return published;
	}
	
	public void setPublished(Instant published) {
		this.published = published;
	}
	
	public Instant getUpdated() {
		return updated;
	}
	
	public void setUpdated(Instant updated) {
		this.updated = updated;
	}
	
	public Instant getEdited() {
		return edited;
	}
	
	public void setEdited(Instant edited) {
		this.edited = edited;
	}
	
	public List<RSSMessage> getEntrys() {
		return entrys;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
	

	public void setEntrys(List<RSSMessage> entrys) {
		this.entrys = entrys;
	}

	public List<RSSLink> getLinks() {
		return links;
	}

	@Override
	public String toString() {
		return "RSSFeedEntry [id=" + id + ", title=" + title + ", authorName=" + authorName + ", authorEmail="
				+ authorEmail + ", pageName=" + pageName + ", revision=" + revision + ", category=" + category
				+ ", published=" + published + ", updated=" + updated + ", edited=" + edited + ", entrys=" + entrys
				+ ", links=" + links + "]";
	}

	
	
	

}
