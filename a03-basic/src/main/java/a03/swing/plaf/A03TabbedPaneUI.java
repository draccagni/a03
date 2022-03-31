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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;


/**
 * UI for tabbed panes in <b>Substance</b> look and feel.
 * 
 * @author Davide Raccagni
 * @author Kirill Grouchnikov
 */
public class A03TabbedPaneUI extends BasicTabbedPaneUI {

	private static Field tabScrollerField = null;
	private static Field viewportField = null;
	
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03TabbedPaneUI();
	}

	private A03TabbedPaneUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		uiDelegate = A03SwingUtilities.getUIDelegate(c, A03TabbedPaneUIDelegate.class);

		super.installUI(c);
		
        tabInsets = uiDelegate.getTabInsets();
        tabAreaInsets = uiDelegate.getTabAreaInsets();
        contentBorderInsets = uiDelegate.getContentInsets();
        tabPane.setOpaque(false);

        tabPane.setFont(uiDelegate.getFont());
	}
	
	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement,
			int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		uiDelegate.paintTabBackground(g, tabPane, tabPlacement, tabIndex, x, y, w, h, isSelected);
	}


	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement,
			Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect, boolean isSelected) {
		// TODO
	}

	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) {

		uiDelegate.paintTabBorder(g, tabPane, tabPlacement, tabIndex, x, y, w, h, isSelected);
	}

	@Override
	protected JButton createScrollButton(int direction) {
		if (direction != SOUTH && direction != NORTH && direction != EAST && direction != WEST) {
			throw new IllegalArgumentException("Direction must be one of: " + "SOUTH, NORTH, EAST or WEST");
		}
		return new A03ArrowButton(direction, uiDelegate);
	}

	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		boolean toSwap = tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT;
		if (toSwap) {
			return super.calculateTabWidth(tabPlacement, tabIndex, this.getFontMetrics());
		}
		return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
	}

	@Override
	protected int calculateTabWidth(int tabPlacement, int tabIndex,
			FontMetrics metrics) {
		boolean toSwap = tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT;
		if (toSwap)
			return super.calculateTabHeight(tabPlacement, tabIndex, metrics
					.getHeight());
		int result = /* this.getTabExtraWidth(tabPlacement, tabIndex)
				+ */ super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		return result;
	}

	@Override
	protected int calculateMaxTabHeight(int tabPlacement) {
		if ((tabPlacement == SwingConstants.TOP)
				|| (tabPlacement == SwingConstants.BOTTOM))
			return super.calculateMaxTabHeight(tabPlacement);
		int result = 0;
		for (int i = 0; i < this.tabPane.getTabCount(); i++)
			result = Math.max(result, this.calculateTabHeight(tabPlacement, i,
					this.getFontMetrics().getHeight()));
		return result;
	}

	@Override
	protected int getTabRunOverlay(int tabPlacement) {
		boolean toSwap = tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT;
		if (!toSwap)
			return super.getTabRunOverlay(tabPlacement);

		return 0;
	}

	@Override
	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects,
			int tabIndex, Rectangle iconRect, Rectangle textRect) {
		boolean toSwap = tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT;
		if (toSwap) {
			if (A03Constants.IS_JAVA_6_OR_LATER) {
				if (tabPane.getTabComponentAt(tabIndex) != null) {
					throw new IllegalComponentStateException("A03 look and feel does not support custom tab components on left and right placement.");
				}
			}
			
			Graphics2D tempG = (Graphics2D) g.create();
			Rectangle tabRect = rects[tabIndex];
			Rectangle correctRect = new Rectangle(tabRect.x, tabRect.y,	tabRect.height, tabRect.width);
			if (tabPlacement == SwingConstants.LEFT) {
				// rotate 90 degrees counterclockwise for LEFT orientation
				tempG.rotate(-Math.PI / 2, tabRect.x, tabRect.y);
				tempG.translate(-tabRect.height, 0);
			} else {
				// rotate 90 degrees clockwise for RIGHT orientation
				tempG.rotate(Math.PI / 2, tabRect.x, tabRect.y);
				tempG.translate(0, -tabRect.getWidth());
			}
			tempG.setColor(Color.red);
			rects[tabIndex] = correctRect;
			super.paintTab(tempG, tabPlacement, rects, tabIndex, iconRect, textRect);
			rects[tabIndex] = tabRect;
			tempG.dispose();
		} else {
			super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
		}
	}

	@Override
	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		int width;
		int height;
		if (tabPane.getTabLayoutPolicy() == JTabbedPane.WRAP_TAB_LAYOUT) {
			switch (tabPlacement) {
			case TOP:
				width = tabPane.getWidth();
				height = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				break;
			case LEFT:
				width = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				height = tabPane.getHeight();
				break;
			case BOTTOM:
				width = tabPane.getWidth();
				height = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
				break;
			default: // case RIGHT:
				width = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
				height = tabPane.getHeight();
				break;
			}
		} else {
			JViewport viewport = (JViewport) getTabScrollerViewport(this);

			Component view = viewport.getView();
			Dimension size = view.getSize();
			width = size.width;
			height = size.height;
		}

		uiDelegate.paintTabAreaBackground(g, tabPane, width, height);
		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	private static JViewport getTabScrollerViewport(TabbedPaneUI tabbedPaneUI) {
		try {
			if (tabScrollerField == null) {
				tabScrollerField = BasicTabbedPaneUI.class.getDeclaredField("tabScroller");
				tabScrollerField.setAccessible(true);
			}

			Object tabScroller = tabScrollerField.get(tabbedPaneUI);

			if (viewportField == null) {
				viewportField = tabScroller.getClass().getDeclaredField("viewport");
				viewportField.setAccessible(true);
			}

			return (JViewport) viewportField.get(tabScroller);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void ensureCurrentLayout() {
		if (!this.tabPane.isValid()) {
			this.tabPane.validate();
		}
		/*
		 * If tabPane doesn't have a peer yet, the validate() call will silently
		 * fail. We handle that by forcing a layout if tabPane is still invalid.
		 * See bug 4237677.
		 */
		if (!this.tabPane.isValid()) {
			LayoutManager lm = this.tabPane.getLayout();

			BasicTabbedPaneUI.TabbedPaneLayout layout = (BasicTabbedPaneUI.TabbedPaneLayout) lm;
			layout.calculateLayoutInfo();
		}
	}

	@Override
	protected int getTabLabelShiftX(int tabPlacement, int tabIndex,	boolean isSelected) {
		return 0;
	}

	@Override
	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
		return 0;
	}
	
	public Rectangle getTabRectangle(int tabIndex) {
		return this.rects[tabIndex];
	}

	@Override
	protected boolean shouldPadTabRun(int tabPlacement, int run) {
		return this.runCount > 1 && run < this.runCount - 1;
	}

	@Override
	protected LayoutManager createLayoutManager() {
		if (this.tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
			return super.createLayoutManager();
		}
		return new TabbedPaneLayout();
	}

	/**
	 * Layout for the tabbed pane.
	 * 
	 * @author Kirill Grouchnikov
	 */
	public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
		/**
		 * Creates a new layout.
		 */
		public TabbedPaneLayout() {
			A03TabbedPaneUI.this.super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.plaf.basic.BasicTabbedPaneUI$TabbedPaneLayout#normalizeTabRuns(int,
		 *      int, int, int)
		 */
		@Override
		protected void normalizeTabRuns(int tabPlacement, int tabCount,
				int start, int max) {
			// Only normalize the runs for top & bottom; normalizing
			// doesn't look right for Metal's vertical tabs
			// because the last run isn't padded and it looks odd to have
			// fat tabs in the first vertical runs, but slimmer ones in the
			// last (this effect isn't noticeable for horizontal tabs).
			if (tabPlacement == TOP || tabPlacement == BOTTOM) {
				super.normalizeTabRuns(tabPlacement, tabCount, start, max);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.plaf.basic.BasicTabbedPaneUI$TabbedPaneLayout#padSelectedTab(int,
		 *      int)
		 */
		@Override
		protected void padSelectedTab(int tabPlacement, int selectedIndex) {
			// Don't pad selected tab
		}
	}

    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        int width = tabPane.getWidth();
        int height = tabPane.getHeight();
        Insets insets = tabPane.getInsets();
        Insets tabAreaInsets = getTabAreaInsets(tabPlacement);

        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;

		boolean tabsOverlapBorder = UIManager.getBoolean("TabbedPane.tabsOverlapBorder");

        switch (tabPlacement) {
		case LEFT:
			x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
			if (tabsOverlapBorder) {
				x -= tabAreaInsets.right;
			}
			w -= (x - insets.left);
			break;
		case RIGHT:
			w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
			if (tabsOverlapBorder) {
				w += tabAreaInsets.left;
			}
			break;
		case BOTTOM:
			h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
			if (tabsOverlapBorder) {
				h += tabAreaInsets.top;
			}
			break;
		case TOP:
		default:
			y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
			if (tabsOverlapBorder) {
				y -= tabAreaInsets.bottom;
			}
			h -= (y - insets.top);
		} 

		boolean contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
        
        if ( tabPane.getTabCount() > 0 && (contentOpaque || tabPane.isOpaque()) ) {
            Color color = uiDelegate.getContentAreaBackground();
            g.setColor(color);
            g.fillRect(x, y, w, h);
        }

		Rectangle selRect = selectedIndex < 0 ? null : this.getTabBounds(selectedIndex, this.calcRect);

		uiDelegate.paintContentBorder(g, tabPane, tabPlacement, selectedIndex, x, y, w, h, selRect);
    }
    
	@Override
	public Rectangle getTabBounds(JTabbedPane pane, int i) {
		this.ensureCurrentLayout();
		Rectangle tabRect = new Rectangle();
		return this.getTabBounds(i, tabRect);
	}

	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font,
			FontMetrics metrics, int tabIndex, String title,
			Rectangle textRect, boolean isSelected) {
		g.setFont(font);

		Graphics2D graphics = (Graphics2D) g.create();

		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);

		View v = this.getTextViewForTab(tabIndex);
		if (v != null) {
			// html
			v.paint(graphics, textRect);
		} else {
			int x = textRect.x;
			int y = textRect.y + metrics.getAscent();
			graphics.setFont(font);
			
			uiDelegate.paintTabText(graphics, tabPane, tabPlacement, tabIndex, title, x, y, isSelected);
		}
		
		graphics.dispose();
	}

	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();

//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);

		paint(graphics, c);

		graphics.dispose();
	}
}