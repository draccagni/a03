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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.Icon;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03OptionPaneUIDelegate;

public class A03BasicOptionPaneUIDelegate implements A03OptionPaneUIDelegate {

	static class WarningIcon implements Icon {
		
		public int getIconHeight() {
			return 36;
		}
		
		public int getIconWidth() {
			return 36;
		}
		
		public void paintIcon(final Component c, Graphics g, int x, int y) {
			Graphics2D graphics = (Graphics2D) g; //.create();
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			
	        GeneralPath path = new GeneralPath();
	        
	        int border = 7;
	        
	        int offset = border / 2;
	        
	        int w = getIconWidth() - 1;
	        
	        path.moveTo(w / 2, offset);
	        
	        int h = (int) (w * Math.sin(Math.PI / 3));
	        
	        path.lineTo(offset, h);
	        
	        path.lineTo(w - offset - 1, h);
	        
	        path.closePath();
	        
	        final Color outerBorderColor = new Color(0xccb565);
	        
	        graphics.setStroke(new BasicStroke(border, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        graphics.setColor(outerBorderColor);
	        
	        graphics.draw(path);
	        
	        graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        
	        path = new GeneralPath();
	        
	        path.moveTo(w / 2 + 1, 1);
	        
	        path.quadTo(w / 2, 0, w / 2 - 1, 1);
	        
	        path.lineTo(2, h - offset + 1);
	        
	        path.quadTo(0, h + offset - 1, 2, h + offset - 1);
	        
	        path.lineTo(w - offset, h + offset - 1);

	        path.quadTo(w - offset + 2, h + offset - 1, w - offset, h - offset + 1);
	        
	        path.closePath();
	        
	        graphics.draw(path);
	        
	        Paint paint = A03GraphicsUtilities.createLinearGradientPaint(w / 2, offset, w / 2, h, new float[] { 0, 0.5f, 0.51f, 1}, 
	        		new Color[] {
	        			new Color(0xffee84),
	        			new Color(0xffe155),
	        			new Color(0xfad86d),
	        			new Color(0xfcc200)
	        		
	        });
	        graphics.setPaint(paint);
	        
	        graphics.fill(path);

	        final Color borderColor = new Color(0xfff692);
	        graphics.setColor(borderColor);
	        
	        graphics.draw(path);
	        

    		graphics.setColor(Color.BLACK);
    		
    		graphics.translate(getIconWidth() / 2 - 3, 8);
    		
			int r1 = 5;
			int r2 = 3;
			int r3 = 2;
			int r4 = 2;
			int h2 = 16;
			
			GeneralPath esclamationMarkPath = new GeneralPath();
			esclamationMarkPath.moveTo(0, r1);
			esclamationMarkPath.curveTo(0, 0, 2 * r2 - 1, 0, 2 * r2 - 1, r1);
			esclamationMarkPath.lineTo(r2 + r3 - 1, h2 - r4);
			esclamationMarkPath.curveTo(r2 + r3 - 1, h2, r2 - r3, h2, r2 - r3, h2 - r4);
			esclamationMarkPath.closePath();
			
			graphics.fill(esclamationMarkPath);

    		graphics.translate(-getIconWidth() / 2 + 3, -8);

	        graphics.fill(new Ellipse2D.Double(getIconWidth() / 2 - 3, getIconHeight() / 2 + 7, 5, 5));
	        
	        //graphics.dispose();
		}
	}
	
	static class InformationIcon implements Icon {
		
		public int getIconHeight() {
			return 36;
		}
		
		public int getIconWidth() {
			return 36;
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D graphics = (Graphics2D) g.create();
	        Stroke oldStroke = graphics.getStroke();

	        int border = 3;
	        int offset = border / 2;
	        
			Color background;
			Container parent = c.getParent();
			if (parent instanceof javax.swing.CellRendererPane) {
				background = c.getBackground();
			} else {
				background = parent.getBackground();
			}

	        final Color borderColor = new Color(0x007fc3);
			Color shadowColor = A03GraphicsUtilities.createColor(background, borderColor, 0.1);
	        
	        Ellipse2D ellipse = new Ellipse2D.Double(offset, offset, getIconWidth() - 2 * offset, getIconHeight() * 0.85 - 2 * offset);
	        Area a1 = new Area(ellipse);
	        
	        GeneralPath path = new GeneralPath();
	        path.moveTo(getIconWidth() / 2, getIconHeight() * 0.85f / 2);
	        path.quadTo(getIconWidth() / 2 - getIconWidth() / 16, getIconHeight() * 0.85f, getIconWidth() / 2 - getIconWidth() / 8, getIconHeight() - offset);
	        path.quadTo(getIconWidth() / 2 + 2 * offset, getIconHeight() - 2 * offset, getIconWidth() / 2 + getIconWidth() / 8, getIconHeight() * 0.8f / 2);
	        path.closePath();
	        Area a2 = new Area(path);
	        
	        a1.add(a2);

	        graphics.setStroke(new BasicStroke(border, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        graphics.setColor(shadowColor);
	        graphics.draw(a1);
	        
	        graphics.setStroke(oldStroke);
	        
	        Paint paint = A03GraphicsUtilities.createLinearGradientPaint(getIconWidth() / 2, offset, getIconWidth() / 2, (int) (getIconHeight() * 0.8) - 2 * offset,
	        		new float[] {
		        	0, 1
		        },
		        new Color[] {
		        	new Color(0x7bd5fa),
        			new Color(0x19b8f2)
		        });
	        graphics.setPaint(paint);
	        graphics.fill(a1);
	        
	        
	        graphics.setColor(borderColor);
	        graphics.draw(a1);

	        graphics.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	        graphics.setColor(Color.WHITE);
	        graphics.fillOval(getIconWidth() / 2 - 2, 5, 5, 5);
	        graphics.drawLine(getIconWidth() / 2, 14, getIconWidth() / 2, (int) (getIconHeight() * 0.6f));
	        
	        graphics.dispose();
		}
	}

	static class ErrorIcon implements Icon {
		
		public int getIconHeight() {
			return 36;
		}
		
		public int getIconWidth() {
			return 36;
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D graphics = (Graphics2D) g.create();

	        int border = 3;

	        int offset = border / 2;
	        
	        Ellipse2D ellipse = new Ellipse2D.Double(offset, offset, getIconWidth() - 2 * offset - 1, getIconHeight() - 2 * offset - 1);
	        
	        graphics.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        graphics.setColor(new Color(0x930008));
	        
	        graphics.draw(ellipse);
	        
	        Paint paint = A03GraphicsUtilities.createLinearGradientPaint(getIconWidth() / 2, offset, getIconWidth() / 2, getIconHeight() - 2 * offset,
		        new float[] {
		        	0, 1
		        },
		        new Color[] {
		        	new Color(0xd93536),
		        	new Color(0x7f0b0b)
		        });
	        graphics.setPaint(paint);
	        graphics.fill(ellipse);
	        
	        graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	        paint = A03GraphicsUtilities.createLinearGradientPaint(getIconWidth() / 2, offset, getIconWidth() / 2, getIconHeight() - 2 * offset,
		        new float[] {
		        	0, 1
		        },
		        new Color[] {
		        	new Color(0xff7975),
		        	new Color(0x7f0b0b)
		        });
	        graphics.setPaint(paint);
	        graphics.draw(ellipse);
	        
	        int x1 = 10;
	        int y1 = 10;
	        
	        int x2 = getIconWidth() - x1 - 1;
	        int y2 = getIconHeight() - y1 - 1;
	        
	        int x3 = x1;
	        int y3 = y2;
	        
	        int x4 = x2;
	        int y4 = y1;
	        
	        graphics.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        
	        graphics.setColor(Color.WHITE);
	        
	        graphics.drawLine(x1, y1, x2, y2);
	        
	        graphics.drawLine(x3, y3, x4, y4);
	        
	        graphics.dispose();
		}
		
	}

	static class QuestionIcon implements Icon {
		
		public int getIconHeight() {
			return 36;
		}
		
		public int getIconWidth() {
			return 36;
		}
		
		public void paintIcon(final Component c, Graphics g, int x, int y) {
			final Graphics2D graphics = (Graphics2D) g.create();
			
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

	        int border = 3;

	        int offset = border / 2;
	        
	        Ellipse2D ellipse = new Ellipse2D.Double(offset, offset, getIconWidth() - 2 * offset - 1, getIconHeight() - 2 * offset - 1);
	        
	        final Color outerBorderColor = new Color(0x4544c4);
	        graphics.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	        graphics.setColor(outerBorderColor);
	        
	        graphics.draw(ellipse);
	        
	        Paint paint = A03GraphicsUtilities.createLinearGradientPaint(getIconWidth() / 2, offset, getIconWidth() / 2, getIconHeight() - 2 * offset,
		        new float[] {
		        	0, 1
		        },
		        new Color[] {
		        	new Color(0x407ef9),
		        	new Color(0x155cb8)
		        });
	        graphics.setPaint(paint);
	        graphics.fill(ellipse);
	        
	        graphics.setStroke(new BasicStroke(1));

	        paint = A03GraphicsUtilities.createLinearGradientPaint(getIconWidth() / 2, offset, getIconWidth() / 2, getIconHeight() - 2 * offset,
		        new float[] {
		        	0, 1
		        },
		        new Color[] {
		        	new Color(0xd4edff),
		        	new Color(0x155cb8)
		        });
	        graphics.setPaint(paint);
	        graphics.draw(ellipse);
	        
	        int x0 = getIconWidth() / 2;
	        int y0 = 12;
	        
	        int w = 10;
	        
	        Arc2D arc = new Arc2D.Double(x0 - w / 2, y0 - w / 2, w, w, 155, -200, Arc2D.OPEN);
	        
	        GeneralPath path = new GeneralPath();
	        path.append(arc, true);

	        graphics.setColor(Color.WHITE);
	        Line2D line2d = new Line2D.Double(x0 + w * Math.cos(-Math.PI / 4) / 2, y0 - w * Math.sin(-Math.PI / 4) / 2, x0, y0 - w * Math.sin(-Math.PI / 4) / 2 + 4);
	        path.append(line2d, true);
	        
	        line2d = new Line2D.Double(x0, y0 - w * Math.sin(-Math.PI / 4) / 2 + 4, x0, y0 - w * Math.sin(-Math.PI / 4) / 2 + 7);
	        path.append(line2d, true);

	        graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
	        graphics.draw(path);
	        
	        graphics.fill(A03GraphicsUtilities.createRoundRectangle(x0 - 2, (int) (y0 - w * Math.sin(-Math.PI / 4) / 2 + 12), 5, 5, 1));
	        
	        graphics.dispose();
		}		
	}
	
	private static Icon informationIcon = new InformationIcon();
	private static Icon questionIcon = new QuestionIcon();
	private static Icon warningIcon = new WarningIcon();
	private static Icon errorIcon = new ErrorIcon();
	
	public A03BasicOptionPaneUIDelegate() {
	}
	
	public int getErrorIconHeight() {
		return errorIcon.getIconHeight();
	}
	
	public int getErrorIconWidth() {
		return errorIcon.getIconWidth();
	}
	
	public int getInformationIconHeight() {
		return informationIcon.getIconHeight();
	}
	
	public int getInformationIconWidth() {
		return informationIcon.getIconWidth();
	}
	
	public int getQuestionIconHeight() {
		return questionIcon.getIconHeight();
	}
	
	public int getQuestionIconWidth() {
		return questionIcon.getIconWidth();
	}
	
	public int getWarningIconHeight() {
		return warningIcon.getIconHeight();
	}
	
	public int getWarningIconWidth() {
		return warningIcon.getIconWidth();
	}
	
	public void paintErrorIcon(Component c, Graphics g, int x, int y) {
		errorIcon.paintIcon(c, g, x, y);
	}
	
	public void paintInformationIcon(Component c, Graphics g, int x, int y) {
		informationIcon.paintIcon(c, g, x, y);
	}
	
	public void paintQuestionIcon(Component c, Graphics g, int x, int y) {
		questionIcon.paintIcon(c, g, x, y);
	}
	 
	public void paintWarningIcon(Component c, Graphics g, int x, int y) {
		warningIcon.paintIcon(c, g, x, y);
	}
	
	
	public Icon getErrorIcon() {
		return errorIcon;
	}
	
	public Icon getInformationIcon() {
		return informationIcon;
	}
	
	public Icon getQuestionIcon() {
		return questionIcon;
	}
	
	
	public Icon getWarningIcon() {
		return warningIcon;
	}
	
	public Insets getMessageAreaBorderInsets(Component c, Insets insets) {
		insets.top = 10;
		insets.left = 10;
		insets.bottom = 10;
		insets.right = 30;

		return insets;
	}

	public Insets getButtonAreaBorderInsets(Component c, Insets insets) {
		insets.top = 6;
		insets.left = 0;
		insets.bottom = 6;
		insets.right = 0;

		return insets;
	}

	public String getErrorSound() {
        return "/javax/swing/plaf/metal/sounds/OptionPaneError.wav";
	}

	public String getInformationSound() {
        return "/javax/swing/plaf/metal/sounds/OptionPaneInformation.wav";
	}

	public String getQuestionSound() {
        return "/javax/swing/plaf/metal/sounds/OptionPaneQuestion.wav";
	}

	public String getWarningSound() {
        return "/javax/swing/plaf/metal/sounds/OptionPaneWarning.wav";
	}

	public void paintButtonAreaBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}

	public void paintMessageAreaBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		Graphics2D graphics = (Graphics2D) g.create(x, y, width, height);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphics.setColor(Color.BLACK);
		graphics.drawRect(0, 0, width - 1, height - 1);
		
		graphics.dispose();
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 4;
		insets.left = 4;
		insets.bottom = 2;
		insets.right = 4;

		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing
	}
	
	public ColorUIResource getMessageAreaBackground() {
		return new ColorUIResource(Color.RED);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.RED);
	}

	public FontUIResource getMessageFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
}
