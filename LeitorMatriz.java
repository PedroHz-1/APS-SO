import java.io.*;
import java.util.*;

public class LeitorMatriz {

    public static int[][] lerMatriz(String arquivo) throws Exception {

        Scanner sc = new Scanner(new File(arquivo));

        int tamanho = sc.nextInt();

        int[][] matriz = new int[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                matriz[i][j] = sc.nextInt();
            }
        }

        sc.close();

        return matriz;
    }
}
