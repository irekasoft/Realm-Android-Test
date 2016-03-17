package com.irekasoft.realmtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.irekasoft.realmtest.model.Person;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends Activity {


    ArrayList<Person> persons = new ArrayList<>();

    private ListView mListView;
    private MyListAdapter mAdapter;

    public static final int REQUEST_CODE = 0x1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.mListView);

        loadData();

        mAdapter = new MyListAdapter(persons, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

                Person p = persons.get(pos);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("person", p.getId());
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivity result", "");

        if (requestCode == REQUEST_CODE) {
            loadData();
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
        mAdapter.notifyDataSetChanged();

    }

    public void loadData() {

        persons.clear();

        Realm realm = Realm.getInstance(this);
        RealmResults<Person> query = realm.where(Person.class)
                .findAll();

        for (Person p : query) {
            if (p.isValid()) {
                persons.add(p);
            }
        }
    }

    public void refresh(View view) {
        loadData();
        mAdapter.notifyDataSetChanged();
    }

    public void add(View view) {

        Realm realm = Realm.getInstance(getApplicationContext());
        realm.beginTransaction();

        Person p = realm.createObject(Person.class);
        p.setCity("New City");
        p.setName("New Guy");
        p.setId(UUID.randomUUID().toString());
        realm.commitTransaction();
        persons.add(p);

        mAdapter.notifyDataSetChanged();


        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("person", p.getId());
        startActivity(intent);

    }



    public class MyListAdapter extends BaseAdapter{

        private ArrayList<Person> persons;
        private Context mContext;

        public MyListAdapter(ArrayList<Person> persons, Context context) {
            this.persons = persons;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return persons.size();
        }

        @Override
        public Object getItem(int position) {
            return persons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Person p = (Person) this.getItem(position);

            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_cell, parent, false);
                holder.name = (TextView) convertView.findViewById(R.id.mMainLabel);
                holder.city = (TextView) convertView.findViewById(R.id.mCityLabel);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.name.setText(p.getName());
            holder.city.setText(p.getCity());

            return convertView;

        }

//        public void notifyDataSetChanged(){
//
//        }

        private class ViewHolder {
            TextView name;
            TextView city;

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


}
