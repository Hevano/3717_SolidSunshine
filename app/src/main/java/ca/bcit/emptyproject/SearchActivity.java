package ca.bcit.emptyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private final static String JSON_FILE = "data.json";
    private DataAdapter dataAdapter;
    List<Data> lis= new ArrayList<>();
    private ListView listView;
    EditText search ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ListView v = findViewById(R.id.searchResults);
        search = findViewById(R.id.search);
        //init();

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                     @Override
                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                         Intent i = new Intent(SearchActivity.this, LocationDescription.class);
                                         Data d = lis.get(position);
                                         //  Log.d("HEllo",d.getLOCATION());
                                         i.putExtra("location", d.getLOCATION());
                                         i.putExtra("type", d.getFACILITY_TYPE());
                                         i.putExtra("link",d.getWEBLINK());
                                         i.putExtra("name", d.getNAME());
                                         startActivity(i);
                                     }
                                 }
        );

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void init() {
        listView = findViewById(R.id.searchResults);
        dataAdapter = new DataAdapter(SearchActivity.this, getData(search.getText().toString().toLowerCase()));
        listView.setAdapter(dataAdapter);
    }

    /* Convert JSON String to BaseStudent Model via GSON */
    public List<Data> getData(String se) {
        String jsonString = getAssetsJSON(JSON_FILE);
        Log.d("MainActivity", "Json: " + jsonString);
        Gson gson = new Gson();
        BaseData baseStudent = gson.fromJson(jsonString, BaseData.class);
        List<Data> filter;
        List<Data> filtered = new ArrayList<>();
        filter = baseStudent.getData();
        //String se = search.getText().toString().toLowerCase();
        Data data;
        for(int x = 0; x <filter.size();x++){
            data = filter.get(x);
            String loc = data.getLOCATION().toLowerCase();
            String typ = data.getFACILITY_TYPE().toLowerCase();
            String name = data.getNAME().toLowerCase();
            String web = data.getWEBLINK().toLowerCase();
            if(!(!(loc.contains(se)) && !(typ.contains(se)) && !(name.contains(se)) && !(web.contains(se)))){
                filtered.add(data);
            }

        }
        lis = filtered;
        return filtered;
    }

    /* Get File in Assets Folder */
    public String getAssetsJSON(String fileName) {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void find(View view) {
        init();
        // lis = getData();
    }

    public void TagButtonClicked(View view) {
        Button b = (Button) view;
        String id = b.getText().toString().toLowerCase();
        listView = findViewById(R.id.searchResults);
        dataAdapter = new DataAdapter(SearchActivity.this, getData(id));
        listView.setAdapter(dataAdapter);

    }

}
