package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyEmployeeModel;
import io.eliteblue.erp.core.model.ErpEmployee;
import io.eliteblue.erp.core.service.ErpEmployeeService;
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
public class ErpEmployeeListMB implements Serializable {

    @Autowired
    private ErpEmployeeService employeeService;

    private LazyDataModel<ErpEmployee> lazyErpEmployees;

    private List<ErpEmployee> filteredErpEmployees;

    private List<ErpEmployee> employees;

    private ErpEmployee selectedEmployee;

    @PostConstruct
    public void init() {
        //employees = employeeService.getAll();
        employees = employeeService.getAllFiltered();
        lazyErpEmployees = new LazyEmployeeModel(employees);
        lazyErpEmployees.setRowCount(10);
    }

    public LazyDataModel<ErpEmployee> getLazyErpEmployees() {
        return lazyErpEmployees;
    }

    public void setLazyErpEmployees(LazyDataModel<ErpEmployee> lazyErpEmployees) {
        this.lazyErpEmployees = lazyErpEmployees;
    }

    public List<ErpEmployee> getFilteredErpEmployees() {
        return filteredErpEmployees;
    }

    public void setFilteredErpEmployees(List<ErpEmployee> filteredErpEmployees) {
        this.filteredErpEmployees = filteredErpEmployees;
    }

    public List<ErpEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ErpEmployee> employees) {
        this.employees = employees;
    }

    public ErpEmployee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(ErpEmployee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public void onRowSelect(SelectEvent<ErpEmployee> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+selectedEmployee.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpEmployee> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("employee-form.xhtml?id="+selectedEmployee.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpEmployee erpEmployee = (ErpEmployee) value;
        return erpEmployee.getFirstname().toLowerCase().contains(filterText)
            || erpEmployee.getLastname().toLowerCase().contains(filterText)
            || erpEmployee.getGender().name().toLowerCase().contains(filterText)
            || erpEmployee.getEmployeeId().toLowerCase().contains(filterText)
            || erpEmployee.getStatus().name().toLowerCase().contains(filterText)
            || (erpEmployee.getFirstname().toLowerCase()+" "+erpEmployee.getLastname().toLowerCase()).contains(filterText);
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
