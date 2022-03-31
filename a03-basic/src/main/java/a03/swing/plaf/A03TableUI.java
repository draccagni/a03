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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.ViewportLayout;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import a03.swing.plugin.A03UIPluginManager;

/**
 * @author Davide Raccagni
 * @author Kirill Grouchnikov
 */
public class A03TableUI extends BasicTableUI {

	protected Map<Class<?>, TableCellRenderer> defaultRenderers;

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03TableUI();
	}

	private A03TableUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03TableUIDelegate.class);

		super.installUI(c);
		
//		"Table.focusSelectedCellHighlightBorder", A03BorderFactory.createTableFocusSelectedCellHighlightBorder(),
//        "Table.ascendingSortIcon", A03IconFactory.createAscendingSortIcon(tableUIDelegate),
//        "Table.descendingSortIcon", A03IconFactory.createDescendingSortIcon(tableUIDelegate),

		// install the scrollpane border
        Container parent = table.getParent();  // should be viewport
        if (parent != null) {
            parent = parent.getParent();  // should be the scrollpane
            if (parent != null && parent instanceof JScrollPane) {
                ((JScrollPane) parent).setBorder(A03BorderFactory.createScrollPaneTableBorder(uiDelegate));
            }
        }
        
        table.setForeground(uiDelegate.getForeground());
        table.setBackground(uiDelegate.getBackground());
        table.setGridColor(uiDelegate.getGridColor());
	    table.setFont(uiDelegate.getFont());
		table.setSelectionForeground(uiDelegate.getSelectionForeground());
        table.setSelectionBackground(uiDelegate.getSelectionBackground());

        table.putClientProperty(A03Constants.ORIGINAL_OPACITY, table.isOpaque());
        table.setOpaque(false);

		A03UIPluginManager.getInstance().getUIPlugins().installUI(table);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		A03UIPluginManager.getInstance().getUIPlugins().uninstallUI(table);

		table.setOpaque((Boolean) table.getClientProperty(A03Constants.ORIGINAL_OPACITY));
		table.putClientProperty(A03Constants.ORIGINAL_OPACITY, null);
		
		super.uninstallUI(c);
	}

	// Kirill Grouchnikov 2007/11/05
	@Override
	protected void installDefaults() {
		super.installDefaults();

		this.defaultRenderers = new HashMap<Class<?>, TableCellRenderer>();

		installRendererIfNecessary(Object.class, new A03TableCellRenderer());
		installRendererIfNecessary(Icon.class,
				new A03TableCellRenderer.IconRenderer());
		installRendererIfNecessary(ImageIcon.class,
				new A03TableCellRenderer.IconRenderer());
		installRendererIfNecessary(Number.class,
				new A03TableCellRenderer.NumberRenderer());
		installRendererIfNecessary(Float.class,
				new A03TableCellRenderer.NumberRenderer());
		installRendererIfNecessary(Double.class,
				new A03TableCellRenderer.NumberRenderer());
		installRendererIfNecessary(Date.class,
				new A03TableCellRenderer.DateRenderer());
		installRendererIfNecessary(Boolean.class,
				new A03TableCellRenderer.BooleanRenderer());

		if (A03Constants.IS_JAVA_6_OR_LATER) {
			table.setFillsViewportHeight(true);
		} else {
			table.addPropertyChangeListener("ancestor", createAncestorPropertyChangeListener());
		}
	}
	
	/*
	 * http://explodingpixels.wordpress.com/
	 */
	
	private PropertyChangeListener createAncestorPropertyChangeListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // indicate that the parent of the JTable has changed.
                parentDidChange();
            }
        };
    }

    private void parentDidChange() {
        // if the parent of the table is an instance of JViewport, and that JViewport's parent is
        // a JScrollpane, then install the custom BugFixedViewportLayout.
        if (table.getParent() instanceof JViewport
                && table.getParent().getParent() instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            scrollPane.getViewport().setLayout(new BugFixedViewportLayout());
        }
    }


	// Kirill Grouchnikov 2007/11/05
	protected void installRendererIfNecessary(Class<?> clazz,
			TableCellRenderer renderer) {
		TableCellRenderer currRenderer = this.table.getDefaultRenderer(clazz);
		if (currRenderer != null) {
			boolean isCore = (currRenderer instanceof DefaultTableCellRenderer.UIResource)
					|| (currRenderer.getClass().getName()
							.startsWith("javax.swing.JTable"));
			if (!isCore)
				return;
		}

		this.table.setDefaultRenderer(clazz, renderer);
	}

	// Kirill Grouchnikov 2007/11/05
	@Override
	protected void uninstallDefaults() {
		for (Map.Entry<Class<?>, TableCellRenderer> entry : this.defaultRenderers
				.entrySet()) {
			uninstallRendererIfNecessary(entry.getKey(), entry.getValue());
		}

		super.uninstallDefaults();
	}

	// Kirill Grouchnikov 2007/11/05
	protected void uninstallRendererIfNecessary(Class<?> clazz,
			TableCellRenderer renderer) {
		TableCellRenderer currRenderer = this.table.getDefaultRenderer(clazz);
		if (currRenderer != null) {
			boolean isA03Renderer = (currRenderer instanceof A03TableCellRenderer)
					|| (currRenderer instanceof A03TableCellRenderer.BooleanRenderer);
			if (!isA03Renderer)
				return;
		}
		if (renderer instanceof Component)
			SwingUtilities.updateComponentTreeUI((Component) renderer);
		this.table.setDefaultRenderer(clazz, renderer);
	}

	public void paint(Graphics g, JComponent c) {
		Rectangle clip = g.getClipBounds();
		
		Rectangle bounds = table.getBounds();
		bounds.x = bounds.y = 0;

		boolean ltr = table.getComponentOrientation().isLeftToRight();

		Point upperLeft = clip.getLocation();
		if (!ltr) {
			upperLeft.x++;
		}

		Point lowerRight = new Point(clip.x + clip.width - (ltr ? 1 : 0),
				clip.y + clip.height);

		int rMin = table.rowAtPoint(upperLeft);
		int rMax = table.rowAtPoint(lowerRight);
		// This should never happen (as long as our bounds intersect the clip,
		// which is why we bail above if that is the case).
		if (rMin == -1) {
			rMin = 0;
		}
		// If the table does not have enough rows to fill the view we'll get -1.
		// (We could also get -1 if our bounds don't intersect the clip,
		// which is why we bail above if that is the case).
		// Replace this with the index of the last row.
		if (rMax == -1) {
			rMax = table.getRowCount() - 1;
		}

		int cMin = table.columnAtPoint(ltr ? upperLeft : lowerRight);
		int cMax = table.columnAtPoint(ltr ? lowerRight : upperLeft);
		// This should never happen.
		if (cMin == -1) {
			cMin = 0;
		}
		// If the table does not have enough columns to fill the view we'll get
		// -1.
		// Replace this with the index of the last column.
		if (cMax == -1) {
			cMax = table.getColumnCount() - 1;
		}

		// Paint the cells.
		paintCells(g, rMin, rMax, cMin, cMax);

		// Paint the grid.
		paintGrid(g, rMin, rMax, cMin, cMax);
	}

	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);		
		
		JTableHeader header = table.getTableHeader();
		TableColumn draggedColumn = (header == null) ? null : header.getDraggedColumn();

		TableColumnModel cm = table.getColumnModel();

		Rectangle cellRect;
		TableColumn aColumn;

		/*
		 * Compute visible table width
		 */
		int width = 0;

		for (int column = cMin; column <= cMax; column++) {
			aColumn = cm.getColumn(column);
			
			width += aColumn.getWidth();
		}

		boolean rowSelectionAllowed = table.getRowSelectionAllowed();
		
		boolean columnSelectionAllowed = table.getColumnSelectionAllowed();
		
		Color selectionBackground = table.getSelectionBackground();
		
