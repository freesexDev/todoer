package io.freesexdev.todoer;

import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class HomeFragment extends ListFragment {

    String[] values = new String[]{"Feed the Cat", "Buy some milk", "Go to barber"};
    private ListView lv;
    private int layout = R.layout.fragment_home;
    private int listItemLayout = R.layout.list_item;
    private LayoutInflater inflater;
    private int[] to = new int[]{R.id.label};
    public static final String APP_PREFERENCES = "todolist";
    private SharedPreferences database;
    private SharedPreferences.Editor editor;


    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO: impelment GetString, and se thus string to textView
        database = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        View view = inflater.inflate(layout, container, false);

        String taskNames;
        String[] taskDescriptions = getResources().getStringArray(R.array.task_descriptions);

        ArrayList<Task> taskList = new ArrayList<Task>();

        for (int i = 0; i < taskNames.length; i++) {
            taskList.add(new Task(taskNames, taskDescriptions[i]));
        }

        setListAdapter(new TaskAdapter(getActivity(), listItemLayout, taskList));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    class Task {
        private String taskName;
        private String taskDesc;

        public Task(String name, String address) {
            taskDesc = name;
            taskName = address;
        }

        public String getName() {
            return taskDesc;
        }

        public void setName(String name) {
            taskDesc = name;
        }

        public String getAddress() {
            return taskName;
        }

        public void setAddress(String address) {
            taskName = address;
        }
    }

    class TaskAdapter extends ArrayAdapter<Task> {
        private ArrayList<Task> items;
        private TaxiViewHolder taxiHolder;

        public TaskAdapter(Context context, int tvResId, ArrayList<Task> items) {
            super(context, tvResId, items);
            this.items = items;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(listItemLayout, parent, false);
                taxiHolder = new TaxiViewHolder();
                taxiHolder.name = (TextView) v.findViewById(R.id.label);
                taxiHolder.description = (TextView) v.findViewById(R.id.desc);
                v.setTag(taxiHolder);
            } else taxiHolder = (TaxiViewHolder) v.getTag();

            Task task = items.get(pos);

            if (task != null) {
                taxiHolder.name.setText(task.getName());
                taxiHolder.description.setText(task.getAddress());
            }

            return v;
        }

        class TaxiViewHolder {
            TextView name;
            TextView description;
        }
    }
}
