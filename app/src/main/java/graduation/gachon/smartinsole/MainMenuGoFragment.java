package graduation.gachon.smartinsole;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import graduation.gachon.smartinsole.record.WalkingDTO;
import graduation.gachon.smartinsole.record.WalkingRecord;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuGoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuGoFragment extends Fragment {

    private Button startButton;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainMenuGoFragment() {
        // Required empty public constructor
    }


    public static MainMenuGoFragment newInstance(String param1, String param2) {
        MainMenuGoFragment fragment = new MainMenuGoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu_go, container, false);
        startButton = v.findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), StartActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

}