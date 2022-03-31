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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;

public class A03BorderFactory {

	public static Border createButtonBorder() {
    	return new BorderUIResource.CompoundBorderUIResource(new UIDelegatedBorder(null), new ButtonMarginBorder());
	}
	
	public static Border createArrowButtonDelegatedBorder(A03ArrowButtonUIDelegate uiDelegate) {
    	return new BorderUIResource.CompoundBorderUIResource(new ArrowButtonDelegatedBorder(uiDelegate), new ArrowButtonMarginBorder(uiDelegate));
	}

	public static Border createDelegatedBorder(A03BorderUIDelegate uiDelegate) {
    	return new UIDelegatedBorder(uiDelegate);
	}

    public static Border createSplitPaneDividerBorder(A03SplitPaneUIDelegate uiDelegate) {
    	return new SplitPaneDividerBorder(uiDelegate);
    }

	public static Border createScrollPaneTableBorder(A03TableUIDelegate uiDelegate) {
		return new ScrollPaneTableBorder(uiDelegate);
	}
	
	public static Border createTableFocusSelectedCellHighlightBorder(A03TableUIDelegate uiDelegate) {
		return new TableFocusSelectedCellHighlightBorder(uiDelegate);
	}

	public static Border createListFocusCellHighlightBorder(A03ListUIDelegate uiDelegate) {
		return new ListFocusCellHighlightBorder(uiDelegate);
	}
	
	public static Border createListNoFocusBorder(A03ListUIDelegate uiDelegate) {
		return new ListNoFocusBorder(uiDelegate);
	}

	public static Border createToolBarNonRolloverToggleBorder(A03ToolBarUIDelegate uiDelegate) {
		return new BorderUIResource.CompoundBorderUIResource(new ToolBarButtonBorder(), new ButtonMarginBorder());
	}
	
	public static Border createToolBarNonRolloverBorder(A03ToolBarUIDelegate uiDelegate) {
		return new BorderUIResource.CompoundBorderUIResource(new ToolBarButtonBorder(), new ButtonMarginBorder());
	}
	
	public static Border createScrollPaneViewportBorder(A03ScrollPaneUIDelegate uiDelegate) {
		return new ScrollPaneViewportBorder(uiDelegate);
	}
	
	public static Border createOptionPaneMessageAreaBorder(A03OptionPaneUIDelegate uiDelegate) {
		return new OptionPaneMessageAreaBorder(uiDelegate);
	}
	
	public static Border createOptionPaneButtonAreaBorder(A03OptionPaneUIDelegate uiDelegate) {
		return new OptionPaneButtonAreaBorder(uiDelegate);
	}

	public static Border createTitledBorder(A03TitledBorderUIDelegate uiDelegate) {
		return new TitledBorder(uiDelegate);
	}

	public static Border createTableHeaderCellBorder(A03TableHeaderUIDelegate uiDelegate) {
		return new BorderUIResource.CompoundBorderUIResource(new UIDelegatedBorder(uiDelegate), new TableHeaderMarginBorder());
	}

	public static Border createTextComponentBorder(A03TextComponentUIDelegate uiDelegate) {
		return new BorderUIResource.CompoundBorderUIResource(new UIDelegatedBorder(uiDelegate), new TextComponentMarginBorder());
	}
	
	public static Border createRootPaneBorder() {
		return new RootPaneBorder();
	}

	static class OptionPaneMessageAreaBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2289397920006615878L;
		
		private A03OptionPaneUIDelegate uiDelegate;
		
