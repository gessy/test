package com.offgrid.coupler.ui.contact;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.offgrid.coupler.data.entity.User;
import com.offgrid.coupler.data.repository.UserRepository;


public class ContactInfoViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private LiveData<User> liveUser;

    public ContactInfoViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }


    void loadUser(String gid) {
        liveUser = userRepository.getUserByGid(gid);
    }

    User getUser() {
        return liveUser != null ? liveUser.getValue() : null;
    }

    void insertUser(User user) {
        userRepository.insert(user);
    }

    void delete() {
        if (liveUser != null && liveUser.getValue() != null) {
            userRepository.delete(liveUser.getValue().getId());
        }
    }

    void delete(String gid) {
        userRepository.delete(gid);
    }

    void delete(long id) {
        userRepository.delete(id);
    }

    void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super User> observer) {
        liveUser.observe(owner, observer);
    }
}
