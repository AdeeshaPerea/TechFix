package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.GalleryItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryRepository {
    private static GalleryRepository instance;

    private final MutableLiveData<List<GalleryItem>> galleryLiveData = new MutableLiveData<>();
    private final List<GalleryItem> galleryList = new ArrayList<>();

    private GalleryRepository() {
        galleryList.addAll(MockDataGenerator.getMockGallery());
        galleryLiveData.setValue(new ArrayList<>(galleryList));
    }

    public static synchronized GalleryRepository getInstance() {
        if (instance == null) {
            instance = new GalleryRepository();
        }
        return instance;
    }

    public LiveData<List<GalleryItem>> getGalleryItems() {
        return galleryLiveData;
    }

    public void addGalleryItem(GalleryItem item) {
        item.setId("GAL_00" + (galleryList.size() + 1));
        galleryList.add(item);
        galleryLiveData.postValue(new ArrayList<>(galleryList));
    }

    public void deleteGalleryItem(String id) {
        for (int i = 0; i < galleryList.size(); i++) {
            if (galleryList.get(i).getId().equals(id)) {
                galleryList.remove(i);
                break;
            }
        }
        galleryLiveData.postValue(new ArrayList<>(galleryList));
    }
}
