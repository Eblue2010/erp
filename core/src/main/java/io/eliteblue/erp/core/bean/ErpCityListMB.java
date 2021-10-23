package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyCityModel;
import io.eliteblue.erp.core.model.ErpCity;
import io.eliteblue.erp.core.model.ErpRegion;
import io.eliteblue.erp.core.service.RegionCityService;
import io.eliteblue.erp.core.util.EmpMastProcessUtil;
import io.eliteblue.erp.core.util.ExcelUtils;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ErpCityListMB implements Serializable {

    @Autowired
    private RegionCityService regionCityService;

    @Autowired
    private EmpMastProcessUtil processUtil;

    private LazyDataModel<ErpCity> lazyErpCities;
    private List<ErpCity> filteredErpCities;
    private List<ErpCity> cities;
    private Long id;
    private ErpRegion erpRegion;
    private ErpCity selectedCity;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpRegion = regionCityService.findRegionById(Long.valueOf(id));
            cities = regionCityService.getAllCitiesFromRegion(erpRegion);
            lazyErpCities = new LazyCityModel(cities);
            lazyErpCities.setRowCount(10);
        } else {
            erpRegion = new ErpRegion();
        }
    }

    public LazyDataModel<ErpCity> getLazyErpCities() {
        return lazyErpCities;
    }

    public void setLazyErpCities(LazyDataModel<ErpCity> lazyErpCities) {
        this.lazyErpCities = lazyErpCities;
    }

    public List<ErpCity> getFilteredErpCities() {
        return filteredErpCities;
    }

    public void setFilteredErpCities(List<ErpCity> filteredErpCities) {
        this.filteredErpCities = filteredErpCities;
    }

    public List<ErpCity> getCities() {
        return cities;
    }

    public void setCities(List<ErpCity> cities) {
        this.cities = cities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpRegion getErpRegion() {
        return erpRegion;
    }

    public void setErpRegion(ErpRegion erpRegion) {
        this.erpRegion = erpRegion;
    }

    public ErpCity getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(ErpCity selectedCity) {
        this.selectedCity = selectedCity;
    }

    public void onRowSelect(SelectEvent<ErpCity> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("city-form.xhtml?id="+selectedCity.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpCity> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("city-form.xhtml?id="+selectedCity.getId());
    }

    public String newCityPressed() throws Exception {
        return "city-form?regionId="+id+"faces-redirect=true&includeViewParams=true";
        //processUtil.startProcess();
        //return "cities?id="+id+"faces-redirect=true&includeViewParams=true";
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpCity erpCity = (ErpCity) value;
        return erpCity.getName().toLowerCase().contains(filterText);
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
}
