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
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JSlider;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SliderUIDelegate;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicSliderUIDelegate implements A03SliderUIDelegate, A03ComponentState {

	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicSliderUIDelegate() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing
	}
	
	public void paintFocus(Component c, Graphics g, Rectangle focusRect) {
		int width = c.getWidth();
		int height = c.getHeight();
		
	    Graphics2D graphics = (Graphics2D) g.create(0, 0, width, height);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	
		graphics.setColor(Color.RED);
		graphics.drawRect(0, 0, width - 1, height - 1);

		graphics.dispose();
	}
	
	public void paintThumb(Component c, Graphics g, Rectangle trackRect,
			Rectangle thumbRect) {
		
		JSlider slider = (JSlider) c;

		int orientation = slider.getOrientation();
		
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		float fadeLevel;
		if (slider.getPaintTrack()) {

			int x = 0;
			int y = 0;
			int width = 0;
			int height = 0;
			
			int trackSize = 6;
			
			// Draw slider track here
			if (orientation == JSlider.HORIZONTAL) {
				x = trackRect.x;
				y = trackRect.y + trackRect.height / 2 - 3;
				
				graphics.setPaint(Color.GRAY);			
				graphics.fillRect(x, y, trackRect.width, trackSize);
				
				graphics.setPaint(Color.DARK_GRAY);			
				graphics.drawRect(x, y, trackRect.width - 1, trackSize - 1);
			} else {
				x = trackRect.x + trackRect.width / 2 - 3;
				y = trackRect.y;
				
				graphics.setPaint(Color.GRAY);			
				graphics.fillRect(x, y, trackSize, trackRect.height);
				
				graphics.setPaint(Color.DARK_GRAY);			
				graphics.drawRect(x, y, trackSize - 1, trackRect.height - 1);
			}
			
			if (slider.getValue() > slider.getMinimum()) {
				if (orientation == JSlider.HORIZONTAL) {
					if (A03SwingUtilities.isLeftToRight(slider)) {
						if (slider.getValue() == slider.getMaximum()) {
							width = trackRect.width - 2;
						} else {
							width = thumbRect.x - trackRect.x + thumbRect.width / 2 - 1;
						}
						x = trackRect.x + 1;
						y = trackRect.y + trackRect.height / 2 - 2;
						height = 4;
					} else {
						if (slider.getValue() == slider.getMaximum()) {
							x = trackRect.x + 1;
							width = trackRect.width - 2;
						} else {
							x = thumbRect.x + thumbRect.width / 2 + 1;
							width = trackRect.width - (thumbRect.x + thumbRect.width / 2 - trackRect.x) - 1;
						}
						
						y = trackRect.y + trackRect.height / 2 - 2;
						height = 4;
					}					
				} else {
					if (slider.getValue() == slider.getMaximum()) {
						y = trackRect.y + 1;
						height = trackRect.height - 2;
					} else {
						y = thumbRect.y + thumbRect.height / 2 + 1;
						height = trackRect.height - (thumbRect.y + thumbRect.height / 2 - trackRect.y) - 1;
					}
	
					x = trackRect.x + trackRect.width / 2 - 2;
					width = 4;
				}

				graphics.setPaint(Color.WHITE);
				graphics.fillRect(x, y, width, height);

				graphics.setPaint(Color.BLACK);
				graphics.drawRect(x - 1, y - 1, width + 1, height + 1);
			}

			fadeLevel = (float) fadeTracker.getFadeLevel(slider);
		} else {
			fadeLevel = 1.0f;
		}

		if (fadeLevel > 0) {
			Image image = A03GraphicsUtilities.createImage(c, thumbRect.width, thumbRect.height);
			
			Graphics2D imageGraphics = (Graphics2D) image.getGraphics();
			imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);

			if (fadeLevel < 1) {
				imageGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
			}

			imageGraphics.setPaint(Color.WHITE);
			imageGraphics.fillRect(0, 0, thumbRect.width - 1, thumbRect.height - 1);

			imageGraphics.setPaint(Color.BLACK);

			imageGraphics.drawRect(0, 0, thumbRect.width - 1, thumbRect.height - 1);

			imageGraphics.dispose();
			
			graphics.drawImage(image, thumbRect.x, thumbRect.y, c);
		}
		
		graphics.dispose();
	}
	
	protected Shape getThumbBorderShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	protected Shape getTrackBorderShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	protected Shape getTrackBackgroundShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}

	public void paintTrack(Component c, Graphics g, Rectangle trackRect,
			Rectangle thumbRect) {
		// do nothing
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}

	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public Color getBackgroundColor() {
		return Color.WHITE;
	}

	public Paint getBackgroundPaint(int state, int orientation, int x, int y,
			int width, int height) {
		return null;
	}

	public Paint getBorderPaint(int state, int orientation, int x, int y,
			int width, int height) {
		return null;
	}

	public Color getForegroundColor() {
		return Color.BLACK;
	}
}
