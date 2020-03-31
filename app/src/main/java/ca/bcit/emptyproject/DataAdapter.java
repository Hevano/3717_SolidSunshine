package ca.bcit.emptyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends BaseAdapter {
    private Context context;
    private List<Data> dataList;
    private TextView tvLocation;
    private TextView tvName;
    private TextView tvType;
    private TextView tvWeb;
    private View vi;
    private static LayoutInflater inflater = null;

    public DataAdapter(Context context, List<Data> dataList){
        this.context = context;
        this.dataList = dataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return dataList.size(); }

    @Override
    public Object getItem(int i) { return i; }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //Populate the Listview
        final int pos = position;
        Data data = dataList.get(pos);

        View vi = inflater.inflate(R.layout.list_item_data, viewGroup, false);
        tvLocation = vi.findViewById(R.id.tvLocation);
        tvName = vi.findViewById(R.id.tvName);
        tvType = vi.findViewById(R.id.tvType);
        tvWeb = vi.findViewById(R.id.tvWeb);

        tvLocation.setText(data.getLOCATION());
        tvName.setText(data.getNAME());
        tvType.setText(data.getFACILITY_TYPE());
        tvWeb.setText(data.getWEBLINK());
        return vi;
    }
}

