package vargovcik.rss.reader.dto;

public class RSSLink {
	String rel, type, href;
	

	public RSSLink(String rel, String type, String href) {
		super();
		this.rel = rel;
		this.type = type;
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "RSSLink [rel=" + rel + ", type=" + type + ", href=" + href + "]";
	}
	

}
