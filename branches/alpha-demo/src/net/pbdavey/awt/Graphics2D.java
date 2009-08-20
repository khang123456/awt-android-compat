package net.pbdavey.awt;


import android.graphics.Canvas;
import android.graphics.Paint;
import and.awt.Shape;
import and.awt.Stroke;
import net.pbdavey.awt.RenderingHints.Key;
import and.awt.BasicStroke;
import and.awt.geom.Arc2D;
import and.awt.geom.Ellipse2D;
import and.awt.geom.GeneralPath;
import and.awt.geom.Line2D;
import and.awt.geom.Path2D;
import and.awt.geom.PathIterator;
import and.awt.geom.Rectangle2D;
import and.awt.geom.RoundRectangle2D;
import and.awt.geom.RoundRectangle2D.Double;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.shapes.OvalShape;
/**
 * So far it appears that Graphics2D is rougly equivalent to a Canvas with Paint.
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
		paint.setColor(this.color.getInt());
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
/*
	public void draw(Line2D line) {
		paint.setStyle(Style.STROKE);
		canvas.drawLine((float)line.getX1(), (float)line.getY1(), (float)line.getX2(), (float)line.getY2(), paint);
	}

	public void draw(Rectangle2D rectangle) {
		paint.setStyle(Style.STROKE);
		canvas.drawRect((float)rectangle.getMinX(), (float)rectangle.getMinY(), (float)rectangle.getMaxX(), (float)rectangle.getMaxY(), paint);
	}

	public void draw(RoundRectangle2D rectangle) {
		paint.setStyle(Style.STROKE);
		canvas.drawRoundRect(new RectF((float)rectangle.getMinX(), (float)rectangle.getMinY(), (float)rectangle.getMaxX(), (float)rectangle.getMaxY()),
				(float)rectangle.getArcWidth(), (float)rectangle.getArcHeight(), paint);	
	}

	public void draw(Arc2D arc) {
		paint.setStyle(Style.STROKE);
		RectF rect = new RectF((float)arc.getMinX(), (float)arc.getMinY(), (float)arc.getMaxX(), (float)arc.getMaxY());
		canvas.drawArc(rect, (float)arc.getAngleStart(), (float)arc.getAngleExtent(), false, paint);
	}

	public void draw(Ellipse2D ellipse) {
		paint.setStyle(Style.STROKE);
		RectF rect = new RectF((float)ellipse.getMinX(), (float)ellipse.getMinY(), (float)ellipse.getMaxX(), (float)ellipse.getMaxY());
		canvas.drawOval(rect, paint);
	}

	public void draw(Path2D.Float path) {
		paint.setStyle(Style.STROKE);
		
		Path andPath = new Path();
		//TODO Transfer and.awt.geom.Path into android.graphics.Path
		
	}
	*/
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
	/*
	public void fill(Rectangle2D rectangle) {
		paint.setStyle(Style.FILL);
		canvas.drawRect((float)rectangle.getMinX(), (float)rectangle.getMinY(), (float)rectangle.getMaxX(), (float)rectangle.getMaxY(), paint);
	}

	public void fill(RoundRectangle2D rectangle) {
		paint.setStyle(Style.FILL);
		canvas.drawRoundRect(new RectF((float)rectangle.getMinX(), (float)rectangle.getMinY(), (float)rectangle.getMaxX(), (float)rectangle.getMaxY()),
				(float)rectangle.getArcWidth(), (float)rectangle.getArcHeight(), paint);	
	}

	public void fill(Arc2D arc) {
		paint.setStyle(Style.FILL);
		RectF rect = new RectF((float)arc.getMinX(), (float)arc.getMinY(), (float)arc.getMaxX(), (float)arc.getMaxY());
		canvas.drawArc(rect, (float)arc.getAngleStart(), (float)arc.getAngleExtent(), false, paint);
	}

	public void fill(Ellipse2D ellipse) {
		paint.setStyle(Style.FILL);
		RectF rect = new RectF((float)ellipse.getMinX(), (float)ellipse.getMinY(), (float)ellipse.getMaxX(), (float)ellipse.getMaxY());
		canvas.drawOval(rect, paint);
	}

	public void fill(Path2D.Float path) {
		paint.setStyle(Style.FILL);
		// TODO Auto-generated method stub
	}
	*/
	
}
