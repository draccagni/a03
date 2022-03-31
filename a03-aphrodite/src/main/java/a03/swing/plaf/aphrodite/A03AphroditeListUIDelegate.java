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

import java.awt.Color;

import javax.swing.JList;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.basic.A03BasicFonts;
import a03.swing.plaf.basic.A03BasicListUIDelegate;
import a03.swing.plaf.basic.A03BasicUtilities;

public class A03AphroditeListUIDelegate extends A03BasicListUIDelegate implements A03AphroditePaints {

	public A03AphroditeListUIDelegate() {
	}
	
	@Override
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getForeground(JList list, int layoutOrientation, int row) {
		int state = A03BasicUtilities.getState(list);
		
		double fadeLevel = fadeTracker.getFadeLevel(list, row);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		if (layoutOrientation == JList.VERTICAL) {
			return getForeground(state, row);
		} else {
			if ((state & ARMED) != 0) {
				return getSelectionForeground();
			} else {
				return getForeground();
			}
		}
	}
	
	public ColorUIResource getBackground(JList list, int layoutOrientation, int row) {
		int state = A03BasicUtilities.getState(list);
		
		double fadeLevel = fadeTracker.getFadeLevel(list, row);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		if (layoutOrientation == JList.VERTICAL) {
			return getBackground(state, row);
		} else {
			if ((state & ARMED) != 0) {
				return getSelectionBackground();
			} else {
				return getBackground();
			}
		}
	}
	
	public ColorUIResource getSelectionForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getBackground(int state, int row) {
		if ((state & ARMED) != 0) {
			return new ColorUIResource(ROW_ARMED_COLOR);
		} else {
			return new ColorUIResource(ROW_COLORS[row % ROW_COLORS.length]);
		}
	}

	public ColorUIResource getForeground(int state, int row) {
		if ((state & ENABLED) != 0 ) {
			if ((state & ARMED) != 0) {
				return getSelectionForeground();
			} else {
				return new ColorUIResource(Color.BLACK);
			}
		} else {
			return new ColorUIResource(Color.BLACK);
		}
	}
	
	public ColorUIResource getDisabledSelectionBackground() {
		return new ColorUIResource(ROW_ARMED_COLOR);
	}

	public ColorUIResource getDisabledSelectionForeground() {
		return new ColorUIResource(Color.GRAY);
	}

}	

