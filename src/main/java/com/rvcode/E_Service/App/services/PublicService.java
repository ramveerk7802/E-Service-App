package com.rvcode.E_Service.App.services;

import com.rvcode.E_Service.App.entities.ServiceType;
import com.rvcode.E_Service.App.repositories.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicService {
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    public List<ServiceType> getAllServiceTypeProvidedByAllElectrician(){
        try {
            List<ServiceType>  list =  serviceTypeRepository.findAll();
            return list;
        }catch (Exception e){
            return null;
        }
    }
}

