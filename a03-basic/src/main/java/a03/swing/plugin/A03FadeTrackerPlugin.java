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

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.TreePath;

import a03.swing.plaf.A03SwingUtilities;

public class A03FadeTrackerPlugin extends A03UIPlugin {

	private static final String FADE_EFFECT_HANDLER = "FADE_EFFECT_HANDLER";
	private static final double FADE_EFFECT_TIME_STEP = 0.25;
	
	private interface Handler {		
	}
	
	private class ComponentHandler
			implements ActionListener, MouseListener, MouseMotionListener, Handler {
	
		private Timer timer = null;
		private double time = 0.0;
		private double timeStep;
		private double level = 0.0;
		private Component component;
		
		ComponentHandler(Component c) {
			component = c;
		}
		
		public void mouseEntered(MouseEvent e) {
			if (component.isEnabled()) {
				startTimer(FADE_EFFECT_TIME_STEP);
			}
		}
		
		public void mouseExited(MouseEvent e) {
			if (component.isEnabled()) {
				startTimer(-FADE_EFFECT_TIME_STEP);
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			startTimer(-FADE_EFFECT_TIME_STEP);
		}
		
	    private void startTimer(double timeStep) {
	    	this.timeStep = timeStep;
	    	
	    	if (timer == null) {
		    	timer = new Timer(30, new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		        		level = calculateFadeLevel(time);

		        		time += ComponentHandler.this.timeStep;

		        		if ((ComponentHandler.this.timeStep > 0 && level >= 1.0) || (ComponentHandler.this.timeStep < 0 && level <= 0)) {
		        			synchronized (component) {
			            		stopTimer();
							}
		                	
		            		level = ComponentHandler.this.timeStep > 0 ? 1.0 : 0.0;
		            	} else if (ComponentHandler.this.timeStep > 0 && level <= 0.0) {
		            		level = 0;
		            	} else if (ComponentHandler.this.timeStep < 0 && level >= 1.0) {
		            		level = 1.0;
		            	}
		            	
		            	component.repaint();
		            }    		
		    	});
		    	
		    	
	    	}

			synchronized (component) {
		    	timer.start();
			}
	    }
	    
	    private void stopTimer() {
	    	if (timer != null) {
	    		timer.stop();
	    		timer = null;
	    	}	    	
	    }
	    
	    public double getFadeLevel() {
	    	return level;
	    }
	    
	    public void mouseMoved(MouseEvent e) {
	    	// do nothing
	    }
	    
	    public void mouseDragged(MouseEvent e) {
	    	// do nothing
	    }
	    
	    public void mouseClicked(MouseEvent e) {
	    	// do nothing
	    }
	    
	    public void mousePressed(MouseEvent e) {
	    	// do nothing
	    }
	    
