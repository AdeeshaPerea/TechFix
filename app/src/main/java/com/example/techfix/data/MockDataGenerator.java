package com.example.techfix.data;

import com.example.techfix.model.AppointmentItem;
import com.example.techfix.model.BranchItem;
import com.example.techfix.model.GalleryItem;
import com.example.techfix.model.RepairItem;
import com.example.techfix.model.RepairNoteItem;
import com.example.techfix.model.RepairServiceItem;
import com.example.techfix.model.SparePartItem;
import com.example.techfix.model.User;

import java.util.ArrayList;
import java.util.List;

public class MockDataGenerator {

    public static User getMockTechnician() {
        return new User(
                "TECH_001",
                "Nuwan Silva",
                "technician@techfix.com",
                "+94 77 123 4567",
                "TECH",
                "Mobile & Laptop Specialist",
                "BRANCH_01",
                "Colombo 03",
                "08:30 AM - 05:30 PM",
                5
        );
    }

    public static User getMockAdmin() {
        return new User(
                "ADMIN_001",
                "Nimal Jayasinghe",
                "admin@techfix.com",
                "+94 71 987 6543",
                "ADMIN",
                "Operations & Service Management",
                "BRANCH_01",
                "TechFix Colombo",
                "08:00 AM - 06:00 PM",
                0
        );
    }

    public static List<User> getMockTechnicians() {
        List<User> list = new ArrayList<>();
        list.add(getMockTechnician());
        list.add(new User("TECH_002", "Ruwan Bandara", "ruwan@techfix.com", "+94 75 234 5678", "TECH", "Laptop Hardware & OS Specialist", "BRANCH_02", "TechFix Galle", "08:30 AM - 05:30 PM", 3));
        list.add(new User("TECH_003", "Kavindu Silva", "kavindu@techfix.com", "+94 78 345 6789", "TECH", "Apple Devices Specialist", "BRANCH_01", "TechFix Colombo", "09:00 AM - 06:00 PM", 2));
        list.add(new User("TECH_004", "Tharindu Perera", "tharindu@techfix.com", "+94 76 456 7890", "TECH", "Android Motherboard Specialist", "BRANCH_02", "TechFix Galle", "08:30 AM - 05:30 PM", 1));
        return list;
    }

    public static List<BranchItem> getMockBranches() {
        List<BranchItem> list = new ArrayList<>();
        list.add(new BranchItem("BRANCH_01", "TechFix Colombo", "142 Galle Road, Colombo 03", "+94 11 234 5678", "08:00 AM - 07:00 PM", 6.9271, 79.8612, 8, 15));
        list.add(new BranchItem("BRANCH_02", "TechFix Galle", "88 Main Street, Galle Fort", "+94 91 223 3445", "08:30 AM - 06:00 PM", 6.0535, 80.2210, 5, 9));
        return list;
    }

