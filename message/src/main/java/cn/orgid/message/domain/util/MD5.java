package cn.orgid.message.domain.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5 {

	private static Log log = LogFactory.getLog(MD5.class.getName());

	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * @return 密文
	 */
	public static String md5(String text, String cat) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("PlatformManager doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes(cat)); // 注意改接口是按照指定编码形式签名

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException("PlatformManager doesn't support your  EncodingException.");

		}
		byte[] bytes = msgDigest.digest();
		String md5Str = new String(encodeHex(bytes));
		return md5Str;
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * @return 密文
	 */
	public static String md5(String text) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("PlatformManager doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes("UTF-8")); // 注意改接口是按照指定编码形式签名

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException("PlatformManager doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	public static byte[] hex2byte(String str) { // 字符串转二进制
		if (str == null)
			return null;
		str = str.trim();
		str = str.replace(" ", "");
		int len = str.length();
		if (len == 0 || len % 2 != 0)
			return null;

		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	public static String byte2hex(byte[] b, int max) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < max; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().toUpperCase().trim();
	}

	public static String createUrl(Map params) {
		return getSignContent(params, "");
	}

	public static String getSignContent(Map params, String privateKey) {

		params = ParaFilter(params);

		List keys = new ArrayList(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr + privateKey;
	}

	/**
	 * 功能：除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map ParaFilter(Map sArray) {
		List keys = new ArrayList(sArray.keySet());
		Map sArrayNew = new HashMap();

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) sArray.get(key);

			if (StringUtils.isBlank(value) || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}

			sArrayNew.put(key, value);
		}

		return sArrayNew;
	}

	/**
	 * 功能：将签名变成map直接引用
	 * 格式务必：cacheKey=user_login_&method=verifyCodeGet&phoneNum=15336531661
	 */
	public static Map<String, String> getSignMaps(String pa) {
		Map<String, String> map = new HashMap<String, String>();
		String pas[] = pa.split("&");
		for (int i = 0; i < pas.length; i++) {
			String[] maps = pas[i].split("=");
			if (maps.length > 1) {
				map.put(maps[0], maps[1]);
			}
		}
		return map;
	}

	public static void main(String[] arg) {

		// e10adc3949ba59abbe56e057f20f883e -- 123456
		//System.out.println(MD5.md5("123456"));

	}

}
