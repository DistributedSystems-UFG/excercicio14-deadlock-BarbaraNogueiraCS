package br.ufg.scd.deadlock;

/**
 * Exemplo propositalmente incorreto: duas threads adquirem os mesmos locks
 * em ordens diferentes, criando uma situação de deadlock.
 */
public final class DeadlockExample {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();
    private static final long PAUSE_MS = 50L;

    private DeadlockExample() {
        // Classe utilitária: não deve ser instanciada.
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(DeadlockExample::runThreadOne, "Thread-1");
        Thread thread2 = new Thread(DeadlockExample::runThreadTwo, "Thread-2");

        thread1.start();
        thread2.start();

        // Aguarda indefinidamente. Na versão com deadlock, estas chamadas não retornam.
        thread1.join();
        thread2.join();
    }

    private static void runThreadOne() {
        synchronized (LOCK_A) {
            log("segurando LOCK_A");
            pause();

            log("aguardando LOCK_B");
            synchronized (LOCK_B) {
                log("adquiriu LOCK_B");
            }
        }
    }

    private static void runThreadTwo() {
        synchronized (LOCK_B) {
            log("segurando LOCK_B");
            pause();

            log("aguardando LOCK_A");
            synchronized (LOCK_A) {
                log("adquiriu LOCK_A");
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
