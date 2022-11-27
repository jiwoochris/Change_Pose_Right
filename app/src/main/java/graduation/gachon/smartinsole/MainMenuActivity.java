package graduation.gachon.smartinsole;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainMenuGoFragment fragmentGo = new MainMenuGoFragment();
    private MainMenuProfileFragment fragmentProfile = new MainMenuProfileFragment();
    private MainMenuSettingFragment fragmentSetting = new MainMenuSettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED)
                        || (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                        || (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)

        ) {
            System.out.println(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACTIVITY_RECOGNITION));
            System.out.println(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION));
            System.out.println(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentGo, "0").commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    public boolean isLeft(int idx) {
        boolean flag = false;
        for (int i = 0; i < idx; i++) {
            if (fragmentManager.findFragmentByTag(i + "") != null){
                flag = true;
            }
        }

        return flag;
    }


    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (fragmentManager.findFragmentByTag(1 + "") != null){
                Log.d("frag", "1");
            }
            switch (menuItem.getItemId()) {
                case R.id.menu_go:
                    if(isLeft(0)) {
                        transaction
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.menu_frame_layout, fragmentGo, "0").commitAllowingStateLoss();
                    }

                    else {
                        transaction
                                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.menu_frame_layout, fragmentGo, "0").commitAllowingStateLoss();
                    }
                    break;
                case R.id.menu_setting:

                    if(isLeft(1)) {
                        transaction
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.menu_frame_layout, fragmentSetting, "1").commitAllowingStateLoss();
                    }

                    else {
                        transaction
                                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.menu_frame_layout, fragmentSetting, "1").commitAllowingStateLoss();
                    }

                    break;
                case R.id.menu_profile:

                    if(isLeft(2)) {
                        transaction
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.menu_frame_layout, fragmentProfile, "2").commitAllowingStateLoss();
                    }

                    else {
                        transaction
                                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                                .replace(R.id.menu_frame_layout, fragmentProfile, "2").commitAllowingStateLoss();
                    }

                    break;

            }

            return true;
        }
    }
}