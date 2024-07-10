public  class VBDsender implements Runnable {
    Main main;
    int queueIndex;
    boolean stop = true;
    String message;

    public VBDsender(Main main, int queueIndex, String message) {
        this.main = main;
        this.queueIndex = queueIndex;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            while (stop) {
                String queue = main.queues.get(queueIndex);
                main.SendSMS(this.message, queue);
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
