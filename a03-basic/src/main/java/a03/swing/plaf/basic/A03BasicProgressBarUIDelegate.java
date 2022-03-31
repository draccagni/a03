/*
 * Copyright (c) 2003-2008 Davide Raccagni. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  * Neither the name of Davide Raccagni nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package a03.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import javax.swing.JProgressBar;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03ProgressBarUIDelegate;
import a03.swing.plaf.A03SwingUtilities;

public class A03BasicProgressBarUIDelegate implements A03ProgressBarUIDelegate, A03ComponentState {

	private static final DimensionUIResource preferredInnerHorizontal = new DimensionUIResource(146, 16);
	
	private static final DimensionUIResource preferredInnerVertical = new DimensionUIResource(16, 146);
	
	public A03BasicProgressBarUIDelegate() {
	}
	
	public void paintDeterminate(Component c, Graphics g, int amountFull) {
		JProgressBar progressBar = (JProgressBar) c;
		
		Insets insets = progressBar.getInsets();
		int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
		int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);

		if (barRectWidth <= 0 || barRectHeight <= 0) {
			return;
		}

		Image image = A03GraphicsUtilities.createImage(c, barRectWidth, barRectHeight);
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		graphics.translate(insets.left, insets.top);
		
		int state = A03BasicUtilities.getState(c);
		int orientation = progressBar.getOrientation();
		
		Paint backgroundPaint = Color.GRAY;

		int width = c.getWidth();
		int height = c.getHeight();
		
		Shape clip = new Rectangle(0, 0, width - 1, height - 1);
		graphics.setClip(clip);
		
		graphics.setPaint(backgroundPaint);
		graphics.fillRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);

		if (amountFull > 0) {
			if (orientation == JProgressBar.HORIZONTAL) {
				graphics.setPaint(backgroundPaint);
				if (A03SwingUtilities.isLeftToRight(progressBar)) {
					graphics.fillRect(0, 0, amountFull, barRectHeight);
				} else {
					graphics.fillRect(barRectWidth - amountFull, 0, barRectWidth, barRectHeight);
				}
	
				graphics.setPaint(Color.BLACK);
				if (A03SwingUtilities.isLeftToRight(progressBar)) {
					graphics.fillRect(0, 0, amountFull, barRectHeight);
				} else {
					graphics.fillRect(barRectWidth - amountFull, 0, barRectWidth, barRectHeight);
				}
			} else {
				graphics.setPaint(Color.BLACK);
				graphics.fillRect(0, barRectHeight - amountFull, barRectWidth, barRectHeight);
			}
		}

		graphics.dispose();
		
		g.drawImage(image, 0, 0, c);
	}
	
	public void paintIndeterminate(Component c, Graphics g, int animationIndex) {
		JProgressBar progressBar = (JProgressBar) c;
		
		Insets insets = progressBar.getInsets();
		int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
		int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);

		if (barRectWidth <= 0 || barRectHeight <= 0) {
			return;
		}

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.translate(insets.left, insets.top);
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape clip = graphics.getClip().getBounds().createIntersection(new Rectangle(0, 0, barRectWidth, barRectHeight));
		graphics.setClip(clip);
		
		boolean leftToRight = A03SwingUtilities.isLeftToRight(c);

		int orientation = progressBar.getOrientation();
		
		int state = A03BasicUtilities.getState(c);
		Paint backgroundPaint = Color.GRAY;

		Image tempImage = A03GraphicsUtilities.createImage(c, barRectWidth, barRectHeight);
		Graphics2D graphics2 = (Graphics2D) tempImage.getGraphics();

		Shape clip2 = new Rectangle(0, 0, barRectWidth - 1, barRectHeight - 1);
		graphics2.setClip(clip2);
		
		if (orientation == JProgressBar.HORIZONTAL) {
			// Pattern
			int w = 2 * barRectHeight;

			Image tempImage2 = A03GraphicsUtilities.createImage(c, w, barRectHeight);
			
			Graphics2D g2d = (Graphics2D) tempImage2.getGraphics();
			
			int halfW = w / 2;
			
			GeneralPath path = new GeneralPath();
			path.moveTo(halfW, 0);
			path.lineTo(w, 0);
			path.lineTo(halfW, barRectHeight);
			path.lineTo(0, barRectHeight);
			path.closePath();
			
			g2d.setPaint(backgroundPaint);
			g2d.fillRect(0, 0, w, barRectHeight);
			g2d.setPaint(Color.BLACK);
			g2d.fill(path);
			
			g2d.dispose();

			// Animation
			int x = leftToRight ? -w : barRectWidth;
			int t = animationIndex % w;
			int n = barRectWidth / w;
			
			for (int i = 0; i <= n + 1; i++) {
				int xt = x + (leftToRight ? t : -t);
				
				graphics2.drawImage(tempImage2, xt, 0, c);
				
				x += leftToRight ? w : -w;
			}
		} else {
			// Pattern
			int h = 2 * barRectWidth;
			
			Image tempImage2 = A03GraphicsUtilities.createImage(c, barRectWidth, h);
			
			Graphics2D g2d = (Graphics2D) tempImage2.getGraphics();
			
			int halfH = h / 2;

			GeneralPath path = new GeneralPath();
			path.moveTo(0, 0);
			path.lineTo(barRectWidth, halfH);
			path.lineTo(barRectWidth, h);
			path.lineTo(0, halfH);
			path.closePath();
			
			g2d.setPaint(backgroundPaint);
			g2d.fillRect(0, 0, barRectWidth, h);
			g2d.setPaint(Color.BLACK);
			g2d.fill(path);
			
			g2d.dispose();

			// Animation
			int y = 0;
			int t = animationIndex % h;
			int n = barRectHeight / h;
			
			for (int i = 0; i <= n + 1; i++) {
				int yt = barRectHeight + y - t;
				
				graphics2.drawImage(tempImage2, 0, yt, c);
				
				y -= h;
			}		
		}
		
		graphics2.dispose();

		graphics.drawImage(tempImage, insets.left, insets.top, c);
		
		graphics.dispose();
	}

	public void paintText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g;

		int state = A03BasicUtilities.getState(c);
		
		Color foregroundColor = Color.RED;
		
		graphics.setColor(foregroundColor);
		graphics.drawString(text, x, y);
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 0;
		insets.left = 0;
		insets.bottom = 0;
		insets.right = 0;

		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D graphics = (Graphics2D) g; //.create();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		JProgressBar progressBar = (JProgressBar) c;
		
		int state = A03BasicUtilities.getState(c);
		
		int orientation = progressBar.getOrientation();
		Paint border = Color.BLACK;			
		graphics.setPaint(border);

		Shape shape = new Rectangle(x, y, width - 1, height - 1);
		graphics.draw(shape);
		
		//graphics.dispose();
	}

	public DimensionUIResource getPreferredInnerHorizontal() {
			return preferredInnerHorizontal;
		}
	
	public DimensionUIResource getPreferredInnerVertical() {
		return preferredInnerVertical;
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.RED);
	}
}
