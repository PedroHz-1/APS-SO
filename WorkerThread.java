public class WorkerThread extends Thread {

    private int[][] A;
    private int[][] B;
    private int[][] C;

    private int inicio;
    private int fim;

    public WorkerThread(int[][] A, int[][] B, int[][] C,
                        int inicio, int fim) {

        this.A = A;
        this.B = B;
        this.C = C;

        this.inicio = inicio;
        this.fim = fim;
    }

    @Override
    public void run() {

        int tamanho = A.length;

        for (int i = inicio; i < fim; i++) {

            for (int j = 0; j < tamanho; j++) {

                int soma = 0;

                for (int k = 0; k < tamanho; k++) {
                    soma += A[i][k] * B[k][j];
                }

                C[i][j] = soma;
            }
        }
    }
}