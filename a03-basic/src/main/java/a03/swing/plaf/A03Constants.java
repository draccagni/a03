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

import java.awt.Cursor;

public interface A03Constants {
	
	public final static Cursor DEFAULT_CURSOR_INSTANCE = new Cursor(Cursor.DEFAULT_CURSOR); 
	public final static Cursor WAIT_CURSOR_INSTANCE = new Cursor(Cursor.WAIT_CURSOR); 
	public final static Cursor HAND_CURSOR_INSTANCE = new Cursor(Cursor.HAND_CURSOR); 
	public final static Cursor MOVE_CURSOR_INSTANCE = new Cursor(Cursor.MOVE_CURSOR); 
	public final static Cursor TEXT_CURSOR_INSTANCE = new Cursor(Cursor.TEXT_CURSOR); 

	public final static Cursor E_RESIZE_CURSOR_INSTANCE = new Cursor(Cursor.E_RESIZE_CURSOR); 
	public final static Cursor N_RESIZE_CURSOR_INSTANCE = new Cursor(Cursor.N_RESIZE_CURSOR); 

    public final static String ORIGINAL_OPACITY = "A03_ORIGINAL_OPACITY";
    public final static String ORIGINAL_CURSOR = "A03_ORIGINAL_CURSOR";
    public final static String ORIGINAL_LAYOUT = "A03_ORIGINAL_LAYOUT";
    public final static String ORIGINAL_NONROLLOVER_TOGGLE_BORDER = "A03_ORIGINAL_NONROLLOVER_TOGGLE_BORDER";
    public final static String ORIGINAL_BORDER = "A03_ORIGINAL_BORDER";
    public final static String ORIGINAL_MENU_DELAY = "A03_ORIGINAL_MENU_DELAY";

	public final static String ORIGINAL_BACKGROUND = "A03_ORIGINAL_BACKGROUND";

	public final static String UI_DELEGATE = "A03_UI_DELEGATE";

	/*
     * Provides convenience behavior used by the JGoodies Looks.
     *
     * @author  Karsten Lentzsch
     * @version $Revision: 1.1 $
     */
    final static String OS_NAME = System.getProperty("os.name");
    
    final static String JAVA_VERSION = System.getProperty("java.version");
    
    final static String JAVA_VENDOR = System.getProperty("java.vm.specification.vendor");

    public static final boolean IS_JAVA_5 = JAVA_VERSION.startsWith("1.5");
    public final static boolean IS_JAVA_6 = JAVA_VERSION.startsWith("1.6");
    
    public final static boolean IS_JAVA_6_OR_LATER = IS_JAVA_6 || !IS_JAVA_5;

    public static final boolean IS_OS_MAC = OS_NAME.startsWith("Mac");
    
    public static final boolean IS_SUN_JVM = OS_NAME.startsWith("Sun");
}
