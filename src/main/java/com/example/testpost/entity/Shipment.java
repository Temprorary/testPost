package com.example.testpost.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientName;

    @Enumerated(EnumType.STRING)
    private ShipmentType type;

    private int recipientZipCode;

    private String recipientAddress;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    public Shipment(Long shipmentId) {
        this.id = shipmentId;
    }

    public Shipment(Long id, int status) {
        this.id = id;
        this.status = ShipmentStatus.values()[status];
    }

    public void arrivedShipmentStatus() {
        this.status = ShipmentStatus.IN_POST_OFFICE;
    }

    public void leftShipmentStatus() {
        this.status = ShipmentStatus.IN_TRANSIT;
    }

    public void receiveShipmentStatus() {
        this.status = ShipmentStatus.DELIVERED;
    }

    enum ShipmentType {
        PACKAGE, LETTER, PARCEL, POSTCARD
    }

    enum ShipmentStatus {
        REGISTERED, IN_POST_OFFICE, IN_TRANSIT, DELIVERED
    }

    public Shipment() {
    }

    public Shipment(String recipientName, int type, int recipientZipCode, String recipientAddress){
        this.recipientName = recipientName;
        this.type = ShipmentType.values()[type];
        this.recipientZipCode = recipientZipCode;
        this.recipientAddress = recipientAddress;
        this.status = ShipmentStatus.REGISTERED;
    }
}