    public static List<RepairItem> getMockRepairs() {
        List<RepairItem> list = new ArrayList<>();

        // Card 1: iPhone 13 • Screen Replacement (Kasun Perera • #TF-2026-00125, URGENT, Drop-off 9:00 AM • Est. 1.5 hrs)
        RepairItem r1 = new RepairItem(
                "REP_001",
                "TF-2026-00125",
                "Kasun Perera",
                "+94 77 123 4567",
                "iPhone 13",
                "A2633",
                "Cracked front glass digitizer and touch response drop.",
                "Screen Replacement",
                "High",
                "URGENT",
                "2026-08-22",
                "9:00 AM",
                "Colombo 03",
                "BRANCH_01",
                "TECH_001",
                "Nuwan Silva",
                32000.0
        );
        r1.setEstimatedDurationHours(1);
        r1.setDiagnosisSummary("Outer digitizer fractured; inner OLED intact.");
        r1.getSparePartsUsed().add(new RepairItem.SparePartUsed("PART_008", "iPhone 13 OEM Screen", 1, 32000.0));
        r1.getNotes().add(new RepairNoteItem("NOTE_1", "REP_001", "Nuwan Silva", "DIAGNOSING", "Phone received at counter. Initial check passed.", "9:15 AM"));
        list.add(r1);

        // Card 2: Dell XPS 13 • Battery Issue (Ishara Silva • #TF-2026-00126, IN PROGRESS, Drop-off 10:30 AM • Est. 45 min)
        RepairItem r2 = new RepairItem(
                "REP_002",
                "TF-2026-00126",
                "Ishara Silva",
                "+94 71 987 6543",
                "Dell XPS 13",
                "9315",
                "Battery swelling and rapidly discharging.",
                "Battery Issue",
                "Medium",
                "IN PROGRESS",
                "2026-08-22",
                "10:30 AM",
                "Colombo 03",
                "BRANCH_01",
                "TECH_001",
                "Nuwan Silva",
                22500.0
        );
        r2.setEstimatedDurationHours(1);
        list.add(r2);

        // Card 3: Samsung A54 • Water Damage (Dilani Fernando • #TF-2026-00127, PENDING, Drop-off 1:00 PM • Est. 2 hrs)
        RepairItem r3 = new RepairItem(
                "REP_003",
                "TF-2026-00127",
                "Dilani Fernando",
                "+94 75 334 4556",
                "Samsung A54",
                "SM-A546B",
                "Accidental liquid submersion. No power on.",
                "Water Damage",
                "High",
                "PENDING",
                "2026-08-22",
                "1:00 PM",
                "Colombo 03",
                "BRANCH_01",
                "TECH_001",
                "Nuwan Silva",
                15000.0
        );
        r3.setEstimatedDurationHours(2);
        list.add(r3);

        // Card 4: MacBook Pro 16 • Trackpad Issue
        RepairItem r4 = new RepairItem(
                "REP_004",
                "TF-2026-00128",
                "Dilini Wickramasinghe",
                "+94 78 445 5667",
                "MacBook Pro 16",
                "A2485",
                "Haptic feedback unresponsive on bottom left click.",
                "Trackpad Issue",
                "Urgent",
                "IN PROGRESS",
                "2026-08-21",
                "2:30 PM",
                "Colombo 03",
                "BRANCH_01",
                "TECH_001",
                "Nuwan Silva",
                28000.0
        );
        r4.setEstimatedDurationHours(1);
        list.add(r4);

        // Card 5: Google Pixel 7 • Charging Port
        RepairItem r5 = new RepairItem(
                "REP_005",
                "TF-2026-00129",
                "Kasun Jayawardena",
                "+94 76 556 6778",
                "Google Pixel 7",
                "GVU6C",
                "USB-C cable disconnects loosely.",
                "Charging Port",
                "Medium",
                "COMPLETED",
                "2026-08-20",
                "4:00 PM",
                "Colombo 03",
                "BRANCH_01",
                "TECH_001",
                "Nuwan Silva",
                9500.0
        );
        r5.setEstimatedDurationHours(1);
        list.add(r5);

        return list;
    }

    public static List<AppointmentItem> getMockAppointments() {
        List<AppointmentItem> list = new ArrayList<>();
        list.add(new AppointmentItem("APT_001", "APT-8821", "Anura Kumara", "+94 77 999 1111", "anura@gmail.com", "MacBook Air M2", "Screen Replacement", "2026-08-25", "09:30 AM", "BRANCH_01", "TechFix Colombo", "TECH_001", "Nuwan Silva", "PENDING", "Display flickers when opening hinge past 90 degrees."));
        list.add(new AppointmentItem("APT_002", "APT-8822", "Mahesh Mendis", "+94 71 888 2222", "mahesh@gmail.com", "Xiaomi 13 Pro", "Battery Replacement", "2026-08-25", "10:30 AM", "BRANCH_02", "TechFix Galle", "TECH_002", "Ruwan Bandara", "CONFIRMED", "Battery drains from 100 to 0 in 3 hours."));
        list.add(new AppointmentItem("APT_003", "APT-8823", "Nirosha Perera", "+94 75 777 3333", "nirosha@gmail.com", "Lenovo ThinkPad X1", "Keyboard Replacement", "2026-08-26", "01:15 PM", "BRANCH_01", "TechFix Colombo", "", "Unassigned", "PENDING", "Spacebar and Shift key non-responsive after coffee spill."));
        list.add(new AppointmentItem("APT_004", "APT-8824", "Sahan Wickrama", "+94 76 666 4444", "sahan@gmail.com", "iPad Air 5th Gen", "Glass Replacement", "2026-08-27", "11:00 AM", "BRANCH_01", "TechFix Colombo", "TECH_003", "Kavindu Silva", "CONFIRMED", "Front screen glass cracked on top left corner."));
        return list;
    }

