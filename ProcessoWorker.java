public class ProcessoWorker {

    public static void main(String[] args) throws Exception {

        int inicio = Integer.parseInt(args[0]);
        int fim = Integer.parseInt(args[1]);

        int[][] A = LeitorMatriz.lerMatriz("matriz_1000.txt");
        int[][] B = LeitorMatriz.lerMatriz("matriz_1000.txt");

        int tamanho = A.length;

        int[][] C = new int[tamanho][tamanho];

        for (int i = inicio; i < fim; i++) {

            for (int j = 0; j < tamanho; j++) {

                int soma = 0;

                for (int k = 0; k < tamanho; k++) {
                    soma += A[i][k] * B[k][j];
                }

                C[i][j] = soma;
            }
        }

        System.out.println(
                "Processo terminou linhas "
                        + inicio + " até " + fim
        );
    }
}