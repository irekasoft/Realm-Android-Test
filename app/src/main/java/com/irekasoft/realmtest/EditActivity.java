package com.irekasoft.realmtest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.irekasoft.realmtest.model.Person;

import io.realm.Realm;

public class EditActivity extends Activity {

    private EditText mName;
    private EditText mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        mName = (EditText) findViewById(R.id.mName);
        mCity = (EditText) findViewById(R.id.mCity);
        Realm realm = Realm.getInstance(this);
        Person toEdit = realm.where(Person.class)
                .equalTo("id", getIntent().getStringExtra("person")).findFirst();
        mName.setText(toEdit.getName());
        mCity.setText(toEdit.getCity());

        mName.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mName, InputMethodManager.SHOW_IMPLICIT);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }


    public void update(View view) {
        Realm realm = Realm.getInstance(getApplicationContext());

        Person toEdit = realm.where(Person.class)
                .equalTo("id", getIntent().getStringExtra("person")).findFirst();

        realm.beginTransaction();
        toEdit.setCity(mCity.getText().toString());
        toEdit.setName(mName.getText().toString());
        realm.commitTransaction();

        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

        finish();
    }

    public void delete(View view) {

        Realm realm = Realm.getInstance(getApplicationContext());

        Person toEdit = realm.where(Person.class)
                .equalTo("id", getIntent().getStringExtra("person")).findFirst();

        realm.beginTransaction();
        toEdit.removeFromRealm();
        realm.commitTransaction();

        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();

        finish();

    }



    @Override
    public void finish() {
        super.finish();
        finishActivity(MainActivity.REQUEST_CODE);
    }
}
