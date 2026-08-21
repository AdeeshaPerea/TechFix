package com.example.techfix.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.techfix.data.AppointmentRepository;
import com.example.techfix.data.BranchRepository;
import com.example.techfix.data.GalleryRepository;
import com.example.techfix.data.RepairRepository;
import com.example.techfix.data.ServiceRepository;
import com.example.techfix.data.SparePartRepository;
import com.example.techfix.data.TechnicianRepository;
import com.example.techfix.model.AppointmentItem;
import com.example.techfix.model.BranchItem;
import com.example.techfix.model.GalleryItem;
import com.example.techfix.model.RepairItem;
import com.example.techfix.model.RepairServiceItem;
import com.example.techfix.model.SparePartItem;
import com.example.techfix.model.User;

import java.util.List;

public class AdminViewModel extends ViewModel {
    private final AppointmentRepository appointmentRepository;
    private final RepairRepository repairRepository;
    private final TechnicianRepository technicianRepository;
    private final BranchRepository branchRepository;
    private final SparePartRepository sparePartRepository;
    private final ServiceRepository serviceRepository;
    private final GalleryRepository galleryRepository;

    public AdminViewModel() {
        appointmentRepository = AppointmentRepository.getInstance();
        repairRepository = RepairRepository.getInstance();
        technicianRepository = TechnicianRepository.getInstance();
        branchRepository = BranchRepository.getInstance();
        sparePartRepository = SparePartRepository.getInstance();
        serviceRepository = ServiceRepository.getInstance();
        galleryRepository = GalleryRepository.getInstance();
    }

    public LiveData<List<AppointmentItem>> getAppointments() { return appointmentRepository.getAppointments(); }
    public LiveData<List<RepairItem>> getRepairs() { return repairRepository.getRepairs(); }
    public LiveData<List<User>> getTechnicians() { return technicianRepository.getTechnicians(); }
    public LiveData<List<BranchItem>> getBranches() { return branchRepository.getBranches(); }
    public LiveData<List<SparePartItem>> getSpareParts() { return sparePartRepository.getSpareParts(); }
    public LiveData<List<RepairServiceItem>> getServices() { return serviceRepository.getServices(); }
    public LiveData<List<GalleryItem>> getGalleryItems() { return galleryRepository.getGalleryItems(); }

    public void updateAppointmentStatus(String id, String status) { appointmentRepository.updateStatus(id, status); }
    public void assignTechnicianToAppointment(String aptId, String techId, String techName) { appointmentRepository.assignTechnician(aptId, techId, techName); }
    public void changeAppointmentBranch(String aptId, String branchId, String branchName) { appointmentRepository.changeBranch(aptId, branchId, branchName); }

    public void addBranch(BranchItem branch) { branchRepository.addBranch(branch); }
    public void updateBranch(BranchItem branch) { branchRepository.updateBranch(branch); }

    public void addTechnician(User tech) { technicianRepository.addTechnician(tech); }
    public void updateTechnician(User tech) { technicianRepository.updateTechnician(tech); }

    public void addSparePart(SparePartItem part) { sparePartRepository.addPart(part); }
    public void updateSparePart(SparePartItem part) { sparePartRepository.updatePart(part); }

    public void addService(RepairServiceItem service) { serviceRepository.addService(service); }
    public void updateService(RepairServiceItem service) { serviceRepository.updateService(service); }
    public void deleteService(String id) { serviceRepository.deleteService(id); }

    public void addGalleryItem(GalleryItem item) { galleryRepository.addGalleryItem(item); }
    public void deleteGalleryItem(String id) { galleryRepository.deleteGalleryItem(id); }
}
