package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpDTR;
import io.eliteblue.erp.core.service.ErpDTRService;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
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
public class ErpDTRListMB implements Serializable {

    @Autowired
    private ErpDTRService erpDTRService;

    private List<ErpDTR> filteredErpDTRs;

    private List<ErpDTR> erpDTRs;

    private ErpDTR selectedErpDTR;

    @PostConstruct
    public void init() {
        erpDTRs = erpDTRService.getAllFilteredLocation();
    }

    public List<ErpDTR> getFilteredErpDTRs() {
        return filteredErpDTRs;
    }

    public void setFilteredErpDTRs(List<ErpDTR> filteredErpDTRs) {
        this.filteredErpDTRs = filteredErpDTRs;
    }

    public List<ErpDTR> getErpDTRs() {
        return erpDTRs;
    }

    public void setErpDTRs(List<ErpDTR> erpDTRs) {
        this.erpDTRs = erpDTRs;
    }

    public ErpDTR getSelectedErpDTR() {
        return selectedErpDTR;
    }

    public void setSelectedErpDTR(ErpDTR selectedErpDTR) {
        this.selectedErpDTR = selectedErpDTR;
    }

    public void onRowSelect(SelectEvent<ErpDTR> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("dtr-form.xhtml?id="+selectedErpDTR.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpDTR> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("dtr-form.xhtml?id="+selectedErpDTR.getId());
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