//		if (table.getComponentOrientation().isLeftToRight()) {
			for (int row = rMin; row <= rMax; row++) {
				/*
				 * Paint row background
				 */
				cellRect = table.getCellRect(row, cMin, true);
				int x = cellRect.x;
				int y = cellRect.y;
				int height = cellRect.height;

				boolean rowSelected = table.isRowSelected(row);
				if (
					    !columnSelectionAllowed &&
						rowSelected &&
						rowSelectionAllowed
					) {
					graphics.setColor(selectionBackground);
				} else {
					graphics.setColor(uiDelegate.getBackground(table, row, cMin));
				} 
				graphics.fillRect(x, y, width, height);

				for (int column = cMin; column <= cMax; column++) {
					cellRect = table.getCellRect(row, column, false);
					
					if (columnSelectionAllowed) {
						boolean columnSelected = table.isColumnSelected(column);
						
						if (rowSelectionAllowed) {
							if (columnSelected && rowSelected) {
								graphics.setColor(selectionBackground);
							} else {
								graphics.setColor(uiDelegate.getBackground(table, row, column));
							}
						} else {
							if (columnSelected) {
								graphics.setColor(selectionBackground);
							} else {
								graphics.setColor(uiDelegate.getBackground(table, row, column));
							}
						}
						
						Rectangle cellRect0 = table.getCellRect(row, column, true);
						graphics.fillRect(cellRect0.x, cellRect0.y, cellRect0.width, cellRect0.height);
					}
					
					aColumn = cm.getColumn(column);
					
					if (aColumn != draggedColumn) {
						paintCell(graphics, cellRect, row, column);
					}
				}
			}
