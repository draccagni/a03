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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import a03.swing.plugin.A03UIPluginManager;

public class A03SpinnerUI extends BasicSpinnerUI {
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03SpinnerUI();
	}

    class PropertyChangeListenerImpl implements PropertyChangeListener {

    	public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("enabled")) {
				updateBackground(spinner);
			} else if (evt.getPropertyName().equals("editor")) {
				updateBackground(spinner);
			}
		}
    }

    private PropertyChangeListener propertyChangeListener = new PropertyChangeListenerImpl();
    
	protected JButton nextButton;

	protected JButton prevButton;
	
	private A03SpinnerUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		uiDelegate = A03SwingUtilities.getUIDelegate(c, A03SpinnerUIDelegate.class);

		super.installUI(c);
		
	    spinner.setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
	    spinner.setFont(uiDelegate.getFont());
	    spinner.setForeground(uiDelegate.getForeground());
	    spinner.setBackground(uiDelegate.getBackground());

	    //"Spinner.disabledBackground", spinnerUIDelegate.getDisabledBackground(),
    	
		A03UIPluginManager.getInstance().getUIPlugins().installUI(spinner);
	}

	@Override
	public void uninstallUI(JComponent c) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(spinner);

		super.uninstallUI(c);
	}
    
    private void updateBackground(JSpinner c) {
        Color background = c.getBackground();
//        if (background instanceof UIResource) {
            Color newColor = null;
            if (!c.isEnabled()) {
                newColor = uiDelegate.getDisabledBackground();
            }

            if (newColor == null) {
                newColor = uiDelegate.getBackground();
            }

            if (newColor != null && newColor != background) {
                c.setBackground(newColor);
                
                JComponent editor = c.getEditor();
                for (int i = 0; i < editor.getComponentCount(); i++) {
                	Component comp = editor.getComponent(i);
                	if (comp.getName().equals("Spinner.formattedTextField")){
                		comp.setBackground(newColor);
                	}
                }
                
                editor.setBackground(newColor);
            }
//        }
	}
    
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		updateBackground(spinner);
	}
	
    @Override
    protected void installListeners() {
    	super.installListeners();
    	
    	spinner.addPropertyChangeListener(propertyChangeListener);
    }
    
    @Override
    protected void uninstallListeners() {
    	super.uninstallListeners();
    	
    	spinner.removePropertyChangeListener(propertyChangeListener);
    }
	
	@Override
	protected Component createPreviousButton() {
		this.prevButton = createArrowButton(SwingConstants.SOUTH);
		this.prevButton.setName("Spinner.previousButton");
        installPreviousButtonListeners(this.prevButton);
        return this.prevButton;
	}
	
	@Override
	protected Component createNextButton() {
		this.nextButton = createArrowButton(SwingConstants.NORTH);
		this.nextButton.setName("Spinner.nextButton");
        installNextButtonListeners(this.nextButton);
        return this.nextButton;
	}
	
    private JButton createArrowButton(int direction) {
    	JButton button = new A03ArrowButton(direction, uiDelegate);
        button.setInheritsPopupMenu(true);
		button.setBorder(A03BorderFactory.createArrowButtonDelegatedBorder(uiDelegate));
		button.setPreferredSize(UIManager.getDimension("Spinner.arrowButtonSize"));
        
    	return button;
    }
    
    @Override
    public void paint(Graphics g, JComponent c) {
		int width = c.getWidth();
		int height = c.getHeight();
		
        Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_OFF);
		
		graphics.setColor(c.getBackground());
        graphics.fillRect(0, 0, width - 1, height - 1);
        
        graphics.dispose();
    }

	@Override
	protected LayoutManager createLayout() {
		return new SpinnerLayoutManager();
	}

	/**
	 * Layout manager for the spinner.
	 * 
	 * @author Kirill Grouchnikov
	 */
	protected class SpinnerLayoutManager implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public Dimension minimumLayoutSize(Container parent) {
			return this.preferredLayoutSize(parent);
		}

		public Dimension preferredLayoutSize(Container parent) {
			Dimension buttonSize = UIManager.getDimension("Spinner.arrowButtonSize");
			
			Dimension editorD = spinner.getEditor().getPreferredSize();

			/*
			 * Force the editors height to be a multiple of 2
			 */
			editorD.height = ((editorD.height + 1) / 2) * 2;

			Dimension size = new Dimension(editorD.width, editorD.height);
			size.width += buttonSize.width;
			Insets insets = parent.getInsets();
			size.width += insets.left + insets.right;
			size.height += insets.top + insets.bottom;

			Insets buttonInsets = nextButton.getInsets();
			size.width += (buttonInsets.left + buttonInsets.right);

			return size;
		}

		public void layoutContainer(Container parent) {
			int width = parent.getWidth();
			int height = parent.getHeight();

			Dimension buttonSize = UIManager.getDimension("Spinner.arrowButtonSize");
			
			Insets insets = parent.getInsets();

			int buttonsWidth = buttonSize.width;
			int editorHeight = height - (insets.top + insets.bottom);

			/*
			 * Deal with the spinner's componentOrientation property.
			 */
			int editorX, editorWidth, buttonsX;
			if (parent.getComponentOrientation().isLeftToRight()) {
				editorX = insets.left;
				editorWidth = width - insets.left - buttonsWidth;
				buttonsX = width - buttonsWidth - insets.right;
			} else {
				buttonsX = insets.left;
				editorX = buttonsX + buttonsWidth;
				editorWidth = width - buttonsWidth - insets.right;
			}

			int nextY = insets.top;
			int nextHeight = (height - insets.top - insets.bottom) / 2;
			int previousY = nextY + nextHeight;
			int previousHeight = nextHeight;

			spinner.getEditor().setBounds(editorX, insets.top, editorWidth,
					editorHeight);
			nextButton.setBounds(buttonsX, nextY, buttonsWidth, nextHeight);
			prevButton.setBounds(buttonsX, previousY, buttonsWidth,
					previousHeight);
		}
	}
}
