package io.github.zy945.util.algorithm;

/**
 * @author 伍六七
 * @date 2023/6/29 9:23
 */
public class AlgorithmUtil {
    public static void dp() {
        int[] value = new int[]{1, 4, 3, 4, 5};
        int[] weight = new int[]{1, 2, 3, 4, 5};
        //2 3
        int[][] res = new int[value.length + 1][value.length + 1];
        for (int i = 1; i < value.length; i++) {
            for (int j = 1; j < weight.length; j++) {
                if (weight[j] < j) {
                    res[i][j] = Math.max(res[i - 1][j], res[i][j - weight[i]] + value[j]);
                }
            }

        }
        System.out.println(res[value.length - 1][weight.length - 1]);
    }

    public static void main(String[] args) {
        dp();
    }
}
