package net.pbdavey.awt;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


public abstract class AwtView extends View {
	public AwtView(Context context) {
		super(context);
	}

	public AwtView(Context context, AttributeSet attribSet) {
		super(context, attribSet);
	}

	public void setBackground(Color bgColor) {
		this.setBackgroundColor(bgColor.getInt());
	}
	
	public Dimension getSize() {
		return new Dimension(this.getWidth(), this.getHeight());
	}
	
	@Override
	protected final void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Graphics2D g2 = new Graphics2D(canvas);
		paint(g2);
	}
	
	protected abstract void paint(Graphics2D g2); 
}
