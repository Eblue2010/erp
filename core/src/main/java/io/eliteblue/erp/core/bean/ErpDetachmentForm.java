package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyEmployeeModel;
import io.eliteblue.erp.core.lazy.LazyErpPostModel;
import io.eliteblue.erp.core.model.*;
import io.eliteblue.erp.core.service.ErpClientService;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.ErpEmployeeService;
import io.eliteblue.erp.core.service.OperationsAreaService;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ErpDetachmentForm implements Serializable {

    final DateFormat format = new SimpleDateFormat("HH:mm");

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    @Autowired
    private ErpClientService erpClientService;

    @Autowired
    private OperationsAreaService operationsAreaService;

    @Autowired
    private ErpEmployeeService erpEmployeeService;

    private Long id;
    private Long clientId;
    private ErpDetachment erpDetachment;
    private ErpClient client;
    private List<SelectItem> locations;
    private String locationValue;

    private LazyDataModel<ErpPost> lazyErpPosts;
    private List<ErpPost> filteredErpPosts;
    private List<ErpPost> posts;
    private ErpPost selectedPost;

    private List<ErpTimeSchedule> filteredErpTimeSchedules;
    private List<ErpTimeSchedule> erpTimeSchedules;
    private ErpTimeSchedule selectedErpTimeSchedule;

    private List<ErpEmployee> employees;
    private List<ErpEmployee> filteredEmployees;
    private LazyDataModel<ErpEmployee> lazyErpEmployees;
    private ErpEmployee selectedEmployee;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpDetachment = erpDetachmentService.findById(Long.valueOf(id));
            if(erpDetachment != null) {
                locationValue = erpDetachment.getLocation().getLocation();
                client = erpDetachment.getErpClient();
                clientId = client.getId();
                posts = new ArrayList<ErpPost>(erpDetachment.getPosts());
                lazyErpPosts = new LazyErpPostModel(posts);
                employees = new ArrayList<>(erpDetachment.getAssignedEmployees());
                lazyErpEmployees = new LazyEmployeeModel(employees);
                erpTimeSchedules = new ArrayList<ErpTimeSchedule>(erpDetachment.getErpTimeSchedules());
            }
        } else {
            erpDetachment = new ErpDetachment();
            if(has(clientId)){
                client = erpClientService.findById(clientId);
                erpDetachment.setErpClient(client);
            }
        }
        List<OperationsArea> areas = operationsAreaService.getAll();
        locations = new ArrayList<SelectItem>();
        for(OperationsArea o: areas) {
            SelectItem itm = new SelectItem(o.getLocation(), o.getLocation());
            locations.add(itm);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpDetachment getErpDetachment() {
        return erpDetachment;
    }

    public void setErpDetachment(ErpDetachment detachment) {
        this.erpDetachment = detachment;
    }

    public Long getClientId() {
        return clientId;
    }

    public ErpClient getClient() {
        return client;
    }

    public void setClient(ErpClient client) {
        this.client = client;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public List<SelectItem> getLocations() {
        return locations;
    }

    public void setLocations(List<SelectItem> locations) {
        this.locations = locations;
    }

    public List<ErpPost> getPosts() {
        return posts;
    }

    public void setPosts(List<ErpPost> posts) {
        this.posts = posts;
    }

    public ErpPost getSelectedPost() {
        return selectedPost;
    }

    public void setSelectedPost(ErpPost selectedPost) {
        this.selectedPost = selectedPost;
    }

    public String getLocationValue() {
        return locationValue;
    }

    public void setLocationValue(String locationValue) {
        this.locationValue = locationValue;
    }

    public LazyDataModel<ErpPost> getLazyErpPosts() {
        return lazyErpPosts;
    }

    public void setLazyErpPosts(LazyDataModel<ErpPost> lazyErpPosts) {
        this.lazyErpPosts = lazyErpPosts;
    }

    public List<ErpPost> getFilteredErpPosts() {
        return filteredErpPosts;
    }

    public void setFilteredErpPosts(List<ErpPost> filteredErpPosts) {
        this.filteredErpPosts = filteredErpPosts;
    }

    public List<ErpTimeSchedule> getFilteredErpTimeSchedules() {
        return filteredErpTimeSchedules;
    }

    public void setFilteredErpTimeSchedules(List<ErpTimeSchedule> filteredErpTimeSchedules) {
        this.filteredErpTimeSchedules = filteredErpTimeSchedules;
    }

    public List<ErpTimeSchedule> getErpTimeSchedules() {
        return erpTimeSchedules;
    }

    public void setErpTimeSchedules(List<ErpTimeSchedule> erpTimeSchedules) {
        this.erpTimeSchedules = erpTimeSchedules;
    }

    public ErpTimeSchedule getSelectedErpTimeSchedule() {
        return selectedErpTimeSchedule;
    }

    public void setSelectedErpTimeSchedule(ErpTimeSchedule selectedErpTimeSchedule) {
        this.selectedErpTimeSchedule = selectedErpTimeSchedule;
    }

    public List<ErpEmployee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<ErpEmployee> employees) {
        this.employees = employees;
    }

    public List<ErpEmployee> getFilteredEmployees() {
        return filteredEmployees;
    }

    public ErpEmployee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(ErpEmployee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public void setFilteredEmployees(List<ErpEmployee> filteredEmployees) {
        this.filteredEmployees = filteredEmployees;
    }

    public LazyDataModel<ErpEmployee> getLazyErpEmployees() {
        return lazyErpEmployees;
    }

    public void setLazyErpEmployees(LazyDataModel<ErpEmployee> lazyErpEmployees) {
        this.lazyErpEmployees = lazyErpEmployees;
    }

    public DateFormat getFormat() {
        return format;
    }

    public String newPostPressed() {
        return "post-form?detachmentId="+id+"faces-redirect=true&includeViewParams=true";
    }

    public String newTimePressed() {
        return "time-schedule-form?detachmentId="+id+"faces-redirect=true&includeViewParams=true";
    }

    public String newEmployeePressed() {
        return "employee-assign?detachmentId="+id+"faces-redirect=true&includeViewParams=true";
    }

    public String backBtnPressed() { return "client-form?id="+clientId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        erpDetachment = new ErpDetachment();
        id = null;
    }

    public boolean isNew() {
        return erpDetachment == null || erpDetachment.getId() == null;
    }

    public void onRowSelect(SelectEvent<ErpPost> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("post-form.xhtml?id="+selectedPost.getId());
    }

    public void onRowUnselect(UnselectEvent<ErpPost> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("post-form.xhtml?id="+selectedPost.getId());
    }

    public void onRowSelectTime(SelectEvent<ErpTimeSchedule> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("time-schedule-form.xhtml?id="+ selectedErpTimeSchedule.getId());
    }

    public void onRowUnselectTime(UnselectEvent<ErpTimeSchedule> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("time-schedule-form.xhtml?id="+ selectedErpTimeSchedule.getId());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpPost erpPost = (ErpPost) value;
        return erpPost.getName().toLowerCase().contains(filterText);
    }

    public boolean globalEmployeeFilterFunction(Object value, Object filter, Locale locale) {
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
                || erpEmployee.getEmail().toLowerCase().contains(filterText);
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void removeAssignedEmployee() throws Exception {
        //this.products.remove(this.selectedProduct);
        erpDetachment.getAssignedEmployees().remove(this.selectedEmployee);
        employees.remove(this.selectedEmployee);
        //System.out.println("ASSIGNED EMPLOYEE COUNT: "+erpDetachment.getAssignedEmployees().size());
        selectedEmployee.setErpDetachment(null);
        erpEmployeeService.save(selectedEmployee);
        erpDetachmentService.save(erpDetachment);
        selectedEmployee = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee1 Removed"));
        FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+erpDetachment.getId()+"&clientId="+erpDetachment.getErpClient().getId());
    }

    public void remove() throws Exception {
        if(has(erpDetachment) && has(erpDetachment.getId())) {
            String name = erpDetachment.getName();
            erpDetachmentService.delete(erpDetachment);
            addDetailMessage("DETACHMENT DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("client-form.xhtml?id="+clientId);
        }
    }

    public void save() throws Exception {
        if(erpDetachment != null) {
            if(locationValue != null) {
                erpDetachment.setLocation(operationsAreaService.findByLocation(locationValue));
            }
            erpDetachmentService.save(erpDetachment);
            addDetailMessage("DETACHMENT SAVED", erpDetachment.getName(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("client-form.xhtml?id="+clientId);
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
