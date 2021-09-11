package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyErpDetachmentModel;
import io.eliteblue.erp.core.model.ErpClient;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.service.ErpClientService;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
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
public class ErpClientForm implements Serializable {

    @Autowired
    private ErpClientService erpClientService;

    private Long id;
    private ErpClient erpClient;

    private LazyDataModel<ErpDetachment> lazyErpDetachments;
    private List<ErpDetachment> filteredErpDetachments;
    private List<ErpDetachment> detachments;
    private ErpDetachment selectedDetachment;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpClient = erpClientService.findById(Long.valueOf(id));
            detachments = new ArrayList<ErpDetachment>(erpClient.getErpDetachments());
            lazyErpDetachments = new LazyErpDetachmentModel(detachments);
        } else {
            erpClient = new ErpClient();
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpClient getErpClient() {
        return erpClient;
    }

    public void setErpClient(ErpClient erpClient) {
        this.erpClient = erpClient;
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

    public void clear() {
        erpClient = new ErpClient();
        id = null;
    }

    public boolean isNew() {
        return erpClient == null || erpClient.getId() == null;
    }

    public String newDetachmentPressed() {
        return "detachment-form?clientId="+id+"&faces-redirect=true&includeViewParams=true";
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
        return erpDetachment.getName().toLowerCase().contains(filterText);
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void remove() throws Exception {
        if(has(erpClient) && has(erpClient.getId())) {
            String name = erpClient.getName();
            erpClientService.delete(erpClient);
            addDetailMessage("CLIENT DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("clients.xhtml");
        }
    }

    public void save() throws Exception {
        if(erpClient != null) {
            erpClientService.save(erpClient);
            addDetailMessage("CLIENT SAVED", erpClient.getName(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("clients.xhtml");
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
