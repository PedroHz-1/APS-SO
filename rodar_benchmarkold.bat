@echo off
REM ============================================================
REM  Script de Benchmark - APS Processos vs Threads
REM  Execute este arquivo na pasta onde estao os .java e .txt
REM ============================================================

echo Compilando todos os arquivos Java...
javac LeitorMatriz.java WorkerThread.java ProcessoWorker.java MainThreads.java MainProcessos.java ValidadorResultado.java

if errorlevel 1 (
    echo ERRO na compilacao. Verifique os arquivos.
    pause
    exit /b 1
)

echo Compilacao concluida!
echo.

echo ============================================================
echo  ETAPA 1: Validacao de corretude
echo ============================================================
java ValidadorResultado
echo.

echo ============================================================
echo  ETAPA 2: Benchmark - THREADS
echo  >> Abra o Monitor de Recursos (resmon) agora e filtre
echo     pelo processo "java.exe" para capturar CPU e RAM
echo ============================================================
pause
java MainThreads
echo.

echo ============================================================
echo  ETAPA 3: Benchmark - PROCESSOS
echo  >> Mantenha o resmon aberto para capturar pico de CPU/RAM
echo  >> Voce vera varios processos java.exe simultaneamente
echo ============================================================
pause
java MainProcessos
echo.

echo ============================================================
echo  CONCLUIDO! Resultados salvos em:
echo    resultados_threads.txt
echo    resultados_processos.txt
echo ============================================================
pause
