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
package a03.swing.plaf.basic;

import java.awt.Component;
import java.awt.Window;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JInternalFrame;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;


public class A03BasicUtilities {
	
	public static int getState(Component c) {
		
		int state = 0;
		
		if (c instanceof AbstractButton) {
			ButtonModel model = ((AbstractButton) c).getModel();
	
			if (model.isEnabled()) {
				state = A03ComponentState.ENABLED;
			}
			
			if (model.isSelected()) {
				state |= A03ComponentState.SELECTED;
			}
			
			if (model.isArmed()) {
				state |= A03ComponentState.ARMED;
			}

			if (model.isPressed()) {
				state |= A03ComponentState.PRESSED;
			}
			
			if (model.isRollover()) {
				//state |= A03ComponentState.ROLLOVER;
			}
			
			if (c.isFocusable() && c.isFocusOwner()) {
				state |= A03ComponentState.FOCUSED;
			}			
		} else if (c instanceof JSlider) {
			JSlider slider = (JSlider) c;
			
			if (slider.isEnabled()) {
				state = A03ComponentState.ENABLED;
			}
			
			if (slider.getValue() > 0) {
				state |= A03ComponentState.ARMED;
			}
		} else {
			if (c.isEnabled()) {
				state = A03ComponentState.ENABLED;
			}
		}
	
		return state;
	}

	public static int getTitlePaneState(Component c) {
		int state;
		
		if (c instanceof JInternalFrame) {
			state = ((JInternalFrame) c).isSelected() ? A03ComponentState.ENABLED : 0;
		} else {
			Component parent = SwingUtilities.getAncestorOfClass(JInternalFrame.class, c);
			if (parent != null) {
				state = ((JInternalFrame) parent).isSelected() ? A03ComponentState.ENABLED : 0;
			} else {
				Window window = SwingUtilities.getWindowAncestor(c);
				if (window != null) {
					state = window.isActive() ? A03ComponentState.ENABLED : 0;
				} else {
					state = A03ComponentState.ENABLED;
				}
			}
		}
		
		return state;
	}
	
}

