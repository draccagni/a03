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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03TableUIDelegate;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicTableUIDelegate implements A03TableUIDelegate, A03ComponentState {

	private static Stroke gridStroke;
	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicTableUIDelegate() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 2;
		insets.left = 1;
		insets.right = 2;
		insets.bottom = 1;

		return insets;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing here
		
	}
	
	public InsetsUIResource getScrollPaneBorderInsets(Component c, Insets insets) {
		/*
		 * must be != null
		 */
		return new InsetsUIResource(insets.top, insets.left, insets.bottom, insets.right);
	}
	
	public void paintScrollPaneBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getForeground(JTable table, int row, int column) {
		int state = A03BasicUtilities.getState(table);
		
		double fadeLevel = fadeTracker.getFadeLevel(table, row, column);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		return new ColorUIResource(getForegroundColor(state, row, column));
	}

	public ColorUIResource getSelectionForeground() {
		return new ColorUIResource(getSelectionForegroundColor());
	}


	public Color getSelectionForegroundColor() {
		return Color.WHITE;
	}

	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getBackground(JTable table, int row, int column) {
		int state = A03BasicUtilities.getState(table);
		
		double fadeLevel = fadeTracker.getFadeLevel(table, row, column);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		return new ColorUIResource(getBackgroundColor(state, row, column));
	}

	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(getSelectionBackgroundColor());
	}
	
	public Color getSelectionBackgroundColor() {
		return Color.BLACK;
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getGridColor() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public Stroke getGridStroke() {
		if (gridStroke == null) {
			/*
			 * Performance
			 */
			gridStroke = new BasicStroke(0);
//			float dash[] = {1.0f, 1.0f};
//			gridStroke = new BasicStroke (0, BasicStroke.CAP_BUTT,
//	                 BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		}
		
	    return gridStroke;
	}
	
	public int getAscendingSortIconHeight() {
		return 14;
	}
	
	public int getAscendingSortIconWidth() {
		return 14;
	}
	
	public int getDescendingSortIconHeight() {
		return 14;
	}
	
	public int getDescendingSortIconWidth() {
		return 14;
	}
	
	public ColorUIResource getDisabledSelectionBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getDisabledSelectionForeground() {
		return new ColorUIResource(Color.GRAY);
	}
	
	public void paintAscendingSortIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g;

		int size = 8;

		x += 3;
		y += 3;

		A03BasicGraphicsUtilities.paintArrow(graphics, x, y, size, size,
				SwingConstants.SOUTH, Color.BLACK);
	}
	
	public void paintDescendingSortIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int size = 8;

		x += 3;
		y += 3;

		A03BasicGraphicsUtilities.paintArrow(graphics, x, y, size, size,
				SwingConstants.SOUTH, Color.BLACK);
	}
	
	public ColorUIResource getBackgroundColor(int state, int row, int column) {
		if ((state & ARMED) != 0) {
			return new ColorUIResource(Color.BLACK);
		} else {
			return new ColorUIResource(row % 2 == 0 ? Color.WHITE : Color.GRAY);
		}
	}

	public ColorUIResource getForegroundColor(int state, int row, int column) {
		if ((state & ARMED) != 0) {
			return new ColorUIResource(Color.WHITE);
		} else {
			return new ColorUIResource(Color.BLACK);
		}
	}
}
