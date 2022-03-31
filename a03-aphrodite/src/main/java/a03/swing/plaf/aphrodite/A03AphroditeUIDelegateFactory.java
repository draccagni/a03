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

import a03.swing.plaf.A03ButtonUIDelegate;
import a03.swing.plaf.A03CheckBoxMenuItemUIDelegate;
import a03.swing.plaf.A03CheckBoxUIDelegate;
import a03.swing.plaf.A03ComboBoxUIDelegate;
import a03.swing.plaf.A03DesktopIconUIDelegate;
import a03.swing.plaf.A03DesktopPaneUIDelegate;
import a03.swing.plaf.A03EditorPaneUIDelegate;
import a03.swing.plaf.A03FileChooserUIDelegate;
import a03.swing.plaf.A03FormattedTextFieldUIDelegate;
import a03.swing.plaf.A03InternalFrameUIDelegate;
import a03.swing.plaf.A03LabelUIDelegate;
import a03.swing.plaf.A03ListUIDelegate;
import a03.swing.plaf.A03MenuBarUIDelegate;
import a03.swing.plaf.A03MenuItemUIDelegate;
import a03.swing.plaf.A03MenuUIDelegate;
import a03.swing.plaf.A03OptionPaneUIDelegate;
import a03.swing.plaf.A03PanelUIDelegate;
import a03.swing.plaf.A03PasswordFieldUIDelegate;
import a03.swing.plaf.A03PopupMenuSeparatorUIDelegate;
import a03.swing.plaf.A03PopupMenuUIDelegate;
import a03.swing.plaf.A03ProgressBarUIDelegate;
import a03.swing.plaf.A03RadioButtonMenuItemUIDelegate;
import a03.swing.plaf.A03RadioButtonUIDelegate;
import a03.swing.plaf.A03ReflectiveUIDelegateFactory;
import a03.swing.plaf.A03RootPaneUIDelegate;
import a03.swing.plaf.A03ScrollBarUIDelegate;
import a03.swing.plaf.A03ScrollPaneUIDelegate;
import a03.swing.plaf.A03SeparatorUIDelegate;
import a03.swing.plaf.A03SliderUIDelegate;
import a03.swing.plaf.A03SpinnerUIDelegate;
import a03.swing.plaf.A03SplitPaneUIDelegate;
import a03.swing.plaf.A03SystemColorUIDelegate;
import a03.swing.plaf.A03TabbedPaneUIDelegate;
import a03.swing.plaf.A03TableHeaderUIDelegate;
import a03.swing.plaf.A03TableUIDelegate;
import a03.swing.plaf.A03TextAreaUIDelegate;
import a03.swing.plaf.A03TextComponentUIDelegate;
import a03.swing.plaf.A03TextFieldUIDelegate;
import a03.swing.plaf.A03TextPaneUIDelegate;
import a03.swing.plaf.A03TitledBorderUIDelegate;
import a03.swing.plaf.A03ToggleButtonUIDelegate;
import a03.swing.plaf.A03ToolBarSeparatorUIDelegate;
import a03.swing.plaf.A03ToolBarUIDelegate;
import a03.swing.plaf.A03ToolTipUIDelegate;
import a03.swing.plaf.A03TreeUIDelegate;
import a03.swing.plaf.A03ViewportUIDelegate;
import a03.swing.plaf.basic.A03BasicFileChooserUIDelegate;
import a03.swing.plaf.basic.A03BasicLabelUIDelegate;

public class A03AphroditeUIDelegateFactory extends A03ReflectiveUIDelegateFactory {
	
	public A03AphroditeUIDelegateFactory() {
	}
	
	public String getLookAndFeelID() {
		return "A03_APHRODITE_LOOK_AND_FEEL";
	}
	
	public A03ButtonUIDelegate getButtonUIDelegate() {
		return new A03AphroditeButtonUIDelegate();
	}
	
	public A03ToggleButtonUIDelegate getToggleButtonUIDelegate() {
		return new A03AphroditeButtonUIDelegate();
	}
	
	public A03CheckBoxUIDelegate getCheckBoxUIDelegate() {
		return new A03AphroditeCheckBoxUIDelegate();
	}

	public A03RadioButtonUIDelegate getRadioButtonUIDelegate() {
		return new A03AphroditeRadioButtonUIDelegate();
	}

	public A03ComboBoxUIDelegate getComboBoxUIDelegate() {
		return new A03AphroditeComboBoxUIDelegate();
	}
	
	public A03TabbedPaneUIDelegate getTabbedPaneUIDelegate() {
		return new A03AphroditeTabbedPaneUIDelegate();
	}
	
	public A03ScrollBarUIDelegate getScrollBarUIDelegate() {
		return new A03AphroditeScrollBarUIDelegate();
	}
	

	public A03SpinnerUIDelegate getSpinnerUIDelegate() {
		return new A03AphroditeSpinnerUIDelegate();
	}

	public A03LabelUIDelegate getLabelUIDelegate() {
		return new A03BasicLabelUIDelegate();
	}
	

	public A03DesktopPaneUIDelegate getDesktopPaneUIDelegate() {
		return new A03AphroditeDesktopPaneUIDelegate();
	}
	

