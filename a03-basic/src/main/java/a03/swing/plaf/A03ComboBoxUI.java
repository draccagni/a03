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
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import a03.swing.plugin.A03UIPluginManager;

public class A03ComboBoxUI extends BasicComboBoxUI {

	private final static DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03ComboBoxUI();
	}

	class PropertyChangeListenerImpl implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("enabled")) {
				Color background = comboBox.getBackground();
				if (background instanceof UIResource) {
					if (comboBox.isEnabled()) {
						comboBox.setBackground(UIManager.getColor("ComboBox.background"));
					} else {
						comboBox.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
					}
				}
			} else if (evt.getPropertyName().equals("background")) {
				updateEditorBackgroundIfNecessary(comboBox);
			} else if (evt.getPropertyName().equals("UI")) {
				updateEditorBackgroundIfNecessary(comboBox);
				
				popup.getList().updateUI();
			} else if (evt.getPropertyName().equals("editable")) {
				if (comboBox != null) {
					if (!comboBox.isEnabled()) {
						Color background = comboBox.getBackground();
						if (background instanceof UIResource) {
							comboBox.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
						}
					}
					
					ComboBoxEditor editor = comboBox.getEditor();
					if (editor != null) {
						editor.getEditorComponent().setComponentOrientation(comboBox.getComponentOrientation());
					}
					
					updateEditorBackgroundIfNecessary(comboBox);
				}
			}
		}
	}
	
	class A03ComboPopup extends BasicComboPopup {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5637107226644208855L;

		public A03ComboPopup(JComboBox combo) {
			/*
			 * Workaround to fix Swing bug on uninstallUI
			 */
			super(combo);
		}
		
		@Override
		protected void configurePopup() {
			super.configurePopup();
			
			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // UIManager.getBorder("PopupMenu.border"));
		}

		
		@Override
		protected JScrollPane createScroller() {
			JScrollPane scrollPane = super.createScroller();
			
			// remove A03ScrollPaneViewNavigator button
			scrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, null);

			scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, null);

			/*
			 * XXX Fix the problem of wrong side of the scrollbar for RTL
			 * locale(s) 
			 */
			scrollPane.setComponentOrientation(comboBox.getComponentOrientation());
			return scrollPane;
		}
		
		@Override
		protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
			Rectangle rect = super.computePopupBounds(px, py, pw, ph);
			
			ListModel model = list.getModel();
			
			ListCellRenderer renderer = this.list.getCellRenderer();
			int width = 0;
			for (int index = 0; index < model.getSize(); index++) {
				Component c = renderer.getListCellRendererComponent(this.list,
						model.getElementAt(index), index, true, true);
				width = Math.max(width, getSizeForComponent(c).width);
				
			}
			
			Insets insets = list.getInsets();
			width += insets.left + insets.right;

			JScrollBar verticalBar = this.scroller.getVerticalScrollBar();
			if (verticalBar != null && verticalBar.isVisible()) {
				width += verticalBar.getPreferredSize().width;
			}

			rect.width = Math.max(width, comboBox.getBounds().width);

			return rect;
		}
		
		@Override
		public void setVisible(boolean b) {
			/*
			 * to repaint ComboBox border
			 */
			comboBox.repaint(); 
			
			super.setVisible(b);
		}
	}


	private PropertyChangeListener propertyChangeListener = new PropertyChangeListenerImpl();

	private void updateEditorBackgroundIfNecessary(JComboBox c) {
		if (c != null) {
			Color background = c.getBackground();
			if (c.isEditable()) {
				ComboBoxEditor editor = c.getEditor();
				if (editor != null) {
					editor.getEditorComponent().setBackground(background);
				}
			}
		}
	}
	
	private A03ComboBoxUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		uiDelegate = A03SwingUtilities.getUIDelegate(c, A03ComboBoxUIDelegate.class);
		
		super.installUI(c);
		
		c.setForeground(uiDelegate.getForeground());
		c.setBackground(uiDelegate.getBackground());
		((JComboBox) c).setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
		
		listBox.setSelectionForeground(uiDelegate.getSelectionForeground());
