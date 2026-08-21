package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.User;

import java.util.ArrayList;
import java.util.List;

public class TechnicianRepository {
    private static TechnicianRepository instance;

    private final MutableLiveData<List<User>> techniciansLiveData = new MutableLiveData<>();
    private final List<User> technicianList = new ArrayList<>();

    private TechnicianRepository() {
        technicianList.addAll(MockDataGenerator.getMockTechnicians());
        techniciansLiveData.setValue(new ArrayList<>(technicianList));
    }

    public static synchronized TechnicianRepository getInstance() {
        if (instance == null) {
            instance = new TechnicianRepository();
        }
        return instance;
    }

    public LiveData<List<User>> getTechnicians() {
        return techniciansLiveData;
    }

    public User getTechnicianById(String id) {
        for (User u : technicianList) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public void addTechnician(User tech) {
        tech.setId("TECH_" + (technicianList.size() + 1));
        technicianList.add(tech);
        techniciansLiveData.postValue(new ArrayList<>(technicianList));
    }

    public void updateTechnician(User updatedTech) {
        for (int i = 0; i < technicianList.size(); i++) {
            if (technicianList.get(i).getId().equals(updatedTech.getId())) {
                technicianList.set(i, updatedTech);
                break;
            }
        }
        techniciansLiveData.postValue(new ArrayList<>(technicianList));
    }

    public void assignBranch(String techId, String branchId, String branchName) {
        User tech = getTechnicianById(techId);
        if (tech != null) {
            tech.setBranchId(branchId);
            tech.setBranchName(branchName);
            techniciansLiveData.postValue(new ArrayList<>(technicianList));
        }
    }
}
