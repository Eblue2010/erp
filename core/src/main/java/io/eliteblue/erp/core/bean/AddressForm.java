package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.Address;
import io.eliteblue.erp.core.model.ErpCity;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.model.ErpRegion;
import io.eliteblue.erp.core.service.AddressService;
import io.eliteblue.erp.core.service.ErpEmployeeService;
import io.eliteblue.erp.core.service.RegionCityService;
import io.eliteblue.erp.core.util.DateTimeUtil;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class AddressForm implements Serializable {

    private final String pattern = "dd MMM yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    @Autowired
    private AddressService addressService;

    @Autowired
    private ErpEmployeeService employeeService;

    @Autowired
    private RegionCityService regionCityService;

    private Long id;
    private Long employeeId;
    private ErpEmployee erpEmployee;
    private Address address;
    private String joinedDate;
    private String lastUpdate;
    private Map<String, String> regions;
    private Map<String, String> cities;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            address = addressService.findById(Long.valueOf(id));
            erpEmployee = address.getEmployee();
            employeeId = erpEmployee.getId();
            if(erpEmployee.getJoinedDate() != null) {
                joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
            }
            lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
        } else {
            address = new Address();
            if(has(employeeId)){
                erpEmployee = employeeService.findById(employeeId);
                address.setEmployee(erpEmployee);
                if(erpEmployee.getJoinedDate() != null) {
                    joinedDate = "Joined " +simpleDateFormat.format(erpEmployee.getJoinedDate());
                }
                lastUpdate = DateTimeUtil.timeAgo(new Date(), erpEmployee.getLastUpdate());
            }
        }
        regions = new HashMap<>();
        for(ErpRegion r: regionCityService.getAllRegions()) {
            regions.put(r.getName(), r.getName());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public ErpEmployee getErpEmployee() {
        return erpEmployee;
    }

    public void setErpEmployee(ErpEmployee erpEmployee) {
        this.erpEmployee = erpEmployee;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, String> regions) {
        this.regions = regions;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    public String backBtnPressed() { return "employee-form?id="+employeeId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        address = new Address();
        id = null;
    }

    public boolean isNew() {
        return address == null || address.getId() == null;
    }

    public Map<String, String> getCitiesByRegion(String selectedRegion) {
        Map<String, String> retVal = new HashMap<>();
        ErpRegion _region = regionCityService.findRegionByName(selectedRegion);
        List<ErpCity> _cities = regionCityService.getAllCitiesFromRegion(_region);
        for(ErpCity c: _cities) {
            retVal.put(c.getName(), c.getName());
        }
        return retVal;
    }

    public void remove() throws Exception {
        if(has(address) && has(address.getId())) {
            String name = address.getAddressName();
            addressService.delete(address);
            addDetailMessage("ADDRESS DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+employeeId);
        }
    }

    public void save() throws Exception {
        if(address != null) {
            addressService.save(address);
            addDetailMessage("ADDRESS SAVED", address.getAddressName(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+employeeId);
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
