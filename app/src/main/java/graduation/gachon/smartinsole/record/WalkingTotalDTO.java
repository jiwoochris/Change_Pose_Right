package graduation.gachon.smartinsole.record;

import java.util.ArrayList;

public class WalkingTotalDTO {
    private WalkingDTO record ;
    private ArrayList<WalkingBluetoothDTO> leftRecord = new ArrayList<>();
    private ArrayList<WalkingBluetoothDTO> rightRecord = new ArrayList<>();

    public WalkingTotalDTO(WalkingDTO record, ArrayList<WalkingBluetoothDTO> leftRecord, ArrayList<WalkingBluetoothDTO> rightRecord) {
        this.record = record;
        this.leftRecord = leftRecord;
        this.rightRecord = rightRecord;
    }

    public WalkingDTO getRecord() {
        return record;
    }

    public ArrayList<WalkingBluetoothDTO> getLeftRecord() {
        return leftRecord;
    }

    public ArrayList<WalkingBluetoothDTO> getRightRecord() {
        return rightRecord;
    }
}
