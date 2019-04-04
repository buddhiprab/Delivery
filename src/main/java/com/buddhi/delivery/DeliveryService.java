package com.buddhi.delivery;

import com.buddhi.util.SimulateSlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DeliveryService {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    SimulateSlowService simulateSlowService;

    @Cacheable("deliveries")
    public List<Delivery> findAll(){
        log.info("deliveries: findAll");
        return deliveryRepository.findAll();
    }

    @Cacheable("delivery")
    public Delivery findById(Long id){
        log.info("deliveries: findById");
        return deliveryRepository.findById(id).orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value="delivery", allEntries=true),
            @CacheEvict(value="deliveries", allEntries=true) })
    public void saveOrUpdate(Delivery delivery){
        log.info("deliveries: saveOrUpdate");
        deliveryRepository.save(delivery);
    }
}
