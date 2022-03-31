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
package a03.swing.plaf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

import a03.swing.plugin.A03UIPluginManager;

public class A03TextAreaUI extends BasicTextAreaUI {

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03TextAreaUI();
	}
	
	private A03TextAreaUIDelegate uiDelegate;
	
	@Override
	protected void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);

		if (evt.getPropertyName().equals("componentOrientation")) {
			/*
			 * XXX: a tricky to update JTextArea text orientation 
			 */
			getComponent().setText(getComponent().getText());
		}
	}

	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03TextAreaUIDelegate.class);
		
		super.installUI(c);
		
	    JTextComponent editor = getComponent();
	    
	    Font font = c.getFont();
        if (font instanceof FontUIResource) {
        	font = uiDelegate.getFont();
        }
    	editor.setFont(font);
        Color bg = c.getBackground();
        if (bg instanceof UIResource) {
        	bg = uiDelegate.getBackground();
        }
        editor.setBackground(bg);
        editor.setForeground(uiDelegate.getForeground());
        editor.setCaretColor(uiDelegate.getCaretColor());
        editor.setSelectionColor(uiDelegate.getSelectionBackground());
        editor.setSelectedTextColor(uiDelegate.getSelectionForeground());
        editor.setDisabledTextColor(uiDelegate.getInactiveForeground());
        editor.setBorder(A03BorderFactory.createTextComponentBorder(uiDelegate));
        editor.setMargin(uiDelegate.getMargin());
		
		A03UIPluginManager.getInstance().getUIPlugins().installUI(editor);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(getComponent());

		super.uninstallUI(c);
	}
	

	@Override
	protected void paintBackground(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		super.paintBackground(graphics);
		
		graphics.dispose();
	}
	
//	@Override
//	protected void paintSafely(Graphics g) {
//		Graphics2D graphics = (Graphics2D) g.create();
//		
//		/*
//		 * XXX: to render text properly we need both
//		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//		A03GraphicsUtilities.installDesktopHints(graphics);
//	    
//		super.paintSafely(graphics);
//    	
//    	graphics.dispose();
//	}
	

	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);
	    
    	paint(graphics, c);
    	
    	graphics.dispose();
	}	
}
