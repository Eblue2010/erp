package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyOperationsAreaModel;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.service.OperationsAreaService;
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
public class OperationsAreaListMB implements Serializable {

    @Autowired
    private OperationsAreaService operationsAreaService;

    private List<OperationsArea> areas;

    private OperationsArea selectedArea;

    private LazyDataModel<OperationsArea> lazyOperationsAreas;

    private List<OperationsArea> filteredOperationsAreas;

    @PostConstruct
    public void init() {
        areas = operationsAreaService.getAll();
        lazyOperationsAreas = new LazyOperationsAreaModel(areas);
    }

    public LazyDataModel<OperationsArea> getLazyOperationsAreas() {
        return lazyOperationsAreas;
    }

    public void setLazyOperationsAreas(LazyDataModel<OperationsArea> lazyOperationsAreas) {
        this.lazyOperationsAreas = lazyOperationsAreas;
    }

    public List<OperationsArea> getFilteredOperationsAreas() {
        return filteredOperationsAreas;
    }

    public void setFilteredOperationsAreas(List<OperationsArea> filteredOperationsAreas) {
        this.filteredOperationsAreas = filteredOperationsAreas;
    }

    public List<OperationsArea> getAreas() {
        return areas;
    }

    public void setAreas(List<OperationsArea> areas) {
        this.areas = areas;
    }

    public OperationsArea getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(OperationsArea selectedArea) {
        this.selectedArea = selectedArea;
    }

    public void onRowSelect(SelectEvent<OperationsArea> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("area-form.xhtml?id="+selectedArea.getId());
    }

    public void onRowUnselect(UnselectEvent<OperationsArea> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("area-form.xhtml?id="+selectedArea.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        OperationsArea operationsArea = (OperationsArea) value;
        return operationsArea.getLocation().toLowerCase().contains(filterText);
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