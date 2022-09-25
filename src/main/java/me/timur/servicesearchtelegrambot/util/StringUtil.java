package me.timur.servicesearchtelegrambot.util;

/**
 * Created by Temurbek Ismoilov on 10/05/22.
 */

public class StringUtil {

    public static double findSimilarities(String str, String str2) {
        String[] words = split(str);
        String[] words2 = split(str2);

        double max = Double.MIN_VALUE;

        for (String word1: words) {
            for (String word2: words2){
                double similarity = findSimilarity(word1, word2);
                if (similarity > max)
                    max = similarity;
            }
        }

        return max;
    }

    private static String[] split(String str) {
        if (str == null)
            return new String[0];

        return str.replaceAll("/", " ").split(" ");
    }

    private static double findSimilarity(String x, String y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            return (maxLength - getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

    private static int getLevenshteinDistance(String X, String Y){
        int m = X.length();
        int n = Y.length();

        int[][] T = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            T[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            T[0][j] = j;
        }

        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = X.charAt(i - 1) == Y.charAt(j - 1) ? 0: 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost);
            }
        }

        return T[m][n];
    }
}
