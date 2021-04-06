package it.jump3.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLE_T", schema = "public")
@ToString
@Data
@NoArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = -6673293266451391597L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CODE", nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Role code", example = "ADMIN, USER", required = true)
    private it.jump3.security.profile.Role code;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
