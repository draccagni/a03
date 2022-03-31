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

import javax.swing.JLabel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03LabelUIDelegate;

public class A03BasicLabelUIDelegate implements A03LabelUIDelegate {

	public A03BasicLabelUIDelegate() {
	}

	public void paintDisabledText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g;

		JLabel label = (JLabel) c;
		int mnemIndex = label.getDisplayedMnemonicIndex();
		
		graphics.setColor(getDisabledForeground());
		A03GraphicsUtilities.drawStringUnderlineCharAt(label, graphics, text, mnemIndex, x, y);
	}

	public void paintEnabledText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g;

		JLabel label = (JLabel) c;
		int mnemIndex = label.getDisplayedMnemonicIndex();
		
		graphics.setColor(c.getForeground());
		A03GraphicsUtilities.drawStringUnderlineCharAt(label, graphics, text, mnemIndex, x, y);
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getDisabledForeground() {
		return new ColorUIResource(Color.BLACK);
	}

	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	

}
