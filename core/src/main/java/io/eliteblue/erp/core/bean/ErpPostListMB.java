package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.model.ErpPost;
import io.eliteblue.erp.core.service.ErpPostService;
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
public class ErpPostListMB implements Serializable {

    @Autowired
    private ErpPostService erpPostService;

    private List<ErpPost> posts;

    private ErpPost selectedPost;

    @PostConstruct
    public void init() {
        posts = erpPostService.getAll();
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

    public void onRowSelect(SelectEvent<ErpPost> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("post-form.xhtml?id="+event.getObject().getId());
    }

    public void onRowUnselect(UnselectEvent<ErpPost> event) throws Exception {
        FacesContext.getCurrentInstance().getExternalContext().redirect("post-form.xhtml?id="+event.getObject().getId());
    }
}