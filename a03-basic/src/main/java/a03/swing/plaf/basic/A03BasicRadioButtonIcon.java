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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.plaf.UIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicRadioButtonIcon implements Icon, UIResource, A03ComponentState {
	
	private Image image;
	private Image imageDisabled;
	private Image imageOver;
	private Image imageSelected;
	private Image imageSelectedDisabled;
	
	private int iconWidth;
	private int iconHeight;

	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicRadioButtonIcon(int iconWidth, int iconHeight) {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);

		this.iconWidth = iconWidth;
		
		this.iconHeight = iconHeight;

		image = A03GraphicsUtilities.createImage(null, iconWidth, iconHeight);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
    	
		paintRadio(ENABLED, graphics);
		graphics.dispose();

		imageDisabled = A03GraphicsUtilities.createImage(null, iconWidth, iconHeight);
		graphics = (Graphics2D) imageDisabled.getGraphics();
    	
		paintRadio(0, graphics);
		graphics.dispose();

		imageOver = A03GraphicsUtilities.createImage(null, iconWidth, iconHeight);
		graphics = (Graphics2D) imageOver.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		paintRadio(ENABLED | ARMED, graphics);

		graphics.dispose();
		
		imageSelected = A03GraphicsUtilities.createImage(null, iconWidth, iconHeight);
		graphics = (Graphics2D) imageSelected.getGraphics();
		graphics.drawImage(imageOver, 0, 0, null);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		paintRadioSelected(SELECTED | ENABLED, graphics);
	    
		graphics.dispose();

		imageSelectedDisabled = A03GraphicsUtilities.createImage(null, iconWidth, iconHeight);

		graphics = (Graphics2D) imageSelectedDisabled.getGraphics();

		paintRadio(SELECTED, graphics);
		paintRadioSelected(SELECTED, graphics);
	    
		graphics.dispose();
	}

	protected void paintRadio(int state, Graphics2D graphics) {
		int width = getIconWidth();
		int height = getIconHeight();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		

		graphics.setPaint(Color.GRAY);
		graphics.fillOval(2, 2, width - 5, height - 5);

		graphics.setPaint(Color.DARK_GRAY);
		graphics.drawOval(1, 1, width - 4, height - 4);
	}
	
	protected void paintRadioSelected(int state, Graphics2D graphics) {
		Graphics2D graphics0 = (Graphics2D) graphics.create();
		int width = getIconWidth();
		int height = getIconHeight();
		
		graphics0.translate((width - 6) / 2, (height - 6) / 2);
		graphics0.setPaint(Color.BLACK);
		graphics0.fillOval(0, 0, 5, 5);
		
		graphics0.dispose();
	}
	
	public int getIconWidth() {
		return iconWidth;
	}

	public int getIconHeight() {
		return iconHeight;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.translate(x, y + 1);
		
		if (model.isEnabled()) {
			if (model.isSelected()) {
				graphics.drawImage(imageSelected, 0, 0, c);
			} else if (model.isArmed()) {
				graphics.drawImage(imageOver, 0, 0, c);
			} else {
				if (c instanceof JRadioButtonMenuItem) {
					graphics.drawImage(image, 0, 0, c);
				} else {
					double fadeLevel = fadeTracker.getFadeLevel(c);
					if (fadeLevel > 0) {
						if (fadeLevel < 1) {
							graphics.drawImage(image, 0, 0, c);
						}
						
						graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) fadeLevel));
						graphics.drawImage(imageOver, 0, 0, c);
					} else {
						graphics.drawImage(image, 0, 0, c);
					}
				}
				
			}
		} else {
			if (model.isSelected()) {
				graphics.drawImage(imageSelectedDisabled, 0, 0, c);
			} else {
				graphics.drawImage(imageDisabled, 0, 0, c);
			}
		}
		
		graphics.dispose();
	}
}
