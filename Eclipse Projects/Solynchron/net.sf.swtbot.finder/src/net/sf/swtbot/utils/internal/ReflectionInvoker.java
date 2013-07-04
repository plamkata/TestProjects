package net.sf.swtbot.utils.internal;

import java.lang.reflect.Method;

import net.sf.swtbot.finder.UIThreadRunnable.StringResult;

/**
 * This is an object use to invoke a method using reflections.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public final class ReflectionInvoker implements StringResult {
	/**
	 * The object to invoke a method on.
	 */
	private final Object	w;
	/**
	 * The method to invoke.
	 */
	private final String	methodName;

	/**
	 * Constructs this object.
	 * 
	 * @param o the object to be invoked on.
	 * @param methodName the method to invoke on the object.
	 */
	public ReflectionInvoker(Object o, String methodName) {
		w = o;
		this.methodName = methodName;
	}

	/**
	 * Runs the processing to trigger the method to be invoked.
	 * 
	 * @see net.sf.swtbot.finder.UIThreadRunnable.StringResult#run()
	 * @return The results of the invoke.
	 */
	public String run() {
		String result = "";
		try {
			Method method = w.getClass().getMethod(methodName, null);
			Object invoke = method.invoke(w, new Object[0]);
			if (invoke != null)
				result = invoke.toString();
		} catch (Exception e) {
			// do nothing
		}
		return result;
	}
}
