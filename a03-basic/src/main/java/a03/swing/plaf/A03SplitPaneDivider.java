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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class A03SplitPaneDivider extends BasicSplitPaneDivider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7014073257296835390L;

	private A03SplitPaneUIDelegate uiDelegate;
	
	private Border border;
	
	public A03SplitPaneDivider(BasicSplitPaneUI ui) {
		super(ui);
		
		this.uiDelegate = A03SwingUtilities.getUIDelegate(ui.getSplitPane(), A03SplitPaneUIDelegate.class);
		
		this.border = A03BorderFactory.createSplitPaneDividerBorder(this.uiDelegate);
	}
	
	public void setBorder(Border border) {
	}
	
	public Border getBorder() {
		return this.border;
	}

	protected JButton createLeftOneTouchButton() {
		JButton button = new JButton() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4720812671453018556L;

			public void setBorder(Border b) {
			}

			public void paint(Graphics g) {
				Graphics2D graphics = (Graphics2D) g.create();

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);

				graphics.setColor(splitPaneUI.getSplitPane().getBackground());
				int w = getWidth();
				int h = getHeight();

				graphics.fillRect(0, 0, w, h);

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				if (orientation == JSplitPane.VERTICAL_SPLIT) {
					uiDelegate.paintArrow(this, graphics,  SwingConstants.NORTH);
				} else {
					uiDelegate.paintArrow(this, graphics, SwingConstants.WEST);
				}
				
				graphics.dispose();
			}

			public boolean isFocusTraversable() {
				return false;
			}
		};
		button.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setRequestFocusEnabled(false);
		return button;
	}

	@Override
	protected JButton createRightOneTouchButton() {
		JButton button = new JButton() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 831100152405822728L;

			public void setBorder(Border border) {
			}

			public void paint(Graphics g) {
				Graphics2D graphics = (Graphics2D) g.create();

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);


				graphics.setColor(splitPaneUI.getSplitPane().getBackground());
				int w = getWidth();
				int h = getHeight();

				graphics.fillRect(0, 0, w, h);

				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				if (orientation == JSplitPane.VERTICAL_SPLIT) {
					uiDelegate.paintArrow(this, graphics,  SwingConstants.SOUTH);
				} else {
					uiDelegate.paintArrow(this, graphics, SwingConstants.EAST);
				}
				
				graphics.dispose();
			}

			public boolean isFocusTraversable() {
				return false;
			}
		};
		
		button.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setRequestFocusEnabled(false);
		
		return button;
	}
	
    public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		if (isOpaque()) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
	    	
			graphics.setColor(getBackground());
		    graphics.fillRect(0, 0, getWidth(), getHeight());
		}
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

        super.paint(graphics);
        
        uiDelegate.paintDividerGrip(splitPane, graphics);

    	graphics.dispose();
    }
}
