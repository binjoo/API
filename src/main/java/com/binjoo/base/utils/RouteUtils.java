package com.binjoo.base.utils;

public class RouteUtils {
	/**
	 * 替换下划线并且相邻的后一位字母大写
	 * 
	 * @param source
	 *            字符串
	 * @param firstCase
	 *            是否转换首字母为大写
	 * @return
	 */
	public static String replaceUnderlineAndFirstLetterToUpper(String source, boolean firstCase) {
		int index = source.indexOf("_");
		while (index != -1) {
			source = source.replaceFirst("_", "");
			if (index != source.length()) {
				char[] letter = source.substring(index, index + 1).toCharArray();
				source = source.substring(0, index) + Character.toTitleCase(letter[0])
						+ source.substring(index + 1, source.length());
			}
			index = source.indexOf("_");
		}

		final char firstChar = source.charAt(0);
		if (firstCase && !Character.isTitleCase(firstChar)) {
			source = Character.toTitleCase(firstChar) + source.substring(1);
		}

		return source;
	}

	/**
	 * 替换下划线并且相邻的后一位字母大写
	 * 
	 * @param source
	 *            字符串
	 * @return
	 */
	public static String replaceUnderlineAndFirstLetterToUpper(String source) {
		return replaceUnderlineAndFirstLetterToUpper(source, true);
	}
}
