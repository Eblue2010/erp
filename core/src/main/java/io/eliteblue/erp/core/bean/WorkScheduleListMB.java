package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpWorkSchedule;
import io.eliteblue.erp.core.service.WorkScheduleService;
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
public class WorkScheduleListMB implements Serializable {

    @Autowired
    private WorkScheduleService workScheduleService;

    private List<ErpWorkSchedule> filteredErpWorkSchedules;

    private List<ErpWorkSchedule> workSchedules;

    private ErpWorkSchedule selectedWorkSchedule;

    @PostConstruct
    public void init() {
        workSchedules = workScheduleService.getAllFilteredLocation();
    }

    public List<ErpWorkSchedule> getFilteredErpWorkSchedules() {
        return filteredErpWorkSchedules;
    }

    public void setFilteredErpWorkSchedules(List<ErpWorkSchedule> filteredErpWorkSchedules) {
        this.filteredErpWorkSchedules = filteredErpWorkSchedules;
    }

    public List<ErpWorkSchedule> getWorkSchedules() {
        return workSchedules;
    }

    public void setWorkSchedules(List<ErpWorkSchedule> workSchedules) {
        this.workSchedules = workSchedules;
    }

    public ErpWorkSchedule getSelectedWorkSchedule() {
        return selectedWorkSchedule;
    }

    public void setSelectedWorkSchedule(ErpWorkSchedule selectedWorkSchedule) {
        this.selectedWorkSchedule = selectedWorkSchedule;
    }

    public String downloadTemplate() {
        return "ws-download-templates?faces-redirect=true&includeViewParams=true";
    }

    public void onRowSelect(SelectEvent<ErpWorkSchedule> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("workschedule-form.xhtml?id="+selectedWorkSchedule.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpWorkSchedule> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("workschedule-form.xhtml?id="+selectedWorkSchedule.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpWorkSchedule erpWorkSchedule = (ErpWorkSchedule) value;
        return erpWorkSchedule.getDescription().toLowerCase().contains(filterText);
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