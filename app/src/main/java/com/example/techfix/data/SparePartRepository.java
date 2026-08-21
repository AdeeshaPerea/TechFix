package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.SparePartItem;

import java.util.ArrayList;
import java.util.List;

public class SparePartRepository {
    private static SparePartRepository instance;

    private final MutableLiveData<List<SparePartItem>> sparePartsLiveData = new MutableLiveData<>();
    private final List<SparePartItem> sparePartList = new ArrayList<>();

    private SparePartRepository() {
        sparePartList.addAll(MockDataGenerator.getMockSpareParts());
        sparePartsLiveData.setValue(new ArrayList<>(sparePartList));
    }

    public static synchronized SparePartRepository getInstance() {
        if (instance == null) {
            instance = new SparePartRepository();
        }
        return instance;
    }

    public LiveData<List<SparePartItem>> getSpareParts() {
        return sparePartsLiveData;
    }

    public SparePartItem getPartById(String id) {
        for (SparePartItem p : sparePartList) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void addPart(SparePartItem part) {
        part.setId("PART_00" + (sparePartList.size() + 1));
        sparePartList.add(part);
        sparePartsLiveData.postValue(new ArrayList<>(sparePartList));
    }

    public void updatePart(SparePartItem updatedPart) {
        for (int i = 0; i < sparePartList.size(); i++) {
            if (sparePartList.get(i).getId().equals(updatedPart.getId())) {
                sparePartList.set(i, updatedPart);
                break;
            }
        }
        sparePartsLiveData.postValue(new ArrayList<>(sparePartList));
    }

    public void updateQuantity(String partId, int newQty) {
        SparePartItem item = getPartById(partId);
        if (item != null) {
            item.setQuantity(newQty);
            sparePartsLiveData.postValue(new ArrayList<>(sparePartList));
        }
    }
}
