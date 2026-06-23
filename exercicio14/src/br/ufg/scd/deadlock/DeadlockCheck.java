package br.ufg.scd.deadlock;

/**
 * Programa auxiliar para testar, com tempo limite, um cenário com deadlock
 * e um cenário corrigido.
 */
public final class DeadlockCheck {
    private static final long PAUSE_MS = 50L;
    private static final long TIMEOUT_MS = 1_000L;

    private DeadlockCheck() {
        // Classe utilitária: não deve ser instanciada.
    }

    public static void main(String[] args) throws InterruptedException {
        testScenarioWithDeadlock();
        System.out.println();
        testScenarioWithoutDeadlock();
    }

    private static void testScenarioWithDeadlock() throws InterruptedException {
        Object lockA = new Object();
        Object lockB = new Object();

        Runnable task1 = () -> acquireLocks("Cenário com deadlock - Thread 1", lockA, lockB);
        Runnable task2 = () -> acquireLocks("Cenário com deadlock - Thread 2", lockB, lockA);

        runScenario("CENÁRIO 1: versão com deadlock", task1, task2, true);
    }

    private static void testScenarioWithoutDeadlock() throws InterruptedException {
        Object lockA = new Object();
        Object lockB = new Object();

        Runnable task1 = () -> acquireLocks("Cenário corrigido - Thread 1", lockA, lockB);
        Runnable task2 = () -> acquireLocks("Cenário corrigido - Thread 2", lockA, lockB);

        runScenario("CENÁRIO 2: versão corrigida", task1, task2, false);
    }

    private static void runScenario(
            String scenarioName,
            Runnable task1,
            Runnable task2,
            boolean daemonThreads) throws InterruptedException {

        System.out.println(scenarioName);

        Thread thread1 = new Thread(task1, "Thread-1");
        Thread thread2 = new Thread(task2, "Thread-2");

        // No cenário que trava, as threads são daemon para permitir que o programa de teste termine.
        thread1.setDaemon(daemonThreads);
        thread2.setDaemon(daemonThreads);

        thread1.start();
        thread2.start();

        thread1.join(TIMEOUT_MS);
        thread2.join(TIMEOUT_MS);

        if (thread1.isAlive() || thread2.isAlive()) {
            System.out.println("Resultado: DEADLOCK detectado por timeout.");
        } else {
            System.out.println("Resultado: programa terminou sem deadlock.");
        }
    }

    private static void acquireLocks(String taskName, Object firstLock, Object secondLock) {
        log(taskName + " tentando adquirir o primeiro lock");

        synchronized (firstLock) {
            log(taskName + " adquiriu o primeiro lock");
            pause();

            log(taskName + " tentando adquirir o segundo lock");
            synchronized (secondLock) {
                log(taskName + " adquiriu o segundo lock");
            }
        }
    }

    private static void pause() {
        try {
            Thread.sleep(PAUSE_MS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            log("interrompida durante a pausa");
        }
    }

    private static void log(String message) {
        System.out.printf("%s: %s.%n", Thread.currentThread().getName(), message);
    }
}
