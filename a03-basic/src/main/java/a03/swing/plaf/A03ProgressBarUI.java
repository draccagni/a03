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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import a03.swing.plugin.A03UIPluginManager;


public class A03ProgressBarUI extends BasicProgressBarUI {

    private int animationIndex = 0;
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03ProgressBarUI();
	}
	
	private A03ProgressBarUIDelegate uiDelegate;
	
	private Timer timer;
	
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03ProgressBarUIDelegate.class);

		super.installUI(c);
		
	    progressBar.setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
	    progressBar.setFont(uiDelegate.getFont());
	    progressBar.setForeground(uiDelegate.getForeground());
	    
		A03UIPluginManager.getInstance().getUIPlugins().installUI(c);
		
		progressBar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("indeterminate".equals(evt.getPropertyName())) {
					if (evt.getNewValue().equals(Boolean.TRUE)) {
						synchronized (progressBar.getTreeLock()) {
							if (timer != null) {
								timer.stop();
								timer = null;
							}
							
							timer = new Timer(30, new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									progressBar.repaint();
								}
							});
							timer.start();
						}
					} else {
						synchronized (progressBar.getTreeLock()) {
							if (timer != null) {
								timer.stop();
								timer = null;
							}
						}
					}
				}
			}
		});
		
		if (progressBar.isIndeterminate()) {
			synchronized (progressBar.getTreeLock()) {
				if (timer != null) {
					timer.stop();
					timer = null;
				}
				
				timer = new Timer(30, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						progressBar.repaint();
					}
				});
				timer.start();
			}
		}
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		if (progressBar.isIndeterminate()) {
			if (timer != null) {
				timer.stop();
				timer = null;
			}
		}

		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(c);

		super.uninstallUI(c);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		
		progressBar.putClientProperty(A03Constants.ORIGINAL_OPACITY, progressBar.isOpaque());
		progressBar.setOpaque(false);
	}
	
	@Override
	protected void uninstallDefaults() {
		super.uninstallDefaults();

		progressBar.setOpaque((Boolean) progressBar.getClientProperty(A03Constants.ORIGINAL_OPACITY));
		progressBar.putClientProperty(A03Constants.ORIGINAL_OPACITY, null);
	}

	@Override
	protected void paintDeterminate(Graphics g, JComponent c) {
		Insets insets = c.getInsets();
		int barRectWidth = c.getWidth() - (insets.right + insets.left);
		int barRectHeight = c.getHeight() - (insets.top + insets.bottom);

		if (barRectWidth <= 0 || barRectHeight <= 0) {
			return;
		}

		int amountFull = getAmountFull(insets, barRectWidth, barRectHeight);
		uiDelegate.paintDeterminate(progressBar, g, amountFull);
		
		if (progressBar.isStringPainted()) {
		    paintString(g, insets.left, insets.top, barRectWidth, barRectHeight, insets);
		}
	}

	@Override
	protected void paintIndeterminate(Graphics g, JComponent c) {
		Insets insets = progressBar.getInsets();
		g.translate(insets.left, insets.top);
		int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
		int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);

		if (barRectWidth <= 0 || barRectHeight <= 0) {
			return;
		}
		
		uiDelegate.paintIndeterminate(progressBar, g, animationIndex);

		if (progressBar.isStringPainted()) {
			if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
				paintString(g, insets.left, insets.top, barRectWidth, barRectHeight, insets);
			} else {
				paintString(g, insets.left, insets.top, barRectWidth, barRectHeight, insets);
			}
		}
	}
	
	private void paintString(Graphics g, int x, int y, int width, int height, Insets b) {
		Graphics2D graphics = (Graphics2D) g;
		
		/*
		 * XXX: to render text properly we need both
		 */
//		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
		A03GraphicsUtilities.installDesktopHints(graphics);

		String progressString = progressBar.getString();
		Point renderLocation = getStringPlacement(graphics, progressString, x, y, width, height);
		
		Font font;
		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			font = progressBar.getFont();
		} else { // VERTICAL
			AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2);
			font = progressBar.getFont().deriveFont(rotate);
		}
		graphics.setFont(font);
		
		if (progressBar.isEnabled()) {
			graphics.setColor(progressBar.getForeground());
		} else {
			graphics.setColor(UIManager.getColor("textInactiveText"));
		}
		
		uiDelegate.paintText(progressBar, graphics, progressString, renderLocation.x, renderLocation.y);
	}
	
	@Override
    protected Point getStringPlacement(Graphics g, String progressString,
		       int x,int y,int width,int height) {
		FontMetrics fm = g.getFontMetrics(progressBar.getFont());
		int stringWidth = A03GraphicsUtilities.stringWidth(fm, progressString);
		
		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
			return new Point(x + Math.round(width/2 - stringWidth/2),
					y + ((height + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2));
		} else { // VERTICAL
			return new Point(x + width - ((width + fm.getAscent() - fm.getLeading() - fm.getDescent()) / 2),
					y + Math.round(height/2 - stringWidth/2));
		}
    }

	@Override
	protected void incrementAnimationIndex() {
		animationIndex++;
	}

	@Override
	protected void setAnimationIndex(int newValue) {
		animationIndex = newValue;
	}
	
	@Override
	protected int getAnimationIndex() {
		return animationIndex;
	}
	
	@Override
	protected Dimension getPreferredInnerHorizontal() {
		return uiDelegate.getPreferredInnerHorizontal();
	}
	
	@Override
	protected Dimension getPreferredInnerVertical() {
		return uiDelegate.getPreferredInnerVertical();
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
		
    	paint(graphics, c);
    	
    	graphics.dispose();
	}
	
	
}
