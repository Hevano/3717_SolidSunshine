package ca.bcit.emptyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Meeting extends AppCompatActivity {

    DatabaseReference ref ;
    String userid;
    FirebaseUser user;
    ListView v;
    ListView shared;
    List<MeetUp> lis ;
    List<String> meeetUpIds;
    List<MeetUp> sharedList;
    MeetUpAdapter sharedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        v = findViewById(R.id.meeting_list);
        shared = findViewById(R.id.shared_list);
        lis = new ArrayList<>();
        sharedList = new ArrayList<>();
        meeetUpIds = new ArrayList<>();
        user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        userid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("MeetUp").child(userid);


        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String meetId = meeetUpIds.get(position);

                Intent i = new Intent(Meeting.this, MeetupActivity.class);
                i.putExtra("meetupId", meetId);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lis.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    if(!studentSnapshot.getKey().equals("shared")){
                        MeetUp student = studentSnapshot.getValue(MeetUp.class);
                        meeetUpIds.add(studentSnapshot.getKey());
                        lis.add(student);
                    }

                }

                MeetUpAdapter adapter = new MeetUpAdapter(Meeting.this, lis);
                v.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        ref.child("shared").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sharedList.clear();
                List<String> foundShared = new ArrayList<>();
                Log.println(Log.ERROR, "SHARED", "Looping shared");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    foundShared.add(data.getKey());
                }
                Log.println(Log.ERROR, "SHARED", "Found: " + foundShared.size());
                searchMeetup(foundShared);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    /*
    
    */

    private void searchMeetup(final List<String> ids){
        Log.println(Log.ERROR, "SHARED", "searching meetup");
        final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("MeetUp");
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Log.println(Log.ERROR, "SHARED", "starting loop: "+ ids.get(0));
                    for(String id : ids){
                        Log.println(Log.ERROR, "SHARED", "Looping ids");
                        if(d.child(id).exists()){
                            Log.println(Log.ERROR, "SHARED", "ids exist");
                            sharedList.add(d.child(id).getValue(MeetUp.class));
                        }
                    }
                }
                sharedAdapter = new MeetUpAdapter(Meeting.this, sharedList);
                shared.setAdapter(sharedAdapter);
                r.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
