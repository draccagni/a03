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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.UIManager;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicRadioButtonIcon;

public class A03AphroditeRadioButtonIcon extends A03BasicRadioButtonIcon implements A03AphroditePaints {
	
	public A03AphroditeRadioButtonIcon(int iconWidth, int iconHeight) {
		super(iconWidth, iconHeight);
	}

	protected void paintRadio(int state, Graphics2D graphics) {
		int width = getIconWidth();
		int height = getIconHeight();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		

		graphics.setPaint(getBackgroundPaint(state, 2, 2, 2, height - 5));
		graphics.fillOval(2, 2, width - 5, height - 5);

		graphics.setPaint(getOuterBorderPaint(state, 0, 0, width - 2, height - 2));
		graphics.drawOval(0, 0, width - 2, height - 2);

		graphics.setPaint(getBorderPaint(state, 1, 1, width - 4, height - 4));
		graphics.drawOval(1, 1, width - 4, height - 4);
	}
	
	protected void paintRadioSelected(int state, Graphics2D graphics) {
		Graphics2D graphics0 = (Graphics2D) graphics.create();
		int width = getIconWidth();
		int height = getIconHeight();
		
		graphics0.translate((width - 6) / 2, (height - 6) / 2);
		graphics0.setPaint(Color.BLACK);
		graphics0.fillOval(0, 0, 5, 5);
		
		graphics0.dispose();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		super.paintIcon(c, g, x, y);
		
		if (c.hasFocus()) {
			int width = getIconWidth();
			int height = getIconHeight();

			Shape shape = new Ellipse2D.Double(1, 2, width - 4, height - 4);
		    A03AphroditeGraphicsUtilities.paintBorderShadow((Graphics2D) g, 2, shape,
		    		FOCUS_COLOR,
	                UIManager.getColor("control"));
		}
	}
	
	protected Paint getBackgroundPaint(int state, int x, int y, int width, int height) {
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
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}

	protected Paint getOuterBorderPaint(int state, int x, int y, int width, int height) {
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
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}

	protected Paint getBorderPaint(int state, int x, int y, int width, int height) {
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
