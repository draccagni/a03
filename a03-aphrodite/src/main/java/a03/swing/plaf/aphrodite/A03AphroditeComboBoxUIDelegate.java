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

import javax.swing.JComboBox;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicComboBoxUIDelegate;

public class A03AphroditeComboBoxUIDelegate extends A03BasicComboBoxUIDelegate implements A03AphroditePaints {

	public A03AphroditeComboBoxUIDelegate() {
	}

	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}

	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}
	
	public ColorUIResource getDisabledForeground() {
		return new ColorUIResource(Color.GRAY);
	}
	
	public ColorUIResource getDisabledBackground() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 3;
		insets.bottom = 3;
		insets.left = 3;
		insets.right = 3;
		
		return insets;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
	    Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);
		JComboBox comboBox = (JComboBox) c;
		
		if (
				!comboBox.isPopupVisible() &&
				(
						c.hasFocus() ||
						comboBox.getEditor().getEditorComponent().hasFocus()
				)
			) {
		    A03AphroditeGraphicsUtilities.paintBorderShadow(graphics, 3, A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 3),
		    		FOCUS_COLOR,
                    c.getBackground());
			
			return;
		} else {
			graphics.setPaint(getOuterBorderPaint(ENABLED, 0, 0, width - 1, height - 1));
			graphics.drawRect(0, 0, width - 1, height - 1);
			A03AphroditeGraphicsUtilities.paintShadowedBorder(c, g, 1, 1, width - 2, height - 2, c.getBackground());
		}
		graphics.dispose();
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
}
