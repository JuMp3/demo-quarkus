package it.jump3.dao.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import it.jump3.enumz.UserStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_T", schema = "public")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@NoArgsConstructor
@RegisterForReflection
public class User implements Serializable {

    private static final long serialVersionUID = 3245809205175185789L;

    @Id
    @Column(name = "USERNAME", nullable = false)
    @Schema(description = "Username", example = "mrossi", required = true)
    @EqualsAndHashCode.Include
    private String username;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column(name = "INSERT_TIME", nullable = false)
    private LocalDateTime insertTime;

    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;

    @Version
    private Long version;
}
