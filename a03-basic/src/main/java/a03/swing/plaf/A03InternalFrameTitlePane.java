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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;



public class A03InternalFrameTitlePane extends BasicInternalFrameTitlePane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5946085458048351590L;
	
	private final static int TITLE_HGAP = 3;
	
	private Handler handler;
	
	private A03InternalFrameUIDelegate uiDelegate;
	
	private int state;
	
	public A03InternalFrameTitlePane(JInternalFrame f) {
		super(f);
	}
	
	@Override
	protected void installTitlePane() {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(frame, A03InternalFrameUIDelegate.class);

		super.installTitlePane();
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		this.setFont(UIManager.getFont(uiDelegate.getTitleFont(), this.getLocale()));		
	}
	
	@Override
	protected void paintTitleBackground(Graphics g) {
		uiDelegate.paintTitleBackground(frame, g);
	}
	
	@Override
	protected void createButtons() {
		super.createButtons();
		
		iconButton.setBorderPainted(false);
		iconButton.setContentAreaFilled(false);

		maxButton.setBorderPainted(false);
		maxButton.setContentAreaFilled(false);

		closeButton.setBorderPainted(false);
		closeButton.setContentAreaFilled(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		paintTitleBackground(g);

		if (frame.getTitle() != null) {
			FontMetrics fm = frame.getFontMetrics(getFont());
			
			int titleY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() - 1;
			
			if (frame != null) {
				JRootPane rootPane = frame.getRootPane();
				if (((state & Frame.MAXIMIZED_BOTH) != 0)
						&& ((rootPane.getBorder() == null) || (rootPane
								.getBorder() instanceof UIResource))
						&& frame.isShowing()) {
					titleY += 2;
				}
			}

			int titleX, titleW;
			Rectangle bounds = new Rectangle(0, 0, 0, 0);
			if (frame.isIconifiable())
				bounds = iconButton.getBounds();
			else if (frame.isMaximizable())
				bounds = maxButton.getBounds();
			else if (frame.isClosable())
				bounds = closeButton.getBounds();

			String title = frame.getTitle();
			if (A03SwingUtilities.isLeftToRight(frame)) {
				if (bounds.x == 0)
					bounds.x = frame.getWidth() - frame.getInsets().right;
				titleX = menuBar.getX() + TITLE_HGAP + menuBar.getWidth();
				titleW = bounds.x - titleX - TITLE_HGAP;
				title = getTitle(frame.getTitle(), fm, titleW);
			} else {
				int endX = menuBar.getX() - TITLE_HGAP;
				titleW = endX - bounds.x - bounds.width - TITLE_HGAP;
				title = getTitle(frame.getTitle(), fm, titleW);
				titleX = endX - A03GraphicsUtilities.stringWidth(fm, title);
			}

			uiDelegate.paintTitleText(this, g, title, titleX, titleY);
		}
	}

	JInternalFrame getFrame() {
		return frame;
	}
	
    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    @Override
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    @Override
    protected LayoutManager createLayout() {
        return getHandler();
    }

    private class Handler implements LayoutManager, PropertyChangeListener {
        //
        // PropertyChangeListener
        //
        public void propertyChange(PropertyChangeEvent evt) {
            String prop = (String)evt.getPropertyName();

            if (prop == JInternalFrame.IS_SELECTED_PROPERTY) {
                repaint();
                return;
            } 

            if (prop == JInternalFrame.IS_ICON_PROPERTY ||
                    prop == JInternalFrame.IS_MAXIMUM_PROPERTY) {
                setButtonIcons();
                enableActions();
                return;
            }

            if ("closable" == prop) {
                if ((Boolean)evt.getNewValue() == Boolean.TRUE) {
                    add(closeButton);
                } else {
                    remove(closeButton);
                }
            } else if ("maximizable" == prop) {
                if ((Boolean)evt.getNewValue() == Boolean.TRUE) {
                    add(maxButton);
                } else {
                    remove(maxButton);
                }
            } else if ("iconable" == prop) {
                if ((Boolean)evt.getNewValue() == Boolean.TRUE) {
                    add(iconButton);
                } else {
                    remove(iconButton);
                }
            }
            enableActions();
            
            revalidate();
            repaint();
        }


        //
        // LayoutManager
        //
        public void addLayoutComponent(String name, Component c) {}
        public void removeLayoutComponent(Component c) {}    
        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }
    
        public Dimension minimumLayoutSize(Container c) {
            // Calculate width.
            int width = 22;
 
            if (frame.isClosable()) {
                width += 19;
            }
            if (frame.isMaximizable()) {
                width += 19;
            }
            if (frame.isIconifiable()) {
                width += 19;
            }

            Font font = uiDelegate.getTitleFont();
            FontMetrics fm = frame.getFontMetrics(font);
            String frameTitle = frame.getTitle();
            int title_w = frameTitle != null ? fm.stringWidth(frameTitle) : 0;
            int title_length = frameTitle != null ? frameTitle.length() : 0;

            // Leave room for three characters in the title.
            if (title_length > 3) {
                int subtitle_w = fm.stringWidth(frameTitle.substring(0, 3) + "...");
                width += (title_w < subtitle_w) ? title_w : subtitle_w;
            } else {
                width += title_w;
            }

            // Calculate height.
            Icon icon = frame.getFrameIcon();
            int fontHeight = fm.getHeight();
            fontHeight += 2;
            int iconHeight = 0;
            if (icon != null) {
                // SystemMenuBar forces the icon to be 16x16 or less.
                iconHeight = Math.min(icon.getIconHeight(), 16);
            }
            iconHeight += 4;
      
            int height = Math.max( fontHeight, iconHeight );

            Dimension dim = new Dimension(width, height);

            // Take into account the border insets if any.
            if (getBorder() != null) {
                Insets insets = getBorder().getBorderInsets(c);
                dim.height += insets.top + insets.bottom;
                dim.width += insets.left + insets.right;
            }
            return dim;
        }
    
        public void layoutContainer(Container c) {
            boolean leftToRight = A03SwingUtilities.isLeftToRight(frame);
            
            int w = getWidth();
            int h = getHeight();
            int x;

            int buttonHeight = closeButton.getIcon().getIconHeight();

            Icon icon = frame.getFrameIcon();
            int iconHeight = 0;
            if (icon != null) {
                iconHeight = icon.getIconHeight();
            }
            x = (leftToRight) ? 2 : w - 16 - 2;
            menuBar.setBounds(x, (h - iconHeight) / 2, 16, 16);

            x = (leftToRight) ? w - 16 - 2 : 2;
            
            if (frame.isClosable()) {
                closeButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
                x += (leftToRight) ? -(16 + 10) : 16 + 10;
            } 
            
            if (frame.isMaximizable()) {
                maxButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
                x += (leftToRight) ? -(16 + 2) : 16 + 2;
            }
        
            if (frame.isIconifiable()) {
                iconButton.setBounds(x, (h - buttonHeight) / 2, 16, 14);
            } 
        }
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class PropertyChangeHandler implements PropertyChangeListener {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this      
        // class calls into the Handler.
        public void propertyChange(PropertyChangeEvent evt) {
            getHandler().propertyChange(evt);
	}
    }

    /**
     * This class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of <Foo>.
     */
    public class TitlePaneLayout implements LayoutManager {
        // NOTE: This class exists only for backward compatability. All
        // its functionality has been moved into Handler. If you need to add
        // new functionality add it to the Handler, but make sure this      
        // class calls into the Handler.
        public void addLayoutComponent(String name, Component c) {
            getHandler().addLayoutComponent(name, c);
        }

        public void removeLayoutComponent(Component c) {
            getHandler().removeLayoutComponent(c);
        }    

        public Dimension preferredLayoutSize(Container c)  {
            return getHandler().preferredLayoutSize(c);
        }
    
        public Dimension minimumLayoutSize(Container c) {
            return getHandler().minimumLayoutSize(c);
        }
    
        public void layoutContainer(Container c) {
            getHandler().layoutContainer(c);
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
}
