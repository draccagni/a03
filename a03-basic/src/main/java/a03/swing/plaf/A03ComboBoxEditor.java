/*
k * Copyright (c) 2003-2008 Davide Raccagni. All Rights Reserved.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class A03ComboBoxEditor extends BasicComboBoxEditor {

	public A03ComboBoxEditor() {
        editor = new BorderlessTextField("", 9);
    }

    static class BorderlessTextField extends JTextField implements PropertyChangeListener {
        /**
		 * 
		 */
		private static final long serialVersionUID = -6922542792165546161L;

		public BorderlessTextField(String value, int n) {
            super(value, n);

            setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));

            setHorizontalAlignment(A03SwingUtilities.isLeftToRight(this) ? LEFT : RIGHT);

            addPropertyChangeListener(this);
        }
        
        /*
         * XXX: invoking SwingUtilities.updateComponentTreeUI on ComboBox
         */
        public void propertyChange(PropertyChangeEvent evt) {
        	if (evt.getPropertyName().equals("UI")) {
                setBorder(null);
        	} else if (evt.getPropertyName().equals("componentOrientation")) {
                setHorizontalAlignment(A03SwingUtilities.isLeftToRight(this) ? LEFT : RIGHT);
        	}
        }

        // workaround for 4530952
        public void setText(String s) {
            if (getText().equals(s)) {
                return;
            }
            super.setText(s);
        }

        public void setBorder(Border b) {
            if (!(b instanceof UIResource)) {
                super.setBorder(b);
            }
        }
    }

    public static class UIResource extends A03ComboBoxEditor
    	implements javax.swing.plaf.UIResource {
    }
}

