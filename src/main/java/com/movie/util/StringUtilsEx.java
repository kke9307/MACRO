package com.movie.util;

import org.springframework.util.StringUtils;

public class StringUtilsEx extends StringUtils {
	public static String padLeft(String in, int len, String fill) {
		if(in == null || fill == null ||
			in.length() == 0 || len == 0 || fill.length() == 0) {
			return in;
		}
		if(in.length() >= len) {
			return in;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < len - in.length()) {
			sb.append(fill);
		}
		sb.append(in);
		
		return sb.toString();
	}
}
