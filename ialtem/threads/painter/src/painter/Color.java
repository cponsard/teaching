package painter;

public enum Color {
	
	none (0), red(1), green(2), blue (3);
	
	private final int index;

	private Color(int c) {
		index=c;
	}
	
	public int getIndex() {
		return index;
	}
}
