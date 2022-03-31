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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.DefaultEditorKit;

import a03.swing.plugin.A03UIPlugin;

public class A03SwingUIPlugin extends A03UIPlugin {
	
	private final static String A03_PREFIX = "a03.swing.plaf.";

	public void installDefaults(UIDefaults table) {
		A03SystemColorUIDelegate systemColorUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03SystemColorUIDelegate.class);
		
		Object listCellRendererActiveValue = new UIDefaults.ActiveValue() {
		    public Object createValue(UIDefaults table) {
		    	return new A03ListCellRenderer.UIResource();
		    }
		};
		
		Object[] defaultCueList = new Object[] {
				"OptionPane.errorSound",
				"OptionPane.informationSound",
				"OptionPane.questionSound",
				"OptionPane.warningSound",
				};

		A03TitledBorderUIDelegate titledBorderUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03TitledBorderUIDelegate.class);
		
		A03OptionPaneUIDelegate optionPaneUIDelegate =  A03UIDelegateFactories.getInstance().getUIDelegate(null, A03OptionPaneUIDelegate.class);
		
    	Object fieldInputMap = new UIDefaults.LazyInputMap(new Object[] {
				"ctrl C", DefaultEditorKit.copyAction,
				"ctrl V", DefaultEditorKit.pasteAction,
				"ctrl X", DefaultEditorKit.cutAction,
				"COPY", DefaultEditorKit.copyAction,
				"PASTE", DefaultEditorKit.pasteAction,
				"CUT", DefaultEditorKit.cutAction,
				"control INSERT", DefaultEditorKit.copyAction,
				"shift INSERT",	DefaultEditorKit.pasteAction,
				"shift DELETE",	DefaultEditorKit.cutAction,
				"shift LEFT", DefaultEditorKit.selectionBackwardAction,
				"shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
				"shift RIGHT", DefaultEditorKit.selectionForwardAction, 
				"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, 
				"ctrl LEFT", DefaultEditorKit.previousWordAction, 
				"ctrl KP_LEFT", DefaultEditorKit.previousWordAction, 
				"ctrl RIGHT", DefaultEditorKit.nextWordAction, 
				"ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, 
				"ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, 
				"ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, 
				"ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, 
				"ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, 
				"ctrl A", DefaultEditorKit.selectAllAction, 
				"HOME",	DefaultEditorKit.beginLineAction, 
				"END", DefaultEditorKit.endLineAction, 
				"shift HOME", DefaultEditorKit.selectionBeginLineAction, 
				"shift END", DefaultEditorKit.selectionEndLineAction, 
				"BACK_SPACE", DefaultEditorKit.deletePrevCharAction, 
				"shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, 
				"ctrl H", DefaultEditorKit.deletePrevCharAction, 
				"DELETE", DefaultEditorKit.deleteNextCharAction, 
				"RIGHT", DefaultEditorKit.forwardAction, 
				"LEFT", DefaultEditorKit.backwardAction, 
				"KP_RIGHT", DefaultEditorKit.forwardAction, 
				"KP_LEFT", DefaultEditorKit.backwardAction, 
				"ENTER", JTextField.notifyAction, 
				"ctrl BACK_SLASH", "unselect", 
				"control shift O", "toggle-componentOrientation"
		});

		Object passwordInputMap = new UIDefaults.LazyInputMap(new Object[] {
				"ctrl C", DefaultEditorKit.copyAction,
				"ctrl V", DefaultEditorKit.pasteAction,
				"ctrl X", DefaultEditorKit.cutAction,
				"COPY", DefaultEditorKit.copyAction,
				"PASTE", DefaultEditorKit.pasteAction,
				"CUT", DefaultEditorKit.cutAction,
				"control INSERT", DefaultEditorKit.copyAction,
				"shift INSERT",	DefaultEditorKit.pasteAction,
				"shift DELETE",	DefaultEditorKit.cutAction,
				"shift LEFT", DefaultEditorKit.selectionBackwardAction,
				"shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
				"shift RIGHT", DefaultEditorKit.selectionForwardAction, 
				"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, 
				"ctrl LEFT", DefaultEditorKit.previousWordAction, 
				"ctrl KP_LEFT", DefaultEditorKit.previousWordAction, 
				"ctrl RIGHT", DefaultEditorKit.nextWordAction, 
				"ctrl KP_RIGHT", DefaultEditorKit.nextWordAction, 
				"ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, 
				"ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, 
				"ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, 
				"ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, 
				"ctrl A", DefaultEditorKit.selectAllAction, 
				"HOME",	DefaultEditorKit.beginLineAction, 
				"END", DefaultEditorKit.endLineAction, 
				"shift HOME", DefaultEditorKit.selectionBeginLineAction, 
				"shift END", DefaultEditorKit.selectionEndLineAction, 
				"BACK_SPACE", DefaultEditorKit.deletePrevCharAction, 
				"shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, 
				"ctrl H", DefaultEditorKit.deletePrevCharAction, 
				"DELETE", DefaultEditorKit.deleteNextCharAction, 
				"RIGHT", DefaultEditorKit.forwardAction, 
				"LEFT", DefaultEditorKit.backwardAction, 
				"KP_RIGHT", DefaultEditorKit.forwardAction, 
				"KP_LEFT", DefaultEditorKit.backwardAction, 
				"ENTER", JTextField.notifyAction, 
				"ctrl BACK_SLASH", "unselect", 
				"control shift O", "toggle-componentOrientation"
		});

		Object multilineInputMap = new UIDefaults.LazyInputMap(new Object[] {
				"ctrl C", DefaultEditorKit.copyAction,
				"ctrl V", DefaultEditorKit.pasteAction,
				"ctrl X", DefaultEditorKit.cutAction,
				"COPY", DefaultEditorKit.copyAction,
				"PASTE", DefaultEditorKit.pasteAction,
				"CUT", DefaultEditorKit.cutAction,
				"control INSERT", DefaultEditorKit.copyAction,
				"shift INSERT",	DefaultEditorKit.pasteAction,
				"shift DELETE",	DefaultEditorKit.cutAction,
				"shift LEFT", DefaultEditorKit.selectionBackwardAction,
				"shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
				"shift RIGHT", DefaultEditorKit.selectionForwardAction, 
				"shift KP_RIGHT", DefaultEditorKit.selectionForwardAction, 
				"ctrl LEFT", DefaultEditorKit.previousWordAction, 
				"ctrl KP_LEFT", DefaultEditorKit.previousWordAction, 
				"ctrl RIGHT", DefaultEditorKit.nextWordAction, 
				"ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
				"ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction, 
				"ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction, 
				"ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction, 
				"ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction, 
				"ctrl A", DefaultEditorKit.selectAllAction, 
				"HOME",	DefaultEditorKit.beginLineAction, 
				"END", DefaultEditorKit.endLineAction, 
				"shift HOME", DefaultEditorKit.selectionBeginLineAction, 
				"shift END", DefaultEditorKit.selectionEndLineAction,
				"UP", DefaultEditorKit.upAction,
				"KP_UP", DefaultEditorKit.upAction,
				"DOWN", DefaultEditorKit.downAction,
				"KP_DOWN", DefaultEditorKit.downAction,
				"PAGE_UP", DefaultEditorKit.pageUpAction,
				"PAGE_DOWN", DefaultEditorKit.pageDownAction,
				"shift PAGE_UP", "selection-page-up",
				"shift PAGE_DOWN", "selection-page-down",
				"ctrl shift PAGE_UP", "selection-page-left",
				"ctrl shift PAGE_DOWN", "selection-page-right",
				"shift UP",	DefaultEditorKit.selectionUpAction,
				"shift KP_UP", DefaultEditorKit.selectionUpAction,
				"shift DOWN", DefaultEditorKit.selectionDownAction,
				"shift KP_DOWN", DefaultEditorKit.selectionDownAction,
				"ENTER", DefaultEditorKit.insertBreakAction,
				"BACK_SPACE", DefaultEditorKit.deletePrevCharAction, 
				"shift BACK_SPACE", DefaultEditorKit.deletePrevCharAction, 
				"ctrl H", DefaultEditorKit.deletePrevCharAction, 
				"DELETE", DefaultEditorKit.deleteNextCharAction, 
				"RIGHT", DefaultEditorKit.forwardAction, 
				"LEFT", DefaultEditorKit.backwardAction, 
				"KP_RIGHT", DefaultEditorKit.forwardAction, 
				"KP_LEFT", DefaultEditorKit.backwardAction, 
				"TAB", DefaultEditorKit.insertTabAction,
				"ctrl BACK_SLASH", "unselect", 
				"ctrl HOME", DefaultEditorKit.beginAction, 
				"ctrl END",	DefaultEditorKit.endAction, 
				"ctrl shift HOME", DefaultEditorKit.selectionBeginAction, 
				"ctrl shift END", DefaultEditorKit.selectionEndAction,
				"ctrl T", "next-link-action", 
				"ctrl shift T", "previous-link-action",
				"ctrl SPACE", "activate-link-action",
				"control shift O", "toggle-componentOrientation"
		});
        		
		A03ViewportUIDelegate viewportUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03ViewportUIDelegate.class);
		
		A03TableHeaderUIDelegate tableHeaderUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03TableHeaderUIDelegate.class);
		
		A03TableUIDelegate tableUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03TableUIDelegate.class);
		
		A03ListUIDelegate listUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03ListUIDelegate.class);
		
		A03MenuItemUIDelegate menuItemUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03MenuItemUIDelegate.class);

		A03MenuUIDelegate menuUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03MenuUIDelegate.class);
		
		A03CheckBoxMenuItemUIDelegate checkBoxMenuItemUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03CheckBoxMenuItemUIDelegate.class);

		A03InternalFrameUIDelegate internalFrameUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03InternalFrameUIDelegate.class);
		
		A03RadioButtonMenuItemUIDelegate radioButtonMenuItemUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03RadioButtonMenuItemUIDelegate.class);

		A03TreeUIDelegate treeUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03TreeUIDelegate.class);
		
		A03SplitPaneUIDelegate splitPaneUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03SplitPaneUIDelegate.class);

		A03ButtonUIDelegate buttonUIDelegate = A03UIDelegateFactories.getInstance().getUIDelegate(null, A03ButtonUIDelegate.class);

		Object[] keyValueList = {
				"ButtonUI", A03_PREFIX + "A03ButtonUI", 
				"ColorChooserUI", A03_PREFIX + "A03ColorChooserUI", 
				"CheckBoxUI", A03_PREFIX + "A03CheckBoxUI", 
				"CheckBoxMenuItemUI", A03_PREFIX + "A03CheckBoxMenuItemUI", 
				"ComboBoxUI", A03_PREFIX + "A03ComboBoxUI", 
			    "DesktopIconUI", A03_PREFIX + "A03DesktopIconUI",
			    "DesktopPaneUI", A03_PREFIX + "A03DesktopPaneUI",
			    "EditorPaneUI", A03_PREFIX + "A03EditorPaneUI",
			    "FileChooserUI", A03_PREFIX + "A03AdvancedFileChooserUI",
			    "FormattedTextFieldUI", A03_PREFIX + "A03FormattedTextFieldUI",
			    "InternalFrameUI", A03_PREFIX + "A03InternalFrameUI",
			    "LabelUI", A03_PREFIX + "A03LabelUI",
			    "ListUI", A03_PREFIX + "A03ListUI",
			    "MenuUI", A03_PREFIX + "A03MenuUI",
			    "MenuBarUI", A03_PREFIX + "A03MenuBarUI",
			    "MenuItemUI", A03_PREFIX + "A03MenuItemUI",
			    "OptionPaneUI", A03_PREFIX + "A03OptionPaneUI",
			    "PanelUI", A03_PREFIX + "A03PanelUI",
			    "PasswordFieldUI", A03_PREFIX + "A03PasswordFieldUI",
			    "PopupMenuUI", A03_PREFIX + "A03PopupMenuUI",
			    "PopupMenuSeparatorUI", A03_PREFIX + "A03PopupMenuSeparatorUI",
				"ProgressBarUI", A03_PREFIX + "A03ProgressBarUI", 
				"RadioButtonUI", A03_PREFIX + "A03RadioButtonUI", 
			    "RadioButtonMenuItemUI", A03_PREFIX + "A03RadioButtonMenuItemUI",
			    "RootPaneUI", A03_PREFIX + "A03RootPaneUI",
				"ScrollPaneUI", A03_PREFIX + "A03ScrollPaneUI",
				"ScrollBarUI", A03_PREFIX + "A03ScrollBarUI",
				"SeparatorUI", A03_PREFIX + "A03SeparatorUI",
				"SliderUI", A03_PREFIX + "A03SliderUI",
				"SpinnerUI", A03_PREFIX + "A03SpinnerUI",
				"SplitPaneUI", A03_PREFIX + "A03SplitPaneUI",
				"TabbedPaneUI", A03_PREFIX + "A03TabbedPaneUI",
				"TableUI", A03_PREFIX + "A03TableUI",
				"TableHeaderUI", A03_PREFIX + "A03TableHeaderUI",
				"TextFieldUI", A03_PREFIX + "A03TextFieldUI",
				"TextAreaUI", A03_PREFIX + "A03TextAreaUI",
				"TextPaneUI", A03_PREFIX + "A03TextPaneUI",
			    "ToggleButtonUI", A03_PREFIX + "A03ToggleButtonUI",
			    "ToolBarUI", A03_PREFIX + "A03ToolBarUI",
			    "ToolBarSeparatorUI", A03_PREFIX + "A03ToolBarSeparatorUI",
			    "ToolTipUI", A03_PREFIX + "A03ToolTipUI",
			    "TreeUI", A03_PREFIX + "A03TreeUI",
			    "ViewportUI", A03_PREFIX + "A03ViewportUI",

			    "window", systemColorUIDelegate.getWindow(),
	               "desktop", systemColorUIDelegate.getDesktop(), /* Color of the desktop background */
	              "textText", systemColorUIDelegate.getTextText(), /* Text foreground color */
	         "textHighlight", systemColorUIDelegate.getTextHighlight(), /* Text background color when selected */
	     "textHighlightText", systemColorUIDelegate.getTextHighlightText(), /* Text color when selected */
	      "textInactiveText", systemColorUIDelegate.getTextInactiveText(), /* Text color when disabled */
	               "control", systemColorUIDelegate.getControl(), /* Default color for controls (buttons, sliders, etc) */
	           "controlText", systemColorUIDelegate.getControlText(), /* Default color for text in controls */
	         "controlShadow", systemColorUIDelegate.getControlShadow(), /* Shadow color for controls */
	       "controlDkShadow", systemColorUIDelegate.getControlDkShadow(), /* Dark shadow color for controls */
	                  "info", systemColorUIDelegate.getInfo(), /* ToolTip Background */
	              "infoText", systemColorUIDelegate.getInfoText(),  /* ToolTip Text */
	                  "menu", systemColorUIDelegate.getMenu(),
	              "menuText", systemColorUIDelegate.getMenuText(),
	              
				"Button.margin", buttonUIDelegate.getMargin(),
				"Button.border", A03BorderFactory.createButtonBorder(),
				"Button.font", buttonUIDelegate.getFont(),

	    		"CheckBox.font", null,
	    		"RadioButton.font", null,
	    		
	    		"ComboBox.font", null,
				"ComboBox.padding", null, // XXX

	            "FileChooser.newFolderIcon", null,
	    		"FileChooser.upFolderIcon", null,
	    		"FileChooser.homeFolderIcon", null,
	    		"FileChooser.newFolderIconDisabled", null,
	    		"FileChooser.upFolderIconDisabled", null,
	    		"FileChooser.detailsViewIcon", null,
	    		"FileChooser.listViewIcon", null,
	    		"FileChooser.folderIcon", null,
	    		"FileChooser.fileAttrHeaderText", "Attr",
	            "FileChooser.fileNameLabelText", "File name", 
	    		"FileChooser.filesOfTypeLabelText", "Type of", 
	    		"FileChooser.fileNameHeaderText", "File name", 
	    		"FileChooser.fileSizeHeaderText", "Size", 
	    		"FileChooser.fileTypeHeaderText", "Type", 
	    		"FileChooser.fileDateHeaderText", "Date", 
	    		//"FileChooser.fileAttrHeaderText", "Attributes", 
	    		"FileChooser.lookInLabelText", "Look in", 
	    		"FileChooser.saveInLabelText", "Save in",
	    		"FileChooser.saveButtonText", "Save",
	    		"FileChooser.openButtonText", "Open",
	    		"FileChooser.directoryFileTextColor", new ColorUIResource(Color.BLUE), 
	    		"FileChooser.readOnlyFileTextColor", new ColorUIResource(Color.RED), 
	    		"FileChooser.hiddenFileTextColor", new ColorUIResource(Color.LIGHT_GRAY), 
	    		/*
			     * Advanced
			     */
				"FileChooser.useWildcardsFileFilter", Boolean.TRUE,
				"FileChooser.navigationHistoryText", "Navigation History",
				"FileChooser.fileDetailsText", "File Details",
				"FileChooser.fileListText", "File List",
				"FileChooser.filterText", "Filter",
	            
			    "InternalFrame.border", A03BorderFactory.createDelegatedBorder(internalFrameUIDelegate),
	            "InternalFrame.maximizeIcon", A03IconFactory.createMaximizeIcon(internalFrameUIDelegate),
	            "InternalFrame.minimizeIcon", A03IconFactory.createMinimizeIcon(internalFrameUIDelegate),
	            "InternalFrame.iconifyIcon", A03IconFactory.createIconifyIcon(internalFrameUIDelegate),
	            "InternalFrame.closeIcon", A03IconFactory.createCloseIcon(internalFrameUIDelegate),
	            "InternalFrame.titleFont", internalFrameUIDelegate.getTitleFont(),

	            "Label.font", null,
				
				"List.cellRenderer", listCellRendererActiveValue,
			    "List.focusCellHighlightBorder", A03BorderFactory.createListFocusCellHighlightBorder(listUIDelegate),
			    "List.cellNoFocusBorder", A03BorderFactory.createListNoFocusBorder(listUIDelegate),
				
			    "AuditoryCues.defaultCueList", defaultCueList,
			    "AuditoryCues.playList", defaultCueList,
			    
			    "Menu.font", menuUIDelegate.getFont(),
			    "Menu.checkIcon", A03IconFactory.getCheckBoxMenuItemIcon(checkBoxMenuItemUIDelegate),
			    "Menu.acceleratorFont", menuUIDelegate.getAcceleratorFont(),
			    "Menu.arrowIcon", A03IconFactory.createArrowIcon(menuUIDelegate),

			    "MenuItem.font", menuItemUIDelegate.getFont(),
			    "MenuItem.checkIcon", A03IconFactory.getCheckBoxMenuItemIcon(checkBoxMenuItemUIDelegate),
			    "MenuItem.acceleratorFont", menuItemUIDelegate.getAcceleratorFont(),
			    
			    "CheckBoxMenuItem.checkIcon", A03IconFactory.getCheckBoxMenuItemIcon(checkBoxMenuItemUIDelegate),
	    		"CheckBoxMenuItem.font", checkBoxMenuItemUIDelegate.getFont(),
	    	    "CheckBoxMenuItem.acceleratorFont", checkBoxMenuItemUIDelegate.getAcceleratorFont(),

			    "RadioButtonMenuItem.checkIcon", A03IconFactory.createRadioButtonMenuItemIcon(radioButtonMenuItemUIDelegate),
	    	    "RadioButtonMenuItem.font", radioButtonMenuItemUIDelegate.getFont(),
	    	    "RadioButtonMenuItem.acceleratorFont", radioButtonMenuItemUIDelegate.getAcceleratorFont(),
			    
			    "RootPane.border", A03BorderFactory.createRootPaneBorder(),
//			    "RootPane.titleFont", rootPaneUIDelegate.getTitleFont(),
	    	    
			    "OptionPane.messageAreaBorder", null,
			    "OptionPane.buttonAreaBorder", null,
		        "OptionPane.informationSound", optionPaneUIDelegate.getInformationSound(),
		        "OptionPane.warningSound", optionPaneUIDelegate.getWarningSound(),
		        "OptionPane.errorSound", optionPaneUIDelegate.getErrorSound(),
		        "OptionPane.questionSound", optionPaneUIDelegate.getQuestionSound(),
		        "OptionPane.messageFont", optionPaneUIDelegate.getMessageFont(),
			    
			    "ScrollBar.squareButtons", Boolean.FALSE,
			    
			    "Spinner.arrowButtonInsets", null,
			    "Spinner.arrowButtonSize", new Dimension(16, 5),
			    
			    "SplitPane.dividerSize", splitPaneUIDelegate.getDividerSize(),
			    
			    "TabbedPane.tabsOverlapBorder", Boolean.FALSE,
	            "TabbedPane.tabRunOverlay", new Integer(2),
	            "TabbedPane.tabsOpaque", Boolean.TRUE,
	            "TabbedPane.contentOpaque", Boolean.TRUE,
	            
	            "Table.focusCellHighlightBorder", null,
				"Table.cellRendererDateFormat", "dd-MMM-yyyy",
	            "Table.scrollPaneBorder", A03BorderFactory.createScrollPaneTableBorder(tableUIDelegate),
	            "Table.focusSelectedCellHighlightBorder", A03BorderFactory.createTableFocusSelectedCellHighlightBorder(tableUIDelegate),
				
				"TableHeader.cellBorder", A03BorderFactory.createTableHeaderCellBorder(tableHeaderUIDelegate),
	    	    	            
				"TextComponent.border", A03BorderFactory.createTextComponentBorder(null),
//	    	    "TextComponent.disabledBackground", textComponentUIDelegate.getDisabledBackground(),
//	    	    "TextComponent.inactiveForeground", textComponentUIDelegate.getInactiveForeground(),
//	    	    "TextComponent.inactiveBackground", textComponentUIDelegate.getInactiveBackground(),
//	    	    "TextComponent.background", textComponentUIDelegate.getBackground(),
//	    	    "TextComponent.selectionBackground", textComponentUIDelegate.getSelectionBackground(),
//	    	    "TextComponent.foreground", textComponentUIDelegate.getForeground(),
//	    	    "TextComponent.selectionForeground", textComponentUIDelegate.getSelectionForeground(),
	    	    
				"TextField.border", A03BorderFactory.createTextComponentBorder(null),
		         

				
	    	    "EditorPane.focusInputMap", multilineInputMap,
	    	    
	       		"FormattedTextField.focusInputMap", fieldInputMap,
//	    	    "FormattedTextField.disabledForeground", null,
	    		"FormattedTextField.border", A03BorderFactory.createTextComponentBorder(null),
//	    		"FormattedTextField.margin", null,
//	    	    "FormattedTextField.font", null,
//	    	    "FormattedTextField.disabledBackground", null,
//	    	    "FormattedTextField.inactiveForeground", null,
//	    	    "FormattedTextField.inactiveBackground", null,
//	    	    "FormattedTextField.background", null,
//	    	    "FormattedTextField.selectionBackground", null,
//	    	    "FormattedTextField.foreground", null,
//	    	    "FormattedTextField.selectionForeground", null,

		
				"PasswordField.focusInputMap", passwordInputMap,
				"PasswordField.echoChar", 'A', // (char) 0x2022,
				
	    		"TextArea.focusInputMap", multilineInputMap,
	    	    
				"TextField.focusInputMap", fieldInputMap,

				"TextPane.focusInputMap", multilineInputMap,
				
				"TitledBorder.border", A03BorderFactory.createDelegatedBorder(titledBorderUIDelegate),
	            "TitledBorder.font", titledBorderUIDelegate.getFont(),
	            "TitledBorder.titleColor", titledBorderUIDelegate.getTitleColor(),
	            
				"ToggleButton.margin", buttonUIDelegate.getMargin(),
				"ToggleButton.border", A03BorderFactory.createButtonBorder(),
				"ToggleButton.font", buttonUIDelegate.getFont(),
	    		
	            // XXX to force to use A03ToolBarSeparatorUI.getPreferredSize
	    	    "ToolBar.separatorSize", null,
	        	
//			    "Tree.foreground", treeDelegate.getForeground(),
			    "Tree.paintLines", Boolean.FALSE,
			    "Tree.lineTypeDashed", Boolean.FALSE,
			    "Tree.scrollsOnExpand", Boolean.TRUE,
			    "Tree.changeSelectionWithFocus", Boolean.TRUE,
			    "Tree.drawsFocusBorderAroundIcon", Boolean.FALSE,
			    "Tree.rendererFillBackground", Boolean.FALSE,
			    "Tree.drawDashedFocusIndicator", Boolean.FALSE,
			    "Tree.rendererFillBackground", Boolean.FALSE,
			    "Tree.rendererMargins", treeUIDelegate.getRendererMargins(),
				/*
				 * XXX: trick for JDK 5
				 */
				"Tree.textBackground", new Color(0, 0, 0, 0),
				"Tree.selectionBorderColor", null,
				
				"Viewport.border", A03BorderFactory.createDelegatedBorder(viewportUIDelegate),
		};
		
		table.putDefaults(keyValueList);
	}
	
	
	public final void installUI(Component c) {
		// do nothing
	}
	
	public final void uninstallUI(Component c) {
		// do nothing
	}
}
