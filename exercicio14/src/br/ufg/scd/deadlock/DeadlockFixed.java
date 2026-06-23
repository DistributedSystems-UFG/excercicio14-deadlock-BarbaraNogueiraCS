package br.ufg.scd.deadlock;

/**
 * Versão corrigida do exemplo de deadlock.
 *
 * Estratégia usada: ordenação global de locks.
 * Todas as threads adquirem os locks sempre na mesma ordem: primeiro LOCK_A,
 * depois LOCK_B. Assim, não há espera circular.
 */
public final class DeadlockFixed {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();
    private static final long PAUSE_MS = 50L;

    private DeadlockFixed() {
        // Classe utilitária: não deve ser instanciada.
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> executeTask("tarefa da Thread 1"), "Thread-1");
        Thread thread2 = new Thread(() -> executeTask("tarefa da Thread 2"), "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Programa finalizado sem deadlock.");
    }

    private static void executeTask(String taskName) {
        log("iniciando " + taskName);

        synchronized (LOCK_A) {
            log("segurando LOCK_A");
            pause();

            log("aguardando LOCK_B");
            synchronized (LOCK_B) {
                log("adquiriu LOCK_B");
                log("executando região crítica da " + taskName);
            }
        }

        log("finalizou " + taskName);
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
