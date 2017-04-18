package edu.cuny.foodproof;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by losty on 4/17/2017.
 */
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import static edu.cuny.foodproof.R.drawable.default_button;

public class Calender extends AppCompatActivity {
    CaldroidFragment caldroidFragment;
    FragmentTransaction t;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Standard onCreate() procedures
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_SHORT).show();
                TextView tView = (TextView) view;
                tView.setText("My Recipe");
            }
        });

        t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.Calender, caldroidFragment).commit();


    }
}

