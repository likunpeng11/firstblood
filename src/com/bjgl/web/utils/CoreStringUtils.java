package com.bjgl.web.utils;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class CoreStringUtils {
	
	private static Map<String, CharsetDecoder> charsetDecoderMap = new HashMap<String, CharsetDecoder>();
	
	public static synchronized CharsetDecoder getCharsetDecoder(String charset) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
		if (charsetDecoderMap.containsKey(charset)) {
			return charsetDecoderMap.get(charset);
		}
		Charset charsetObj = Charset.forName(charset);
		CharsetDecoder decoder = charsetObj.newDecoder();
		charsetDecoderMap.put(charset, decoder);
		return decoder;
	}
	
	public static String convertByteBuffer(ByteBuffer bb, String charset) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException, CharacterCodingException {
		CharsetDecoder decoder = getCharsetDecoder(charset);
		CharBuffer cb = decoder.decode(bb);
		return cb.toString();
	}
	
	/**
	 * 按组替换字符串中内容，与String.replaceAll方法不同的是不采用正则匹配
	 * 每一行表示一次替换，第1个元素表示要查找的内容，查找到以后替换成第2个元素的值
	 * 顺序执行
	 * @param src
	 * @param delimiterArray
	 * @return
	 */
	public static String replaceAll(String src, String[][] delimiterArray) {
		String tmp = src;
		int length = delimiterArray.length;
		for (int i = 0; i < length; i++) {
			tmp = StringUtils.replace(tmp, delimiterArray[i][0], delimiterArray[i][1]);
		}
		return tmp;
	}
	
	public static String replaceMax(String src, String[][] delimiterArray, int max) {
		String tmp = src;
		int length = delimiterArray.length;
		for (int i = 0; i < length; i++) {
			tmp = StringUtils.replace(tmp, delimiterArray[i][0], delimiterArray[i][1], max);
		}
		return tmp;
	}	
	
	public static String join(Collection<String> col, String delimiter) {
		StringBuffer buffer = new StringBuffer();
        Iterator<String> iter = col.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
	}
	
    public static String unicodeToString(String str) {
    	StringBuffer src = new StringBuffer(str);
    	StringBuffer des = new StringBuffer();
    	
    	char[] unicodeBuffer = new char[6];
    	int unicodeBufferIndex = -1;
    	
    	int srcLength = src.length();
    	int readIndex = -1;
    	while ((++ readIndex) < srcLength) {
    		char c = src.charAt(readIndex);
    		// 已有前序的匹配
    		switch (unicodeBufferIndex) {
    			case -1:
    				if (c != '\\') {
    					break;
        			} else {
        				unicodeBufferIndex ++;
            			unicodeBuffer[unicodeBufferIndex] = c;
            			continue;
        			}
    			case 0:
    				if (c != 'u') {
    					break;
    				} else {
    					unicodeBufferIndex ++;
    	    			unicodeBuffer[unicodeBufferIndex] = c;
    	    			continue;
    				}
    			case 1:
    			case 2:
    			case 3:
    			case 4:
    				// 判断16进制字符
    				if ((c >= 48 && c <= 57) || (c >= 97 && c <= 102)) {
    					unicodeBufferIndex ++;
    					unicodeBuffer[unicodeBufferIndex] = c;
    					
    					if (unicodeBufferIndex == 5) {
    						//完全匹配，执行转换
    						StringBuffer chBuffer = new StringBuffer();
    						for (int i = 2; i <= unicodeBufferIndex; i++) {
    							chBuffer.append(unicodeBuffer[i]);
							}
    						char ch = (char)Integer.parseInt(chBuffer.toString(), 16);
    						des.append(ch);
    						
    						unicodeBufferIndex = -1;
    						continue;
    					}
    					
    	    			continue;
    				} else {
    					break;
    				}
    		}
    		
    		// 匹配的都已经continue了，这里处理匹配失败的
    		for (int i = 0; i <= unicodeBufferIndex; i ++) {
    			des.append(unicodeBuffer[i]);
    		}
    		des.append(c);
    		unicodeBufferIndex = -1;
    	}
    	return des.toString();
    	
    	/*
    	 * 正则版本的转换，大数据量下占用CPU资源过高，转换耗时太长弃用
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");   
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");   
        }
        return str;
        */
    }
    
	/**
	 * MD5标准计算摘要,32位
	 * */
	public static String md5(String s, String encoding) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes(encoding);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * MD5标准计算摘要,16位
	 * */
	public static String md5_16(String s, String encoding) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes(encoding);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).substring(8, 24).toUpperCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将字符串的值转换成Double
	 * @param val
	 * @return
	 */
    public static Double convertDouble(String val){
    	Double d = null;
    	if(val != null){
    		try{
    			d = Double.valueOf(val);
    		}catch(Exception e){
    			return null;
    		}
    	}
    	return d;
    }
    /**
     * 去掉字符串中的所有空格、制表符、换行符等
     * @param str
     * @return
     */
    public static String trimAll(String str){
    	if(str == null){
    		return null;
    	}
    	String result = null;
    	result = str.trim();
    	result = replaceAll(result, new String[][] {
    			{" ", ""},
    			{"　", ""},
    			{"&nbsp;", ""},
    			{"&nbsp", ""},
    			{"\t", ""},
    			{"\r\n", ""},
    			{"\n", ""}
    			
    	});
    	return result;
    }
	
	/**
	 * 截取位于开始位置和结束位置之间的文本
	 * @param text
	 * @param begin
	 * @param end
	 * @return
	 */
	public static String substringBetween(String text, String begin, String end) {
	    return StringUtils.substringBetween(text, begin, end);
	}
	
	/**
	 * Java自带大小写会转换全角字符，此系列方法只处理ASCII字符
	 * @param str
	 * @return
	 */
	public static String lowerCaseAscII(String str) {
		return StringUtils.replaceChars(str, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz");
	}
	
	public static String upperCaseAscII(String str) {
		return StringUtils.replaceChars(str, "abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}
	
	/**
	 * 根据规则生成字符串，默认为对String.format的方法包装
	 * @param pattern
	 * @param params
	 * @return
	 */
	public static String build(String pattern, Object... params) {
		return String.format(pattern, params);
	}

    /**
     * zLib压缩，压缩字符串
     * @param data
     * @param encoding
     * @return
     * @throws Exception
     */
	public static byte[] compressData(String data ,String encoding) throws Exception {
		
		if("".equals(data) || data == null){
    		throw new Exception();
    	}
		byte[] dataBytes = data.getBytes(encoding);
		ByteArrayOutputStream bos = null;
		DeflaterOutputStream zos = null;
		byte[] buf = new byte[dataBytes.length];
		try {
			bos = new ByteArrayOutputStream();
			zos = new DeflaterOutputStream(bos);
			zos.write(dataBytes);
			// 此处必须关闭压缩流，否则bos.toByteArray()为null
			zos.close();
			buf = bos.toByteArray();
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				throw e;
			}
		}
		return buf;
	}
    
	/**
	 * zLib解压缩，将压缩后的字节数组解压
	 * @param data
	 * @return
	 * @throws Exception
	 */
    public static byte[] decompress(byte[] data) throws Exception {

    	if(data.length == 0){
    		throw new Exception();
    	}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InflaterOutputStream zos = new InflaterOutputStream(bos);
		byte[] buf = null;
		try {
			zos.write(data);
			buf = bos.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {  
            try {
            	zos.close();  
            	bos.close();
            } catch (IOException e) {
            	throw e; 
            }  
        }  
		return buf;
    }
}
