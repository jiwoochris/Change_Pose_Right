package graduation.gachon.smartinsole.record;


import java.util.ArrayList;

public class WalkingBluetoothDTO {
    private ArrayList<String> acceleration;
    private ArrayList<String> fsr;
    private String time;
    public WalkingBluetoothDTO(ArrayList<String> acceleration, ArrayList<String> fsr, String time) {
        this.acceleration = acceleration;
        this.fsr = fsr;
        this.time = time;
    }

    @Override
    public String toString() {
        return "WalkingBluetoothDTO{" +
                "acceleration=" + acceleration +
                ", fsr=" + fsr +
                ", time='" + time + '\'' +
                '}';
    }

    public ArrayList<String> getAcceleration() {
        return acceleration;
    }

    public ArrayList<String> getFsr() {
        return fsr;
    }

    public String getTime() {
        return time;
    }
}
