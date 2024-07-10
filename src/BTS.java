import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public  class BTS implements Runnable {
    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    private boolean flag = true;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    private final Main main;
    String receiverNumber;
    String senderNumber;
    String status;

    public BTS(Main main, String status) {
        this.main = main;
        this.status = status;
    }

    private byte[] sendingInfromation;

    @Override
    public void run() {
        while (flag) {

            try {
                Thread.sleep(3000);
                if (main.BTSSenderQueue.size() == 0) {
                    Thread.sleep(3000);
                    System.out.println("Waiting for Queue");
                } else {
                    if (main.BTSSenderQueue.size() / main.BSSList.size() > 3) {
                        main.CreateNewBTS();
                    }
                    if (status == "S") {

                        String data = main.BTSSenderQueue.take();
                        String sender = "";
                        String receiver = "";
                        String massage = "";
                        for (int i = 0; i < 6; i++) {
                            sender += data.charAt(i);
                            setSenderNumber(sender);
                        }
                        for (int i = 6; i < 12; i++) {
                            receiver += data.charAt(i);
                            setReceiverNumber(receiver);
                        }
                        for (int i = 12; i < data.length(); i++) {
                            massage += data.charAt(i);
                            setMassage(massage);
                        }
                        Thread.sleep(10);
                        byte[] a = this.TPDU(false, false, "cls_0", "X.400", "7bit", main, main.getMassage());
                        main.BSSQueue.add(a);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String massage;

    public byte[] TPDU(boolean uncompressed, boolean hasMassageClass, String Massage_Class, String interworking, String codingAlphabet, Main main, String message) throws FileNotFoundException {

        int l;
        try {
            DataInputStream reader = new DataInputStream(new FileInputStream(getSenderNumber() + ".bin"));
            l = reader.readByte();
            reader.readByte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] sends = new byte[l];
        try {
            DataInputStream reader = new DataInputStream(new FileInputStream(getSenderNumber() + ".bin"));
            reader.read(sends);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String address_value;
        String name = "+";
        String bin = "TPDU.bin";
        //name+=receiver.getAddress_Value();

        try {
            int max = Main.Rand(main.getVBDList().size() - 1);
            DataInputStream reader = new DataInputStream(new FileInputStream(main.PhoneNumbers[max] + ".bin"));
            int length = reader.readByte();
            reader.readByte();
            byte[] infoNum = new byte[length - 2];
            reader.read(infoNum);
            address_value = decodeSemiOctets(infoNum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        name += address_value;
        name += bin;
        try (DataOutputStream write = new DataOutputStream(new FileOutputStream(name)))//tp_da
        {//tp_da
            write.write(sends);
            byte FirstOctet = (byte) 0b00000001;//tp_da
            write.write(FirstOctet);//tp_da
            byte[] r = TP_DA(address_value + ".bin");//tp_da
            for (int i : r)//tp_da
            {//tp_da
                if (i != 0)//tp_da
                {//tp_da
                    write.write(i);//tp_da
                }//tp_da
            }//tp_da
            write.write(TP_PID(interworking, name));
            write.write(TP_DCS(uncompressed, hasMassageClass, Massage_Class, interworking));//dcs
            byte TP_VP__ValidityPeriod = 0b0000000; // simplified
            write.write(TP_VP__ValidityPeriod);
            byte UDL = TP__UDL(message, codingAlphabet);//UD LENGTH
            write.write(UDL);
            byte[] UD = TP__UD(message, codingAlphabet);//USERDATA
            write.write(UD);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int i = 0;
        byte[] info;
        try (DataInputStream read = new DataInputStream(new FileInputStream(name))) {
            while (true) {
                read.readByte();
                i++;
            }
        } catch (EOFException e) {
            info = new byte[i];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (DataInputStream read = new DataInputStream(new FileInputStream(name))) {
            read.read(info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return info;
    }

    public String getMassage() {
        return massage;
    }

    public byte FirstOctetData(boolean Submited) {
        byte FOctet;
        if (Submited) {
            FOctet = 0b00000001;
            return FOctet;
        } else FOctet = 0b00000000;
        return FOctet;
    }

    public byte[] TP_DA(String nameFromWhereToTake) throws IOException {
        DataInputStream read = new DataInputStream(new FileInputStream(nameFromWhereToTake));
        byte[] r = new byte[300];
        read.read(r);
        return r;
    }

    public byte TP_PID(String interworking_scheme, String name) {
        if (interworking_scheme == "") {
            return 0b00000000;
        } else {
            switch (interworking_scheme) {
                case "Implicit":
                    return 0b00100000;
                //break;
                case "telex":
                    return 0b00100001;
                //break;
                case "group_3_telefax":
                    return 0b00100010;
                //break;
                case "voice_telephone":
                    return 0b00100100;
                // break;
                case "ERMES":
                    return 0b00100101;
                //break;
                case "NationalPagingSystem":
                    return 0b00110110;
                //break;
                case "X.400":
                    return 0b00110001;
                //break;
                case "InternetElectronicMail":
                    return 0b00110010;
                //break;
            }
        }
        return 0b00000000;
    }

    public byte TP_DCS(boolean uncompressed, boolean hasMessageClass, String messageClass, String typeOfAlphabet) {
        int first = 0b00;
        int five;
        int fourth;
        int thirdSecond;
        int firstSecond;

        if (uncompressed) {
            five = 0b0;
        } else {
            five = 0b1;
        }

        if (hasMessageClass) {
            fourth = 0b1;
        } else {
            fourth = 0b0;
        }

        switch (messageClass) {
            case "Cls_1":
                firstSecond = 0b01;
                break;
            case "Cls_2":
                firstSecond = 0b10;
                break;
            case "Cls_3":
                firstSecond = 0b11;
                break;
            default:
                firstSecond = 0b00;
                break;
        }

        switch (typeOfAlphabet) {
            case "8_bit":
                thirdSecond = 0b01;
                break;
            case "UCS2":
                thirdSecond = 0b10;
                break;
            case "Reserved":
                thirdSecond = 0b11;
                break;
            default:
                thirdSecond = 0b00;
                break;
        }

        byte DCS;
        DCS = (byte) ((first << 6) | (five << 5) | (fourth << 4) | (thirdSecond << 2) | firstSecond);
        return DCS;
    }

    public byte TP__UDL(String massage, String type) {
        byte[] byteMassage;
        switch (type) {
            case "7bit":
                byteMassage = encodeGSM7Bit(massage);
                return (byte) byteMassage.length;
            case "UCS2":
                byteMassage = encodeUCS2(massage);
                return (byte) byteMassage.length;
            default:
                byteMassage = encode8Bit(massage);
                return (byte) byteMassage.length;
        }
    }

    public byte[] TP__UD(String massage, String type) {
        byte[] byteMassage;
        switch (type) {
            case "7bit":
                byteMassage = encodeGSM7Bit(massage);
                return byteMassage;
            case "UCS2":
                byteMassage = encodeUCS2(massage);
                return byteMassage;
            default:
                byteMassage = encode8Bit(massage);
                return byteMassage;
        }
    }

    private static byte semiOctetByte(char a, char b) {
        int v1 = Character.digit(a, 16);
        int v2 = Character.digit(b, 16);

        int semiOctet = (v1 << 4) | v2;
        return (byte) semiOctet;
    }

    public static String decode7Bit(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int shift = 0;
        int carryOver = 0;

        for (byte b : bytes) {
            int current = b & 0xFF;
            int charValue = ((current << shift) | carryOver) & 0x7F;
            sb.append((char) charValue);

            if (shift == 6) {
                carryOver = (current >> 1) & 0x7F;
                shift = 0;
            } else {
                carryOver = (current >> (shift + 1)) & 0x7F;
                shift++;
            }
        }

        return sb.toString();
    }

    public static String decodeUCS2(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; i += 2) {
            int highByte = bytes[i] & 0xFF;
            int lowByte = bytes[i + 1] & 0xFF;
            int charValue = (highByte << 8) | lowByte;
            sb.append((char) charValue);
        }

        return sb.toString();
    }

    public static String decodeSemiOctets(byte[] data) {
        StringBuilder sb = new StringBuilder();

        for (byte b : data) {
            int value = b & 0xFF;
            String semiOctet = String.format("%02X", value);
            sb.append(semiOctet);
        }

        return sb.toString();
    }

    /*S26871Project03.BSS last needs here*/
    public static byte[] encode8Bit(String massage) {
        return massage.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] encodeGSM7Bit(String message) {
        return message.getBytes(StandardCharsets.US_ASCII);
    }

    public static byte[] encodeUCS2(String inputString) {
        Charset ucs2Charset = StandardCharsets.UTF_16BE;
        return inputString.getBytes(ucs2Charset);
    }

    public static String decode8Bit(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String decodeGSM7Bit(byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public byte[] getSendingInfromation() {
        return sendingInfromation;
    }

    public void setSendingInfromation(byte[] sendingInfromation) {
        this.sendingInfromation = sendingInfromation;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

}
