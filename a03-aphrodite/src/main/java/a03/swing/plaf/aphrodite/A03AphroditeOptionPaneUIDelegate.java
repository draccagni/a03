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
import java.awt.RenderingHints;

import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicOptionPaneUIDelegate;

public class A03AphroditeOptionPaneUIDelegate extends A03BasicOptionPaneUIDelegate implements A03AphroditePaints {
	
	public A03AphroditeOptionPaneUIDelegate() {
	}
	
	public void paintMessageAreaBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics.setColor(BUTTON_BORDER_ENABLED_COLOR);
		graphics.draw(A03GraphicsUtilities.createRoundRectangle(1, 1, width - 3, height - 3, 3));
		Color[] colors = BUTTON_OUTER_BORDER_ENABLED_ARMED_COLORS;
		graphics.setPaint(new GradientPaint(0, 0, colors[0], 0, height - 1, colors[1]));
		graphics.draw(A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 2));
		
		graphics.dispose();
	}

	public ColorUIResource getMessageAreaBackground() {
		return new ColorUIResource(CONTROL);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(CONTROL);
	}
}
