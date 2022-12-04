package graduation.gachon.smartinsole;

import android.Manifest;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import graduation.gachon.smartinsole.record.WalkingBluetoothDTO;
import graduation.gachon.smartinsole.record.WalkingBluetoothRecord;
import graduation.gachon.smartinsole.record.WalkingDTO;
import graduation.gachon.smartinsole.record.WalkingRecord;
import graduation.gachon.smartinsole.record.WalkingTotalDTO;


public class StartActivity extends AppCompatActivity implements SensorEventListener {

    private Sensor stepCountSensor;
    private SensorManager sensorManager;

    private TextView locationText;
    private TextView walkingText;
    private TextView timerText;
    private Button timerPause;
    private Button timerStop;

    private DatabaseReference mDatabase;

    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    long MillisecondTime = 0L;  // 스탑워치 시작 버튼을 누르고 흐른 시간
    long StartTime = 0L;        // 스탑워치 시작 버튼 누르고 난 이후 부터의 시간
    long TimeBuff = 0L;         // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간
    long UpdateTime = 0L;       // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간 + 시작 버튼 누르고 난 이후 부터의 시간 = 총 시간


    // 현재 걸음 수
    private int currentSteps = 0;

    //흐른 시간
    public static String time;
    public static  boolean startFlag = false;
    //현재 날짜 및 시간
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    WalkingRecord walkingRecord = new WalkingRecord();

    public static boolean flag = false;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        flag = true;
        handler = new Handler();

        locationText = (TextView) findViewById(R.id.loactionText);
        walkingText = (TextView) findViewById(R.id.walkingText);

        timerText = (TextView) findViewById(R.id.timerText);
        timerPause = (Button) findViewById(R.id.timerPause);
        timerStop = (Button) findViewById(R.id.timerStop);

        // SystemClock.uptimeMillis()는 디바이스를 부팅한후 부터 쉰 시간을 제외한 밀리초를 반환
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        startLocationService();

        // 스탑워치 일시정시 버튼
        timerPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간
                if (flag) {
                    TimeBuff += MillisecondTime;
                    flag = !flag;
                    // Runnable 객체 제거
                    handler.removeCallbacks(runnable);
                } else {
                    // SystemClock.uptimeMillis()는 디바이스를 부팅한후 부터 쉰 시간을 제외한 밀리초를 반환
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    flag = !flag;
                }
            }
        });

        // 스탑워치 스탑 버튼
        timerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeBuff += MillisecondTime;
                // Runnable 객체 제거
                handler.removeCallbacks(runnable);
                ArrayList<WalkingTotalDTO> record = walkingRecord.getRecord();
                ArrayList<WalkingBluetoothDTO> rightRecord = WalkingBluetoothRecord.rightRecord;
                ArrayList<WalkingBluetoothDTO> leftRecord = WalkingBluetoothRecord.leftRecord;

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                mDatabase = FirebaseDatabase.getInstance().getReference();
//                데이터베이스에 정보 저장
                mDatabase.child(user.getUid())//유저이름
                        .child("record")
                        .child(record.get(0).getRecord().getTime().substring(0, 4))//연도
                        .child(record.get(0).getRecord().getTime().substring(5, 7))//월
                        .child(record.get(0).getRecord().getTime().substring(8, 10))//일
                        .child(record.get(0).getRecord().getTime().substring(10) + " ~ " + record.get(record.size() - 1).getRecord().getTime().substring(10))//시작 시간
                        .child("PD")
                        .setValue(record);//데이터
//
//                mDatabase.child(user.getUid())//유저이름
//                        .child("record")
//                        .child(record.get(0).getTime().substring(0, 4))//연도
//                        .child(record.get(0).getTime().substring(5, 7))//월
//                        .child(record.get(0).getTime().substring(8, 10))//일
//                        .child(record.get(0).getTime().substring(10) + " ~ " + record.get(record.size() - 1).getTime().substring(10))//시작 시간
//                        .child("BDLeft")
//                        .setValue(leftRecord);//데이터
//
//                mDatabase.child(user.getUid())//유저이름
//                        .child("record")
//                        .child(record.get(0).getTime().substring(0, 4))//연도
//                        .child(record.get(0).getTime().substring(5, 7))//월
//                        .child(record.get(0).getTime().substring(8, 10))//일
//                        .child(record.get(0).getTime().substring(10) + " ~ " + record.get(record.size() - 1).getTime().substring(10))//시작 시간
//                        .child("BDRight")
//                        .setValue(rightRecord);//데이터
                flag = !flag;
            }
        });

    }


    public Runnable runnable = new Runnable() {

        public void run() {

            if(!startFlag) {
                // 디바이스를 부팅한 후 부터 현재까지 시간 - 시작 버튼을 누른 시간
                MillisecondTime = SystemClock.uptimeMillis() - StartTime;

                // 스탑워치 일시정지 버튼 눌렀을 때의 총 시간 + 시작 버튼 누르고 난 이후 부터의 시간 = 총 시간
                UpdateTime = TimeBuff + MillisecondTime;

                Seconds = (int) (UpdateTime / 1000);

                Minutes = Seconds / 60;

                Seconds = Seconds % 60;

                MilliSeconds = (int) (UpdateTime % 1000);

                // TextView에 UpdateTime을 갱신해준다
                time = Minutes + ":" + String.format("%02d", Seconds);
                timerText.setText(time);

                handler.postDelayed(this, 0);
            }
        }

    };

    public void startLocationService() {

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 가장최근 위치정보 가져오기
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(StartActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {

            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null && !flag) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();


                locationText.setText("위치정보 : " + provider + "\n" +
                        "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n" +
                        "고도  : " + altitude);
            }
                // 위치정보를 원하는 시간, 거리마다 갱신해준다.
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000, //1초마다
                        0,
                        gpsLocationListener);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000,
                        0,
                        gpsLocationListener);

        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onLocationChanged(Location location) {

            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현

            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도

            locationText.setText("위치정보 : " + provider + "\n" + "위도 : " + longitude + "\n" + "경도 : " + latitude + "\n" + "고도 : " + altitude);

            walkingRecord.addRecord(new WalkingDTO(longitude, latitude, altitude, getTime(), time, currentSteps));

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };

    public void onStart() {
        super.onStart();
        if (stepCountSensor != null) {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {

            if (event.values[0] == 1.0f && flag) {
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                walkingText.setText(String.valueOf(currentSteps));
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getTime() {

        LocalDateTime now = LocalDateTime.now();

        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



}
