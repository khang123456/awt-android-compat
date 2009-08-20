package net.pbdavey.awt;

/**
 * This class defines a bridge between android.graphics.Color and java.awt.Color
 * Create an object of this type and all manipulations should adhere to awt.
 * @author pbdavey
 *
 */
public class Color {
	public static final Color lightGray = new Color(0xcc,0xcc,0xcc);
	public static final Color darkGray = new Color(0x44,0x44,0x44);
	public static final Color magenta = new Color(0xff,0x0,0xff);
	public static final Color yellow = new Color(0xff,0xff,0x0);
	
	public static final Color gray = new Color(0x88,0x88,0x88);
	public static final Color white = new Color(0xff,0xff,0xff);
	public static final Color black = new Color(0x0,0x0,0x0);
	public static final Color red = new Color(0xff,0x0,0x0);
	public static final Color green = new Color(0x0,0xff,0x0);
	public static final Color blue = new Color(0x0,0x0,0xff);
	
	int color = 0;
	public Color(int r, int g, int b) {
		int alpha = 255;
		this.color = (alpha << 24) | (r << 16) | (g << 8) | b;
	}
	
	public int getInt() {
		return this.color;
	}
}
