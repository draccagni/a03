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


import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLookAndFeel;

import a03.swing.plugin.A03UIPluginManager;

public abstract class A03AbstractLookAndFeel extends BasicLookAndFeel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1452810291686326017L;
	
	public A03AbstractLookAndFeel() {
    }
    
	public abstract String getID();

	public abstract String getDescription();

	public abstract String getName();

	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		//System.out.println(System.getProperties());
		// TODO JDK > 1.5
		return A03Constants.IS_JAVA_5 || A03Constants.IS_JAVA_6_OR_LATER;
	}
	
    public boolean getSupportsWindowDecorations() {
        return true;
    }
    
    @Override
    public UIDefaults getDefaults() {
        UIDefaults table = super.getDefaults();

//        initClassDefaults(table);
        // XXX needs BasicLookAndFeel defaults
        initSystemColorDefaults(table);
        initComponentDefaults(table);

    	A03UIPluginManager.getInstance().getUIPlugins().installDefaults(table);
    	
    	return table;
    }
    
//    @Override
//    public void initialize() {
//    	super.initialize();
//    	
//    	A03SwingUtilities.updateUI();
//    }
    
//    @Override
//    public void uninitialize() {
//    	super.uninitialize();
//    	
//    	A03SwingUtilities.updateUI();
//    }
}
