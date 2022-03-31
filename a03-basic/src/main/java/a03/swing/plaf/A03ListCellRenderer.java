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
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;


public class A03ListCellRenderer extends JLabel implements ListCellRenderer, UIResource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5281773338872964596L;

	public A03ListCellRenderer() {
		setOpaque(false);
	}
		
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
		    setText(String.valueOf(value));
		}
		
		if (cellHasFocus) {
			if (isSelected) {
				setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
			} else {
				setBorder(UIManager.getBorder("List.cellNoFocusBorder"));
			}
		} else {
			setBorder(UIManager.getBorder("List.cellNoFocusBorder"));
		}
		
		setHorizontalAlignment(A03SwingUtilities.isLeftToRight(list) ? LEFT : RIGHT);

		return this;
	}
    
    // Kirill Grouchnikov 2007/11/02 {
    /*
     * @see org.jvnet.substance.SubstanceDefaultComboBoxRenderer#getPreferredSize()
     */
	@Override
	public Dimension getPreferredSize() {
		Dimension size;

		if ((this.getText() == null) || (this.getText().equals(""))) {
			this.setText(" ");
			size = super.getPreferredSize();
			this.setText("");
		} else {
			size = super.getPreferredSize();
		}

		return size;
	}
	// } Kirill Grouchnikov 2007/11/02
	
    public static class UIResource extends A03ListCellRenderer
    	implements javax.swing.plaf.UIResource {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6561778734188671505L;
    }
}
