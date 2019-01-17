package org.openmrs.mobile.application;

public interface Logger {

	void v(final String msg);

	void v(final String msg, Throwable throwable);

	void d(final String msg);

	void d(final String msg, Throwable throwable);

	void i(final String msg);

	void i(final String msg, Throwable throwable);

	void w(final String msg);

	void w(final String msg, Throwable throwable);

	void e(final String msg);

	void e(final String msg, Throwable throwable);

	void e(Throwable throwable);
}
