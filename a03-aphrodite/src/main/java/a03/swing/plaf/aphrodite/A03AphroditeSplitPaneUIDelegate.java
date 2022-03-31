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
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JSplitPane;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicSplitPaneUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;
import a03.swing.plaf.basic.A03ComponentState;

public class A03AphroditeSplitPaneUIDelegate extends A03BasicSplitPaneUIDelegate implements A03AphroditePaints {

	public A03AphroditeSplitPaneUIDelegate() {
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(CONTROL);
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

			Shape outerShape;
			Shape shape;
			int x;
			int y;
			
			int state = A03BasicUtilities.getState(c);
			
			int orientation = splitPane.getOrientation();

			if (orientation == JSplitPane.VERTICAL_SPLIT) {
		        x = width / 2 - length / 2;
		        y = (size - gripSize) / 2;
	
		        outerShape = A03GraphicsUtilities.createRoundRectangle(x - 1, y - 1, length + 1, gripSize + 1, 3);
		        shape = A03GraphicsUtilities.createRoundRectangle(x, y, length - 1, gripSize - 1, 3);
			} else {
		        x = (size - gripSize) / 2;
	    		y = height / 2 - length / 2;
	
		        outerShape = A03GraphicsUtilities.createRoundRectangle(x - 1, y - 1, gripSize + 1, length + 1, 3);
		        shape = A03GraphicsUtilities.createRoundRectangle(x, y, gripSize - 1, length - 1, 3);
			}

			graphics.setPaint(getGripBackgroundPaint(A03ComponentState.ENABLED, orientation, x, y, length, length));
			graphics.fill(shape);
			
			graphics.setPaint(getGripBorderPaint(A03ComponentState.ENABLED, orientation, x, y, length, length));
			graphics.draw(shape);
			
			graphics.setPaint(getGripOuterBorderPaint(A03ComponentState.ENABLED, orientation, x, y, length, length));
			graphics.draw(outerShape);
			
			graphics.dispose();
		}
	}
	
	public Shape getGripShape(int state, int orientation, int x, int y, int width,
			int height) {
//		return A03GraphicsUtilities.createRoundRectangle(x, y, width, height, getGripSize() / 2);
		return new Rectangle(x, y, width, height);
	}
	
	public int getGripSize() {
		return 7;
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
		
		if (orientation == JSplitPane.VERTICAL_SPLIT) {
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
		
		if (orientation == JSplitPane.VERTICAL_SPLIT) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}
}
