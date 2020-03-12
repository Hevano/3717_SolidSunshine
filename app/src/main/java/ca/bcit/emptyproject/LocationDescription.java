package ca.bcit.emptyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LocationDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_description);
        //displaying information
        Bundle bundle = getIntent().getExtras();
        String name  = bundle.getString("name");
        String link = bundle.getString("link");
        String type = bundle.getString("type");
        String loc = bundle.getString("location");
        TextView n = findViewById(R.id.name);
        n.setText(name);
        TextView a = findViewById(R.id.address);
        a.setText(loc);
        TextView t = findViewById(R.id.type);
        t.setText(type);
        TextView l = findViewById(R.id.link);
        l.setText(link);

        Button b = findViewById(R.id.chip);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(LocationDescription.this, SignUp.class);
                startActivity(i);
            }
        });
    }




}
