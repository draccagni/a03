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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03CheckBoxMenuItemUIDelegate;
import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03MenuItemUIDelegate;
import a03.swing.plaf.A03MenuUIDelegate;
import a03.swing.plaf.A03RadioButtonMenuItemUIDelegate;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicMenuItemUIDelegate implements A03MenuItemUIDelegate, A03MenuUIDelegate, A03CheckBoxMenuItemUIDelegate, A03RadioButtonMenuItemUIDelegate, A03ComponentState {

	protected Icon radioButtonMenuItemIcon;

	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicMenuItemUIDelegate() {
		this.radioButtonMenuItemIcon = new A03BasicRadioButtonIcon(14, 14);
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		return null;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// do nothing
	}
	
	protected int getState(JMenuItem menuItem) {
		int state = 0;

		ButtonModel model = menuItem.getModel();
		
		if (model.isEnabled()) {
			state = A03ComponentState.ENABLED;
		}
		
		if (menuItem instanceof JMenu) {
			JPopupMenu popupMenu = ((JMenu) menuItem).getPopupMenu();
			if (popupMenu.isShowing()) {
				state |= A03ComponentState.SELECTED;
			}
		} else {
			double fadeLevel = fadeTracker.getFadeLevel(menuItem);
			if (fadeLevel > 0) {
				state |= A03ComponentState.ARMED;
			}
		}

		return state;
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
				int state;
				JInternalFrame internalFrame = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, menuItem);
				if (internalFrame != null) {
					state = internalFrame.isSelected() ? getState(menuItem) : 0;
				} else {
			        Window window = SwingUtilities.getWindowAncestor(menuItem);
			        
			        state = window.isActive() ? getState(menuItem) : 0;
				}

				graphics.setPaint(getMenuBarMenuBackgroundPaint(state, A03SwingUtilities.isAnchestorDecorated(c), 0, 0, width, height));

				graphics.fillRect(0, 0, width, height);

				if (popupMenu.isShowing()) {
					Point popupMenuLocation = popupMenu.getLocationOnScreen();
			        
			        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);

			        Shape shape;
					if (menuItemLocation.y < popupMenuLocation.y) { // DOWN
						graphics.setPaint(getBackgroundPaint(state, 2, 1, width - 5, height + 1));
						shape = new Rectangle(2, 1, width - 5, height + 1);
					} else { // UP
						graphics.setPaint(getBackgroundPaint(state, 2, -3, width - 5, height + 2));
						shape = new Rectangle(2, -3, width - 5, height + 2);
					}
	
					graphics.setPaint(getBackgroundPaint(state, 0, 0, width, height));
					graphics.fill(shape);
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

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}

	public FontUIResource getAcceleratorFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_10);
	}

	public void paintAcceleratorText(Component c, Graphics g, String text, int x, int y) {
		int state = getState((JMenuItem) c);
		
		Graphics2D graphics = (Graphics2D) g; //.create();

		graphics.setColor(getAcceleratorColor(state));

		graphics.drawString(text, x, y);

		//graphics.dispose();
	}
	
	public void paintText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g; //.create();

		JMenuItem menuItem = (JMenuItem) c;

		if (menuItem instanceof JMenu) {
			if (menuItem.getParent() instanceof JMenuBar) {
				int state;
				JInternalFrame internalFrame = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, menuItem);
				if (internalFrame != null) {
					state = internalFrame.isSelected() ? getState(menuItem) : 0;
				} else {
			        Window window = SwingUtilities.getWindowAncestor(menuItem);
			        
			        state = window.isActive() ? getState(menuItem) : 0;
				}
				graphics.setColor(getMenuBarMenuForegroundColor(state, A03SwingUtilities.isAnchestorDecorated(c)));
			} else {
				graphics.setColor(getForegroundColor(getState(menuItem)));
			}
		} else {
			graphics.setColor(getForegroundColor(getState(menuItem)));
		}

		int mnemIndex = menuItem.getDisplayedMnemonicIndex();
		A03GraphicsUtilities.drawStringUnderlineCharAt(menuItem, graphics, text, mnemIndex, x, y);
		
		//graphics.dispose();
	}
	
	public int getArrowIconHeight() {
		return 9;
	}
	
	public int getArrowIconWidth() {
		return 9;
	}
	
	public int getCheckBoxIconHeight() {
		return radioButtonMenuItemIcon.getIconHeight();
	}
	
	public int getCheckBoxIconWidth() {
		return radioButtonMenuItemIcon.getIconWidth();
	}
	
	public int getRadioButtonIconHeight() {
		return radioButtonMenuItemIcon.getIconHeight();
	}
	
	public int getRadioButtonIconWidth() {
		return radioButtonMenuItemIcon.getIconWidth();
	}
	
	public void paintArrowIcon(Component c, Graphics g, int x, int y) {
        boolean leftToRight = A03SwingUtilities.isLeftToRight(c);

		int size = 8;

    	A03BasicGraphicsUtilities.paintArrow(g, x, y, size / 2, size, leftToRight ? SwingUtilities.EAST : SwingUtilities.WEST, 
    			Color.BLACK);

	}
	
	public void paintCheckBoxIcon(Component c, Graphics g, int x, int y) {
		int state = A03BasicUtilities.getState(c);
		
		int width = getCheckBoxIconWidth();
		int height = getCheckBoxIconHeight();
		
		if ((state & A03ComponentState.SELECTED) != 0) {
			Graphics2D graphics = (Graphics2D) g;
			A03BasicGraphicsUtilities.paintCheck(graphics,
					getCheckBoxCheckPaint(state, 3, 3, width - 3, height - 3),
					3, 3, width - 3, height - 3);
		}
	}
	
	public void paintRadioButtonIcon(Component c, Graphics g, int x, int y) {
		radioButtonMenuItemIcon.paintIcon(c, g, x, y);
	}

	public Color getAcceleratorColor(int state) {
		return Color.BLACK;
	}

	public Color getBackgroundColor() {
		return Color.WHITE;
	}

	public Color getMenuBarMenuForegroundColor(int state,
			boolean anchestorDecorated) {
		if ((state & (SELECTED | ARMED)) != 0) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}
	
	public Paint getBackgroundPaint(int state, int x, int y, int width,
			int height) {
		if ((state & (SELECTED | ARMED)) != 0) {
			return Color.LIGHT_GRAY;
		} else {
			return Color.WHITE;
		}
	}

	public Paint getMenuBarMenuBackgroundPaint(int state, boolean anchestorDecorated, int x, int y, int width, int height) {
		return Color.BLACK;
	}

	public Color getSelectionBackgroundPaint(int x, int y, int width, int height) {
		return Color.BLACK;
	}

	public Color getForegroundColor(int state) {
		if ((state & ENABLED) != 0) {
			return Color.BLACK;
		} else {
			return Color.GRAY;
		}
	}

	public Paint getRadioBackgroundPaint(int state, int x, int y, int width,
			int height) {
		return Color.BLACK;
	}

	public Paint getRadioCheckPaint(int state, int x, int y, int width,
			int height) {
		return Color.BLACK;
	}

	public Paint getRadioBorderPaint(int state, int x, int y, int width,
			int height) {
		return Color.BLACK;
	}

	public Paint getCheckBoxBackgroundPaint(int state, int x, int y, int width,
			int height) {
		return null;
	}

	public Paint getCheckBoxBorderPaint(int state, int x, int y, int width,
			int height) {
		return null;
	}

	public Paint getCheckBoxCheckPaint(int state, int x, int y, int width,
			int height) {
		return Color.BLACK;
	}
}
