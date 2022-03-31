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
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plaf.basic.A03BasicMenuItemUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeMenuItemUIDelegate extends A03BasicMenuItemUIDelegate implements A03AphroditePaints {

	public A03AphroditeMenuItemUIDelegate() {
		this.radioButtonMenuItemIcon = new A03AphroditeRadioButtonIcon(14, 14);
	}

	public void paintText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g; //.create();

		JMenuItem menuItem = (JMenuItem) c;

//		Color shadowForeground = null;
		Color foreground;
		int state = getState(menuItem);
		if (menuItem instanceof JMenu && menuItem.getParent() instanceof JMenuBar) {
//			shadowForeground = getMenuBarMenuShadowForegroundColor(state, A03SwingUtilities.isAnchestorDecorated(c));
			foreground = getMenuBarMenuForegroundColor(state, A03SwingUtilities.isAnchestorDecorated(c));
		} else {
//			shadowForeground = getShadowForegroundColor(state);
			foreground = getForegroundColor(state);
		}

		int mnemIndex = menuItem.getDisplayedMnemonicIndex();
//		if (shadowForeground != null) {
//			graphics.setColor(shadowForeground);
//			A03GraphicsUtilities.drawStringUnderlineCharAt(menuItem, graphics, text, mnemIndex, x, y + 1);
//		}

		graphics.setColor(foreground);
		A03GraphicsUtilities.drawStringUnderlineCharAt(menuItem, graphics, text, mnemIndex, x, y);
		
		//graphics.dispose();
	}

	public void paintBackground(Component c, Graphics g) {
		JMenuItem menuItem = (JMenuItem) c;

		int width = menuItem.getWidth();
		int height = menuItem.getHeight();
		
		Graphics2D graphics = (Graphics2D) g; //.create();

		if (menuItem instanceof JMenu) {
			JPopupMenu popupMenu = ((JMenu) menuItem).getPopupMenu();
			Point menuItemLocation = menuItem.getLocationOnScreen();
			
			if (menuItem.getParent() instanceof JMenuBar) {
		        int state = A03BasicUtilities.getTitlePaneState(c);
				graphics.setPaint(CONTROL);

				graphics.fillRect(0, 0, width, height);

				if (popupMenu.isShowing()) {
					Point popupMenuLocation = popupMenu.getLocationOnScreen();
			        
			        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);

			        Shape outerShape;
			        Shape shape;
					if (menuItemLocation.y < popupMenuLocation.y) { // DOWN
						outerShape = A03GraphicsUtilities.createRoundRectangle(1, 0, width - 3, height + 3, 2);
						shape = A03GraphicsUtilities.createRoundRectangle(2, 1, width - 5, height + 1, 2);
					} else { // UP
						outerShape = A03GraphicsUtilities.createRoundRectangle(1, 0, width - 3, height + 3, 2);
						shape = A03GraphicsUtilities.createRoundRectangle(2, -3, width - 5, height + 2, 2);
					}
	
					graphics.setPaint(getMenuBarMenuBackgroundPaint(ARMED, A03SwingUtilities.isAnchestorDecorated(c), 0, 0, width, height));
					graphics.fill(shape);

					graphics.setPaint(getMenuBarMenuBorderPaint(ARMED, A03SwingUtilities.isAnchestorDecorated(c), 0, 0, width, height));
					graphics.draw(shape);
					graphics.setPaint(getMenuBarMenuOuterBorderPaint(ARMED, A03SwingUtilities.isAnchestorDecorated(c), 0, 0, width, height));
					graphics.draw(outerShape);
				}
			} else {
		        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);

				graphics.setPaint(getBackgroundPaint(getState(menuItem), 0, 0, width, height));
				graphics.fillRect(0, 0, width, height);
			}
		} else {
	        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);

			graphics.setPaint(getBackgroundPaint(getState(menuItem), 0, 0, width, height));
			graphics.fillRect(0, 0, width, height);
		}
		
		//graphics.dispose();
	}

	public Color getMenuBarMenuShadowForegroundColor(int state,
			boolean anchestorDecorated) {
		if ((state & SELECTED) != 0) {
			return BUTTON_SHADOW_FOREGROUND_ENABLED_ARMED_COLOR;
		} else {
			return BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR;
		}
	}

	public Color getMenuBarMenuForegroundColor(int state,
			boolean anchestorDecorated) {
		if ((state & ENABLED) != 0) {
			return Color.BLACK;
		} else {
			return Color.GRAY;
		}
	}
	
	public Paint getBackgroundPaint(int state, int x, int y, int width,
			int height) {
		if ((state & (SELECTED | ARMED)) != 0) {
			return ROW_ARMED_COLOR;
		} else {
			return POPUPMENU_BACKGROUND_COLOR;
		}
	}

	public Paint getMenuBarMenuBorderPaint(int state, boolean anchestorDecorated, int x, int y, int width, int height) {
//		if ((state & ENABLED) != 0) {
//  		if ((state & (SELECTED | ARMED)) != 0) {
	    if ((state & (SELECTED | ARMED)) != 0) {
				return BUTTON_BORDER_DISABLED_COLOR;
//			} else {
//				return UIManager.getColor("control");
//			}
		} else {
			return UIManager.getColor("control");
		}
	}

	public Paint getMenuBarMenuOuterBorderPaint(int state, boolean anchestorDecorated, int x, int y, int width, int height) {
		Color[] colors;

		colors = BUTTON_OUTER_BORDER_DISABLED_COLORS;
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}

	public Paint getMenuBarMenuBackgroundPaint(int state, boolean anchestorDecorated, int x, int y, int width, int height) {
		Color[] colors;
		
//		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED)) != 0) {
		if ((state & (SELECTED | ARMED)) != 0) {
				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
				
				return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
//			} else {
//				return UIManager.getColor("control");
//			}
		} else {
			return UIManager.getColor("control");
		}
	}

	public Color getSelectionBackgroundPaint(int x, int y, int width, int height) {
		return Color.BLACK;
	}

	public Color getShadowForegroundColor(int state) {
//		if ((state & SELECTED) != 0) {
			return BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR;
//		} else {
//			return BUTTON_SHADOW_FOREGROUND_DISABLED_COLOR;
//		}
	}

	public Color getForegroundColor(int state) {
		if ((state & ENABLED) != 0) {
			return Color.BLACK;
		} else {
			return Color.GRAY;
		}
	}
}
