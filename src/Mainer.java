import javax.swing.*;
import java.util.ArrayList;

public  class Mainer {
    static ArrayList<String> numbers = new ArrayList<>();
    String eg;

    int queueIndex = 0;
    static int qu = 0;

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        MainLogic ml = new MainLogic() {

            @Override
            public void StartTheWork() {
                new Thread(main).start();
            }

            @Override
            public void addBSC() {
                BSS a = new BSS(main, main.a);
                new Thread(a);
                main.BSSList.add(a);
                System.out.println("added S26871Project03.BSC");
            }

            @Override
            public void stopBSC() {

            }

            @Override
            public String addNewQueues(String message, int Frequency) throws InterruptedException {
                VBD senders = main.VBDList.get(Main.Rand(main.VBDList.size() - 1));
                VBD receivers = main.VBDList.get(Main.Rand(main.VBDList.size() - 1));
                String sender;
                sender = senders.getAddress_Value();
                sender += receivers.getAddress_Value();
                Mainer.numbers.add(sender);
                main.queues.add(sender);
                VBDsender a = new VBDsender(main, qu, message);
                main.getQueues().put(qu, sender);
                new Thread(a).start();
                qu++;
                StringBuilder sb = new StringBuilder(sender);
                for (int i = 0; i < 6; i++) {
                    sb.deleteCharAt(i);
                }
                System.out.println(sender);
                return sender;
            }

            @Override
            public int getCounter() {
                return main.getCounter();
            }

            @Override
            public void delete_BTS() {
                int ind = main.BTSListSender.size() - 1;
                main.BTSListSender.get(Main.Rand(ind)).setFlag(false);
                main.BTSListSender.remove(ind);
            }

            @Override
            public int CheckHowManyBTSS() {
                return main.BTSListSender.size() - 1;
            }

            @Override
            public int CheckHowManyBTSR() {
                return main.BTSListReceiver.size() - 1;
            }

            @Override
            public void ClearTheQueueOf() {

            }
        };
        ml.StartTheWork();
        new Thread(new BTSS(ml)).start();
        SwingUtilities.invokeLater(() -> {
            Centre centre = new Centre(ml);
            centre.setVisible(true);
        });


    }
}
