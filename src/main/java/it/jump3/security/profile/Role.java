package it.jump3.security.profile;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(implementation = Role.class)
public enum Role {
    EVERYONE,
    USER,
    ADMIN
}