	public A03DesktopIconUIDelegate getDesktopIconUIDelegate() {
		return new A03AphroditeDesktopIconUIDelegate();
	}
	

	public A03FileChooserUIDelegate getFileChooserUIDelegate() {
		return new A03BasicFileChooserUIDelegate();
	}
	

	public A03InternalFrameUIDelegate getInternalFrameUIDelegate() {
		return new A03AphroditeInternalFrameUIDelegate();
	}
	

	public A03ListUIDelegate getListUIDelegate() {
		return new A03AphroditeListUIDelegate();
	}
	

	public A03MenuBarUIDelegate getMenuBarUIDelegate() {
		return new A03AphroditeMenuBarUIDelegate();
	}
	

	public A03MenuItemUIDelegate getMenuItemUIDelegate() {
		return new A03AphroditeMenuItemUIDelegate();
	}

	public A03MenuUIDelegate getMenuUIDelegate() {
		return new A03AphroditeMenuItemUIDelegate();
	}

	public A03CheckBoxMenuItemUIDelegate getCheckBoxMenuItemUIDelegate() {
		return new A03AphroditeMenuItemUIDelegate();
	}

	public A03RadioButtonMenuItemUIDelegate getRadioButtonMenuItemUIDelegate() {
		return new A03AphroditeMenuItemUIDelegate();
	}

	public A03OptionPaneUIDelegate getOptionPaneUIDelegate() {
		return new A03AphroditeOptionPaneUIDelegate();
	}


	public A03PopupMenuUIDelegate getPopupMenuUIDelegate() {
		return new A03AphroditePopupMenuUIDelegate();
	}


	public A03PopupMenuSeparatorUIDelegate getPopupMenuSeparatorUIDelegate() {
		return new A03AphroditePopupMenuSeparatorUIDelegate();
	}
	

	public A03ProgressBarUIDelegate getProgressBarUIDelegate() {
		return new A03AphroditeProgressBarUIDelegate();
	}
	

	public A03RootPaneUIDelegate getRootPaneUIDelegate() {
		return new A03AphroditeRootPaneUIDelegate();
	}


	public A03ScrollPaneUIDelegate getScrollPaneUIDelegate() {
		return new A03AphroditeScrollPaneUIDelegate();
	}


	public A03SeparatorUIDelegate getSeparatorUIDelegate() {
		return new A03AphroditeSeparatorUIDelegate();
	}


	public A03SliderUIDelegate getSliderUIDelegate() {
		return new A03AphroditeSliderUIDelegate();
	}


	public A03SplitPaneUIDelegate getSplitPaneUIDelegate() {
		return new A03AphroditeSplitPaneUIDelegate();
	}


	public A03SystemColorUIDelegate getSystemColorUIDelegate() {
		return new A03AphroditeSystemColorUIDelegate();
	}


	public A03TableUIDelegate getTableUIDelegate() {
		return new A03AphroditeTableUIDelegate();
	}


	public A03TableHeaderUIDelegate getTableHeaderUIDelegate() {
		return new A03AphroditeTableHeaderUIDelegate();
	}


	public A03TextComponentUIDelegate getTextComponentUIDelegate() {
		return new A03AphroditeTextComponentUIDelegate();
	}

	public A03TextFieldUIDelegate getTextFieldUIDelegate() {
		return new A03AphroditeTextComponentUIDelegate();
	}

	public A03TextAreaUIDelegate getTextAreaUIDelegate() {
		return new A03AphroditeTextComponentUIDelegate();
	}

	public A03TextPaneUIDelegate getTextPaneUIDelegate() {
		return new A03AphroditeTextComponentUIDelegate();
	}

	public A03FormattedTextFieldUIDelegate getFormattedTextFieldUIDelegate() {
		return new A03AphroditeTextComponentUIDelegate();
	}

	public A03EditorPaneUIDelegate getEditorPaneUIDelegate() {
		return new A03AphroditeTextComponentUIDelegate();
	}

	public A03PasswordFieldUIDelegate getPasswordFieldUIDelegate() {
		return new A03AphroditePasswordFieldUIDelegate();
	}

	public A03PanelUIDelegate getPanelUIDelegate() {
		return new A03AphroditePanelUIDelegate();
	}


	public A03TitledBorderUIDelegate getTitledBorderUIDelegate() {
		return new A03AphroditeTitledBorderUIDelegate();
	}


	public A03ToolBarUIDelegate getToolBarUIDelegate() {
		return new A03AphroditeToolBarUIDelegate();
	}


	public A03ToolBarSeparatorUIDelegate getToolBarSeparatorUIDelegate() {
		return new A03AphroditeToolBarSeparatorUIDelegate();
	}


	public A03ToolTipUIDelegate getToolTipUIDelegate() {
		return new A03AphroditeToolTipUIDelegate();
	}


	public A03TreeUIDelegate getTreeUIDelegate() {
		return new A03AphroditeTreeUIDelegate();
	}


	public A03ViewportUIDelegate getViewportUIDelegate() {
		return new A03AphroditeViewportUIDelegate();
	}
}
