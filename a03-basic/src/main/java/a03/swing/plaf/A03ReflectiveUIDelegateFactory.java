package a03.swing.plaf;

import java.lang.reflect.Method;

import javax.swing.JComponent;

public abstract class A03ReflectiveUIDelegateFactory extends A03AbstractUIDelegateFactory {
	
	private Method[] methods;
	
	public A03ReflectiveUIDelegateFactory() {
		Class<A03ReflectiveUIDelegateFactory> clazz = (Class<A03ReflectiveUIDelegateFactory>) getClass();

		methods = clazz.getMethods();
	}
	
	public <T extends A03UIDelegate> T getUIDelegate(JComponent c, Class<T> clazz) {
		if (c != null) {
			StringBuilder builder = new StringBuilder("get");
			builder.append(c.getUIClassID());
			builder.append("Delegate");
			
			try {
				Method method = getClass().getMethod(builder.toString());
				
				Class returnType = method.getReturnType();
				if (clazz.isAssignableFrom(returnType)) {
					return (T) method.invoke(this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			for (Method method : methods) {
				try {
					Class returnType = method.getReturnType();
					if (returnType.equals(clazz)) {
						return (T) method.invoke(this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

}
