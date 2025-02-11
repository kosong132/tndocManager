package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class EdiData {
    @Id
    private String salesOrderNumber;
    private String shipmentDate;
    private String formattedShipmentDateYYMMDD; // YYMMDD format
    private String shipmentTime;
    private String formattedShipmentTimeHHMM;
    private String shipmentNumber;
    private String shipmentStatusCode;
    private String shipmentAppointmentReasonCode;
    private String DONumber;
    private String DODate;
    private String consigneeName;
    private String consigneeAddress1;
    private String consigneeAddress2;
    private String consigneeCity;
    private String consigneeStateCode;
    private String consigneePostalCode;
    private String routingCode;
    public String getSalesOrderNumber() {
        return salesOrderNumber;
    }
    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }
    public String getShipmentDate() {
        return shipmentDate;
    }
    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }
    public String getShipmentTime() {
        return shipmentTime;
    }
    public void setShipmentTime(String shipmentTime) {
        this.shipmentTime = shipmentTime;
    }
    public String getShipmentNumber() {
        return shipmentNumber;
    }
    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }
    public String getShipmentStatusCode() {
        return shipmentStatusCode;
    }
    public void setShipmentStatusCode(String shipmentStatusCode) {
        this.shipmentStatusCode = shipmentStatusCode;
    }
    public String getShipmentAppointmentReasonCode() {
        return shipmentAppointmentReasonCode;
    }
    public void setShipmentAppointmentReasonCode(String shipmentAppointmentReasonCode) {
        this.shipmentAppointmentReasonCode = shipmentAppointmentReasonCode;
    }
    public String getDONumber() {
        return DONumber;
    }
    public void setDONumber(String dONumber) {
        DONumber = dONumber;
    }
    public String getDODate() {
        return DODate;
    }
    public void setDODate(String dODate) {
        DODate = dODate;
    }
    public String getConsigneeName() {
        return consigneeName;
    }
    public void setConsigneeName(String consignorName) {
        this.consigneeName = consignorName;
    }
    public String getConsigneeAddress1() {
        return consigneeAddress1;
    }
    public void setConsigneeAddress1(String consigneeAddress1) {
        this.consigneeAddress1 = consigneeAddress1;
    }
    public String getConsigneeAddress2() {
        return consigneeAddress2;
    }
    public void setConsigneeAddress2(String consigneeAddress2) {
        this.consigneeAddress2 = consigneeAddress2;
    }
    public String getConsigneeCity() {
        return consigneeCity;
    }
    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }
    public String getConsigneeStateCode() {
        return consigneeStateCode;
    }
    public void setConsigneeStateCode(String consigneeStateCode) {
        this.consigneeStateCode = consigneeStateCode;
    }
    public String getConsigneePostalCode() {
        return consigneePostalCode;
    }
    public void setConsigneePostalCode(String consigneePostalCode) {
        this.consigneePostalCode = consigneePostalCode;
    }
    public String getRoutingCode() {
        return routingCode;
    }
    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }
    public String getFormattedShipmentDateYYMMDD() {
        return formattedShipmentDateYYMMDD;
    }
    public void setFormattedShipmentDateYYMMDD(String formattedShipmentDateYYMMDD) {
        this.formattedShipmentDateYYMMDD = formattedShipmentDateYYMMDD;
    }
    public String getFormattedShipmentTimeHHMM() {
        return formattedShipmentTimeHHMM;
    }
    public void setFormattedShipmentTimeHHMM(String formattedShipmentTimeHHMM) {
        this.formattedShipmentTimeHHMM = formattedShipmentTimeHHMM;
    }

    
}