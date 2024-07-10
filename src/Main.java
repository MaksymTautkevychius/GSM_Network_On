import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public  class Main implements Runnable
    {
        private Map<Integer,String> Queues = new HashMap<>();
        private int counter=0;

        public int getCounter() {
            return counter;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

        public Map<Integer, String> getQueues() {
            return Queues;
        }
        public byte[] a = {0,1,2};

        public List<VRD> VRDList = new ArrayList<>();
        public List<BSS> BSSList = new ArrayList<>();
        public List<VBD> VBDList = new ArrayList<>();
        public List<BTS> BTSListSender = new ArrayList<>();
        public List<BTSReceiver> BTSListReceiver = new ArrayList<>();
        public List<String> queues = new ArrayList<>();
        private String massage = "s";
        public BlockingDeque<byte[]> BSSQueue = new LinkedBlockingDeque<>();
        BlockingQueue<String> BTSSenderQueue = new LinkedBlockingDeque<>();
        BlockingDeque<byte[]> BTSReceiverQueue = new LinkedBlockingDeque<>();

        public String[] PhoneNumbers = new String[6];

        public void StartTheWork()
        {
            new Thread(new Main()).start();
        }



        public void main(String[] args) {
            new Thread(new Main()).start();
        }

        public void run() {
            try {
                VBDStart();
                for (int i = 0; i < 4; i++) {
                    BSS a = new BSS(this,this.a);
                    BSSList.add(a);
                }
                // S26871Project03.BSSStarter a = new S26871Project03.BSSStarter(this);
                Thread.sleep(3000);
                System.out.println("S26871Project03.BTS added");
                CreateNewBTS();
                CreateReceiverSMS();
                for (String i : PhoneNumbers) {System.out.println(i + " added");}
                Thread.sleep(3000);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean isLast(List<BSS> list, BSS element) {
            if (list.isEmpty()) {return false;}
            BSS lastElement = list.get(list.size() - 1);
            return lastElement.equals(element);
        }
        public boolean isFirst(List<BSS> list, BSS element) {
            if (list.isEmpty()) {return false;}
            BSS firstElement = list.get(0);
            return firstElement.equals(element);
        }
        public boolean isTheres(List<BSS> list, BSS element) {
            if (list.isEmpty()) {return false;}
            BSS firstElement = list.get(0);
            return firstElement.equals(element);
        }
        public void SendSMS(String massage,String numbers) {
            this.setMassage(massage);
            StringBuilder senders= new StringBuilder();
            StringBuilder receivers= new StringBuilder();
            for (int i=0;i<6;i++)
            {
                senders.append(numbers.charAt(i));
            }
            for (int i=6;i<12;i++)
            {
                receivers.append(numbers.charAt(i));
            }
            String dataBTS = senders + receivers.toString() + massage;
            this.BTSSenderQueue.add(dataBTS);
            System.out.println("dataBTS: " + dataBTS);
        }


        public String getMassage() {
            return massage;
        }

        public String[] getPhoneNumbers() {
            return PhoneNumbers;
        }

        private int index;
        public List<VBD> getVBDList() {
            return VBDList;
        }
        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }


        public static int Rand(int length) {
            Random random = new Random();
            Set<Integer> generator = new HashSet<>();
            int rand;
            do {rand = random.nextInt(length) + 1;}
            while (generator.contains(rand));
            generator.add(rand);
            return rand;
        }
        public void VBDStart()
        {
            for (int i = 0; i < 6; i++) {
                VBD ister = new VBD(this, "N");
                VBDList.add(ister);
                String phoneNumber = ister.getAddress_Value();
                PhoneNumbers[i] = phoneNumber;
                new Thread(ister).start();
            }
        }
        public void CreateReceiverSMS()
        {
            BTSReceiver receiver =  new BTSReceiver(this,"R");
            new Thread(receiver).start();
            this.BTSListReceiver.add(receiver);
        }

        public void setMassage(String massage) {
            this.massage = massage;
        }



        public void CreateNewBTS()
        {
            BTS a = new BTS(this,"S");
            BTSListSender.add(a);
            new Thread(a).start();
        }
        public void removeBTS(BTS bts) {
            bts.setFlag(false);
            BTSListSender.remove(bts);
        }
    }


