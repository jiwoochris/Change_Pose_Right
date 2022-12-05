package graduation.gachon.smartinsole.record;

import java.util.ArrayList;

public class WalkingRecord {

    ArrayList<WalkingTotalDTO> record = new ArrayList<>();

    public void addRecord(WalkingDTO walking){
        record.add(new WalkingTotalDTO(walking, WalkingBluetoothRecord.getLeftRecord(),WalkingBluetoothRecord.getRightRecord()));
        WalkingBluetoothRecord.clear();
    }

    public ArrayList<WalkingTotalDTO> getRecord() {
        return record;
    }
}


