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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.UIResource;


public class A03ArrowButton extends JButton implements UIResource {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3715017185739912842L;
	
	private static final Dimension prefferedSize = new Dimension(16, 16); 
	private static final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE); 
	private static final Dimension minimumSize = new Dimension(5, 5); 

	protected int direction;
	
	private A03ArrowButtonUIDelegate uiDelegate;
	
	private Border border;

    public A03ArrowButton(int direction, A03ArrowButtonUIDelegate delegate) {
    	this.direction = direction;
    	
    	this.uiDelegate = delegate;

    	this.border = A03BorderFactory.createArrowButtonDelegatedBorder(uiDelegate);
    }
    
    @Override
    public void setUI(ButtonUI ui) {
    	super.setUI(ui);
    }
    
    @Override
    public Border getBorder() {
    	return border;
    }
    
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

    public Dimension getPreferredSize() {
        return prefferedSize;
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

    public Dimension getMaximumSize() {
        return maximumSize;
    }

	public boolean isFocusTraversable() {
	    return false;
	}
	
	@Override
	public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g.create();
        
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		Container parent = getParent();
		if (parent instanceof JComboBox) {
			A03ComboBoxUIDelegate comboBoxDelegate = (A03ComboBoxUIDelegate) A03SwingUtilities.getUIDelegate((JComponent) parent, A03ComboBoxUIDelegate.class);
			
			comboBoxDelegate.paintArrowBackground(this, graphics);
		}
		
//		paintBorder(graphics);

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		uiDelegate.paintArrow(this, graphics, direction);
		
		graphics.dispose();
	}
}