		public OptionPaneMessageAreaBorder(A03OptionPaneUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			uiDelegate.paintMessageAreaBorder(c, g, x, y, width, height);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getMessageAreaBorderInsets(c, insets);
		}
	}
	

	static class OptionPaneButtonAreaBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7734699072720898323L;
		
		private A03OptionPaneUIDelegate uiDelegate;
		
		public OptionPaneButtonAreaBorder(A03OptionPaneUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			uiDelegate.paintButtonAreaBorder(c, g, x, y, width, height);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getButtonAreaBorderInsets(c, insets);
		}
	}
	

	public static class UIDelegatedBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6765602694989528273L;
		
		private A03BorderUIDelegate uiDelegate;
		
		public UIDelegatedBorder(A03BorderUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			if (uiDelegate == null) {
				uiDelegate = A03SwingUtilities.getUIDelegate((JComponent) c, A03BorderUIDelegate.class);
			}
			if (uiDelegate != null) {
				Insets borderInsets = uiDelegate.getBorderInsets(c, insets);
				
				if (borderInsets != null) {
					insets.top = borderInsets.top;
					insets.left = borderInsets.left;
					insets.bottom = borderInsets.bottom;
					insets.right = borderInsets.right;
				}
			}

			return insets;
		}
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			if (uiDelegate == null) {
				uiDelegate = A03SwingUtilities.getUIDelegate((JComponent) c, A03BorderUIDelegate.class);
			}
			if (uiDelegate != null) {
				uiDelegate.paintBorder(c, g, x, y, width, height);
			}
		}
	}

	public static class RootPaneBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6765602694989528273L;
				
		public RootPaneBorder() {
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			A03BorderUIDelegate uiDelegate = A03SwingUtilities.getUIDelegate((JComponent) c, A03RootPaneUIDelegate.class);
			Insets borderInsets = uiDelegate.getBorderInsets(c, insets);
			
			if (borderInsets != null) {
				insets.top = borderInsets.top;
				insets.left = borderInsets.left;
				insets.bottom = borderInsets.bottom;
				insets.right = borderInsets.right;
			}

			return insets;
		}
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			A03BorderUIDelegate uiDelegate = A03SwingUtilities.getUIDelegate((JComponent) c, A03RootPaneUIDelegate.class);
			uiDelegate.paintBorder(c, g, x, y, width, height);
		}
	}

	public static class ButtonMarginBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3010554796378299044L;

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			AbstractButton b = (AbstractButton) c;
			
			Insets margin;
			
			JToolBar toolBar = (JToolBar) SwingUtilities.getAncestorOfClass(JToolBar.class, c);
			if (toolBar != null) {
				A03ToolBarUIDelegate toolBarUIDelegate = A03SwingUtilities.getUIDelegate(toolBar, A03ToolBarUIDelegate.class);
				margin = toolBarUIDelegate.getMargin();
			} else {
				margin = b.getMargin();
			}
			
			insets.top = margin.top;
			insets.left = margin.left;
			insets.bottom = margin.bottom;
			insets.right = margin.right;

			return insets;
		}
	}

	public static class TableHeaderMarginBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3178995686179477126L;

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			Insets margin = UIManager.getInsets("TableHeader.margin");
			
			if (margin != null) {
				insets.top = margin.top;
				insets.left = margin.left;
				insets.bottom = margin.bottom;
				insets.right = margin.right;
			}

			return insets;
		}
	}

	public static class ArrowButtonMarginBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8977008175382851286L;
		
		private A03ArrowButtonUIDelegate uiDelegate;
		
		public ArrowButtonMarginBorder(A03ArrowButtonUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			Insets margin = uiDelegate.getArrowMargin();
			
			insets.top = margin.top;
			insets.left = margin.left;
			insets.bottom = margin.bottom;
			insets.right = margin.right;

			return insets;
		}
	}

	public static class ArrowButtonDelegatedBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6765602694989528273L;
		
		private A03ArrowButtonUIDelegate uiDelegate;
		
		public ArrowButtonDelegatedBorder(A03ArrowButtonUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			Insets borderInsets = uiDelegate.getArrowBorderInsets(c, insets);
			
			if (borderInsets != null) {
				insets.top = borderInsets.top;
				insets.left = borderInsets.left;
				insets.bottom = borderInsets.bottom;
				insets.right = borderInsets.right;
			}

			return insets;
		}
		
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			uiDelegate.paintArrowBorder(c, g, x, y, width, height);
		}
	}

	static class ScrollPaneTableBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7968141264398216456L;
		
		private A03TableUIDelegate uiDelegate;
		
		public ScrollPaneTableBorder(A03TableUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}
		
		public Insets getBorderInsets(Component c) {
			return uiDelegate.getScrollPaneBorderInsets(c, new Insets(0, 0, 0, 0));
		}
		
		public boolean isBorderOpaque() {
			return true;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			uiDelegate.paintScrollPaneBorder(c, g, x, y, width, height);
		}
	}
	
	static class TableFocusSelectedCellHighlightBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7968141264398216456L;
		
		private A03TableUIDelegate uiDelegate;
		
		public TableFocusSelectedCellHighlightBorder(A03TableUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}
		
		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}
		
		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			insets.top = 2;
			insets.left = 1;
			insets.right = 2;
			insets.bottom = 1;

			return insets;
		}
		
		public boolean isBorderOpaque() {
			return true;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			// do nothing
		}
	}
	
	static class ListFocusCellHighlightBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7968141264398216456L;
		
		private A03ListUIDelegate uiDelegate;
		
		public ListFocusCellHighlightBorder(A03ListUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}
		
		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}
		
		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getFocusBorderInsets(c, insets);
		}
		
		public boolean isBorderOpaque() {
			return true;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			uiDelegate.paintFocusBorder(c, g, x, y, width, height);
		}
	}
	
	static class ListNoFocusBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7968141264398216456L;
		
		private A03ListUIDelegate uiDelegate;
		
		public ListNoFocusBorder(A03ListUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}
		
		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}
		
		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getNoFocusBorderInsets(c, insets);
		}
		
		public boolean isBorderOpaque() {
			return true;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			uiDelegate.paintNoFocusBorder(c, g, x, y, width, height);
		}
	}
	
	static class SplitPaneDividerBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7698765249749822588L;

		private A03SplitPaneUIDelegate uiDelegate;
		
		public SplitPaneDividerBorder(A03SplitPaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			uiDelegate.paintDividerBorder(c, g, x, y, width, height);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getDividerBorderInsets(c, insets);
		}
	}
	
	static class TextComponentMarginBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3010554796378299044L;

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			/*
			 * http://www.javalobby.org/forums/thread.jspa?messageID=92129299&#92129299
			 */
			if (c instanceof JTextComponent) {
				Insets margin = ((JTextComponent) c).getMargin();

				if (margin != null) {
					insets.top = margin.top;
					insets.left = margin.left;
					insets.bottom = margin.bottom;
					insets.right = margin.right;
				}
			} else {
				insets.top = 0;
				insets.left = 0;
				insets.bottom = 0;
				insets.right = 0;			
			}
			
			return insets;
		}
	}
	
	static class ScrollPaneViewportBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7968141264398216456L;
		
		private A03ScrollPaneUIDelegate uiDelegate;
		
		public ScrollPaneViewportBorder(A03ScrollPaneUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}
		
		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}
		
		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getViewportBorderInsets(c, insets);
		}
		
		public boolean isBorderOpaque() {
			return true;
		}
		
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			uiDelegate.paintViewportBorder(c, g, x, y, width, height);
		}
	}

	static class ToolBarMarginBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3345689612176994793L;
		
		private A03ToolBarUIDelegate uiDelegate;
		
		public ToolBarMarginBorder(A03ToolBarUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			Insets margin = uiDelegate.getMargin();
			
			if (margin != null) {
				insets.top = margin.top;
				insets.left = margin.left;
				insets.bottom = margin.bottom;
				insets.right = margin.right;
			}

			return insets;
		}
	}

	static class TitledBorder extends AbstractBorder implements UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2670058652954901675L;
		
		private A03TitledBorderUIDelegate uiDelegate;
		
		public TitledBorder(A03TitledBorderUIDelegate uiDelegate) {
			this.uiDelegate = uiDelegate;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//			// XXX This is a tricky to configure text anti-aliasing for the Title Pane Border
//			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
//					RenderingHints.VALUE_ANTIALIAS_ON);

			A03GraphicsUtilities.installDesktopHints(g);
			
			uiDelegate.paintBorder(c, g, x, y, width, height);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return uiDelegate.getBorderInsets(c, insets);
		}
	}

	static class ToolBarButtonBorder extends AbstractBorder implements UIResource {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -9026128254901451163L;

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return insets;
		}
	}
}
