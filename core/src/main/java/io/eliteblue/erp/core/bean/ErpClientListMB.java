package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpClient;
import io.eliteblue.erp.core.service.ErpClientService;
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
public class ErpClientListMB implements Serializable {

    @Autowired
    private ErpClientService erpClientService;

    private List<ErpClient> clients;

    private ErpClient selectedClient;

    @PostConstruct
    public void init() {
        clients = erpClientService.getAll();
    }

    public List<ErpClient> getClients() {
        return clients;
    }

    public void setClients(List<ErpClient> clients) {
        this.clients = clients;
    }

    public ErpClient getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(ErpClient selectedClient) {
        this.selectedClient = selectedClient;
    }

    public void onRowSelect(SelectEvent<ErpClient> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("client-form.xhtml?id="+event.getObject().getId());
    }

    public void onRowUnselect(UnselectEvent<ErpClient> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("client-form.xhtml?id="+event.getObject().getId());
    }
}