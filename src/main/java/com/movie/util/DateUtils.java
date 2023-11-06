package com.movie.util;

import java.text.SimpleDateFormat;

/**
 * 날짜 util 클래스.
 *
 * @author tsis
 *
 */
public class DateUtils {

	private static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat SDF_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat SDF_YYYYMMDDHHMMSSS = new SimpleDateFormat("yyyyMMddHHmmsss");

	/**
	 * 현재시간의 문자열 취득
	 *
	 * @return yyyyMMdd
	 */
	public static String currentDateYmd() {
		return SDF_YYYYMMDD.format(System.currentTimeMillis());
	}

	/**
	 * 현재시간의 문자열 취득
	 *
	 * @return yyyyMMddHHmmss 예)20210224103219
	 */
	public static String currentDate() {
		return SDF_YYYYMMDDHHMMSS.format(System.currentTimeMillis());
	}

	/**
	 * 현재시간의 문자열 취득
	 *
	 * @return yyyyMMddHHmmsss 예)202102241032198
	 */
	public static String currentDateYmdHmss() {
		return SDF_YYYYMMDDHHMMSSS.format(System.currentTimeMillis());
	}

	/**
	 * 시간의 문자열 취득
	 *
	 * @return yyyyMMddHHmmsss 예)202102241032198
	 */
	public static String currentDateYmdHmss(long timeMilllis) {
		return SDF_YYYYMMDDHHMMSSS.format(timeMilllis);
	}

	/**
	 * 지정한 문자열과 시간을 결합한 문자열 취득
	 *
	 * @param prefix
	 *            접두 문자열
	 * @param timeMilllis
	 *            시간의 long값
	 * @return 예)[SKT202102241032198]
	 */
	public static String createRequestId(String prefix, long timeMilllis) {
		return "[" + prefix + SDF_YYYYMMDDHHMMSSS.format(timeMilllis) + "]";
	}
}
