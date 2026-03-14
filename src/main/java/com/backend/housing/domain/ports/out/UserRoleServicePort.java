
package com.backend.housing.domain.ports.out;

import java.util.Optional;

import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.UserBasicInfo;

public interface UserRoleServicePort {

    
    boolean isUserOwner(String userEmail);

    
    User assignOwnerRole(String userEmail);

   
    boolean isUserTenant(String userEmail);

   
    Optional<UserBasicInfo> getUserBasicInfo(String userEmail);
}