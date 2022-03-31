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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class A03SwingUtilities implements SwingConstants {

	private static transient boolean updatePending;
	
	private static int MIN_MENUITEM_WIDTH = 120;

	public static boolean isLeftToRight(Component c) {
		return c.getComponentOrientation().isLeftToRight();
	}

	public static int loc2IndexFileList(JList list, Point point) {
		int index = list.locationToIndex(point);
		if (index != -1) {
			Object bySize = list.getClientProperty("List.isFileList");
			if (bySize instanceof Boolean && ((Boolean) bySize).booleanValue()
					&& !pointIsInActualBounds(list, index, point)) {
				index = -1;
			}
		}
		return index;
	}

	private static boolean pointIsInActualBounds(JList list, int index,
			Point point) {
		ListCellRenderer renderer = list.getCellRenderer();
		ListModel dataModel = list.getModel();
		Object value = dataModel.getElementAt(index);
		Component item = renderer.getListCellRendererComponent(list, value,
				index, false, false);
		Dimension itemSize = item.getPreferredSize();
		Rectangle cellBounds = list.getCellBounds(index, index);
		if (!isLeftToRight(item)) {
			cellBounds.x += (cellBounds.width - itemSize.width);
		}
		cellBounds.width = itemSize.width;

		return cellBounds.contains(point);
	}

	public static int getMaxIconWidth(Container parent) {
		int width = 0;

		if (parent != null) {
			for (Component component : parent.getComponents()) {
				if (component instanceof JMenuItem) {
					JMenuItem mi = (JMenuItem) component;
					Icon icon = mi.getIcon();
					if (icon != null) {
						width = Math.max(width, icon.getIconWidth());
					}
				}
			}
		}

		return width;
	}

	public static int getMaxIconHeight(Container parent) {
		int height = 0;

		if (parent != null) {
			for (Component component : parent.getComponents()) {
				if (component instanceof JMenuItem) {
					JMenuItem mi = (JMenuItem) component;
					Icon icon = mi.getIcon();
					if (icon != null) {
						height = Math.max(height, icon.getIconHeight());
					}
				}
			}
		}

		return height;
	}

	public static boolean isCheckIconVisible(Container parent) {
		if (parent != null) {
			for (Component component : parent.getComponents()) {
				if (component instanceof JCheckBoxMenuItem
						|| component instanceof JRadioButtonMenuItem) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isArrowIconVisible(Container parent) {
		if (parent != null) {
			for (Component component : parent.getComponents()) {
				if (component instanceof JMenu) {
					return true;
				}
			}
		}

		return false;
	}

	public static Dimension getPreferredMenuItemSize(JMenuItem menuItem,
			Icon checkIcon, Icon arrowIcon, int defaultTextIconGap,
			Font acceleratorFont) {
		
		Insets insets = menuItem.getInsets();

		String acceleratorDelimiter = UIManager
				.getString("MenuItem.acceleratorDelimiter");

		int width = insets.left + checkIcon.getIconWidth() + defaultTextIconGap;

		int height = checkIcon.getIconHeight();

		Container parent = menuItem.getParent();

		int maxIconWidth = getMaxIconWidth(parent);

		if (maxIconWidth > 0) {
			width += maxIconWidth + defaultTextIconGap;
		}

		height = Math.max(height, getMaxIconHeight(parent));

		String text = menuItem.getText();

		FontMetrics fm = menuItem.getFontMetrics(menuItem.getFont());

		int stringWidth = A03GraphicsUtilities.stringWidth(fm, text);
		width += stringWidth + defaultTextIconGap;
		
		height = Math.max(height, fm.getHeight());

		KeyStroke accelerator = menuItem.getAccelerator();
		if (accelerator != null) {
			StringBuilder builder = new StringBuilder();
			int modifiers = accelerator.getModifiers();
			if (modifiers > 0) {
				builder.append(KeyEvent.getKeyModifiersText(modifiers));
				builder.append(acceleratorDelimiter);
			}

			int keyCode = accelerator.getKeyCode();
			if (keyCode != 0) {
				builder.append(KeyEvent.getKeyText(keyCode));
			} else {
				builder.append(accelerator.getKeyChar());
			}

			FontMetrics fmAccel = menuItem.getFontMetrics(acceleratorFont);

			String acceleratorText = builder.toString();

			int accelStringWidth = A03GraphicsUtilities.stringWidth(fmAccel,
					acceleratorText);

			width += accelStringWidth + defaultTextIconGap;

			height = Math.max(height, fmAccel.getHeight());
		}
		
		if (isArrowIconVisible(parent)) {
			int arrowIconWidth = UIManager.getIcon("Menu.arrowIcon")
					.getIconWidth();

			width += arrowIconWidth;
		}
		
		width += insets.right;
		
		width = Math.max(width, MIN_MENUITEM_WIDTH);
		
		height += insets.top + insets.bottom;

		return new Dimension(width, height);
	}

	public static boolean isAnchestorDecorated(Component c) {
		return SwingUtilities.getAncestorOfClass(JInternalFrame.class, c) != null
				|| JFrame.isDefaultLookAndFeelDecorated()
				|| JDialog.isDefaultLookAndFeelDecorated();
	}

	private static void setUpdatePending(boolean update) {
		updatePending = update;
	}

	private static boolean isUpdatePending() {
		return updatePending;
	}

	public static void updateUI() {
		if (!isUpdatePending()) {
			setUpdatePending(true);
			Runnable uiUpdater = new Runnable() {
				public void run() {
					for (Frame frame : Frame.getFrames()) {
						SwingUtilities.updateComponentTreeUI(frame);
						for (Window window : frame.getOwnedWindows()) {
							SwingUtilities.updateComponentTreeUI(window);
						}
					}

					setUpdatePending(false);
				}
			};
			SwingUtilities.invokeLater(uiUpdater);
		}
	}

	public static <T extends A03UIDelegate> T getUIDelegate(JComponent c, Class<T> clazz) {
		T uiDelegate = null;
		
		if (c != null) {
			uiDelegate = (T) c.getClientProperty(A03Constants.UI_DELEGATE);
		}

		if (uiDelegate == null) {
			uiDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(c, clazz);
		}
		
//		if (uiDelegate == null) {
//			System.out.println(c.getUIClassID());
//		}
		
		if (uiDelegate != null && !(uiDelegate instanceof A03UIDelegate)) {
			throw new IllegalArgumentException(uiDelegate.getClass().getName() + " is not an instance of " + A03UIDelegate.class.getName());
		}

		return (T) uiDelegate;
	}
	
	public static void setUIDelegate(JComponent c, A03UIDelegate uiDelegate) {
		c.putClientProperty(A03Constants.UI_DELEGATE, uiDelegate);
		SwingUtilities.updateComponentTreeUI(c);
	}
	
	public static boolean hasCustomUIDelegate(JComponent c) {
		return c.getClientProperty(A03Constants.UI_DELEGATE) != null;
	}

	public static void paintMenuItem(A03MenuItemUIDelegate delegate,
			Graphics2D graphics, JMenuItem menuItem, Icon checkIcon,
			Icon arrowIcon, Font acceleratorFont, int defaultTextIconGap) {
		boolean leftToRight = isLeftToRight(menuItem);

		int menuWidth = menuItem.getWidth();
		int menuHeight = menuItem.getHeight();
		Insets insets = menuItem.getInsets();

		delegate.paintBackground(menuItem, graphics);

		Container parent = menuItem.getParent();

		int x;
		if (leftToRight) {
			x = insets.left;
		} else {
			x = menuWidth - insets.right;
		}

		if (!(parent instanceof JMenuBar)) {
			int checkIconWidth = checkIcon.getIconWidth();

			if (!leftToRight) {
				x -= checkIconWidth;
			}
			
			if (menuItem instanceof JCheckBoxMenuItem
					|| menuItem instanceof JRadioButtonMenuItem) {
				checkIcon.paintIcon(menuItem, graphics, x, (menuHeight - checkIcon
					.getIconHeight()) / 2);
			}

			if (leftToRight) {
				x += checkIconWidth + defaultTextIconGap;
			} else {
				x -= defaultTextIconGap;
			}
		}

		int maxIconWidth = getMaxIconWidth(parent);
		if (maxIconWidth > 0) {
			if (!leftToRight) {
				x -= defaultTextIconGap;
			}
		}

		Icon icon = menuItem.getIcon();

		if (icon != null) {
			icon.paintIcon(menuItem, graphics, x + (maxIconWidth - icon.getIconWidth()) / 2, (menuHeight - icon
					.getIconHeight()) / 2);
		}

		if (maxIconWidth > 0) {
			if (leftToRight) {
				x += maxIconWidth + defaultTextIconGap;
			}
		}

		String text = menuItem.getText();

		FontMetrics fm = menuItem.getFontMetrics(menuItem.getFont());

		int stringWidth = A03GraphicsUtilities.stringWidth(fm, text);

		if (!leftToRight) {
			x -= stringWidth;
		}

		Rectangle textRect = new Rectangle(x, (menuItem.getHeight()
				+ fm.getAscent() - fm.getDescent()) / 2, stringWidth,
				menuHeight - (insets.top + insets.bottom));

		String acceleratorDelimiter = UIManager
				.getString("MenuItem.acceleratorDelimiter");

		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);

		String acceleratorText = "+";
		KeyStroke accelerator = menuItem.getAccelerator();
		if (accelerator != null) {
			int modifiers = accelerator.getModifiers();
			if (modifiers > 0) {
				acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
				acceleratorText += acceleratorDelimiter;
			}

			int keyCode = accelerator.getKeyCode();
			if (keyCode != 0) {
				acceleratorText += KeyEvent.getKeyText(keyCode);
			} else {
				acceleratorText += accelerator.getKeyChar();
			}

			FontMetrics fmAccel = menuItem.getFontMetrics(acceleratorFont);

			int accelStringWidth = A03GraphicsUtilities.stringWidth(fmAccel,
					acceleratorText);

			Rectangle acceleratorRect = new Rectangle(0, 1, accelStringWidth,
					fmAccel.getHeight());

			if (leftToRight) {
				acceleratorRect.x = menuWidth - acceleratorRect.width
						- A03MenuItemUI.ACC_OFFSET - insets.right;
			} else {
				acceleratorRect.x = insets.left + A03MenuItemUI.ACC_OFFSET;
			}

			if (isArrowIconVisible(parent)) {
				int arrowIconWidth = UIManager.getIcon("Menu.arrowIcon")
						.getIconWidth();

				if (leftToRight) {
					acceleratorRect.x -= arrowIconWidth;
				} else {
					acceleratorRect.x += arrowIconWidth;
				}
			}

			Font oldfont = graphics.getFont();
			graphics.setFont(acceleratorFont);

			delegate.paintAcceleratorText(menuItem, graphics, acceleratorText,
					acceleratorRect.x, acceleratorRect.y + fmAccel.getAscent());

			graphics.setFont(oldfont);
		}

		if (menuItem.getParent() instanceof JMenuBar) {
			if (leftToRight) {
				textRect.x += defaultTextIconGap;
			} else {
				textRect.x -= defaultTextIconGap;
			}
		}

		delegate.paintText(menuItem, graphics, text, textRect.x, textRect.y);

		if (arrowIcon != null && !(menuItem.getParent() instanceof JMenuBar)) {
			if (leftToRight) {
				x = menuWidth - arrowIcon.getIconWidth() - insets.right;
			} else {
				x = insets.left;
			}
			arrowIcon.paintIcon(menuItem, graphics, x, textRect.y
					- arrowIcon.getIconHeight()); // (menuHeight -
													// arrowIcon.getIconHeight())
													// / 2);
		}

		delegate.paintBorder(menuItem, graphics, 0, 0, menuWidth, menuHeight);
	}

	public static Dimension getPreferredButtonSize(AbstractButton b,
			int textIconGap) {
		Dimension result;
		boolean toTweakWidth = false;
		boolean toTweakHeight = false;

		Icon icon = b.getIcon();
		String text = b.getText();
		if (
				(
						text == null || 
						text.equals("")
				) && 
				icon == null) {
			text = " ";
		}

		Font font = b.getFont();
		FontMetrics fm = b.getFontMetrics(font);

		Rectangle iconR = new Rectangle();
		Rectangle textR = new Rectangle();
		Rectangle viewR = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);

		SwingUtilities.layoutCompoundLabel(b, fm, text, icon, b
				.getVerticalAlignment(), b.getHorizontalAlignment(), b
				.getVerticalTextPosition(), b.getHorizontalTextPosition(),
				viewR, iconR, textR, (text == null ? 0 : b.getIconTextGap()));

		Rectangle r = iconR.union(textR);

		Insets insets = b.getInsets();
		r.width += insets.left + insets.right;
		r.height += insets.top + insets.bottom;
		
		Dimension baseDimension = r.getSize();
		result = baseDimension;

		if ((text != null) && (text.length() > 0)) {
			int baseWidth = baseDimension.width;
			baseWidth = Math.max(baseWidth, getMinButtonWidth(b.getFont().getSize()));
			result = new Dimension(baseWidth, baseDimension.height);
			int baseHeight = result.height;
			baseHeight = Math.max(baseHeight, getMinButtonHeight(b.getFont().getSize()));
			result = new Dimension(result.width, baseHeight);
		}

		if (icon != null) {
			// check the icon height
			int iconHeight = icon.getIconHeight();
			if (iconHeight > (result.getHeight() - 6)) {
				result = new Dimension(result.width, iconHeight);
				toTweakHeight = true;
			}
			int iconWidth = icon.getIconWidth();
			if (iconWidth > (result.getWidth() - 6)) {
				result = new Dimension(iconWidth, result.height);
				toTweakWidth = true;
			}
		}

		if (toTweakWidth) {
			result = new Dimension(result.width + 6, result.height);
		}
		if (toTweakHeight) {
			result = new Dimension(result.width, result.height + 6);
		}

		return result;
	}

	/*
	 * org.jvnet.substance.utils.SubstanceSizeUtils
	 */
	
	/**
	 * Returns the minimum button height under the specified font size.
	 * 
	 * @param fontSize
	 * 		Font size.
	 * @return Minimum button height under the specified font size.
	 */
	public static int getMinButtonHeight(int fontSize) {
		return 11 + (fontSize - 5) * 3 / 2;
	}

	/**
	 * Returns the maximum button height under the specified font size.
	 * 
	 * @param fontSize
	 * 		Font size.
	 * @return Maximum button height under the specified font size.
	 */
	public static int getMinButtonWidth(int fontSize) {
		return getMinButtonHeight(fontSize);
	}

	public static void performEdtViolationCheck(Component comp) {
		if (!SwingUtilities.isEventDispatchThread()) {
			RuntimeException threadingViolationError = new RuntimeException(
					"Operation to be done on Event Dispatch Thread");
			threadingViolationError.printStackTrace(System.err);
			throw threadingViolationError;
		}
	}
}