//		} else {
//			for (int row = rMin; row <= rMax; row++) {
//				cellRect = table.getCellRect(row, cMax, true);
//
//				int x = cellRect.x;
//				int y = cellRect.y;
//				int height = cellRect.height;
//				
//				boolean rowSelected = table.isRowSelected(row);
//				
//				if (
//					    !columnSelectionAllowed &&
//						rowSelected &&
//						rowSelectionAllowed
//					) {
//					graphics.setColor(selectionBackground);
//				} else {
//					graphics.setColor(uiDelegate.getBackground(table, row, cMin));
//				} 
//				graphics.fillRect(x, y, width, height);
//				
//				for (int column = cMin; column <= cMax; column++) {
//					cellRect = table.getCellRect(row, column, false);
//					
//					if (columnSelectionAllowed) {
//						boolean columnSelected = table.isColumnSelected(column);
//						
//						if (rowSelectionAllowed) {
//							if (columnSelected && rowSelected) {
//								graphics.setColor(selectionBackground);
//							} else {
//								graphics.setColor(uiDelegate.getBackground(table, row, column));
//							}
//						} else {
//							if (columnSelected) {
//								graphics.setColor(selectionBackground);
//							} else {
//								graphics.setColor(uiDelegate.getBackground(table, row, column));
//							}
//						}
//						
//						Rectangle cellRect0 = table.getCellRect(row, column, true);
//						graphics.fillRect(cellRect0.x, cellRect0.y, cellRect0.width, cellRect0.height);
//					}
//					
//					aColumn = cm.getColumn(column);
//					
//					if (aColumn != draggedColumn) {
//						paintCell(graphics, cellRect, row, column);
//					}
//				}
//			}
//		}
		
		cellRect = table.getCellRect(rMax, cMin, true);

		/*
		 * Inspired by
		 * 
		 * 		http://explodingpixels.wordpress.com/
		 */

        // get the y coordinate of the first row to paint. if there are no rows in the table, start
        // painting at the top of the supplied clipping bounds.
        int topY = cellRect.y + cellRect.height;

        // create a counter variable to hold the current row. if there are no rows in the table,
        // start the counter at 0.
        int currentRow = rMax + 1;
        
        Rectangle clipBounds = graphics.getClipBounds();
        
        int y = topY;
        
        int rowHeight = table.getRowHeight();
        while (y < clipBounds.y + clipBounds.height) {
        	graphics.setColor(uiDelegate.getBackground(table, currentRow, -1));
        	graphics.fillRect(clipBounds.x, y, clipBounds.width, rowHeight);
            y += rowHeight;
            currentRow ++;
        }

		// Paint the dragged column if we are dragging.
//		if (draggedColumn != null) {
//			paintDraggedArea(g, rMin, rMax, draggedColumn, header
//					.getDraggedDistance());
//		}

		// Remove any renderers that may be left in the rendererPane.
		rendererPane.removeAll();
		
		graphics.dispose();
	}

