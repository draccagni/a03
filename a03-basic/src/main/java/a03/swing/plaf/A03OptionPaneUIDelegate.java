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
import java.awt.Insets;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public interface A03OptionPaneUIDelegate extends A03BorderUIDelegate, A03UIDelegate {
	
	public ColorUIResource getBackground();

	public ColorUIResource getMessageAreaBackground();
	
	public FontUIResource getMessageFont();
	
	public Insets getMessageAreaBorderInsets(Component c, Insets insets);

	public void paintMessageAreaBorder(Component c, Graphics g, int x, int y, int width, int height);
	
	public Insets getButtonAreaBorderInsets(Component c, Insets insets);

	public void paintButtonAreaBorder(Component c, Graphics g, int x, int y, int width, int height);
	
	public String getErrorSound();

	public String getInformationSound();

	public String getQuestionSound();

	public String getWarningSound();
	
	public int getErrorIconWidth();
	
	public int getErrorIconHeight();
	
	public void paintErrorIcon(Component c, Graphics g, int x, int y);

	public int getWarningIconWidth();
	
	public int getWarningIconHeight();
	
	public void paintWarningIcon(Component c, Graphics g, int x, int y);

	public int getInformationIconWidth();
	
	public int getInformationIconHeight();
	
	public void paintInformationIcon(Component c, Graphics g, int x, int y);

	public int getQuestionIconWidth();
	
	public int getQuestionIconHeight();
	
	public void paintQuestionIcon(Component c, Graphics g, int x, int y);

	public FontUIResource getFont();
}
