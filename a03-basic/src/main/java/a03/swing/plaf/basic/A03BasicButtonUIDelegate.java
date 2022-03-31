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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.AbstractButton;
import javax.swing.JToolBar;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

import a03.swing.plaf.A03ButtonUIDelegate;
import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03ToggleButtonUIDelegate;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicButtonUIDelegate implements A03ButtonUIDelegate, A03ToggleButtonUIDelegate, A03ComponentState, A03BasicFonts {

	protected A03FadeTrackerPlugin fadeTracker;
	protected static Stroke focusStroke = new BasicStroke(2.0f);
	protected static InsetsUIResource margin = new InsetsUIResource(2, 10, 2, 10);
	
	public A03BasicButtonUIDelegate() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 1;
		insets.bottom = 1;
		insets.left = 1;
		insets.right = 1;
		
		return insets;
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(FONT_PLAIN_11);
	}
	
	public InsetsUIResource getMargin() {
		return margin;
	}
	
	public boolean contains(Component c, int x, int y) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		return new Rectangle(0, 0, width - 1, height - 1).contains(x, y);
	}
	
	public void paintBackground(Component c, Graphics g) {
		if (c.getParent() instanceof JToolBar) {
			return;
		}
		
		AbstractButton b = (AbstractButton) c;
		
		Insets insets = b.getInsets();
		Insets margin = b.getMargin();
	
		int x = insets.left - margin.left;
		int y = insets.top - margin.top;
		
		int width = b.getWidth();
		int height = b.getHeight();
		
		Image tmpImage = A03GraphicsUtilities.getTempImage(c, width, height);

		width -= x + insets.right - margin.right;
		height -= y + insets.bottom - margin.bottom;

		Graphics2D graphics = (Graphics2D) tmpImage.getGraphics();
	
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	
		graphics.translate(x, y);
		Color background = b.getBackground();
	    if (background instanceof UIResource) {
	    	int state = A03BasicUtilities.getState(c);
	    	
	    	if ((state & ENABLED) != 0) {
		    	if ((state & SELECTED) == 0) {
		    		float fadeLevel = (float) fadeTracker.getFadeLevel(b);
					if (fadeLevel > 0) {
						if (fadeLevel < 1) {
							graphics.setPaint(getBackgroundPaint(ENABLED, 0, 0, width, height));
							graphics.fillRect(0, 0, width, height);
	
							graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
						}
						
						state |= ARMED;
					}
		    	}
	    	}
	    	
			graphics.setPaint(getBackgroundPaint(state, x, y, width, height));
	    } else {
			graphics.setPaint(getBackgroundPaint(ENABLED, x, y, width, height));
			graphics.fillRect(0, 0, width, height);
			
			graphics.setPaint(background);
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
	    }

	    graphics.fillRect(0, 0, width, height);
		
		graphics.dispose();
		
		g.drawImage(tmpImage, 0, 0, c);
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		if (c.getParent() instanceof JToolBar || c.hasFocus()) {
			return;
		}
		
		AbstractButton b = (AbstractButton) c;
	
	    Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	    
		graphics.translate(x, y);
		Color background = b.getBackground();
	    if (background instanceof UIResource) {
	    	int state = A03BasicUtilities.getState(c);
	    	
	    	if (state == ENABLED) {
	    		float fadeLevel = (float) fadeTracker.getFadeLevel(b);
				if (fadeLevel > 0) {
					if (fadeLevel < 1) {
						graphics.setPaint(getBorderPaint(ENABLED, 0, 0, width, height));
						graphics.drawRect(0, 0, width - 1, height - 1);

						graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
					}
					
					state |= ARMED;
				}
	    	}
			
			graphics.setPaint(getBorderPaint(state, 0, 0, width, height));
	    } else {
			graphics.setPaint(getBorderPaint(ENABLED, 0, 0, width, height));
			graphics.drawRect(0, 0, width - 1, height - 1);
			
			graphics.setColor(background);
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
	    }

		graphics.drawRect(0, 0, width - 1, height - 1);

		graphics.dispose();
	}
	
	public void paintFocus(Component c, Graphics g, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {
	    Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	
		int width = c.getWidth();
		int height = c.getHeight();
		
		graphics.setStroke(focusStroke);

	    graphics.setColor(Color.RED);
	    graphics.drawRect(1, 1, width - 3, height - 3);
	    
		graphics.dispose();
	}
	
	public void paintText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		AbstractButton b = (AbstractButton) c;

		graphics.setColor(getForegroundColor(A03BasicUtilities.getState(c)));
		
		int mnemonicIndex = b.getDisplayedMnemonicIndex();
		A03GraphicsUtilities.drawStringUnderlineCharAt(c, graphics, text, mnemonicIndex, x, y);
	
		graphics.dispose();
	}
	
	protected Paint getBackgroundPaint(int state, int x, int y, int width, int height) {
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED)) != 0) {
				return Color.LIGHT_GRAY;
			} else {
				return Color.GRAY;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return Color.DARK_GRAY;
			} else {
				return Color.WHITE;
			}
		}
	}

	protected Color getForegroundColor(int state)  {
		if ((state & ENABLED) != 0 ) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				return Color.WHITE;
			} else {
				return Color.BLACK;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return Color.LIGHT_GRAY;
			} else {
				return Color.GRAY;
			}
		}
	}

	protected Paint getBorderPaint(int state, int x, int y, int width, int height) {
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				return Color.DARK_GRAY;
			} else {
				return Color.BLACK;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return Color.GRAY;
			} else {
				return Color.LIGHT_GRAY;
			}
		}
	}
}
