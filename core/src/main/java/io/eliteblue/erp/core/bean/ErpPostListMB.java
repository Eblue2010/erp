package io.eliteblue.erp.core.bean;

import io.eliteblue.erp.core.lazy.LazyErpPostModel;
import io.eliteblue.erp.core.model.ErpDetachment;
import io.eliteblue.erp.core.model.ErpPost;
import io.eliteblue.erp.core.service.ErpPostService;
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
public class ErpPostListMB implements Serializable {

    @Autowired
    private ErpPostService erpPostService;

    private LazyDataModel<ErpPost> lazyErpPosts;

    private List<ErpPost> filteredErpPosts;

    private List<ErpPost> posts;

    private ErpPost selectedPost;

    @PostConstruct
    public void init() {
        posts = erpPostService.getAll();
        lazyErpPosts = new LazyErpPostModel(posts);
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
}