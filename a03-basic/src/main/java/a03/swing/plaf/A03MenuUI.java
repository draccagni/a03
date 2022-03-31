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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

import a03.swing.plugin.A03UIPluginManager;

public class A03MenuUI extends BasicMenuUI {

	private JPopupMenu popupMenu;

	private PopupMenuListener popupMenuListener;
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03MenuUI();
	}

	private A03MenuUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03MenuUIDelegate.class);

		super.installUI(c);
		
	    c.setFont(uiDelegate.getFont());
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		
		A03UIPluginManager.getInstance().getUIPlugins().installUI(menuItem);

		popupMenuListener = new PopupMenuListener() {
			public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
			};

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				if (menuItem != null) {
					menuItem.repaint();
				}
			}

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if (menuItem != null) {
					menuItem.repaint();
				}
			}
		};

		popupMenu = ((JMenu) menuItem).getPopupMenu();

		popupMenu.addPopupMenuListener(popupMenuListener);
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();

		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(menuItem);

		popupMenu.removePopupMenuListener(popupMenuListener);
	}

	@Override
	protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap) {
		Graphics2D graphics = (Graphics2D) g;
		
		A03SwingUtilities.paintMenuItem(uiDelegate, graphics, (JMenuItem) c, checkIcon, arrowIcon, acceleratorFont, defaultTextIconGap);
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
