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
import java.awt.Insets;

import javax.swing.JList;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import a03.swing.plaf.A03ListUIDelegate;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicListUIDelegate implements A03ListUIDelegate, A03ComponentState {

	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicListUIDelegate() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}
	
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 0;
		insets.left = 0;
		insets.right = 0;
		insets.bottom = 0;

		return insets;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		// do nothing
	}
	
	public Insets getNoFocusBorderInsets(Component c, Insets insets) {
		insets.top = 1;
		insets.left = 1;
		insets.right = 1;
		insets.bottom = 1;

		return insets;
	}
	
	public void paintNoFocusBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
	}
	
	public Insets getFocusBorderInsets(Component c, Insets insets) {
		insets.top = 1;
		insets.left = 1;
		insets.right = 1;
		insets.bottom = 1;

		return insets;
	}
	
	public void paintFocusBorder(Component c, Graphics g, int x, int y,
			int width, int height) {
		// do nothing
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
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getForeground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getBackground(int state, int row) {
		if ((state & ARMED) != 0) {
			return getSelectionBackground();
		} else {
			return new ColorUIResource(row % 2 == 0 ? Color.WHITE : Color.GRAY);
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
		return new ColorUIResource(Color.GRAY);
	}

	public ColorUIResource getDisabledSelectionForeground() {
		return new ColorUIResource(Color.WHITE);
	}

}	

