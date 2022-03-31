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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

import a03.swing.plugin.A03UIPluginManager;

public class A03SliderUI extends BasicSliderUI {
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03SliderUI((JSlider) c);
	}
	
	public A03SliderUI(JSlider c) {
		super(c);
	}

	private A03SliderUIDelegate uiDelegate;

	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03SliderUIDelegate.class);

		super.installUI(c);
		
		slider.setFont(uiDelegate.getFont());
	    slider.setBackground(uiDelegate.getBackground());

		scrollListener.setScrollByBlock(false);

		A03UIPluginManager.getInstance().getUIPlugins().installUI(slider);
	}

	@Override
	public void uninstallUI(JComponent c) {
		// TODO
		// reset scrollListener.setScrollByBlock(...);
		
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(slider);

		super.uninstallUI(c);
	}
	
	@Override
	public void paintThumb(Graphics g) {
		uiDelegate.paintThumb(slider, g, trackRect, thumbRect);
	}

	@Override
    public void paintTrack(Graphics g)  {
		uiDelegate.paintTrack(slider, g, trackRect, thumbRect);
	}
	
	@Override
	public void paintFocus(Graphics g) {
		Rectangle rect = new Rectangle(focusRect);
		
		if (slider.getLabelTable() == null) {
			if (slider.getOrientation() == JSlider.HORIZONTAL) {
				rect.height--;
			} else {
				rect.width--;
			}
		}
		
		uiDelegate.paintFocus(slider, g, rect);
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
    	
		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);

		super.update(graphics, c);
		
		graphics.dispose();
	}	
}
