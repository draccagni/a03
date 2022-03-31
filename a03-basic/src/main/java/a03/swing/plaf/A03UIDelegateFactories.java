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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.UIManager;

public final class A03UIDelegateFactories {
	
	private static A03UIDelegateFactories instance = new A03UIDelegateFactories();
	
	private List<A03AbstractUIDelegateFactory> factories;
	
	private A03UIDelegateFactories() {
		factories = new ArrayList<A03AbstractUIDelegateFactory>();
		
		try {
			Enumeration<URL> e = getClass().getClassLoader().getResources("META-INF/services/a03.swing.plaf.A03AbstractUIDelegateFactory");
			for (; e.hasMoreElements(); ) {
				URL u = e.nextElement();
				BufferedReader r = new BufferedReader(new InputStreamReader(u.openStream()));
				String l;
				while ((l = r.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(l, ",");
					while (st.hasMoreTokens()) {
						try {
							String t = st.nextToken().trim();
							Class<A03AbstractUIDelegateFactory> clazz = (Class<A03AbstractUIDelegateFactory>) Class.forName(t);

							A03AbstractUIDelegateFactory factory = (A03AbstractUIDelegateFactory) clazz.newInstance();
							factories.add(factory);
						} catch (InstantiationException e1) {
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static A03UIDelegateFactories getInstance() {
		return instance;
	}

	public <T extends A03UIDelegate> T getUIDelegate(JComponent c, Class<T> clazz) {
		String lookAndFeelID = UIManager.getLookAndFeel().getID();
		
		for (A03AbstractUIDelegateFactory factory : factories) {
			if (factory.getLookAndFeelID().equals(lookAndFeelID)) {
				A03UIDelegate uiDelegate = factory.getUIDelegate(c, clazz);
				if (uiDelegate != null) {
					return (T) uiDelegate;
				}
			}
		}
		
		return null;
	}

}
