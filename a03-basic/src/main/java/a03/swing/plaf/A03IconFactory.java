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

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.UIResource;

public class A03IconFactory {

	public static IconUIResource createArrowIcon(A03MenuUIDelegate delegate) {
		return new IconUIResource(new ArrowIcon(delegate));
	}
	
	private static class ArrowIcon implements Icon, UIResource {
		
		private A03MenuUIDelegate uiDelegate;
		
		public ArrowIcon(A03MenuUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getArrowIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getArrowIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintArrowIcon(c, g, x, y);
		}
	}

	private static class CheckBoxMenuItemIcon implements Icon, UIResource {
		
		private A03CheckBoxMenuItemUIDelegate uiDelegate;
		
		public CheckBoxMenuItemIcon(A03CheckBoxMenuItemUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getCheckBoxIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getCheckBoxIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintCheckBoxIcon(c, g, x, y);
		}
	}

	private static class RadioButtonMenuItemIcon implements Icon, UIResource {
		
		private A03RadioButtonMenuItemUIDelegate uiDelegate;
		
		public RadioButtonMenuItemIcon(A03RadioButtonMenuItemUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getRadioButtonIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getRadioButtonIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintRadioButtonIcon(c, g, x, y);
		}
	}

	private static class ErrorIcon implements Icon, UIResource {
		
		private A03OptionPaneUIDelegate uiDelegate;
		
		public ErrorIcon(A03OptionPaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getErrorIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getErrorIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintErrorIcon(c, g, x, y);
		}
	}

	private static class InformationIcon implements Icon, UIResource {
		
		private A03OptionPaneUIDelegate delegate;
		
		public InformationIcon(A03OptionPaneUIDelegate delegate) {
			this.delegate = delegate;
		}
		
		public int getIconHeight() {
			return delegate.getInformationIconHeight();
		}
		
		public int getIconWidth() {
			return delegate.getInformationIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			delegate.paintInformationIcon(c, g, x, y);
		}
	}

	private static class WarningIcon implements Icon, UIResource {
		
		private A03OptionPaneUIDelegate uiDelegate;
		
		public WarningIcon(A03OptionPaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getWarningIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getWarningIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintWarningIcon(c, g, x, y);
		}
	}

	private static class QuestionIcon implements Icon, UIResource {
		
		private A03OptionPaneUIDelegate uiDelegate;
		
		public QuestionIcon(A03OptionPaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getQuestionIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getQuestionIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintQuestionIcon(c, g, x, y);
		}
	}
	
	private static class TreeOpenIcon implements Icon, UIResource {
		
		private A03TreeUIDelegate uiDelegate;
		
		public TreeOpenIcon(A03TreeUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getTreeOpenIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getTreeOpenIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintTreeOpenIcon(c, g, x, y);
		}
	}
	
	private static class TreeClosedIcon implements Icon, UIResource {
		
		private A03TreeUIDelegate uiDelegate;
		
		public TreeClosedIcon(A03TreeUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getTreeClosedIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getTreeClosedIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintTreeClosedIcon(c, g, x, y);
		}
	}
	
	private static class TreeLeafIcon implements Icon, UIResource {
		
		private A03TreeUIDelegate uiDelegate;
		
		public TreeLeafIcon(A03TreeUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getTreeLeafIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getTreeLeafIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintTreeLeafIcon(c, g, x, y);
		}
	}
	
	private static class TreeExpandedIcon implements Icon, UIResource {
		
		private A03TreeUIDelegate uiDelegate;
		
		public TreeExpandedIcon(A03TreeUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getTreeExpandedIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getTreeExpandedIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintTreeExpandedIcon(c, g, x, y);
		}
	}
	
	private static class TreeCollapsedIcon implements Icon, UIResource {
		
		private A03TreeUIDelegate uiDelegate;
		
		public TreeCollapsedIcon(A03TreeUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getTreeCollapsedIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getTreeCollapsedIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintTreeCollapsedIcon(c, g, x, y);
		}
	}

	private static class CloseIcon implements Icon, UIResource {
		
		private A03TitlePaneUIDelegate uiDelegate;
		
		public CloseIcon(A03TitlePaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getCloseIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getCloseIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintCloseIcon(c, g, x, y);
		}
	}

	private static class IconifyIcon implements Icon, UIResource {
		
		private A03TitlePaneUIDelegate delegate;
		
		public IconifyIcon(A03TitlePaneUIDelegate delegate) {
			this.delegate = delegate;
		}
		
		public int getIconHeight() {
			return delegate.getIconifyIconHeight();
		}
		
		public int getIconWidth() {
			return delegate.getIconifyIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			delegate.paintIconifyIcon(c, g, x, y);
		}
	}

	private static class MaximizeIcon implements Icon, UIResource {
		
		private A03TitlePaneUIDelegate uiDelegate;
		
		public MaximizeIcon(A03TitlePaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getMaximizeIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getMaximizeIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintMaximizeIcon(c, g, x, y);
		}
	}

	private static class MinimizeIcon implements Icon, UIResource {
		
		private A03TitlePaneUIDelegate uiDelegate;
		
