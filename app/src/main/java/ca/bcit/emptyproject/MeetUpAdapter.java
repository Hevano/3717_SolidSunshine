package ca.bcit.emptyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MeetUpAdapter extends BaseAdapter {
    private Context context;
    private List<MeetUp> meetUpList;
    private TextView meetupLocation;
    private TextView meetupDate;
    private View vi;
    private static LayoutInflater inflater = null;

    public MeetUpAdapter(Context context, List<MeetUp> dataList){
        this.context = context;
        this.meetUpList = dataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return meetUpList.size(); }

    @Override
    public Object getItem(int i) { return i; }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //Populate the Listview
        final int pos = position;
        MeetUp data = meetUpList.get(pos);
        View vi = inflater.inflate(R.layout.list_layout, viewGroup, false);
        meetupLocation = vi.findViewById(R.id.meetloc);
        meetupDate = vi.findViewById(R.id.meetdate);
        meetupLocation.setText(data.getMeetLoc());
        meetupDate.setText(data.getMeetDate());
        return vi;
    }
}
