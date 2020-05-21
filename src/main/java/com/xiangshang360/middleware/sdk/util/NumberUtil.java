package com.xiangshang360.middleware.sdk.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

public class NumberUtil {
    /**
     * 在允许的范围内比较两个数字的大小
     *
     * @param preNumber  前一个数字
     * @param lastNumber 后一个数字
     * @param scale      误差范围
     * @return
     */
    public static int compareBigDecimal(BigDecimal preNumber, BigDecimal lastNumber, int scale) {
        BigDecimal cha = preNumber.subtract(lastNumber).setScale(scale, RoundingMode.DOWN);
        return cha.compareTo(BigDecimal.ZERO);
    }

    /**
     * 10进制与62进制互相转换值，请不要修改参数
     */
    private static final char[] BASE_62_VALUE_ARRAY = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};

    /**
     * 将10进制数字转换成62进制字符串
     *
     * @param number
     * @return
     */
    public static final String base10ToBase62(long number) {
        Long rest = number;
        Stack<Character> stack = new Stack<Character>();
        StringBuilder result = new StringBuilder(0);
        while (rest != 0) {
            stack.add(BASE_62_VALUE_ARRAY[new Long((rest - (rest / 62) * 62)).intValue()]);
            rest = rest / 62;
        }
        for (; !stack.isEmpty(); ) {
            result.append(stack.pop());
        }
        return result.toString();
    }

    /**
     * 将62进制字符串转换成10进制数字
     *
     * @param numberFor62
     * @return
     */
    public static long base62ToBase10(String numberFor62) {
        int multiple = 1;
        long result = 0;
        Character c;
        for (int i = 0; i < numberFor62.length(); i++) {
            c = numberFor62.charAt(numberFor62.length() - i - 1);
            result += getBase62Value(c) * multiple;
            multiple = multiple * 62;
        }
        return result;
    }

    private static int getBase62Value(Character c) {
        for (int i = 0; i < BASE_62_VALUE_ARRAY.length; i++) {
            if (c == BASE_62_VALUE_ARRAY[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        long l = 1502018083000000001l;
        String s = NumberUtil.base10ToBase62(l);
        System.out.println(s);
    }

}
