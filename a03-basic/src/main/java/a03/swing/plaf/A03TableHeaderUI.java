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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class A03TableHeaderUI extends BasicTableHeaderUI {

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03TableHeaderUI();
	}

	private A03TableHeaderUIDelegate uiDelegate;

	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03TableHeaderUIDelegate.class);

		super.installUI(c);
		
	    header.setFont(uiDelegate.getFont());
	}

	@Override
	public void update(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g.create();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		if (c.isOpaque()) {
			graphics.setColor(c.getBackground());
			graphics.fillRect(0, 0, c.getWidth(), c.getHeight());
		}

		paint(graphics, c);

		graphics.dispose();
	}

	//
	// Instance Variables
	//

	// The column header over which the mouse currently is.
	private int rolloverColumn = -1;

	// The column that should be highlighted when the table header has the
	// focus.
	private int selectedColumnIndex = 0; // Read ONLY via
											// getSelectedColumnIndex!

	//
	// Support for mouse rollover
	//

	/**
	 * Returns the index of the column header over which the mouse currently is.
	 * When the mouse is not over the table header, -1 is returned.
	 * 
	 * @see #rolloverColumnUpdated(int, int)
	 * @return the index of the current rollover column
	 * @since 1.6
	 */
	protected int getRolloverColumn() {
		return rolloverColumn;
	}

	/**
	 * This method gets called every time the rollover column in the table
	 * header is updated. Every look and feel supporting rollover effect in
	 * table header should override this method and repaint the header.
	 * 
	 * @param oldColumn
	 *            the index of the previous rollover column or -1 if the mouse
	 *            was not over a column
	 * @param newColumn
	 *            the index of the new rollover column or -1 if the mouse is not
	 *            over a column
	 * @see #getRolloverColumn()
	 * @see JTableHeader#getHeaderRect(int)
	 * @since 1.6
	 */
	protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
	}

	private int getSelectedColumnIndex() {
		int numCols = header.getColumnModel().getColumnCount();
		if (selectedColumnIndex >= numCols && numCols > 0) {
			selectedColumnIndex = numCols - 1;
		}
		return selectedColumnIndex;
	}

	public void paint(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g;

		uiDelegate.paintBackground(header, graphics);

		if (header.getColumnModel().getColumnCount() <= 0) {
			return;
		}
		boolean ltr = header.getComponentOrientation().isLeftToRight();

		Rectangle clip = graphics.getClipBounds();
		Point left = clip.getLocation();
		Point right = new Point(clip.x + clip.width - 1, clip.y);
		TableColumnModel cm = header.getColumnModel();
		int cMin = header.columnAtPoint(ltr ? left : right);
		int cMax = header.columnAtPoint(ltr ? right : left);
		// This should never happen.
		if (cMin == -1) {
			cMin = 0;
		}
		// If the table does not have enough columns to fill the view we'll get
		// -1.
		// Replace this with the index of the last column.
		if (cMax == -1) {
			cMax = cm.getColumnCount() - 1;
		}

		TableColumn draggedColumn = header.getDraggedColumn();
		int columnWidth;
		Rectangle cellRect = header.getHeaderRect(ltr ? cMin : cMax);
		TableColumn aColumn;
		if (ltr) {
			for (int column = cMin; column <= cMax; column++) {
				aColumn = cm.getColumn(column);
				columnWidth = aColumn.getWidth();
				cellRect.width = columnWidth;
				if (aColumn != draggedColumn) {
					paintCell(graphics, cellRect, column);
				}
				cellRect.x += columnWidth;
			}
		} else {
			for (int column = cMax; column >= cMin; column--) {
				aColumn = cm.getColumn(column);
				columnWidth = aColumn.getWidth();
				cellRect.width = columnWidth;
				if (aColumn != draggedColumn) {
					paintCell(graphics, cellRect, column);
				}
				cellRect.x += columnWidth;
			}
		}

		// Paint the dragged column if we are dragging.
		if (draggedColumn != null) {
        	graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));

			int draggedColumnIndex = viewIndexForColumn(draggedColumn);
			Rectangle draggedCellRect = header
					.getHeaderRect(draggedColumnIndex);

			// Draw a gray well in place of the moving column.
			graphics.setColor(header.getParent().getBackground());
			graphics.fillRect(draggedCellRect.x, draggedCellRect.y,
					draggedCellRect.width, draggedCellRect.height);

			draggedCellRect.x += header.getDraggedDistance();

			// Fill the background.
			graphics.setColor(header.getBackground());
			graphics.fillRect(draggedCellRect.x, draggedCellRect.y,
					draggedCellRect.width, draggedCellRect.height);

			paintCell(graphics, draggedCellRect, draggedColumnIndex);
		}

		// Remove all components in the rendererPane.
		rendererPane.removeAll();

		paintGrid(graphics, c);
	}

	private Component getHeaderRenderer(int columnIndex) {
		TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
		TableCellRenderer renderer = aColumn.getHeaderRenderer();
		if (renderer == null) {
			renderer = header.getDefaultRenderer();
		}

		boolean hasFocus = (columnIndex == getSelectedColumnIndex())
				&& header.hasFocus();
		Component component = renderer.getTableCellRendererComponent(header.getTable(),
				aColumn.getHeaderValue(), false, hasFocus, -1, columnIndex);
		
		
		JTable table = header.getTable();
		A03TableUIDelegate uiDelegate = A03SwingUtilities.getUIDelegate(table, A03TableUIDelegate.class);
		component.setForeground(uiDelegate.getForeground(table, -1, -1));
		
		return component;
	}

	private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
		Component component = getHeaderRenderer(columnIndex);
		rendererPane.paintComponent(g, component, header, cellRect.x,
				cellRect.y, cellRect.width, cellRect.height, true);
	}

	private int viewIndexForColumn(TableColumn aColumn) {
		TableColumnModel cm = header.getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			if (cm.getColumn(column) == aColumn) {
				return column;
			}
		}
		return -1;
	}
	
    /*
     * org.jvnet.substance.SubstanceTableHeaderUI
     */
	
	protected void paintGrid(Graphics g, JComponent c) {
		Graphics2D graphics = (Graphics2D) g;

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);

		JTable table = header.getTable(); 
		Color gridColor = table.getGridColor();
		graphics.setColor(gridColor);

		Rectangle visibleRect = table.getVisibleRect();
		visibleRect.x = 0;
		visibleRect.y = 0; 
		visibleRect.width++;
		visibleRect.height = c.getHeight() - 1;
		
		graphics.draw(visibleRect);
		
		boolean ltr = header.getComponentOrientation().isLeftToRight();

		Rectangle clip = g.getClipBounds();
		Point left = clip.getLocation();
		left = new Point(left.x - 2, left.y);
		Point right = new Point(clip.x + clip.width + 2, clip.y);
		
		TableColumnModel cm = header.getColumnModel();

		int cMin = header.columnAtPoint(ltr ? left : right);
		int cMax = header.columnAtPoint(ltr ? right : left);
		// This should never happen.
		if (cMin == -1) {
			cMin = 0;
		}

		Rectangle cellRect0 = header.getHeaderRect(cMin);
		int bottom = cellRect0.y + cellRect0.height - 1;

		// If the table does not have enough columns to fill the view we'll
		// get
		// -1.
		// Replace this with the index of the last column.
		if (cMax == -1) {
			cMax = cm.getColumnCount() - 1;
		}

		TableColumn draggedColumn = header.getDraggedColumn();
		TableColumn aColumn;
		if (ltr) {
			for (int column = cMin + 1; column <= cMax; column++) {
				Rectangle cellRect = header.getHeaderRect(column);
				aColumn = cm.getColumn(column);
				if (aColumn != draggedColumn) {
					graphics.drawLine(cellRect.x, cellRect.y, cellRect.x,
							bottom);
				}
			}
		} else {
			for (int column = cMax - 1; column >= cMin; column--) {
				Rectangle cellRect = header.getHeaderRect(column);
				aColumn = cm.getColumn(column);
				if (aColumn != draggedColumn) {
					graphics.drawLine(cellRect.x, cellRect.y, cellRect.x,
							bottom);
				}
			}			
		}
	}
}
