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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;


public class A03ToolBarSeparatorUI extends BasicToolBarSeparatorUI {

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03ToolBarSeparatorUI();
	}
	
	private A03ToolBarSeparatorUIDelegate uiDelegate;

	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03ToolBarSeparatorUIDelegate.class);

		super.installUI(c);
	}
	
    public void paint(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g;
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		uiDelegate.paintSeparator(c, g);
    }
    
    /*
     * @see org.jvnet.substance.SubstanceToolBarSeparatorUI@getPreferredSize(javax.swing.JComponent)
     */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension size = ((JToolBar.Separator) c).getSeparatorSize();

		if (size != null) {
			size = size.getSize();
		} else {
			size = new Dimension(6, 6);

			if (((JSeparator) c).getOrientation() == SwingConstants.VERTICAL) {
				size.height = 0;
			} else {
				size.width = 0;
			}
		}
		return size;
	}

    /*
     * @see org.jvnet.substance.SubstanceToolBarSeparatorUI#getMaximumSize(javax.swing.JComponent)
     */
	public Dimension getMaximumSize(JComponent c) {
		Dimension pref = getPreferredSize(c);
		if (((JSeparator) c).getOrientation() == SwingConstants.VERTICAL) {
			return new Dimension(pref.width, Short.MAX_VALUE);
		} else {
			return new Dimension(Short.MAX_VALUE, pref.height);
		}
	}

	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		if (c.isOpaque()) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
	    	
			graphics.setColor(c.getBackground());
		    graphics.fillRect(0, 0, c.getWidth(),c.getHeight());
		}
		
    	paint(graphics, c);
    	
    	graphics.dispose();
	}	
}
