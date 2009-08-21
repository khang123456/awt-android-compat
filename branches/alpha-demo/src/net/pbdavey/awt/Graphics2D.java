package net.pbdavey.awt;


import net.pbdavey.awt.RenderingHints.Key;
import and.awt.BasicStroke;
import and.awt.Color;
import and.awt.Shape;
import and.awt.Stroke;
import and.awt.geom.PathIterator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
/**
 * So far it appears that Graphics2D is roughly equivalent to a Canvas with Paint.
 * The Paint object contains information regarding Fonts and FontMetrics, while
 * the Canvas is a more raw drawing tool.
 * @author pbdavey
 *
 */
public class Graphics2D {
	Color color = Color.white;
	Paint paint = new Paint();
	Font font = new Font();
	Stroke stroke;
	Canvas canvas;
	
	public Graphics2D(Canvas canvas) {
		this.canvas = canvas;
	}
	
	public void setPaint(Color color) {
		this.color = color;
		paint.setColor(this.color.getRGB());
	}
	
	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
		
		Paint.Cap cap = Paint.Cap.BUTT;
		switch(stroke.getEndCap()) {
		case BasicStroke.CAP_BUTT:
			cap = Paint.Cap.BUTT;
			break;
		case BasicStroke.CAP_ROUND:
			cap = Paint.Cap.ROUND;
			break;
		case BasicStroke.CAP_SQUARE:
			cap = Paint.Cap.SQUARE;
			break;
		}
		this.paint.setStrokeCap(cap);

		Paint.Join join = Paint.Join.BEVEL;
		switch(stroke.getLineJoin()) {
		case BasicStroke.JOIN_BEVEL:
			join = Paint.Join.BEVEL;
			break;
		case BasicStroke.JOIN_MITER:
			join = Paint.Join.MITER;
			break;
		case BasicStroke.JOIN_ROUND:
			join = Paint.Join.ROUND;
			break;
		}
		this.paint.setStrokeJoin(join);
		
		this.paint.setStrokeMiter(stroke.getMiterLimit());
		this.paint.setStrokeWidth(stroke.getLineWidth());
	}

	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		// TODO - raised is meaningless here.
		canvas.drawRect(x, y, x+width, y+height, paint);		
	}

	public void setRenderingHint(Key renderingHintKey,
			RenderingHints renderingHint) {
		// TODO Auto-generated method stub
		if (renderingHintKey == RenderingHints.KEY_ANTIALIASING) {
			if (renderingHint == RenderingHints.VALUE_ANTIALIAS_ON)
				paint.setAntiAlias(true);
			else
				paint.setAntiAlias(false);
		}
	}

	public Font getFont() {
		return this.font;
	}
	
	public FontMetrics getFontMetrics() {
		return new FontMetrics(this.paint);
	}

	public void setFont(Font font) {
		paint.setTypeface(font.typeFace);
		this.font = font;
	}

	public void drawString(String string, int x, int y) {
		paint.setStyle(Style.STROKE);
		canvas.drawText(string, x, y, paint);
	}

	public void draw(Shape s) {
		PathIterator pi = s.getPathIterator(null);
		Path path = convertAwtPathToAndroid(pi);
		// Draw the outline, don't fill
		paint.setStyle(Style.STROKE);
		canvas.drawPath(path, paint);
	}
	
	public void fill(Shape s) {
		PathIterator pi = s.getPathIterator(null);
		Path path = convertAwtPathToAndroid(pi);
		// Draw the outline and fill
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawPath(path, paint);
	}

	private Path convertAwtPathToAndroid(PathIterator pi) {
		Path path = new Path();
		float [] coords = new float [6];
		while (!pi.isDone()) {
			int windingRule = pi.getWindingRule();
			
			if (windingRule == PathIterator.WIND_EVEN_ODD) {
				path.setFillType(Path.FillType.EVEN_ODD);
			}
			else {
				path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
			}
			
			int pathType = pi.currentSegment(coords);

			switch (pathType) {
			case PathIterator.SEG_CLOSE:
				path.close();
				break;
			case PathIterator.SEG_CUBICTO:
				path.cubicTo(coords [0], coords [1], coords [2], coords [3], coords [4], coords [5]);
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(coords [0], coords [1]);
				break;
			case PathIterator.SEG_MOVETO:
				path.moveTo(coords [0], coords [1]);
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(coords [0], coords [1], coords [2], coords [3]);
				break;
			}
			
			pi.next();
		}		
		return path;
	}
}
