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
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JToolBar;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plaf.A03ToolBarUI;
import a03.swing.plaf.basic.A03BasicToolBarUIDelegate;
import a03.swing.plaf.basic.A03ComponentState;

public class A03AphroditeToolBarUIDelegate extends A03BasicToolBarUIDelegate implements A03AphroditePaints {

	protected static InsetsUIResource margin = new InsetsUIResource(2, 4, 2, 4);

	public A03AphroditeToolBarUIDelegate() {
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		JToolBar toolBar = (JToolBar) c;

//		g.setColor(Color.RED);
//		g.drawRect(x, y, width - 1, height - 1);
		
		if (!((A03ToolBarUI) toolBar.getUI()).isFloating()) {
			if (toolBar.isFloatable()) {
				Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
		
				Insets insets = getBorderInsets(c, new Insets(0, 0, 0, 0));
				
				Shape outerShape;
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

			        outerShape = A03GraphicsUtilities.createRoundRectangle(x1 - 1, y1 - 1, gripSize + 1, length + 1, 3);
			        shape = A03GraphicsUtilities.createRoundRectangle(x1, y1, gripSize - 1, length - 1, 3);
				} else {
					length = Math.min(width - insets.left - insets.right, 24);
					x1 = (width - length) / 2;
					y1 = (insets.top - gripSize) / 2;
			        outerShape = A03GraphicsUtilities.createRoundRectangle(x1 - 1, y1 - 1, length + 1, gripSize + 1, 3);
			        shape = A03GraphicsUtilities.createRoundRectangle(x1, y1, length - 1, gripSize - 1, 3);
				}
				graphics.setPaint(getGripBackgroundPaint(A03ComponentState.ENABLED, orientation, x1, y1, length, length));
				graphics.fill(shape);
				
				graphics.setPaint(getGripBorderPaint(A03ComponentState.ENABLED, orientation, x1, y1, length, length));
				graphics.draw(shape);
				
				graphics.setPaint(getGripOuterBorderPaint(A03ComponentState.ENABLED, orientation, x1, y1, length, length));
				graphics.draw(outerShape);
				
				graphics.dispose();
			}
		}
	}

	public InsetsUIResource getMargin() {
		return margin;
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(CONTROL);
	}
	
	protected Paint getGripOuterBorderPaint(int state, int orientation, int x, int y, int width, int height) {
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
		
		if (orientation == JToolBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}

	protected Paint getGripBorderPaint(int state, int orientation, int x, int y, int width, int height) {
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

	protected Paint getGripBackgroundPaint(int state, int orientation, int x, int y, int width, int height) {
		Color[] colors;
		
//		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED)) != 0) {
//				colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
//			} else {
//				colors = BUTTON_BACKGROUND_ENABLED_COLORS;
//			}
//		} else {
//			if ((state & SELECTED) != 0) {
				colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
//			} else {
//				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
//			}
//		}
		
		if (orientation == JToolBar.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}
}
