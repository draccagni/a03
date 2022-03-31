package a03.swing.plaf.aphrodite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;

import a03.swing.plaf.A03GraphicsUtilities;


public class A03AphroditeGraphicsUtilities {

	public static void paintHVDepressedLine(Graphics2D graphics, int x1, int y1, int x2,
			int y2, int depressionLength, Color background, Color foreground) {
		if (x1 == x2) {
			Paint paint = new GradientPaint(x1, y1, background, x1, y1
					+ depressionLength, foreground);
			graphics.setPaint(paint);
			graphics.drawLine(x1, y1, x1, y1 + depressionLength);
	
			graphics.setPaint(foreground);
			graphics.drawLine(x1, y1 + depressionLength, x1, y2 - depressionLength);
	
			paint = new GradientPaint(x1, y2 - depressionLength,
					foreground, x1, y2, background);
			graphics.setPaint(paint);
			graphics.drawLine(x1, y2 - depressionLength, x1, y2);
		} else {
			Paint paint = new GradientPaint(x1, y1, background, x1
					+ depressionLength, y1, foreground);
			graphics.setPaint(paint);
			graphics.drawLine(x1, y1, x1 + depressionLength, y1);
	
			graphics.setPaint(foreground);
			graphics.drawLine(x1 + depressionLength, y1, x2 - depressionLength, y1);
	
			paint = new GradientPaint(x2 - depressionLength, y1,
					foreground, x2, y1, background);
			graphics.setPaint(paint);
			graphics.drawLine(x2 - depressionLength, y1, x2, y1);
		}
	}

	public static void paintBorderShadow(Graphics2D g2, int shadowWidth, Shape clipShape, Color c1, Color c2) {
		Graphics2D graphics = (Graphics2D) g2.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                        RenderingHints.VALUE_ANTIALIAS_ON);
	    int sw = shadowWidth*2;
	    for (int i=sw; i >= 2; i-=2) {
	        float pct = (float)(sw - i) / (sw - 1);
	        graphics.setColor(A03AphroditeGraphicsUtilities.getMixedColor(c1, pct,
	                                  c2, 1.0f-pct));
	        graphics.setStroke(new BasicStroke(i));
	        graphics.draw(clipShape);
	    }
	    
	    graphics.dispose();
	}

	public static Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
	    float[] clr1 = c1.getComponents(null);
	    float[] clr2 = c2.getComponents(null);
	    for (int i = 0; i < clr1.length; i++) {
	        clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
	    }
	    return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
	}

	public static void paintShadowedBorder(Component c, Graphics g, int x, int y, int width, int height, Color background) {
		Graphics2D graphics = (Graphics2D) g; //.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	
		Color top = A03GraphicsUtilities.createColor(background, 0.6);
		Color hidden = A03GraphicsUtilities.createColor(background, 0.8);
		Color side = A03GraphicsUtilities.createColor(background, 0.7);
		Color bottom = A03GraphicsUtilities.createColor(background, 0.85);
		
		Paint paint = new GradientPaint(x, y, top, 
				x, y + 3, side);
		graphics.setPaint(paint);
		graphics.drawRect(x, y, width - 1, height - 1);
		
		paint = new GradientPaint(x + 1, y + 1, hidden, x + 1, y + 2, background);
		graphics.setPaint(paint);
	
		graphics.drawLine(x + 1, y + 1, x + width - 2, y + 1);
		graphics.drawLine(x + 1, y + 2, x + width - 2, y + 2);
		
		graphics.setColor(bottom);
		graphics.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1);
		
		//graphics.dispose();
	}

}
