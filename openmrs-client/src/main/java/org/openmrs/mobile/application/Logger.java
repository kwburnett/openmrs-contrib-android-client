package org.openmrs.mobile.application;

public interface Logger {

	void v(String msg);
	void v(String tag, String msg);
	void v(String msg, Throwable throwable);
	void v(String tag, String msg, Throwable throwable);

	void d(String msg);
	void d(String tag, String msg);
	void d(String msg, Throwable throwable);
	void d(String tag, String msg, Throwable throwable);

	void i(String msg);
	void i(String tag, String msg);
	void i(String msg, Throwable throwable);
	void i(String tag, String msg, Throwable throwable);

	void w(String msg);
	void w(String tag, String msg);
	void w(String msg, Throwable throwable);
	void w(String tag, String msg, Throwable throwable);

	void e(String msg);
	void e(String tag, String msg);
	void e(String msg, Throwable throwable);
	void e(String tag, String msg, Throwable throwable);
	void e(Throwable throwable);

	void setUser(String user);
}
