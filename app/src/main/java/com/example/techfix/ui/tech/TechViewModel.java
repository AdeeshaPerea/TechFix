package com.example.techfix.ui.tech;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.techfix.data.RepairRepository;
import com.example.techfix.data.SparePartRepository;
import com.example.techfix.model.RepairItem;
import com.example.techfix.model.SparePartItem;

import java.util.List;

public class TechViewModel extends ViewModel {
    private final RepairRepository repairRepository;
    private final SparePartRepository sparePartRepository;

    public TechViewModel() {
        repairRepository = RepairRepository.getInstance();
        sparePartRepository = SparePartRepository.getInstance();
    }

    public LiveData<List<RepairItem>> getRepairs() {
        return repairRepository.getRepairs();
    }

    public LiveData<List<SparePartItem>> getSparePartsCatalog() {
        return sparePartRepository.getSpareParts();
    }

    public RepairItem getRepairById(String id) {
        return repairRepository.getRepairById(id);
    }

    public void updateStatus(String repairId, String status) {
        repairRepository.updateStatus(repairId, status);
    }

    public void saveDiagnosis(String repairId, String summary, String problem, String recommended, int hours, String partsNote) {
        repairRepository.saveDiagnosis(repairId, summary, problem, recommended, hours, partsNote);
    }

    public void addNote(String repairId, String author, String category, String text) {
        repairRepository.addNote(repairId, author, category, text);
    }

    public void addSparePartUsed(String repairId, String partId, String partName, int qty, double unitPrice) {
        repairRepository.addSparePartUsed(repairId, partId, partName, qty, unitPrice);
    }

    public void addBeforePhoto(String repairId, String photoTag) {
        repairRepository.addBeforePhoto(repairId, photoTag);
    }

    public void addAfterPhoto(String repairId, String photoTag) {
        repairRepository.addAfterPhoto(repairId, photoTag);
    }
}
