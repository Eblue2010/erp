package io.eliteblue.erp.client.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import io.eliteblue.erp.client.model.security.ErpOAuthUser;
import io.eliteblue.erp.client.model.security.ErpUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@ViewScoped
public class SessionMB implements Serializable {

	private String currentUser;
	private String firstname;
	private String lastname;

	@PostConstruct
	public void init() {
		currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof ErpUserDetails) {
			ErpUserDetails erpUserDetails = (ErpUserDetails) principal;
			firstname = erpUserDetails.getFirstname();
			lastname = erpUserDetails.getLastname();
		}
		else if(principal instanceof ErpOAuthUser) {
			ErpOAuthUser erpOAuthUser = (ErpOAuthUser) principal;
			firstname = erpOAuthUser.getFirstname();
			lastname = erpOAuthUser.getLastname();
		}
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
