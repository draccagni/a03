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
package a03.swing.plaf.aphrodite;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plaf.basic.A03BasicSliderUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;


public class A03AphroditeSliderUIDelegate extends A03BasicSliderUIDelegate implements A03AphroditePaints {

	public A03AphroditeSliderUIDelegate() {
	}
	
	public void paintFocus(Component c, Graphics g, Rectangle focusRect) {
//		int width = c.getWidth();
//		int height = c.getHeight();
//		
//	    Graphics2D graphics = (Graphics2D) g.create(0, 0, width, height);
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//	
//	    A03AphroditeGraphicsUtilities.paintBorderShadow(graphics, 2, A03GraphicsUtilities.createRoundRectangle(1, 1, width - 3, height - 3, 2),
//	    		FOCUS_COLOR,
//                UIManager.getColor("control"));
//
//		graphics.dispose();
	}
	
	public void paintThumb(Component c, Graphics g, Rectangle trackRect,
			Rectangle thumbRect) {
		
		JSlider slider = (JSlider) c;

		int state = A03BasicUtilities.getState(c);
		int orientation = slider.getOrientation();
		
		Paint background;
		Paint border = null;
		Paint outerBorder = null;
		Shape backgroundShape = null;
		Shape borderShape;
		Shape outerBorderShape = null;

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		int trackSize = 6;
		
		float fadeLevel;
		boolean paintTrack = slider.getPaintTrack();
		boolean leftToRight = A03SwingUtilities.isLeftToRight(slider);
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		
		int value = slider.getValue();
		int minimum = slider.getMinimum();
		int maximum = slider.getMaximum();
		boolean hasFocus = slider.hasFocus();
		
		if (paintTrack) {
			if (value > minimum) {
				if (orientation == JSlider.HORIZONTAL) {
					if (leftToRight) {
						if (value == maximum) {
							width = trackRect.width - 2;
						} else {
							width = thumbRect.x - trackRect.x + thumbRect.width / 2 - 1;
						}
						x = trackRect.x + 1;
						y = trackRect.y + trackRect.height / 2 - 2;
						height = trackSize - 2;
					} else {
						if (value == maximum) {
							x = trackRect.x + 1;
							width = trackRect.width - 2;
						} else {
							x = thumbRect.x + thumbRect.width / 2 + 1;
							width = trackRect.width - (thumbRect.x + thumbRect.width / 2 - trackRect.x) - 1;
						}
						
						y = trackRect.y + trackRect.height / 2 - 2;
						height = trackSize - 2;
					}					
				} else {
					if (value == maximum) {
						y = trackRect.y + 1;
						height = trackRect.height - 2;
					} else {
						y = thumbRect.y + thumbRect.height / 2 + 1;
						height = trackRect.height - (thumbRect.y + thumbRect.height / 2 - trackRect.y) - 2;
					}
	
					x = trackRect.x + trackRect.width / 2 - 2;
					width = trackSize - 2;
				}
			}

			// Draw slider track here
			if (orientation == JSlider.HORIZONTAL) {
				int x1 = trackRect.x;
				int y1 = trackRect.y + trackRect.height / 2 - 3;
				int width1 = trackRect.width;

				int radius = 2;
				
//				if (leftToRight) {
//					if (value > minimum) {
//						x1 = x + width - radius;
//					} else {
//						x1 = trackRect.x;
//					}
//				} else {
//					x1 = trackRect.x;
//				}				
				
				background = getTrackBackgroundPaint(state, orientation, x1, y1, trackSize - 1, trackSize);
				backgroundShape = A03GraphicsUtilities.createRoundRectangle(x1, y1, width1, trackSize, radius);

				if (hasFocus) {
					borderShape = A03GraphicsUtilities.createRoundRectangle(x1, y1, width1 - 1, trackSize - 1, radius);
				} else {
					border = getTrackBorderPaint(state, orientation, x1, y1, trackSize - 1, trackSize - 1);
					borderShape = A03GraphicsUtilities.createRoundRectangle(x1, y1, width1 - 1, trackSize - 1, radius);
	
					outerBorder = getTrackOuterBorderPaint(state, orientation, x1 - 1, y1 - 1, trackSize + 1, trackSize + 1);
					outerBorderShape = A03GraphicsUtilities.createRoundRectangle(x1 - 1, y1 - 1, width1 + 1, trackSize + 1, radius);
				}
			} else {
				int x1 = trackRect.x + trackRect.width / 2 - 3;
				int y1 = trackRect.y;
				int height1 = trackRect.height;

				int radius = 2;

				background = getTrackBackgroundPaint(state, orientation, x1, y1, trackSize, trackRect.height);
				backgroundShape = A03GraphicsUtilities.createRoundRectangle(x1, y1, trackSize, height1, radius);

				if (hasFocus) {
					borderShape = A03GraphicsUtilities.createRoundRectangle(x1, y1, trackSize - 1, height1 - 1, radius);
				} else {
					border = getTrackBorderPaint(state, orientation, x1, y1, trackSize - 1, trackSize - 1);
					borderShape = A03GraphicsUtilities.createRoundRectangle(x1, y1, trackSize - 1, height1 - 1, radius);
					
					outerBorder = getTrackOuterBorderPaint(state, orientation, x1 - 1, y1 - 1, trackSize + 1, trackSize + 1);
					outerBorderShape = A03GraphicsUtilities.createRoundRectangle(x1 - 1, y1 - 1, trackSize + 1, height1 + 1, radius);
				}
			}
	
			graphics.setPaint(background);			
			graphics.fill(backgroundShape);
			if (hasFocus) {
			    A03AphroditeGraphicsUtilities.paintBorderShadow(graphics, 2, borderShape,
			    		FOCUS_COLOR,
		                UIManager.getColor("control"));
			} else {
				graphics.setPaint(border);			
				graphics.draw(borderShape);
				graphics.setPaint(outerBorder);			
				graphics.draw(outerBorderShape);
			}

			if (x > 1 && y > 1) {
				background = getThumbBackgroundPaint(state, orientation, x - 1, y - 1, width + 1, height + 1);
				graphics.setPaint(background);
				graphics.fill(A03GraphicsUtilities.createRoundRectangle(x - 1, y - 1, width + 1, height + 1, 2));
	
				border = getThumbBorderPaint(state, orientation, x - 1, y - 1, width + 1, height + 1);
				graphics.setPaint(border);
				graphics.draw(A03GraphicsUtilities.createRoundRectangle(x - 1, y - 1, width + 1, height + 1, 2));
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

			background = getThumbBackgroundPaint(state, orientation, 2, 2, thumbRect.width - 3, thumbRect.height - 3);				
			
			imageGraphics.setPaint(background);
			backgroundShape = A03GraphicsUtilities.createRoundRectangle(2, 2, thumbRect.width - 3, thumbRect.height - 3, 2);
			imageGraphics.fill(backgroundShape);

			if (paintTrack) {
				x = orientation == JSlider.HORIZONTAL ? -1 : thumbRect.width - 5;
				y = orientation == JSlider.HORIZONTAL ? thumbRect.height - 5 : -1;
			}
			imageGraphics.setPaint(getTrackOuterBorderPaint(state, orientation, 0, 0, thumbRect.width, thumbRect.height));
			backgroundShape = A03GraphicsUtilities.createRoundRectangle(0, 0, thumbRect.width - 1, thumbRect.height - 1, 2);
			imageGraphics.draw(backgroundShape);
			
			if (paintTrack || slider.hasFocus()) {
			    A03AphroditeGraphicsUtilities.paintBorderShadow(imageGraphics, 2, A03GraphicsUtilities.createRoundRectangle(1, 1, thumbRect.width - 3, thumbRect.height - 3, 2),
			    		FOCUS_COLOR,
		                UIManager.getColor("control"));
			} else {
				imageGraphics.setClip(A03GraphicsUtilities.createRoundRectangle(x, y, thumbRect.width - x, thumbRect.height - y, 2));
				border = getThumbBorderPaint(state, orientation, x + 1, x + 1, thumbRect.width - 3, thumbRect.height - 3);				
				imageGraphics.setPaint(border);
				backgroundShape = A03GraphicsUtilities.createRoundRectangle(x + 1, x + 1, thumbRect.width - 3, thumbRect.height - 3, 2);

				imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				imageGraphics.draw(backgroundShape);
			}

			imageGraphics.dispose();
			
			graphics.drawImage(image, thumbRect.x, thumbRect.y, c);
		}
		
		graphics.dispose();
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(CONTROL);
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

	public Paint getThumbBackgroundPaint(int state, int orientation, int x,
			int y, int width, int height) {
		Color[] colors;
		
//		if ((state & ENABLED) != 0) {
//		if ((state & (SELECTED | ARMED)) != 0) {
//			colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
//		} else {
//			colors = new Color[] {new Color(0x9ab8cf),
//					new Color(0x7a98af), }; // BUTTON_BACKGROUND_ENABLED_COLORS;
//		}
//	} else {
//		if ((state & SELECTED) != 0) {
			colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
//		} else {
//			colors = BUTTON_BACKGROUND_DISABLED_COLORS;
//		}
//	}
		
		
		if (orientation == JSlider.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
	}

	public Paint getThumbBorderPaint(int state, int orientation, int x, int y,
			int width, int height) {
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

	public Paint getTrackBackgroundPaint(int state, int orientation, int x,
			int y, int width, int height) {
		Color[] colors;
//		
		if ((state & ENABLED) != 0) {
//			if ((state & (SELECTED | ARMED)) != 0) {
				colors = BUTTON_BACKGROUND_ENABLED_ARMED_COLORS;
//			} else {
//				colors = BUTTON_BACKGROUND_ENABLED_COLORS;
//			}
		} else {
//			if ((state & SELECTED) != 0) {
//				colors = BUTTON_BACKGROUND_DISABLED_ARMED_COLORS;
//			} else {
				colors = BUTTON_BACKGROUND_DISABLED_COLORS;
//			}
		}
//		
		if (orientation == JSlider.HORIZONTAL) {
			return new GradientPaint(x, y, colors[0], x, y + height, colors[1]);
		} else {
			return new GradientPaint(x, y, colors[0], x + width, y, colors[1]);
		}
//		return Color.RED;
	}

	protected Paint getTrackOuterBorderPaint(int state, int orientation, int x, int y, int width, int height) {
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

	public Paint getTrackBorderPaint(int state, int orientation, int x, int y,
			int width, int height) {
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
