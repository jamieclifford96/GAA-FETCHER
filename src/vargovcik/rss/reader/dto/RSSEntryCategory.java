package vargovcik.rss.reader.dto;

public class RSSEntryCategory {
	private String scheme, term, label;

	public RSSEntryCategory(String scheme, String term, String label) {
		super();
		this.scheme = scheme;
		this.term = term;
		this.label = label;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "RSSEntryCategory [scheme=" + scheme + ", term=" + term + ", label=" + label + "]";
	}
	

}
