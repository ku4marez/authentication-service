package com.github.ku4marez.authenticationservice.entity;

import com.github.ku4marez.commonlibraries.entity.entity.common.PersistentAuditedEntity;
import com.github.ku4marez.commonlibraries.entity.entity.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends PersistentAuditedEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
