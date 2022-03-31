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

import static a03.swing.plaf.A03Constants.DEFAULT_CURSOR_INSTANCE;
import static a03.swing.plaf.A03Constants.WAIT_CURSOR_INSTANCE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FontMetrics;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

//import sun.awt.shell.ShellFolder;


public class A03AdvancedFileChooserUI extends BasicFileChooserUI {
	
	private static final int GAP = 4;
	
	private static List<String> fileHistory = new ArrayList<String>();
	private static List<String> directoryHistory = new ArrayList<String>();

	private static final Hashtable<String, Icon> systemIcons = new Hashtable<String, Icon>();
	private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
	
	private JList fileList;
	private FileListModel fileListModel;
	private JTable fileTable;
	private FileTableModel fileTableModel;
	private TableRowSorter<FileTableModel> sorter;

	private JScrollPane fileScrollPane;

	private DefaultTreeModel directoryTreeModel;
	private DirectoryTreeNode currentNode;
	private JTree directoryTree;

	private JLabel filterLabel;
	private JComboBox filterComboBox;
	
	private ListSelectionListener listSelectionListener;
	
	private JLabel choiceLabel;
	private JTextField fileTextField;
	private EditableComboBoxModel historyComboBoxModel;
	private EditableComboBoxModel filterComboBoxModel;

	private JButton approveButton;
	private JButton cancelButton;

	private Locale locale;

	private JPopupMenu filePopupMenu;
	private JMenuItem newFolderItem;
	private JMenuItem deleteItem;
	private JMenuItem renameItem;
	
    private JPanel accessoryContainer;
    
	private JComboBox navigationHistoryList;
	private static NavigationHistoryComboBoxModel navigationHistoryListModel;
	
	private boolean filePathSelectionIsAdjusting;
	private boolean directoryTreeSelectionIsAdjusting;
	private boolean fileSelectionsIsAdjusting;
	
	private WildcardsFileFilter wildcardsFileFilter;
    
    class FilterCellRenderer extends A03ListCellRenderer {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = -1565894849528339182L;

		@Override
    	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    		
    		if (value != null) {
    			FileFilter filter = (FileFilter) value;
    			setText(filter.getDescription());
    		}
    		
