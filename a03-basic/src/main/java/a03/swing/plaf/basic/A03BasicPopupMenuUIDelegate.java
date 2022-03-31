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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;

import a03.swing.plaf.A03PopupMenuUIDelegate;

public class A03BasicPopupMenuUIDelegate implements A03PopupMenuUIDelegate {
	
	public A03BasicPopupMenuUIDelegate() {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		if (c instanceof JPopupMenu && ((JPopupMenu) c).getInvoker() instanceof JComboBox) {
			insets.top = 1;
			insets.left = 1;
			insets.bottom = 1;
			insets.right = 1;
		} else {
			int left = 2;
			int right = 2;
			if (c instanceof JPopupMenu) { // not JToolTip
				JPopupMenu popupMenu = (JPopupMenu) c;
				Component invoker = popupMenu.getInvoker();
		 		Point menuScreenLocation = new Point();
				if (invoker instanceof JMenu) {
					JMenu menu = (JMenu) invoker;
					
			 		SwingUtilities.convertPointToScreen(menuScreenLocation, menu);
			 		
			 		if (menuScreenLocation.x < 0) {
			 			left = 0;
			 		} else if (menuScreenLocation.x + menu.getWidth() > Toolkit.getDefaultToolkit().getScreenSize().width) {
			 			right = 0;
			 		}
				}
			}

			insets.top = 2;
			insets.left = left;
			insets.bottom = 2;
			insets.right = right;
		}

		return insets;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D graphics = (Graphics2D) g; //.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		graphics.setPaint(getBorderPaint(x, y, width - 1, height - 1));
		graphics.drawRect(x, y, width - 1, height - 1);
		
//		if (c instanceof JPopupMenu) { // not JToolTip
//			final JPopupMenu popupMenu = (JPopupMenu) c;
//			
//			Component invoker = popupMenu.getInvoker();
//			graphics.setColor(c.getBackground());
//	 		
//			if (invoker instanceof JMenu) {
//				JMenu menu = (JMenu) invoker;
//				
//		 		Point menuScreenLocation = new Point();
//		 		SwingUtilities.convertPointToScreen(menuScreenLocation, menu);
//
//				Point popupLocation = A03GraphicsUtils.getPopupMenuOrigin(JMenu.class, menu);
//		 		
//				if (menu.getParent() instanceof JMenuBar) {						
//			 		int dx = 0;
//			 		int dy = 0;
//			 		
//			 		if (popupLocation.y < 0) {
//			 			dy = height - 1;
//			 		}
//
//			 		int leftDx = menuScreenLocation.x;
//			 		int rightDx = Toolkit.getDefaultToolkit().getScreenSize().width - (menuScreenLocation.x + menu.getWidth());
//			 		if (leftDx < 0) {
//					 	dx = leftDx;
//				 		
//					 	graphics.drawLine(x, 1, x, y + height - 2);
//			 		} else if (rightDx < 0) {
//					 	dx = width - menu.getWidth() - rightDx;
//					 	
//					 	graphics.drawLine(x + width - 1, 1, x + width -1, y + height - 2);
//					 } else  if (popupLocation.x < 0) {
//			 			dx = width - menu.getWidth();
//			 		}
//
//			 		graphics.drawLine(x + 2 + dx, y + dy, x + menu.getWidth() - 4 + dx, y + dy);
//				} else {
//			 		Point popupScreenLocation = new Point();
//			 		SwingUtilities.convertPointToScreen(popupScreenLocation, menu.getPopupMenu());
//			 		
//			 		int dx = 0;
//			 		int dy = menuScreenLocation.y - popupScreenLocation.y; // -popupLocation.y;
//			 		if (popupLocation.x < 0) {
//			 			dx = width - 1;
//			 		}
//			 		
//			 		graphics.drawLine(x + dx, y + 2 + dy, x + dx, y + menu.getHeight() - 3 + dy);
//				}					
//			} else if (invoker instanceof JComboBox) {
//				graphics.drawLine(x + width - 2, 1, x + width - 2, height - 2);					
//			}
//		}
		
		//graphics.dispose();
	}

	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}

	public Paint getBorderPaint(int x, int y, int width, int height) {
		return Color.BLACK;
	}
}
