package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyCityModel;
import io.eliteblue.erp.core.model.ErpCity;
import io.eliteblue.erp.core.model.ErpRegion;
import io.eliteblue.erp.core.service.RegionCityService;
import org.omnifaces.util.Faces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ErpCityForm implements Serializable {

    @Autowired
    private RegionCityService regionCityService;

    private Long id;
    private Long regionId;
    private ErpCity erpCity;
    private ErpRegion erpRegion;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpCity = regionCityService.findCityById(Long.valueOf(id));
            erpRegion = erpCity.getErpRegion();
            regionId = erpRegion.getId();
        } else {
            erpCity = new ErpCity();
            if(has(regionId)) {
                erpRegion = regionCityService.findRegionById(Long.valueOf(regionId));
                erpCity.setErpRegion(erpRegion);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ErpCity getErpCity() {
        return erpCity;
    }

    public void setErpCity(ErpCity erpCity) {
        this.erpCity = erpCity;
    }

    public ErpRegion getErpRegion() {
        return erpRegion;
    }

    public void setErpRegion(ErpRegion erpRegion) {
        this.erpRegion = erpRegion;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public void clear() {
        erpCity = new ErpCity();
        id = null;
    }

    public String backBtnPressed() { return "cities?id="+regionId+"faces-redirect=true&includeViewParams=true"; }

    public boolean isNew() {
        return erpCity == null || erpCity.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpCity) && has(erpCity.getId())) {
            String cityName = erpCity.getName();
            regionCityService.removeCity(erpCity);
            addDetailMessage("CITY DELETED", cityName, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("cities.xhtml?id="+erpRegion.getId());
        }
    }

    public void save() throws Exception {
        if(has(erpCity)) {
            regionCityService.saveCity(erpCity);
            addDetailMessage("CITY DELETED", erpCity.getName(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("cities.xhtml?id="+erpRegion.getId());
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
