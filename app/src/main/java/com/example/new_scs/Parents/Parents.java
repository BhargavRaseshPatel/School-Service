package com.example.new_scs.Parents;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.new_scs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class Parents extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_parents, new ChildrenHomework());
        transaction.commit();

        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.bottom);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.calendar:
                        transaction.replace(R.id.layout_parents, new ChildrenCalendar());
                        break;
                    case R.id.attendance:
                        transaction.replace(R.id.layout_parents, new ChildrenAttendance());
                        break;
                    case R.id.homework:
                        transaction.replace(R.id.layout_parents, new ChildrenHomework());
                        break;
                    case R.id.profile:
                        transaction.replace(R.id.layout_parents, new ChildrenProfile());
                        break;
                    case R.id.time_table:
                        transaction.replace(R.id.layout_parents, new ChildrenTimetable());
                        break;
                }
                transaction.commit();
                return true;
            }
        });
        bnv.getMenu().findItem(R.id.homework).setChecked(true);
    }
}