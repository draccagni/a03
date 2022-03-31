/*
 * Copyright (c) 2003-2008 Davide Raccagni. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Davide Raccagni nor the names of 
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
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

import a03.swing.plugin.A03UIPluginManager;

/**
 * @author Kirill Grouchnikov (Substance version)
 * @author Larry Salibra (fix for defect 198)
 * @author Davide Raccagni (A03 version)
 */
public class A03RootPaneUI extends BasicRootPaneUI {
	/**
	 * The amount of space (in pixels) that the cursor is changed on.
	 */
	//static final int CORNER_DRAG_WIDTH = 14;

	/**
	 * Region from edges that dragging is active from.
	 */
//	public static final int BORDER_DRAG_THICKNESS = 5;

	/**
	 * Window the <code>JRootPane</code> is in.
	 */
	private Window window;

	/**
	 * <code>JComponent</code> providing window decorations. This will be null
	 * if not providing window decorations.
	 */
	private JComponent titlePane;

	/**
	 * <code>MouseInputListener</code> that is added to the parent
	 * <code>Window</code> the <code>JRootPane</code> is contained in.
	 */
	private MouseInputListener mouseInputListener;

	/**
	 * Mouse listener on the title pane (dragging).
	 */
	private MouseInputListener titleMouseInputHandler;

	/**
	 * The <code>LayoutManager</code> that is set on the
	 * <code>JRootPane</code>.
	 */
	private LayoutManager layoutManager;

	/**
	 * <code>LayoutManager</code> of the <code>JRootPane</code> before we
	 * replaced it.
	 */
	private LayoutManager savedOldLayout;

	/**
	 * <code>JRootPane</code> providing the look and feel for.
	 */
	protected JRootPane root;

	/**
	 * The current window.
	 */
	protected Window currentWindow;

	protected Dimension currentSize;
	
	
	/**
	 * Hierarchy listener to keep track of the associated top-level window.
	 */
	protected HierarchyListener hierarchyListener;

	/**
	 * Component listener to keep track of the primary graphics configuration
	 * (for recomputing the maximized bounds) - fix for defect 213.
	 */
	protected ComponentListener windowComponentListener;

	/**
	 * The graphics configuration that contains the top-left corner of the
	 * window (fix for defect 213).
	 */
	protected GraphicsConfiguration currentRootPaneGC;

	/**
	 * <code>Cursor</code> used to track the cursor set by the user. This is
	 * initially <code>Cursor.DEFAULT_CURSOR</code>.
	 */
	private Cursor lastCursor = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Stores the pointer to {@link MouseInfo#getLocationOnScreen()} method
	 * (available only under JDK 6.0). Is used to provide fix for defect 198.
	 */
	protected static Method getLocationOnScreenMethod;
	
	protected WindowListener windowListener;

	static {
		try {
			getLocationOnScreenMethod = MouseEvent.class.getDeclaredMethod(
					"getLocationOnScreen", new Class[0]);
		} catch (Exception e) {
			// running under JDK 5.0 or sandbox
			getLocationOnScreenMethod = null;
		}
	}

	/**
	 * Creates a UI for a <code>JRootPane</code>.
	 * 
	 * @param c
	 *            the JRootPane the RootPaneUI will be created for
	 * @return the RootPaneUI implementation for the passed in JRootPane
	 */
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03RootPaneUI();
	}

	private A03RootPaneUIDelegate uiDelegate;
	
