package com.greatwall.lock.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class Utils {
	
	public static byte[] login() {
		int[] arrayOfInt = new int[20];
		arrayOfInt[0] = 54;
		arrayOfInt[1] = 48;
		arrayOfInt[2] = 50;
		arrayOfInt[3] = 0;
		arrayOfInt[4] = 54;
		arrayOfInt[5] = 48;
		arrayOfInt[6] = 50;
		
		arrayOfInt[7] = 0x59;
		arrayOfInt[8] = 0x91;
		arrayOfInt[9] = 0x5a;
		arrayOfInt[10] = 0x9d;
		arrayOfInt[11] = 0x08;
		arrayOfInt[19] = Utils.crc8(arrayOfInt, 19);
		byte[] arrayOfByte = Utils.getBytes(Utils
				.encode(arrayOfInt, 51));//end
		return arrayOfByte;
	}
	
	public static byte[] pack(String mPassword) {
		int[] arrayOfInt = new int[20];
		arrayOfInt[0] = 49;
		arrayOfInt[1] = 50;
		arrayOfInt[2] = 51;
		arrayOfInt[3] = 3;
		arrayOfInt[4] = mPassword.charAt(0);
		arrayOfInt[5] = mPassword.charAt(1);
		arrayOfInt[6] = mPassword.charAt(2);
		arrayOfInt[7] = mPassword.charAt(3);
		arrayOfInt[8] = mPassword.charAt(4);
		arrayOfInt[9] = mPassword.charAt(5);
		arrayOfInt[19] = Utils.crc8(arrayOfInt, 19);
		byte[] arrayOfByte = Utils.getBytes(Utils
				.encode(arrayOfInt, 51));//end
		return arrayOfByte;
	}
	
	public static int[] encode2(int[] data, int d){
		int[] code = new int[data.length];
		int h = 0;
		int i = 0;
		
		h = data[0] & 0x80;
		
		for(i=0; i<data.length - 1; i++){
			code[i] = (data[i] << 1) & 0xFF;
			if((data[i+1] & 0x80) != 0){
				code[i] |= 0x01;
			}
		}
		
		code[i] = (data[i] << 1) & 0xFF;
		if(h != 0){
			code[i] |= 0x01;
		}
		
		for(i=0; i<code.length - 1; i++){
			code[i] ^= d;
			code[i] &= 0xFF;
		}
		
		return code;
	}
	
	public static byte[] login2() {
		
		int[] idata = new int[20];
		long time = System.currentTimeMillis();
		time /= 1000;
		int tzone = TimeZone.getDefault().getRawOffset();
		tzone /= 3600000;
		
		idata[0] = 0x31;
		idata[1] = 0x32;
		idata[2] = 0x33;
		idata[3] = 0x00;
		idata[4] = 0x31;
		idata[5] = 0x32;
		idata[6] = 0x33;
		idata[7] = (int)(time & 0xFF000000) >> 24;
		idata[8] = (int)(time & 0x00FF0000) >> 16;
		idata[9] = (int)(time & 0x0000FF00) >> 8;
			idata[10] = (int)(time & 0x000000FF);
			if(tzone >= 0){
				idata[11] = tzone & 0xFF;
			}
			else{
				idata[11] = (0xFF - (tzone & 0xFF) + 1) | 0xF0;
			}
			idata[19] = Utils.crc8(idata, 19);
			idata = Utils.encode(idata, 0x33);
			
//			Log.e(TAG, Utils.int2Str(idata));
			byte[] bdata = Utils.getBytes(idata);
			return null;
	}
	// public static int Date1CompareToDate2(String paramString1, String
	// paramString2)
	// {
	// SimpleDateFormat localSimpleDateFormat = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm");
	// Calendar localCalendar1 = Calendar.getInstance();
	// Calendar localCalendar2 = Calendar.getInstance();
	// try
	// {
	// localCalendar1.setTime(localSimpleDateFormat.parse(paramString1));
	// localCalendar2.setTime(localSimpleDateFormat.parse(paramString2));
	// label38: return localCalendar1.compareTo(localCalendar2);
	// }
	// catch (ParseException localParseException)
	// {
	// break label38;
	// }
	// }

//	public static int crc8(int[] paramArrayOfInt, int paramInt) {
//		int i = 0;
//		int j = 0;
//		int m;
//		for (int k = paramInt;; k = m) {
//			m = k - 1;
//			if (k == 0)
//				break;
//			int n = 128;
//			if (n != 0) {
//				if ((i & 0x80) != 0)
//					i = 0x7 ^ i << 1;
//				while (true) {
//					if ((n & paramArrayOfInt[j]) != 0) {
//						i ^= 7;
//						n >>= 1;
//						break;
//					}
//					i <<= 1;
//				}
//			}
//			i &= 255;
//			j++;
//		}
//		return i;
//	}

	public static int crc8(int[] arrayOfInt, int len) {
		// TODO Auto-generated method stub
		int crc = 0;
		int i = 0;
		int j = 0;

		while (len-- != 0) {
			for (i = 0x80; i != 0; i >>= 1) {
				if ((crc & 0x80) != 0) {
					crc <<= 1;
					crc ^= 0x07;
				} else {
					crc <<= 1;
				}

				if ((arrayOfInt[j] & i) != 0) {
					crc ^= 0x07;
				}
			}
			j++;

		}
		return crc;
	}
	
	public static int[] decode(int[] paramArrayOfInt, int paramInt) {
		int[] arrayOfInt = new int[paramArrayOfInt.length];
		for (int i = 0; i < -1 + paramArrayOfInt.length; i++) {
			paramArrayOfInt[i] = (paramInt ^ paramArrayOfInt[i]);
			paramArrayOfInt[i] = (0xFF & paramArrayOfInt[i]);
		}
		int j = 0x1 & paramArrayOfInt[(-1 + paramArrayOfInt.length)];
		arrayOfInt[0] = (0xFF & paramArrayOfInt[0] >> 1);
		if (j != 0)
			arrayOfInt[0] = (0x80 | arrayOfInt[0]);
		for (int k = 1; k < paramArrayOfInt.length; k++) {
			arrayOfInt[k] = (0xFF & paramArrayOfInt[k] >> 1);
			if ((0x1 & paramArrayOfInt[(k - 1)]) == 0)
				continue;
			arrayOfInt[k] = (0x80 | arrayOfInt[k]);
		}
		return arrayOfInt;
	}

	public static int[] encode(int[] paramArrayOfInt, int paramInt) {
		int[] arrayOfInt = new int[paramArrayOfInt.length];
		int i = 0x80 & paramArrayOfInt[0];
		int j = 0;
		for (j = 0; j < -1 + paramArrayOfInt.length; j++) {
			arrayOfInt[j] = (0xFF & paramArrayOfInt[j] << 1);
			if ((0x80 & paramArrayOfInt[(j + 1)]) == 0)
				continue;
			arrayOfInt[j] = (0x1 | arrayOfInt[j]);
		}
		arrayOfInt[j] = (0xFF & paramArrayOfInt[j] << 1);
		if (i != 0)
			arrayOfInt[i] = (0x1 | arrayOfInt[i]);
		for (int k = 0; k < -1 + arrayOfInt.length; k++) {
			arrayOfInt[k] = (paramInt ^ arrayOfInt[k]);
			arrayOfInt[k] = (0xFF & arrayOfInt[k]);
		}
		return arrayOfInt;
	}

	public static byte getByte(int paramInt) {
		if ((paramInt & 0x80) == 0)
			return (byte) paramInt;
		return (byte) (-256 + (paramInt & 0xFF));
	}

	public static byte[] getBytes(int[] paramArrayOfInt) {
		byte[] arrayOfByte = new byte[paramArrayOfInt.length];
		if (paramArrayOfInt.length > 0)
			for (int i = 0; i < paramArrayOfInt.length; i++)
				arrayOfByte[i] = getByte(paramArrayOfInt[i]);
		return arrayOfByte;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}

	public static String getCurrentTime(String paramString) {
		Date localDate = new Date();
		return new SimpleDateFormat(paramString, Locale.getDefault())
				.format(localDate);
	}

	public static int getUnsignedByte(byte paramByte) {
		return paramByte & 0xFF;
	}

	public static int[] getUnsignedBytes(byte[] paramArrayOfByte) {
		int[] arrayOfInt = new int[paramArrayOfByte.length];
		if (paramArrayOfByte.length > 0)
			for (int i = 0; i < paramArrayOfByte.length; i++)
				arrayOfInt[i] = getUnsignedByte(paramArrayOfByte[i]);
		return arrayOfInt;
	}

	public static long strToDateLong(String paramString) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).parse(
				paramString, new ParsePosition(0)).getTime();
	}
}

/*
 * Location: C:\Users\yuanye\Desktop\arti-lock\dex2jar\classes_dex2jar.jar
 * Qualified Name: com.miot.doorsystem.utils.Utils JD-Core Version: 0.6.0
 */