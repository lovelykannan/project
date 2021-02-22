package com.example.health;

public class UsersData {
    int HeartRate,O2_Saturation,Temperature;
    String BP;
    public UsersData(){}
    public UsersData(int HeartRate, int O2_Saturation, int Temperature, String BP){
        this.HeartRate=HeartRate;
        this.O2_Saturation=O2_Saturation;
        this.Temperature=Temperature;
        this.BP=BP;
    }

    public int getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int heartRate) {
        HeartRate = heartRate;
    }

    public int getO2_Saturation() {
        return O2_Saturation;
    }

    public void setO2_Saturation(int o2_Saturation) {
        O2_Saturation = o2_Saturation;
    }

    public int getTemperature() {
        return Temperature;
    }

    public void setTemperature(int temperature) {
        Temperature = temperature;
    }

    public String getBP() {
        return BP;
    }

    public void setBP(String BP) {
        this.BP = BP;
    }
}
