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
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import a03.swing.plaf.A03TreeUIDelegate;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03UIPluginManager;

public class A03BasicTreeUIDelegate implements A03TreeUIDelegate, A03ComponentState {
	
	private static final InsetsUIResource margin = new InsetsUIResource(1, 1, 1, 1);
	
	protected A03FadeTrackerPlugin fadeTracker;

	public A03BasicTreeUIDelegate() {
		this.fadeTracker = A03UIPluginManager.getInstance().getUIPlugins().get(A03FadeTrackerPlugin.class);
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		return insets;
	}
	
	public int getRowHeight() {
		return 16;
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public FontUIResource getFont() {
		return new FontUIResource(A03BasicFonts.FONT_PLAIN_11);
	}
	
	public ColorUIResource getForeground(JTree tree, int row) {
		int state = A03BasicUtilities.getState(tree);
		
		double fadeLevel = fadeTracker.getFadeLevel(tree, row);
		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		return getForeground(state, row);
	}
	
	public ColorUIResource getBackground(JTree tree, int row) {
		int state = A03BasicUtilities.getState(tree);
		
		double fadeLevel = fadeTracker.getFadeLevel(tree, row);

		if (fadeLevel > 0) {
			state |= ARMED;
		}
		
		return getBackground(state, row);
	}
	
	public ColorUIResource getSelectionForeground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public ColorUIResource getSelectionBackground() {
		return new ColorUIResource(Color.BLACK);
	}
	
	public ColorUIResource getBackground() {
		return new ColorUIResource(Color.WHITE);
	}
	
	public int getTreeOpenIconHeight() {
		return 0;
	}
	
	public int getTreeOpenIconWidth() {
		return 0;
	}
	
	public void paintTreeOpenIcon(Component c, Graphics g, int x, int y) {
		// do nothing
	}
	
	public int getTreeClosedIconHeight() {
		return 0;
	}
	
	public int getTreeClosedIconWidth() {
		return 0;
	}
	
	public int getTreeCollapsedIconHeight() {
		return 14;
	}
	
	public int getTreeCollapsedIconWidth() {
		return 14;
	}
	
	public int getTreeExpandedIconHeight() {
		return 14;
	}
	
	public int getTreeExpandedIconWidth() {
		return 14;
	}
	
	public int getTreeLeafIconHeight() {
		return 0;
	}
	
	public int getTreeLeafIconWidth() {
		return 0;
	}
	
	public void paintTreeClosedIcon(Component c, Graphics g, int x, int y) {
		// do nothing
	}
	
	public void paintTreeExpandedIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		graphics.translate(x, y);

        graphics.setColor(c.getBackground());
        graphics.fillRect(2, 3, 8, 8);

        graphics.setColor(UIManager.getColor("controlDkShadow"));
        graphics.drawRect(2, 3, 8, 8);
		graphics.drawLine(4, 7, 8, 7);
		
		graphics.dispose();
	}
	
	public void paintTreeCollapsedIcon(Component c, Graphics g, int x, int y) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		
		graphics.translate(x, y);

        graphics.setColor(c.getBackground());
        graphics.fillRect(2, 3, 8, 8);

        graphics.setColor(UIManager.getColor("controlDkShadow"));
        graphics.drawRect(2, 3, 8, 8);
		graphics.drawLine(4, 7, 8, 7);
		graphics.drawLine(6, 5, 6, 9);
		
		graphics.dispose();
	}
	
	public void paintTreeLeafIcon(Component c, Graphics g, int x, int y) {
		// do nothing
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

	public ColorUIResource getBackground(int state, int row) {
		if ((state & ARMED) != 0) {
			return getSelectionBackground();
		} else {
			return new ColorUIResource(row == -1 ? Color.WHITE : (row % 2 == 0 ? Color.WHITE : Color.GRAY));
		}
	}

	@Override
	public InsetsUIResource getRendererMargins() {
		return margin;
	}
}
