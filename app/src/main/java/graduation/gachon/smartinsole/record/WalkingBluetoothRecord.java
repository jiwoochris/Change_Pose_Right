package graduation.gachon.smartinsole.record;

import java.util.ArrayList;

public class WalkingBluetoothRecord {
    public static ArrayList<WalkingBluetoothDTO> leftRecord = new ArrayList<>();
    public static ArrayList<WalkingBluetoothDTO> rightRecord = new ArrayList<>();

    public static ArrayList<WalkingBluetoothDTO> getLeftRecord() {
        return leftRecord;
    }

    public static ArrayList<WalkingBluetoothDTO> getRightRecord() {
        return rightRecord;
    }
}
