package com.example.testpost.service;

import com.example.testpost.entity.Shipment;
import com.example.testpost.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public void saveShipment(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    public Shipment findById(Long shipmentId) {
        return shipmentRepository.findById(shipmentId).get();
    }

}
