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
package a03.swing.plugin;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreePath;

public class A03ToolTipManagerPlugin extends A03UIPlugin  {
	
	static class TableToolTipListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			JTable table = (JTable) e.getSource();
			
	        int row    = table.rowAtPoint(e.getPoint());
	        int column = table.columnAtPoint(e.getPoint());
			
	        if (row == -1 || column == -1) {
	        	return;
	        }
	        
			// http://www.tutorials.de/forum/swing-awt-swt/243350-tooltip-jtable-zelle-bei-zuviel-inhalt.html
	        
	        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
        	Object value = table.getValueAt(row, column);
			if (value != null) {
				String str = value.toString();
				if (!str.equals("")) {
					boolean isSelected = true;
					boolean hasFocus = false;
					Component component = cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			        char[] chars = str.toCharArray();
			        FontMetrics fm = component.getFontMetrics(component.getFont());
					int width = fm.charsWidth(chars, 0, chars.length);
					
					int height = fm.getHeight();
					
					if (component instanceof JLabel) {
						Icon icon = ((JLabel) component).getIcon();
						if (icon != null) {
							width += icon.getIconWidth() + ((JLabel) component).getIconTextGap();
							
							height = Math.max(height, icon.getIconHeight());
						}
					}

			        if (table.getColumnModel().getColumn(column).getWidth() < width || table.getRowHeight(row) < height) {
			        	table.setToolTipText(str);
			        } else {
			        	table.setToolTipText(null);
			        }
				} else {
					table.setToolTipText(null);
				}
			}
		}
	}
	
	static class TreeToolTipListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			JTree tree = (JTree) e.getSource();
			
			TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            Rectangle pathBounds = tree.getPathBounds(path);
            
            Rectangle visibleRect = tree.getVisibleRect();

            
            if (pathBounds != null && !visibleRect.contains(pathBounds)) {
            	Object value = path.getLastPathComponent();
        		if (value != null) {
        			String str = value.toString();
        			if (str != null && !str.equals("")) {
        				tree.setToolTipText(str);
        			} else {
        				tree.setToolTipText(null);
        			}
        		}
            } else {
				tree.setToolTipText(null);
    		}
			
		}
	}
	
	static class ListToolTipListener extends MouseMotionAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			JList list = (JList) e.getSource();
			
            int index = list.locationToIndex(e.getPoint());

            Rectangle visibleRect = list.getVisibleRect();
            
            Rectangle cellBounds = list.getCellBounds(index, index);

            if (cellBounds != null && !visibleRect.contains(cellBounds)) {
                Object item = list.getModel().getElementAt(index);
        		if (item != null) {
        			String str = item.toString();
        			if (str != null && !str.equals("")) {
        				list.setToolTipText(str);
        			} else {
        				list.setToolTipText(null);
        			}
        		}
            } else {
            	list.setToolTipText(null);
    		}
		}
	}
	
	private TableToolTipListener tableToolTipListener = new TableToolTipListener();
	
	private TreeToolTipListener treeToolTipListener = new TreeToolTipListener();
	
	private ListToolTipListener listToolTipListener = new ListToolTipListener();

    public void installUI(Component component) {
    	if (component instanceof JTable) {
    		component.addMouseMotionListener(tableToolTipListener);
    	} else if (component instanceof JTree) {
    		component.addMouseMotionListener(treeToolTipListener);
    	} else if (component instanceof JList) {
    		component.addMouseMotionListener(listToolTipListener);
		}
    }

    public void uninstallUI(Component component) {
    	if (component instanceof JTable) {
    		component.removeMouseMotionListener(tableToolTipListener);
    	} else if (component instanceof JTree) {
    		component.removeMouseMotionListener(treeToolTipListener);
    	} else if (component instanceof JList) {
    		component.removeMouseMotionListener(listToolTipListener);
		}
    }
    
	public void installDefaults(UIDefaults table) {
		// do nothing
	}
}

