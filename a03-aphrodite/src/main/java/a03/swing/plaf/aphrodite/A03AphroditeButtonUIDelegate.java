/*
 * Copyright (c) 2003-2009 Davide Raccagni. All Rights Reserved.
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
package a03.swing.plaf.aphrodite;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.AbstractButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.basic.A03BasicButtonUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeButtonUIDelegate extends A03BasicButtonUIDelegate implements A03AphroditePaints {

	public A03AphroditeButtonUIDelegate() {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 2;
		insets.bottom = 2;
		insets.left = 2;
		insets.right = 2;
		
		return insets;
	}
	
	public boolean contains(Component c, int x, int y) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		return A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 2).contains(x, y);
	}
	
	public void paintBackground(Component c, Graphics g) {
		if (c.getParent() instanceof JToolBar) {
			return;
		}
		
		AbstractButton b = (AbstractButton) c;
		
		Insets insets = b.getInsets();
		Insets margin = b.getMargin();
	
		int x = insets.left - margin.left;
		int y = insets.top - margin.top ;
		
		int width = b.getWidth();
		int height = b.getHeight();

		width -= x + insets.right - margin.right;
		height -= y + insets.bottom - margin.bottom;
		
		Image tmpImage = A03GraphicsUtilities.getTempImage(c, width, height);

		Graphics2D graphics = (Graphics2D) tmpImage.getGraphics();
	
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	
//		graphics.translate(x, y);
		
		Shape shape = A03GraphicsUtilities.createRoundRectangle(0, 0, width, height, 2);
    	int state = A03BasicUtilities.getState(c);
    	
    	if ((state & ENABLED) != 0) {
	    	if ((state & SELECTED) == 0) {
	    		float fadeLevel = (float) fadeTracker.getFadeLevel(b);
				if (fadeLevel > 0) {
					if (fadeLevel < 1) {
						graphics.setPaint(getBackgroundPaint(ENABLED, 0, 0, width, height));
						graphics.fill(shape);

						graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
					}
					
					state |= ARMED;
				}
	    	}
    	}
    	
		graphics.setPaint(getBackgroundPaint(state, 0, 0, width, height));
	    graphics.fill(shape);
		
		graphics.dispose();
		
		g.drawImage(tmpImage, x, y, c);
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
	    
		Shape shape = A03GraphicsUtilities.createRoundRectangle(1, 1, width - 3, height - 3, 2);

    	int state = A03BasicUtilities.getState(c);
    	
    	if (state == ENABLED) {
	    	if ((state & SELECTED) == 0) {
	    		float fadeLevel = (float) fadeTracker.getFadeLevel(b);
				if (fadeLevel > 0) {
					if (fadeLevel < 1) {
						graphics.setPaint(getBorderPaint(ENABLED, 1, 1, width - 3, height - 3));
						graphics.draw(shape);

						graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
					}
					
					state |= ARMED;
				}
	    	}
    	}
		graphics.setPaint(getBorderPaint(state, 1, 1, width - 3, height - 3));
		graphics.draw(shape);

		Shape outerShape = A03GraphicsUtilities.createRoundRectangle(0, 0, width - 1, height - 1, 2);
		graphics.setPaint(getOuterBorderPaint(state, 0, 0, width, height));
		graphics.draw(outerShape);

		graphics.dispose();
	}
	
	public void paintFocus(Component c, Graphics g, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		Shape shape = A03GraphicsUtilities.createRoundRectangle(1, 1, width - 3, height - 3, 2);
	    A03AphroditeGraphicsUtilities.paintBorderShadow((Graphics2D) g, 2, shape,
	    		FOCUS_COLOR,
                UIManager.getColor("control"));
	}
	
	public void paintText(Component c, Graphics g, String text, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		AbstractButton b = (AbstractButton) c;
		int mnemonicIndex = b.getDisplayedMnemonicIndex();

		Color fg = c.getForeground();
		if (fg instanceof ColorUIResource) {
			int state = A03BasicUtilities.getState(c);
			
			fg =  getForegroundColor(state);
		}
		
		graphics.setColor(fg);

//		int state = A03BasicUtilities.getState(c);
		
//		graphics.setColor(getShadowForegroundColor(state));
//		A03GraphicsUtilities.drawStringUnderlineCharAt(c, graphics, text, mnemonicIndex, x, y + 1); 

		graphics.setColor(fg);
		A03GraphicsUtilities.drawStringUnderlineCharAt(c, graphics, text, mnemonicIndex, x, y); 

		graphics.dispose();
	}
	
	protected Paint getBackgroundPaint(int state, int x, int y, int width, int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED)) != 0) {
				colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_BACKGROUND_ENABLED_COLORS;
			}
		} else {
			if ((state & SELECTED) != 0) {
				colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
			}
		}
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}

	protected Color getShadowForegroundColor(int state)  {
		if ((state & ENABLED) != 0 ) {
//			if ((state & SELECTED) != 0) {
//				return BUTTON_SHADOW_FOREGROUND_ENABLED_ARMED_COLOR;
//			} else {
				return BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR;
//			}
		} else {
//			if ((state & SELECTED) != 0) {
//				return BUTTON_SHADOW_FOREGROUND_DISABLED_ARMED_COLOR;
//			} else {
				return BUTTON_SHADOW_FOREGROUND_DISABLED_COLOR;
//			}
		}
	}
	
	protected Color getForegroundColor(int state)  {
		if ((state & ENABLED) != 0 ) {
			if ((state & SELECTED) != 0) {
				return BUTTON_FOREGROUND_ENABLED_ARMED_COLOR;
			} else {
				return BUTTON_FOREGROUND_ENABLED_COLOR;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return BUTTON_FOREGROUND_DISABLED_ARMED_COLOR;
			} else {
				return BUTTON_FOREGROUND_DISABLED_COLOR;
			}
		}
	}

	protected Paint getOuterBorderPaint(int state, int x, int y, int width, int height) {
		Color[] colors;
		
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				colors = BUTTON_OUTER_BORDER_ENABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_OUTER_BORDER_ENABLED_COLORS;
			}
		} else {
			if ((state & SELECTED) != 0) {
				colors = BUTTON_OUTER_BORDER_DISABLED_ARMED_COLORS;
			} else {
				colors = BUTTON_OUTER_BORDER_DISABLED_COLORS;
			}
		}
		
		return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
	}

	protected Paint getBorderPaint(int state, int x, int y, int width, int height) {
		if ((state & ENABLED) != 0) {
			if ((state & (SELECTED | ARMED | FOCUSED)) != 0) {
				return BUTTON_BORDER_ENABLED_ARMED_COLOR;
			} else {
				return BUTTON_BORDER_ENABLED_COLOR;
			}
		} else {
			if ((state & SELECTED) != 0) {
				return BUTTON_BORDER_DISABLED_ARMED_COLOR;
			} else {
				return BUTTON_BORDER_DISABLED_COLOR;
			}
		}
	}
}
