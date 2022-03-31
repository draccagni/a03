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

import javax.swing.JSplitPane;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03SplitPaneUIDelegate;

public class A03BasicSplitPaneUIDelegate implements A03SplitPaneUIDelegate, A03ComponentState {

	public A03BasicSplitPaneUIDelegate() {
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 0;
		insets.left = 0;
		insets.bottom = 0;
		insets.right = 0;

		return insets;
	}

	public Insets getDividerBorderInsets(Component c, Insets insets) {
		insets.top = 0;
		insets.left = 0;
		insets.bottom = 0;
		insets.right = 0;

		return insets;
	}

	public void paintDividerBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing
	}

	public void paintDividerGrip(Component c, Graphics g) {
		JSplitPane splitPane = (JSplitPane) c;
		
		int size = splitPane.getDividerSize();

		int gripSize = getGripSize();
		gripSize = ((gripSize + 1) / 2) * 2 - 1;
		
		if (size >= gripSize + 2) {
			int length = gripSize * 3;

			int width = splitPane.getWidth();
			int height = splitPane.getHeight();

			Graphics2D graphics = (Graphics2D) g.create();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			Shape shape;
			int x;
			int y;
			
			int state = A03BasicUtilities.getState(c);
			
			int orientation = splitPane.getOrientation();

			if (orientation == JSplitPane.VERTICAL_SPLIT) {
		        x = width / 2 - length / 2;
		        y = (size - gripSize) / 2;
	
		        shape = new Rectangle(x, y, length - 1, gripSize - 1);
			} else {
		        x = (size - gripSize) / 2;
	    		y = height / 2 - length / 2;
	
		        shape = new Rectangle(x, y, gripSize - 1, length - 1);
			}

			graphics.setPaint(Color.WHITE);
			graphics.fill(shape);

			graphics.setPaint(Color.BLACK);
			graphics.draw(shape);
			
			graphics.dispose();
		}
	}
	
	public Integer getDividerSize() {
		return 9;
	}
	
	public int getGripSize() {
		return 7;
	}

	public void paintArrow(Component c, Graphics g, int direction) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		int size = 6;

		if (height < size || width < size) {
			return;
		}

		int x = (width - size) / 2;
		int y = (height - size) / 2;
    		
    	A03BasicGraphicsUtilities.paintArrow(g, x, y, size, size, direction, 
    			Color.BLACK);
	}
}
