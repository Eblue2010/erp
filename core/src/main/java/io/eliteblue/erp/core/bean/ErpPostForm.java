package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.ErpPost;
import io.eliteblue.erp.core.service.ErpDetachmentService;
import io.eliteblue.erp.core.service.ErpPostService;
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
public class ErpPostForm implements Serializable {

    @Autowired
    private ErpPostService erpPostService;

    @Autowired
    private ErpDetachmentService erpDetachmentService;

    private Long id;
    private Long detachmentId;
    private ErpDetachment detachment;
    private ErpPost erpPost;

    public void init() {
        if(Faces.isAjaxRequest()) {
            return;
        }
        if(has(id)) {
            erpPost = erpPostService.findById(Long.valueOf(id));
            detachment = erpPost.getErpDetachment();
            detachmentId = detachment.getId();
        } else {
            erpPost = new ErpPost();
            if(has(detachmentId)) {
                detachment = erpDetachmentService.findById(detachmentId);
                erpPost.setErpDetachment(detachment);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpPost getErpPost() {
        return erpPost;
    }

    public void setErpPost(ErpPost post) {
        this.erpPost = post;
    }

    public Long getDetachmentId() {
        return detachmentId;
    }

    public void setDetachmentId(Long detachmentId) {
        this.detachmentId = detachmentId;
    }

    public ErpDetachment getDetachment() {
        return detachment;
    }

    public void setDetachment(ErpDetachment detachment) {
        this.detachment = detachment;
    }

    public String backBtnPressed() { return "detachment-form?id="+detachmentId+"faces-redirect=true&includeViewParams=true"; }

    public void clear() {
        erpPost = new ErpPost();
        id = null;
    }

    public boolean isNew() {
        return erpPost == null || erpPost.getId() == null;
    }

    public void remove() throws Exception {
        if(has(erpPost) && has(erpPost.getId())) {
            String name = erpPost.getName();
            erpPostService.delete(erpPost);
            addDetailMessage("POST DELETED", name, FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+detachment.getId()+"&clientId="+detachment.getErpClient().getId());
        }
    }

    public void save() throws Exception {
        if(erpPost != null) {
            erpPostService.save(erpPost);
            addDetailMessage("POST SAVED", erpPost.getName(), FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().getExternalContext().redirect("detachment-form.xhtml?id="+detachment.getId()+"&clientId="+detachment.getErpClient().getId());
        }
    }

    public void addDetailMessage(String message, String detail, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(message, detail);
        msg.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
