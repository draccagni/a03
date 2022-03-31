/*
 * Copyright (c) 2005-2007 Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of 
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
import java.awt.Graphics;
import java.text.SimpleDateFormat;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Kirill Grouchnikov
 * @author Davide Raccagni (A03 version)
 */

public class A03TableCellRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7799932802450118618L;

	private static final Border noFocusBorder = new EmptyBorder(2, 1, 1, 2);

	/**
	 * Renderer for boolean columns.
	 * 
	 * @author Kirill Grouchnikov
	 */
	public static class BooleanRenderer extends JCheckBox implements
			TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4194424044961042536L;
		
		/**
		 * Creates a new renderer for boolean columns.
		 */
		public BooleanRenderer() {
			super();
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setBorderPainted(true);

			this.setOpaque(false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent
		 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			this.setSelected(((value != null) && ((Boolean) value).booleanValue()));
			
			this.setEnabled(table.isEnabled());
			
			this.setBorder(noFocusBorder);

			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 */
		@Override
		public final void paint(Graphics g) {
			super.paint(g);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected final void paintComponent(Graphics g) {
			super.paintComponent(g);
		}
	}

	public static class IconRenderer extends A03TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5050808272348452615L;

		public IconRenderer() {
			super();
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setText(null);
		}

		@Override
		public void setValue(Object value) {
			this.setIcon((value instanceof Icon) ? (Icon) value : null);
		}
	}

	public static class NumberRenderer extends A03TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3005802337956053117L;

		public NumberRenderer() {
			super();
			this.setHorizontalAlignment(SwingConstants.RIGHT);
		}
	}

	public static class DateRenderer extends A03TableCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7702255836498522701L;
		private static final SimpleDateFormat formatter = new SimpleDateFormat(UIManager.getString("Table.cellRendererDateFormat"));

		@Override
		public void setValue(Object value) {
			this.setText((value == null) ? "" : formatter.format(value));
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			
			setHorizontalAlignment(CENTER);

			return this;
		}
	}
	
	public A03TableCellRenderer() {
		setOpaque(false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		this.setFont(table.getFont());
		
		this.setValue(value);
		
		if (hasFocus) {
			Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = noFocusBorder;
            }
            setBorder(border);
		} else {
			this.setBorder(noFocusBorder);
		}

		return this;
	}
}
