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

import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.basic.A03BasicScrollPaneUIDelegate;
import a03.swing.plaf.basic.A03ComponentState;

public class A03AphroditeScrollPaneUIDelegate extends A03BasicScrollPaneUIDelegate implements A03AphroditePaints {

	public A03AphroditeScrollPaneUIDelegate() {
	}
	
	public void paintViewportBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		if (c instanceof JScrollPane) {
			JScrollPane scrollpane = (JScrollPane) c;

			Graphics2D graphics = (Graphics2D) g;
			if (scrollpane.getViewport().getView() instanceof JTable) {
				graphics.setPaint(SCROLL_BAR_BORDER_COLOR);
				graphics.drawRect(x, y, width - 1, height - 1);
			} else {
				graphics.setPaint(getOuterBorderPaint(A03ComponentState.ENABLED, 0, 0, width - 1, height - 1));
				graphics.drawRect(x, y, width - 1, height - 1);
				A03AphroditeGraphicsUtilities.paintShadowedBorder(c, g, x + 1, y + 1, width - 2, height - 2, c.getBackground());
			}
		}
	}
	
	protected Paint getOuterBorderPaint(int state, int x, int y, int width, int height) {
		Color[] colors = BUTTON_OUTER_BORDER_ENABLED_COLORS;	
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}


	public Insets getViewportBorderInsets(Component c, Insets insets) {
		if (c instanceof JScrollPane) {
			JScrollPane scrollpane = (JScrollPane) c;

			if (scrollpane.getViewport().getView() instanceof JTable) {
				insets.right = 1;
				insets.left = 1;
				insets.bottom = 1;				
			} else if (SwingUtilities.getAncestorOfClass(JPopupMenu.class, c) == null) {
				insets.top = 2;
				insets.right = 2;
				insets.left = 2;
				insets.bottom = 2;				
			}
		}

		return insets;
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(CONTROL);
	}
}
