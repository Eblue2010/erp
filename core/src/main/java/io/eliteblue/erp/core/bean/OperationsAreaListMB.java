package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.service.OperationsAreaService;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class OperationsAreaListMB implements Serializable {

    @Autowired
    private OperationsAreaService operationsAreaService;

    private List<OperationsArea> areas;

    private OperationsArea selectedArea;

    @PostConstruct
    public void init() {
        areas = operationsAreaService.getAll();
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("area-form.xhtml?id="+event.getObject().getId());
    }

    public void onRowUnselect(UnselectEvent<OperationsArea> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("area-form.xhtml?id="+event.getObject().getId());
    }
}