	    public void mouseReleased(MouseEvent e) {
	    	// do nothing
	    }
	}
	
	private class MenuItemHandler extends MouseAdapter implements ActionListener, Handler {
		
		private double level = 0;
		private JMenuItem component;
		
		MenuItemHandler(JMenuItem c) {
			component = c;
		}
		
		public void mouseEntered(MouseEvent e) {
			if (component.isEnabled()) {
				level = 1;
				
				// DO NOT REPAINT
				//component.repaint();
			}
		}
		
		public void mouseExited(MouseEvent e) {
			if (component.isEnabled()) {
				level = 0;
				
				// DO NOT REPAINT
				//component.repaint();
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			if (component.isEnabled()) {
				level = 0;
				
				// DO NOT REPAINT
				//component.repaint();
			}
		}
		
	    public double getFadeLevel() {
	    	return level;
	    }
	}

	
	private class SliderHandler extends ComponentHandler {
		
		SliderHandler(JSlider slider) {
			super(slider);
		}
		
		public void mouseDragged(MouseEvent e) {
			((JSlider) e.getSource()).repaint();
		}
	}

//	private class MenuItemHandler extends MouseAdapter implements ActionListener, Handler {
//		
//		private double level = 0;
//		private JMenuItem component;
//		
//		MenuItemHandler(JMenuItem c) {
//			component = c;
//		}
//		
//		public void mouseEntered(MouseEvent e) {
//      	if (component.isEnabled() && A03SwingUtils.isParentEnabled(component)) {
//				level = 1;
//				
//				component.repaint();
//			}
//		}
//		
//		public void mouseExited(MouseEvent e) {
//	        if (component.isEnabled() && A03SwingUtils.isParentEnabled(component)) {
//				level = 0;
//				
//				component.repaint();
//			}
//		}
//		
//		public void actionPerformed(ActionEvent e) {
//			level = 0;
//				
//			component.repaint();
//		}
//		
//	    public double getFadeLevel() {
//	    	return level;
//	    }
//	}
	
	abstract class RowHandler extends MouseInputAdapter implements Handler {

		protected JComponent component;

		protected int lastRow = -1;

		public RowHandler(JComponent component) {
			this.component = component;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (component.isEnabled()) {
				int row = getRow(e);

				lastRow = row;
				
				component.repaint();
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			if (component.isEnabled()) {
				lastRow = -1;
				
				component.repaint();
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			if (component.isEnabled()) {
				int row = getRow(e);
				if (row != lastRow) {
					lastRow = row;
					
					component.repaint();
				}
			}
		}
		
		protected abstract int getRow(MouseEvent e);

		public double getFadeLevel(int row) {
			return lastRow == row ? 1 : 0;
		}		
	}

	class ListHandler extends RowHandler {

		public ListHandler(JList list) {
			super(list);
		}

		@Override
		protected int getRow(MouseEvent e) {
			int row = A03SwingUtilities.loc2IndexFileList((JList) component, e.getPoint());
			return row;
		}
	}


	class TreeHandler extends RowHandler {

		public TreeHandler(JTree tree) {
			super(tree);
		}
		
		@Override
		protected int getRow(MouseEvent e) {
			JTree tree = (JTree) component;
			
			TreePath closestPath = tree.getClosestPathForLocation(e.getX(), e
					.getY());
			Rectangle bounds = tree.getPathBounds(closestPath);
			
			int row = -1;
			if (
					bounds != null &&
					(e.getY() > bounds.y && e.getY() < (bounds.y + bounds.height))
				) {
				row = tree.getRowForLocation(bounds.x, bounds.y);
			}
			return row;
		}
	}

	class TableHandler  extends MouseInputAdapter implements Handler {

		protected JTable table;

		protected int lastRow = -1;

		protected int lastColumn = -1;

		public TableHandler(JTable table) {
			this.table = table;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			table.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (table.isEnabled()) {
				boolean needsRepaint = false;
				
				if (table.getRowSelectionAllowed()) {
					int row = getRow(e);

					needsRepaint = true;

					lastRow = row;
				}
				
				if (table.getColumnSelectionAllowed()) {
					int column = getColumn(e);
	
					lastColumn = column;
					
					needsRepaint = true;
				}
				
				if (needsRepaint) {
					table.repaint();
				}
			}
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			if (table.isEnabled()) {
				lastRow = -1;
				
				lastColumn = -1;
				
				if (
						table.getRowSelectionAllowed() ||
						table.getColumnSelectionAllowed()
					) {
					table.repaint();
				}
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			if (table.isEnabled()) {
				boolean needsRepaint = false;
				
				if (table.getRowSelectionAllowed()) {
					int row = getRow(e);
					if (row != lastRow) {
						lastRow = row;
						
						needsRepaint = true;
					}
				}
				
				if (table.getColumnSelectionAllowed()) {
					int column = getColumn(e);
					if (column != lastColumn) {
						lastColumn = column;
						
						needsRepaint = true;
					}
				}
				
				if (needsRepaint) {
					table.repaint();
				}
			}
		}
		
		public double getFadeLevel(int row, int column) {
			if (lastRow == -1 && lastColumn == -1) {
				return 0;
			}
			
			boolean rowSelectionAllowed = table.getRowSelectionAllowed();
			
			boolean columnSectionAllowed = table.getColumnSelectionAllowed();
			
			if (rowSelectionAllowed && columnSectionAllowed) {
				return row == lastRow && column == lastColumn ? 1 : 0;
			}
			
			return (rowSelectionAllowed && lastRow == row) || 
				(columnSectionAllowed && column == lastColumn) ? 1 : 0;
		}		
		
		
		protected int getRow(MouseEvent e) {
			int row = table.rowAtPoint(e.getPoint());
			return row;
		}
		
		protected int getColumn(MouseEvent e) {
			int column = table.columnAtPoint(e.getPoint());
			return column;
		}
	}
	
    public void installUI(Component c) {
    	if (c instanceof JMenuItem) {
    		installMenuItemUI((JMenuItem) c);
    	} else if (c instanceof JSlider) {
        	installSliderUI((JSlider) c);
    	} else if (c instanceof JTable) {
    		installTableUI((JTable) c);
    	} else if (c instanceof JList) {
    		installListUI((JList) c);
    	} else if (c instanceof JTree) {
    		installTreeUI((JTree) c);
    	} else if (c instanceof JComponent) {
        	installComponentUI((JComponent) c);
    	}
    }
    
    public void uninstallUI(Component c) {
    	if (c instanceof JMenuItem) {
    		uninstallMenuItemUI((JMenuItem) c);
    	} else if (c instanceof JSlider) {
        	uninstallSliderUI((JSlider) c);
    	} else if (c instanceof JTable) {
    		uninstallTableUI((JTable) c);
    	} else if (c instanceof JList) {
    		uninstallListUI((JList) c);
    	} else if (c instanceof JTree) {
    		uninstallTreeUI((JTree) c);
    	} else if (c instanceof JComponent) {
        	uninstallComponentUI((JComponent) c);
    	}
    }
    
	public void installMenuItemUI(JMenuItem menuItem) {
		MenuItemHandler handler = new MenuItemHandler(menuItem);
		menuItem.putClientProperty(FADE_EFFECT_HANDLER, handler);
		
		menuItem.addMouseListener(handler);
    	menuItem.addActionListener(handler);
	}
	
	public void uninstallMenuItemUI(JMenuItem menuItem) {
		MenuItemHandler handler = (MenuItemHandler) menuItem.getClientProperty(FADE_EFFECT_HANDLER);
		
		menuItem.removeMouseListener(handler);
		menuItem.removeActionListener(handler);
	}
    
	private double getMenuItemFadeLevel(JMenuItem menuItem) {
		MenuItemHandler handler = (MenuItemHandler) menuItem.getClientProperty(FADE_EFFECT_HANDLER);
		
	    double level = handler != null ? handler.getFadeLevel() : 0.0;
		return level;
	}


    public void installComponentUI(JComponent c) {
    	ComponentHandler handler = new ComponentHandler(c); 
    	
    	c.addMouseListener(handler);
    	
        if (c instanceof AbstractButton) {
        	AbstractButton b = (AbstractButton) c;
        	
        	b.addActionListener(handler);
        }
        
        c.putClientProperty(FADE_EFFECT_HANDLER, handler);
    }
    
    public void uninstallComponentUI(JComponent c) {
    	ComponentHandler handler = (ComponentHandler) c.getClientProperty(FADE_EFFECT_HANDLER);

    	c.removeMouseListener(handler);
        
        if (c instanceof AbstractButton) {
        	AbstractButton b = (AbstractButton) c;
        	
        	b.removeActionListener(handler);
        }
    }
    
    private double getComponentFadeLevel(Component c) {
		if (!c.isEnabled()) {
			return 0;
		}
    	
    	ComponentHandler handler = (ComponentHandler) ((JComponent) c).getClientProperty(FADE_EFFECT_HANDLER);
		
	    double level = handler != null ? handler.getFadeLevel() : 0.0;
		return level;
	}

	public void installSliderUI(JSlider slider) {
    	SliderHandler handler = new SliderHandler(slider); 
    	
		slider.addMouseMotionListener(handler);
    	slider.addMouseListener(handler);
    	
        slider.putClientProperty(FADE_EFFECT_HANDLER, handler);
    }
    
	public void uninstallSliderUI(JSlider slider) {
		SliderHandler handler = (SliderHandler) slider.getClientProperty(FADE_EFFECT_HANDLER);

    	slider.removeMouseListener(handler);
	}
    
	private double getSliderFadeLevel(JSlider slider) {
		if (!slider.isEnabled()) {
			return 0;
		}
		
		SliderHandler handler = (SliderHandler) slider.getClientProperty(FADE_EFFECT_HANDLER);

		return handler != null ? handler.getFadeLevel() : 0;
	}    
    
	public void installTableUI(JTable table) {
		TableHandler handler = new TableHandler(table);
		table.putClientProperty(FADE_EFFECT_HANDLER, handler);
		
		table.addMouseMotionListener(handler);
		table.addMouseListener(handler);
	}
	
	
	public void uninstallTableUI(JTable table) {
		TableHandler handler = (TableHandler) table.getClientProperty(FADE_EFFECT_HANDLER);
		
		table.removeMouseListener(handler);
		table.removeMouseMotionListener(handler);
	}
	
	public void installListUI(JList list) {
		ListHandler handler = new ListHandler(list);
		list.putClientProperty(FADE_EFFECT_HANDLER, handler);
		
		list.addMouseMotionListener(handler);
		list.addMouseListener(handler);
	}
	
	
	public void uninstallListUI(JList list) {
		ListHandler handler = (ListHandler) list.getClientProperty(FADE_EFFECT_HANDLER);
		
		list.removeMouseListener(handler);
		list.removeMouseMotionListener(handler);
	}
	
    public void installTreeUI(JTree tree) {
		TreeHandler handler = new TreeHandler(tree);
		tree.putClientProperty(FADE_EFFECT_HANDLER, handler);
		
		tree.addMouseMotionListener(handler);
		tree.addMouseListener(handler);
	}
	
	
	public void uninstallTreeUI(JTree tree) {
		TreeHandler handler = (TreeHandler) tree.getClientProperty(FADE_EFFECT_HANDLER);
		
        tree.removeMouseListener(handler);
        tree.removeMouseMotionListener(handler);
	}
	
	protected static double calculateFadeLevel(double time) {
//		double expValue = Math.exp(time);
//		
//		double value = expValue - 1;
//
//		return value;
		return time;
	}	

	public void installDefaults(UIDefaults table) {
		// do nothing
	}
	
	public double getFadeLevel(Component c) {
		if (c instanceof JMenuItem) {
			return getMenuItemFadeLevel((JMenuItem) c);
		} else if (c instanceof JSlider) {
			return getSliderFadeLevel((JSlider) c);
		} else {
			return getComponentFadeLevel(c);
		}
	}
	
	public double getFadeLevel(Component c, int row) {
		if (!c.isEnabled()) {
			return 0;
		}
		
		RowHandler handler = (RowHandler) ((JComponent) c).getClientProperty(FADE_EFFECT_HANDLER);

		return handler != null ? handler.getFadeLevel(row) : 0;
	}
	
	public double getFadeLevel(Component c, int row, int column) {
		if (!c.isEnabled()) {
			return 0;
		}
		
		TableHandler handler = (TableHandler) ((JComponent) c).getClientProperty(FADE_EFFECT_HANDLER);

		return handler != null ? handler.getFadeLevel(row, column) : 0;
	}
}
