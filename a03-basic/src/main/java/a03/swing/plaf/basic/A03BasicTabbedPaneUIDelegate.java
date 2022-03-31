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
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03TabbedPaneUIDelegate;

public class A03BasicTabbedPaneUIDelegate implements A03TabbedPaneUIDelegate, A03ComponentState {

	private static InsetsUIResource tabAreaInsets = new InsetsUIResource(4, 4, 0, 4);
	private static InsetsUIResource tabInsets = new InsetsUIResource(3, 7, 3, 7);
	private static InsetsUIResource contentInsets = new InsetsUIResource(1, 1, 1, 1);
	
	public A03BasicTabbedPaneUIDelegate() {
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
		
		graphics.setColor((state & ENABLED) != 0 ? Color.BLACK : Color.DARK_GRAY);
		
        int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
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

		Graphics2D graphics = (Graphics2D) g; //.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		graphics.setPaint((state & ENABLED) != 0 ? Color.WHITE : Color.LIGHT_GRAY);
		
    	if (tabPlacement == SwingConstants.TOP) {
    		graphics.fillRect(x + 1, y + 1, w - 3, h - 1);
    	} else if (tabPlacement == SwingConstants.BOTTOM) {
    		graphics.fillRect(x + 1, y, w - 3, h - 1);
    	} else if (tabPlacement == SwingConstants.RIGHT) {
    		graphics.fillRect(x + 1, y + 1, w - 3, h - 1);
    	} else {
    		graphics.fillRect(x + 1, y + 1, w - 3, h - 1);
    	}
    	
		//graphics.dispose();
	}
	
	protected Shape createTabShape(int tabPlacement, int x, int y, int w, int h) {
		Shape shape;
//    	if (tabPlacement == SwingConstants.TOP) {
//        	shape = A03GraphicsUtilities.createRoundRectangle(x, y, w - 2, h + 1, 3);
//    	} else if (tabPlacement == SwingConstants.BOTTOM) {
//    		shape = A03GraphicsUtilities.createRoundRectangle(x, y - 2, w - 2, h + 1, 3);
//    	} else if (tabPlacement == SwingConstants.RIGHT) {
//    		shape = A03GraphicsUtilities.createRoundRectangle(x, y + 1, w - 2, h + 1, 3);
//    	} else {
//    		shape = A03GraphicsUtilities.createRoundRectangle(x + 1, y, w - 2, h + 1, 3);
//    	}
    	if (tabPlacement == SwingConstants.TOP) {
        	shape = new Rectangle(x, y, w - 2, h + 1);
    	} else if (tabPlacement == SwingConstants.BOTTOM) {
    		shape = new Rectangle(x, y - 2, w - 2, h + 1);
    	} else if (tabPlacement == SwingConstants.RIGHT) {
    		shape = new Rectangle(x, y + 1, w - 2, h + 1);
    	} else {
    		shape = new Rectangle(x + 1, y, w - 2, h + 1);
    	}

    	return shape;
	}
	
	public void paintTabBorder(Graphics g, JTabbedPane tabbedPane, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		int state = 0;
    	
		if (tabbedPane.isEnabled()) {
        	if (tabbedPane.isEnabledAt(tabIndex)) {
        		state |= A03ComponentState.ENABLED;
        		
            	if (isSelected) {
            		state |= A03ComponentState.SELECTED;
            	}
        	}
        }

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setPaint((state & ENABLED) == 0 ? Color.BLACK : Color.GRAY);

		Shape shape = createTabShape(tabPlacement, x, y, w, h);
		graphics.clipRect(x, y, w - 1, h);
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

		graphics.setPaint(Color.WHITE);
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
			path.lineTo(x, selRect.y + 1);
			if (selRect.y + selRect.height < y + h - 2) {
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
			if (selRect.x + selRect.width < x + w - 2) {
				path.moveTo(selRect.x + selRect.width - 2, y + h - 1);
				path.lineTo(x + w - 1, y + h - 1);
			}
		}


		if (tabPlacement != JTabbedPane.RIGHT || selectedIndex < 0 || (selRect.x - 1 > w)
				|| (selRect.y < y || selRect.y > y + h)) {
			path.lineTo(x + w - 1, y);
		} else {
			path.lineTo(x + w - 1, selRect.y + selRect.height - 2); // );

			if (selRect.y + selRect.height < y + h - 2) {
				path.moveTo(x + w - 1, selRect.y);
				path.lineTo(x + w - 1, y);
			}
		}

		if (tabPlacement != JTabbedPane.TOP || selectedIndex < 0
				|| (selRect.y + selRect.height + 1 < y)
				|| (selRect.x < x || selRect.x > x + w)) {
			path.lineTo(x, y);
		} else {
			path.lineTo(selRect.x + selRect.width - 2, y);
					
			if (selRect.x + selRect.width < x + w - 2) {
				path.moveTo(selRect.x, y);
				path.lineTo(x + 1, y);
			}
		}
		
		graphics.setPaint((A03BasicUtilities.getState(tabPane)
				& ENABLED) == 0 ? Color.BLACK : Color.GRAY);
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
	
	public ColorUIResource getContentAreaBackground() {
		return new ColorUIResource(Color.WHITE);
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
    			Color.BLACK);
	}
	
	public void paintArrowBackground(Component c, Graphics g) {
		// do nothing
	}
	
	public void paintArrowBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}
}
