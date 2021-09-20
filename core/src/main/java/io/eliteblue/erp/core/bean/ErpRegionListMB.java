package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyRegionModel;
import io.eliteblue.erp.core.model.ErpRegion;
import io.eliteblue.erp.core.service.RegionCityService;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@Named
@ViewScoped
public class ErpRegionListMB implements Serializable {

    @Autowired
    private RegionCityService regionCityService;

    private LazyDataModel<ErpRegion> lazyErpRegions;
    private List<ErpRegion> filteredErpRegions;
    private List<ErpRegion> regions;
    private ErpRegion selectedRegion;

    @PostConstruct
    public void init() {
        regions = regionCityService.getAllRegions();
        lazyErpRegions = new LazyRegionModel(regions);
    }

    public LazyDataModel<ErpRegion> getLazyErpRegions() {
        return lazyErpRegions;
    }

    public void setLazyErpRegions(LazyDataModel<ErpRegion> lazyErpRegions) {
        this.lazyErpRegions = lazyErpRegions;
    }

    public List<ErpRegion> getFilteredErpRegions() {
        return filteredErpRegions;
    }

    public void setFilteredErpRegions(List<ErpRegion> filteredErpRegions) {
        this.filteredErpRegions = filteredErpRegions;
    }

    public List<ErpRegion> getRegions() {
        return regions;
    }

    public void setRegions(List<ErpRegion> regions) {
        this.regions = regions;
    }

    public ErpRegion getSelectedRegion() {
        return selectedRegion;
    }

    public void setSelectedRegion(ErpRegion selectedRegion) {
        this.selectedRegion = selectedRegion;
    }

    public void onRowSelect(SelectEvent<ErpRegion> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("cities.xhtml?id="+selectedRegion.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpRegion> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("cities.xhtml?id="+selectedRegion.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpRegion erpRegion = (ErpRegion) value;
        return erpRegion.getName().toLowerCase().contains(filterText);
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