		public MinimizeIcon(A03TitlePaneUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getMinimizeIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getMinimizeIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintMinimizeIcon(c, g, x, y);
		}
	}

	private static class AscendingSortIcon implements Icon, UIResource {
		
		private A03TableUIDelegate uiDelegate;
		
		public AscendingSortIcon(A03TableUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getAscendingSortIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getAscendingSortIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintAscendingSortIcon(c, g, x, y);
		}
	}

	private static class DescendingSortIcon implements Icon, UIResource {
		
		private A03TableUIDelegate uiDelegate;
		
		public DescendingSortIcon(A03TableUIDelegate delegate) {
			this.uiDelegate = delegate;
		}
		
		public int getIconHeight() {
			return uiDelegate.getDescendingSortIconHeight();
		}
		
		public int getIconWidth() {
			return uiDelegate.getDescendingSortIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			uiDelegate.paintDescendingSortIcon(c, g, x, y);
		}
	}

	public static IconUIResource createWarningIcon(A03OptionPaneUIDelegate delegate) {
		return new IconUIResource(new WarningIcon(delegate));
	}

	public static IconUIResource createQuestionIcon(A03OptionPaneUIDelegate delegate) {
		return new IconUIResource(new QuestionIcon(delegate));
	}

	public static IconUIResource createInformationIcon(A03OptionPaneUIDelegate delegate) {
		return new IconUIResource(new InformationIcon(delegate));
	}

	public static IconUIResource createErrorIcon(A03OptionPaneUIDelegate delegate) {
		return new IconUIResource(new ErrorIcon(delegate));
	}

	public static IconUIResource getCheckBoxMenuItemIcon(A03CheckBoxMenuItemUIDelegate delegate) {
		return new IconUIResource(new CheckBoxMenuItemIcon(delegate));
	}

	public static IconUIResource createRadioButtonMenuItemIcon(A03RadioButtonMenuItemUIDelegate delegate) {
		return new IconUIResource(new RadioButtonMenuItemIcon(delegate));
	}

	public static IconUIResource createTreeOpenIcon(A03TreeUIDelegate delegate) {
		return new IconUIResource(new TreeOpenIcon(delegate));
	}

	public static IconUIResource createTreeClosedIcon(A03TreeUIDelegate delegate) {
		return new IconUIResource(new TreeClosedIcon(delegate));
	}

	public static IconUIResource createTreeLeafIcon(A03TreeUIDelegate delegate) {
		return new IconUIResource(new TreeLeafIcon(delegate));
	}

	public static IconUIResource createTreeExpandedIcon(A03TreeUIDelegate delegate) {
		return new IconUIResource(new TreeExpandedIcon(delegate));
	}

	public static IconUIResource createTreeCollapsedIcon(A03TreeUIDelegate delegate) {
		return new IconUIResource(new TreeCollapsedIcon(delegate));
	}

	public static IconUIResource createCloseIcon(A03TitlePaneUIDelegate delegate) {
		return new IconUIResource(new CloseIcon(delegate));
	}

	public static IconUIResource createIconifyIcon(A03TitlePaneUIDelegate delegate) {
		return new IconUIResource(new IconifyIcon(delegate));
	}
	
	public static IconUIResource createMinimizeIcon(A03TitlePaneUIDelegate delegate) {
		return new IconUIResource(new MinimizeIcon(delegate));
	}

	public static IconUIResource createMaximizeIcon(A03TitlePaneUIDelegate delegate) {
		return new IconUIResource(new MaximizeIcon(delegate));
	}

	public static IconUIResource createAscendingSortIcon(A03TableUIDelegate delegate) {
		return new IconUIResource(new AscendingSortIcon(delegate));
	}

	public static IconUIResource createDescendingSortIcon(A03TableUIDelegate delegate) {
		return new IconUIResource(new DescendingSortIcon(delegate));
	}

	public static IconUIResource createCheckBoxIcon(A03CheckBoxUIDelegate delegate) {
		return new IconUIResource(new CheckBoxIcon(delegate));
	}

	public static IconUIResource createRadioButtonIcon(A03RadioButtonUIDelegate delegate) {
		return new IconUIResource(new RadioButtonIcon(delegate));
	}

	private static class CheckBoxIcon implements Icon, UIResource {
		
		private A03CheckBoxUIDelegate delegate;
		
		public CheckBoxIcon(A03CheckBoxUIDelegate delegate) {
			this.delegate = delegate;
		}
		
		public int getIconHeight() {
			return delegate.getIconHeight();
		}
		
		public int getIconWidth() {
			return delegate.getIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			delegate.paintIcon(c, g, x, y);
		}
	}

	private static class RadioButtonIcon implements Icon, UIResource {
		
		private A03RadioButtonUIDelegate delegate;
		
		public RadioButtonIcon(A03RadioButtonUIDelegate delegate) {
			this.delegate = delegate;
		}
		
		public int getIconHeight() {
			return delegate.getIconHeight();
		}
		
		public int getIconWidth() {
			return delegate.getIconWidth();
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			delegate.paintIcon(c, g, x, y);
		}
	}
}
