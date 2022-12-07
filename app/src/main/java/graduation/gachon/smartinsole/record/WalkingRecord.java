package graduation.gachon.smartinsole.record;

import java.util.ArrayList;

public class WalkingRecord {

    ArrayList<WalkingDTO> record = new ArrayList<>();

    public void addRecord(WalkingDTO walking){
        record.add(walking);
//        WalkingBluetoothRecord.clear();
//        WalkingBluetoothRecord.rightRecord.clear();
//        WalkingBluetoothRecord.leftRecord.clear();

    }

    public ArrayList<WalkingDTO> getRecord() {
        return record;
    }
}


