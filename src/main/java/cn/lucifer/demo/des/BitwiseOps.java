/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.lucifer.demo.des;

/**
 *
 * @author petermeckiffe
 */
public class BitwiseOps {
    public static char XOR(char partA, char partB){
        if(partA != partB){
            return '1';
        }
        else{
            return '0';
        }
    }
    public static String getHexToBinary(String hex) {
        // Get binary representation of hex Number
        String bin = Long.toBinaryString(Long.parseLong(hex, 16));
        
        // Find amoutn of padding required
        int diff = hex.length()*4-bin.length();
        // Pad binary number with required 0's
        for(int i = 0; i<diff;i++){
            bin = "0"+bin;
        }
        return bin;
    }
    
    public static String getIntToBinary(int integer){
        return BitwiseOps.getHexToBinary(Long.toHexString(integer));
        
    }

    public static String getHex(String bin) {
        int mod = bin.length()%4;
        // Pad binary if not already padded
        for(int i = 0;i<mod;i++){
            bin = "0"+bin;
        }
        // Get hex string and required padding for hex
        String hex = Long.toHexString(Long.parseLong(bin, 2));
        int diff = bin.length()/4-hex.length();
        // Pad binary number with required 0's
        for(int i = 0; i<diff;i++){
            hex = "0"+hex;
        }
        return hex;
    }
    
    public static String getXOR(String block, String key) {
        String result = "";
        // Get char arrays for easy comparison
        char[] keyItems = block.toCharArray();
        char[] blockItems = key.toCharArray();
        
        // Cycle over chars and XOR each one
        for (int i = 0; i < keyItems.length; i++) {
            result += BitwiseOps.XOR(blockItems[i], keyItems[i]);
        }
        return result;
    }
}
