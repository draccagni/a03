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
import java.awt.RenderingHints;
import java.awt.Window;

import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03MenuBarUIDelegate;
import a03.swing.plaf.A03SwingUtilities;

public class A03BasicMenuBarUIDelegate implements A03MenuBarUIDelegate, A03ComponentState {
	
	public A03BasicMenuBarUIDelegate() {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 0;
		insets.left = 0;
		insets.bottom = 0;
		insets.right = 0;

		return insets;
	}
	
	public void paintBackground(Component c, Graphics g) {
		int width = c.getWidth();
		int height = c.getHeight();

		Graphics2D graphics = (Graphics2D) g; //.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		JRootPane rootPane = (JRootPane) SwingUtilities.getAncestorOfClass(JRootPane.class, c);

		int state;
		JInternalFrame internalFrame = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, rootPane);
		if (internalFrame != null) {
			state = internalFrame.isSelected() ? ENABLED : 0;
		} else {
	        Window window = SwingUtilities.getWindowAncestor(rootPane);
	        
	        state = window.isActive() ? ENABLED : 0;
		}
		
		graphics.setPaint(getBackgroundPaint(state, A03SwingUtilities.isAnchestorDecorated(c), 0, 0, width, height));
		graphics.fillRect(0, 0, width, height);
		
		//graphics.dispose();
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// do nothing
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public Paint getBackgroundPaint(int state, boolean anchestorDecorated, int x, int y, int width, int height) {
		return Color.BLACK;
	}
}
