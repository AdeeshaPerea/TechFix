package com.example.techfix.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.techfix.data.GalleryRepository;
import com.example.techfix.data.firebase.FirebaseAppointmentRepository;
import com.example.techfix.data.firebase.FirebaseBranchRepository;
import com.example.techfix.data.firebase.FirebaseRepairRepository;
import com.example.techfix.data.firebase.FirebaseServiceRepository;
import com.example.techfix.data.firebase.FirebaseSparePartRepository;
import com.example.techfix.data.firebase.FirebaseTechnicianRepository;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.model.BranchItem;
import com.example.techfix.model.GalleryItem;
import com.example.techfix.model.RepairItem;
import com.example.techfix.model.RepairServiceItem;
import com.example.techfix.model.SparePartItem;
import com.example.techfix.model.User;

import java.util.List;

public class AdminViewModel extends ViewModel {
    private final FirebaseAppointmentRepository appointmentRepository;
    private final FirebaseRepairRepository repairRepository;
    private final FirebaseTechnicianRepository technicianRepository;
    private final FirebaseBranchRepository branchRepository;
    private final FirebaseSparePartRepository sparePartRepository;
    private final FirebaseServiceRepository serviceRepository;
    private final GalleryRepository galleryRepository;

    public AdminViewModel() {
        appointmentRepository = FirebaseAppointmentRepository.getInstance();
        repairRepository = FirebaseRepairRepository.getInstance();
        technicianRepository = FirebaseTechnicianRepository.getInstance();
        branchRepository = FirebaseBranchRepository.getInstance();
        sparePartRepository = FirebaseSparePartRepository.getInstance();
        serviceRepository = FirebaseServiceRepository.getInstance();
        galleryRepository = GalleryRepository.getInstance();
    }

    public LiveData<List<AppointmentItem>> getAppointments() { return appointmentRepository.getAppointmentsLiveData(); }
    public LiveData<List<RepairItem>> getRepairs() { return repairRepository.getRepairsLiveData(); }
    public LiveData<List<User>> getTechnicians() { return technicianRepository.getTechniciansLiveData(); }
    public LiveData<List<BranchItem>> getBranches() { return branchRepository.getBranchesLiveData(); }
    public LiveData<List<SparePartItem>> getSpareParts() { return sparePartRepository.getSparePartsLiveData(); }
    public LiveData<List<RepairServiceItem>> getServices() { return serviceRepository.getServicesLiveData(); }
    public LiveData<List<GalleryItem>> getGalleryItems() { return galleryRepository.getGalleryItems(); }

    public void updateAppointmentStatus(String id, String status) { appointmentRepository.updateStatus(id, status, null); }
    public void assignTechnicianToAppointment(String aptId, String techId, String techName) { appointmentRepository.assignTechnician(aptId, techId, techName, null); }
    public void changeAppointmentBranch(String aptId, String branchId, String branchName) { appointmentRepository.assignTechnician(aptId, "", branchName, null); }

    public void addBranch(BranchItem branch) { branchRepository.addBranch(branch, null); }

    public void addTechnician(User tech) { technicianRepository.addTechnician(tech, null); }

    public void addSparePart(SparePartItem part) { sparePartRepository.addOrUpdatePart(part, null); }
    public void updateSparePart(SparePartItem part) { sparePartRepository.addOrUpdatePart(part, null); }

    public void addService(RepairServiceItem service) { serviceRepository.addOrUpdateService(service, null); }
    public void updateService(RepairServiceItem service) { serviceRepository.addOrUpdateService(service, null); }

    public void addGalleryItem(GalleryItem item) { galleryRepository.addGalleryItem(item); }
    public void deleteGalleryItem(String id) { galleryRepository.deleteGalleryItem(id); }
}
