package com.example.techfix.ui.tech;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.techfix.data.firebase.FirebaseRepairRepository;
import com.example.techfix.data.firebase.FirebaseSparePartRepository;
import com.example.techfix.model.RepairItem;
import com.example.techfix.model.SparePartItem;

import java.util.List;

public class TechViewModel extends ViewModel {
    private final FirebaseRepairRepository repairRepository;
    private final FirebaseSparePartRepository sparePartRepository;

    public TechViewModel() {
        repairRepository = FirebaseRepairRepository.getInstance();
        sparePartRepository = FirebaseSparePartRepository.getInstance();
    }

    public LiveData<List<RepairItem>> getRepairs() {
        return repairRepository.getRepairsLiveData();
    }

    public LiveData<List<SparePartItem>> getSparePartsCatalog() {
        return sparePartRepository.getSparePartsLiveData();
    }

    public RepairItem getRepairById(String id) {
        return repairRepository.getRepairById(id);
    }

    public void updateStatus(String repairId, String status) {
        repairRepository.updateStatus(repairId, status, null);
    }

    public void saveDiagnosis(String repairId, String summary, String problem, String recommended, int hours, String partsNote) {
        repairRepository.saveDiagnosis(repairId, summary, problem, recommended, hours, null);
    }

    public void addNote(String repairId, String author, String status, String text) {
        repairRepository.addRepairNote(repairId, author, status, text, null);
    }

    public void addSparePartUsed(String repairId, String partId, String partName, int qty, double unitPrice) {
        repairRepository.addSparePartUsed(repairId, partId, partName, qty, unitPrice, null);
    }

    public void uploadRepairImage(String repairId, String type, Uri imageUri, FirebaseRepairRepository.ImageUploadCallback callback) {
        repairRepository.uploadRepairImage(repairId, type, imageUri, callback);
    }

    public void addBeforePhoto(String repairId, String photoTag) {
        repairRepository.uploadRepairImage(repairId, "BEFORE", null, null);
    }

    public void addAfterPhoto(String repairId, String photoTag) {
        repairRepository.uploadRepairImage(repairId, "AFTER", null, null);
    }
}
