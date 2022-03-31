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
import java.awt.geom.GeneralPath;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicFonts;
import a03.swing.plaf.basic.A03BasicTabbedPaneUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeTabbedPaneUIDelegate extends A03BasicTabbedPaneUIDelegate implements A03AphroditePaints {

	private static InsetsUIResource tabAreaInsets = new InsetsUIResource(4, 4, 0, 4);
	private static InsetsUIResource tabInsets = new InsetsUIResource(3, 7, 3, 7);
	private static InsetsUIResource contentInsets = new InsetsUIResource(1, 1, 1, 1);
	
	public A03AphroditeTabbedPaneUIDelegate() {
	}

	public InsetsUIResource getTabAreaInsets() {
		return tabAreaInsets;
	}
	
	public InsetsUIResource getTabInsets() {
		return tabInsets;
	}
	
	public InsetsUIResource getContentInsets() {
		return contentInsets;
	}
	
	public void paintTabText(Graphics g, JTabbedPane tabPane, int tabPlacement, int tabIndex, String title, int x, int y, boolean isSelected) {

		int state = 0;
		
		if (tabPane.isEnabled()) {
        	if (tabPane.isEnabledAt(tabIndex)) {
        		state |= ENABLED;
        		
            	if (isSelected) {
            		state |= SELECTED;
            	}
        	}
        }
		
		Graphics2D graphics = (Graphics2D) g; // .create();
		
		
        int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
//		graphics.setColor(getShadowForegroundColor(state));
//		A03GraphicsUtilities.drawStringUnderlineCharAt(tabPane, graphics,
//                title, mnemIndex,
//                x, y + 1);

		graphics.setColor(getForegroundColor(state));
		A03GraphicsUtilities.drawStringUnderlineCharAt(tabPane, graphics,
                title, mnemIndex,
                x, y);
		//graphics.dispose();
   	}	
	
	public void paintTabBackground(Graphics g, JTabbedPane tabbedPane, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		int state = 0;
    	
		if (tabbedPane.isEnabled()) {
        	if (tabbedPane.isEnabledAt(tabIndex)) {
        		state |= ENABLED;
        		
            	if (isSelected) {
            		state |= SELECTED;
            	}
        	}
        }

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		graphics.setPaint(getTabBackgroundPaint(state, tabPlacement, x, y, w, h));
		Shape shape = createTabShape(tabPlacement, x, y, w, h);
		graphics.clipRect(x, y, w, h + 1);
		graphics.fill(shape);
//		
//    	if (tabPlacement == SwingConstants.TOP) {
//    		graphics.fillRect(x + 1, y + 1, w - 3, h - 1);
//    	} else if (tabPlacement == SwingConstants.BOTTOM) {
//    		graphics.fillRect(x + 1, y, w - 3, h - 1);
//    	} else if (tabPlacement == SwingConstants.RIGHT) {
//    		graphics.fillRect(x + 1, y + 1, w - 3, h - 1);
//    	} else {
//    		graphics.fillRect(x + 1, y + 1, w - 3, h - 1);
//    	}
    	
		graphics.dispose();
	}
	
	protected Shape createTabShape(int tabPlacement, int x, int y, int w, int h) {
		Shape shape;
    	if (tabPlacement == SwingConstants.TOP) {
        	shape = A03GraphicsUtilities.createRoundRectangle(x, y, w - 1, h + 1, 2);
    	} else if (tabPlacement == SwingConstants.BOTTOM) {
    		shape = A03GraphicsUtilities.createRoundRectangle(x, y - 2, w - 1, h + 1, 2);
    	} else if (tabPlacement == SwingConstants.RIGHT) {
    		shape = A03GraphicsUtilities.createRoundRectangle(x, y + 1, w - 1, h + 1, 2);
    	} else {
    		shape = A03GraphicsUtilities.createRoundRectangle(x + 1, y, w - 1, h + 1, 2);
    	}

    	return shape;
	}
	
	public void paintTabBorder(Graphics g, JTabbedPane tabbedPane, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		int state = 0;
    	
		if (tabbedPane.isEnabled()) {
        	if (tabbedPane.isEnabledAt(tabIndex)) {
        		state |= ENABLED;
        		
            	if (isSelected) {
            		state |= SELECTED;
            	}
        	}
        }

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setPaint(getTabBorderPaint(state, tabPlacement, x, y, w, h));

		Shape shape = createTabShape(tabPlacement, x, y, w, h);
		graphics.clipRect(x, y, w, h);
		graphics.draw(shape);

//    	if (tabPlacement == SwingConstants.TOP) {
//    		g.drawRect(x, y, w - 2, h - 1);
//    	} else if (tabPlacement == SwingConstants.BOTTOM) {
//        	g.drawRect(x, y, w - 2, h - 1);
//    	} else if (tabPlacement == SwingConstants.RIGHT) {
//    		g.drawRect(x, y + 1, w - 2, h - 1);
//    	} else {
//    		g.drawRect(x + 1, y, w - 2, h - 1);
//    	}
    	
    	graphics.dispose();
	}

	public void paintTabFocusIndicator(Graphics g, JTabbedPane tabbedPane, int tabPlacement, Rectangle[] rects, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
	    // do nothing
	}	
	
	public void paintTabAreaBackground(Graphics g, JTabbedPane tabbedPane, int tabAreaWidth, int tabAreaHeight) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setPaint(getTabAreaBackgroundPaint(tabAreaWidth, tabAreaHeight));
		graphics.fillRect(0, 0, tabAreaWidth, tabAreaHeight);

		graphics.dispose();
	}

	public void paintContentBorder(Graphics g, JTabbedPane tabPane, int tabPlacement,
			int selectedIndex, int x, int y, int w, int h, Rectangle selRect) {
		Graphics2D graphics = (Graphics2D) g;

		GeneralPath path = new GeneralPath();
		
		path.moveTo(x, y);
		if (tabPlacement != JTabbedPane.LEFT || selectedIndex < 0
				|| (selRect.x + selRect.width + 1 < x)
				|| (selRect.y < y || selRect.y > y + h)) {
			path.lineTo(x, y + h - 1);
		} else {
			path.lineTo(x, selRect.y);
			if (selRect.y + selRect.height < y + h - 1) {
				int state = 0;
				if (tabPane.isEnabled()) {
		        	if (tabPane.isEnabledAt(selectedIndex)) {
		        		state |= SELECTED;
		        	}
		        }
				graphics.setPaint(getTabBackgroundPaint(state, tabPlacement, x, y, w, h));
				graphics.drawLine(x, selRect.y + 1, x, selRect.y + selRect.height - 2);

				path.moveTo(x, selRect.y + selRect.height - 1);
				path.lineTo(x, y + h - 1);
			}
		}

		if (tabPlacement != JTabbedPane.BOTTOM || selectedIndex < 0 || (selRect.y - 1 > h)
				|| (selRect.x < x || selRect.x > x + w)) {
			path.lineTo(x + w - 1, y + h - 1);
		} else {
			// Break line to show visual connection to selected tab
			path.lineTo(selRect.x, y + h - 1);
			if (selRect.x + selRect.width < x + w - 1) {
				int state = 0;
				if (tabPane.isEnabled()) {
		        	if (tabPane.isEnabledAt(selectedIndex)) {
		        		state |= SELECTED;
		        	}
		        }
				graphics.setPaint(getTabBackgroundPaint(state, tabPlacement, x, y, w, h));
				graphics.drawLine(selRect.x + 1, y + h - 1, selRect.x + selRect.width - 2, y + h - 1);
				
				path.moveTo(selRect.x + selRect.width - 1, y + h - 1);
				path.lineTo(x + w - 1, y + h - 1);
			}
		}


		if (tabPlacement != JTabbedPane.RIGHT || selectedIndex < 0 || (selRect.x - 1 > w)
				|| (selRect.y < y || selRect.y > y + h)) {
			path.lineTo(x + w - 1, y);
		} else {
			path.lineTo(x + w - 1, selRect.y + selRect.height - 1); // );
			if (selRect.y + selRect.height < y + h - 1) {
				int state = 0;
				if (tabPane.isEnabled()) {
		        	if (tabPane.isEnabledAt(selectedIndex)) {
		        		state |= SELECTED;
		        	}
		        }
				graphics.setPaint(getTabBackgroundPaint(state, tabPlacement, x, y, w, h));
				graphics.drawLine(x + w - 1, selRect.y + 1, x + w - 1, selRect.y + selRect.height - 2);

				path.moveTo(x + w - 1, selRect.y);
				path.lineTo(x + w - 1, y);
			}
		}

		if (tabPlacement != JTabbedPane.TOP || selectedIndex < 0
				|| (selRect.y + selRect.height + 1 < y)
				|| (selRect.x < x || selRect.x > x + w)) {
			path.lineTo(x, y);
		} else {
			path.lineTo(selRect.x + selRect.width - 1, y);					
			if (selRect.x + selRect.width < x + w - 1) {
				int state = 0;
				if (tabPane.isEnabled()) {
		        	if (tabPane.isEnabledAt(selectedIndex)) {
		        		state |= SELECTED;
		        	}
		        }
				graphics.setPaint(getTabBackgroundPaint(state, tabPlacement, x, y, w, h));
				graphics.drawLine(selRect.x + 1, y, selRect.x + selRect.width - 2, y);

				path.moveTo(selRect.x, y);
				path.lineTo(x + 1, y);
			}
		}
		
		graphics.setPaint(getContentBorderPaint(A03BasicUtilities.getState(tabPane), tabPlacement, x, y, w, h));
		
		graphics.draw(path);
	}	
	
	public Insets getArrowBorderInsets(Component c, Insets insets) {
		return null;
	}
	
	public InsetsUIResource getArrowMargin() {
		return null;
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public Color getContentAreaColor() {
		return CONTROL;
	}
	
	public Paint getContentBorderPaint(int state, int tabPlacement, int x,
			int y, int width, int height) {
		return TABBED_PANE_CONTENT_BORDER_COLOR;
		
//		Color[] colors;
//		
//		if ((state & ENABLED) != 0) {
//			colors = new Color[] {
//					new Color(0x5e5e5e),
//					CONTROL
//			};
//		} else {
//			colors = new Color[] {
//					new Color(0x5e5e5e),
//					CONTROL
//			};
//		}
//		
//		int length = width / 2;
//		
//		if (tabPlacement == JTabbedPane.TOP) {
//			return new GradientPaint(x, y, colors[0], x, y + length, colors[1]);
//		} else if (tabPlacement == JTabbedPane.RIGHT) {
//			return new GradientPaint(x + width, y, colors[0], x + width - length, y, colors[1]);
//		} else if (tabPlacement == JTabbedPane.BOTTOM) {
//			return new GradientPaint(x, y + height, colors[0], x, y + height - length, colors[1]);
//		} else {
//			return new GradientPaint(x, y, colors[0], x + length, y, colors[1]);
//		}
	}
	
	public Paint getTabAreaBackgroundPaint(int width, int height) {
		return CONTROL;
	}

	public Color getShadowForegroundColor(int state) {
		if ((state & SELECTED) != 0) {
			return BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR;
		} else {
			return BUTTON_SHADOW_FOREGROUND_DISABLED_COLOR;
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

	public Paint getTabBackgroundPaint(int state, int tabPlacement, int x,
			int y, int width, int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED)) != 0) {
				if (tabPlacement == JTabbedPane.BOTTOM) {
					colors = A03GraphicsUtilities.revertColors(TABBED_PANE_TAB_BACKGROUND_ENABLED_ARMED_COLORS);
				} else {
					colors = TABBED_PANE_TAB_BACKGROUND_ENABLED_ARMED_COLORS;
				}
			} else {
				if (tabPlacement == JTabbedPane.BOTTOM) {
					colors = A03GraphicsUtilities.revertColors(TABBED_PANE_TAB_BACKGROUND_ENABLED_COLORS);
				} else {
					colors = TABBED_PANE_TAB_BACKGROUND_ENABLED_COLORS;
				}
			}
		} else {
			if ((state & SELECTED) != 0) {
				if (tabPlacement == JTabbedPane.BOTTOM) {
					colors = A03GraphicsUtilities.revertColors(TABBED_PANE_TAB_BACKGROUND_DISABLED_ARMED_COLORS);
				} else {
					colors = TABBED_PANE_TAB_BACKGROUND_DISABLED_ARMED_COLORS;
				}
			} else {
				if (tabPlacement == JTabbedPane.BOTTOM) {
					colors = A03GraphicsUtilities.revertColors(TABBED_PANE_TAB_BACKGROUND_DISABLED_COLORS);
				} else {
					colors = TABBED_PANE_TAB_BACKGROUND_DISABLED_COLORS;
				}
			}
		}
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}

	public Paint getTabBorderPaint(int state, int tabPlacement, int x, int y,
			int width, int height) {
		return TABBED_PANE_CONTENT_BORDER_COLOR;
	}
}
