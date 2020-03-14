package com.offgrid.coupler.ui.message;

import android.app.Application;
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
import com.offgrid.coupler.data.entity.Chat;
import com.offgrid.coupler.data.entity.User;
import com.offgrid.coupler.adapter.NewMessageContactListAdapter;
import com.offgrid.coupler.data.repository.ChatRepository;
import com.offgrid.coupler.model.Info;
import com.offgrid.coupler.model.view.ContactListViewModel;
import com.offgrid.coupler.ui.contact.NewContactActivity;

import java.util.List;

public class NewMessageActivity extends AppCompatActivity implements Observer<List<User>>, View.OnClickListener {

    private NewMessageContactListAdapter contactListAdapter;
    private ContactListViewModel contactListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Info info = Info.getInstance(getIntent().getExtras());

        setContentView(R.layout.activity_new_message);
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
        findViewById(R.id.action_new_group).setOnClickListener(this);

        contactListAdapter = new NewMessageContactListAdapter(this);

        contactListViewModel = new ViewModelProvider(this).get(ContactListViewModel.class);
        contactListViewModel.load();
        contactListViewModel.observe(this, NewMessageActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_contact_list);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
            Intent intent = new Intent(NewMessageActivity.this, NewContactActivity.class);
            intent.putExtras(
                    new Info.BundleBuilder()
                            .withTitle("Add Contact")
                            .withText("This is new contact activity")
                            .withAction(Info.Action.add_contact)
                            .build()
            );
            startActivityForResult(intent, 1);
        } else if (view.getId() == R.id.action_new_group) {
            new ChatRepository(getApplication()).insert(Chat.randGroupChat());
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }

}
