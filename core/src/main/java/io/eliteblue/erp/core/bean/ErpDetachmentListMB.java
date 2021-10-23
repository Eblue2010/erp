package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyErpDetachmentModel;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.service.ErpDetachmentService;
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
public class ErpDetachmentListMB implements Serializable {

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    private LazyDataModel<ErpDetachment> lazyErpDetachments;

    private List<ErpDetachment> filteredErpDetachments;

    private List<ErpDetachment> detachments;

    private ErpDetachment selectedDetachment;

    @PostConstruct
    public void init() {
        detachments = erpDetachmentService.getAll();
        lazyErpDetachments = new LazyErpDetachmentModel(detachments);
    }

    public LazyDataModel<ErpDetachment> getLazyErpDetachments() {
        return lazyErpDetachments;
    }

    public void setLazyErpDetachments(LazyDataModel<ErpDetachment> lazyErpDetachments) {
        this.lazyErpDetachments = lazyErpDetachments;
    }

    public List<ErpDetachment> getFilteredErpDetachments() {
        return filteredErpDetachments;
    }

    public void setFilteredErpDetachments(List<ErpDetachment> filteredErpDetachments) {
        this.filteredErpDetachments = filteredErpDetachments;
    }

    public List<ErpDetachment> getDetachments() {
        return detachments;
    }

    public void setDetachments(List<ErpDetachment> detachments) {
        this.detachments = detachments;
    }

    public ErpDetachment getSelectedDetachment() {
        return selectedDetachment;
    }

    public void setSelectedDetachment(ErpDetachment selectedDetachment) {
        this.selectedDetachment = selectedDetachment;
    }

    public void onRowSelect(SelectEvent<ErpDetachment> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+selectedDetachment.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpDetachment> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+selectedDetachment.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpDetachment erpDetachment = (ErpDetachment) value;
        return erpDetachment.getName().toLowerCase().contains(filterText)
            || erpDetachment.getLocation().getLocation().toLowerCase().contains(filterText);
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