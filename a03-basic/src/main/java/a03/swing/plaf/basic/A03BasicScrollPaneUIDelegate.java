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

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03ScrollPaneUIDelegate;

public class A03BasicScrollPaneUIDelegate implements A03ScrollPaneUIDelegate {

	public A03BasicScrollPaneUIDelegate() {
	}
	
	public void paintViewportBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		if (c instanceof JScrollPane) {
			Graphics2D graphics = (Graphics2D) g;
			graphics.setPaint(Color.DARK_GRAY);
			graphics.drawRect(x, y, width - 1, height - 1);
		}
	}

	public Insets getViewportBorderInsets(Component c, Insets insets) {
		if (c instanceof JScrollPane) {
			JScrollPane scrollpane = (JScrollPane) c;

			if (!(scrollpane.getViewport().getView() instanceof JTable)) {
				insets.top = 1;
			}
			
			insets.right = 1;
			insets.left = 1;
			insets.bottom = 1;
		}

		return insets;
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 0;
		insets.left = 0;
		insets.bottom = 0;				
		insets.right = 0;

		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
}