	/**
	 * Invokes supers implementation of <code>installUI</code> to install the
	 * necessary state onto the passed in <code>JRootPane</code> to render the
	 * metal look and feel implementation of <code>RootPaneUI</code>. If the
	 * <code>windowDecorationStyle</code> property of the
	 * <code>JRootPane</code> is other than <code>JRootPane.NONE</code>,
	 * this will add a custom <code>Component</code> to render the widgets to
	 * <code>JRootPane</code>, as well as installing a custom
	 * <code>Border</code> and <code>LayoutManager</code> on the
	 * <code>JRootPane</code>.
	 * 
	 * @param c
	 *            the JRootPane to install state onto
	 */
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03RootPaneUIDelegate.class);
		
		super.installUI(c);
		this.root = (JRootPane) c;
		int style = this.root.getWindowDecorationStyle();
		if (style != JRootPane.NONE) {
			this.installClientDecorations(this.root);
		}
	}

	/**
	 * Invokes supers implementation to uninstall any of its state. This will
	 * also reset the <code>LayoutManager</code> of the <code>JRootPane</code>.
	 * If a <code>Component</code> has been added to the
	 * <code>JRootPane</code> to render the window decoration style, this
	 * method will remove it. Similarly, this will revert the Border and
	 * LayoutManager of the <code>JRootPane</code> to what it was before
	 * <code>installUI</code> was invoked.
	 * 
	 * @param c
	 *            the JRootPane to uninstall state from
	 */
	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		this.uninstallClientDecorations(this.root);

		this.layoutManager = null;
		this.mouseInputListener = null;

		this.root = null;
	}

	/**
	 * Installs the appropriate <code>Border</code> onto the
	 * <code>JRootPane</code>.
	 * 
	 * @param root
	 *            Root pane.
	 */
	public void installBorder(JRootPane root) {
		int style = root.getWindowDecorationStyle();

		if (style == JRootPane.NONE) {
			LookAndFeel.uninstallBorder(root);
		} else {
			LookAndFeel.installBorder(root, "RootPane.border");
		}
	}

	/**
	 * Removes any border that may have been installed.
	 * 
	 * @param root
	 *            Root pane.
	 */
	private void uninstallBorder(JRootPane root) {
		LookAndFeel.uninstallBorder(root);
	}

	/**
	 * Installs the necessary Listeners on the parent <code>Window</code>, if
	 * there is one.
	 * <p>
	 * This takes the parent so that cleanup can be done from
	 * <code>removeNotify</code>, at which point the parent hasn't been reset
	 * yet.
	 * 
	 * 
	 * @param root
	 *            Root pane.
	 * @param parent
	 *            The parent of the JRootPane
	 */
	private void installWindowListeners(JRootPane root, Component parent) {
		if (parent instanceof Window) {
			this.window = (Window) parent;
		} else {
			this.window = SwingUtilities.getWindowAncestor(parent);
		}

		if (this.window != null) {
//			AWTUtilities.setWindowOpaque(window, false);

			if (this.mouseInputListener == null) {
				this.mouseInputListener = this
						.createWindowMouseInputListener(root);
			}
			this.window.addMouseListener(this.mouseInputListener);
			this.window
					.addMouseMotionListener(this.mouseInputListener);

			if (this.titlePane != null) {
				if (this.titleMouseInputHandler == null) {
					this.titleMouseInputHandler = new TitleMouseInputHandler();
				}
				this.titlePane
						.addMouseMotionListener(this.titleMouseInputHandler);
				this.titlePane
						.addMouseListener(this.titleMouseInputHandler);
			}
			this.setMaximized();
		}
	}

	/**
	 * Uninstalls the necessary Listeners on the <code>Window</code> the
	 * Listeners were last installed on.
	 * 
	 * @param root
	 *            Root pane.
	 */
	private void uninstallWindowListeners(JRootPane root) {
		if (this.window != null) {
			this.window.removeMouseListener(this.mouseInputListener);
			this.window
					.removeMouseMotionListener(this.mouseInputListener);
		}
		if (this.titlePane != null) {
			this.titlePane
					.removeMouseListener(this.titleMouseInputHandler);
			this.titlePane
					.removeMouseMotionListener(this.titleMouseInputHandler);
		}
	}

	/**
	 * Installs the appropriate LayoutManager on the <code>JRootPane</code> to
	 * render the window decorations.
	 * 
	 * @param root
	 *            Root pane.
	 */
	private void installLayout(JRootPane root) {
		if (this.layoutManager == null) {
			this.layoutManager = this.createLayoutManager();
		}
		this.savedOldLayout = root.getLayout();
		root.setLayout(this.layoutManager);
	}

	@Override
	protected void installListeners(final JRootPane root) {
		super.installListeners(root);

		this.hierarchyListener = new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				Component parent = root.getParent();

				if (parent != null) {
					Window currWindow = null;
					if (root.getParent() instanceof Window) {
						currWindow = (Window) root.getParent();
					} else {
						currWindow = SwingUtilities.getWindowAncestor(root
								.getParent());
					}

					if (windowComponentListener != null) {
						currentWindow
								.removeComponentListener(windowComponentListener);
						windowComponentListener = null;
					}
					if (currWindow != null) {
						// fix for defect 213 - maximizing frame under multiple
						// screens shouldn't always use insets of the primary
						// screen.
						windowComponentListener = new ComponentAdapter() {
							@Override
							public void componentMoved(ComponentEvent e) {
								this.processNewPosition();
							}

							@Override
							public void componentResized(ComponentEvent e) {
								this.processNewPosition();
							}

							protected void processNewPosition() {
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										currentRootPaneGC = null;
										if (!window.isShowing())
											return;

										GraphicsEnvironment ge = GraphicsEnvironment
												.getLocalGraphicsEnvironment();
										GraphicsDevice[] gds = ge
												.getScreenDevices();
										Point midLoc = new Point(window
												.getLocationOnScreen().x
												+ window.getWidth() / 2, window
												.getLocationOnScreen().y);
										// clamp y (otherwise on decorated mode
										// the top y is negative).
										if (midLoc.y < 0)
											midLoc.y = 0;
										// System.out.println("Loc : "
										// + window.getLocationOnScreen()
										// + ", width : "
										// + window.getWidth()
										// + ", mid : " + midLoc);
										int index = 0;
										for (GraphicsDevice gd : gds) {
											GraphicsConfiguration gc = gd
													.getDefaultConfiguration();
											Rectangle bounds = gc.getBounds();
											// System.out.println("Bounds : "
											// + bounds);
											if (bounds.contains(midLoc)) {
												currentRootPaneGC = gc;
												setMaximized();
												// System.out.println("Set");
												break;
											}
											index++;
										}
									}
								});
							}
						};
						// fix for defect 225 - install the listener only on
						// JFrames.
						if (parent instanceof JFrame) {
							currWindow.addComponentListener(windowComponentListener);
						}

						A03RootPaneUI.this.window = currWindow;
					}
					currentWindow = currWindow;
					
					// Davide Raccagni {
					if (currentWindow != null) {
						A03UIPluginManager.getInstance().getUIPlugins().installUI(currentWindow);
					}
					// } Davide Raccagni
				}
			}
		};
		root.addHierarchyListener(this.hierarchyListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicRootPaneUI#uninstallListeners(javax.swing.JRootPane)
	 */
	@Override
	protected void uninstallListeners(JRootPane root) {
		if (this.window != null) {
			this.window.removeComponentListener(this.windowComponentListener);
			this.window.removeWindowListener(this.windowListener);
			this.windowComponentListener = null;
			this.windowListener = null;
		}
		root.removeHierarchyListener(this.hierarchyListener);
		this.hierarchyListener = null;
		super.uninstallListeners(root);
	}

	/**
	 * Uninstalls the previously installed <code>LayoutManager</code>.
	 * 
	 * @param root
	 *            Root pane.
	 */
	private void uninstallLayout(JRootPane root) {
		if (this.savedOldLayout != null) {
			root.setLayout(this.savedOldLayout);
			this.savedOldLayout = null;
		}
	}

	/**
	 * Installs the necessary state onto the JRootPane to render client
	 * decorations. This is ONLY invoked if the <code>JRootPane</code> has a
	 * decoration style other than <code>JRootPane.NONE</code>.
	 * 
	 * @param root
	 *            Root pane.
	 */
	private void installClientDecorations(JRootPane root) {
		this.installBorder(root);

		JComponent titlePane = this.createTitlePane(root);

		this.setTitlePane(root, titlePane);
		this.installWindowListeners(root, root.getParent());
		this.installLayout(root);
		if (this.window != null) {
			root.revalidate();
			root.repaint();
		}
	}

	/**
	 * Uninstalls any state that <code>installClientDecorations</code> has
	 * installed.
	 * <p>
	 * NOTE: This may be called if you haven't installed client decorations yet
	 * (ie before <code>installClientDecorations</code> has been invoked).
	 * 
	 * @param root
	 *            Root pane.
	 */
	private void uninstallClientDecorations(JRootPane root) {
		this.uninstallBorder(root);
		this.uninstallWindowListeners(root);
		this.setTitlePane(root, null);
		this.uninstallLayout(root);
		// We have to revalidate/repaint root if the style is JRootPane.NONE
		// only. When we needs to call revalidate/repaint with other styles
		// the installClientDecorations is always called after this method
		// imediatly and it will cause the revalidate/repaint at the proper
		// time.
		int style = root.getWindowDecorationStyle();
		if (style == JRootPane.NONE) {
			root.repaint();
			root.revalidate();
		}
		// Reset the cursor, as we may have changed it to a resize cursor
		if (this.window != null) {
			this.window.setCursor(Cursor
					.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		this.window = null;
	}

	/**
	 * Returns the <code>JComponent</code> to render the window decoration
	 * style.
	 * 
	 * @param root
	 *            Root pane.
	 * @return The title pane component.
	 */
	protected JComponent createTitlePane(JRootPane root) {
		return new A03TitlePane(root, this);
	}

	/**
	 * Returns a <code>MouseListener</code> that will be added to the
	 * <code>Window</code> containing the <code>JRootPane</code>.
	 * 
	 * @param root
	 *            Root pane.
	 * @return Window mouse listener.
	 */
	private MouseInputListener createWindowMouseInputListener(JRootPane root) {
		return new MouseInputHandler();
	}
	
	/**
	 * Returns a <code>LayoutManager</code> that will be set on the
	 * <code>JRootPane</code>.
	 * 
	 * @return Layout manager.
	 */
	protected LayoutManager createLayoutManager() {
		return new A03RootLayout();
	}

	/**
	 * Sets the window title pane -- the JComponent used to provide a plaf a way
	 * to override the native operating system's window title pane with one
	 * whose look and feel are controlled by the plaf. The plaf creates and sets
	 * this value; the default is null, implying a native operating system
	 * window title pane.
	 * 
	 * @param root
	 *            Root pane
	 * @param titlePane
	 *            The <code>JComponent</code> to use for the window title
	 *            pane.
	 */
	private void setTitlePane(JRootPane root, JComponent titlePane) {
		JLayeredPane layeredPane = root.getLayeredPane();
		JComponent oldTitlePane = this.getTitlePane();

		if (oldTitlePane != null) {
			A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(oldTitlePane);

			// fix for defect 109 - memory leak on theme change
			if (oldTitlePane instanceof A03TitlePane)
				((A03TitlePane) oldTitlePane).uninstall();
			// oldTitlePane.setVisible(false);
			layeredPane.remove(oldTitlePane);
		}
		if (titlePane != null) {
			A03UIPluginManager.getInstance().getUIPlugins().installUI(titlePane);
			
			layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
			titlePane.setVisible(true);
		}
		this.titlePane = titlePane;
	}

	/**
	 * Sets maximized bounds according to the display screen insets.
	 */
	private void setMaximized() {
		Component tla = this.root.getTopLevelAncestor();
		// fix for defect 213 - maximizing frame under multiple
		// screens shouldn't always use insets of the primary
		// screen.
		GraphicsConfiguration gc = (currentRootPaneGC != null) ? currentRootPaneGC
				: tla.getGraphicsConfiguration();
		Rectangle screenBounds = gc.getBounds();
		screenBounds.x = 0;
		screenBounds.y = 0;
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Border rootBorder = this.root.getBorder();
		Insets insets = (rootBorder == null) ? new Insets(0, 0, 0, 0)
				: rootBorder.getBorderInsets(this.root);
		Rectangle maxBounds = new Rectangle(
				(screenBounds.x + screenInsets.left) - insets.left,
				(screenBounds.y + screenInsets.top) - insets.right,
				screenBounds.width
						- ((screenInsets.left + screenInsets.right)
								- insets.left - insets.right),
				screenBounds.height
						- ((screenInsets.top + screenInsets.bottom)
								- insets.top - insets.bottom));
		if (tla instanceof JFrame)
			((JFrame) tla).setMaximizedBounds(maxBounds);
	}

	/**
	 * Returns the <code>JComponent</code> rendering the title pane. If this
	 * returns null, it implies there is no need to render window decorations.
	 * This method is <b>for internal use only</b>.
	 * 
	 * @see #setTitlePane
	 * @return Title pane.
	 */
	public JComponent getTitlePane() {
		return this.titlePane;
	}

	/**
	 * Returns the <code>JRootPane</code> we're providing the look and feel
	 * for.
	 * 
	 * @return The associated root pane.
	 */
	private JRootPane getRootPane() {
		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicRootPaneUI#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		String propertyName = e.getPropertyName();
		if (propertyName == null) {
			return;
		}

		if (propertyName.equals("windowDecorationStyle")) {
			JRootPane root = (JRootPane) e.getSource();
			int style = root.getWindowDecorationStyle();

			this.uninstallClientDecorations(root);
			if (style != JRootPane.NONE) {
				this.installClientDecorations(root);
			}
		} else if (propertyName.equals("ancestor")) {
			this.uninstallWindowListeners(this.root);
			if (((JRootPane) e.getSource()).getWindowDecorationStyle() != JRootPane.NONE) {
				this.installWindowListeners(this.root, this.root.getParent());
			}
		}
		return;
	}

	/**
	 * A custom layout manager that is responsible for the layout of
	 * layeredPane, glassPane, menuBar and titlePane, if one has been installed.
	 */
	// NOTE: Ideally this would extends JRootPane.RootLayout, but that
	// would force this to be non-static.
	protected static class A03RootLayout implements LayoutManager2 {
		/**
		 * Returns the amount of space the layout would like to have.
		 * 
		 * 
		 * aram the Container for which this layout manager is being used
		 * 
		 * @return a Dimension object containing the layout's preferred size
		 */
		public Dimension preferredLayoutSize(Container parent) {
			Dimension cpd, mbd, tpd;
			int cpWidth = 0;
			int cpHeight = 0;
			int mbWidth = 0;
			int mbHeight = 0;
			int tpWidth = 0;

			Insets i = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getPreferredSize();
			} else {
				cpd = root.getSize();
			}
			if (cpd != null) {
				cpWidth = cpd.width;
				cpHeight = cpd.height;
			}

			if (root.getJMenuBar() != null) {
				mbd = root.getJMenuBar().getPreferredSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}

			if ((root.getWindowDecorationStyle() != JRootPane.NONE)
					&& (root.getUI() instanceof A03RootPaneUI)) {
				JComponent titlePane = ((A03RootPaneUI) root.getUI())
						.getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getPreferredSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						// tpHeight = tpd.height;
					}
				}
			}

			return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth)
					+ i.left + i.right, cpHeight + mbHeight + tpWidth + i.top
					+ i.bottom);
		}

		/**
		 * Returns the minimum amount of space the layout needs.
		 * 
		 * 
		 * aram the Container for which this layout manager is being used
		 * 
		 * @return a Dimension object containing the layout's minimum size
		 */
		public Dimension minimumLayoutSize(Container parent) {
			Dimension cpd, mbd, tpd;
			int cpWidth = 0;
			int cpHeight = 0;
			int mbWidth = 0;
			int mbHeight = 0;
			int tpWidth = 0;
			
			Insets i = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getMinimumSize();
			} else {
				cpd = root.getSize();
			}
			if (cpd != null) {
				cpWidth = cpd.width;
				cpHeight = cpd.height;
			}

			if (root.getJMenuBar() != null) {
				mbd = root.getJMenuBar().getMinimumSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}
			if ((root.getWindowDecorationStyle() != JRootPane.NONE)
					&& (root.getUI() instanceof A03RootPaneUI)) {
				JComponent titlePane = ((A03RootPaneUI) root.getUI())
						.getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getMinimumSize();
					if (tpd != null) {
						tpWidth = tpd.width;
					}
				}
			}

			return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth)
					+ i.left + i.right, cpHeight + mbHeight + tpWidth + i.top
					+ i.bottom);
		}

		/**
		 * Returns the maximum amount of space the layout can use.
		 * 
		 * 
		 * aram the Container for which this layout manager is being used
		 * 
		 * @return a Dimension object containing the layout's maximum size
		 */
		public Dimension maximumLayoutSize(Container target) {
			Dimension cpd, mbd, tpd;
			int cpWidth = Integer.MAX_VALUE;
			int cpHeight = Integer.MAX_VALUE;
			int mbWidth = Integer.MAX_VALUE;
			int mbHeight = Integer.MAX_VALUE;
			int tpWidth = Integer.MAX_VALUE;
			int tpHeight = Integer.MAX_VALUE;
			Insets i = target.getInsets();
			JRootPane root = (JRootPane) target;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getMaximumSize();
				if (cpd != null) {
					cpWidth = cpd.width;
					cpHeight = cpd.height;
				}
			}

			if (root.getJMenuBar() != null) {
				mbd = root.getJMenuBar().getMaximumSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}

			if ((root.getWindowDecorationStyle() != JRootPane.NONE)
					&& (root.getUI() instanceof A03RootPaneUI)) {
				JComponent titlePane = ((A03RootPaneUI) root.getUI())
						.getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getMaximumSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						tpHeight = tpd.height;
					}
				}
			}

			int maxHeight = Math.max(Math.max(cpHeight, mbHeight), tpHeight);
			// Only overflows if 3 real non-MAX_VALUE heights, sum to >
			// MAX_VALUE
			// Only will happen if sums to more than 2 billion units. Not
			// likely.
			if (maxHeight != Integer.MAX_VALUE) {
				maxHeight = cpHeight + mbHeight + tpHeight + i.top + i.bottom;
			}

			int maxWidth = Math.max(Math.max(cpWidth, mbWidth), tpWidth);
			// Similar overflow comment as above
			if (maxWidth != Integer.MAX_VALUE) {
				maxWidth += i.left + i.right;
			}

			return new Dimension(maxWidth, maxHeight);
		}

		/**
		 * Instructs the layout manager to perform the layout for the specified
		 * container.
		 * 
		 * 
		 * aram the Container for which this layout manager is being used
		 */
		public void layoutContainer(Container parent) {
			JRootPane root = (JRootPane) parent;
			Rectangle b = root.getBounds();
			Insets i = root.getInsets();
			int nextY = 0;
			int w = b.width - i.right - i.left;
			int h = b.height - i.top - i.bottom;

			if (root.getLayeredPane() != null) {
				root.getLayeredPane().setBounds(i.left, i.top, w, h);
			}
			if (root.getGlassPane() != null) {
				root.getGlassPane().setBounds(i.left, i.top, w, h);
			}
			// Note: This is laying out the children in the layeredPane,
			// technically, these are not our children.
			if ((root.getWindowDecorationStyle() != JRootPane.NONE)
					&& (root.getUI() instanceof A03RootPaneUI)) {
				JComponent titlePane = ((A03RootPaneUI) root.getUI())
						.getTitlePane();
				if (titlePane != null) {
					Dimension tpd = titlePane.getPreferredSize();
					if (tpd != null) {
						int tpHeight = tpd.height;
						titlePane.setBounds(0, 0, w, tpHeight);
						nextY += tpHeight;
					}
				}
			}
			if (root.getJMenuBar() != null) {
				Dimension mbd = root.getJMenuBar().getPreferredSize();
				root.getJMenuBar().setBounds(0, nextY, w, mbd.height);
				nextY += mbd.height;
			}
			if (root.getContentPane() != null) {
				// Dimension cpd = root.getContentPane().getPreferredSize();
				root.getContentPane().setBounds(0, nextY, w,
						h < nextY ? 0 : h - nextY);
			}
		}

		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public void addLayoutComponent(Component comp, Object constraints) {
		}

		public float getLayoutAlignmentX(Container target) {
			return 0.0f;
		}

		public float getLayoutAlignmentY(Container target) {
			return 0.0f;
		}

		public void invalidateLayout(Container target) {
		}
	}

	/**
	 * Maps from positions to cursor type. Refer to calculateCorner and
	 * calculatePosition for details of this.
	 */
	private static final int[] cursorMapping = new int[] {
			Cursor.NW_RESIZE_CURSOR,
			Cursor.NW_RESIZE_CURSOR,
			Cursor.N_RESIZE_CURSOR,
			Cursor.NE_RESIZE_CURSOR,
			Cursor.NE_RESIZE_CURSOR,
			Cursor.NW_RESIZE_CURSOR, 
			0, 
			0, 
			0,
			Cursor.NE_RESIZE_CURSOR,
			Cursor.W_RESIZE_CURSOR,
			0, 
			0, 
			0,
			Cursor.E_RESIZE_CURSOR,
			Cursor.SW_RESIZE_CURSOR,
			Cursor.SW_RESIZE_CURSOR,
			0,
			// Davide Raccagni {
			/*
			0,
			0,
			 */
			Cursor.SE_RESIZE_CURSOR,
			Cursor.SE_RESIZE_CURSOR,
			// } Davide Raccagni
			Cursor.SW_RESIZE_CURSOR,
			Cursor.SW_RESIZE_CURSOR,
			Cursor.S_RESIZE_CURSOR,
			Cursor.SE_RESIZE_CURSOR,
			Cursor.SE_RESIZE_CURSOR 
		};

	/**
	 * MouseInputHandler is responsible for handling resize/moving of the
	 * Window. It sets the cursor directly on the Window when then mouse moves
	 * over a hot spot.
	 */
	private class MouseInputHandler implements MouseInputListener {
		/**
		 * Set to true if the drag operation is moving the window.
		 */
		private boolean isMovingWindow;

		/**
		 * Used to determine the corner the resize is occuring from.
		 */
		private int dragCursor;

		/**
		 * X location the mouse went down on for a drag operation.
		 */
		private int dragOffsetX;

		/**
		 * Y location the mouse went down on for a drag operation.
		 */
		private int dragOffsetY;

		/**
		 * Width of the window when the drag started.
		 */
		private int dragWidth;

		/**
		 * Height of the window when the drag started.
		 */
		private int dragHeight;

		/**
		 * PrivilegedExceptionAction needed by mouseDragged method to obtain new
		 * location of window on screen during the drag.
		 */
		// Davide Raccagni added <Point> 
		private final PrivilegedExceptionAction<Point> getLocationAction = new PrivilegedExceptionAction<Point>() {
			public Point run() throws HeadlessException {
				return MouseInfo.getPointerInfo().getLocation();
			}
		};

		public void mousePressed(MouseEvent ev) {
			JRootPane rootPane = A03RootPaneUI.this.getRootPane();

			if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}
			Point dragWindowOffset = ev.getPoint();
			Window w = (Window) ev.getSource();
			if (w != null) {
				w.toFront();
			}
			Point convertedDragWindowOffset = SwingUtilities.convertPoint(w,
					dragWindowOffset, A03RootPaneUI.this.getTitlePane());

			Frame f = null;
			Dialog d = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else if (w instanceof Dialog) {
				d = (Dialog) w;
			}

			int frameState = (f != null) ? f.getExtendedState() : 0;

			if ((A03RootPaneUI.this.getTitlePane() != null)
					&& A03RootPaneUI.this.getTitlePane().contains(
							convertedDragWindowOffset)) {
				Insets insets = getRootPane().getInsets();
				
				if ((((f != null) && ((frameState & Frame.MAXIMIZED_BOTH) == 0)) || (d != null))
						&& (dragWindowOffset.y >= insets.top) // A03RootPaneUI.BORDER_DRAG_THICKNESS)
						&& (dragWindowOffset.x >= insets.left) // A03RootPaneUI.BORDER_DRAG_THICKNESS)
						&& (dragWindowOffset.x < w.getWidth()
								- insets.right)) { // A03RootPaneUI.BORDER_DRAG_THICKNESS)) {
					this.isMovingWindow = true;
					this.dragOffsetX = dragWindowOffset.x;
					this.dragOffsetY = dragWindowOffset.y;
				}
			} else if (((f != null) && f.isResizable() && ((frameState & Frame.MAXIMIZED_BOTH) == 0))
					|| ((d != null) && d.isResizable())) {
				this.dragOffsetX = dragWindowOffset.x;
				this.dragOffsetY = dragWindowOffset.y;
				this.dragWidth = w.getWidth();
				this.dragHeight = w.getHeight();
				this.dragCursor = this.getCursor(this.calculateCorner(w,
						dragWindowOffset.x, dragWindowOffset.y));
			}
		}

		public void mouseReleased(MouseEvent ev) {
			if ((this.dragCursor != 0)
					&& (A03RootPaneUI.this.window != null)
					&& !A03RootPaneUI.this.window.isValid()) {
				// Some Window systems validate as you resize, others won't,
				// thus the check for validity before repainting.
				A03RootPaneUI.this.window.validate();
				A03RootPaneUI.this.getRootPane().repaint();
			}
			this.isMovingWindow = false;
			this.dragCursor = 0;
		}

		public void mouseMoved(MouseEvent ev) {
			JRootPane root = A03RootPaneUI.this.getRootPane();

			if (root.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}

			Window w = (Window) ev.getSource();

			Frame f = null;
			Dialog d = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else if (w instanceof Dialog) {
				d = (Dialog) w;
			}

			// Update the cursor
			int cursor = this.getCursor(this.calculateCorner(w, ev.getX(), ev
					.getY()));

			if ((cursor != 0)
					&& (((f != null) && (f.isResizable() && ((f
							.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))) || ((d != null) && d
							.isResizable()))) {
				w.setCursor(Cursor.getPredefinedCursor(cursor));
			} else {
				w.setCursor(A03RootPaneUI.this.lastCursor);
			}
		}

		/**
		 * Adjusts the bounds.
		 * 
		 * @param bounds
		 *            Original bounds.
		 * @param min
		 *            Minimum dimension.
		 * @param deltaX
		 *            Delta X.
		 * @param deltaY
		 *            Delta Y.
		 * @param deltaWidth
		 *            Delta width.
		 * @param deltaHeight
		 *            Delta height.
		 */
		private void adjust(Rectangle bounds, Dimension min, int deltaX,
				int deltaY, int deltaWidth, int deltaHeight) {
			bounds.x += deltaX;
			bounds.y += deltaY;
			bounds.width += deltaWidth;
			bounds.height += deltaHeight;
			if (min != null) {
				if (bounds.width < min.width) {
					int correction = min.width - bounds.width;
					if (deltaX != 0) {
						bounds.x -= correction;
					}
					bounds.width = min.width;
				}
				if (bounds.height < min.height) {
					int correction = min.height - bounds.height;
					if (deltaY != 0) {
						bounds.y -= correction;
					}
					bounds.height = min.height;
				}
			}
		}

		public void mouseDragged(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			Point pt = ev.getPoint();

			if (this.isMovingWindow) {
				Point windowPt;
				try {
					windowPt = (Point) AccessController
							.doPrivileged(this.getLocationAction);
					windowPt.x = windowPt.x - this.dragOffsetX;
					windowPt.y = windowPt.y - this.dragOffsetY;
					w.setLocation(windowPt);
				} catch (PrivilegedActionException e) {
				}
			} else if (this.dragCursor != 0) {
				Rectangle r = w.getBounds();
				Rectangle startBounds = new Rectangle(r);
				Dimension min = w.getMinimumSize();

				switch (this.dragCursor) {
				case Cursor.E_RESIZE_CURSOR:
					this.adjust(r, min, 0, 0, pt.x
							+ (this.dragWidth - this.dragOffsetX) - r.width, 0);
					break;
				case Cursor.S_RESIZE_CURSOR:
					this.adjust(r, min, 0, 0, 0, pt.y
							+ (this.dragHeight - this.dragOffsetY) - r.height);
					break;
				case Cursor.N_RESIZE_CURSOR:
					this.adjust(r, min, 0, pt.y - this.dragOffsetY, 0,
							-(pt.y - this.dragOffsetY));
					break;
				case Cursor.W_RESIZE_CURSOR:
					this.adjust(r, min, pt.x - this.dragOffsetX, 0,
							-(pt.x - this.dragOffsetX), 0);
					break;
				case Cursor.NE_RESIZE_CURSOR:
					this.adjust(r, min, 0, pt.y - this.dragOffsetY, pt.x
							+ (this.dragWidth - this.dragOffsetX) - r.width,
							-(pt.y - this.dragOffsetY));
					break;
				case Cursor.SE_RESIZE_CURSOR:
					this.adjust(r, min, 0, 0, pt.x
							+ (this.dragWidth - this.dragOffsetX) - r.width,
							pt.y + (this.dragHeight - this.dragOffsetY)
									- r.height);
					break;
				case Cursor.NW_RESIZE_CURSOR:
					this.adjust(r, min, pt.x - this.dragOffsetX, pt.y
							- this.dragOffsetY, -(pt.x - this.dragOffsetX),
							-(pt.y - this.dragOffsetY));
					break;
				case Cursor.SW_RESIZE_CURSOR:
					this.adjust(r, min, pt.x - this.dragOffsetX, 0,
							-(pt.x - this.dragOffsetX), pt.y
									+ (this.dragHeight - this.dragOffsetY)
									- r.height);
					break;
				default:
					break;
				}
				if (!r.equals(startBounds)) {
					w.setBounds(r);
					// Defer repaint/validate on mouseReleased unless dynamic
					// layout is active.
					if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
						w.validate();
						A03RootPaneUI.this.getRootPane().repaint();
					}
				}
			}
		}

		public void mouseEntered(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			// fix for defect 107
			A03RootPaneUI.this.lastCursor = w.getCursor();
			// Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
			this.mouseMoved(ev);
		}

		public void mouseExited(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			w.setCursor(A03RootPaneUI.this.lastCursor);
		}

		public void mouseClicked(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			Frame f = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else {
				return;
			}

			Point convertedPoint = SwingUtilities.convertPoint(w,
					ev.getPoint(), A03RootPaneUI.this.getTitlePane());

			int state = f.getExtendedState();
			if ((A03RootPaneUI.this.getTitlePane() != null)
					&& A03RootPaneUI.this.getTitlePane().contains(
							convertedPoint)) {
				if (((ev.getClickCount() % 2) == 0)
						&& ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
					if (f.isResizable()) {
						if ((state & Frame.MAXIMIZED_BOTH) != 0) {
							f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
						} else {
							f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
						}
						return;
					}
				}
			}
		}

		/**
		 * Returns the corner that contains the point <code>x</code>,
		 * <code>y</code>, or -1 if the position doesn't match a corner.
		 * 
		 * @param w
		 *            Window.
		 * @param x
		 *            X coordinate.
		 * @param y
		 *            Y coordinate.
		 * @return Corner that contains the specified point.
		 */
		private int calculateCorner(Window w, int x, int y) {
			Insets insets = w.getInsets();
			int xPosition = this.calculatePosition(x - insets.left, w
					.getWidth()
					- insets.left - insets.right);
			int yPosition = this.calculatePosition(y - insets.top, w
					.getHeight()
					- insets.top - insets.bottom);

			if ((xPosition == -1) || (yPosition == -1)) {
				return -1;
			}
			return yPosition * 5 + xPosition;
		}

		/**
		 * Returns the Cursor to render for the specified corner. This returns 0
		 * if the corner doesn't map to a valid Cursor
		 * 
		 * @param corner
		 *            Corner
		 * @return Cursor to render for the specified corner.
		 */
		private int getCursor(int corner) {
			if (corner == -1) {
				return 0;
			}
			return A03RootPaneUI.cursorMapping[corner];
		}

		/**
		 * Returns an integer indicating the position of <code>spot</code> in
		 * <code>width</code>. The return value will be: 0 if <
		 * BORDER_DRAG_THICKNESS 1 if < CORNER_DRAG_WIDTH 2 if >=
		 * CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS 3 if >= width -
		 * CORNER_DRAG_WIDTH 4 if >= width - BORDER_DRAG_THICKNESS 5 otherwise
		 * 
		 * @param spot
		 *            Spot.
		 * @param width
		 *            Width.
		 * @return The position of spot in width.
		 */
		private int calculatePosition(int spot, int width) {
			Insets insets = getRootPane().getInsets();
			
			if (spot < insets.left) { // A03RootPaneUI.BORDER_DRAG_THICKNESS) {
				return 0;
			}
			/*
			if (spot < A03RootPaneUI.BORDER_DRAG_THICKNESS) { // CORNER_DRAG_WIDTH) {
				return 1;
			}
			*/
			if (spot >= (width - insets.right)) { // A03RootPaneUI.BORDER_DRAG_THICKNESS)) {
				return 4;
			}
			/*
			if (spot >= (width - A03RootPaneUI.BORDER_DRAG_THICKNESS)) { // CORNER_DRAG_WIDTH)) {
				return 3;
			}
			*/
			return 2;
		}
	}

	/**
	 * Mouse handler on the title pane.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class TitleMouseInputHandler extends MouseInputAdapter {

		/**
		 * Pointer location when the mouse was pressed for a drag relative to
		 * the upper-lefthand corner of the window.
		 */
		private Point dragOffset = new Point(0, 0);

		@Override
		public void mousePressed(MouseEvent ev) {
			JRootPane rootPane = A03RootPaneUI.this.getRootPane();

			if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}

			Point dragWindowOffset = ev.getPoint();
			Component source = (Component) ev.getSource();

			Point convertedDragWindowOffset = SwingUtilities.convertPoint(
					source, dragWindowOffset, getTitlePane());

			dragWindowOffset = SwingUtilities.convertPoint(source,
					dragWindowOffset, A03RootPaneUI.this.window);

			if (getTitlePane() != null
					&& getTitlePane().contains(convertedDragWindowOffset)) {
				if (A03RootPaneUI.this.window != null) {
					A03RootPaneUI.this.window.toFront();
					dragOffset = dragWindowOffset;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent ev) {
			Component source = (Component) ev.getSource();

			// Point pt = SwingUtilities.convertPoint(source, ev.getPoint(),
			// SubstanceRootPaneUI.this.window);

			Point eventLocationOnScreen = null;
			if (getLocationOnScreenMethod != null) {
				try {
					// fix for issue 198 - only available under JDK 6.0+
					eventLocationOnScreen = (Point) getLocationOnScreenMethod
							.invoke(ev, new Object[0]);
				} catch (Exception exc) {
					eventLocationOnScreen = null;
				}
			}
			if (eventLocationOnScreen == null) {
				eventLocationOnScreen = new Point(ev.getX()
						+ source.getLocationOnScreen().x, ev.getY()
						+ source.getLocationOnScreen().y);
			}
			// Fix for issue 192 - disable dragging maximized frame.
			if (A03RootPaneUI.this.window instanceof Frame) {
				Frame f = (Frame) A03RootPaneUI.this.window;
				int frameState = (f != null) ? f.getExtendedState() : 0;
				if ((f != null) && ((frameState & Frame.MAXIMIZED_BOTH) == 0)) {
					A03RootPaneUI.this.window.setLocation(
							eventLocationOnScreen.x - dragOffset.x,
							eventLocationOnScreen.y - dragOffset.y);
				}
			} else {
				// fix for issue 193 - allow dragging decorated dialogs.
				A03RootPaneUI.this.window.setLocation(
						eventLocationOnScreen.x - dragOffset.x,
						eventLocationOnScreen.y - dragOffset.y);
			}

		}

		@Override
		public void mouseClicked(MouseEvent ev) {
			Frame f = null;

			if (A03RootPaneUI.this.window instanceof Frame) {
				f = (Frame) A03RootPaneUI.this.window;
			} else {
				return;
			}

			Point convertedPoint = SwingUtilities.convertPoint(
					A03RootPaneUI.this.window, ev.getPoint(),
					A03RootPaneUI.this.getTitlePane());

			int state = f.getExtendedState();
			if ((A03RootPaneUI.this.getTitlePane() != null)
					&& A03RootPaneUI.this.getTitlePane().contains(
							convertedPoint)) {
				if (((ev.getClickCount() % 2) == 0)
						&& ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
					if (f.isResizable()) {
						if ((state & Frame.MAXIMIZED_BOTH) != 0) {
							f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
						} else {
							f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
						}
						return;
					}
				}
			}
		}
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