//		listBox.setSelectionBackground(uiDelegate.getSelectionBackground());

		c.setFont(uiDelegate.getFont());

		A03UIPluginManager.getInstance().getUIPlugins().installUI(comboBox);
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		updateEditorBackgroundIfNecessary(comboBox);
	}

	@Override
	protected void installListeners() {
		super.installListeners();

		comboBox.addPropertyChangeListener(propertyChangeListener);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		/*
		 * Workaround to assure a popup not null.
		 * FIX Swing bug
		 */
		if (comboBox == null) {
			comboBox = new JComboBox();
		}

		if (popup == null) {
			popup = createPopup();
		}

		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(comboBox);
		
		super.uninstallUI(c);
	}

	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();

		comboBox.removePropertyChangeListener(propertyChangeListener);
	}

	@Override
	protected JButton createArrowButton() {
		A03ArrowButton button = new A03ArrowButton(SwingConstants.SOUTH, uiDelegate);

		return button;
	}

	@Override
	protected ComboPopup createPopup() {
		return new A03ComboPopup(comboBox);
	}

	protected ListCellRenderer createRenderer() {
		A03ListCellRenderer listCellRender = new A03ListCellRenderer();
		// Kirill Grouchnikov 2007/11/02 {
		listCellRender.setComponentOrientation(A03SwingUtilities
				.isLeftToRight(comboBox) ? ComponentOrientation.LEFT_TO_RIGHT
				: ComponentOrientation.RIGHT_TO_LEFT);
		// } Kirill Grouchnikov 2007/11/02

		return listCellRender;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();

		hasFocus = comboBox.hasFocus();
		if (!comboBox.isEditable()) {
			Rectangle r = rectangleForCurrentValue();

//			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//					RenderingHints.VALUE_ANTIALIAS_ON);

			paintCurrentValue(graphics, r, hasFocus);
		}
		
		graphics.dispose();
	}

	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		ListCellRenderer renderer = comboBox.getRenderer();
		Component c;

		if (hasFocus && !isPopupVisible(comboBox)) {
			c = renderer.getListCellRendererComponent(listBox, comboBox
					.getSelectedItem(), -1, true, false);
		} else {
			c = renderer.getListCellRendererComponent(listBox, comboBox
					.getSelectedItem(), -1, false, false);
		}
		c.setFont(comboBox.getFont());
		if (comboBox.isEnabled()) {
			c.setForeground(uiDelegate.getForeground());
			c.setBackground(uiDelegate.getBackground());
		} else {
			c.setForeground(uiDelegate.getDisabledForeground());
			c.setBackground(uiDelegate.getDisabledBackground());
		}

		// Fix for 4238829: should lay out the JPanel.
		boolean shouldValidate = false;
		if (c instanceof JPanel) {
			shouldValidate = true;
		}

		// Fix RTL bug
		if (c instanceof JLabel) {
			((JLabel) c).setHorizontalAlignment(comboBox.getComponentOrientation().isLeftToRight() ? SwingConstants.LEFT : SwingConstants.RIGHT);
		}
		
		currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y,
				bounds.width, bounds.height, shouldValidate);
	}

	protected Dimension getSizeForComponent(Component comp) {
		currentValuePane.add(comp);
		comp.setFont(comboBox.getFont());
		Dimension d = comp.getPreferredSize();
		currentValuePane.remove(comp);
		return d;
	}
	
	@Override
	protected Dimension getDefaultSize() {
		ListCellRenderer renderer = comboBox.getRenderer();
		if (renderer == null) {
			renderer = defaultListCellRenderer;
		}

		Dimension d = getSizeForComponent(renderer.getListCellRendererComponent(listBox, null, -1, true, true));
		return new Dimension(d.width, d.height);
	}
	
    protected ComboBoxEditor createEditor() {
    	ComboBoxEditor editor = new A03ComboBoxEditor.UIResource();
    	
    	Component editorComponent = editor.getEditorComponent();

    	if (editorComponent instanceof JTextField) {
        	((JTextField) editorComponent).setHorizontalAlignment(comboBox.getComponentOrientation().isLeftToRight() ? SwingConstants.LEFT : SwingConstants.RIGHT);
    	}
    	
        return editor;
    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicComboBoxUI#createLayoutManager()
	 */
	@Override
	protected LayoutManager createLayoutManager() {
		return new ComboBoxLayoutManager();
	}

	/**
	 * Layout manager for combo box.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class ComboBoxLayoutManager extends
			BasicComboBoxUI.ComboBoxLayoutManager {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 */
		@Override
		public void layoutContainer(Container parent) {
			JComboBox cb = (JComboBox) parent;

			int width = cb.getWidth();
			int height = cb.getHeight();

			Insets insets = A03ComboBoxUI.this.getInsets();
			
            int buttonSize = height - (insets.top + insets.bottom);

			if (A03ComboBoxUI.this.arrowButton != null) {
				if (cb.getComponentOrientation().isLeftToRight()) {
					A03ComboBoxUI.this.arrowButton.setBounds(width
							- buttonSize - insets.right, insets.top, buttonSize, buttonSize);
				} else {
					A03ComboBoxUI.this.arrowButton.setBounds(insets.left, insets.top,
							buttonSize, buttonSize);
				}
			}
			if (A03ComboBoxUI.this.editor != null) {
				A03ComboBoxUI.this.editor.setBounds(new Rectangle(A03ComboBoxUI.this
						.rectangleForCurrentValue()));
			}
		}
	}

    /**
     * Returns the area that is reserved for drawing the currently selected item.
     */
    protected Rectangle rectangleForCurrentValue() {
        int width = comboBox.getWidth();
        int height = comboBox.getHeight();
        Insets insets = getInsets();
        int buttonSize = height - (insets.top + insets.bottom);
		if ( arrowButton != null ) {
	            buttonSize = arrowButton.getWidth();
		}
		if(comboBox.getComponentOrientation().isLeftToRight()) {
		    return new Rectangle(insets.left, insets.top,
				     width - (insets.left + insets.right + buttonSize),
				     buttonSize);
		}
		else {
		    return new Rectangle(insets.left + buttonSize, insets.top,
				     width - (insets.left + insets.right + buttonSize),
				     buttonSize);
		}
    }
}
