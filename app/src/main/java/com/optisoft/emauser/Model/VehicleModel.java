package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

public class VehicleModel {
    @SerializedName("car_plate")
    private String carPlate;

    @SerializedName("car_status")
    private String carStatus;

    public String getCarPlate() { return carPlate; }
    public void setCarPlate(String cPlate) { carPlate = cPlate; }

    public String getCarStatus() { return carStatus; }
    public void setCarStatus(String cStatus) { carStatus = cStatus; }
}
