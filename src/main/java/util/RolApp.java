package util;

import io.javalin.core.security.RouteRole;

public enum RolApp implements RouteRole{
    ROLE_PERSONAL,
    ROLE_ADMIN;
}
