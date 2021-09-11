package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.converters.OperationsAreaConverter;
import io.eliteblue.erp.core.lazy.LazyErpPostModel;
import io.eliteblue.erp.core.model.ErpClient;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.ErpPost;
import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.service.ErpClientService;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.OperationsAreaService;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class ErpDetachmentForm implements Serializable {

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    @Autowired
    private ErpClientService erpClientService;

    @Autowired
    private OperationsAreaService operationsAreaService;

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

    public String newPostPressed() {
        return "post-form?detachmentId="+id+"faces-redirect=true&includeViewParams=true";
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

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);

        ErpPost erpPost = (ErpPost) value;
        return erpPost.getName().toLowerCase().contains(filterText);
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
