import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Versão B — Multiplicação de matrizes usando Threads.
 *
 * Todas as threads rodam dentro da mesma JVM e compartilham
 * o mesmo heap (arrays A, B e C são passados por referência).
 *
 * Executa automaticamente para 2, 4 e 8 threads,
 * repetindo 5 vezes cada cenário e calculando a média
 * (descartando a 1ª execução como aquecimento da JVM).
 */
public class MainThreads {

    public static void main(String[] args) throws Exception {

        // Arquivo de matriz a usar (altere para matriz_1500.txt se desejar)
        String arquivoMatriz = "matriz_1000.txt";

        // Carrega as matrizes uma única vez (compartilhadas entre todas as rodadas)
        System.out.println("Carregando matrizes...");
        int[][] A = LeitorMatriz.lerMatriz(arquivoMatriz);
        int[][] B = LeitorMatriz.lerMatriz(arquivoMatriz);
        int tamanho = A.length;
        System.out.println("Matrizes " + tamanho + "x" + tamanho + " carregadas.\n");

        // Delay para facilitar o monitoramento externo de CPU/RAM (resmon, Get-Process)
        System.out.println("Aguarde 5 segundos para iniciar o monitoramento externo...");
        Thread.sleep(5000);

        int[] configuracoesThreads = {2, 4, 8};
        int repeticoes = 5;

        // Arquivo de log dos tempos para uso no relatório
        PrintWriter log = new PrintWriter(new FileWriter("resultados_threads.txt"));
        log.println("=== Resultados - Versão Threads ===");
        log.println("Matriz: " + tamanho + "x" + tamanho);
        log.println();

        System.out.println("=== VERSÃO THREADS ===\n");

        for (int numThreads : configuracoesThreads) {

            System.out.println("--- " + numThreads + " thread(s) ---");
            log.println("Threads: " + numThreads);

            double somaTempos = 0;
            int validasParaMedia = 0;

            for (int rodada = 1; rodada <= repeticoes; rodada++) {

                // Matriz resultado zerada a cada rodada
                int[][] C = new int[tamanho][tamanho];

                WorkerThread[] threads = new WorkerThread[numThreads];
                int linhasPorThread = tamanho / numThreads;

                long inicio = System.nanoTime();

                // Cria e inicia cada thread com seu intervalo de linhas
                for (int i = 0; i < numThreads; i++) {
                    int inicioLinha = i * linhasPorThread;
                    // Última thread pega o restante (caso tamanho não seja divisível)
                    int fimLinha = (i == numThreads - 1) ? tamanho : inicioLinha + linhasPorThread;
                    threads[i] = new WorkerThread(A, B, C, inicioLinha, fimLinha);
                    threads[i].start();
                }

                // Aguarda todas as threads terminarem
                for (int i = 0; i < numThreads; i++) {
                    threads[i].join();
                }

                long fim = System.nanoTime();
                double tempo = (fim - inicio) / 1_000_000_000.0;

                String status = (rodada == 1) ? " (aquecimento — descartado)" : "";
                System.out.printf("  Rodada %d: %.4f s%s%n", rodada, tempo, status);
                log.printf("  Rodada %d: %.4f s%s%n", rodada, tempo, status);

                // Descarta a primeira execução (aquecimento da JVM)
                if (rodada > 1) {
                    somaTempos += tempo;
                    validasParaMedia++;
                }
            }

            double media = somaTempos / validasParaMedia;
            System.out.printf("  >> Media (rodadas 2-5): %.4f s%n%n", media);
            log.printf("  >> Media (rodadas 2-5): %.4f s%n%n", media);
        }

        log.close();
        System.out.println("Resultados salvos em resultados_threads.txt");
    }
}