    public static List<SparePartItem> getMockSpareParts() {
        List<SparePartItem> list = new ArrayList<>();
        list.add(new SparePartItem("PART_001", "Samsung A54 Display Assembly", "Mobile Display", "Samsung Galaxy A54", 4, 25000.0, 2));
        list.add(new SparePartItem("PART_002", "iPhone 14 Pro OEM Battery", "Battery", "iPhone 14 Pro", 1, 18500.0, 3));
        list.add(new SparePartItem("PART_003", "MacBook Air M2 Keyboard Deck", "Keyboard", "MacBook Air M2", 0, 22000.0, 2));
        list.add(new SparePartItem("PART_004", "Universal Laptop Thermal Paste Pack", "Cooling", "Universal Laptop", 18, 3500.0, 5));
        list.add(new SparePartItem("PART_005", "Google Pixel 7 USB-C Port Flex", "Flex Cable", "Google Pixel 7", 8, 6500.0, 2));
        list.add(new SparePartItem("PART_006", "Dell XPS 15 Battery Pack 86Wh", "Battery", "Dell XPS 15", 3, 28000.0, 2));
        return list;
    }

    public static List<RepairServiceItem> getMockServices() {
        List<RepairServiceItem> list = new ArrayList<>();
        list.add(new RepairServiceItem("SERV_001", "Screen Replacement", "Mobile", 25000.0, "2 Hours", "Full OEM display panel replacement with 6 months warranty."));
        list.add(new RepairServiceItem("SERV_002", "Battery Replacement", "Mobile", 12000.0, "1 Hour", "Genuine battery cell replacement with safety calibration."));
        list.add(new RepairServiceItem("SERV_003", "Windows Installation", "Laptop", 5000.0, "1.5 Hours", "Clean Windows 11 installation with updated hardware drivers & tools."));
        list.add(new RepairServiceItem("SERV_004", "Motherboard Micro-Soldering", "Mobile/Laptop", 35000.0, "1 Day", "Logic board trace repair, power IC swap, and short circuit diagnostic."));
        list.add(new RepairServiceItem("SERV_005", "Thermal Paste & Deep Clean", "Laptop", 4500.0, "1 Hour", "Fan overhaul, dust extraction, and Arctic MX-4 thermal compound application."));
        return list;
    }

    public static List<GalleryItem> getMockGallery() {
        List<GalleryItem> list = new ArrayList<>();
        list.add(new GalleryItem("GAL_001", "Shattered Screen Restored", "Mobile", "Samsung Galaxy A54 display replaced to factory perfection.", "Shattered AMOLED", "Pristine Display", "2026-08-18"));
        list.add(new GalleryItem("GAL_002", "Corroded Motherboard Revival", "Laptop", "Micro-soldering repair on liquid damaged Dell XPS motherboard.", "Severe Corrosion", "Cleaned & Functioning Board", "2026-08-19"));
        list.add(new GalleryItem("GAL_003", "Swollen iPhone Battery Replacement", "Mobile", "Safely replaced high-risk swollen battery on iPhone 14 Pro.", "Damaged Battery", "New OEM Battery", "2026-08-20"));
        return list;
    }
}
