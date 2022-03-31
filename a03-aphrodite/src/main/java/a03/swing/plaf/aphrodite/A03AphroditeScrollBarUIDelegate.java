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
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicGraphicsUtilities;
import a03.swing.plaf.basic.A03BasicScrollBarUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;
import a03.swing.plaf.basic.A03ComponentState;

public class A03AphroditeScrollBarUIDelegate extends A03BasicScrollBarUIDelegate implements A03AphroditePaints {

	private static final DimensionUIResource minimumThumbSize = new DimensionUIResource(22, 22);

	public A03AphroditeScrollBarUIDelegate() {
	}
	
	public void paintBackground(Component c, Graphics g) {
		// do nothing
	}

	public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		JScrollBar scrollbar = (JScrollBar) c;

		int x = thumbBounds.x + 1;
		int y = thumbBounds.y + 1;
		int width = thumbBounds.width - 3;
		int height = thumbBounds.height - 3;
		
		int state = A03BasicUtilities.getState(c);
		int orientation = scrollbar.getOrientation();
		
		int radius;
		if (orientation == JScrollBar.HORIZONTAL) {
        	radius = height / 2;
        } else {
        	radius = width / 2;
        }
		
		Shape shape = A03GraphicsUtilities.createRoundRectangle(0, 0, width, height, radius); 
        Paint backgroundPaint = getThumbBackgroundPaint(state, orientation, 0, 0, width, height); 
		Paint borderPaint = getThumbBorderPaint(state, orientation, 0, 0, width, height);

		Graphics2D graphics = (Graphics2D) g.create(x, y, width + 1, height + 1);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setPaint(backgroundPaint);
        graphics.fill(shape);
        
