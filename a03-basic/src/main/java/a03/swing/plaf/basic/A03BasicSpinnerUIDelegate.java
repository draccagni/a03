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

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SpinnerUIDelegate;
import a03.swing.plaf.A03SwingUtilities;

public class A03BasicSpinnerUIDelegate implements A03SpinnerUIDelegate, A03ComponentState {

	public A03BasicSpinnerUIDelegate() {
	}
	
	public InsetsUIResource getArrowMargin() {
		return new InsetsUIResource(0, 0, 0, 0);
	}
	
	public void paintArrow(Component c, Graphics g, int direction) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		int size = 8;
		
		int min = size / 2 + 2;

		if (height < min || width < min) {
			return;
		}

		int x = (width - size) / 2;
		int y = (height - size / 2) / 2;
		
    	A03BasicGraphicsUtilities.paintArrow(g, x, y, size, size / 2, direction, 
    			Color.BLACK);
	}
	
	public void paintArrowBackground(Component c, Graphics g) {
		// do nothing
	}
	
	public void paintArrowBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}
	
	
	public Insets getArrowBorderInsets(Component c, Insets insets) {
		return null;
	}
	
	public void paintArrowButtonBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		boolean leftToRight = A03SwingUtilities.isLeftToRight(c);
		insets.top = 3;
		insets.left = leftToRight ? 3 : 1;
		insets.bottom = 3;
		insets.right = leftToRight ? 1 : 3;

		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	    Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);

	    JSpinner spinner = (JSpinner) c;
		
		JComponent editor = spinner.getEditor();
		
		// XXX
		boolean focused = editor.hasFocus();
		if (!focused) {
			for (int i = 0; i < editor.getComponentCount(); i++) {
				if (editor.getComponent(i).hasFocus()) {
					focused = true;
					break;
				}
			}
		}
		
		if (focused) {
			graphics.setColor(Color.RED);
		} else {
			graphics.setColor(Color.BLACK);
		}
		graphics.drawRect(0, 0, width - 1, height - 1);

		graphics.dispose();
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getDisabledBackground() {
		return new ColorUIResource(Color.GRAY);
	}
}
