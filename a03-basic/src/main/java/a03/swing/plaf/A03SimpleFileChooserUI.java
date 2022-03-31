/*
 * Copyright (c) 2007 Davide Raccagni. All Rights Reserved.
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

import static a03.swing.plaf.A03Constants.DEFAULT_CURSOR_INSTANCE;
import static a03.swing.plaf.A03Constants.WAIT_CURSOR_INSTANCE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class A03SimpleFileChooserUI extends BasicFileChooserUI {

	private static final Hashtable<String, Icon> systemIcons = new Hashtable<String, Icon>();
	private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
	
	private static class AllFileFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			return true;
		}
		
		@Override
		public String getDescription() {
			return "";
		}
	}
	
	private static FileFilter ALL_FILEFILTER = new AllFileFilter();
	
	private JTable fileTable;
	private FileTableModel fileTableModel;
	private TableRowSorter<FileTableModel> sorter;

	private JScrollPane tableScrollPane;

	private DefaultTreeModel directoryTreeModel;
	private DirectoryTreeNode currentNode;
	private JTree directoryTree;

	private ListSelectionListener listSelectionListener;
	
	private JButton approveButton;
	private JButton cancelButton;

	private Locale locale;

	private JPopupMenu filePopupMenu;
	private JMenuItem newFolderItem;
	private JMenuItem deleteItem;
	private JMenuItem renameItem;
	
    private JPanel accessoryContainer;
    
	private boolean filePathSelectionIsAdjusting;
	private boolean directoryTreeSelectionIsAdjusting;
	private boolean fileSelectionsIsAdjusting;

	private String filename;
	

	class ListSelectionListenerImpl implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting() && !fileSelectionsIsAdjusting) {
				//DefaultListSelectionModel source = (DefaultListSelectionModel) e.getSource();
				
				Object target = getFileTable();
				
				File[] selectedFiles = getSelectedFiles(target);
				
				if (selectedFiles.length > 0) {
					List<File> files = new ArrayList<File>();
					
					for (File selectedFile : selectedFiles) {
						if (
								(
										getFileChooser().isDirectorySelectionEnabled() && selectedFile.isDirectory()
								) || 
								(
										!getFileChooser().isDirectorySelectionEnabled() && !selectedFile.isDirectory()
								)
							) {
							files.add(selectedFile);
						}
					}

					if (files.isEmpty()) {
						if (getFileChooser().isMultiSelectionEnabled()) {
							getFileChooser().setSelectedFiles(new File[] {});
						} else {
							getFileChooser().setSelectedFile(null);
						}
					} else if (files.size() == 1) {
						if (getFileChooser().isMultiSelectionEnabled()) {
							getFileChooser().setSelectedFiles(files.toArray(new File[1]));
						} else {
							getFileChooser().setSelectedFile(files.get(0));
						}
 					} else if (files.size() > 1) {
						getFileChooser().setSelectedFiles(files.toArray(new File[files.size()]));							
					}  
				} else if (!filePathSelectionIsAdjusting) {
					getFileChooser().setSelectedFile(null);
				}
				
				approveButton.setEnabled(getFileChooser().getSelectedFile() != null);
			}
		}
	}
	
	class PropertyChangeListenerImpl implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			String name = e.getPropertyName();

			if (name.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
				File currentDirectory = (File) e.getNewValue();

				currentNode = getDirectoryTreeNode(currentDirectory);
				
				TreePath path = new TreePath(currentNode.getPath());
				
				getDirectoryTree().expandPath(path);
				
				if (!directoryTreeSelectionIsAdjusting) {
					getDirectoryTree().setSelectionPath(path);
				}
				
				updateDetails();				
			} else if (name.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
				accessoryContainer.removeAll();
				
				JComponent accessory = (JComponent) e.getNewValue();
				
				accessoryContainer.add(accessory, BorderLayout.CENTER);
			} else if (name.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
				fileSelectionsIsAdjusting = true;
				
				File file = (File) e.getNewValue();
				if (file != null) {
					getFileTableModel().selectIfPossible(file);
				}
				
				String filename = file != null ? file.getAbsolutePath() : null;					
				setFileName(filename);										
				
				fileSelectionsIsAdjusting = false;
			} else if(name.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
				approveButton.setText(getApproveButtonText(getFileChooser()));
				approveButton.setToolTipText(getApproveButtonToolTipText(getFileChooser()));
			} else if (name.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
				fileSelectionsIsAdjusting = true;
				
				StringBuffer sb = new StringBuffer();
				
				File[] files = (File[]) e.getNewValue();
				if (files.length > 0) {
					for (File file : files) {
						if (sb.length() > 0) {
							sb.append(File.pathSeparatorChar);
						}
						
						sb.append(file.getAbsolutePath());
						
						getFileTableModel().selectIfPossible(file);
					}
				}
				
				setFileName(sb.toString());
				
				fileSelectionsIsAdjusting = false;
			} else if (name.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
				int selectionMode = (Integer) e.getNewValue();

				getFileTable().getSelectionModel().setSelectionMode(selectionMode);
			} else if (name.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY)) { 
				approveButton.setText((String) e.getNewValue());
			} else if (name.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY)) { 
				approveButton.setToolTipText((String) e.getNewValue());
			} else if(name.equals(JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY)) {
				boolean shown = (Boolean) e.getNewValue();
				
				approveButton.setVisible(shown);
				cancelButton.setVisible(false);
			} else if (name.equals("componentOrientation")) {
			    ComponentOrientation orientation = (ComponentOrientation) e.getNewValue();
			    if (orientation != (ComponentOrientation) e.getOldValue()) {
			    	getFileChooser().applyComponentOrientation(orientation);
			    }
			} else if (name.equals("ancestor")) {
				getDirectoryTreeModel().reload();
			}
		}
	}

	class DirectoryNodeRenderer extends DefaultTreeCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5338193231571179409L;

		public DirectoryNodeRenderer() {
			setBorderSelectionColor(UIManager.getColor("Tree.selectionBackground"));
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

			DirectoryTreeNode node = (DirectoryTreeNode) value;

			setText(getFileChooser().getName(node.getFile()));
			setIcon(getSystemIcon(node.getFile()));

			return this;
		}
	}

	class DirectoryTreeNode extends DefaultMutableTreeNode {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1254389343396857828L;

		private File file;

		private File[] files = null;

		public DirectoryTreeNode(File file) {
			super(file);

			this.file = file;
		}

		public File getFile() {
			return file;
		}

		public boolean isLeaf() {
			return !file.isDirectory();
		}

		public File[] getFiles() {
			if (files == null) {
				rescan();
			}
			return files;
		}

		public void rescan() {
			try {
				files = getFileChooser().getFileSystemView().getFiles(file, getFileChooser().isFileHidingEnabled());
				
				removeAllChildren();

				for (File file : files) {
					if (file.isDirectory()) {
						add(new DirectoryTreeNode(file));
					}
				}
			} catch (Throwable t) {
				
			}
		}
	}

	class DirectoryTreeWillExpandListener implements TreeWillExpandListener {
		public void treeWillExpand(final TreeExpansionEvent e) {
			getFileChooser().setCursor(WAIT_CURSOR_INSTANCE);
			TreePath path = e.getPath();
			DirectoryTreeNode node = (DirectoryTreeNode) path.getLastPathComponent();
			try {
				if (!node.isLeaf() && !node.children().hasMoreElements()) {
					node.rescan();
				}

				currentNode = node;
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				getFileChooser().setCursor(DEFAULT_CURSOR_INSTANCE);
			}
		}

		public void treeWillCollapse(TreeExpansionEvent e) {
		}
	}

	class FileTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -5842775978156316657L;

		String[] columnNames = new String[] {
				UIManager.getString("FileChooser.fileNameHeaderText", locale),
				UIManager.getString("FileChooser.fileDateHeaderText", locale),
		};

		List<File> files;

		public FileTableModel() {
			files = new ArrayList<File>();
		}
		
		public Object getValueAt(int row, int col) {
			File file = files.get(row);
			switch (col) {
			case 0:
				return file.getName();
			case 1:
				return new Date(file.lastModified());
			default:
				return null;
			}
		}

		public void setCurrentNode(DirectoryTreeNode currentNode) {
			files = new ArrayList<File>();

			files.add(new File(".."));
			for (File file : currentNode.getFiles()) {
				if (getFileChooser().getFileSystemView().isFileSystem(file)) {
					if (getFileChooser().isDirectorySelectionEnabled()) {
						if (file.isDirectory()) {
							files.add(file);
						}
					} else {
						files.add(file);
					}
				}
			}

			this.fireTableDataChanged();
		}

		public int getRowCount() {
			return files == null ? 0 : files.size();
		}

		public File getFileAt(int index) {
			return files.get(index);
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Class<?> getColumnClass(int col) {
			switch (col) {
			case 1:
				return Date.class;
			default:
				return String.class;
			}
		}

		public void selectIfPossible(File file) {
			for (int i = 0; i < files.size(); i++) {
				File filteredFile = files.get(sorter.convertRowIndexToModel(i));
				if (filteredFile.getAbsolutePath().equals(file.getAbsolutePath())) {
					JTable table = getFileTable();

					table.getSelectionModel().setSelectionInterval(i, i);
			        break;
				}
			}
		}
	}

	class FileTableCellRenderer extends A03TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2898644540191122620L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			int index = sorter.convertRowIndexToModel(row);
			
			File file = getFileTableModel().getFileAt(index);
			Color foreground = table.getForeground();

			switch (column) {
			case 0:
				if (file.isDirectory()) {
					setForeground(UIManager.getColor("FileChooser.directoryFileTextColor"));
				} else if (file.isHidden()) {
					setForeground(UIManager.getColor("FileChooser.hiddenFileTextColor"));
				} else if (!file.canWrite()) {
					setForeground(UIManager.getColor("FileChooser.readOnlyFileTextColor"));
				}

				setText(getFileChooser().getName(file));
				setIcon(getSystemIcon(file));
				setHorizontalAlignment(SwingConstants.LEFT);
				break;
			case 1:
				setForeground(foreground);
				Date date = (Date) value;
				
				String text;
				if (date != null) {
					text = simpleDateTimeFormat.format(date);
				} else {
					text = "";
				}
				setText(text);
				setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			default:
				setIcon(null);
				setHorizontalAlignment(SwingConstants.LEFT);
				break;
			}

			return this;
		}
	}
	
	class DnDTableHandler extends DragSourceAdapter implements DragGestureListener, DropTargetListener {
		private DragSource dragSource;

		private JTable table;

		public DnDTableHandler(JTable table) {
			this.table = table;
			dragSource = new DragSource();
			dragSource.createDefaultDragGestureRecognizer(table, DnDConstants.ACTION_MOVE, this);
			
			new DropTarget(table, this);
		}

		public void dragGestureRecognized(DragGestureEvent dge) {
			StringBuilder sb = new StringBuilder();
			int[] rows = table.getSelectedRows();
			if( rows.length > 0 ) {
				for (int row : rows) {
					File file = getFileTableModel().getFileAt(sorter.convertRowIndexToModel(row));
					sb.append(file.getAbsolutePath());
					sb.append(File.pathSeparatorChar);
				}
				
				StringSelection ss = new StringSelection(sb.toString());
			
				this.dragSource.startDrag( dge, DragSource.DefaultCopyDrop, ss, this );
			}
		}
		
		public void dragEnter(DropTargetDragEvent dtde) {
			// do nothing
		}
		
		public void dragExit(DropTargetEvent dte) {
			// do nothing
		}
		
		public void dragOver(DropTargetDragEvent dtde) {
			// do nothing
		}
		
		public void dropActionChanged(DropTargetDragEvent dtde) {
			// do nothing
		}
		
		public void drop(DropTargetDropEvent dtde) {
			Point mousePoint = dtde.getLocation();
			if (mousePoint != null) {
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					try {
						String str = (String) t.getTransferData(DataFlavor.stringFlavor);
						
						String[] paths = str.split(File.pathSeparator);

						int row = table.rowAtPoint(mousePoint);
						File destFile = getFileTableModel().getFileAt(sorter.convertRowIndexToModel(row));
					
						if (destFile.isDirectory()) {
							for (String path : paths) {
								// File (or directory) to be moved
							    File file = getFileChooser().getFileSystemView().createFileObject(path);
							    
							    String newPath = destFile.getAbsolutePath() + File.separatorChar + file.getName();
							    
							    // Move file to new directory
							    boolean success = file.renameTo(new File(newPath));
							    if (!success) {
							        JOptionPane.showMessageDialog(getFileChooser(), "System Message", "Cannot move '" + path + "' file", JOptionPane.ERROR_MESSAGE);
							        break;
							    }
							}
		
							dtde.getDropTargetContext().dropComplete(true);
							
							getFileChooser().setCurrentDirectory(destFile);
						} else {
							dtde.getDropTargetContext().dropComplete(false);
						}
					} catch (Exception e) {
						e.printStackTrace();
						dtde.getDropTargetContext().dropComplete(false);
					}
				}
			}
		}
	}

	class DnDTreeHandler extends DropTarget {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5701314883967362981L;
		private JTree tree;
		
		public DnDTreeHandler(JTree tree) {
			this.tree = tree;
			
			new DropTarget( tree, this ); 
		}

		public void drop(DropTargetDropEvent dtde) {
			Point mousePoint = dtde.getLocation();
			if (mousePoint != null) {
				TreePath selPath = tree.getPathForLocation(mousePoint.x, mousePoint.y);
				if (selPath == null) {
					return;
				}

				DirectoryTreeNode node = (DirectoryTreeNode) selPath.getLastPathComponent();
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
					try {
						String str = (String) t.getTransferData(DataFlavor.stringFlavor);
						
						String[] paths = str.split(File.pathSeparator);
						
						for (String path : paths) {
							// File (or directory) to be moved
						    File file = getFileChooser().getFileSystemView().createFileObject(path);
						    
						    String newPath = node.getFile().getAbsolutePath() + File.separatorChar + file.getName();
						    
						    // Move file to new directory
						    boolean success = file.renameTo(new File(newPath));
						    if (!success) {
						        JOptionPane.showMessageDialog(getFileChooser(), "System Message", "Cannot move '" + path + "' file", JOptionPane.ERROR_MESSAGE);
						        break;
						    }
						}
	
						dtde.getDropTargetContext().dropComplete(true);
						
						getFileChooser().setCurrentDirectory(node.getFile());
					} catch (Exception e) {
						e.printStackTrace();
						dtde.getDropTargetContext().dropComplete(false);
					}
				}
			}
		}
	}
	
	public A03SimpleFileChooserUI(JFileChooser fc) {
		super(fc);

		locale = fc.getLocale();
	}

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03SimpleFileChooserUI((JFileChooser) c);
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
	
		installComponents(getFileChooser());
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
	
		uninstallComponents((JFileChooser) c);
	}
	
	@SuppressWarnings("all")
	private JPanel buildSouthPanel() {
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		
		layout.setAlignment(FlowLayout.RIGHT);
		
		JPanel southPanel = new JPanel(layout);

		southPanel.setBorder(new EmptyBorder(3, 0, 0, 0));

		approveButton = new JButton(getApproveButtonText(getFileChooser()));
		approveButton.addActionListener(getApproveSelectionAction());
		approveButton.setToolTipText(getApproveButtonToolTipText(getFileChooser()));
		approveButton.setEnabled(false);
		southPanel.add(approveButton);

		southPanel.add(Box.createHorizontalStrut(4));

		cancelButton = new JButton(cancelButtonText);
		cancelButton.setToolTipText(cancelButtonToolTipText);
		cancelButton.addActionListener(getCancelSelectionAction());

		southPanel.add(cancelButton);

        return southPanel;		
	}

	private void buildDetailsPopupMenu() {
		filePopupMenu = new JPopupMenu();
		filePopupMenu.setOpaque(false);
		filePopupMenu.setDoubleBuffered(true);
		filePopupMenu.setFocusable(false);

		JMenuItem homeItem = new JMenuItem();
		homeItem.setAction(getGoHomeAction());
		filePopupMenu.add(homeItem);
		filePopupMenu.addSeparator();

		newFolderItem = new JMenuItem("New Folder");
		newFolderItem.setAction(getNewFolderAction());
		filePopupMenu.add(newFolderItem);
		deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(EventHandler.create(ActionListener.class, this, "doDelete"));
		filePopupMenu.add(deleteItem);
		renameItem = new JMenuItem("Rename");
		renameItem.addActionListener(EventHandler.create(ActionListener.class, this, "doRename"));
		filePopupMenu.add(renameItem);
	}
	
	public void doRename() {
		File file = getSelectedFiles(filePopupMenu.getInvoker())[0];
		
		String newName = JOptionPane.showInputDialog(getFileChooser(), "To", file.getName());
		if (newName != null) {
			File newFile = new File(file.getParentFile(), newName);
			
			if (file.renameTo(newFile)) {
				getFileChooser().setCurrentDirectory(newFile.getParentFile());
				rescanCurrentDirectory(getFileChooser());
			}
		}
	}

	public static boolean delete(File directory) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					boolean answer = delete(file);
					if (!answer) {
						return false;
					}
				} else {
					if (!file.delete()) {
						return false;
					}				
				}
			}
		}
		
		return directory.delete();
	}

	public void doDelete() {
		if (JOptionPane.showConfirmDialog(getFileChooser(), "Are you sure ?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			Object source = filePopupMenu.getInvoker();
			
			File[] files = getSelectedFiles(source);

			if (source == getFileTable()) {
				for (File file : files) {
					delete(file);
					
					if (file.isDirectory()) {
						DirectoryTreeNode node = getDirectoryTreeNode(file);
						node.removeFromParent();
					}
				}
				
				rescanCurrentDirectory(getFileChooser());
			} else {
				delete(currentNode.getFile());
				DirectoryTreeNode parent = (DirectoryTreeNode) currentNode.getParent();
				currentNode.removeFromParent();
				currentNode = parent;
				
				rescanCurrentDirectory(getFileChooser());
				
				getFileChooser().setCurrentDirectory(currentNode.getFile());
			}
		}
	}

	public void installComponents(JFileChooser fc) {
		fc.setBorder(new EmptyBorder(3, 3, 3, 3));

		fc.setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane();
		
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		splitPane.setLeftComponent(new JScrollPane(getDirectoryTree()));
		
		splitPane.setDividerLocation(300);
		
		splitPane.setRightComponent(getFileTableScrollPane());
		
		splitPane.setPreferredSize(new Dimension(800, 300));

		fc.add(splitPane, BorderLayout.CENTER);
		
		accessoryContainer = new JPanel();
		accessoryContainer.setLayout(new BorderLayout());
		fc.add(accessoryContainer, BorderLayout.EAST);

		JPanel southPanel = buildSouthPanel();
		fc.add(southPanel, BorderLayout.SOUTH);

		buildDetailsPopupMenu();
	}

	@Override
	public void uninstallComponents(JFileChooser fc) {
		fc.removeAll();
	}

	private Icon getSystemIcon(File f) {
		/*
		try {
			FileSystemView fsv = getFileChooser().getFileSystemView();

			ShellFolder sf = ShellFolder.getShellFolder(f);

			String folderType = sf.getFolderType();
			Icon icon = (Icon) systemIcons.get(folderType);
			if (icon == null) {
				icon = fsv.getSystemIcon(f);
				systemIcons.put(folderType, icon);
			}

			return icon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		*/
		return null;
	}

	@Override
	public void ensureFileIsVisible(JFileChooser fc, File f) {
		JTable table = getFileTable();
		int index = table.getSelectedRow();

		// http://www.exampledepot.com/egs/javax.swing.table/Vis.html
		
		if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) table.getParent();
    
        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(index, 0, true);
    
        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();
    
        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
    
        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
	}

	@Override
	public void rescanCurrentDirectory(JFileChooser fc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				currentNode.rescan();

				// directory deleted
				getDirectoryTreeModel().reload();
				
				// XXX the directory could be the same (New Folder) : in
				// this case 
				//		getFileChooser().setCurrentDirectory(currentNode.getFile());
				// has no effect.
				
				TreePath path = new TreePath(currentNode.getPath());
				
				getDirectoryTree().expandPath(path);
				
				getDirectoryTree().setSelectionPath(path);

				updateDetails();
			}
		});
	}

	@Override
	public PropertyChangeListener createPropertyChangeListener(JFileChooser fc) {
		return new PropertyChangeListenerImpl();
	}

	private DefaultTreeModel getDirectoryTreeModel() {
		if (directoryTreeModel == null) {
			currentNode = new DirectoryTreeNode(getFileChooser().getFileSystemView().getRoots()[0]);
			directoryTreeModel = new DefaultTreeModel(currentNode);
		}
	
		return directoryTreeModel;
	}

	private JTree getDirectoryTree() {
		if (directoryTree == null) {
			directoryTree = new JTree(getDirectoryTreeModel());
			new DnDTreeHandler(directoryTree);
			
			directoryTree.addTreeWillExpandListener(new DirectoryTreeWillExpandListener());
			
			directoryTree.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					processMouseEvent(directoryTree, e);
				}
			});
			
			directoryTree.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
					
					if (newLeadSelectionPath != null) {
	                    currentNode = (DirectoryTreeNode) newLeadSelectionPath.getLastPathComponent();
						if (currentNode != null) {
							directoryTreeSelectionIsAdjusting = true;
						
							getFileChooser().setCurrentDirectory(currentNode.getFile());
							
							directoryTreeSelectionIsAdjusting = false;
						}
					}
				}
			});
			
			directoryTree.setCellRenderer(new DirectoryNodeRenderer());
	
			directoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			directoryTree.setShowsRootHandles(false);
		}
	
		return directoryTree;
	}

	private DirectoryTreeNode getDirectoryTreeNode(File target) {
		DirectoryTreeNode node = (DirectoryTreeNode) getDirectoryTree().getModel().getRoot();
		
		FileSystemView fsw = getFileChooser().getFileSystemView();

		if (fsw.getParentDirectory(target) == null) {
			return node;
		}
		
		List<File> children = new ArrayList<File>();
	
		File file = target;
		do {
			children.add(file);
			file = fsw.getParentDirectory(file);
		} while (fsw.getParentDirectory(file) != null);
	
		for (int i = children.size(); --i >= 0;) { // parents.size() - 1 is the root
			file = children.get(i);
	
			Enumeration<?> e = node.children();
			if (!node.isLeaf() && !e.hasMoreElements()) {
				node.rescan();
				e = node.children();
			}
	
			for (; e.hasMoreElements();) {
				DirectoryTreeNode child = (DirectoryTreeNode) e.nextElement();
				
				File childFile = child.getFile();
				
				if (childFile.getAbsolutePath().equals(file.getAbsolutePath())) {
					node = child;
					break;
				}
			}
		}
	
		return node;
	}

	public ListSelectionListener getListSelectionListener() {
		if (listSelectionListener == null) {
			listSelectionListener = new ListSelectionListenerImpl();
		}
		return listSelectionListener;
	}

	public FileTableModel getFileTableModel() {
		if (fileTableModel == null) {
			fileTableModel = new FileTableModel();
		}

		return fileTableModel;
	}

    public JTable getFileTable() {
		if (fileTable == null) {
			fileTable = new JTable();
			
			new DnDTableHandler(fileTable);
			
			sorter = new TableRowSorter<>(getFileTableModel());
			getFileTable().setRowSorter(sorter);

			fileTable.getSelectionModel().addListSelectionListener(getListSelectionListener());
			
			fileTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					processMouseEvent(fileTable, e);
				}
			});
			
			fileTable.setShowHorizontalLines(false);
			fileTable.setShowVerticalLines(false);
			
			fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			
			fileTable.setRowHeight(19);

			FileTableCellRenderer renderer = new FileTableCellRenderer();

			//SortTableCellRenderer headerRenderer = new SortTableCellRenderer(fileTable);
			fileTable.getTableHeader().setReorderingAllowed(false);

			TableColumnModel tcm = fileTable.getColumnModel();
			TableColumn tc = tcm.getColumn(0);
			//tc.setHeaderRenderer(headerRenderer);
			tc.setCellRenderer(renderer);

			tc = tcm.getColumn(1);
			//tc.setHeaderRenderer(headerRenderer);
			tc.setCellRenderer(renderer);
			tc.setMinWidth(120);
			tc.setMaxWidth(120);
		}

		return fileTable;
	}

    private File[] getSelectedFiles(Object source) {
		File[] files = null;

		if (source == getFileTable()) {
			int[] rows = getFileTable().getSelectedRows();
    		files = new File[rows.length];
			for (int i = 0; i < rows.length; i++) {
				files[i] = (File) getFileTableModel().getFileAt(sorter.convertRowIndexToModel(rows[i]));
				/* JDK 6
				files[i] = (File) getFileTableModel().getFileAt(sorter.convertRowIndexToModel(rows[i]));
				*/
			}
    	} else if (source == getDirectoryTree()) {
    		files = new File[] { currentNode.getFile() };
    	}
    	
    	return files;
    }
    
    private void processMouseEvent(Component target, MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (target == getFileTable()) {
				int[] rows = getFileTable().getSelectedRows();
				
				boolean enabled;
				if (rows.length == 1) {
					File file = getFileTableModel().getFileAt(sorter.convertRowIndexToModel(rows[0]));
					enabled = isFileModifiable(file);
				} else {
					enabled = false;
				}
				
				
				deleteItem.setEnabled(enabled);
				renameItem.setEnabled(enabled);

				filePopupMenu.show(target, e.getX(), e.getY());
			} else {
				boolean enabled;
				
				int row = getDirectoryTree().getRowForLocation(e.getX(), e.getY());
				if (getDirectoryTree().isRowSelected(row)) {
					TreePath treePath = getDirectoryTree().getPathForRow(row);
					DirectoryTreeNode node = (DirectoryTreeNode) treePath.getLastPathComponent();
					
					File file = node.getFile();
					enabled = isFileModifiable(file);
				} else {
					enabled = false;
				}
				
				deleteItem.setEnabled(enabled);
				renameItem.setEnabled(enabled);

				filePopupMenu.show(target, e.getX(), e.getY());
			}
		} else {
			if (e.getClickCount() == 2) {
				File file = getSelectedFiles(target)[0];
				if (file.getName().equals("..")) {
					getChangeToParentDirectoryAction().actionPerformed(null);
				} else if (file.isDirectory()) {
					getFileChooser().setCursor(WAIT_CURSOR_INSTANCE);

					getFileChooser().setCurrentDirectory(file);

					getFileChooser().setCursor(DEFAULT_CURSOR_INSTANCE);
				}
			}
		}
    }
	
	private boolean isFileModifiable(File file) {
		return !(
				getFileChooser().getFileSystemView().isFileSystemRoot(file) ||
				getFileChooser().getFileSystemView().isRoot(file) ||
				getFileChooser().getFileSystemView().isDrive(file)
			);
	}
    
	private JScrollPane getFileTableScrollPane() {
		if (tableScrollPane == null) {
			tableScrollPane = new JScrollPane(getFileTable());
			tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}

		return tableScrollPane;
	}

	private void updateDetails() {
		getFileTableModel().setCurrentNode(currentNode);
	}
	
	@Override
	public void setFileName(String filename) {
		//filePathComboBox.setSelectedItem(filename);
		this.filename = filename;
		
		if (filename != null) {
			File file = getFileChooser().getFileSystemView().createFileObject(filename);
			filePathSelectionIsAdjusting = true;
			
			if (getFileChooser().isMultiSelectionEnabled()) {
				getFileChooser().setSelectedFiles(new File[] { file });
			} else {
				getFileChooser().setSelectedFile(file);
			}
		}
		
		approveButton.setEnabled(filename != null);
	}
	
	@Override
	public String getFileName() {
		return filename;
	}
	
	@Override
	public FileFilter getAcceptAllFileFilter(JFileChooser fc) {
		return ALL_FILEFILTER;
	}
}
