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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03CheckBoxUIDelegate;
import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicCheckBoxUIDelegate implements A03CheckBoxUIDelegate, A03ComponentState {
	
	private Image image;
	private Image imageFocused;
	private Image imageDisabled;
	private Image imageOver;
	private Image imageOverFocused;
	private Image imageSelected;
	private Image imageSelectedFocused;
	private Image imageSelectedDisabled;
	
	private Image tmpImage;
	
	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicCheckBoxUIDelegate() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);

		int width = getIconWidth();
		int height = getIconHeight();
		
		tmpImage = A03GraphicsUtilities.createImage(null, width, height);
		
		image = A03GraphicsUtilities.createImage(null, width, height);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		paintBox(ENABLED, graphics);
		graphics.dispose();

		imageFocused = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageFocused.getGraphics();
		paintBox(ENABLED, graphics);
		paintFocus(graphics);
		graphics.dispose();

		imageDisabled = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageDisabled.getGraphics();
		paintBox(0, graphics);
		graphics.dispose();

		imageOver = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageOver.getGraphics();
		paintBox(SELECTED | ENABLED, graphics);
		graphics.dispose();
		
		imageOverFocused = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageOverFocused.getGraphics();
		graphics.drawImage(imageOver, 0, 0, null);
		paintFocus(graphics);
		graphics.dispose();
		
		imageSelected = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageSelected.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(imageOver, 0, 0, null);
		paintCheck(SELECTED | ENABLED, graphics);
		graphics.dispose();

		imageSelectedFocused = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageSelectedFocused.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(imageOver, 0, 0, null);
		paintFocus(graphics);
		paintCheck(SELECTED | ENABLED, graphics);
		graphics.dispose();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(imageOver, 0, 0, null);
		paintCheck(SELECTED | ENABLED, graphics);
		graphics.dispose();

		imageSelectedDisabled = A03GraphicsUtilities.createImage(null, width, height);
		graphics = (Graphics2D) imageSelectedDisabled.getGraphics();
		paintBox(SELECTED, graphics);
		paintCheck(SELECTED | ENABLED, graphics);
		graphics.dispose();
	}
	
	protected void paintBox(int state, Graphics2D graphics) {
		int width = getIconWidth();
		int height = getIconHeight();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics.setPaint(Color.GRAY);
		graphics.fillRect(2, 2, width - 4, height - 4);

		graphics.setPaint(Color.BLACK);
		graphics.drawRect(1, 1, width - 3, height - 3);
	}

	protected void paintCheck(int state, Graphics2D graphics) {
		int width = getIconWidth();
		int height = getIconHeight();

		A03BasicGraphicsUtilities.paintCheck(graphics,
				getCheckPaint(state, 3, 1, width - 1, height - 3),
				3, 1, width - 1, height - 3);
	}
	
	protected void paintFocus(Graphics g) {
		// do nothing
	}


	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public int getIconHeight() {
		return 14;
	}
	
	public int getIconWidth() {
		return 14;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y) {
		AbstractButton button = (AbstractButton) c;
		ButtonModel model = button.getModel();
		
		if (model.isEnabled()) {
			if (model.isPressed() || model.isSelected()) {
				if (button.hasFocus() && button.isFocusPainted()) {
					g.drawImage(imageSelectedFocused, x, y, c);
				} else {
					g.drawImage(imageSelected, x, y, c);
				}
			} else {
				Graphics2D graphics = (Graphics2D) tmpImage.getGraphics();

				double fadeLevel = fadeTracker.getFadeLevel(c);
				if (fadeLevel > 0) {
					if (fadeLevel < 1) {
						if (button.hasFocus() && button.isFocusPainted()) {
							graphics.drawImage(imageFocused, x, y, c);
						} else {
							graphics.drawImage(image, x, y, c);
						}
					}
					
					graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) fadeLevel));

					if (button.hasFocus() && button.isFocusPainted()) {
						graphics.drawImage(imageOverFocused, x, y, c);
					} else {
						graphics.drawImage(imageOver, x, y, c);
					}
				} else {
					if (button.hasFocus() && button.isFocusPainted()) {
						graphics.drawImage(imageFocused, x, y, c);
					} else {
						graphics.drawImage(image, x, y, c);
					}
				}
				
				graphics.dispose();

				g.drawImage(tmpImage, x, y, c);
			}
		} else {
			if (model.isSelected()) {
				g.drawImage(imageSelectedDisabled, x, y, c);
			} else {
				g.drawImage(imageDisabled, x, y, c);
			}
		}
	}
	
	public void paintText(Component c, Graphics g, String text, int x, int y) {
        AbstractButton b = (AbstractButton) c;                       

		ButtonModel model = b.getModel();
		
		int state = 0;
		if (model.isEnabled()) {
			state = A03ComponentState.ENABLED;
		}
		g.setColor(getForegroundColor(state));

        int mnemonicIndex = b.getDisplayedMnemonicIndex();
	    A03GraphicsUtilities.drawStringUnderlineCharAt(c, g, text, mnemonicIndex, x, y);
	}
	
	@Override
	public void paintFocus(Component c, Graphics g, Rectangle textRect, Dimension size) {
		// do nothing
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		return null;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing
	}
	
	public Paint getCheckBackgroundPaint(int state, int x, int y, int width,
			int height) {
		return Color.GRAY;
	}

	public Paint getCheckBorderPaint(int state, int x, int y, int width, int height) {
		return Color.BLACK;
	}

	public Paint getCheckPaint(int state, int x, int y, int width, int height) {
		return Color.BLACK;
	}

	public Color getForegroundColor(int state) {
		return Color.BLACK;
	}

}
