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

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;

import a03.swing.plugin.A03UIPluginManager;


public class A03TreeUI extends BasicTreeUI {
	
	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03TreeUI();
	}
	
	class MouseListenerImpl extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JTree tree = (JTree) e.getSource();
			
			int row = tree.getRowForLocation(e.getX(), e
					.getY());
			if (row == -1) {
				TreePath closestPath = tree.getClosestPathForLocation(e.getX(), e
						.getY());
				Rectangle bounds = tree.getPathBounds(closestPath);
				
				if (
						bounds != null &&
						(e.getY() > bounds.y && e.getY() < (bounds.y + bounds.height))
					) {
					row = tree.getRowForLocation(bounds.x, bounds.y);
				}
			}
			
			if (row >= 0) {
				if (e.getClickCount() == 1) {
					tree.setSelectionRow(row);
				} else if (e.getClickCount() == 2) {
					tree.expandRow(row);
				}
			}
		}
	}
	
	private A03TreeUIDelegate uiDelegate;
	private MouseListener mouseListenerImpl;

	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03TreeUIDelegate.class);

		super.installUI(c);
		
//	    "Tree.openIcon", A03IconFactory.createTreeOpenIcon(uiDelegate), 
//	    "Tree.closedIcon", A03IconFactory.createTreeClosedIcon(uiDelegate),
//        "Tree.leafIcon", A03IconFactory.createTreeLeafIcon(uiDelegate),

		expandedIcon = A03IconFactory.createTreeExpandedIcon(uiDelegate);
		collapsedIcon = A03IconFactory.createTreeCollapsedIcon(uiDelegate);

		tree.setBackground(uiDelegate.getBackground());
		tree.setBorder(A03BorderFactory.createDelegatedBorder(uiDelegate));
//		tree.setRowHeight(uiDelegate.getRowHeight());
		Font font = tree.getFont();
		if (font instanceof FontUIResource) {
			tree.setFont(uiDelegate.getFont());
		}
		
		mouseListenerImpl = new MouseListenerImpl();
		
		tree.addMouseListener(mouseListenerImpl);
		
		A03UIPluginManager.getInstance().getUIPlugins().installUI(c);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(c);

		tree.removeMouseListener(mouseListenerImpl);
		
		super.uninstallUI(c);
	}
	
	protected void paintRow(Graphics g, Rectangle clipBounds,
		    Insets insets, Rectangle bounds, TreePath path,
		    int row, boolean isExpanded,
		    boolean hasBeenExpanded, boolean isLeaf) {
		if (editingComponent != null && editingRow == row)
			return;

		boolean hasFocus = tree.hasFocus() &&
				row == getRowForPath(tree, tree.getLeadSelectionPath());
		
		Component component = currentCellRenderer.getTreeCellRendererComponent(tree, path
				.getLastPathComponent(), tree.isRowSelected(row), isExpanded,
				isLeaf, row, hasFocus);

		boolean isSelected = tree.isRowSelected(row);

		if (isSelected) {
			component.setForeground(uiDelegate.getSelectionForeground());
		} else {
			component.setForeground(uiDelegate.getForeground(tree, row));
		}
		
		rendererPane.paintComponent(g, component, tree, bounds.x, bounds.y,	bounds.width, bounds.height, true);	
	}

    public void paint(Graphics g, JComponent c) {
		if (treeState == null) {
			return;
		}

		Rectangle paintBounds = g.getClipBounds();
		TreePath initialPath = getClosestPathForLocation(tree, 0, paintBounds.y);
		Enumeration<?> paintingEnumerator = treeState.getVisiblePathsFrom(initialPath);
		int row = treeState.getRowForPath(initialPath);
		int endY = paintBounds.y + paintBounds.height;

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);

		try {
			// Klaus Rheinwald
			Rectangle bounds = treeState.getBounds(initialPath, new Rectangle());
			int y = bounds != null ? bounds.y : 0;
			
			
			Insets insets = tree.getInsets();
			if (insets != null) {
				if (y == 0 && insets.top > 0) {
					graphics.setColor(uiDelegate.getBackground(tree, 1));
					graphics.fillRect(0, 0, tree.getWidth(), insets.top);
					
					y += insets.top;
				}
			}
			
			if (initialPath != null && paintingEnumerator != null) {
				while (paintingEnumerator.hasMoreElements()) {
					boolean isSelected = tree.isRowSelected(row);

					if (isSelected) {
						graphics.setColor(uiDelegate.getSelectionBackground());
					} else {
						graphics.setColor(uiDelegate.getBackground(tree, row));
					}
					
					int rowHeight = getRowHeight(row);
					
					graphics.fillRect(0, y, tree.getWidth(), rowHeight);
					
					y += rowHeight;
					if (y >= endY) {
						break;
					}
					row++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.paint(graphics, c);
		
		graphics.dispose();
	}
    
    private int getRowHeight(int row) {
		int rowHeight = tree.getRowHeight();
		// Konrad Twardowski
		if (rowHeight <= 0) {
			Rectangle rowBounds = tree.getRowBounds(row);
			if (rowBounds != null) {
				rowHeight = rowBounds.height;
			} else {
				// JTree.getPreferredScrollableViewportSize()  
				rowHeight = 16;
			}
		}

    	return rowHeight;
    }
}
