package com.offgrid.coupler.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.offgrid.coupler.R;
import com.offgrid.coupler.data.entity.User;
import com.offgrid.coupler.model.Info;
import com.offgrid.coupler.ui.contact.model.ContactListViewModel;

import java.util.List;


public class ContactListActivity extends AppCompatActivity implements Observer<List<User>>, View.OnClickListener {

    private ContactListAdapter contactListAdapter;
    private ContactListViewModel contactListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Info info = Info.getInstance(getIntent().getExtras());

        setContentView(R.layout.activity_contact_list);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(info.getTitle());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });

        findViewById(R.id.action_new_contact).setOnClickListener(this);

        contactListAdapter = new ContactListAdapter(this);

        contactListViewModel = new ViewModelProvider(this).get(ContactListViewModel.class);
        contactListViewModel.load();
        contactListViewModel.observe(this, ContactListActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_contact_list);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onChanged(List<User> users) {
        contactListAdapter.setUsers(users);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_new_contact) {
            Intent intent = new Intent(ContactListActivity.this, NewContactActivity.class);
            intent.putExtras(
                    new Info.BundleBuilder()
                            .withTitle("Add Contact")
                            .withText("This is new contact activity")
                            .withAction(Info.Action.add_contact)
                            .build()
            );
            startActivityForResult(intent, 1);
        }
    }
}
