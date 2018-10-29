package com.emanuel.fridolin.util;

import com.emanuel.fridolin.config.ConfigurationManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public enum PermissionLevel {

    ANYONE, TRUSTED, MODERATOR, ADMIN;

    public static PermissionLevel forUser(User author, Guild guild, ConfigurationManager config) {
        if (guild == null) {
            return PermissionLevel.TRUSTED;
        } else {
            List<Role> roles = guild.getMember(author).getRoles();

            if (roles.stream().anyMatch(role -> role.getName().equals(config.server().adminRoleName()))) {
                return PermissionLevel.ADMIN;
            }

            if (roles.stream().anyMatch(role -> role.getName().equals(config.server().moderatorRoleName()))) {
                return PermissionLevel.MODERATOR;
            }

            if (roles.stream().anyMatch(role -> role.getName().equals(config.server().trustedRoleName()))) {
                return PermissionLevel.TRUSTED;
            }

            return PermissionLevel.ANYONE;
        }
    }

    public boolean isAtLeast(PermissionLevel permissionLevel) {
        switch (permissionLevel) {
            case ADMIN:
                return this == ADMIN;
            case MODERATOR:
                return this == ADMIN || this == MODERATOR;
            case TRUSTED:
                return this == ADMIN || this == MODERATOR || this == TRUSTED;
            case ANYONE:
                return true;
            default:
                return false;
        }
    }
}
