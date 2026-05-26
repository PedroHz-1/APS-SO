import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Versão A — Multiplicação de matrizes usando Processos (múltiplas JVMs).
 *
 * Cada processo filho é uma instância independente da JVM.
 * Eles recebem o intervalo de linhas via argumentos de linha de comando
 * e leem as matrizes diretamente do arquivo em disco.
 *
 * Executa automaticamente para 2, 4 e 8 processos,
 * repetindo 5 vezes cada cenário e calculando a média
 * (descartando a 1ª execução como aquecimento).
 */
public class MainProcessos {

    public static void main(String[] args) throws Exception {

        int tamanho = 1000; // deve bater com o arquivo de matriz usado em ProcessoWorker

        int[] configuracoesProcessos = {2, 4, 8};
        int repeticoes = 5;

        // Delay para facilitar o monitoramento externo (resmon, Get-Process)
        System.out.println("Aguarde 5 segundos para iniciar o monitoramento externo...");
        Thread.sleep(5000);

        PrintWriter log = new PrintWriter(new FileWriter("resultados_processos.txt"));
        log.println("=== Resultados - Versão Processos ===");
        log.println("Matriz: " + tamanho + "x" + tamanho);
        log.println();

        System.out.println("=== VERSÃO PROCESSOS ===\n");

        for (int numProcessos : configuracoesProcessos) {

            System.out.println("--- " + numProcessos + " processo(s) ---");
            log.println("Processos: " + numProcessos);

            double somaTempos = 0;
            int validasParaMedia = 0;
            int linhasPorProcesso = tamanho / numProcessos;

            for (int rodada = 1; rodada <= repeticoes; rodada++) {

                List<Process> processos = new ArrayList<>();

                long inicio = System.nanoTime();

                // Cria um processo filho (nova JVM) para cada intervalo de linhas
                for (int i = 0; i < numProcessos; i++) {
                    int inicioLinha = i * linhasPorProcesso;
                    // Último processo pega o restante
                    int fimLinha = (i == numProcessos - 1) ? tamanho : inicioLinha + linhasPorProcesso;

                    ProcessBuilder pb = new ProcessBuilder(
                            "java",
                            "-cp", ".",          // IMPORTANTE: informa onde estão as classes compiladas
                            "ProcessoWorker",
                            String.valueOf(inicioLinha),
                            String.valueOf(fimLinha)
                    );

                    // Redireciona stdout/stderr dos filhos para o terminal do pai
                    pb.inheritIO();

                    Process p = pb.start();
                    processos.add(p);
                }

                // Aguarda todos os processos filhos terminarem
                for (Process p : processos) {
                    p.waitFor();
                }

                long fim = System.nanoTime();
                double tempo = (fim - inicio) / 1_000_000_000.0;

                String status = (rodada == 1) ? " (aquecimento — descartado)" : "";
                System.out.printf("  Rodada %d: %.4f s%s%n", rodada, tempo, status);
                log.printf("  Rodada %d: %.4f s%s%n", rodada, tempo, status);

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
        System.out.println("Resultados salvos em resultados_processos.txt");
    }
}
