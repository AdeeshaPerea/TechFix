package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.RepairServiceItem;

import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {
    private static ServiceRepository instance;

    private final MutableLiveData<List<RepairServiceItem>> servicesLiveData = new MutableLiveData<>();
    private final List<RepairServiceItem> serviceList = new ArrayList<>();

    private ServiceRepository() {
        serviceList.addAll(MockDataGenerator.getMockServices());
        servicesLiveData.setValue(new ArrayList<>(serviceList));
    }

    public static synchronized ServiceRepository getInstance() {
        if (instance == null) {
            instance = new ServiceRepository();
        }
        return instance;
    }

    public LiveData<List<RepairServiceItem>> getServices() {
        return servicesLiveData;
    }

    public RepairServiceItem getServiceById(String id) {
        for (RepairServiceItem s : serviceList) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public void addService(RepairServiceItem service) {
        service.setId("SERV_00" + (serviceList.size() + 1));
        serviceList.add(service);
        servicesLiveData.postValue(new ArrayList<>(serviceList));
    }

    public void updateService(RepairServiceItem updatedService) {
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).getId().equals(updatedService.getId())) {
                serviceList.set(i, updatedService);
                break;
            }
        }
        servicesLiveData.postValue(new ArrayList<>(serviceList));
    }

    public void deleteService(String id) {
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).getId().equals(id)) {
                serviceList.remove(i);
                break;
            }
        }
        servicesLiveData.postValue(new ArrayList<>(serviceList));
    }
}
