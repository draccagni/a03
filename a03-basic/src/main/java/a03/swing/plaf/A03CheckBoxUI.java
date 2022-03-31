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
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import a03.swing.plugin.A03UIPluginManager;

public class A03CheckBoxUI extends BasicCheckBoxUI {

	private static A03CheckBoxUI ui;
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		if (A03SwingUtilities.hasCustomUIDelegate(c)) {
			return new A03CheckBoxUI(A03SwingUtilities.getUIDelegate(c, A03CheckBoxUIDelegate.class));
		}
		
		synchronized (A03CheckBoxUI.class) {
			if (ui == null) {
				ui = new A03CheckBoxUI(A03SwingUtilities.getUIDelegate(null, A03CheckBoxUIDelegate.class)); 
			}
		}
		
		return ui; 
	}
	
	private A03CheckBoxUIDelegate uiDelegate;

	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03CheckBoxUIDelegate.class);
	
		super.installUI(c);
	
		((AbstractButton) c).setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
		((JCheckBox) c).setIcon(A03IconFactory.createCheckBoxIcon(uiDelegate));
		
		c.setFont(uiDelegate.getFont());
	}

	public A03CheckBoxUI(A03CheckBoxUIDelegate uiDelegate) {
		this.uiDelegate = uiDelegate;
	}
	
	public synchronized void paint(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g;

		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();

		Dimension size = c.getSize();

		Font f = c.getFont();
		graphics.setFont(f);
		FontMetrics fm = c.getFontMetrics(f);

		Rectangle viewRect = new Rectangle(size);
		Rectangle iconRect = new Rectangle();
		Rectangle textRect = new Rectangle();

		Insets i = c.getInsets();
		viewRect.x += i.left;
		viewRect.y += i.top;
		viewRect.width -= (i.right + viewRect.x);
		viewRect.height -= (i.bottom + viewRect.y);

		Icon altIcon = b.getIcon();

		String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(),
			altIcon != null ? altIcon : getDefaultIcon(), b
					.getVerticalAlignment(), b.getHorizontalAlignment(), b
					.getVerticalTextPosition(), b
					.getHorizontalTextPosition(), viewRect, iconRect,
			textRect, b.getIconTextGap());

		if (c.isOpaque()) {
			graphics.setColor(b.getBackground());
			graphics.fillRect(0, 0, size.width, size.height);
		}

		if (altIcon != null) {
			if (!model.isEnabled()) {
				if (model.isSelected()) {
					altIcon = b.getDisabledSelectedIcon();
				} else {
					altIcon = b.getDisabledIcon();
				}
			} else if (model.isPressed() && model.isArmed()) {
				altIcon = b.getPressedIcon();
				if (altIcon == null) {
					altIcon = b.getSelectedIcon();
				}
			} else if (model.isSelected()) {
				if (b.isRolloverEnabled() && model.isRollover()) {
					altIcon = (Icon) b.getRolloverSelectedIcon();
					if (altIcon == null) {
						altIcon = (Icon) b.getSelectedIcon();
					}
				} else {
					altIcon = (Icon) b.getSelectedIcon();
				}
			} else if (b.isRolloverEnabled() && model.isRollover()) {
				altIcon = (Icon) b.getRolloverIcon();
			}

			if (altIcon == null) {
				altIcon = b.getIcon();
			}

			altIcon.paintIcon(c, graphics, iconRect.x, iconRect.y);
		} else {
			getDefaultIcon().paintIcon(c, graphics, iconRect.x, iconRect.y);
		}

		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);
	    
		if (text != null) {
			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
			if (v != null) {
				v.paint(graphics, textRect);
			} else {
				paintText(g, c, textRect, text);
			}
		}

		if (b.hasFocus() && b.isFocusPainted()) {
			uiDelegate.paintFocus(b, graphics, textRect, size);
		}
	}
	
	@Override
	protected void installDefaults(AbstractButton b) {
		super.installDefaults(b);
		
		if (uiDelegate != null) {
			b.putClientProperty(A03Constants.ORIGINAL_OPACITY, b.isOpaque());
			b.setOpaque(false);
		}
	}

	@Override
	protected void uninstallDefaults(AbstractButton b) {
		super.uninstallDefaults(b);
		
		if (uiDelegate != null) {
			b.setOpaque((Boolean) b.getClientProperty(A03Constants.ORIGINAL_OPACITY));
			b.putClientProperty(A03Constants.ORIGINAL_OPACITY, null);
		}
	}
	
	@Override
	protected void installListeners(AbstractButton b) {
		super.installListeners(b);

		A03UIPluginManager.getInstance().getUIPlugins().installUI(b);
	}

	@Override
	protected void uninstallListeners(AbstractButton b) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(b);

		super.uninstallListeners(b);
	}

	@Override
	protected void paintText(Graphics g, JComponent c, Rectangle textRect,
			String text) {
		if (uiDelegate != null) {
			FontMetrics fm = c.getFontMetrics(g.getFont());
			int x = textRect.x + getTextShiftOffset();
			int y = textRect.y + getTextShiftOffset() + fm.getAscent();
	
			uiDelegate.paintText(c, g, text, x, y);
		} else {
			super.paintText(g, c, textRect, text);
		}
	}

	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		if (c.isOpaque()) {
			graphics.setColor(c.getBackground());
			graphics.fillRect(0, 0, c.getWidth(), c.getHeight());
		}

		paint(graphics, c);

		graphics.dispose();
	}
}
