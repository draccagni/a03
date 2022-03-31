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
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03RootPaneUI;
import a03.swing.plaf.A03RootPaneUIDelegate;
import a03.swing.plaf.A03TitlePane;

public class A03BasicRootPaneUIDelegate implements A03RootPaneUIDelegate, A03ComponentState {

	protected Icon closeIcon;
	
	protected Icon iconifyIcon;
	
	protected Icon maximizeIcon;
	
	protected Icon minimizeIcon;

	public A03BasicRootPaneUIDelegate() {
		this.closeIcon = new A03BasicCloseIcon();

		this.iconifyIcon = new A03BasicIconifyIcon();

		this.maximizeIcon = new A03BasicMaximizeIcon();

		this.minimizeIcon = new A03BasicMinimizeIcon();
	}

	public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = 1;		
		insets.left = 3;
		insets.bottom = 3;
		insets.right = 3;

		return insets;
	}
	
	public FontUIResource getTitleFont() {
		return new FontUIResource(A03BasicFonts.FONT_BOLD_11);
	}

	public Insets getTitleMargin() {
		return new Insets(0, 5, 0, 0);
	}
	
	public boolean isMenuVisible() {
		return true;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
        JRootPane rootPane = (JRootPane) c;

        A03RootPaneUI rootPaneUI = (A03RootPaneUI) rootPane.getUI();

		Graphics2D graphics = (Graphics2D) g.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
        A03TitlePane titlePane = (A03TitlePane) rootPaneUI.getTitlePane();
        if (titlePane != null) {
			int titlePaneHeight = titlePane.getHeight();
			
			Insets insets = getBorderInsets(c, new Insets(0, 0, 0, 0));
			
			int h = titlePaneHeight - 1;
			JMenuBar menuBar = ((JRootPane) c).getJMenuBar();
			
			if (menuBar != null) {
				int menuBarHeight = menuBar.getHeight();
				h += menuBarHeight;
			}
			
			graphics.translate(x, y);
			
	        int state = A03BasicUtilities.getTitlePaneState(c);
	        
			graphics.setPaint((state & ENABLED) != 0 ? Color.BLACK : Color.DARK_GRAY);
			graphics.drawRect(0, 0, width - 1, height - 1);
	
			graphics.fillRect(1, 1, insets.left, height - 1 - 1);			
	
			graphics.fillRect(width - insets.right, 1, insets.right - 1, height - 1 - 1);			
	
			graphics.fillRect(insets.left, height - insets.bottom, width - insets.left - insets.right, insets.bottom - 1);
        }

		graphics.dispose();
	}
	
	public void paintTitleBackground(Component c, Graphics g) {
        JRootPane rootPane = (JRootPane) c;

        A03RootPaneUI ui = (A03RootPaneUI) rootPane.getUI();
        
        A03TitlePane titlePane = (A03TitlePane) ui.getTitlePane();
        
        int width = titlePane.getWidth();
		int height = titlePane.getHeight();

		Graphics2D graphics = (Graphics2D) g; //.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	
        int state = A03BasicUtilities.getTitlePaneState(c);
        
		graphics.setPaint((state & ENABLED) != 0 ? Color.BLACK : Color.DARK_GRAY);
		graphics.fillRect(0, 0, width, height);
	    
		//graphics.dispose();
	}
	
	public void paintTitleText(Component c, Graphics g, String text, int x,
			int y) {
        Graphics2D graphics = (Graphics2D) g; //.create();
        
        int state = A03BasicUtilities.getTitlePaneState(c);
        
		graphics.setColor((state & ENABLED) != 0 ? Color.WHITE : Color.GRAY);
		graphics.drawString(text, x, y);

		//graphics.dispose();
	}

	public int getCloseIconHeight() {
		return closeIcon.getIconHeight();
	}
	
	public int getCloseIconWidth() {
		return closeIcon.getIconWidth();
	}
	
	public int getIconifyIconHeight() {
		return iconifyIcon.getIconHeight();
	}
	
	public int getIconifyIconWidth() {
		return iconifyIcon.getIconWidth();
	}
	
	public int getMaximizeIconHeight() {
		return maximizeIcon.getIconHeight();
	}
	
	public int getMaximizeIconWidth() {
		return maximizeIcon.getIconWidth();
	}
	
	public int getMinimizeIconHeight() {
		return minimizeIcon.getIconHeight();
	}
	
	public int getMinimizeIconWidth() {
		return minimizeIcon.getIconWidth();
	}
	
	public void paintCloseIcon(Component c, Graphics g, int x, int y) {
		closeIcon.paintIcon(c, g, x, y);
	}
	
	public void paintIconifyIcon(Component c, Graphics g, int x, int y) {
		iconifyIcon.paintIcon(c, g, x, y);
	}
	
	public void paintMaximizeIcon(Component c, Graphics g, int x, int y) {
		maximizeIcon.paintIcon(c, g, x, y);
	}
	
	public void paintMinimizeIcon(Component c, Graphics g, int x, int y) {
		minimizeIcon.paintIcon(c, g, x, y);
	}
}
