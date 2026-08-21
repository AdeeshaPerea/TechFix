package com.example.techfix.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techfix.model.RepairItem;
import com.example.techfix.model.RepairNoteItem;

import java.util.ArrayList;
import java.util.List;

public class RepairRepository {
    private static RepairRepository instance;

    private final MutableLiveData<List<RepairItem>> repairsLiveData = new MutableLiveData<>();
    private final List<RepairItem> repairList = new ArrayList<>();

    private RepairRepository() {
        repairList.addAll(MockDataGenerator.getMockRepairs());
        repairsLiveData.setValue(new ArrayList<>(repairList));
    }

    public static synchronized RepairRepository getInstance() {
        if (instance == null) {
            instance = new RepairRepository();
        }
        return instance;
    }

    public LiveData<List<RepairItem>> getRepairs() {
        return repairsLiveData;
    }

    public RepairItem getRepairById(String id) {
        for (RepairItem item : repairList) {
            if (item.getId().equals(id) || item.getRepairCode().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    public void updateStatus(String repairId, String newStatus) {
        RepairItem item = getRepairById(repairId);
        if (item != null) {
            item.setStatus(newStatus);
            repairsLiveData.postValue(new ArrayList<>(repairList));
        }
    }

    public void saveDiagnosis(String repairId, String diagnosisSummary, String problemFound, String recommendedRepair, int estHours, String partsNote) {
        RepairItem item = getRepairById(repairId);
        if (item != null) {
            item.setDiagnosisSummary(diagnosisSummary);
            item.setProblemFound(problemFound);
            item.setRecommendedRepair(recommendedRepair);
            item.setEstimatedDurationHours(estHours);
            item.setRequiredPartsNotes(partsNote);
            if ("BOOKED".equals(item.getStatus()) || "CONFIRMED".equals(item.getStatus()) || "RECEIVED".equals(item.getStatus())) {
                item.setStatus("DIAGNOSING");
            }
            repairsLiveData.postValue(new ArrayList<>(repairList));
        }
    }

    public void addNote(String repairId, String author, String category, String noteText) {
        RepairItem item = getRepairById(repairId);
        if (item != null) {
            String noteId = "NOTE_" + (item.getNotes().size() + 1);
            String time = "Just now";
            item.getNotes().add(new RepairNoteItem(noteId, repairId, author, category, noteText, time));
            repairsLiveData.postValue(new ArrayList<>(repairList));
        }
    }

    public void addSparePartUsed(String repairId, String partId, String partName, int qty, double price) {
        RepairItem item = getRepairById(repairId);
        if (item != null) {
            item.getSparePartsUsed().add(new RepairItem.SparePartUsed(partId, partName, qty, price));
            double addedCost = qty * price;
            item.setEstimatedCost(item.getEstimatedCost() + addedCost);
            repairsLiveData.postValue(new ArrayList<>(repairList));
        }
    }

    public void addBeforePhoto(String repairId, String photoTag) {
        RepairItem item = getRepairById(repairId);
        if (item != null) {
            item.getBeforeImages().add(photoTag);
            repairsLiveData.postValue(new ArrayList<>(repairList));
        }
    }

    public void addAfterPhoto(String repairId, String photoTag) {
        RepairItem item = getRepairById(repairId);
        if (item != null) {
            item.getAfterImages().add(photoTag);
            repairsLiveData.postValue(new ArrayList<>(repairList));
        }
    }
}
