package com.example.testpost.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class HistoryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Shipment shipment;

    @ManyToOne
    PostOffice postOffice;

    Shipment.ShipmentStatus shipmentStatus;

    private Date date;

    public HistoryEvent() {
    }

    public HistoryEvent(Shipment shipment, PostOffice postOffice, Date date) {
        this.shipment = shipment;
        this.postOffice = postOffice;
        this.date = date;
        this.shipmentStatus = shipment.getStatus();
    }
}
