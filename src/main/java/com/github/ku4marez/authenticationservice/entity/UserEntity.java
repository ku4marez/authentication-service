package com.github.ku4marez.authenticationservice.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import com.github.ku4marez.commonlibraries.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends PersistentAuditedEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
