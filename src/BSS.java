import java.util.List;

public  class BSS implements Runnable {
    private final Main main;
    private byte[] Massage;
    boolean flaglockerLast = false;
    boolean flagLockerCentre = false;
    boolean flagLockerStart = false;
    public boolean notstoped;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index = 0;
    private boolean stopper = true;

    public BSS(Main main, byte[] Massage) {
        super();
        this.main = main;
        this.Massage = Massage;
    }

    synchronized public void run() {
        int ind = main.BSSList.indexOf(this);
        System.out.println(ind);
        try {
            if (main.isFirst(main.BSSList, this) && main.BSSQueue.size() > 0) {
                setMassage(main.BSSQueue.take());
                System.out.println("start bss");
            } else {
                while (getMassage() == null) {
                    Thread.sleep(2000);
                }
                if (isInCentre(main.BSSList, this)) {
                    main.BSSList.get(ind + 1).setMassage(this.getMassage());
                    this.setMassage(null);
                    System.out.println("middle: " + ind);
                }
                if (main.isLast(main.BSSList, this)) {
                    System.out.println("S26871Project03.BSS end");
                    main.BTSReceiverQueue.add(getMassage());
                }
            }

            Thread.sleep(RandomTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void processData(byte[] data) {
        // Process the data here
        System.out.println("Processing data: " + data);
    }

    public int RandomTime() {
        int max = 15000;
        int min = 3000;
        int RandomTime = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return RandomTime;
    }

    public static boolean PreLast(java.util.List<BSS> list, BSS object) {
        int lastIndex = list.size() - 1;
        if (lastIndex >= 1) {
            BSS preLastItem = list.get(lastIndex - 1);
            return preLastItem.equals(object);
        }
        return false;
    }

    private boolean isInCentre(List<BSS> list, Object obj) {
        int index = list.indexOf(obj);
        int lastIndex = list.size() - 1;

        return index > 0 && index < lastIndex;
    }

    public void sendMassage(BSS a, BSS b) {
        b.setMassage(a.getMassage());
    }

    public synchronized void setMassage(byte[] massage) {
        Massage = massage;
    }

    public synchronized byte[] getMassage() {
        return Massage;
    }
}
