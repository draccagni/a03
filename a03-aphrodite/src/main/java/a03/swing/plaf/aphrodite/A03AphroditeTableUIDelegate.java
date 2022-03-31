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
package a03.swing.plaf.aphrodite;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.basic.A03BasicFonts;
import a03.swing.plaf.basic.A03BasicTableUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeTableUIDelegate extends A03BasicTableUIDelegate implements A03AphroditePaints {

	private static Stroke gridStroke;

	public A03AphroditeTableUIDelegate() {
	}

	public ColorUIResource getForeground(JTable table, int row, int column) {
		int state = A03BasicUtilities.getState(table);
		
		double fadeLevel = fadeTracker.getFadeLevel(table, row, column);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		return new ColorUIResource(getForegroundColor(state, row, column));
	}

	public ColorUIResource getSelectionForegroundColor() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(CONTROL);
	}
	
	public ColorUIResource getBackground(JTable table, int row, int column) {
		int state = A03BasicUtilities.getState(table);
		
		double fadeLevel = fadeTracker.getFadeLevel(table, row, column);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		return new ColorUIResource(getBackgroundColor(state, row, column));
	}

	public ColorUIResource getSelectionBackgroundColor() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
//	public ColorUIResource getGridColor() {
//		return new ColorUIResource(Color.BLACK);
//	}
	
	public Stroke getGridStroke() {
		if (gridStroke == null) {
			/*
			 * Performance
			 */
//			gridStroke = new BasicStroke(0);
			float dash[] = {1.0f, 1.0f};
			gridStroke = new BasicStroke (0, BasicStroke.CAP_BUTT,
	                 BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		}
		
	    return gridStroke;
	}
	
	public ColorUIResource getDisabledSelectionBackground() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}
	
//	public Color getDisabledSelectionForeground() {
//		return Color.GRAY;
//	}
	
	public ColorUIResource getBackgroundColor(int state, int row, int column) {
		if ((state & ARMED) != 0) {
			return getSelectionBackgroundColor();
		} else {
			return new ColorUIResource(ROW_COLORS[row % ROW_COLORS.length]);
		}
	}

	public ColorUIResource getForegroundColor(int state, int row, int column) {
		if ((state & ARMED) != 0) {
			return getSelectionForegroundColor();
		} else {
			return new ColorUIResource(Color.BLACK);
		}
	}
	
	@Override
	public ColorUIResource getGridColor() {
		return new ColorUIResource(SCROLL_BAR_BORDER_COLOR);
	}
}
