package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyErpClientModel;
import io.eliteblue.erp.core.model.ErpClient;
import io.eliteblue.erp.core.service.ErpClientService;
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
public class ErpClientListMB implements Serializable {

    @Autowired
    private ErpClientService erpClientService;

    private LazyDataModel<ErpClient> lazyErpClients;

    private List<ErpClient> filteredErpClients;

    private List<ErpClient> clients;

    private ErpClient selectedClient;

    @PostConstruct
    public void init() {
        clients = erpClientService.getAll();
        lazyErpClients = new LazyErpClientModel(clients);
    }

    public LazyDataModel<ErpClient> getLazyErpClients() {
        return lazyErpClients;
    }

    public void setLazyErpClients(LazyDataModel<ErpClient> lazyErpClients) {
        this.lazyErpClients = lazyErpClients;
    }

    public List<ErpClient> getFilteredErpClients() {
        return filteredErpClients;
    }

    public void setFilteredErpClients(List<ErpClient> filteredErpClients) {
        this.filteredErpClients = filteredErpClients;
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("client-form.xhtml?id="+selectedClient.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpClient> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("client-form.xhtml?id="+selectedClient.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpClient erpClient = (ErpClient) value;
        return erpClient.getName().toLowerCase().contains(filterText);
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