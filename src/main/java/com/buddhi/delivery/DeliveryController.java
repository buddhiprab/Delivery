package com.buddhi.delivery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/deliveries")
public class DeliveryController {
    @Autowired
    CachedDeliveryService cachedDeliveryService;
    @Autowired
    DeliveryRepository deliveryRepository;

    @PostMapping(path = "")
    public ResponseEntity<Object> createOrUpdateDelivery(@RequestBody DeliveryDto deliveryDto){
        Delivery delivery = Delivery.builder()
                .pickupName(deliveryDto.getPickupName())
                .pickupAddress(deliveryDto.getPickupAddress())
                .pickupDateTime(deliveryDto.getPickupDateTime())
                .pickupContactNumbers(String.join(",",deliveryDto.getPickupContactNumbers()))
                .pickupComment(deliveryDto.getPickupComment())
                .dropName(deliveryDto.getDropName())
                .dropAddress(deliveryDto.getDropAddress())
                .dropContactNumbers(String.join(",",deliveryDto.getDropContactNumbers()))
                .dropComment(deliveryDto.getDropComment()).build();
        if(deliveryDto.getId()==null){//create new delivery
            cachedDeliveryService.saveOrUpdate(delivery);
        } else {//update delivery
            delivery.setId(deliveryDto.getId());
            cachedDeliveryService.saveOrUpdate(delivery);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Delivery>> getDeliveries(){
        List<Delivery> deliveries = cachedDeliveryService.findAll();
        if(deliveries.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Delivery>>(deliveries,HttpStatus.OK);
    }

    @PostMapping(path = "/filter")
    public ResponseEntity<Delivery> getDelivery(@RequestBody DeliverFilter deliverFilter){
        Delivery delivery = cachedDeliveryService.findById(deliverFilter.getId());
        return new ResponseEntity<Delivery>(delivery, HttpStatus.OK);
    }

    @GetMapping(path = "/filter/{pickupName}")
    public ResponseEntity<List<Delivery>> getDeliveryByName(@PathVariable String pickupName){
        List<Delivery> deliveries = deliveryRepository.findByPickupName(pickupName);
        return new ResponseEntity<List<Delivery>>(deliveries, HttpStatus.OK);
    }
    @GetMapping(path = "/fetch/{dropAddress}")
    public ResponseEntity<List<Delivery>> fetchDeliveryByDropAddress(@PathVariable String dropAddress){
        List<Delivery> deliveries = deliveryRepository.fetchDeliveryByDropAddress(dropAddress);
        return new ResponseEntity<List<Delivery>>(deliveries, HttpStatus.OK);
    }
}
