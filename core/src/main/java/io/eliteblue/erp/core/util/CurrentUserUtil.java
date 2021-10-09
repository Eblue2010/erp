package io.eliteblue.erp.core.util;

import io.eliteblue.erp.core.model.OperationsArea;
import io.eliteblue.erp.core.model.security.ErpOAuthUser;
import io.eliteblue.erp.core.model.security.ErpUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrentUserUtil {

    public static String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static ErpUserDetails getUserDetails(Object principal) {
        if(principal instanceof ErpUserDetails) {
            return (ErpUserDetails) principal;
        }
        else {
            return null;
        }
    }

    public static ErpOAuthUser getOAuthUser(Object principal) {
        if(principal instanceof ErpOAuthUser) {
            return (ErpOAuthUser) principal;
        } else {
            return null;
        }
    }

    public static String getFullName() {
        Object principal = getPrincipal();
        if(principal instanceof ErpUserDetails) {
            ErpUserDetails details = getUserDetails(principal);
            return details.getFirstname()+" "+details.getLastname();
        } else {
            ErpOAuthUser oAuthUser = getOAuthUser(principal);
            return oAuthUser.getFirstname()+" "+oAuthUser.getLastname();
        }
    }

    public static List<OperationsArea> getOperationsAreas() {
        Object principal = getPrincipal();
        if(principal instanceof ErpUserDetails) {
            ErpUserDetails details = getUserDetails(principal);
            return details.getOperationsAreas();
        } else {
            ErpOAuthUser oAuthUser = getOAuthUser(principal);
            return oAuthUser.getOperationsAreas();
        }
    }

    public static boolean isAdmin() {
        Object principal = getPrincipal();
        if(principal instanceof ErpUserDetails) {
            ErpUserDetails details = getUserDetails(principal);
            for(GrantedAuthority g: details.getAuthorities()) {
                if(g.getAuthority().equals("ROLE_SYS_ADMIN")) {
                    return true;
                }
            }
            return false;
        } else {
            ErpOAuthUser oAuthUser = getOAuthUser(principal);
            for(GrantedAuthority g: oAuthUser.getAuthorities()) {
                if(g.getAuthority().equals("ROLE_SYS_ADMIN")) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isHeadOffice() {
        List<OperationsArea> areas = getOperationsAreas();
        for(OperationsArea a: areas) {
            if(a.getLocation().equals("HEAD OFFICE")) {
                return true;
            }
        }
        return false;
    }
}
