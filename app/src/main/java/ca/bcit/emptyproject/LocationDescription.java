package ca.bcit.emptyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LocationDescription extends AppCompatActivity {

    MeetUp meet;
    TextView n;
    TextView a;
    TextView t;
    TextView l;
    DatabaseReference ref ;
    FirebaseUser user;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_description);
        //displaying information
        Bundle bundle = getIntent().getExtras();
        String name  = bundle.getString("name");
        String link = bundle.getString("link");
        String type = bundle.getString("type");
        final String loc = bundle.getString("location");
        n = findViewById(R.id.name);
        n.setText(name);
        a = findViewById(R.id.address);
        a.setText(loc);
        t = findViewById(R.id.type);
        t.setText(type);
        l = findViewById(R.id.link);
        l.setText(link);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userid =user.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("MeetUp");

        ImageButton goTo = findViewById(R.id.showOnMapButton);
        goTo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(LocationDescription.this, MapsActivity.class);
                i.putExtra("Address", loc);
                startActivity(i);
            }
        });
    }



    public void meet_up(View view) {

        CalendarView cv = findViewById(R.id.calendarView);
        SimpleDateFormat ss = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(cv.getDate());
        String currentdate= ss.format(date);
        meet = new MeetUp();
        meet.setMeetDate(currentdate);
        meet.setMeetLoc(a.getText().toString());
        meet.setMeetLoc(n.getText().toString());
        meet.setMeetType(t.getText().toString());
        meet.setMeetWeb(l.getText().toString());
        meet.setVersion(1);
        ref.child(userid).push().setValue(meet);
        Toast toast = Toast.makeText(LocationDescription.this, "Meet Up created Successfully", Toast.LENGTH_SHORT);
        toast.show();
    }
}
