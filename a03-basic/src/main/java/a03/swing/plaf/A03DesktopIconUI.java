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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;


public class A03DesktopIconUI extends BasicDesktopIconUI {
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03DesktopIconUI();
	}
	
	private A03DesktopIconUIDelegate uiDelegate;
	
	@Override
    protected void installComponents() {
    	desktopIcon.setLayout(new BorderLayout());
    	
    	desktopIcon.setBorder(UIManager.getBorder("InternalFrame.border"));
    	this.uiDelegate = A03SwingUtilities.getUIDelegate(desktopIcon, A03DesktopIconUIDelegate.class);
    	
		desktopIcon.setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
    	
    	iconPane = new A03InternalFrameTitlePane(frame);
    	desktopIcon.add(iconPane, BorderLayout.CENTER);
    	
    	desktopIcon.setBackground(uiDelegate.getBackground());
    }
	
	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
    	
		if (c.isOpaque()) {
			graphics.setColor(c.getBackground());
		    graphics.fillRect(0, 0, c.getWidth(),c.getHeight());
		}
    	
		paint(graphics, c);

		graphics.dispose();
	}	
}
