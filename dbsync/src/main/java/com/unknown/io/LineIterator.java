package com.unknown.io;

import java.io.IOException;

public interface LineIterator<T> {
	void readLine(T context, String line) throws IOException;
}
