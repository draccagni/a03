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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicCloseIcon;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeCloseIcon extends A03BasicCloseIcon implements A03AphroditePaints {
	
	final static Stroke STROKE_3 = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0.0f); // , new float[] { 1.5f, 1.5f }, 3.0f);
	final static Stroke STROKE_4 = new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0.0f); // , new float[] { 1.5f, 1.5f }, 3.0f);

	public A03AphroditeCloseIcon() {
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Image image = A03GraphicsUtilities.createImage(c, getIconWidth(), getIconHeight());
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
        
        int state = A03BasicUtilities.getTitlePaneState(c);
        
//        graphics.translate(0, 1);
//		  graphics.setColor(BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR);
//        drawCross(graphics);
//        graphics.translate(0, -1);
//		graphics.setStroke(STROKE_3);
        graphics.setColor((state & ENABLED) != 0 ? Color.BLACK : Color.GRAY);
        drawCross(graphics);

		if ((state & ENABLED) != 0) {
			float fadeLevel = (float) fadeTracker.getFadeLevel(c);
			if (fadeLevel > 0) {
				graphics.setColor(Color.BLACK);
//				graphics.setStroke(STROKE_4);
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
//		        graphics.translate(0, 1);
//				graphics.setColor(BUTTON_SHADOW_FOREGROUND_ENABLED_ARMED_COLOR);
//		        drawCross(graphics);
//		        graphics.translate(0, -1);
		        graphics.setColor(BUTTON_FOREGROUND_ENABLED_ARMED_COLOR);
		        drawCross(graphics);
			}
		}
        
        graphics.dispose();
        
        g.drawImage(image, x, y, c);
	}
}
