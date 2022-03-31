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

import javax.swing.JComboBox;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03ComboBoxUIDelegate;

public class A03BasicComboBoxUIDelegate implements A03ComboBoxUIDelegate, A03ComponentState {

	public A03BasicComboBoxUIDelegate() {
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}

	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getSelectionForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getDisabledForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getDisabledBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 2;
		insets.bottom = 2;
		insets.left = 2;
		insets.right = 2;
		
		return insets;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		JComboBox comboBox = (JComboBox) c;
		
		if (
				!comboBox.isPopupVisible() &&
				(
						c.hasFocus() ||
						comboBox.getEditor().getEditorComponent().hasFocus()
				)
			) {
		    Graphics2D graphics = (Graphics2D) g.create();
		    graphics.setColor(Color.RED);
		    g.drawRect(x, y, width - 1, height - 1);
			
			graphics.dispose();
			return;
		} else {
			g.drawRect(x, y, width - 1, height - 1);
			//A03BasicUtilities.paintShadowedBorder(c, g, x, y, width, height, c.getBackground());
		}
	}

	public Insets getArrowBorderInsets(Component c, Insets insets) {
		insets.top = 1;
		insets.bottom = 1;
		insets.left = 1;
		insets.right = 1;
		
		return insets;
	}
	
	public InsetsUIResource getArrowMargin() {
		return null;
	}
	
	public void paintArrowBackground(Component c, Graphics g) {
		// do nothing
	}
	
	public void paintArrowBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}

	public void paintArrow(Component c, Graphics g, int direction) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		int size = 8;

		if (height < size || width < size) {
			return;
		}

		int x = (width - size) / 2;
		int y = (height - size / 2) / 2;
    	
    	A03BasicGraphicsUtilities.paintArrow(g, x, y, size, size / 2, direction, 
    			Color.BLACK);
	}
}
