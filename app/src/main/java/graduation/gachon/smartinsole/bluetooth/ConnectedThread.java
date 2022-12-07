package graduation.gachon.smartinsole.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import graduation.gachon.smartinsole.StartActivity;
import graduation.gachon.smartinsole.record.WalkingBluetoothDTO;
import graduation.gachon.smartinsole.record.WalkingBluetoothRecord;
import graduation.gachon.smartinsole.record.WalkingDTO;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final String deviceName;
    private int readBufferPosition = 0;
    private byte[] readBuffer = new byte[1024];

    public ConnectedThread(BluetoothSocket socket,String name) {
        mmSocket = socket;
        deviceName = name;
        System.out.println("socket = " + socket);
        System.out.println("name = " + name);
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    @Override
    public void run() {


        // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int byteAvailable = mmInStream.available();
                if (byteAvailable > 0) {
                    byte[] bytes = new byte[byteAvailable];
                    SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                    mmInStream.read(bytes);

                    int j=0;
                    ArrayList<String> acceleration = new ArrayList<>();
                    ArrayList<String> FsrSensor = new ArrayList<>();

                    for(int i=0; i<byteAvailable; i++){
                        byte tempByte = bytes[i];
                        if(tempByte == '\n'){
                            byte[] encodedBytes = new byte[readBufferPosition];
                            System.arraycopy(readBuffer,0,encodedBytes,0,encodedBytes.length);

                            String text = new String(encodedBytes,"UTF-8");
                            readBufferPosition=0;
//                            System.out.println(byteAvailable);
                            if(j<3){
                                acceleration.add(text);
                                j++;
                            }
                            else if (j>=3){
                                FsrSensor.add(text);
                                j++;
                            }
//                            System.out.println("StartActivity.startFlag"+j + StartActivity.flag);
                            if(j==7&&StartActivity.flag){
                                WalkingBluetoothDTO walkingBluetoothDTO = new WalkingBluetoothDTO(acceleration,FsrSensor,StartActivity.time);
                                if(deviceName.contains("Left")){
                                    WalkingBluetoothRecord.leftRecord.add(walkingBluetoothDTO);
                                }
                                else if (deviceName.contains("Right")){
                                    WalkingBluetoothRecord.rightRecord.add(walkingBluetoothDTO);
                                }
                            }
                            System.out.println(deviceName + " : text = " + text);
                        }
                        else{
                            readBuffer[readBufferPosition++] = tempByte;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String input) {
        byte[] bytes = input.getBytes();           //converts entered String into bytes
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}