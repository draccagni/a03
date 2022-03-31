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
package a03.swing.plaf.aphrodite;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.plaf.DesktopIconUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicDesktopIconUI;

import a03.swing.plaf.A03InternalFrameUI;
import a03.swing.plaf.basic.A03BasicFonts;
import a03.swing.plaf.basic.A03BasicInternalFrameUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;
import a03.swing.plaf.basic.A03ComponentState;

public class A03AphroditeInternalFrameUIDelegate extends A03BasicInternalFrameUIDelegate implements A03AphroditePaints {

	private static Insets margin = new Insets(0, 0, 4, 0);
	
	public A03AphroditeInternalFrameUIDelegate() {
		this.closeIcon = new A03AphroditeCloseIcon();

		this.iconifyIcon = new A03AphroditeIconifyIcon();

		this.maximizeIcon = new A03AphroditeMaximizeIcon();

		this.minimizeIcon = new A03AphroditeMinimizeIcon();
	}

	public FontUIResource getTitleFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof JInternalFrame) {
			insets.top = 4;
			insets.left = 4;
			insets.bottom = 4;
			insets.right = 4;
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
		Graphics2D graphics = (Graphics2D) g.create();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int state;
		if (c instanceof JInternalFrame) {
			state = ((JInternalFrame) c).isSelected() ? A03ComponentState.ENABLED : 0;
		} else {
			state = A03ComponentState.ENABLED;
		}
		
		graphics.setPaint(getOuterBorderPaint(state, x, y, width, height));
		graphics.drawRect(x, y, width - 1, height - 1);
		graphics.setPaint(getBorderPaint(state, x + 1, y + 1, width - 3, height - 3));
		graphics.drawRect(x + 1, y + 1, width - 3, height - 3);

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
			graphics.setPaint(getTitleBackgroundPaint(state, x + 2, y + 2, x + 2, y + titlePaneHeight + 2));
			graphics.drawRect(x + 2, y + 2, width - 5, height - 5);
			graphics.drawRect(x + 3, y + 3, width - 7, titlePaneHeight + 7);
			graphics.setPaint(SCROLL_BAR_BORDER_COLOR); // getOuterBorderPaint(state, x + 3, y + titlePaneHeight + 1, width - x - 7, height - titlePaneHeight - y - 5));
			graphics.drawRect(x + 3, y + titlePaneHeight + 3, width - x - 7, height - titlePaneHeight - y - 7);
    		
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

	        int state = A03BasicUtilities.getTitlePaneState(c);
			
            int width = c.getWidth();
    		
    		graphics.setPaint(getTitleBackgroundPaint(state, 0, 0, 0, titlePaneHeight));
			graphics.fillRect(0, 0, width, titlePaneHeight);
		    
			graphics.setPaint(SCROLL_BAR_BORDER_COLOR); // getOuterBorderPaint(state, 0, height - 1, 0, 1));
			graphics.drawLine(0, titlePaneHeight - 1, width - 1, titlePaneHeight - 1);

			//graphics.dispose();
        }
	}
	
	public void paintTitleText(Component c, Graphics g, String text, int x,
			int y) {
        Graphics2D graphics = (Graphics2D) g; // .create();
        
        int state = A03BasicUtilities.getTitlePaneState(c);
        
//		graphics.setColor(getShadowTitleTextColor(state));
//		graphics.drawString(text, x, y + 1);

		graphics.setColor(getTitleTextColor(state));
		graphics.drawString(text, x, y);

		//graphics.dispose();
	}

	protected Paint getOuterBorderPaint(int state, int x, int y, int width, int height) {
//		Color[] colors;
//		
//		if ((state & ENABLED) != 0) {
//			colors = ROOT_PANE_BORDER_COLORS;
//		} else {
//			colors = ROOT_PANE_BORDER_COLORS;
//		}
//		
//		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		return ROOT_OUTER_BORDER_ENABLED_COLOR;
	}

	protected Paint getBorderPaint(int state, int x, int y, int width, int height) {
		return ROOT_INNER_BORDER_ENABLED_COLOR;
	}
	
	public Paint getTitleBackgroundPaint(int state, int x, int y, int width,
			int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			colors = TITLE_BAR_ENABLED_BACKGROUND_COLORS;
		} else {
			colors = TITLE_BAR_ENABLED_BACKGROUND_COLORS;
		}
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
//		return UIManager.getColor("control");
	}

//	public Color getShadowTitleTextColor(int state) {
//		return BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR;
//	}
	
	public Color getTitleTextColor(int state) {
		if ((state & ENABLED) != 0) {
			return Color.BLACK;
		} else {
			return Color.GRAY;
		}
	}
	
	@Override
	public Insets getTitleMargin() {
		return margin;
	}
}
