package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpClient;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.service.ErpDetachmentService;
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
public class ErpDetachmentListMB implements Serializable {

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    private List<ErpDetachment> detachments;

    private ErpDetachment selectedDetachment;

    @PostConstruct
    public void init() {
        detachments = erpDetachmentService.getAll();
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+event.getObject().getId());
    }

    public void onRowUnselect(UnselectEvent<ErpDetachment> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+event.getObject().getId());
    }
}