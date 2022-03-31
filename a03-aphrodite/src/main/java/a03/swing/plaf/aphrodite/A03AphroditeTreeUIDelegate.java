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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.basic.A03BasicTreeUIDelegate;

public class A03AphroditeTreeUIDelegate extends A03BasicTreeUIDelegate implements A03AphroditePaints {
	
	public A03AphroditeTreeUIDelegate() {
	}

	public void paintTreeExpandedIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		graphics.translate(x, y);

        graphics.setColor(c.getBackground());
        graphics.fillRect(2, 3, 8, 8);

        graphics.setColor(BUTTON_BORDER_ENABLED_COLOR);
        graphics.drawRect(2, 3, 8, 8);
		graphics.drawLine(4, 7, 8, 7);
		
		graphics.dispose();
	}
	
	public void paintTreeCollapsedIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		graphics.translate(x, y);

        graphics.setColor(c.getBackground());
        graphics.fillRect(2, 3, 8, 8);

        graphics.setColor(BUTTON_BORDER_ENABLED_COLOR);
        graphics.drawRect(2, 3, 8, 8);
		graphics.drawLine(4, 7, 8, 7);
		graphics.drawLine(6, 5, 6, 9);
		
		graphics.dispose();
	}

	public ColorUIResource getSelectionForeground() {
		return new ColorUIResource(Color.BLACK);
	}

	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}

//	public Color getDisabledSelectionBackground() {
//		return ROW_ARMED_COLOR;
//	}
//	
//	public Color getDisabledSelectionForeground() {
//		return Color.GRAY;
//	}
	
	public ColorUIResource getBackground(int state, int row) {
		if ((state & ARMED) != 0) {
			return getSelectionBackground();
		} else {
			return new ColorUIResource(ROW_COLORS[row % ROW_COLORS.length]);
		}
	}

	public ColorUIResource getForeground(int state, int row) {
		if ((state & ARMED) != 0) {
			return getSelectionForeground();
		} else {
			return new ColorUIResource(Color.BLACK);
		}
	}

}
