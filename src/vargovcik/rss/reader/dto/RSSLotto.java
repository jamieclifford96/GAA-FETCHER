package vargovcik.rss.reader.dto;

import java.time.LocalDate;
import java.util.Arrays;

public class RSSLotto extends RSSMessage {
	private String message;
	private int[] draw = new int[4];	
	private LocalDate drawDate;

	public LocalDate getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(LocalDate drawDate) {
		this.drawDate = drawDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int[] getDraw() {
		return draw;
	}
	

	public void setDraw(int[] draw) {
		this.draw = draw;
	}

	@Override
	public String toString() {
		return "RSSLotto [message=" + message + ", draw=" + Arrays.toString(draw) + ", drawDate=" + drawDate + "]";
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.LOTTO;
	}

}
