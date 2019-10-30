import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] h = {{0,	0,	1,	0,	1,	1,	1,	0,	0,	1 },
                {0,	0,	0,	1,	0,	1,	0,	1,	0,	1},
                {1,	0,	1,	0,	1,	1,	0,	0,	1,	0},
                {1,	1,	0,	1,	1,	1,	0,	0,	0,	0}
        };
        int[] e = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int s;
        int[][] ans = new int [32][10];
        int[] ans1 = {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11};
        for (int t = 1; t < 1024; t++) {
            int n = 9, k = e[n];
            e[n] = 1 - k;
            while (k == 1) {
                n--;
                k = e[n];
                e[n] = 1 - k;
            }
            k = 0;
            for (int i = 0; i < 10; i++) {
                k += e[i];
            }
            int p = 0;
            for (int i = 0; i < 4; i++) {
                s = 0;

                for (int j = 0; j < 10; j++) {

                    s += e[j] * h[i][j];
                    s %= 2;

                }
                System.out.print(" " + s);
                p *= 2;
                p += s;

            }
            if (k < ans1[p]) {
                ans1[p] = k;
                System.arraycopy(e,0,ans[p],0, 10);
            }
            System.out.print("  ");
            for (int i = 0; i < 10; i++) {
                System.out.print(e[i]);
            }
            System.out.println();
        }
        for (int i = 0; i < 16; i++) {
            System.out.print(" " + i + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(ans[i][j]);
            }
            System.out.println();
        }
    }
}
