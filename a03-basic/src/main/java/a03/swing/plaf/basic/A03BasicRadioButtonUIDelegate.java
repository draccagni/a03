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
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03RadioButtonUIDelegate;

public class A03BasicRadioButtonUIDelegate implements A03RadioButtonUIDelegate {
	
	protected Icon radioButtonIcon;
	
	public A03BasicRadioButtonUIDelegate() {
		radioButtonIcon = new A03BasicRadioButtonIcon(14, 14);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		return null;
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public int getIconHeight() {
		return radioButtonIcon.getIconHeight();
	}
	
	public int getIconWidth() {
		return radioButtonIcon.getIconWidth();
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		radioButtonIcon.paintIcon(c, g, x, y);
	}
	
	public void paintText(Component c, Graphics g, String text, int x, int y) {
        AbstractButton b = (AbstractButton) c;                       

		g.setColor(getForegroundColor(A03BasicUtilities.getState(c)));

        int mnemonicIndex = b.getDisplayedMnemonicIndex();
	    A03GraphicsUtilities.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, x, y);
	}

	protected Color getForegroundColor(int state)  {
		return Color.BLACK;
	}
}
