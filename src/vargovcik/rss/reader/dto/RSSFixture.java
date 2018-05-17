package vargovcik.rss.reader.dto;

import java.time.LocalDateTime;

public class RSSFixture extends RSSMessage {
	private String venue, division, home, away;
	private boolean playedHome;
	private LocalDateTime dateTime;
	
	
	public String getVenue() {
		return venue;
	}


	public void setVenue(String venue) {
		this.venue = venue;
	}


	public String getDivision() {
		return division;
	}


	public void setDivision(String division) {
		this.division = division;
	}


	public boolean isPlayedHome() {
		return playedHome;
	}


	public void setPlayedHome(boolean playedHome) {
		this.playedHome = playedHome;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getHome() {
		return home;
	}


	public void setHome(String home) {
		this.home = home;
	}


	public String getAway() {
		return away;
	}


	public void setAway(String away) {
		this.away = away;
	}


	@Override
	public String toString() {
		return "RSSFixture [venue=" + venue + ", division=" + division + ", home=" + home + ", away=" + away
				+ ", playedHome=" + playedHome + ", dateTime=" + dateTime + "]";
	}


	@Override
	public MessageType getMessageType() {
		return MessageType.FIXTURE;
	}

}