    		return this;
    	}
    	
    }

    class NavigationHistoryCellRenderer extends A03ListCellRenderer {
    	
    	/**
		 * 
		 */
		private static final long serialVersionUID = -1565894849528339182L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			File file = (File) value;
			setText(file.getAbsolutePath());
			return this;
		}
    }    
    
	class ListSelectionListenerImpl implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting() && !fileSelectionsIsAdjusting) {
				DefaultListSelectionModel source = (DefaultListSelectionModel) e.getSource();
				
				Object target = null;
				if (getFileList().getSelectionModel().equals(source)) {
					target = getFileList();
				} else {
					target = getFileTable();
				}
				
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
							String newPath = selectedFile.getAbsolutePath();
							historyComboBoxModel.addIfNecessary(newPath);
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
				
				approveButton.setEnabled(!fileTextField.getText().equals(""));
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
					File parentFile = file.getParentFile();
					
					getNavigationHistoryComboBoxModel().addIfNecessary(parentFile);
					
					getFileTableModel().selectIfPossible(file);
					
					getFileListModel().selectIfPossible(file);
				}
				
				String filename = file != null ? file.getAbsolutePath() : null;					
				setFileName(filename);										
				
				fileSelectionsIsAdjusting = false;
			} else if (name.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
				fileSelectionsIsAdjusting = true;
				
				StringBuffer sb = new StringBuffer();
				
				File[] files = (File[]) e.getNewValue();
				if (files.length > 0) {
					getNavigationHistoryComboBoxModel().addIfNecessary(files[0].getParentFile());

					for (File file : files) {
						if (sb.length() > 0) {
							sb.append(File.pathSeparatorChar);
						}
						
						sb.append(file.getAbsolutePath());
						
						getFileTableModel().selectIfPossible(file);
						
						getFileListModel().selectIfPossible(file);
					}
				}
				
				setFileName(sb.toString());
				
				fileSelectionsIsAdjusting = false;
			} else if (name.equals(JFileChooser.ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY)) {
				boolean acceptAllFileFilterUsed = (Boolean) e.getNewValue();
				if (acceptAllFileFilterUsed) {
					filterComboBoxModel.addIfNecessary(getAcceptAllFileFilter(getFileChooser()));
				} else {
					filterComboBoxModel.remove(getAcceptAllFileFilter(getFileChooser()));
				}
				
				SwingUtilities.updateComponentTreeUI(filterComboBox.getParent());
			} else if (name.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
				FileFilter filter = (FileFilter) e.getNewValue();
				
				filterComboBoxModel.addIfNecessary(filter);
				
				filterComboBox.setSelectedItem(filter);
				
				filterComboBox.getParent().doLayout();
			} else if (name.equals(JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY)) {
				FileFilter[] filters = (FileFilter[]) e.getNewValue();
				
				for (FileFilter filter : filters) {
					filterComboBoxModel.addIfNecessary(filter);
				}
				
				filterComboBox.getParent().validate();
			} else if (name.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
				int selectionMode = (Integer) e.getNewValue();

				getFileTable().getSelectionModel().setSelectionMode(selectionMode);
				
				getFileList().getSelectionModel().setSelectionMode(selectionMode);
			} else if (name.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY)) { 
				approveButton.setText((String) e.getNewValue());
			} else if (name.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY)) { 
				approveButton.setToolTipText((String) e.getNewValue());
			} else if(name.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
				if (e.getNewValue().equals(JFileChooser.DIRECTORIES_ONLY)) {
					historyComboBoxModel.setList(directoryHistory);
				} else {
					historyComboBoxModel.setList(fileHistory);					
				}

				approveButton.setText(getApproveButtonText(getFileChooser()));
				approveButton.setToolTipText(getApproveButtonToolTipText(getFileChooser()));
			} else if(name.equals(JFileChooser.APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY)) {
				// do nothing
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

	class FileCellRenderer extends A03ListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6645127403610636307L;
		
		public FileCellRenderer() {
			setOpaque(false);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			File file = (File) value;
			setText(getFileChooser().getName(file));
			setIcon(getSystemIcon(file));
			return this;
		}
	}

	class FileTableModel extends AbstractTableModel 
		// JDK 5 {
		implements DocumentListener {
		// } JDK 5
		/**
		 * 
		 */
		private static final long serialVersionUID = -5842775978156316657L;

		String[] columnNames = new String[] {
				UIManager.getString("FileChooser.fileNameHeaderText", locale),
				UIManager.getString("FileChooser.fileSizeHeaderText", locale),
				UIManager.getString("FileChooser.fileTypeHeaderText", locale),
				UIManager.getString("FileChooser.fileDateHeaderText", locale),
				//UIManager.getString("FileChooser.fileAttrHeaderText", locale)
		};

		// JDK 5 {
		List<File> list;

		List<File> filteredList;
	    // } JDK 5
		
		FileFilter currentFilter;

		public FileTableModel() {
			// JDK 5 {
			list = new ArrayList<File>();
			filteredList = new ArrayList<File>();
		    // } JDK 5
		}

		// JDK 5 {
		public void setFilter(FileFilter filter) {
			currentFilter = filter; 

			filteredList.clear();

			for (File file : list) {
				if (currentFilter == null || currentFilter.accept(file)) {
					filteredList.add(file);
				}
			}
			
			fireTableDataChanged();
		}
	    // } JDK 5
		
		public Object getValueAt(int row, int col) {
			// JDK 5 {
			/*
			File file = files.get(row);
			*/
			File file = filteredList.get(row);
			// } JDK 5
			switch (col) {
			case 0:
				return file.getName();
			case 1:
				String size = file.isDirectory() ? "" : Long.toString(file.length());

				return size;
			case 2:
				return getFileChooser().getFileSystemView().getSystemTypeDescription(file);
			case 3:
				return new Date(file.lastModified());
//			case 4:
//				if (getFileChooser().getFileSystemView().isFileSystemRoot(file))
//					return null;
//
//				String attributes = "";
//
//				if (!file.canWrite())
//					attributes += "R";
//				if (file.isHidden())
//					attributes += "H";
//				return attributes;
			default:
				return null;
			}
		}

		// JDK 6 {
		/*
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
		*/
		// } JDK 6
		
		public void setCurrentNode(DirectoryTreeNode currentNode) {
			list.clear();
			filteredList.clear();

			if (!currentNode.isRoot()) {
				addElement(new File(".."));
			}
			
			File[] files = currentNode.getFiles();
			if (files != null) {
				for (File file : files) {
					if (getFileChooser().isDirectorySelectionEnabled()) {
						if (file.isDirectory()) {
							addElement(file);
						}
					} else {
						addElement(file);
					}
				}
			}

			this.fireTableDataChanged();
		}

		public int getRowCount() {
			return filteredList.size();
		}
		
		public void addElement(File element) {
			list.add(element);
			if (currentFilter == null || currentFilter.accept(element)) {
				filteredList.add(element);
			}
		}

		public File getFileAt(int index) {
			return filteredList.get(index);
		}

		public void insertUpdate(DocumentEvent event) {
			Document doc = event.getDocument();
			updateFilter(doc);
		}

		public void removeUpdate(DocumentEvent event) {
			Document doc = event.getDocument();
			updateFilter(doc);
		}

		private void updateFilter(Document doc) {
			Object selectedItem = filterComboBox.getSelectedItem();
			if (selectedItem instanceof String || selectedItem instanceof WildcardsFileFilter) {
				try {
					String pattern = doc.getText(0, doc.getLength());
	
					wildcardsFileFilter.setPattern(pattern);
					
					setFilter(wildcardsFileFilter);
				} catch (BadLocationException ble) {
					System.err.println("Bad location: " + ble);
				}
			}
		}

		public void changedUpdate(DocumentEvent event) {
		}
		// }}
		
		public int getColumnCount() {
			return columnNames.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Class<?> getColumnClass(int col) {
			switch (col) {
			case 3:
				return Date.class;
			default:
				return String.class;
			}
		}
		
		public void selectIfPossible(File file) {
			for (int i = 0; i < filteredList.size(); i++) {
				File filteredFile = filteredList.get(sorter.convertRowIndexToModel(i));
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
				setIcon(null);
				setHorizontalAlignment(SwingConstants.RIGHT);
				break;
			case 3:
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
	
	/*
	 * The original code can be found at http://java.sun.com/developer/JDCTechTips/2005/tt1214.html
	 * The filtering strategy has been modified to support regex pattern.
	 */
	class FileListModel extends AbstractListModel implements DocumentListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6666398054236938755L;

		List<File> list;

		List<File> filteredList;
		
		FileFilter currentFilter;

		public FileListModel() {
			list = new ArrayList<File>();
			filteredList = new ArrayList<File>();
		}
		
		public void setFilter(FileFilter filter) {
			filteredList.clear();

			currentFilter = filter;

			for (File file : list) {
				if (currentFilter == null || currentFilter.accept(file)) {
					filteredList.add(file);
				}
			}

			fireContentsChanged(this, 0, getSize());
		}

		public void setCurrentNode(DirectoryTreeNode currentNode) {
			list.clear();
			filteredList.clear();

			if (!currentNode.isRoot()) {
				addElement(new File(".."));
			}
			
			File[] files = currentNode.getFiles();
			if (files != null) {
				for (File file : files) {
					if (getFileChooser().isDirectorySelectionEnabled()) {
						if (file.isDirectory()) {
							addElement(file);
						}
					} else {
						addElement(file);
					}
				}
			}
		}

		public void addElement(File element) {
			list.add(element);

			if (currentFilter != null) {
				if (currentFilter.accept(element)) {
					filteredList.add(element);
				}
			}
			
			fireContentsChanged(this, 0, filteredList.size() - 1);
		}

		public int getSize() {
			return filteredList.size();
		}

		public Object getElementAt(int index) {
			Object returnValue;
			if (index < filteredList.size()) {
				returnValue = filteredList.get(index);
			} else {
				returnValue = null;
			}
			return returnValue;
		}

		public void insertUpdate(DocumentEvent event) {
			Document doc = event.getDocument();
			updateFilter(doc);
		}

		public void removeUpdate(DocumentEvent event) {
			Document doc = event.getDocument();
			updateFilter(doc);
		}

		private void updateFilter(Document doc) {
			Object selectedItem = filterComboBox.getSelectedItem();
			if (selectedItem instanceof String || selectedItem instanceof WildcardsFileFilter) {
				try {
					String pattern = doc.getText(0, doc.getLength());
	
					wildcardsFileFilter.setPattern(pattern);
					
					setFilter(wildcardsFileFilter);
				} catch (BadLocationException ble) {
					System.err.println("Bad location: " + ble);
				}
			}
		}
		
		public void changedUpdate(DocumentEvent event) {
		}
		
		public void selectIfPossible(File file) {
			for (int i = 0; i < filteredList.size(); i++) {
				File filteredFile = filteredList.get(i);
				if (filteredFile.getAbsolutePath().equals(file.getAbsolutePath())) {
					getFileList().getSelectionModel().setSelectionInterval(i, i);
					break;
				}
			}
		}
	}

	class WildcardsFileFilter extends FileFilter {
		
		String pattern;
		Matcher matcher;
		
		public WildcardsFileFilter() {
			setPattern("*");
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
			
			Pattern regex = Pattern.compile(getRegexPattern(pattern));
			matcher = regex.matcher("");
		}
		
		@Override
		public boolean accept(File f) {
			matcher.reset(f.getName());
			return matcher.find();
		}
		
		@Override
		public String getDescription() {
			return "Wildcards Filter (" + pattern + ")";
		}
    	
    	@Override
    	public String toString() {
    		return pattern;
    	}
	}
	
	@SuppressWarnings("unchecked")
	class EditableComboBoxModel extends DefaultComboBoxModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4397009979125052224L;
		
		public void setList(List list) {
			removeAllElements();
			
			for (Object item : list) {
				addElement(item);
			}
		}
		
//		public Object getElementAt(int index) {
//			return list.get(index);
//		}
//		
//		public int getSize() {
//			return list.size();
//		}
		
		public void addIfNecessary(Object item) {
			if (getIndexOf(item) == -1) {
				addElement(item);
			}
			
//			if (item != null && !list.contains(item)) {
//				list.add(item);
//				
//				fireContentsChanged(this, 0, list.size() - 1);
//			}
		}

		public void remove(Object item) {
			removeElement(item);
//			list.remove(item);
//			
//			fireContentsChanged(this, 0, list.size() - 1);
		}
	}
	
	@SuppressWarnings("unchecked")
	static class NavigationHistoryComboBoxModel extends DefaultComboBoxModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 5897879783481281983L;
		
		public NavigationHistoryComboBoxModel() {
		}
		
		public void addIfNecessary(Object item) {
			if (item != null && getIndexOf(item) == -1) {
				addElement(item);
				
				fireContentsChanged(this, 0, this.getSize() - 1);
				
				setSelectedItem(item);
			}
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
							        JOptionPane.showMessageDialog(getFileChooser(), "Error", "Cannot move '" + path + "' file", JOptionPane.ERROR_MESSAGE);
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
	
	class DnDListHandler extends DragSourceAdapter implements DragGestureListener, DropTargetListener {
		private DragSource dragSource;

		private JList list;

		public DnDListHandler(JList list) {
			this.list = list;
			dragSource = new DragSource();
			dragSource.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_MOVE, this);
			
			new DropTarget(list, this);
		}

		public void dragGestureRecognized(DragGestureEvent dge) {
			StringBuilder sb = new StringBuilder();
			int[] indices = list.getSelectedIndices();
			if( indices.length > 0 ) {
				for (int index : indices) {
					File file = getFileTableModel().getFileAt(index);
					sb.append(file.getAbsolutePath());
					sb.append(File.pathSeparatorChar);
				}
				
				StringSelection ss = new StringSelection(sb.toString());
			
				this.dragSource.startDrag(dge, DragSource.DefaultCopyDrop, ss, this);
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

						int index = A03SwingUtilities.loc2IndexFileList(list, mousePoint);
						File destFile = (File) getFileListModel().getElementAt(index);
					
						if (destFile.isDirectory()) {
							for (String path : paths) {
								// File (or directory) to be moved
							    File file = getFileChooser().getFileSystemView().createFileObject(path);
							    
							    String newPath = destFile.getAbsolutePath() + File.separatorChar + file.getName();
							    
							    // Move file to new directory
							    boolean success = file.renameTo(new File(newPath));
							    if (!success) {
							        JOptionPane.showMessageDialog(getFileChooser(), "Error", "Cannot move '" + path + "' file", JOptionPane.ERROR_MESSAGE);
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
						        JOptionPane.showMessageDialog(getFileChooser(), "Error", "Cannot move '" + path + "' file", JOptionPane.ERROR_MESSAGE);
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
	
	public A03AdvancedFileChooserUI(JFileChooser fc) {
		super(fc);

		locale = fc.getLocale();
	}

	public static ComponentUI createUI(JComponent c) {
		A03SwingUtilities.performEdtViolationCheck(c);
		
		return new A03AdvancedFileChooserUI((JFileChooser) c);
	}

	private A03FileChooserUIDelegate uiDelegate;
	
	@Override
	public void installUI(JComponent c) {
		this.uiDelegate = A03SwingUtilities.getUIDelegate(c, A03FileChooserUIDelegate.class);

		super.installUI(c);
	
		installComponents(getFileChooser());
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
	
		uninstallComponents((JFileChooser) c);
	}
	
	private static String getRegexPattern(String text) {
		if (!text.startsWith("*")) {
			if (!text.startsWith("?")) {
				text = "^" + text;
			}
		}

		text = text.replace("?", "(\\w)");
		text = text.replace("*", "(\\w)*");
		text = text.replace(".", "\\.");

		if (!text.endsWith("*")) {
			text = text.concat("$");
		}
		
		return "\\.\\.|" + text; // .. is the parent, always visible (| is regexp OR condition)
	}

	private JPanel buildNorthPanel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));

		northPanel.setBorder(new EmptyBorder(0, 0, 3, 0));
		
		northPanel.add(new JLabel(UIManager.getString("FileChooser.navigationHistoryText")));
		
		northPanel.add(Box.createHorizontalStrut(GAP));

		northPanel.add(getNavigationHistoryComboBox());
		
		return northPanel;
	}

	//@SuppressWarnings("all")
	private JPanel buildSouthPanel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));

		southPanel.setBorder(new EmptyBorder(3, 0, 0, 0));
		
		String str = UIManager.getString("FileChooser.fileNameLabelText");
		
		choiceLabel = new JLabel(str);
		
		FontMetrics fm = choiceLabel.getFontMetrics(choiceLabel.getFont());
		choiceLabel.setPreferredSize(new Dimension(fm.stringWidth(str), 20));
		southPanel.add(choiceLabel);
		
		southPanel.add(Box.createHorizontalStrut(GAP));

		historyComboBoxModel = new EditableComboBoxModel();
		historyComboBoxModel.setList(fileHistory);
		
		fileTextField = new JTextField(32);
		fileTextField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					approveButton.doClick();
				}
			}
		});

		southPanel.add(fileTextField);		
		southPanel.add(Box.createHorizontalStrut(2 * GAP));

		str = UIManager.getString("FileChooser.filterText");
		
		filterLabel = new JLabel(str);
		fm = filterLabel.getFontMetrics(filterLabel.getFont());
		filterLabel.setPreferredSize(new Dimension(fm.stringWidth(str), 20));
		southPanel.add(filterLabel);		
		southPanel.add(Box.createHorizontalStrut(GAP));

		filterComboBoxModel = new EditableComboBoxModel();
		
		// XXX returned by getAcceptAllFileFilter(JFileChooser) by default
		wildcardsFileFilter = new WildcardsFileFilter();

		filterComboBox = new JComboBox(filterComboBoxModel);
		filterComboBox.setRenderer(new FilterCellRenderer());
	
		filterComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					FileFilter filter;
					Object selectedItem = filterComboBox.getSelectedItem();
					if (selectedItem instanceof String) {
						wildcardsFileFilter.setPattern((String) selectedItem);
						filter = wildcardsFileFilter;
					} else {
						filter = (FileFilter) selectedItem;
					}

					filterComboBox.setEditable(filter instanceof WildcardsFileFilter);
					
					applyFilter(filter);
				}
			}
		});

		JTextField filterComboBoxEditor = (JTextField) filterComboBox.getEditor().getEditorComponent();
		filterComboBoxEditor.getDocument().addDocumentListener(getFileListModel());
		/* OR
		filterComboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			        String newSelection = (String) filterComboBox.getSelectedItem();

			        applyFilter(filter);
				}
			}
		});
		 */
		// JDK 5 (only ?) {
		filterComboBoxEditor.getDocument().addDocumentListener(getFileTableModel());
		// }
		
		//applyFilter((FileFilter) filterComboBox.getSelectedItem());
		
		southPanel.add(filterComboBox);
		southPanel.add(Box.createHorizontalStrut(2 * GAP));
		
		approveButton = new JButton(getApproveButtonText(getFileChooser()));
		approveButton.addActionListener(getApproveSelectionAction());
		approveButton.setToolTipText(getApproveButtonToolTipText(getFileChooser()));
		approveButton.setEnabled(false);
		southPanel.add(approveButton);
		southPanel.add(Box.createHorizontalStrut(2 * GAP));

		cancelButton = new JButton(cancelButtonText);
		cancelButton.setToolTipText(cancelButtonToolTipText);
		cancelButton.addActionListener(getCancelSelectionAction());
		southPanel.add(cancelButton);

        return southPanel;		
	}

	private void applyFilter(FileFilter filter) {
        getFileListModel().setFilter(filter);
        
        // JDK 5 {
        /*
   		if (filter.length() == 0) {
			sorter.setRowFilter(null);
		} else {
			try {
				sorter.setRowFilter(...);
			} catch (PatternSyntaxException x) {
			}
		}		
        */
        getFileTableModel().setFilter(filter);
        // } JDK 5
		
		updateDetails();		
	}

	private void buildDetailsPopupMenu() {
		filePopupMenu = new JPopupMenu();
		filePopupMenu.setOpaque(false);
		filePopupMenu.setDoubleBuffered(true);
		filePopupMenu.setFocusable(false);

		JMenu viewMenu = new JMenu("View");

		JMenuItem viewDetailsMenuItem = new JMenuItem(UIManager.getString("FileChooser.fileDetailsText"));
		viewDetailsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFileScrollPane().setViewportView(getFileTable());
			}
		});
		viewMenu.add(viewDetailsMenuItem);
		
		JMenuItem viewListMenuItem = new JMenuItem(UIManager.getString("FileChooser.fileListText"));
		viewListMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFileScrollPane().setViewportView(getFileList());
			}
		});
		viewMenu.add(viewListMenuItem);
		filePopupMenu.add(viewMenu);
		
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
			if (source == getFileList()) {
				for (File file : files) {
					delete(file);
					
					if (file.isDirectory()) {
						DirectoryTreeNode node = getDirectoryTreeNode(file);
						node.removeFromParent();
					}
				}
	
				rescanCurrentDirectory(getFileChooser());
			} else if (source == getFileTable()) {
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
		
		splitPane.setLeftComponent(new JScrollPane(getDirectoryTree())) ; // leftTabbedPane);
		
		splitPane.setDividerLocation(300);
		
		splitPane.setRightComponent(getFileScrollPane());
		
		splitPane.setPreferredSize(new Dimension(800, 300));

		fc.add(splitPane, BorderLayout.CENTER);
		
		accessoryContainer = new JPanel();
		accessoryContainer.setLayout(new BorderLayout());
		fc.add(accessoryContainer, BorderLayout.EAST);

		JPanel southPanel = buildSouthPanel();
		fc.add(southPanel, BorderLayout.SOUTH);
		
		JPanel northPanel = buildNorthPanel();
		fc.add(northPanel, BorderLayout.NORTH);

		buildDetailsPopupMenu();
	}

	@Override
	public void uninstallComponents(JFileChooser fc) {
		fc.removeAll();
	}

	private Icon getSystemIcon(File f) {
		return getFileChooser().getFileSystemView().getSystemIcon(f);
	}

	@Override
	public void ensureFileIsVisible(JFileChooser fc, File f) {
		getFileList().ensureIndexIsVisible(getFileList().getSelectedIndex());

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
	
	public FileListModel getFileListModel() {
		if (fileListModel == null) {
			fileListModel = new FileListModel();
		}

		return fileListModel;
	}

	private JList getFileList() {
		if (fileList == null) {
			fileList = new JList(getFileListModel());
			fileList.setCellRenderer(new FileCellRenderer());
			fileList.setLayoutOrientation(JList.VERTICAL_WRAP);
			fileList.setVisibleRowCount(-1);
			fileList.setFixedCellHeight(19);
			fileList.getSelectionModel().addListSelectionListener(getListSelectionListener());
			fileList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					processMouseEvent(fileList, e);
				}
			});
			
			new DnDListHandler(fileList);
		}
	
		return fileList;
	}

	public FileTableModel getFileTableModel() {
		if (fileTableModel == null) {
			fileTableModel = new FileTableModel();
		}

		return fileTableModel;
	}
	
	private static NavigationHistoryComboBoxModel getNavigationHistoryComboBoxModel() {
		if (navigationHistoryListModel == null) {
			navigationHistoryListModel = new NavigationHistoryComboBoxModel();
		} 
		
		return navigationHistoryListModel;
	}
	
	private JComboBox getNavigationHistoryComboBox() {
		if (navigationHistoryList == null) {
			navigationHistoryList = new JComboBox(getNavigationHistoryComboBoxModel());
			navigationHistoryList.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						int selectedIndex = navigationHistoryList.getSelectedIndex();
						if (selectedIndex != -1) {
							File dir = (File) getNavigationHistoryComboBoxModel().getElementAt(selectedIndex);
							
							getFileChooser().setCurrentDirectory(dir);
						}
					}
				}
			});
		}
	
		return navigationHistoryList;
	}
	
    public JTable getFileTable() {
		if (fileTable == null) {
			fileTable = new JTable(getFileTableModel());

			new DnDTableHandler(fileTable);
			
			sorter = new TableRowSorter<FileTableModel>(getFileTableModel());
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

			fileTable.getTableHeader().setReorderingAllowed(false);

			TableColumnModel tcm = fileTable.getColumnModel();
			TableColumn tc = tcm.getColumn(0);
			tc.setCellRenderer(renderer);
			tc.setPreferredWidth(240);

			tc = tcm.getColumn(1);
			tc.setCellRenderer(renderer);
			tc.setPreferredWidth(120);

			tc = tcm.getColumn(2);
			tc.setPreferredWidth(140);

			tc = tcm.getColumn(3);
			tc.setCellRenderer(renderer);
			tc.setPreferredWidth(120);
		}

		return fileTable;
	}

    private File[] getSelectedFiles(Object source) {
		File[] files = null;
    	if (source == getFileList()) {
			int[] indices = getFileList().getSelectedIndices();
    		files = new File[indices.length];
    		
			for (int i = 0; i < indices.length; i++) {
				files[i] = (File) getFileListModel().getElementAt(indices[i]);
			}
    	} else if (source == getFileTable()) {
			int[] rows = getFileTable().getSelectedRows();
    		files = new File[rows.length];
			for (int i = 0; i < rows.length; i++) {
				files[i] = getFileTableModel().getFileAt(sorter.convertRowIndexToModel(rows[i]));
			}
    	} else if (source == getDirectoryTree()) {
    		files = new File[] { currentNode.getFile() };
    	}
    	
    	return files;
    }
    
    private void processMouseEvent(Component target, MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (target == getFileList()) {
				int[] indices = getFileList().getSelectedIndices();
				
				boolean enabled;
				if (indices.length == 1 && indices[0] == getFileList().locationToIndex(e.getPoint())) {
					File file = (File) getFileList().getSelectedValue();
					enabled = isFileModifiable(file);
				} else {
					enabled = false;
				}
				
				deleteItem.setEnabled(enabled);
				renameItem.setEnabled(enabled);
				filePopupMenu.show(target, e.getX(), e.getY());
			} else if (target == getFileTable()) {
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
    
	private JScrollPane getFileScrollPane() {
		if (fileScrollPane == null) {
			fileScrollPane = new JScrollPane(getFileTable());
			fileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}

		return fileScrollPane;
	}

	private void updateDetails() {
		getFileListModel().setCurrentNode(currentNode);

		getFileTableModel().setCurrentNode(currentNode);
	}
	
	@Override
	public void setFileName(String filename) {
		fileTextField.setText(filename);
	}
	
	@Override
	public String getFileName() {
		return fileTextField.getText();
	}
	
	@Override
	public FileFilter getAcceptAllFileFilter(JFileChooser fc) {
		return UIManager.getBoolean("FileChooser.useWildcardsFileFilter") ? wildcardsFileFilter : super.getAcceptAllFileFilter(fc);
	}
}
