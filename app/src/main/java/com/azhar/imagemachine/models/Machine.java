package com.azhar.imagemachine.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tb_machine_data")
public class Machine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int machineId;

    @ColumnInfo(name = "machine_name")
    public String machineName;

    @ColumnInfo(name = "machine_type")
    public String machineType;

    @ColumnInfo(name = "maintenance_date")
    public String maintenanceDate;

    @ColumnInfo(name = "qr_code")
    public String qrCode;


    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}