public  class VRD
        implements Runnable {
    Main main;

    public VRD(Main main) {
        this.main = main;
    }

    public void run() {
        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
