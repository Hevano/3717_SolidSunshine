package ca.bcit.emptyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class LocationDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_description);
        Button b = findViewById(R.id.chip);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(LocationDescription.this, SignUp.class);
                startActivity(i);
            }
        });
    }




}
