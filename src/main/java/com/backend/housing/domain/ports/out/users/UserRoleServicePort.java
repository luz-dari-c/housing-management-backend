package com.backend.housing.domain.ports.out.users;

import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.entity.users.UserBasicInfo;

import java.util.Optional;
import java.util.Set;

public interface UserRoleServicePort {

    boolean isUserOwner(Long userId);
    boolean isUserTenant(Long userId);
    boolean isUserBuyer(Long userId);

    User assignOwnerRole(Long userId);
    User assignTenantRole(Long userId);
    User assignBuyerRole(Long userId);
    User assignAdminRole(Long userId);

    User removeOwnerRole(Long userId);
    User removeTenantRole(Long userId);

    Optional<UserBasicInfo> getUserBasicInfo(Long userId);
    Optional<String> getUserEmail(Long userId);
    Optional<String> getUserName(Long userId);

    Set<String> getUserRoles(Long userId);

    boolean userExists(Long userId);
}