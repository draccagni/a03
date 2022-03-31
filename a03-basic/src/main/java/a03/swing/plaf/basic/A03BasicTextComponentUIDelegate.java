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

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.JTextComponent;

import a03.swing.plaf.A03EditorPaneUIDelegate;
import a03.swing.plaf.A03FormattedTextFieldUIDelegate;
import a03.swing.plaf.A03TextAreaUIDelegate;
import a03.swing.plaf.A03TextComponentUIDelegate;
import a03.swing.plaf.A03TextFieldUIDelegate;
import a03.swing.plaf.A03TextPaneUIDelegate;

public class A03BasicTextComponentUIDelegate implements A03TextComponentUIDelegate, A03TextFieldUIDelegate, A03TextAreaUIDelegate, A03TextPaneUIDelegate, A03FormattedTextFieldUIDelegate, A03EditorPaneUIDelegate, A03ComponentState {

	private static InsetsUIResource textComponentMargin = new InsetsUIResource(1, 3, 1, 3);

	public A03BasicTextComponentUIDelegate() {
	}
	
	public InsetsUIResource getMargin() {
		return textComponentMargin;
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 2;
		insets.bottom = 2;

		/*
		 * Margin is not used when the component is inside a ComboBox/Spinner.
		 * Set left/right to 0 to delegate insets settings (see A03VenusComboBoxStyle.getBorderInsets)
		 */
		
		insets.left = 0;
		insets.right = 0;
		
		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
	    Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);
		if (SwingUtilities.getAncestorOfClass(JComboBox.class, c) == null) {
//			Color background;
//			if (c.isOpaque()) {
//				background = c.getBackground();
//			} else {
//				Container parent = c.getParent();
//				while (parent != null && !parent.isOpaque()) {
//					parent = parent.getParent();
//				}
//
//				if (parent != null && parent.isOpaque()) {
//					background = parent.getBackground();
//				} else {
//					background = UIManager.getColor("control");
//				}
//			}
			
			if (c instanceof JTextField) {
//				A03BasicUtilities.paintShadowedBorder(c, g, x, y, width, height, background);
				graphics.drawRect(0, 0, width - 1, height - 1);
			}
			
			if (c.hasFocus() && ((JTextComponent) c).isEditable()) {
//			    A03BasicGraphicsUtilities.paintBorderShadow(graphics, 3, A03GraphicsUtilities.createRoundRectangle(x, y, width - 1, height - 1, 3),
//			    		getFocusColor(),
//                      background);
			    graphics.setColor(Color.RED);
			    graphics.drawRect(0, 0, width - 1, height - 1);
			    
				//graphics.dispose();
				return;
			}
		}
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(Color.GRAY);
	}
	
	public ColorUIResource getDisabledBackground() {
		return new ColorUIResource(Color.DARK_GRAY);
	}
	
	public ColorUIResource getInactiveBackground() {
		return new ColorUIResource(Color.LIGHT_GRAY);
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getInactiveForeground() {
		return new ColorUIResource(Color.DARK_GRAY);
	}
	
	public ColorUIResource getSelectionForeground() {
		return new ColorUIResource(Color.WHITE);
	}

	public ColorUIResource getCaretColor() {
		return new ColorUIResource(Color.BLACK);
	}
}
