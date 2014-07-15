/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.lucifer.demo.des;

/**
 *
 * @author petermeckiffe
 */
public class Permutations {

    final int[] InitialPermutation = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    final int[] ExpansionPermutation = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8,
        9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    final int[] FinalPermutation = {16, 7, 20, 21, 29, 12, 28, 17, 1,
        15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27,
        3, 9, 19, 13, 30, 6, 22, 11, 4, 25};
    final int[] AntiInitialPermutation = {40, 8, 48, 16, 56, 24, 64, 32, 39,
        7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45,
        13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51,
        19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    final int[] KeyPermutation1 = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
    final int[] KeyPermutation2 = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    final int[] shiftQ = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    
    
    // EACH PERMUTATION USES FOR EACH AND CONCAT'S A STRING TO PRODUCE OUTPUT
    public String initialPermutation(String block) {
        String resultString = "";
        for (int i : InitialPermutation) {
            resultString += block.charAt(i - 1);
        }
        return resultString;
    }

    public String expansionPermutation(String block) {
        String resultString = "";
        for (int i : ExpansionPermutation) {
            resultString += block.charAt(i - 1);
        }
        return resultString;
    }

    public String finalPermutation(String block) {
        String resultString = "";
        for (int i : FinalPermutation) {
            resultString += block.charAt(i - 1);
        }
        return resultString;
    }
    public String antiInitialPermutation(String block){
        String resultString = "";
        for (int i : AntiInitialPermutation) {
            resultString += block.charAt(i - 1);
        }
        return resultString;
    }

    public String keyPermutationA(String key) {
        String resultString = "";
        for (int i : KeyPermutation1) {
            resultString += key.charAt(i - 1);
        }
        return resultString;
    }

    public String keyPermutationB(String key) {
        String resultString = "";
        for (int i : KeyPermutation2) {
            resultString += key.charAt(i - 1);
        }
        return resultString;
    }

    // Using prespecified shift quantity (shiftQ) for each round,
    // leftshift splits input in two, shifts each side by that amount,
    // and returns the result
    public String leftShift(String key, int round) {
        String result = "";
        result += key.substring(this.shiftQ[round], 28);
        result += key.substring(0, this.shiftQ[round]);
        return result;
    }
}
