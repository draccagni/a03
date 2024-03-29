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
import java.awt.RenderingHints;

import javax.swing.JSeparator;
import javax.swing.UIManager;

import a03.swing.plaf.A03PopupMenuSeparatorUIDelegate;

public class A03BasicPopupMenuSeparatorUIDelegate implements A03PopupMenuSeparatorUIDelegate {

	public A03BasicPopupMenuSeparatorUIDelegate() {
	}

	public void paintSeparator(Component c, Graphics g) {
		Graphics2D graphics = (Graphics2D) g; //.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		JSeparator separator = (JSeparator) c;

		//Color background = c.getParent().getBackground();
		
		Color foreground = UIManager.getColor("controlDkShadow");
		
		graphics.setColor(foreground);
		if (separator.getOrientation() == JSeparator.HORIZONTAL) {
			//A03BasicGraphicsUtilities.paintHVDepressedLine(graphics, 0, 0, c.getWidth(), 0, c.getWidth() / 2, background, foreground);
			graphics.drawLine(0, 0, c.getWidth(), 0);
		} else {
			//A03BasicGraphicsUtilities.paintHVDepressedLine(graphics, 0, 0, 0, c.getHeight(), c.getHeight() / 2, background, foreground);
			graphics.drawLine(0, 0, 0, c.getHeight());
		}

		//graphics.dispose();
	}
}
