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

import static a03.swing.plaf.A03Constants.E_RESIZE_CURSOR_INSTANCE;
import static a03.swing.plaf.A03Constants.N_RESIZE_CURSOR_INSTANCE;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import a03.swing.plaf.A03Constants;
import a03.swing.plaf.A03InternalFrameTitlePane;
import a03.swing.plaf.A03TitlePane;

public class A03CursorManagerPlugin extends A03UIPlugin {

	class CursorChangerListener extends MouseAdapter {
		
		@Override
		public void mouseEntered(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof JComponent) {
				JComponent c = (JComponent) e.getSource();

				Cursor cursor;
				if (c.isEnabled()) {
					if (c instanceof JToolBar || c instanceof A03TitlePane || c instanceof A03InternalFrameTitlePane) {
						cursor = A03Constants.MOVE_CURSOR_INSTANCE;
					} else if (c instanceof JScrollBar) {
						JScrollBar scrollbar = (JScrollBar) c;
						
						if (scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
							cursor = E_RESIZE_CURSOR_INSTANCE;
						} else {
							cursor = N_RESIZE_CURSOR_INSTANCE;
						}
						cursor = A03Constants.HAND_CURSOR_INSTANCE;
					} else if (c instanceof JSlider) {
						JSlider slider = (JSlider) c;
						
						if (slider.getOrientation() == JSlider.HORIZONTAL) {
							cursor = E_RESIZE_CURSOR_INSTANCE;
						} else {
							cursor = N_RESIZE_CURSOR_INSTANCE;
						}
					} else if (c instanceof JTextComponent) {
						cursor = A03Constants.TEXT_CURSOR_INSTANCE;
					} else {
						cursor = A03Constants.HAND_CURSOR_INSTANCE;
					}
				} else if (c instanceof JTableHeader) {
					cursor = A03Constants.E_RESIZE_CURSOR_INSTANCE;
				} else {
					cursor = A03Constants.DEFAULT_CURSOR_INSTANCE;
				}
				
				c.putClientProperty(A03Constants.ORIGINAL_CURSOR, c.getCursor());
				c.setCursor(cursor);
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			Object source = e.getSource();
			if (source instanceof JComponent) {
				JComponent c = (JComponent) source;
				
				c.setCursor((Cursor) c.getClientProperty(A03Constants.ORIGINAL_CURSOR));
				c.putClientProperty(A03Constants.ORIGINAL_CURSOR, null);
			}
		}
		
	}
	
	CursorChangerListener listener = new CursorChangerListener();
	
	public void installUI(Component c) {
		c.addMouseListener(listener);
	}

	public void uninstallUI(Component c) {
		c.removeMouseListener(listener);
	}

	public void installDefaults(UIDefaults table) {
		// do nothing
	}
}
