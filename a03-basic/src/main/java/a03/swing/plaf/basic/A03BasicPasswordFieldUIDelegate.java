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
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03PasswordFieldUIDelegate;

public class A03BasicPasswordFieldUIDelegate extends A03BasicTextComponentUIDelegate implements A03PasswordFieldUIDelegate {

	private static Map<Integer, Image> cachedEchoCharacterImages = new HashMap<Integer, Image>();
	
	public A03BasicPasswordFieldUIDelegate() {
	}
	
	public void paintEchoCharacter(Component c, Graphics g, int x, int y, int charWidth) {
		Image image = cachedEchoCharacterImages.get(charWidth);
		
		if (image == null) {
			image = A03GraphicsUtilities.createImage(null, charWidth - 2, charWidth - 2);
			
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			graphics.setColor(Color.BLACK);
			graphics.fillOval(0, 0, charWidth - 2, charWidth - 2);
			
			graphics.dispose();
			
			cachedEchoCharacterImages.put(charWidth, image);
		}
		
		g.drawImage(image, x, y, null);
	}
}
