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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicListUI;

import a03.swing.plugin.A03UIPluginManager;


public class A03ListUI extends BasicListUI {
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03ListUI();
	}

	private A03ListUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03ListUIDelegate.class);

		super.installUI(c);
		
		if (list.getCellRenderer() instanceof A03ListCellRenderer) {
			Color selectionForeground = list.getSelectionForeground();
			if (selectionForeground instanceof ColorUIResource) {
				list.setSelectionForeground(uiDelegate.getSelectionForeground());
			}
				
			Color selectionBackground = list.getSelectionBackground();
			if (selectionBackground instanceof ColorUIResource) {
			    list.setSelectionBackground(uiDelegate.getSelectionBackground());
			}
		    
			Color background = list.getSelectionBackground();
			if (background instanceof ColorUIResource) {
			    list.setBackground(uiDelegate.getBackground());
			}
	
			Font font = list.getFont();
			if (font == null || font instanceof FontUIResource) {
				list.setFont(uiDelegate.getFont());			
			}
			
			Border border = list.getBorder();
			if (border instanceof BorderUIResource) {
			    list.setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
			}
		}

		list.setOpaque(false);
		
		A03UIPluginManager.getInstance().getUIPlugins().installUI(c);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(c);

		super.uninstallUI(c);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		if (list.getLayoutOrientation() == JList.VERTICAL && list.getCellRenderer() instanceof A03ListCellRenderer) {
			try {
				Rectangle paintBounds = g.getClipBounds();
				
		        int maxY = paintBounds.y + paintBounds.height;
				
				int row = list.getModel().getSize() - 1;
				if (row > 0) {
					Rectangle rowBounds = (Rectangle) getCellBounds(list, row, row);
					int x = rowBounds.x;
		
					int y = rowBounds.y + rowBounds.height;
					
					row++;
					
					for (int y0 = y; y0 < maxY;) {
					    g.setColor(uiDelegate.getBackground(list, list.getLayoutOrientation(), row));
			        
					    g.fillRect(x, y0, rowBounds.width, rowBounds.height);
					    
				    	y0 += rowBounds.height;
	
					    row++;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			g.setColor(c.getBackground());
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}
			
		super.paint(g, c);
	}
	
    @Override
    protected void paintCell(
            Graphics g,
            int row,
            Rectangle rowBounds,
            ListCellRenderer cellRenderer,
            ListModel dataModel,
            ListSelectionModel selModel,
            int leadIndex) {
        Object value = dataModel.getElementAt(row);
        boolean cellHasFocus = list.hasFocus() && (row == leadIndex);
        boolean isSelected = selModel.isSelectedIndex(row);

        Component rendererComponent = cellRenderer.getListCellRendererComponent(list, value, row, isSelected, cellHasFocus);

		if (list.getCellRenderer() instanceof A03ListCellRenderer) {
	        if (isSelected) {
	        	boolean isEnabled = list.isEnabled();
	        	
	        	rendererComponent.setForeground(
	        			isEnabled ?
	           			list.getSelectionForeground() :
	        			uiDelegate.getDisabledSelectionForeground());
	        	g.setColor(
	        			isEnabled ? 
	        			list.getSelectionBackground() :
	        			uiDelegate.getDisabledSelectionBackground());
	        } else {
	            rendererComponent.setForeground(uiDelegate.getForeground(list, list.getLayoutOrientation(), row));
	           	g.setColor(uiDelegate.getBackground(list, list.getLayoutOrientation(), row));
	        }
		}
        
		g.fillRect(rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height);
        
        rendererPane.paintComponent(g, rendererComponent, list, rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height, true);
    }
   
	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		if (c.isOpaque()) {
			Rectangle2D.Double rectangle = (Rectangle2D.Double) g.getClip();
			
			graphics.setColor(c.getBackground());
			graphics.fillRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
		}

		paint(graphics, c);
		
		graphics.dispose();
	}
   
}
