package nfc.communication;

public final class Utility {
	
	public static int currentOrder;

	public static final int APDU_COMMAND_SUCCESS = 36864;
	public static boolean CANCELED = false;

	public static String byteArrayToHexString(byte[] b) 
    {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
        	if(b[i] == 92) {
        		break;
        	}
          int v = b[i] & 0xff;
          if (v < 16) {
            sb.append('0');
          }
          sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
     }
    
    public static String hexToASCII(String hexValue)
    {
       StringBuilder output = new StringBuilder("");
       for (int i = 0; i < hexValue.length(); i += 2)
       {
          String str = hexValue.substring(i, i + 2);
          output.append((char) Integer.parseInt(str, 16));
       }
       return output.toString();
    }

    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }
    
    public static byte[] hexStringToByteArray(String s) 
    {     
            int len = s.length();     
            byte[] data = new byte[len / 2];     
            for (int i = 0; i < len; i += 2) 
            {         
                    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));     
            }     
            return data; 
    }
    
    public static byte[] hexStringToByteArray1(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
