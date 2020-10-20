package com.addy.ashok_neel;

public class ComplaintDataStore {

    String ComplaintId;
    String Complaint;
    String Identity;

    public ComplaintDataStore(){

    }

    public ComplaintDataStore(String complaintId, String complaint, String identity) {
        ComplaintId = complaintId;
        Complaint = complaint;
        Identity = identity;
    }

    public String getComplaintId() {
        return ComplaintId;
    }

    public String getComplaint() {
        return Complaint;
    }

    public String getIdentity() {
        return Identity;
    }
}
