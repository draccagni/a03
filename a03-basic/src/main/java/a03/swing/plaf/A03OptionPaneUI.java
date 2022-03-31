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

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.text.JTextComponent;


public class A03OptionPaneUI extends BasicOptionPaneUI {
	
    public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
    	return new A03OptionPaneUI();
    }
    
    private Icon warningIcon;
    private Icon questionIcon;
    private Icon informationIcon;
    private Icon errorIcon;
    
	private A03OptionPaneUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03OptionPaneUIDelegate.class);

		super.installUI(c);
		
		optionPane.setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
		optionPane.setFont(uiDelegate.getFont());
		warningIcon = A03IconFactory.createWarningIcon(uiDelegate);
		questionIcon = A03IconFactory.createQuestionIcon(uiDelegate);
		informationIcon = A03IconFactory.createInformationIcon(uiDelegate);
		errorIcon = A03IconFactory.createErrorIcon(uiDelegate);
	}
	
    @Override
    protected void installDefaults() {
    	super.installDefaults();
    	
		optionPane.setBackground(uiDelegate.getBackground());
    }
    
    
    
    protected Icon getIconForType(int messageType) {
    	switch(messageType) {
    	case 0:
    	    return errorIcon;
    	case 1:
    	    return informationIcon;
    	case 2:
    	    return warningIcon;
    	case 3:
    	    return questionIcon;
    	default:
    		return null;
    	}
    }

	
    @Override
    protected Container createButtonArea() {
    	Container buttonArea = super.createButtonArea();
    	
    	setBackground(buttonArea, uiDelegate.getBackground());
		((JComponent) buttonArea).setBorder(A03BorderFactory.createOptionPaneButtonAreaBorder(uiDelegate));
    	
    	return buttonArea;
    }
    
	@Override
	protected Container createMessageArea() {
		Container messageArea = super.createMessageArea();
		
		setBackground(messageArea, uiDelegate.getMessageAreaBackground());
		((JComponent) messageArea).setBorder(A03BorderFactory.createOptionPaneMessageAreaBorder(uiDelegate));

		return messageArea;
	}
	
	private void setBackground(Container container, Color background) {
		for (Component component : container.getComponents()) {
			if (
					component instanceof JButton ||
					component instanceof JTextComponent ||
					component instanceof JComboBox
				) {
				continue;
			}
			
			if (component instanceof Container) {
				setBackground((Container) component, background);
			}
		}
		
		container.setBackground(background);
	}

}
