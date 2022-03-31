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
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.plaf.DesktopIconUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicDesktopIconUI;

import a03.swing.plaf.A03InternalFrameUI;
import a03.swing.plaf.A03InternalFrameUIDelegate;

public class A03BasicInternalFrameUIDelegate implements A03InternalFrameUIDelegate, A03ComponentState {

	protected Icon closeIcon;
	
	protected Icon iconifyIcon;
	
	protected Icon maximizeIcon;
	
	protected Icon minimizeIcon;

	public A03BasicInternalFrameUIDelegate() {
		this.closeIcon = new A03BasicCloseIcon();

		this.iconifyIcon = new A03BasicIconifyIcon();

		this.maximizeIcon = new A03BasicMaximizeIcon();

		this.minimizeIcon = new A03BasicMinimizeIcon();
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof JInternalFrame) {
			insets.top = 2;
			insets.left = 3;
			insets.bottom = 3;
			insets.right = 3;
		} else if (c instanceof JDesktopIcon) {
			insets.top = 2;
			insets.left = 1;
			insets.bottom = 1;
			insets.right = 1;
		}

		return insets;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		int titlePaneHeight = 0;
        if (c instanceof JInternalFrame) {
        	try {
	        	A03InternalFrameUI ui = (A03InternalFrameUI) ((JInternalFrame) c).getUI();
	        	titlePaneHeight = ui.getTitlePane().getPreferredSize().height;
			} catch (Exception e) {
				// do nothing
			}
		} else if (c instanceof JDesktopIcon) {
			DesktopIconUI ui = ((JDesktopIcon) c).getUI();
			if (ui instanceof BasicDesktopIconUI) {
				titlePaneHeight = ((BasicDesktopIconUI) ui).getPreferredSize((JDesktopIcon) c).height;
			}
		}

        if (titlePaneHeight > 0) {
    		Graphics2D graphics = (Graphics2D) g.create();

    		Insets insets = getBorderInsets(c, new Insets(0, 0, 0, 0));

    		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    				RenderingHints.VALUE_ANTIALIAS_OFF);

    		int state = A03BasicUtilities.getState(c);
    		
    		graphics.translate(x, y);
    		graphics.setPaint(getBorderPaint(state, 0, 0, width - 1, height - 1));
    		graphics.drawRect(0, 0, width - 1, height - 1);
    		
    		graphics.setPaint(getTitleBackgroundPaint(state, 0, 0, 0, titlePaneHeight + insets.top));
    		
    		graphics.fillRect(1, 1, width - 1 - 1, insets.top - 1);			
    		if (c instanceof JInternalFrame) {
	    		graphics.fillRect(1, 1, insets.left, height - 1 - 1);			
	
	    		graphics.fillRect(width - insets.right, 1, insets.right - 1, height - 1 - 1);			
	
	    		graphics.fillRect(insets.left, height - insets.bottom, width - insets.left - insets.right, insets.bottom - 1);
    		}
    		
    		graphics.dispose();
        }
	}
	
	public void paintTitleBackground(Component c, Graphics g) {
		int titlePaneHeight = 0;
        if (c instanceof JInternalFrame) {
        	try {
	        	A03InternalFrameUI ui = (A03InternalFrameUI) ((JInternalFrame) c).getUI();
	        	titlePaneHeight = ui.getTitlePane().getPreferredSize().height;
			} catch (Exception e) {
				// do nothing
			}
		} else if (c instanceof JDesktopIcon) {
			DesktopIconUI ui = ((JDesktopIcon) c).getUI();
			if (ui instanceof BasicDesktopIconUI) {
				titlePaneHeight = ((BasicDesktopIconUI) ui).getPreferredSize((JDesktopIcon) c).height;
			}
		}

        if (titlePaneHeight > 0) {
			Graphics2D graphics = (Graphics2D) g; //.create();
			
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);

    		int state = A03BasicUtilities.getState(c);
			
    		graphics.setPaint(getTitleBackgroundPaint(state, 0, 0, 0, titlePaneHeight));
			graphics.fillRect(0, 0, c.getWidth(), titlePaneHeight);
		    
			//graphics.dispose();
        }
	}
	
	public void paintTitleText(Component c, Graphics g, String text, int x,
			int y) {
        Graphics2D graphics = (Graphics2D) g.create();
        
		int state = A03BasicUtilities.getState(c);
        
		graphics.setColor(getTitleTextColor(state));
		graphics.drawString(text, x, y);

		graphics.dispose();
	}

	public FontUIResource getTitleFont() {
		return new FontUIResource(A03BasicFonts.FONT_BOLD_11);
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
	
//	public Color getTitleBackground(Component c) {
//        int state = ((A03TitlePane) ((A03RootPaneUI) ((JRootPane) c).getUI()).getTitlePane()).isActive() ? A03ComponentState.ENABLED : 0;
//
//		return getTitleBackground(state);
//	}

	protected Paint getBorderPaint(int state, int x, int y, int width, int height) {
		return Color.BLACK;
	}
	
	public Paint getTitleBackgroundPaint(int state, int x, int y, int width,
			int height) {
		return Color.BLACK;
	}

	public Color getTitleTextColor(int state) {
		return Color.WHITE;
	}

	public Insets getTitleMargin() {
		return new Insets(0, 5, 0, 0);
	}

	public boolean isMenuVisible() {
		return true;
	}
}
