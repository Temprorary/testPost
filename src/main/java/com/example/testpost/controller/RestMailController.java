package com.example.testpost.controller;

import com.example.testpost.entity.HistoryEvent;
import com.example.testpost.entity.PostOffice;
import com.example.testpost.entity.Shipment;
import com.example.testpost.service.HistoryEventService;
import com.example.testpost.service.PostOfficeService;
import com.example.testpost.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class RestMailController {

    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private HistoryEventService historyEventService;

    @Autowired
    private PostOfficeService postOfficeService;

    public RestMailController(ShipmentService shipmentService, HistoryEventService historyEventService, PostOfficeService postOfficeService) {
        this.shipmentService = shipmentService;
        this.historyEventService = historyEventService;
        this.postOfficeService = postOfficeService;
    }

    @PostMapping
    public ResponseEntity<Void> createShipment(@RequestParam String recipientName,
                                                   @RequestParam int ShipmentType, // 0 - PACKAGE, 1 - LETTER, 2 - PARCEL, 3 - POSTCARD
                                                   @RequestParam int recipientZipCode,
                                                   @RequestParam String recipientAddress,
                                                   @RequestParam Long postOffice) {
        Shipment shipment = new Shipment(recipientName, ShipmentType, recipientZipCode, recipientAddress);
        shipmentService.saveShipment(shipment);
        PostOffice currentPostOffice = postOfficeService.findById(postOffice);
        historyEventService.createHistoryEvent(new HistoryEvent(shipment, currentPostOffice, new Date()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{shipmentId}/arrive")
    public void arrivedShipmentStatus(@PathVariable Long shipmentId,
                                     @RequestParam Long postOffice) {

        Shipment shipment = shipmentService.findById(shipmentId);
        shipment.arrivedShipmentStatus();
        shipmentService.saveShipment(shipment);
        PostOffice currentPostOffice = postOfficeService.findById(postOffice);
        historyEventService.createHistoryEvent(new HistoryEvent(shipment, currentPostOffice, new Date()));
    }

    @PutMapping("/{shipmentId}/left")
    public void leftShipmentStatus(@PathVariable Long shipmentId,
                                     @RequestParam Long postOffice) {

        Shipment shipment = shipmentService.findById(shipmentId);
        shipment.leftShipmentStatus();
        shipmentService.saveShipment(shipment);
        PostOffice currentPostOffice = postOfficeService.findById(postOffice);
        historyEventService.createHistoryEvent(new HistoryEvent(shipment, currentPostOffice, new Date()));
    }

    @PutMapping("/{shipmentId}/receive/")
    public String getShipment(@PathVariable Long shipmentId) {
        Shipment shipment = shipmentService.findById(shipmentId);
        shipment.receiveShipmentStatus();
        shipmentService.saveShipment(shipment);
        PostOffice postOffice = historyEventService.getShipmentHistory(shipmentId).stream().sorted(Comparator.comparing(HistoryEvent::getDate).reversed())
                .findFirst().get().getPostOffice();
        historyEventService.createHistoryEvent(new HistoryEvent(shipment,postOffice, new Date()));
        return "Received successfully";
    }

    @GetMapping("/{shipmentId}/history")
    public String getShipmentHistory(@PathVariable Long shipmentId) {
        List<HistoryEvent> historyEvents = historyEventService.getShipmentHistory(shipmentId);

        if (historyEvents.isEmpty()) {
            return "No history found for your shipment";
        }

        StringBuilder output = new StringBuilder("Shipment Status: " + shipmentService.findById(shipmentId).getStatus() + "\n");

        for (HistoryEvent event : historyEvents) {
            output.append(String.format(
                    "  - Date: %s, Post Office: %s (%d), Status: %s\n",
                    event.getDate(), event.getPostOffice().getName(), event.getPostOffice().getIndex(), event.getShipmentStatus()));
        }

        return output.toString();
    }
}
