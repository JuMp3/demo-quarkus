package it.jump3.enumz;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(implementation = UserStatusEnum.class)
public enum UserStatusEnum {
    A, D
}