		graphics.setPaint(borderPaint);
        graphics.draw(shape);
        graphics.dispose();
	}

	public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		JScrollBar scrollbar = (JScrollBar) c;

		Graphics2D graphics = (Graphics2D) g.create();
		
		int width = c.getWidth();
		int height = c.getHeight();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		int state = A03BasicUtilities.getState(c);
		int orientation = scrollbar.getOrientation();
		
		int radius;
		if (orientation == JScrollBar.HORIZONTAL) {
        	radius = height / 2;
        } else {
        	radius = width / 2;
        }
		
		Paint scrollBarBorder = getBorderPaint(state, orientation, 0, 0, width, height);
        
		Paint scrollBarTrackBorder = getBorderPaint(state, orientation, 0, 0, trackBounds.width, trackBounds.height);
		
		Paint scrollBarTrackBackground = getTrackBackgroundPaint(state, orientation, 0, 0, trackBounds.width, trackBounds.height);
        
		Shape outerShape;
		if (SwingUtilities.getAncestorOfClass(JPopupMenu.class, c) == null) {
			outerShape = new Rectangle(0,
					0,
					width - 1,
					height - 1);
		} else {
			outerShape = new Line2D.Float(0,
					0,
					0,
					height - 1);
		}

		
		Shape innerShape = A03GraphicsUtilities.createRoundRectangle(trackBounds.x,
				trackBounds.y,
				trackBounds.width - 1,
				trackBounds.height - 1,
				radius);

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		Paint scrollBarBackground = getBackgroundPaint(state, orientation, 0, 0, width, height);
		graphics.setPaint(scrollBarBackground);

		if (orientation == JScrollBar.HORIZONTAL) {
			if (trackBounds.width > minimumThumbSize.width) {
				graphics.fillRect(0, 0, trackBounds.x + radius, trackBounds.height);
				
				int endX = trackBounds.x + trackBounds.width - radius;
				
				graphics.fillRect(endX, 0, width - endX, trackBounds.height);

				graphics.setPaint(scrollBarBorder);
				graphics.draw(outerShape);

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				
				graphics.setPaint(scrollBarTrackBackground);
				graphics.fill(innerShape);
	
				graphics.setPaint(scrollBarTrackBorder);
				graphics.draw(innerShape);
			} else {
				graphics.fillRect(0, 0, width, height);
			}
		} else {
			if (trackBounds.height > minimumThumbSize.height) {
				/*
				 * TOP, BOTTOM caps
				 */
				graphics.fillRect(0, 0, trackBounds.width, trackBounds.y + radius);
		
				graphics.fillRect(0, trackBounds.height - radius, trackBounds.width, height - (trackBounds.height - radius));

				graphics.setPaint(scrollBarBorder);
				graphics.draw(outerShape);

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				
				graphics.setPaint(scrollBarTrackBackground);
				graphics.fill(innerShape);
	
				graphics.setPaint(scrollBarTrackBorder);
				graphics.draw(innerShape);
			} else {
				graphics.fillRect(0, 0, width, height);
			}
		}

        graphics.dispose();
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// do nothing
	}

	public DimensionUIResource getMinimumThumbSize() {
		return minimumThumbSize;
	}
	
	public Insets getArrowBorderInsets(Component c, Insets insets) {
		return null;
	}
	
	public InsetsUIResource getArrowMargin() {
		return null;
	}
	
	public void paintArrow(Component c, Graphics g, int direction) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		int size = Math.max(width, height) / 3 + 1;

		if (height < size || width < size) {
			return;
		}

		int x = (width - size) / 2;
		int y = (height - size) / 2;
    		
    	A03BasicGraphicsUtilities.paintArrow(g, x, y, size, size, direction, 
    			getArrowPaint(A03ComponentState.ENABLED, direction, x, y, size, size));
	}
	
	public void paintArrowBackground(Component c, Graphics g) {
		// do nothing
	}
	
	public void paintArrowBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}

	public Paint getBackgroundPaint(int state, int orientation, int x, int y, int width, int height) {
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
		
		if (orientation == JScrollBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}
	
	public Paint getArrowPaint(int state, int direction, int x, int y,
			int width, int height) {
		return Color.BLACK;
	}
	
	public Paint getBorderPaint(int state, int orientation, int x, int y,
			int width, int height) {
//		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
//				return BUTTON_BORDER_ENABLED_ARMED_COLOR;
//			} else {
//				return BUTTON_BORDER_ENABLED_COLOR;
//			}
//		} else {
//			if ((state & SELECTED) != 0) {
//				return BUTTON_BORDER_DISABLED_ARMED_COLOR;
//			} else {
//				return BUTTON_BORDER_DISABLED_COLOR;
//			}
//		}
		return SCROLL_BAR_BORDER_COLOR;
	}
	
	public Paint getThumbBackgroundPaint(int state, int orientation, int x,
			int y, int width, int height) {
		Color[] colors;
		
//		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED)) != 0) {
//				colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
//			} else {
//				colors = new Color[] {new Color(0x9ab8cf),
//						new Color(0x7a98af), }; // BUTTON_BACKGROUND_ENABLED_COLORS;
//			}
//		} else {
//			if ((state & SELECTED) != 0) {
				colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
//			} else {
//				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
//			}
//		}
		
		if (orientation == JScrollBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}
	
	public Paint getThumbBorderPaint(int state, int orientation, int x, int y,
			int width, int height) {
//		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
//				return BUTTON_BORDER_ENABLED_ARMED_COLOR;
//			} else {
//				return BUTTON_BORDER_ENABLED_COLOR;
//			}
//		} else {
//			if ((state & SELECTED) != 0) {
				return BUTTON_BORDER_DISABLED_ARMED_COLOR;
//			} else {
//				return BUTTON_BORDER_DISABLED_COLOR;
//			}
//		}
		
//		return new Color(0x5a789f);
	}
	
	public Paint getTrackBackgroundPaint(int state, int orientation, int x,
			int y, int width, int height) {
		Color[] colors;
		
//		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED)) != 0) {
				colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
//			} else {
//				colors = BUTTON_BACKGROUND_ENABLED_COLORS;
//			}
//		} else {
//			if ((state & SELECTED) != 0) {
//				colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
//			} else {
//				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
//			}
//		}
		
		if (orientation == JScrollBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}
	
	public Paint getTrackBorderPaint(int state, int orientation, int x, int y,
			int width, int height) {
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

}
