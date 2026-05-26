# APS-SO📊 Benchmarking — Processos vs Threads
> Avaliação Parcial Semestral — Sistemas Operacionais SI03A  
> Centro Universitário de Brusque — **UNIFEBE**  
> Professor: Sidnei Baron
---
👥 Grupo
Nome	Curso
Leonardo Nascimento Da Cunha Rocha	Sistemas de Informação
Pedro Henrique Zimermann	Sistemas de Informação
Kauan Gonçalves Oliveira	Sistemas de Informação
---
🎯 Objetivo
Analisar empiricamente as diferenças entre Processos (múltiplas JVMs) e Threads (uma única JVM) em Java, utilizando a multiplicação de matrizes quadradas 1000×1000 como problema de benchmark.
Métricas analisadas:
⏱️ Tempo de execução
🖥️ Consumo de CPU
🧠 Consumo de memória
🔒 Nível de isolamento
---
📁 Estrutura do Projeto
```
.
├── LeitorMatriz.java       # Leitura das matrizes a partir de arquivo .txt
├── WorkerThread.java       # Thread que calcula um intervalo de linhas
├── MainThreads.java        # Versão B — benchmark com threads (2, 4 e 8)
├── ProcessoWorker.java     # Processo filho — calcula seu intervalo de linhas
├── MainProcessos.java      # Versão A — benchmark com processos (2, 4 e 8)
├── ValidadorResultado.java # Valida que threads e sequencial geram o mesmo resultado
├── matriz_1000.txt         # Matriz 1000×1000 usada nos testes
└── rodar_benchmark.bat     # Script Windows para compilar e executar tudo
```
---
▶️ Como executar
Pré-requisito
Java JDK 11 ou superior instalado e no PATH
Opção 1 — Script automático (Windows)
```bash
rodar_benchmark.bat
```
O script compila todos os arquivos, executa a validação e roda o benchmark completo.
Opção 2 — Manual
```bash
# 1. Compilar
javac LeitorMatriz.java WorkerThread.java ProcessoWorker.java MainThreads.java MainProcessos.java ValidadorResultado.java

# 2. Validar corretude
java ValidadorResultado

# 3. Benchmark — Threads
java MainThreads

# 4. Benchmark — Processos
java MainProcessos
```
---
📈 Resultados
Unidades	Threads (média)	Processos (média)
2	1,1995 s	2,4936 s
4	0,9843 s	3,1114 s
8	1,7443 s	5,6647 s
> 💡 Threads foram **2× a 3×** mais rápidas em todos os cenários.  
> O ponto ótimo foi **4 threads**, onde o paralelismo supera o overhead de escalonamento.
---
🔍 Metodologia
Cada cenário executado 5 vezes
Primeira execução descartada (aquecimento da JVM)
Resultado = média das rodadas 2 a 5
Tempo medido com `System.nanoTime()`
CPU e memória monitoradas via `resmon` (Monitor de Recursos do Windows)
---
🧪 Validação
A classe `ValidadorResultado.java` compara o resultado da multiplicação por threads com um cálculo sequencial de referência, elemento a elemento, confirmando a corretude da paralelização.
---
