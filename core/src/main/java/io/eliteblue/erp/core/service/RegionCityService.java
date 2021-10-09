package io.eliteblue.erp.core.service;

import io.eliteblue.erp.core.model.ErpCity;
import io.eliteblue.erp.core.model.ErpRegion;
import io.eliteblue.erp.core.repository.ErpCityRepository;
import io.eliteblue.erp.core.repository.ErpRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegionCityService {

    @Autowired
    private ErpRegionRepository regionRepository;

    @Autowired
    private ErpCityRepository cityRepository;

    public List<ErpRegion> getAllRegions() {
        return regionRepository.findAll();
    }

    public List<ErpCity> getAllCitiesFromRegion(ErpRegion region) {
        return cityRepository.findAllByErpRegion(region);
    }

    public ErpRegion findRegionById(Long id) {
        return regionRepository.getOne(id);
    }

    public ErpRegion findRegionByName(String regionName) {
        return regionRepository.findByName(regionName);
    }

    public ErpCity findCityById(Long id) {
        return cityRepository.getOne(id);
    }

    public void removeCity(ErpCity city) {
        cityRepository.delete(city);
    }

    public void saveCity(ErpCity city) {
        cityRepository.save(city);
    }
}
