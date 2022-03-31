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
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03ScrollBarUIDelegate;

public class A03BasicScrollBarUIDelegate implements A03ScrollBarUIDelegate, A03ComponentState {

	private static final DimensionUIResource minimumThumbSize = new DimensionUIResource(22, 22);

	public A03BasicScrollBarUIDelegate() {
	}
	
	public void paintBackground(Component c, Graphics g) {
		// do nothing
	}

	public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		int x = thumbBounds.x + 1;
		int y = thumbBounds.y + 1;
		int width = thumbBounds.width - 3;
		int height = thumbBounds.height - 3;
		
        Paint backgroundPaint = Color.WHITE; 
		Paint borderPaint = Color.DARK_GRAY;

		Graphics2D graphics = (Graphics2D) g; // .create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setPaint(backgroundPaint);
        graphics.fillRect(x,
				y,
				width,
				height);
        
		graphics.setPaint(borderPaint);
        graphics.drawRect(x,
				y,
				width,
				height);
        
        //graphics.dispose();
	}

	public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		JScrollBar scrollbar = (JScrollBar) c;

		Graphics2D graphics = (Graphics2D) g.create();
		
		int width = c.getWidth();
		int height = c.getHeight();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		int orientation = scrollbar.getOrientation();
		
		Paint scrollBarBorder = Color.LIGHT_GRAY;
        
		Paint scrollBarTrackBorder = Color.DARK_GRAY;
		
		Paint scrollBarTrackBackground = Color.GRAY;
        
		graphics.setPaint(scrollBarBorder);
		graphics.drawRect(0,
				0,
				width - 1,
				height - 1);
		
		if (orientation == JScrollBar.HORIZONTAL) {
			if (trackBounds.width > minimumThumbSize.width) {
				graphics.setPaint(scrollBarTrackBackground);
				graphics.fillRect(trackBounds.x,
						trackBounds.y,
						trackBounds.width - 1,
						trackBounds.height - 1);
	
				graphics.setPaint(scrollBarTrackBorder);
				graphics.drawRect(trackBounds.x,
						trackBounds.y,
						trackBounds.width - 1,
						trackBounds.height - 1);
			} else {
				graphics.fillRect(0, 0, width, height);
			}
		} else {
			if (trackBounds.height > minimumThumbSize.height) {
				graphics.setPaint(scrollBarTrackBackground);
				graphics.fillRect(trackBounds.x,
						trackBounds.y,
						trackBounds.width - 1,
						trackBounds.height - 1);
	
				graphics.setPaint(scrollBarTrackBorder);
				graphics.drawRect(trackBounds.x,
						trackBounds.y,
						trackBounds.width - 1,
						trackBounds.height - 1);
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
