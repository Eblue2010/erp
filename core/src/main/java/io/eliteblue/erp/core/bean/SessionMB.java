package io.eliteblue.erp.core.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.model.security.ErpOAuthUser;
import io.eliteblue.erp.core.model.security.ErpUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@ViewScoped
public class SessionMB implements Serializable {

	private String currentUser;
	private String firstname;
	private String lastname;
	private List<OperationsArea> operationsAreas;

	@PostConstruct
	public void init() {
		currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof ErpUserDetails) {
			ErpUserDetails erpUserDetails = (ErpUserDetails) principal;
			firstname = erpUserDetails.getFirstname();
			lastname = erpUserDetails.getLastname();
			operationsAreas = erpUserDetails.getOperationsAreas();
		}
		else if(principal instanceof ErpOAuthUser) {
			ErpOAuthUser erpOAuthUser = (ErpOAuthUser) principal;
			firstname = erpOAuthUser.getFirstname();
			lastname = erpOAuthUser.getLastname();
			operationsAreas = erpOAuthUser.getOperationsAreas();
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

	public List<OperationsArea> getOperationsAreas() {
		return operationsAreas;
	}

	public void setOperationsAreas(List<OperationsArea> operationsAreas) {
		this.operationsAreas = operationsAreas;
	}
}
