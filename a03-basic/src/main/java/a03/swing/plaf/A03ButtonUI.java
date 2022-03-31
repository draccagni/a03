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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import a03.swing.plugin.A03UIPluginManager;

public class A03ButtonUI extends BasicButtonUI {

	private static A03ButtonUI ui;
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		if (A03SwingUtilities.hasCustomUIDelegate(c)) {
			return new A03ButtonUI(A03SwingUtilities.getUIDelegate(c, A03ButtonUIDelegate.class));
		}
		
		synchronized (A03ButtonUI.class) {
			if (ui == null) {
				ui = new A03ButtonUI(A03SwingUtilities.getUIDelegate(null, A03ButtonUIDelegate.class)); 
			}
		}
		
		return ui; 
	}
	
	private A03ButtonUIDelegate uiDelegate;
	
    private Rectangle viewRect = new Rectangle();
    private Rectangle textRect = new Rectangle();
    private Rectangle iconRect = new Rectangle();
	
    public A03ButtonUI(A03ButtonUIDelegate uiDelegate) {
		this.uiDelegate = uiDelegate;
	}
    
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		A03UIPluginManager.getInstance().getUIPlugins().installUI(c);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(c);

		super.uninstallUI(c);
	}
	
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);

		b.putClientProperty(A03Constants.ORIGINAL_OPACITY, b.isOpaque());
		b.setOpaque(false);
	}
	
	@Override
	protected void uninstallDefaults(AbstractButton b) {
		super.uninstallDefaults(b);
		
		b.setOpaque((Boolean) b.getClientProperty(A03Constants.ORIGINAL_OPACITY));
		b.putClientProperty(A03Constants.ORIGINAL_OPACITY, null);
	}
	
	@Override
	public boolean contains(JComponent c, int x, int y) {
		return uiDelegate.contains(c, x, y);
	}

	@Override
	public void paint(final Graphics g, final JComponent c) {
		AbstractButton b = (AbstractButton) c;

		Graphics2D graphics = (Graphics2D) g.create();
		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);

		if (b.isContentAreaFilled()) {
			uiDelegate.paintBackground(c, graphics);
		}
		
		int width = b.getWidth();
		int height = b.getHeight();
		FontMetrics fm = c.getFontMetrics(g.getFont());
		
        String text = layout(b, fm,	width, height);

        clearTextShiftOffset();

        if (b.getIcon() != null) { 
        	paintIcon(graphics, c, iconRect);
        }

        if (text != null && !text.equals("")) {
        	View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        	if (v != null) {
        		v.paint(graphics, textRect);
        	} else {
        		int x = textRect.x + getTextShiftOffset();
        		int y = textRect.y + getTextShiftOffset() + fm.getAscent();

        		uiDelegate.paintText(c, graphics, text, x, y);
        	}
		}

        if (b.isFocusPainted() && b.hasFocus()) {
        	uiDelegate.paintFocus(b, graphics, viewRect, textRect, iconRect);
        }
		
		graphics.dispose();
	}

    private String layout(AbstractButton b, FontMetrics fm, int width,
			int height) {
		Insets i = b.getInsets();
		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = width - (i.right + viewRect.x);
		viewRect.height = height - (i.bottom + viewRect.y);

		textRect.x = textRect.y = textRect.width = textRect.height = 0;
		iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

		// layout the text and icon
		return SwingUtilities.layoutCompoundLabel(b, fm, b.getText(), b
				.getIcon(), b.getVerticalAlignment(), b
				.getHorizontalAlignment(), b.getVerticalTextPosition(), b
				.getHorizontalTextPosition(), viewRect, iconRect, textRect, b
				.getText() == null ? 0 : b.getIconTextGap());
	}
	
	@Override
	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
	}
	
	@Override
	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
	}
	
	@Override
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton b = (AbstractButton) c;
        
        return A03SwingUtilities.getPreferredButtonSize(b, b.getIconTextGap());
    }
	
	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
    	
		if (c.isOpaque()) {
			graphics.setColor(c.getBackground());
		    graphics.fillRect(0, 0, c.getWidth(),c.getHeight());
		}
	    
		paint(graphics, c);
    	
    	graphics.dispose();
	}
}
