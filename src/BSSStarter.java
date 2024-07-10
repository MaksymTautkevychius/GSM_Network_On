public  class BSSStarter implements Runnable {
    private final Main main;

    public boolean stop = true;

    public BSSStarter(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (BSS i : main.BSSList) {
            Thread c = new Thread(i);
            c.start();
        }
    }
}
