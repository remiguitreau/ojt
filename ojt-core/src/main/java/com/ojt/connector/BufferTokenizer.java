package com.ojt.connector;

import java.util.List;

public interface BufferTokenizer {
	List<String> cutOnDelimiters(final String s);
}
