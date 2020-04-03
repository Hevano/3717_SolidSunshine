package ca.bcit.emptyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MeetupActivity extends AppCompatActivity {

    DatabaseReference ref ;
    FirebaseUser user;
    String meetupId;
    ArrayList<String> emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup);

        emails = new ArrayList<>();
        final ListView emailList = findViewById(R.id.emailList);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent i = getIntent();
        meetupId = i.getStringExtra("meetupId");

        ref = FirebaseDatabase.getInstance().getReference().child("MeetUp").child(user.getUid()).child(meetupId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MeetUp m = dataSnapshot.getValue(MeetUp.class);
                TextView meetLoc = findViewById(R.id.meetLoc);
                meetLoc.setText(m.getMeetLoc());
                TextView meetDate = findViewById(R.id.meetDate);
                meetDate.setText(m.getMeetDate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        ref.child("emails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emails.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String e = snapshot.getValue(String.class);
                    emails.add(e.replace(',','.'));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MeetupActivity.this, android.R.layout.simple_list_item_1, emails);
                emailList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        emailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String email = emails.get(position);
                showRemoveEmailDialog(email);
                return false;
            }
        });

        Button addEmailButton = findViewById(R.id.newEmailButton);
        addEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t = findViewById(R.id.newEmailText);
                final String emailText = t.getText().toString();

                final DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("emailLookup");
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(emailText.replace('.',',')).exists()){
                            addEmail(emailText, dataSnapshot.child(emailText.replace('.',',')).getValue().toString());
                            emails.add(emailText);
                            ((EditText) findViewById(R.id.newEmailText)).setText("");
                            Toast.makeText(MeetupActivity.this, getString(R.string.email_added, emailText), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MeetupActivity.this, getString(R.string.email_missing, emailText), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

            }
        });

    }

    private void addEmail(final String email, final String uid){

        String emailFormatted = email.replace('.',',');
        Log.println(Log.WARN, "NEW EMAIL", emailFormatted);
        DatabaseReference newEmail = ref.child("emails").child(uid);
        newEmail.setValue(emailFormatted);


        //Add entry to user's meetups
        //Shared user references are a simple Dictionary of meetup id and version number
        //Version number used to tell if there has been a change in the meetup
        DatabaseReference userMeetups = FirebaseDatabase.getInstance().getReference().child("MeetUp").child(uid).child("shared").child(meetupId);
        userMeetups.setValue(0);

        //Increment meetup version value
        final DatabaseReference ver = ref.child("version");
        final ValueEventListener versionIncr = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ver.setValue((long) dataSnapshot.getValue() + 1);
                } else {
                    ver.setValue(1);
                }
                ver.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ver.addValueEventListener(versionIncr);

    }

    private void showRemoveEmailDialog(final String email) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.remove_email_dialog, null);

        final Button btnConfirm = dialogView.findViewById(R.id.removeEmailConfirmBtn);
        final Button btnCancel = dialogView.findViewById(R.id.removeEmailCancelBtn);

        dialogBuilder.setTitle(getString(R.string.remove_email, email));

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(dialogView);
        alertDialog.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEmail(email);
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void removeEmail(final String email){
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("emailLookup").child(email.replace(".",","));
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("MeetUp").child(dataSnapshot.getValue().toString()).child("shared");
                d.removeValue();
                ref.child("emails").child(dataSnapshot.getValue().toString()).removeValue();
                emails.remove(email);
                Toast.makeText(MeetupActivity.this, getString(R.string.email_removed), Toast.LENGTH_LONG).show();
                user.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
