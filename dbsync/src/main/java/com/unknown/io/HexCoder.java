package com.unknown.io;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HexCoder {

	private static char[] al = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static int high = 0xf0;

	private static int low = 0xf;

	public static String toString(byte[] bytes) {
		StringBuilder strBuilder = new StringBuilder();
		for (byte bb : bytes) {
			strBuilder.append(al[(bb & high) >> 4 & low]);
			strBuilder.append(al[bb & low]);
		}
		return strBuilder.toString();
	}

	public static byte[] toBytes(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		char[] hexA = hex.toCharArray();
		for (int i = 0; i < hexA.length; i++) {
			bytes[i / 2] = (byte) (getByte(hexA[i]) << 4 | getByte(hexA[++i]));
		}
		return bytes;
	}

	public static byte getByte(char c) {
		byte bb = (byte) (c - '0');
		if (bb <= 9) {
			return bb;
		}
		bb = (byte) (c - 'a');
		if (bb <= 5) {
			return (byte) (10 + bb);
		}
		bb = (byte) (c - 'A');
		if (bb <= 5) {
			return (byte) (10 + bb);
		}
		throw new RuntimeException("Bad hex char");
	}

	public static void print(byte[] bytes){
		ByteBuffer bf = ByteBuffer.wrap(bytes);
		bf.order(ByteOrder.LITTLE_ENDIAN);
		while(bf.hasRemaining()){
			short ss = bf.getShort();			
			byte[] dst = new byte[ss];
			bf.get(dst);
			System.out.println(new String(dst));
		}
	}
	
//	public final byte[] a(String paramString)
//	  {
//	    int i = 0;
//	    byte[] arrayOfByte1;
//	    try
//	    {
//	      MessageDigest localMessageDigest = MessageDigest.getInstance("sha-1");
//	      int j = paramString.getBytes().length;
//	      byte[] arrayOfByte2 = paramString.getBytes();
//	      if (j > 0)
//	        localMessageDigest.update(arrayOfByte2, 0, j);
//	      byte[] arrayOfByte3 = localMessageDigest.digest();
//	      "0123456789abcdef".length();
//	      byte[] arrayOfByte4 = "0123456789abcdef".getBytes();
//	      arrayOfByte1 = new byte[40];
//	      if (i >= 20)
//	        break label128;
//	      arrayOfByte1[(i * 2)] = arrayOfByte4[((0xF0 & arrayOfByte3[i]) >> 4)];
//	      arrayOfByte1[(1 + i * 2)] = arrayOfByte4[(0xF & arrayOfByte3[i])];
//	      ++i;
//	    }
//	    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
//	    {
//	    //  Log.e("YYSDK", "kelvin getPasswdSha1 exception");
//	      localNoSuchAlgorithmException.printStackTrace();
//	      arrayOfByte1 = null;
//	    }
//	    label128: return arrayOfByte1;
//	  }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String hex = "0501800fe5c9f00000b01000004d60b00c800750000001100323131383730383339325f707265726567280062376465383535306539373066396634626365333936366433626337323163636463616363336633010000000060eeb67c7adaab060079796d616e6420003532383430343766346666623465303438323461326664316431663063643632000004010000a8ec487e0000000000000060009b287f8a8b63adb088ff27b3e53ff9834b7104a2dd18cf728057d0e441e258036fd3b872e162e9a37bf744f9750b15322c43691884f72c918ad00b598710af2c04e34c0ec59006197b21781d7cc623f00c34aeaa56ca75f354555dcd620efe141100323131383730383339325f70726572656700000000";
		hex = hex.substring(0,80);
//		print(toBytes(hex));
		
		byte[] bytes = "duowan_oo7".getBytes("utf-8");
		bytes = toBytes(hex);
//		ByteBuffer bf = ByteBuffer.allocate(8192);
//		bf.order(ByteOrder.BIG_ENDIAN);
//		bf.put(bytes);
//		bytes = new byte[bf.position()];
//		bf.position(0);
//		bf.get(bytes);
//		System.out.println(toString(bytes));
		System.out.println(new String(bytes));
	}
}
