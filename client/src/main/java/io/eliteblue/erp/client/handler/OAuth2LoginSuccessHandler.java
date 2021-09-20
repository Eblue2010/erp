package io.eliteblue.erp.client.handler;

import io.eliteblue.erp.core.model.security.Authority;
import io.eliteblue.erp.core.model.security.AuthorityName;
import io.eliteblue.erp.core.model.security.ErpOAuthUser;
import io.eliteblue.erp.core.model.security.ErpUser;
import io.eliteblue.erp.core.service.ErpUserService;
import io.eliteblue.erp.client.util.PasswordGenerator;
import io.eliteblue.erp.core.constants.DataOperation;
import io.eliteblue.erp.core.model.OperationsArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final static String USER_NOT_FOUND = "user with username %s not found";

    @Autowired
    private ErpUserService erpUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        ErpOAuthUser oAuthUser = (ErpOAuthUser) authentication.getPrincipal();
        boolean isLogout = false;

        if(oAuthUser != null) {
            String username = (String) oAuthUser.getAttributes().get("email");
            ErpUser erpUser = erpUserService.getErpUser(username);
            System.out.println("ERP_USER: "+erpUser);

            if(erpUser != null) {
                // exists
                // check if user is enabled
                System.out.println("ENABLED: "+erpUser.getEnabled());
                if(erpUser.getEnabled()) {
                    oAuthUser.setUsername(erpUser.getUsername());
                    oAuthUser.setFirstname(erpUser.getFirstname());
                    oAuthUser.setLastname(erpUser.getLastname());
                    oAuthUser.setEmail(erpUser.getEmail());
                    oAuthUser.setAuthorities(new ArrayList<>());
                    List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
                    for (Authority a : erpUser.getAuthorities()) {
                        auths.add(new SimpleGrantedAuthority(a.getName().name()));
                    }
                    oAuthUser.setAuthorities(auths);
                    oAuthUser.setLastLogged(new Date());
                    oAuthUser.setLocked(false);
                    oAuthUser.setEnabled(erpUser.getEnabled());
                    oAuthUser.setOperationsAreas(new ArrayList<>(erpUser.getLocations()));
                } else {
                    isLogout = true;
                }
            }
            else {
                oAuthUser.setUsername(username);
                oAuthUser.setFirstname((String) oAuthUser.getAttributes().get("given_name"));
                oAuthUser.setLastname((String) oAuthUser.getAttributes().get("family_name"));
                oAuthUser.setEmail((String) oAuthUser.getAttributes().get("email"));
                List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
                auths.add(new SimpleGrantedAuthority(AuthorityName.ROLE_USER.name()));
                oAuthUser.setAuthorities(auths);
                oAuthUser.setLastLogged(new Date());
                oAuthUser.setLocked(false);
                oAuthUser.setEnabled(false);

                // save ErpUser
                ErpUser user = new ErpUser();
                user.setUsername(oAuthUser.getUsername());
                user.setPassword(PasswordGenerator.generateStrongPassword());
                user.setFirstname(oAuthUser.getFirstname());
                user.setLastname(oAuthUser.getLastname());
                user.setEmail(oAuthUser.getEmail());
                Set<Authority> au = new HashSet<Authority>();
                au.add(erpUserService.findAuthorityByName(AuthorityName.ROLE_USER));
                user.setAuthorities(au);
                user.setLastUpdate(new Date());
                user.setDateCreated(new Date());
                user.setOperation(DataOperation.CREATED.name());
                user.setLastPasswordResetDate(new Date());
                user.setEnabled(false);
                OperationsArea area = erpUserService.findAreaByLocation("FIELD OFFICE");
                if(area != null) {
                    user.setLocations(new HashSet<>());
                    user.getLocations().add(area);
                }

                try {
                    erpUserService.save(user);
                } catch (Exception e) {
                    super.logger.error(e);
                    isLogout = true;
                }
                System.out.println("OAUTHUSER NAME: "+oAuthUser.getName());
                System.out.println("CLIENT NAME: "+oAuthUser.getClientname());
                isLogout = true;
            }
            System.out.println("OAuth2 Username: " + oAuthUser.getName());
            System.out.println("Attributes: " + oAuthUser.getAttributes());
        } else {
            isLogout = true;
        }

        if(!isLogout) {
            super.setDefaultTargetUrl("/");
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            handleLogout(request, response, authentication);
        }
    }

    public void handleLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        super.setDefaultTargetUrl("/login.xhtml?authfailed=true");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
