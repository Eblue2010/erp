package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.service.OperationsAreaService;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

import static com.github.adminfaces.template.util.Assert.has;

@Named
@ViewScoped
public class OperationsAreaForm implements Serializable {

    @Autowired
    private OperationsAreaService operationsAreaService;

    private Long id;
    private OperationsArea area;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            area = operationsAreaService.findById(Long.valueOf(id));
        } else {
            area = new OperationsArea();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationsArea getArea() {
        return area;
    }

    public void setArea(OperationsArea area) {
        this.area = area;
    }

    public void clear() {
        area = new OperationsArea();
        id = null;
    }

    public boolean isNew() {
        return area == null || area.getId() == null;
    }

    public void remove() throws Exception {
        if(has(area) && has(area.getId())) {
            String locationName = area.getLocation();
            operationsAreaService.delete(area);
            addDetailMessage("LOCATION DELETED", locationName, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("areas.xhtml");
        }
    }

    public void save() throws Exception {
        if(area != null) {
            operationsAreaService.save(area);
            addDetailMessage("LOCATION SAVED", area.getLocation(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("areas.xhtml");
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
