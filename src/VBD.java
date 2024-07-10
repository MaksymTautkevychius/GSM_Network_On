import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public  class VBD implements Runnable {
    private final Main main;
    public int g = 0;

    @Override
    public synchronized void run() {

        try {
            if (g == 0) {
                GenerateNumber();
                this.encodeTheSMSC(this.getAddress_Value(), this.getAddress());
                main.PhoneNumbers[main.getIndex()] = this.getAddress_Value();
                main.setIndex(main.getIndex() + 1);
                Thread.sleep(10);
                g++;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int SMSC_TypeOfNumber;
    private int SMSC_NumberingPlanIdentification;
    private String massage;
    private String address_Value = "";

    public String getType_of_Number() {
        return Type_of_Number;
    }

    public VBD(Main main, String type_of_Number) {
        super();
        this.main = main;
        Type_of_Number = type_of_Number;
    }

    private String Type_of_Number;
    private byte address;
    private static final String charObjects = "0123456789";

    public String getMassage() {
        return massage;

    }

    public static void writeByte(DataOutputStream write, byte value) throws IOException {
        write.write(value & 0xFF);
    }

    public void encodeTheSMSC(String Address_Value, byte Type_Of_Address_Address/*Or just address from vbd*/)/*number of S26871Project03.VBD*/ {
        String nameOfVBD = Address_Value + ".bin";
        VBD_type_ByteSend(this, "N", "R");
        try (DataOutputStream write = new DataOutputStream(new FileOutputStream(nameOfVBD))) {

            byte[] Address = Address_Value.getBytes(StandardCharsets.UTF_8);
            byte Address_length = (byte) ((Address.length / 2) + 2);
            writeByte(write, Address_length);
            writeByte(write, Type_Of_Address_Address);
            System.out.println(Address_Value);
            for (int i = 0; i < Address_Value.length(); i += 2) {
                char a = Address_Value.charAt(i);
                char b = 'F';
                if (i + 1 < Address_Value.length()) {
                    b = Address_Value.charAt(i + 1);
                }
                byte semiOctetByte = semiOctetByte(a, b);
                write.write(semiOctetByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void VBD_type_ByteSend(VBD object, String Type_Of_Number, String extnsn) {
        //System.out.println("Unknown/International/National Number/ Extension : U/I/N/E");

        switch (Type_Of_Number) {
            case "U":
                object.setSMSC_TypeOf_Number(0b1000);
                break;
            case "I":
                object.setSMSC_TypeOf_Number(0b1001);
                break;
            case "N":
                object.setSMSC_TypeOf_Number(0b1010);
                break;
            case "E":
                object.setSMSC_TypeOf_Number(0b1111);
                break;
            default:
                object.setSMSC_TypeOf_Number(0b1000);
                break;

        }
        //System.out.println("Unknown/ISDN/TelephoneNumberingPlan/ReservedForExtension : U/I/T/R");
        Type_Of_Number = extnsn;
        switch (Type_Of_Number) {
            case "U":
                object.setSMSC_NumberingPlanIdentification(0b0000);
                break;
            case "I":
                object.setSMSC_NumberingPlanIdentification(0b0001);
                break;
            case "R":
                object.setSMSC_NumberingPlanIdentification(0b1111);
                break;
            default:
                object.setSMSC_NumberingPlanIdentification(0b1111);
                break;
        }

        byte excluded = (byte) ((object.getSMSC_TypeOfNumber() << 4) | object.getSMSC_NumberingPlanIdentification());
        object.setAddress(excluded);
    }

    public void GenerateNumber() {

        String symbols = "3";
        address_Value += symbols;
        genStringLastDigits();
    }

    public void genStringLastDigits() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int randomI = random.nextInt(charObjects.length());
            char rChar = charObjects.charAt(randomI);
            sb.append(rChar);
        }
        address_Value += sb.toString();
    }

    public String getAddress_Value() {
        return address_Value;
    }

    public byte getAddress() {
        return address;
    }


    public void setAddress(byte address) {
        this.address = address;
    }

    public void setSMSC_TypeOf_Number(int SMSC_TypeOf_Nmber) {
        this.SMSC_TypeOfNumber = SMSC_TypeOf_Nmber;
    }

    public void setSMSC_NumberingPlanIdentification(int SMSC_NumberingPlanIdentification) {
        this.SMSC_NumberingPlanIdentification = SMSC_NumberingPlanIdentification;
    }

    public int getSMSC_NumberingPlanIdentification() {
        return SMSC_NumberingPlanIdentification;
    }

    public int getSMSC_TypeOfNumber() {
        return SMSC_TypeOfNumber;
    }

    private static byte semiOctetByte(char a, char b) {
        int v1 = Character.digit(a, 16);
        int v2 = Character.digit(b, 16);

        int semiOctet = (v1 << 4) | v2;
        return (byte) semiOctet;
    }
}
