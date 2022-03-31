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

public abstract class A03AbstactSwingUIDelegateFactory extends A03ReflectiveUIDelegateFactory {

	public abstract A03ButtonUIDelegate getButtonUIDelegate();
	
	public abstract A03CheckBoxUIDelegate getCheckBoxUIDelegate();

	public abstract A03ComboBoxUIDelegate getComboBoxUIDelegate();

	public abstract A03DesktopPaneUIDelegate getDesktopPaneUIDelegate();

	public abstract A03FileChooserUIDelegate getFileChooserUIDelegate();
	
	public abstract A03InternalFrameUIDelegate getInternalFrameUIDelegate();
	
	public abstract A03LabelUIDelegate getLabelUIDelegate();
	
	public abstract A03ListUIDelegate getListUIDelegate();
	
	public abstract A03MenuBarUIDelegate getMenuBarUIDelegate();

	public abstract A03MenuItemUIDelegate getMenuItemUIDelegate();

	public abstract A03OptionPaneUIDelegate getOptionPaneUIDelegate();
	
	public abstract A03PanelUIDelegate getPanelUIDelegate();
	
	public abstract A03PopupMenuUIDelegate getPopupMenuUIDelegate();
	
	public abstract A03PopupMenuSeparatorUIDelegate getPopupMenuSeparatorUIDelegate();

	public abstract A03ProgressBarUIDelegate getProgressBarUIDelegate();
	
	public abstract A03RadioButtonUIDelegate getRadioButtonUIDelegate();

	public abstract A03RootPaneUIDelegate getRootPaneUIDelegate();

	public abstract A03ScrollBarUIDelegate getScrollBarUIDelegate();

	public abstract A03ScrollPaneUIDelegate getScrollPaneUIDelegate();
	
	public abstract A03SeparatorUIDelegate getSeparatorUIDelegate();

	public abstract A03SliderUIDelegate getSliderUIDelegate();

	public abstract A03SpinnerUIDelegate getSpinnerUIDelegate();

	public abstract A03SplitPaneUIDelegate getSplitPaneUIDelegate();

	public abstract A03SystemColorUIDelegate getSystemColorUIDelegate();
	
	public abstract A03TabbedPaneUIDelegate getTabbedPaneUIDelegate();
	
	public abstract A03TableHeaderUIDelegate getTableHeaderUIDelegate();
	
	public abstract A03TableUIDelegate getTableUIDelegate();
	
	public abstract A03TextComponentUIDelegate getTextComponentDelegate();

	public abstract A03TitledBorderUIDelegate getTitledBorderUIDelegate();

	public abstract A03ToolBarUIDelegate getToolBarUIDelegate();

	public abstract A03ToolBarSeparatorUIDelegate getToolBarSeparatorUIDelegate();

	public abstract A03ToolTipUIDelegate getToolTipUIDelegate();

	public abstract A03TreeUIDelegate getTreeUIDelegate();

	public abstract A03ViewportUIDelegate getViewportUIDelegate();
	
}