//	private void paintDraggedArea(Graphics g, int rMin, int rMax,
//			TableColumn draggedColumn, int distance) {
//		int draggedColumnIndex = viewIndexForColumn(draggedColumn);
//
//		Rectangle minCell = table.getCellRect(rMin, draggedColumnIndex, true);
//		Rectangle maxCell = table.getCellRect(rMax, draggedColumnIndex, true);
//
//		Rectangle vacatedColumnRect = minCell.union(maxCell);
//
//		// Paint a gray well in place of the moving column.
//		g.setColor(table.getParent().getBackground());
//		g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y,
//				vacatedColumnRect.width, vacatedColumnRect.height);
//
//		// Move to the where the cell has been dragged.
//		vacatedColumnRect.x += distance;
//
//		// Fill the background.
//		g.setColor(table.getBackground());
//		g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y,
//				vacatedColumnRect.width, vacatedColumnRect.height);
//
//		// Paint the vertical grid lines if necessary.
//		if (table.getShowVerticalLines()) {
//			g.setColor(table.getGridColor());
//			int x1 = vacatedColumnRect.x;
//			int y1 = vacatedColumnRect.y;
//			int x2 = x1 + vacatedColumnRect.width - 1;
//			int y2 = y1 + vacatedColumnRect.height - 1;
//			// Left
//			g.drawLine(x1 - 1, y1, x1 - 1, y2);
//			// Right
//			g.drawLine(x2, y1, x2, y2);
//		}
//
//		for (int row = rMin; row <= rMax; row++) {
//			// Render the cell value
//			Rectangle r = table.getCellRect(row, draggedColumnIndex, true);
//			r.x += distance;
//			paintCell(g, r, row, draggedColumnIndex);
//
//			// Paint the (lower) horizontal grid line if necessary.
//			if (table.getShowHorizontalLines()) {
//				g.setColor(table.getGridColor());
//				Rectangle rcr = table
//						.getCellRect(row, draggedColumnIndex, true);
//				rcr.x += distance;
//				int x1 = rcr.x;
//				int y1 = rcr.y;
//				int x2 = x1 + rcr.width - 1;
//				int y2 = y1 + rcr.height - 1;
//				g.drawLine(x1, y2, x2, y2);
//			}
//		}
//	}

	private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
		if (table.isEditing() && table.getEditingRow() == row
				&& table.getEditingColumn() == column) {
			JComponent component = (JComponent) table.getEditorComponent();

			component.setBounds(cellRect);

			component.validate();
		} else {
			Graphics2D graphics = (Graphics2D) g.create();
			
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);

			if (component instanceof A03TableCellRenderer ||
					component instanceof A03TableCellRenderer.BooleanRenderer) {
				if (
						(row == table.getSelectedRow() && table.getRowSelectionAllowed() && 
								(
										!table.getColumnSelectionAllowed() ||
										(
												column == table.getSelectedColumn() && table.getColumnSelectionAllowed()
										)
								)
						) ||
				        (!table.getRowSelectionAllowed() && column == table.getSelectedColumn() && table.getColumnSelectionAllowed())
				    ) {
					boolean isEnabled = table.isEnabled();
					Color fg = component.getForeground();
					if (fg instanceof ColorUIResource) {
						fg = isEnabled ?
								uiDelegate.getSelectionForeground() :
								uiDelegate.getDisabledSelectionForeground();
					}
					
					component.setForeground(fg);
				} else {
					Color fg = component.getForeground();
					if (fg instanceof ColorUIResource) {
						fg = uiDelegate.getForeground(table, row, column);
					}
					
					component.setForeground(fg);
				}
			}

			rendererPane.paintComponent(graphics, component, table, cellRect.x,
					cellRect.y, cellRect.width, cellRect.height, true);
			
			graphics.dispose();
		}
	}
	
	private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setColor(table.getGridColor());
		
		Stroke stroke = uiDelegate.getGridStroke();
		if (stroke != null) {
			graphics.setStroke(stroke);
		}

		Rectangle minCell = table.getCellRect(rMin, cMin, true);
		Rectangle maxCell = table.getCellRect(rMax, cMax, true);
		Rectangle damagedArea = minCell.union(maxCell);
		
		int tableWidth = damagedArea.x + damagedArea.width;

		Rectangle clipBounds = g.getClipBounds();

		int tableHeight = clipBounds.y + clipBounds.height;
		
		Dimension intercellSpacing = table.getIntercellSpacing();
		
		if (intercellSpacing.height > 0) {
			if (table.getShowHorizontalLines()) {
				int y = damagedArea.y;
				for (int row = rMin; row < rMax; row++) {
					y += table.getRowHeight(row);
					graphics.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
				}
				
				/*
				 * Draw empty cells horizontal grid lines
				 */
	            int rowHeight = table.getRowHeight();
		        while (y < tableHeight) {
		        	graphics.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
		            y += rowHeight;
		        }
			}
		}

		if (intercellSpacing.width > 0) {
			TableColumnModel cm = table.getColumnModel();
			int x;
			if (table.getShowVerticalLines()) {
				if (table.getComponentOrientation().isLeftToRight()) {
					x = damagedArea.x;
					for (int column = cMin; column < cMax; column++) {
						int w = cm.getColumn(column).getWidth();
						x += w;
						graphics.drawLine(x - 1, 0, x - 1, tableHeight - 1);
					}
				} else {
					x = damagedArea.x + damagedArea.width;
					for (int column = cMin; column < cMax; column++) {
						int w = cm.getColumn(column).getWidth();
						x -= w;
						graphics.drawLine(x - 1, 0, x - 1, tableHeight - 1);
					}
				}
			}
		}
	}

	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		if (c.isOpaque()) {
			graphics.setColor(c.getBackground());
			graphics.fillRect(0, 0,
					c.getWidth(), c.getHeight());
		}

		paint(graphics, c);

		graphics.dispose();
	}
	
	/*
	 * http://explodingpixels.wordpress.com/
	 */
	
	/**
     * A modified ViewportLayout to fix the JFC bug where components that implement Scrollable do
     * not resize correctly, if their size is less than the viewport size.
     * This is a JDK1.2.2 bug (id 4310721). This used to work in Swing 1.0.3 and the fix is putting
     * the old logic back.
     * Copied from: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4310721
     */
    private class BugFixedViewportLayout extends ViewportLayout {
        /**
		 * 
		 */
		private static final long serialVersionUID = -5825893010963635676L;

		public void layoutContainer(Container parent) {
        	JViewport vp = (JViewport)parent;
        	Component view = vp.getView();
        	Scrollable scrollableView = null;

        	if (view == null) {
        	    return;
        	}
        	else if (view instanceof Scrollable) {
        	    scrollableView = (Scrollable) view;
        	}

        	Dimension viewPrefSize = view.getPreferredSize();
        	Dimension vpSize = vp.getSize();
        	Dimension extentSize = vp.toViewCoordinates(vpSize);
        	Dimension viewSize = new Dimension(viewPrefSize);

        	if (scrollableView != null) {
        	    if (scrollableView.getScrollableTracksViewportWidth()) {
        	    	viewSize.width = vpSize.width;
        	    }
        	    if (scrollableView.getScrollableTracksViewportHeight()) {
        	    	viewSize.height = vpSize.height;
        	    }
        	}

        	Point viewPosition = vp.getViewPosition();

        	/* If the new viewport size would leave empty space to the
        	 * right of the view, right justify the view or left justify
        	 * the view when the width of the view is smaller than the
        	 * container.
        	 */
        	if (scrollableView == null ||
        	    vp.getParent() == null ||
        	    vp.getParent().getComponentOrientation().isLeftToRight()) {
        	    if ((viewPosition.x + extentSize.width) > viewSize.width) {
        	    	viewPosition.x = Math.max(0, viewSize.width - extentSize.width);
        	    }
        	} else {
        	    if (extentSize.width > viewSize.width) {
        	    	viewPosition.x = viewSize.width - extentSize.width;
        	    } else {
        	    	viewPosition.x = Math.max(0, Math.min(viewSize.width - extentSize.width, viewPosition.x));
        	    }
        	}

        	if ((viewPosition.y + extentSize.height) > viewSize.height) {
        	    viewPosition.y = Math.max(0, viewSize.height - extentSize.height);
        	}

        	if (scrollableView == null) {
                if ((viewPosition.x == 0)
						&& (vpSize.width > viewPrefSize.width)) {
					viewSize.width = vpSize.width;
				}
				if ((viewPosition.y == 0)
						&& (vpSize.height > viewPrefSize.height)) {
					viewSize.height = vpSize.height;
				}
			}
        	
        	// Fill empty space
        	if ((viewPosition.x == 0) && (vpSize.width > viewPrefSize.width)) {
				viewSize.width = vpSize.width;
			}

			if ((viewPosition.y == 0) && (vpSize.height > viewPrefSize.height)) {
				viewSize.height = vpSize.height;
			}

        	vp.setViewPosition(viewPosition);
        	vp.setViewSize(viewSize);
        }
    }

}
