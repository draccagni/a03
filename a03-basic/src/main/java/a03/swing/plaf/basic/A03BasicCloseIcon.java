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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicCloseIcon implements Icon, UIResource, A03ComponentState {
	
	protected final static Stroke STROKE = new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0.0f); // , new float[] { 1.5f, 1.5f }, 3.0f);

	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicCloseIcon() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}

	public int getIconHeight() {
		return 16;
	}
	
	public int getIconWidth() {
		return 16;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Image image = A03GraphicsUtilities.createImage(c, getIconWidth(), getIconHeight());
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
        int state = A03BasicUtilities.getTitlePaneState(c);
        
        graphics.setColor(Color.WHITE);
        drawCross(graphics);

		if ((state & ENABLED) != 0) {
			float fadeLevel = (float) fadeTracker.getFadeLevel(c);
			if (fadeLevel > 0) {
				graphics.setColor(Color.RED);
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
		        drawCross(graphics);
			}
		}
        
        graphics.dispose();
        
        g.drawImage(image, x, y, c);
	}
	
	private static final float SQRT2 = (float) Math.pow(2.0, 0.5);

	protected void drawCross(Graphics2D graphics) {
		drawCross(graphics, 4, 1);
	}

	protected void drawCross(Graphics2D graphics, final float l, final float t) {
      int x1 = getIconWidth() / 2; 
      int y1 = getIconHeight() / 2; 
      graphics.translate(x1,  y1);

		final GeneralPath p0 = new GeneralPath();
	      p0.moveTo(-l - t, -l + t);
	      p0.lineTo(-l + t, -l - t);
	      p0.lineTo(0.0f, -t * SQRT2);
	      p0.lineTo(l - t, -l - t);
	      p0.lineTo(l + t, -l + t);
	      p0.lineTo(t * SQRT2 + 1, 0.0f);
	      p0.lineTo(l + t, l - t);
	      p0.lineTo(l - t, l + t);
	      p0.lineTo(0.0f, t * SQRT2 + 1);
	      p0.lineTo(-l + t, l + t);
	      p0.lineTo(-l - t, l - t);
	      p0.lineTo(-t * SQRT2, 0.0f);
	      p0.closePath();

		
		
//        int x1 = getIconWidth() / 4; 
//        int y1 = getIconHeight() / 4; 
//        int x2 = getIconWidth() * 3 / 4; 
//        int y2 = getIconWidth() * 3 / 4; 
//        
//        graphics.drawLine(x1, y1, x2, y2);
//        graphics.drawLine(x1, y2, x2, y1);
	      
	      graphics.draw(p0);
	      graphics.translate(-x1,  -y1);
	}
}
