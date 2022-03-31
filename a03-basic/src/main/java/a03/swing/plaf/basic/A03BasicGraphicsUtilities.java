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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import javax.swing.SwingConstants;

public class A03BasicGraphicsUtilities {
	
	/*
	 * http://weblogs.java.net/blog/campbell/archive/2006/07/java_2d_tricker_2.html
	 */
	
	public static void paintArrow(Graphics g, int x, int y, int width, int height, int direction, Paint foreground) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		/*
		 * Force to be a multiple of 2
		 */
//		size = ((size + 1) / 2) * 2;
		
		GeneralPath path = new GeneralPath();
		switch (direction) {
		case SwingConstants.NORTH:
			path.moveTo(0,  height);
			path.lineTo(width, height);
			path.lineTo(width / 2, 0);
			break;
		case SwingConstants.SOUTH:
			path.moveTo(0, 0);
			path.lineTo(width, 0);
			path.lineTo(width / 2, height);
			break;
		case SwingConstants.WEST:
			path.moveTo(0, height / 2);
			path.lineTo(width, 0);
			path.lineTo(width, height);
			break;
		case SwingConstants.EAST:
			path.moveTo(0, 0);
			path.lineTo(width, height / 2);
			path.lineTo(0, height);
			break;
		}
		path.closePath();

		graphics.translate(x, y);

		graphics.setPaint(foreground);
		graphics.fill(path);
		
		graphics.dispose();
	}

	public static void paintCheck(Graphics g, Paint foreground, int x, int y, int width, int height) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		graphics.translate(x, y);
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		Stroke stroke = new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

		GeneralPath path = new GeneralPath();

//		path.moveTo(0, 0.45f * (height - y));
//		path.lineTo(0.35f * (width - x), height - y - 1);
//		path.quadTo(0.45f * (width - x), 0.45f * (height - y), width - x - 1, 0);

		// minus insets
		int w = width - x;
		int h = height - y;
		
		path.moveTo(0, 0.45f * h);
		path.lineTo(0.35f * w, h - 1);
		path.quadTo(0.45f * w, 0.45f * h, w - 1, 0);
		
        graphics.setStroke(stroke);

		graphics.setPaint(foreground);
		graphics.draw(path);
		
		graphics.dispose();
	}

//	final static Stroke STROKE_3 = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0.0f); // , new float[] { 1.5f, 1.5f }, 3.0f);

	public static void drawWindow(Graphics2D g2d, int x, int y, int w, int h, boolean titleVisible) {
//	    g2d.setStroke(STROKE_3);
		g2d.drawRect(x, y, w, h);
	    
	    if (titleVisible) {
	    	g2d.drawLine(x + 1, y + 3, x + w, y + 3);
	    }
	}
	
}
