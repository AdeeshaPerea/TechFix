package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.BranchItem;

import java.util.ArrayList;
import java.util.List;

public class BranchRepository {
    private static BranchRepository instance;

    private final MutableLiveData<List<BranchItem>> branchesLiveData = new MutableLiveData<>();
    private final List<BranchItem> branchList = new ArrayList<>();

    private BranchRepository() {
        branchList.addAll(MockDataGenerator.getMockBranches());
        branchesLiveData.setValue(new ArrayList<>(branchList));
    }

    public static synchronized BranchRepository getInstance() {
        if (instance == null) {
            instance = new BranchRepository();
        }
        return instance;
    }

    public LiveData<List<BranchItem>> getBranches() {
        return branchesLiveData;
    }

    public BranchItem getBranchById(String id) {
        for (BranchItem b : branchList) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

    public void addBranch(BranchItem branch) {
        branch.setId("BRANCH_0" + (branchList.size() + 1));
        branchList.add(branch);
        branchesLiveData.postValue(new ArrayList<>(branchList));
    }

    public void updateBranch(BranchItem updatedBranch) {
        for (int i = 0; i < branchList.size(); i++) {
            if (branchList.get(i).getId().equals(updatedBranch.getId())) {
                branchList.set(i, updatedBranch);
                break;
            }
        }
        branchesLiveData.postValue(new ArrayList<>(branchList));
    }
}
