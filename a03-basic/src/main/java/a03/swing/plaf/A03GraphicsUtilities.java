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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.print.PrinterGraphics;
import java.text.Bidi;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class A03GraphicsUtilities {

	public static Map<?, ?> desktopHints;
	protected static final int CHAR_BUFFER_SIZE = 256;
	public static char[] charsBuffer = new char[CHAR_BUFFER_SIZE];
	public static final Object charsBufferLock = new Object();

	private static final String PROP_DESKTOPHINTS = "awt.font.desktophints";

	protected static Map<Long, Image> cachedImages = new HashMap<Long, Image>();
	protected static Map<Long, Shape> shapes = new HashMap<Long, Shape>();

	/*
	 *  org.jvnet.flamingo.utils.RenderingUtils
	 */
	
	/**
	 * Returns the desktop hints for the specified graphics context.
	 * 
	 * @param graphics
	 *            Graphics context.
	 * @return The desktop hints for the specified graphics context.
	 */
	private static Map getDesktopHints(Graphics2D graphics) {
		if (isPrinting(graphics)) {
			return null;
		}
		
		if (desktopHints == null) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			GraphicsDevice device = graphics.getDeviceConfiguration().getDevice();
			desktopHints = (Map) toolkit.getDesktopProperty(PROP_DESKTOPHINTS
					+ '.' + device.getIDstring());
			if (desktopHints == null) {
				desktopHints = (Map) toolkit.getDesktopProperty(PROP_DESKTOPHINTS);
			}
			// It is possible to get a non-empty map but with disabled AA.
			if (desktopHints != null) {
				Object aaHint = desktopHints
						.get(RenderingHints.KEY_TEXT_ANTIALIASING);
				if ((aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
						|| (aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)) {
					desktopHints = null;
				}
			}
		}
		
		return desktopHints;
	}

	/*
	 *  org.jvnet.flamingo.utils.RenderingUtils
	 */
	
	/**
	 * Checks whether the specified graphics context is a print context.
	 * 
	 * @param g
	 *            Graphics context.
	 * @return <code>true</code> if the specified graphics context is a print
	 *         context.
	 */
	private static boolean isPrinting(Graphics g) {
		return g instanceof PrintGraphics || g instanceof PrinterGraphics;
	}
	
	public static void installDesktopHints(Graphics g) {
//		Graphics2D graphics = (Graphics2D) g;
//		
//		Map desktopHints = getDesktopHints(graphics);
//		if (desktopHints != null && !desktopHints.isEmpty()) {
//			graphics.addRenderingHints(desktopHints);
//		}
	}

	public static String toHexString(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		
		String hex = "#";
		if (red < 16) {
			hex += "0";
		}
		hex += Integer.toHexString(red);
	
		if (green < 16) {
			hex += "0";
		}
		hex += Integer.toHexString(green);
	
		if (blue < 16) {
			hex += "0";
		}
		hex += Integer.toHexString(blue);
	
		return hex;
	}

	public static int stringWidth(FontMetrics fm, String string){
	    if (string == null || string.equals("")) {
	        return 0;
	    }
	    return fm.stringWidth(string);
	}

	public static Color createColor(Color startColor, Color endColor, double fadeLevel) {
		if (fadeLevel == 0.0) {
			return startColor;
		} else if (fadeLevel == 1.0) {
			return endColor;
		}
		
		int red = (int) ((endColor.getRed() - startColor.getRed()) * fadeLevel + startColor.getRed());
		if (red > 255) {
			red = 255;
		} else if (red < 0) {
			red = 0;
		}
		
		int green = (int) ((endColor.getGreen() - startColor.getGreen()) * fadeLevel + startColor.getGreen());
		if (green > 255) {
			green = 255;
		} else if (green < 0) {
			green = 0;
		}
	
		int blue = (int) ((endColor.getBlue() - startColor.getBlue()) * fadeLevel + startColor.getBlue());
		if (blue > 255) {
			blue = 255;
		} else if (blue < 0) {
			blue = 0;
		}
	
		int alpha = endColor.getAlpha();
		if (alpha > 255) {
			alpha = 255;
		} else if (alpha < 0) {
			alpha = 0;
		}
	
		return new Color(red, green, blue, alpha);
	}

	public static Color createAlphaColor(Color color, int alpha) {
		int red = color.getRed();
		
		int green = color.getGreen();
		
		int blue = color.getBlue();
		
		return new Color(red, green, blue, alpha);
	}

	public static String clipString(FontMetrics fm, int width, String text) {
		if (text == null || text.length() == 0) {
			return text;
		}
	
		StringBuilder builder = new StringBuilder();
		
		int strWidth = 0;
		
		for (char ch : text.toCharArray()) {
			strWidth += fm.charWidth(ch);
			builder.append(ch);

			if (strWidth >= width) {
				break;
			}
		}
	
		return builder.toString();
	}

	public static Color createColor(Color color, double factor) {
		int red = color.getRed();
		
		red *= factor;
		if (red > 255) {
			red = 255;
		} else if (red < 0) {
			red = 0;
		}
		
		int green = color.getGreen();
		
		green *= factor;
		if (green > 255) {
			green = 255;
		} else if (green < 0) {
			green = 0;
		}
		
		int blue = color.getBlue();
		
		blue *= factor;
		if (blue > 255) {
			blue = 255;
		} else if (blue < 0) {
			blue = 0;
		}
		
		return new Color(red, green, blue, color.getAlpha());
	}

	public static Color[] revertColors(Color[] colors) {
		Color[] reverted = new Color[colors.length];
		
		for (int i = 0; i < colors.length; i++) {
			reverted[i] = colors[colors.length - i - 1];
		}
		
		return reverted;
	}

	public static float[] revertFractions(float[] fractions) {
		float[] reverted = new float[fractions.length];
		
		for (int i = 0; i < reverted.length; i++) {
			reverted[i] = 1F -  fractions[fractions.length - i - 1];
		}
		
		return reverted;
	}

	public static Paint createLinearGradientPaint(float startX, float startY, 
	        float endX, float endY, float[] fractions, Color[] colors) {
		return new LinearGradientPaint(startX, startY, endX, endY, fractions, colors);
	}

	public static final boolean isComplexLayout(char[] text, int start, int limit) {
	    boolean simpleLayout = true;
		char ch;
		for (int i = start; i < limit; ++i) {
			ch = text[i];
			if (A03GraphicsUtilities.isComplexLayout(ch)) {
				return true;
			}
			if (simpleLayout) {
				simpleLayout = A03GraphicsUtilities.isSimpleLayout(ch);
			}
		}
		if (simpleLayout) {
			return false;
		} else {
			return Bidi.requiresBidi(text, start, limit);
		}
	}

	public static final boolean isComplexLayout(char ch) {
	    return (ch >= '\u0900' && ch <= '\u0D7F') || // Indic
	           (ch >= '\u0E00' && ch <= '\u0E7F') || // Thai
	           (ch >= '\u1780' && ch <= '\u17ff') || // Khmer
	           (ch >= '\uD800' && ch <= '\uDFFF');   // surrogate value range
	}

	public static final boolean isSimpleLayout(char ch) {
	    return ch < 0x590 || (0x2E00 <= ch && ch < 0xD800);
	}

	public static void drawStringUnderlineCharAt(Component c, Graphics g, String text, int underlinedIndex, int x, int y) {
		int textLength = text.length();
		
		if (text == null || textLength <= 0) {
			return;
		}
		g.drawString(text, x, y);
	
		if (underlinedIndex >= 0 && underlinedIndex < textLength) {
			int underlineRectY = y;
			int underlineRectHeight = 1;
			int underlineRectX = 0;
			int underlineRectWidth = 0;
			boolean needsTextLayout = false;
			if (!needsTextLayout) {
				synchronized (charsBufferLock) {
					if (charsBuffer == null || charsBuffer.length < textLength) {
						charsBuffer = text.toCharArray();
					} else {
						text.getChars(0, textLength, charsBuffer, 0);
					}
					needsTextLayout = isComplexLayout(charsBuffer, 0, textLength);
				}
			}
			if (!needsTextLayout) {
				FontMetrics fm = g.getFontMetrics();
				underlineRectX = x + stringWidth(fm, text.substring(0, underlinedIndex));
				underlineRectWidth = fm.charWidth(text.charAt(underlinedIndex));
			} else {
				Graphics2D graphics = (Graphics2D) g;
				if (graphics != null) {
					FontRenderContext frc = graphics.getFontRenderContext();
					TextLayout layout = new TextLayout(text, graphics.getFont(), frc);
					TextHitInfo leading = TextHitInfo.leading(underlinedIndex);
					TextHitInfo trailing = TextHitInfo.trailing(underlinedIndex);
					Shape shape = layout.getVisualHighlightShape(leading, trailing);
					Rectangle rect = shape.getBounds();
					underlineRectX = x + rect.x;
					underlineRectWidth = rect.width;
				}
			}
			g.fillRect(underlineRectX, underlineRectY + 1, underlineRectWidth, underlineRectHeight);
		}
	}
	
	public static Shape createRoundRectangle(int x, int y, int w, int h, int r) {
//		long hashCode = Arrays.hashCode(new int[] { x, y, w, h, r });
		
		Shape shape = null; // (Shape) shapes.get(hashCode);
//		if (shape == null) {
			if (r == 0) {
				shape = new Rectangle(x, y, w, h);
			}
			
			GeneralPath path = new GeneralPath();
			    
		    path.moveTo(x + w - r, y);
		    path.quadTo(x + w, y, x + w, y + r);
		    path.lineTo(x + w, y + h - r);
		    path.quadTo(x + w, y + h, x + w - r, y + h);
		    path.lineTo(x + r, y + h);
		    path.quadTo(x, y + h, x, y + h - r);
		    path.lineTo(x, y + r);
		    path.quadTo(x, y, x + r, y);
		    path.closePath();
		    
		    shape = path;
		    
//		    shapes.put(hashCode, shape);
//		}
		
		return shape;
	}

	public static Image createImage(Component c, int width, int height) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		Image image = gc.createCompatibleImage(width, height,
				Transparency.TRANSLUCENT);
		
		return image;
	}
		
	public static Image getTempImage(Component c, int width, int height) {
		long hashCode = Arrays.hashCode(new int[] { width, height });
		
		Image image = cachedImages.get(hashCode);
		if (image == null) {
			image = createImage(c, width, height);
			
			cachedImages.put(hashCode, image);
		}

		return image;
	}

}
