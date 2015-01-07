package com.unknown.io;

import java.io.IOException;

public interface LineFilter<T> {

	boolean filter(T context, String line) throws IOException;
}
