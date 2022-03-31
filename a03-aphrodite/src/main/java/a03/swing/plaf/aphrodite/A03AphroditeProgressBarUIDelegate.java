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
package a03.swing.plaf.aphrodite;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
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

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plaf.basic.A03BasicProgressBarUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeProgressBarUIDelegate extends A03BasicProgressBarUIDelegate implements A03AphroditePaints {

	public A03AphroditeProgressBarUIDelegate() {
	}
	
	public void paintDeterminate(Component c, Graphics g, int amountFull) {
		JProgressBar progressBar = (JProgressBar) c;
		
		Insets insets = progressBar.getInsets();
		int width = progressBar.getWidth() - insets.left;
		int height = progressBar.getHeight() - insets.top;

		if (width <= 0 || height <= 0) {
			return;
		}

		Image image = A03GraphicsUtilities.createImage(c, width, height);
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		graphics.translate(insets.left, insets.top);
		
		int state = A03BasicUtilities.getState(c);
		int orientation = progressBar.getOrientation();
		
		Paint backgroundPaint = getBackgroundPaint(
				state, 
				orientation, 
				0, 0, width, height);

		Paint determinateBackgroundPaint = getAmountBackgroundPaint(
				state, 
				orientation, 
				0, 0, width, height);
		
		Shape clip = A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 2);
		graphics.setClip(clip);
		
		graphics.setPaint(backgroundPaint);
		graphics.fillRect(0, 0, width, height);

		if (amountFull > 0) {
			if (orientation == JProgressBar.HORIZONTAL) {
				graphics.setPaint(backgroundPaint);
				if (A03SwingUtilities.isLeftToRight(progressBar)) {
					graphics.fillRect(0, 0, amountFull, height);
				} else {
					graphics.fillRect(width - amountFull, 0, width, height);
				}
	
				graphics.setPaint(determinateBackgroundPaint);
				if (A03SwingUtilities.isLeftToRight(progressBar)) {
					graphics.fillRect(0, 0, amountFull, height);
				} else {
					graphics.fillRect(width - amountFull, 0, width, height);
				}
			} else {
				graphics.setPaint(determinateBackgroundPaint);
				graphics.fillRect(0, height - amountFull - insets.top, width, height);
			}
		}

		graphics.dispose();
		
		g.drawImage(image, 0, 0, c);
	}

	public void paintIndeterminate(Component c, Graphics g, int animationIndex) {
		JProgressBar progressBar = (JProgressBar) c;
		
		Insets insets = progressBar.getInsets();
		int width = progressBar.getWidth() - insets.left;
		int height = progressBar.getHeight() - insets.top;

		if (width <= 0 || height <= 0) {
			return;
		}

		Graphics2D graphics = (Graphics2D) g.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Shape clip = graphics.getClip().getBounds().createIntersection(new Rectangle(0, 0, width - 1, height - 1));
		graphics.setClip(clip);
		
		boolean leftToRight = A03SwingUtilities.isLeftToRight(c);

		int orientation = progressBar.getOrientation();
		
		int state = A03BasicUtilities.getState(c);
		Paint backgroundPaint = getBackgroundPaint(
				state, 
				orientation, 
				0, 0, width, height);

		Image tempImage = A03GraphicsUtilities.createImage(c, width - 1, height - 1);
		Graphics2D graphics2 = (Graphics2D) tempImage.getGraphics();

		Shape clip2 = A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 2);
		graphics2.setClip(clip2);
		
		Paint paint = getAmountBackgroundPaint(
				state, 
				orientation, 
				0, 0, width, height);
		
		if (orientation == JProgressBar.HORIZONTAL) {
			// Pattern
			int w = 2 * height;

			Image tempImage2 = A03GraphicsUtilities.createImage(c, w, height);
			
			Graphics2D g2d = (Graphics2D) tempImage2.getGraphics();
			
			int halfW = w / 2;
			
			GeneralPath path = new GeneralPath();
			path.moveTo(halfW, 0);
			path.lineTo(w, 0);
			path.lineTo(halfW, height);
			path.lineTo(0, height);
			path.closePath();
			
			g2d.setPaint(backgroundPaint);
			g2d.fillRect(0, 0, w, height);
			g2d.setPaint(paint);
			g2d.fill(path);
			
			g2d.dispose();

			// Animation
			int x = leftToRight ? -w : width;
			int t = animationIndex % w;
			int n = width / w;
			
			for (int i = 0; i <= n + 1; i++) {
				int xt = x + (leftToRight ? t : -t);
				
				graphics2.drawImage(tempImage2, xt, 0, c);
				
				x += leftToRight ? w : -w;
			}
		} else {
			// Pattern
			int h = 2 * width;
			
			Image tempImage2 = A03GraphicsUtilities.createImage(c, width, h);
			
			Graphics2D g2d = (Graphics2D) tempImage2.getGraphics();
			
			int halfH = h / 2;

			GeneralPath path = new GeneralPath();
			path.moveTo(0, 0);
			path.lineTo(width, halfH);
			path.lineTo(width, h);
			path.lineTo(0, halfH);
			path.closePath();
			
			g2d.setPaint(backgroundPaint);
			g2d.fillRect(0, 0, width, h);
			g2d.setPaint(paint);
			g2d.fill(path);
			
			g2d.dispose();

			// Animation
			int y = 0;
			int t = animationIndex % h;
			int n = height / h;
			
			for (int i = 0; i <= n + 1; i++) {
				int yt = height + y - t;
				
				graphics2.drawImage(tempImage2, 0, yt, c);
				
				y -= h;
			}		
		}
		
		graphics2.dispose();

		graphics.drawImage(tempImage, 0, 0, c); // insets.left, insets.top, c);		
		graphics.dispose();
	}

	public void paintText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g;

		int state = A03BasicUtilities.getState(c);
		
		graphics.setColor(getShadowForegroundColor(state));
		graphics.drawString(text, x, y + 1);

		graphics.setColor(getForegroundColor(state));
		graphics.drawString(text, x, y);

		graphics.dispose();
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 2;
		insets.left = 2;
		insets.bottom = 2;
		insets.right = 2;

		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		JProgressBar progressBar = (JProgressBar) c;
		
		int state = A03BasicUtilities.getState(c);
		
		int orientation = progressBar.getOrientation();
		graphics.setPaint(getBorderPaint(state, orientation, 0, 0, width, height));

		graphics.setPaint(getBorderPaint(state, orientation, 1, 1, width - 2, height - 2));
		Shape shape = A03GraphicsUtilities.createRoundRectangle(1, 1, width - 3, height - 3, 2);
		graphics.draw(shape);
		graphics.setPaint(getOuterBorderPaint(state, orientation, 1, 1, width - 2, height - 2));
		Shape outerShape = A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 2);
		graphics.draw(outerShape);
		
		//graphics.dispose();
	}

	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}

	protected Color getShadowForegroundColor(int state)  {
		if ((state & ENABLED) != 0 ) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				return BUTTON_SHADOW_FOREGROUND_ENABLED_ARMED_COLOR;
			} else {
				return BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return BUTTON_SHADOW_FOREGROUND_DISABLED_ARMED_COLOR;
			} else {
				return BUTTON_SHADOW_FOREGROUND_DISABLED_COLOR;
			}
		}
	}

	public Color getForegroundColor(int state) {
		if ((state & ENABLED) != 0 ) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				return BUTTON_FOREGROUND_ENABLED_ARMED_COLOR;
			} else {
				return BUTTON_FOREGROUND_ENABLED_COLOR;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return BUTTON_FOREGROUND_DISABLED_ARMED_COLOR;
			} else {
				return BUTTON_FOREGROUND_DISABLED_COLOR;
			}
		}
	}

	protected Paint getOuterBorderPaint(int state, int orientation, int x, int y, int width, int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				colors = BUTTON_OUTER_BORDER_ENABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_OUTER_BORDER_ENABLED_COLORS;
			}
		} else {
			if ((state & SELECTED) != 0) {
				colors = BUTTON_OUTER_BORDER_DISABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_OUTER_BORDER_DISABLED_COLORS;
			}
		}
		
//		if (orientation == JProgressBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
//		} else {
//			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
//		}
	}

	protected Paint getBorderPaint(int state, int orientation, int x, int y, int width, int height) {
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				return BUTTON_BORDER_ENABLED_ARMED_COLOR;
			} else {
				return BUTTON_BORDER_ENABLED_COLOR;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return BUTTON_BORDER_DISABLED_ARMED_COLOR;
			} else {
				return BUTTON_BORDER_DISABLED_COLOR;
			}
		}
	}

	public Paint getBackgroundPaint(int state, int orientation, int x, int y,
			int width, int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED)) != 0) {
				colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_BACKGROUND_ENABLED_COLORS;
			}
		} else {
			if ((state & SELECTED) != 0) {
				colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
			}
		}
		
		
		if (orientation == JProgressBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}

	public Paint getAmountBackgroundPaint(int state, int orientation,
			int x, int y, int width, int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			colors = PROGRESS_BAR_AMOUNT_ENABLED_BACKGROUND;
		} else {
			colors = PROGRESS_BAR_AMOUNT_DISABLED_BACKGROUND;
		}
		
		if (orientation == JProgressBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}
}
