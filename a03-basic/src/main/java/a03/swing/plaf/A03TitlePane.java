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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;



/**
 * @author Kirill Grouchnikov (Substance version)
 * @author Davide Raccagni (A03 version)
 */
public class A03TitlePane extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6091035432229915453L;

	private static final int ICON_SIZE = 16;
	
	/**
	 * PropertyChangeListener added to the JRootPane.
	 */
	private PropertyChangeListener propertyChangeListener;

	/**
	 * JMenuBar, typically renders the system menu items.
	 */
	protected JMenuBar menuBar;

	protected JMenu menu;

	/**
	 * Action used to close the Window.
	 */
	private Action closeAction;

	/**
	 * Action used to iconify the Frame.
	 */
	private Action iconifyAction;

	/**
	 * Action to restore the Frame size.
	 */
	private Action restoreAction;

	/**
	 * Action to restore the Frame size.
	 */
	private Action maximizeAction;

	/**
	 * Button used to maximize or restore the frame.
	 */
	protected JButton toggleButton;

	/**
	 * Button used to minimize the frame
	 */
	protected JButton minimizeButton;

	/**
	 * Button used to close the frame.
	 */
	protected JButton closeButton;

	/**
	 * Listens for changes in the state of the Window listener to update the
	 * state of the widgets.
	 */
	private WindowListener windowListener;

	/**
	 * Window we're currently in.
	 */
	protected Window window;

	/**
	 * JRootPane rendering for.
	 */
	protected JRootPane rootPane;

	/**
	 * Buffered Frame.state property. As state isn't bound, this is kept to
	 * determine when to avoid updating widgets.
	 */
	private int state;
	
	/**
	 * A03RootPaneUI that created us.
	 */
	private A03RootPaneUI rootPaneUI;
	
	private A03RootPaneUIDelegate uiDelegate;

	/**
	 * Creates a new title pane.
	 * 
	 * @param root
	 *            Root pane.
	 * @param ui
	 *            Root pane UI.
	 */
	public A03TitlePane(JRootPane root, A03RootPaneUI ui) {
		this.rootPane = root;
		this.rootPaneUI = ui;

		this.state = -1;
		
    	this.uiDelegate = A03SwingUtilities.getUIDelegate(root, A03RootPaneUIDelegate.class);

		this.installSubcomponents();
		this.installDefaults();
		
		this.setLayout(this.createLayout());

		this.setToolTipText(this.getTitle());
	}
	
	@Override
	protected void setUI(ComponentUI newUI) {
		super.setUI(newUI);
	}
	
	@Override
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		super.setFont(font);
	}

	/**
	 * Uninstalls the necessary state.
	 */
	public void uninstall() {
		this.uninstallListeners();
		this.window = null;
		// Swing bug (?) - the updateComponentTree never gets to the
		// system menu (and in our case we have radio menu items with
		// rollover listeners). Fix for defect 109 - memory leak on theme
		// switch
		if ((this.menuBar != null) && (this.menuBar.getMenuCount() > 0)) {
			this.menuBar.getUI().uninstallUI(this.menuBar);
		}

		if (this.menuBar != null)
			this.menuBar.removeAll();
		this.removeAll();
	}

	/**
	 * Installs the necessary listeners.
	 */
	private void installListeners() {
		if (this.window != null) {
			this.windowListener = new WindowHandler();
			this.window.addWindowListener(this.windowListener);
			this.propertyChangeListener = new PropertyChangeHandler();
			this.window.addPropertyChangeListener(this.propertyChangeListener);
		}
	}

	/**
	 * Uninstalls the necessary listeners.
	 */
	private void uninstallListeners() {
		if (this.window != null) {
			this.window.removeWindowListener(this.windowListener);
			this.windowListener = null;
			this.window
					.removePropertyChangeListener(this.propertyChangeListener);
			this.propertyChangeListener = null;
		}
	}

	/**
	 * Returns the <code>JRootPane</code> this was created for.
	 */
	@Override
	public JRootPane getRootPane() {
		return this.rootPane;
	}

	/**
	 * Returns the decoration style of the <code>JRootPane</code>.
	 * 
	 * @return Decoration style of the <code>JRootPane</code>.
	 */
	protected int getWindowDecorationStyle() {
		return this.rootPane.getWindowDecorationStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#addNotify()
	 */
	@Override
	public void addNotify() {
		super.addNotify();

		this.uninstallListeners();

		this.window = SwingUtilities.getWindowAncestor(this);
		if (this.window != null) {
			if (this.window instanceof Frame) {
				this.setState(((Frame) this.window).getExtendedState());
			} else {
				this.setState(0);
			}
			this.installListeners();
		}
		this.setToolTipText(this.getTitle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#removeNotify()
	 */
	@Override
	public void removeNotify() {
		
		super.removeNotify();

		this.uninstall();
		this.window = null;
	}

	/**
	 * Adds any sub-Components contained in the <code>A03TitlePane</code>.
	 */
	protected void installSubcomponents() {
		int decorationStyle = this.getWindowDecorationStyle();
		if (decorationStyle == JRootPane.FRAME) {
			this.createActions();

			this.menuBar = this.createMenuBar();
			if (this.menuBar != null) {
				this.add(this.menuBar);
			}

			this.minimizeButton = this.createTitleButton(this.iconifyAction);
			add(this.minimizeButton);
			this.toggleButton = this.createTitleButton(this.restoreAction);
			add(this.toggleButton);
			this.closeButton = this.createTitleButton(this.closeAction);
			add(this.closeButton);
		} else if ((decorationStyle == JRootPane.PLAIN_DIALOG)
				|| (decorationStyle == JRootPane.INFORMATION_DIALOG)
				|| (decorationStyle == JRootPane.ERROR_DIALOG)
				|| (decorationStyle == JRootPane.COLOR_CHOOSER_DIALOG)
				|| (decorationStyle == JRootPane.FILE_CHOOSER_DIALOG)
				|| (decorationStyle == JRootPane.QUESTION_DIALOG)
				|| (decorationStyle == JRootPane.WARNING_DIALOG)) {
			this.createActions();

			this.closeButton = this.createTitleButton(this.closeAction);
			add(this.closeButton);
		}
	}

	/**
	 * Installs the fonts and necessary properties.
	 */
	private void installDefaults() {
		this.setFont(uiDelegate.getTitleFont());
	}

	/**
	 * Returns the <code>JMenuBar</code> displaying the appropriate system
	 * menu items.
	 * 
	 * @return <code>JMenuBar</code> displaying the appropriate system menu
	 *         items.
	 */
	protected JMenuBar createMenuBar() {
		if (uiDelegate.isMenuVisible()) {
			this.menuBar = new A03MenuBar();
			this.menuBar.setFocusable(false);
			this.menuBar.setBorderPainted(true);
			this.menuBar.add(this.createMenu());
			this.menuBar.setOpaque(false);
			// support for RTL
			this.menuBar.applyComponentOrientation(this.rootPane
					.getComponentOrientation());
		}
		return this.menuBar;
	}
	
	public JMenu getMenu() {
		return this.menu;
	}

	/**
	 * Create the <code>Action</code>s that get associated with the buttons
	 * and menu items.
	 */
	protected void createActions() {
		this.closeAction = new CloseAction();
		if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
			this.iconifyAction = new IconifyAction();
			this.restoreAction = new RestoreAction();
			this.maximizeAction = new MaximizeAction();
		}
	}
	
	/**
	 * Returns the <code>JMenu</code> displaying the appropriate menu items
	 * for manipulating the Frame.
	 * 
	 * @return <code>JMenu</code> displaying the appropriate menu items for
	 *         manipulating the Frame.
	 */
	private JMenu createMenu() {
		this.menu = new JMenu("");
		this.menu.setOpaque(false);
		this.menu.setBackground(null);
		if (this.getWindowDecorationStyle() == JRootPane.FRAME) {
			this.addMenuItems(this.menu);
		}
		return this.menu;
	}

	/**
	 * Adds the necessary <code>JMenuItem</code>s to the specified menu.
	 * 
	 * @param menu
	 *            Menu.
	 */
	private void addMenuItems(JMenu menu) {
		menu.add(this.restoreAction);

		menu.add(this.iconifyAction);

		if (Toolkit.getDefaultToolkit().isFrameStateSupported(
				Frame.MAXIMIZED_BOTH)) {
			menu.add(this.maximizeAction);
		}

		menu.addSeparator();

		menu.add(this.closeAction);
	}

	/**
	 * Returns a <code>JButton</code> appropriate for placement on the
	 * TitlePane.
	 * 
	 * @return Title button.
	 */
	private JButton createTitleButton(Action action) {
		JButton button = new JButton(action);
		button.setText(null);
		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setOpaque(true);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setBorder(null);
		button.putClientProperty("paintActive", Boolean.TRUE);

		return button;
	}

	/**
	 * Returns the <code>LayoutManager</code> that should be installed on the
	 * <code>A03TitlePane</code>.
	 * 
	 * @return Layout manager.
	 */
	protected LayoutManager createLayout() {
		return new TitlePaneLayout();
	}

	/**
	 * Sets the state of the Window.
	 * 
	 * @param state
	 *            Window state.
	 */
	private void setState(int state) {
		this.setState(state, false);
	}

	/**
	 * Sets the state of the window. If <code>updateRegardless</code> is true
	 * and the state has not changed, this will update anyway.
	 * 
	 * @param state
	 *            Window state.
	 * @param updateRegardless
	 *            if <code>true</code>, the update is done in any case.
	 */
	private void setState(int state, boolean updateRegardless) {
		Window w = this.getWindow();

		if ((w != null) && (this.getWindowDecorationStyle() == JRootPane.FRAME)) {
			if ((this.state == state) && !updateRegardless) {
				return;
			}
			Frame frame = this.getFrame();

			if (frame != null) {
				if (((state & Frame.MAXIMIZED_BOTH) != 0)
						&& ((rootPane.getBorder() == null) || (rootPane
								.getBorder() instanceof UIResource))
						&& frame.isShowing()) {
					rootPane.setBorder(null);
				} else {
					if ((state & Frame.MAXIMIZED_BOTH) == 0) {
						// This is a croak, if state becomes bound, this can
						// be nuked.
						this.rootPaneUI.installBorder(rootPane);
					}
				}
				if (frame.isResizable()) {
					if ((state & Frame.MAXIMIZED_BOTH) != 0) {
						this.updateToggleButton(this.restoreAction); //,
								//theme.getMinimizeIcon());
						this.maximizeAction.setEnabled(false);
						this.restoreAction.setEnabled(true);
					} else {
						this.updateToggleButton(this.maximizeAction); //,
								//theme.getMaximizeIcon());
						this.maximizeAction.setEnabled(true);
						this.restoreAction.setEnabled(false);
					}
					if ((this.toggleButton.getParent() == null)
							|| (this.minimizeButton.getParent() == null)) {
						this.add(this.toggleButton);
						this.add(this.minimizeButton);
						this.revalidate();
						this.repaint();
					}
					this.toggleButton.setText(null);
				} else {
					this.maximizeAction.setEnabled(false);
					this.restoreAction.setEnabled(false);
					if (this.toggleButton.getParent() != null) {
						this.remove(this.toggleButton);
						this.revalidate();
						this.repaint();
					}
				}
			} else {
				// Not contained in a Frame
				this.maximizeAction.setEnabled(false);
				this.restoreAction.setEnabled(false);
				this.iconifyAction.setEnabled(false);
				this.remove(this.toggleButton);
				this.remove(this.minimizeButton);
				this.revalidate();
				this.repaint();
			}
			this.closeAction.setEnabled(true);
			this.state = state;
		}
	}

	/**
	 * Updates the toggle button to contain the Icon <code>icon</code>, and
	 * Action <code>action</code>.
	 * 
	 * @param action
	 *            Action.
	 * @param icon
	 *            Icon.
	 */
	private void updateToggleButton(Action action) { //, Icon icon) {
		this.toggleButton.setAction(action);
		//this.toggleButton.setIcon(icon);
		this.toggleButton.setText(null);
	}

	/**
	 * Returns the Frame rendering in. This will return null if the
	 * <code>JRootPane</code> is not contained in a <code>Frame</code>.
	 * 
	 * @return Frame.
	 */
	private Frame getFrame() {
		Window window = this.getWindow();

		if (window instanceof Frame) {
			return (Frame) window;
		}
		return null;
	}

	/**
	 * Returns the <code>Window</code> the <code>JRootPane</code> is
	 * contained in. This will return null if there is no parent ancestor of the
	 * <code>JRootPane</code>.
	 * 
	 * @return Window.
	 */
	private Window getWindow() {
		return this.window;
	}

	/**
	 * Returns the String to display as the title.
	 * 
	 * @return Display title.
	 */
	protected String getTitle() {
		Window w = this.getWindow();

		if (w instanceof Frame) {
			return ((Frame) w).getTitle();
		}
		if (w instanceof Dialog) {
			return ((Dialog) w).getTitle();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		Font font = getFont();
		FontMetrics fm = rootPane.getFontMetrics(font);
		
		// long start = System.nanoTime();
		// As state isn't bound, we need a convenience place to check
		// if it has changed. Changing the state typically changes the
		if (this.getFrame() != null) {
			this.setState(this.getFrame().getExtendedState());
		}
		Window window = this.getWindow();
		boolean leftToRight = (window == null) ? rootPane
				.getComponentOrientation().isLeftToRight() : window
				.getComponentOrientation().isLeftToRight();

		int width = this.getWidth();
		int height = this.getHeight();

		int xOffset;
		String theTitle = this.getTitle();
		Insets margin = uiDelegate.getTitleMargin();
		int leftEnd;
		int rightEnd;
		if (leftToRight) {
			// offset of border
			xOffset = margin.left;

			leftEnd = getTitleGap();
			xOffset += leftEnd;

			// find the leftmost button for the right end
			AbstractButton leftmostButton = null;

			if ((this.minimizeButton != null)
					&& (this.minimizeButton.getParent() != null)
					&& (this.minimizeButton.getBounds().width != 0)) {
				leftmostButton = this.minimizeButton;
			} else {
				if ((this.toggleButton != null)
						&& (this.toggleButton.getParent() != null)
						&& (this.toggleButton.getBounds().width != 0)) {
					leftmostButton = this.toggleButton;
				} else {
					if ((this.closeButton != null)
							&& (this.closeButton.getParent() != null)) {
						leftmostButton = this.closeButton;
					}
				}
			}

			rightEnd = this.getWidth();
			if (leftmostButton != null) {
				Rectangle rect = leftmostButton.getBounds();
				rightEnd = rect.getBounds().x - 5;
				rightEnd--;
			}

			if (theTitle != null) {
				int titleWidth = rightEnd - leftEnd - 20;
				String clippedTitle = A03GraphicsUtilities.clipString(fm, titleWidth, theTitle);
				// show tooltip with full title only if necessary
				if (theTitle.equals(clippedTitle))
					this.setToolTipText(null);
				else
					this.setToolTipText(theTitle);
				theTitle = clippedTitle;
			}
		} else {
			// RTL support

			xOffset = width - margin.left;

			rightEnd = getTitleGap();

			// find the rightmost button for the left transition band
			AbstractButton rightmostButton = null;

			if ((this.minimizeButton != null)
					&& (this.minimizeButton.getParent() != null)
					&& (this.minimizeButton.getBounds().width != 0)) {
				rightmostButton = this.minimizeButton;
			} else {
				if ((this.toggleButton != null)
						&& (this.toggleButton.getParent() != null)
						&& (this.toggleButton.getBounds().width != 0)) {
					rightmostButton = this.toggleButton;
				} else {
					if ((this.closeButton != null)
							&& (this.closeButton.getParent() != null)) {
						rightmostButton = this.closeButton;
					}
				}
			}

			leftEnd = 5;
			if (rightmostButton != null) {
				Rectangle rect = rightmostButton.getBounds();
				leftEnd = (int) rect.getBounds().getMaxX() + 5;
				leftEnd++;
			}

			if (theTitle != null) {
				int titleWidth = rightEnd - leftEnd - 20;
				String clippedTitle = A03GraphicsUtilities.clipString(fm, titleWidth, theTitle);
				// show tooltip with full title only if necessary
				if (theTitle.equals(clippedTitle)) {
					this.setToolTipText(null);
				} else {
					this.setToolTipText(theTitle);
				}
				theTitle = clippedTitle;
				xOffset = rightEnd - fm.stringWidth(theTitle);
			}
		}

		Graphics2D graphics = (Graphics2D) g.create();
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);

		A03GraphicsUtilities.installDesktopHints(graphics);
		
		graphics.setFont(font);

		// Davide Raccagni {{
		
		paintTitleBackground(rootPane, graphics);
		
		Frame frame = this.getFrame();
		
		// draw the title (if needed)
		if (theTitle != null) {
			int yOffset = ((height - fm.getHeight()) / 2) + fm.getAscent();
			
			if (frame != null) {
				if (((state & Frame.MAXIMIZED_BOTH) != 0)
						&& ((rootPane.getBorder() == null) || (rootPane
								.getBorder() instanceof UIResource))
						&& frame.isShowing()) {
					yOffset += 2;
				}
			}

			paintTitleText(rootPane, graphics, theTitle, xOffset, yOffset);
		}

		// Davide Raccagni }}
		
		graphics.dispose();
	}
	
	protected int getTitleGap() {
		boolean leftToRight = (window == null) ? rootPane
				.getComponentOrientation().isLeftToRight() : window
				.getComponentOrientation().isLeftToRight();
				
		if (leftToRight) {
			return (this.menuBar == null) ? 0
					: (this.menuBar.getWidth() + 10);
		} else {
			int width = this.getWidth();
			
			return (this.menuBar == null) ? width - 5 : width - 5
					- this.menuBar.getWidth() - 10;
		}
	}
	
	public void paintTitleBackground(Component c, Graphics g) {
		uiDelegate.paintTitleBackground(c, g);
	}
	
	public void paintTitleText(Component c, Graphics g, String text, int x,
			int y) {
		uiDelegate.paintTitleText(c, g, text, x, y);
	}


	/**
	 * Actions used to <code>close</code> the <code>Window</code>.
	 */
	private class CloseAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8521842796715771159L;

		/**
		 * Creates a new close action.
		 */
		public CloseAction() {
			super("Close", A03IconFactory.createCloseIcon(uiDelegate));
		}

		public void actionPerformed(ActionEvent e) {
			Window window = A03TitlePane.this.getWindow();

			if (window != null) {
				window.dispatchEvent(new WindowEvent(window,
						WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	/**
	 * Actions used to <code>iconfiy</code> the <code>Frame</code>.
	 */
	private class IconifyAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -4395756725217884481L;

		/**
		 * Creates a new iconify action.
		 */
		public IconifyAction() {
			super("Iconify", A03IconFactory.createIconifyIcon(uiDelegate));
		}

		public void actionPerformed(ActionEvent e) {
			Frame frame = A03TitlePane.this.getFrame();
			if (frame != null) {
				frame.setExtendedState(A03TitlePane.this.state
						| Frame.ICONIFIED);
			}
		}
	}

	/**
	 * Actions used to <code>restore</code> the <code>Frame</code>.
	 */
	private class RestoreAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -504362595084463134L;

		/**
		 * Creates a new restore action.
		 */
		public RestoreAction() {
			super("Restore", A03IconFactory.createMinimizeIcon(uiDelegate));
		}

		public void actionPerformed(ActionEvent e) {
			Frame frame = A03TitlePane.this.getFrame();

			if (frame == null) {
				return;
			}

			if ((A03TitlePane.this.state & Frame.ICONIFIED) != 0) {
				frame.setExtendedState(A03TitlePane.this.state
						& ~Frame.ICONIFIED);
			} else {
				frame.setExtendedState(A03TitlePane.this.state
						& ~Frame.MAXIMIZED_BOTH);
			}
		}
	}

	/**
	 * Actions used to <code>restore</code> the <code>Frame</code>.
	 */
	private class MaximizeAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3579085010778225542L;

		/**
		 * Creates a new maximize action.
		 */
		public MaximizeAction() {
			super("Maximize", A03IconFactory.createMaximizeIcon(uiDelegate));
		}

		public void actionPerformed(ActionEvent e) {
			Frame frame = A03TitlePane.this.getFrame();
			if (frame != null) {
				frame.setExtendedState(A03TitlePane.this.state
						| Frame.MAXIMIZED_BOTH);
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g.create();
		
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);

		A03GraphicsUtilities.installDesktopHints(graphics);

		super.paint(graphics);
		
		graphics.dispose();
	}	

	/**
	 * Class responsible for drawing the system menu. Looks up the image to draw
	 * from the Frame associated with the <code>JRootPane</code>.
	 */
	public class A03MenuBar extends JMenuBar {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2670977328234306661L;

		@Override
		public void paint(Graphics g) {
			Graphics2D graphics = (Graphics2D) g.create();
			
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			Frame frame = A03TitlePane.this.getFrame();

			Image image = (frame != null) ? frame.getIconImage() : null;

			if (image != null) {
				int iSize = ICON_SIZE;
				double coef = Math.max((double) iSize
						/ (double) image.getWidth(null), (double) iSize
						/ (double) image.getHeight(null));
				if (coef < 1.0)
					graphics.drawImage(image, 0, 0,
							(int) (coef * image.getWidth(null)),
							(int) (coef * image.getHeight(null)), null);
				else
					graphics.drawImage(image, 0, 0, null);
			} else {
				Icon icon = UIManager.getIcon("InternalFrame.icon");

				if (icon != null) {
					icon.paintIcon(this, graphics, 0, 0);
				}
			}
			
			graphics.dispose();
		}

		@Override
		public Dimension getMinimumSize() {
			return this.getPreferredSize();
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension size = super.getPreferredSize();

			int iSize = ICON_SIZE;
			return new Dimension(Math.max(iSize, size.width), Math.max(
					size.height, iSize));
		}
	}

	/**
	 * Layout manager for the title pane.
	 * 
	 * @author Kirill Graphics
	 */
	protected class TitlePaneLayout implements LayoutManager {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
		 *      java.awt.Component)
		 */
		public void addLayoutComponent(String name, Component c) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
		 */
		public void removeLayoutComponent(Component c) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
		 */
		public Dimension preferredLayoutSize(Container c) {
			int height = this.computeHeight();
			return new Dimension(height, height);
		}

		/**
		 * Computes title pane height.
		 * 
		 * @return Title pane height.
		 */
		protected int computeHeight() {
			// Davide Raccagni {{
			FontMetrics fm = A03TitlePane.this.rootPane.getFontMetrics(A03TitlePane.this.getFont());
			int height = fm.getHeight();
			if (A03TitlePane.this.getWindowDecorationStyle() == JRootPane.FRAME) {
				height = Math.max(height, ICON_SIZE);
			}
			
			height += uiDelegate.getTitleMargin().top + uiDelegate.getTitleMargin().bottom;

			return height;
			// Davide Raccagni }}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
		 */
		public Dimension minimumLayoutSize(Container c) {
			return this.preferredLayoutSize(c);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 */
		public void layoutContainer(Container c) {
			boolean leftToRight = (A03TitlePane.this.window == null) ? A03TitlePane.this
					.rootPane.getComponentOrientation().isLeftToRight()
					: A03TitlePane.this.window.getComponentOrientation()
							.isLeftToRight();

			int w = A03TitlePane.this.getWidth();
			int buttonHeight;
			int buttonWidth;

			if ((A03TitlePane.this.closeButton != null)
					&& (A03TitlePane.this.closeButton.getIcon() != null)) {
				buttonHeight = A03TitlePane.this.closeButton.getIcon()
						.getIconHeight();
				buttonWidth = A03TitlePane.this.closeButton.getIcon()
						.getIconWidth();
			} else {
				buttonHeight = ICON_SIZE;
				buttonWidth = ICON_SIZE;
			}

			int y = (getHeight() - buttonHeight) / 2;
			
			// assumes all buttons have the same dimensions
			// these dimensions include the borders

			int x = leftToRight ? uiDelegate.getTitleMargin().left : w - buttonWidth - uiDelegate.getTitleMargin().right;
			if (A03TitlePane.this.menuBar != null) {
				A03TitlePane.this.menuBar.setBounds(x, y, buttonWidth,
						buttonHeight);
			}

			x = leftToRight ? w - uiDelegate.getTitleMargin().right - 3 * buttonWidth / 2 : uiDelegate.getTitleMargin().left;
			if (A03TitlePane.this.closeButton != null) {
				A03TitlePane.this.closeButton.setBounds(x, y,
						buttonWidth, buttonHeight);
			}

			x += leftToRight ? - 3 * buttonWidth / 2 : buttonWidth;

			if (A03TitlePane.this.getWindowDecorationStyle() == JRootPane.FRAME) {
				if (Toolkit.getDefaultToolkit().isFrameStateSupported(
						Frame.MAXIMIZED_BOTH)) {
					if (A03TitlePane.this.toggleButton.getParent() != null) {
						A03TitlePane.this.toggleButton.setBounds(x, y,
								buttonWidth, buttonHeight);
						x += leftToRight ? - 3 * buttonWidth / 2 : buttonWidth;
					}
				}

				if ((A03TitlePane.this.minimizeButton != null)
						&& (A03TitlePane.this.minimizeButton.getParent() != null)) {
					A03TitlePane.this.minimizeButton.setBounds(x, y,
							buttonWidth, buttonHeight);
					x += leftToRight ? - 3 * buttonWidth / 2 : buttonWidth;
				}
			}
		}
	}

	/**
	 * PropertyChangeListener installed on the Window. Updates the necessary
	 * state as the state of the Window changes.
	 */
	private class PropertyChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent pce) {
			String name = pce.getPropertyName();

			// Frame.state isn't currently bound.
			if ("resizable".equals(name) || "state".equals(name)) {
				Frame frame = A03TitlePane.this.getFrame();

				if (frame != null) {
					A03TitlePane.this.setState(frame.getExtendedState(),
							true);
				}
				if ("resizable".equals(name)) {
					A03TitlePane.this.rootPane.repaint();
				}
			} else if ("title".equals(name)) {
				A03TitlePane.this.repaint();
				A03TitlePane.this.setToolTipText((String) pce.getNewValue());
			} else if ("componentOrientation".equals(name)
						|| "iconImage".equals(name)) {
				A03TitlePane.this.revalidate();
				A03TitlePane.this.repaint();
			}
		}
	}

	/**
	 * WindowListener installed on the Window, updates the state as necessary.
	 */
	private class WindowHandler extends WindowAdapter {
		@Override
		public void windowActivated(WindowEvent ev) {
			ev.getWindow().repaint();
		}

		@Override
		public void windowDeactivated(WindowEvent ev) {
			ev.getWindow().repaint();
		}
	}
}
