package ca.albertlockett.atlanticchip.util;

public class MultiLineStringBuilder {

	private StringBuilder stringBuilder;
	
	public MultiLineStringBuilder() {
		this.stringBuilder = new StringBuilder();
	}
	
	public MultiLineStringBuilder append(String s) {
		this.stringBuilder.append(s);
		return this;
	}
	
	public MultiLineStringBuilder addLine(String line) {
		this.stringBuilder.append(line).append("\n");
		return this;
	}
	
	public String toString() {
		return this.stringBuilder.toString();
	}
}
