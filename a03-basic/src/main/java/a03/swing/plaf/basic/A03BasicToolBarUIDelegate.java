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
import java.awt.Shape;

import javax.swing.JToolBar;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.ToolBarUI;

import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plaf.A03ToolBarUI;
import a03.swing.plaf.A03ToolBarUIDelegate;

public class A03BasicToolBarUIDelegate implements A03ToolBarUIDelegate, A03ComponentState {

	public A03BasicToolBarUIDelegate() {
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		JToolBar toolBar = (JToolBar) c;
		
		ToolBarUI ui = toolBar.getUI();
		if (ui instanceof A03ToolBarUI && ((A03ToolBarUI) ui).isFloating()) {
			insets.top = 1;
			insets.left = 1;
			insets.bottom = 1;
			insets.right = 1;

			return insets;				
		} else {
			if (toolBar.getOrientation() == JToolBar.HORIZONTAL) {
				insets.top = 1;
				insets.bottom = 1;
				
				boolean leftToRight = A03SwingUtilities.isLeftToRight(c);
				if (leftToRight) {
					insets.left = toolBar.isFloatable() ? 12 : 1;
					insets.right = 1;
				} else {
					insets.left = 1;
					insets.right = toolBar.isFloatable() ? 12 : 1;					
				}
	
				return insets;				
			} else {
				insets.top = toolBar.isFloatable() ? 12 : 1;
				insets.left = 1;
				insets.bottom = 1;
				insets.right = 1;
	
				return insets;
			}
		}
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		JToolBar toolBar = (JToolBar) c;

//		g.setColor(Color.RED);
//		g.drawRect(x, y, width - 1, height - 1);
		
		if (!((A03ToolBarUI) toolBar.getUI()).isFloating()) {
			if (toolBar.isFloatable()) {
				Graphics2D graphics = (Graphics2D) g; //.create();
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
		
				Insets insets = getBorderInsets(c, new Insets(0, 0, 0, 0));
				
				Shape shape;
				int x1, y1, length;
				
				int gripSize = getGripSize();
				
				gripSize = ((gripSize + 1) / 2) * 2 - 1;
				
				int orientation = toolBar.getOrientation();
				if (orientation == JToolBar.HORIZONTAL) {
					length = Math.min(height - insets.top - insets.bottom, 24);
					boolean leftToRight = A03SwingUtilities.isLeftToRight(c);
					if (leftToRight) {
						x1 = (insets.left - gripSize) / 2;						
					} else {
						x1 = width - insets.right + gripSize / 2;
					}
					y1 = (height - length) / 2;

			        shape = new Rectangle(x1, y1, gripSize - 1, length - 1);
				} else {
					length = Math.min(width - insets.left - insets.right, 24);
					x1 = (width - length) / 2;
					y1 = (insets.top - gripSize) / 2;
			        shape = new Rectangle(x1, y1, length - 1, gripSize - 1);
				}
				Paint background = Color.WHITE;
				graphics.setPaint(background);
				graphics.fill(shape);
				
				Paint border = Color.BLACK;
				graphics.setPaint(border);
				graphics.draw(shape);
				
				//graphics.dispose();
			}
		}
	}

	public InsetsUIResource getMargin() {
		return new InsetsUIResource(0, 2, 0, 2);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	// XXX ---> getGripShape(int width, int height)
	public int getGripSize() {
		return 7;
	}
}
