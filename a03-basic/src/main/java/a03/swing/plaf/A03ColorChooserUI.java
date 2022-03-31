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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicColorChooserUI;

import a03.swing.widget.A03ColorPreviewPanel;

public class A03ColorChooserUI extends BasicColorChooserUI {
	private JPanel previewContainer;

	protected JComponent previewPanel;

	protected ChangeListener changeListener;
	
	private JTabbedPane chooserContainer;

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03ColorChooserUI();
	}

	@Override
	public void installUI(JComponent c) {
		chooser = (JColorChooser) c;

		installDefaults();
		installListeners();

		chooser.setLayout(new BorderLayout());
		
		previewContainer = new JPanel(new BorderLayout());
		
		chooserContainer = new JTabbedPane();
		chooser.add(chooserContainer, BorderLayout.CENTER);

		A03RGBColorChooserPanel chooserPanel = new A03RGBColorChooserPanel(chooser);
		chooserPanel.setPreviewContainer(previewContainer);
		
		AbstractColorChooserPanel[] choosers = { chooserPanel };
		chooser.setChooserPanels(choosers);
		
		installPreviewPanel();
	}

	public void uninstallUI(JComponent c) {
		chooser.removeAll();

		uninstallListeners();
		uninstallDefaultChoosers();
		uninstallDefaults();

		defaultChoosers = null;
		chooser = null;
	}

	protected void installListeners() {
		propertyChangeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				
	            if (prop == JColorChooser.CHOOSER_PANELS_PROPERTY) {
	                AbstractColorChooserPanel[] oldPanels =
	                    (AbstractColorChooserPanel[]) evt.getOldValue();

	                for (AbstractColorChooserPanel oldPanel : oldPanels) {
	                	oldPanel.uninstallChooserPanel(chooser);
	                }
	                chooserContainer.removeAll();
	                
	            	AbstractColorChooserPanel[] newPanels =
	                    (AbstractColorChooserPanel[]) evt.getNewValue();
	                
	                for (AbstractColorChooserPanel newPanel : newPanels) {
	                	newPanel.installChooserPanel(chooser);
		        		chooserContainer.addTab(newPanel.getDisplayName(), newPanel);
	                }	                
	            } else if (prop == JColorChooser.PREVIEW_PANEL_PROPERTY) {
	            	previewPanel = (JComponent) evt.getNewValue();
	        		previewPanel.setForeground(chooser.getColor());

	        		chooser.setPreviewPanel(previewPanel);
	            }
			}
		};
		chooser.addPropertyChangeListener(propertyChangeListener);

		changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ColorSelectionModel model = (ColorSelectionModel) e.getSource();
				if (previewPanel != null) {
					previewPanel.setForeground(model.getSelectedColor());
					previewPanel.repaint();
				}
			}
		};
		chooser.getSelectionModel().addChangeListener(changeListener);
	}

	@Override
	protected void installPreviewPanel() {
		previewPanel = new A03ColorPreviewPanel();
		previewPanel.setBorder(new EmptyBorder(0, 0, 0, 1));
		previewPanel.setForeground(chooser.getColor());

		chooser.setPreviewPanel(previewPanel);
	}

	@Override
	protected void uninstallDefaultChoosers() {
		chooserContainer.removeAll();
	}

	@Override
	protected void installDefaults() {
		LookAndFeel.installColorsAndFont(chooser, "ColorChooser.background", "ColorChooser.foreground", "ColorChooser.font");
	}

	@Override
	protected void uninstallDefaults() {
	}
}
