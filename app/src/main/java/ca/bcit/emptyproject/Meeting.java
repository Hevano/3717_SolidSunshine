package ca.bcit.emptyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Meeting extends AppCompatActivity {

    DatabaseReference ref ;
    String userid;
    FirebaseUser user;
    ListView v;
    List<MeetUp> lis ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        v = findViewById(R.id.meeting_list);
        lis = new ArrayList<>();
        user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("MeetUp").child(userid);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lis.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    MeetUp student = studentSnapshot.getValue(MeetUp.class);
                    lis.add(student);
                }

                MeetUpAdapter adapter = new MeetUpAdapter(Meeting.this, lis);
                v.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
