package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.AppointmentItem;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {
    private static AppointmentRepository instance;

    private final MutableLiveData<List<AppointmentItem>> appointmentsLiveData = new MutableLiveData<>();
    private final List<AppointmentItem> appointmentList = new ArrayList<>();

    private AppointmentRepository() {
        appointmentList.addAll(MockDataGenerator.getMockAppointments());
        appointmentsLiveData.setValue(new ArrayList<>(appointmentList));
    }

    public static synchronized AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }

    public LiveData<List<AppointmentItem>> getAppointments() {
        return appointmentsLiveData;
    }

    public AppointmentItem getAppointmentById(String id) {
        for (AppointmentItem item : appointmentList) {
            if (item.getId().equals(id) || item.getAppointmentCode().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    public void updateStatus(String appointmentId, String newStatus) {
        AppointmentItem item = getAppointmentById(appointmentId);
        if (item != null) {
            item.setStatus(newStatus);
            appointmentsLiveData.postValue(new ArrayList<>(appointmentList));
        }
    }

    public void assignTechnician(String appointmentId, String techId, String techName) {
        AppointmentItem item = getAppointmentById(appointmentId);
        if (item != null) {
            item.setAssignedTechId(techId);
            item.setAssignedTechName(techName);
            appointmentsLiveData.postValue(new ArrayList<>(appointmentList));
        }
    }

    public void changeBranch(String appointmentId, String branchId, String branchName) {
        AppointmentItem item = getAppointmentById(appointmentId);
        if (item != null) {
            item.setBranchId(branchId);
            item.setBranchName(branchName);
            appointmentsLiveData.postValue(new ArrayList<>(appointmentList));
        }
    }
}
