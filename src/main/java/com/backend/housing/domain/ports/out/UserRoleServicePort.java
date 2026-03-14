

package com.backend.housing.domain.ports.out;

import java.util.Optional;
import java.util.Set;

import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.UserBasicInfo;

public interface UserRoleServicePort {

    
    boolean isUserOwner(String userEmail);
    boolean isUserTenant(String userEmail);
    
   
    User assignOwnerRole(String userEmail);
    User assignTenantRole(String userEmail);
    User assignBuyerRole(String userEmail);
    
    
    Optional<UserBasicInfo> getUserBasicInfo(String userEmail);
    
    
    Set<String> getUserRoles(String userEmail);
}