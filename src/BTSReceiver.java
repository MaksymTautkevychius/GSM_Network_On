import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public  class BTSReceiver implements Runnable {
    public byte[] getSenderNumber() {
        return senderNumber;
    }


    private final Main main;
    String receiverNumber;
    byte[] BreceiverNumber;
    byte[] senderNumber;
    byte[] lastMassage;
    String status;
    int count;

    public BTSReceiver(Main main, String status) {
        this.main = main;
        this.status = status;
    }

    private byte[] sendingInfromation;

    @Override
    public void run() {
        while (true) {

            try {
                if (main.BSSQueue.size() == 0) {
                    Thread.sleep(3000);
                    System.out.println("S26871Project03.BTSR wait");
                } else {
                    byte[] info = main.BSSQueue.take();
                    lastMassage = info;
                    this.TPDUReader(info);
                    Thread.sleep(3000);
                    this.TPDUSend();
                    main.setCounter(main.getCounter() + 1);

                    count++;
                    main.setCounter(count);
                    Thread.sleep(3000);
                }
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        System.out.println("Started writing end file");
                        try {
                            DataOutputStream write = new DataOutputStream(new FileOutputStream("endfile.bin"));
                            write.write(BreceiverNumber);
                            write.write(count);
                            write.write(lastMassage);

                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("ClosingFilePreparing");
                    }
                });


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void writeBinaryFile() {

    }

    private String massage;

    public byte[] TPDU(boolean uncompressed, boolean hasMassageClass, String Massage_Class, String interworking, String codingAlphabet, Main main, String massage) throws FileNotFoundException {

        int l;
        try {
            DataInputStream reader = new DataInputStream(new FileInputStream(getSenderNumber() + ".bin"));
            reader.readByte();
            l = reader.readByte();
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
        System.out.println(name);
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
            byte UDL = TP__UDL("massage", codingAlphabet);//UD LENGTH
            write.write(UDL);
            byte[] UD = TP__UD("massage", codingAlphabet);//USERDATA
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
            System.out.println("recieved data TPDU");
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
    public void TPDUReader(byte[] informationFromBSS) {
        String namer;
        int Type;
        try (DataOutputStream write = new DataOutputStream(new FileOutputStream("decoder.bin"))) {

            write.write(informationFromBSS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (DataInputStream read = new DataInputStream(new FileInputStream("decoder.bin"))) {

            int lengh = read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            String r;
            int length = read.readByte();
            byte[] semi = new byte[length - 2];
            for (int i = 0; i < length - 2; i++) {
                semi[i] = read.readByte();
            }// OriginatingAddressEnd at least 2 numbers
            r = decodeSemiOctets(semi);
            namer = r;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            DataOutputStream write = new DataOutputStream(new FileOutputStream("-" + namer + ".bin"));
            DataInputStream read = new DataInputStream(new FileInputStream("decoder.bin"));
            int lengthsender = read.readByte();
            write.write(lengthsender);
            byte TypeSender = read.readByte();
            write.write(TypeSender);
            byte[] semiSender = new byte[lengthsender - 2];
            for (int i = 0; i < lengthsender - 2; i++) {
                semiSender[i] = read.readByte();
            }
            senderNumber = semiSender;
            write.write(semiSender);
            String sender = decodeSemiOctets(semiSender);
            byte FirstOctet = read.readByte();
            if (FirstOctet == 0b00000001) {
                write.write((0b0000000) & 0xFF);//FirstOctet
            }
            byte length = read.readByte();
            write.write(length);
            String r;

            byte[] semi = new byte[length - 2];
            for (int i = 0; i < 3; i++) {
                semi[i] = read.readByte();
            }
            write.write(semi); // OriginatingAddressEnd at least 2 numbers
            r = decodeSemiOctets(semi);
            BreceiverNumber = semi;

            byte ProtocolIdentifier = read.readByte();
            write.write(ProtocolIdentifier);//PID

            byte CodingScheme = read.readByte();
            write.write(CodingScheme);//DCS
            /*Date*/
            LocalTime currentTime = LocalTime.now();
            ZoneId timeZone = ZoneId.systemDefault();
            String timeZoneString = timeZone.toString();
            LocalDate today = LocalDate.now();

            int year = today.getYear();
            int month = today.getMonthValue();
            int monthday = today.getDayOfMonth();
            int hour = currentTime.getHour();
            int minute = currentTime.getMinute();
            int second = currentTime.getSecond();

            int i = 0;

            String Year = Integer.toString(year);
            write.write(semiOctetByte(Year.charAt(i), Year.charAt(i + 1)));
            write.write(semiOctetByte(Year.charAt(i + 2), Year.charAt(i + 3)));

            write.write((byte) month);

            String day = Integer.toString(monthday);
            write.write(semiOctetByte(day.charAt(i), day.charAt(i + 1)));

            String Hour = Integer.toString(hour);
            write.write(semiOctetByte((char) 1, (char) 0));

            //String Minute = Integer.toString(minute);
            //write.write(semiOctetByte(Minute.charAt(i),Minute.charAt(i+1)));

            // String Second = Integer.toString(second);
            // write.write(semiOctetByte(Second.charAt(i),Second.charAt(i+1)));
            /*Date*/
            read.readByte();
            byte lengthOfMassage = read.readByte();
            write.write(lengthOfMassage);
            //System.out.println(lengthOfMassage + " length");
            byte[] Massage = new byte[lengthOfMassage + 2];
            for (int l = 0; l < lengthOfMassage; l++) {
                Massage[l] = read.readByte();
            }

            write.write(Massage);
            receiverNumber = namer;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void TPDUSend() {
        try {
            DataInputStream read = new DataInputStream(new FileInputStream("-" + receiverNumber + ".bin"));
            int lgh = read.readByte();
            read.readByte();
            byte[] semi = new byte[lgh - 2];
            for (int i = 0; i < 3; i++) {
                semi[i] = read.readByte();
            }
            String r = decodeSemiOctets(semi);
            senderNumber = semi;
            read.readByte();
            int lgh2 = read.readByte();
            byte[] semi2 = new byte[lgh2 - 2];
            for (int i = 0; i < 3; i++) {
                semi2[i] = read.readByte();
            }
            String r2 = decodeSemiOctets(semi2);
            BreceiverNumber = semi2;

            read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            read.readByte();
            /*Date*/

            byte lengthOfMassage = read.readByte();//lengthMSG
            int length = (int) lengthOfMassage;
            byte[] Massage = new byte[length];
            for (int l = 0; l < length; l++) {
                Massage[l] = read.readByte();
            }
            String decodedMessage;
            switch ("8bit") {
                case "7bit":
                    decodedMessage = decode7Bit(Massage);
                    break;
                case "8bit":
                    decodedMessage = decode8Bit(Massage);
                    break;
                case "UCS2":
                    decodedMessage = decodeUCS2(Massage);
                    break;
                default:
                    decodedMessage = decode7Bit(Massage);
                    break;
            }
            System.out.println(decodedMessage + " SMS to " + r2);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

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

}
