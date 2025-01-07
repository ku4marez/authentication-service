package com.github.ku4marez.authenticationservice.configuration;

import com.github.ku4marez.authenticationservice.entity.UserEntity;
import com.github.ku4marez.commonlibraries.entity.enums.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        initializeUsersCollection();
    }

    // =================== USERS COLLECTION ===================
    private void initializeUsersCollection() {
        String collectionName = "users";

        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
        applyUserValidationRules(collectionName);
        seedDefaultUsers();
    }

    private void applyUserValidationRules(String collectionName) {
        String validationSchema = """
        {
            "$jsonSchema": {
                "bsonType": "object",
                "required": ["firstName", "lastName", "email", "password", "role"],
                "properties": {
                    "firstName": { "bsonType": "string" },
                    "lastName": { "bsonType": "string" },
                    "email": {
                        "bsonType": "string",
                        "pattern": "^.+@.+\\\\..+$"
                    },
                    "password": { "bsonType": "string" },
                    "role": {
                        "bsonType": "string",
                        "enum": ["ADMIN", "DOCTOR", "PATIENT"]
                    },
                    "createdBy": { "bsonType": "string" },
                    "updatedBy": { "bsonType": "string" },
                    "creationDate": { "bsonType": "date" },
                    "updatedDate": { "bsonType": "date" }
                }
            }
        }
        """;

        mongoTemplate.executeCommand("""
        {
            "collMod": "%s",
            "validator": %s,
            "validationLevel": "strict",
            "validationAction": "error"
        }
        """.formatted(collectionName, validationSchema));
    }

    private void seedDefaultUsers() {
        if (mongoTemplate.findAll(UserEntity.class).isEmpty()) {
            UserEntity adminUser = new UserEntity();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("securePassword"));
            adminUser.setRole(Role.ADMIN);

            mongoTemplate.save(adminUser);
        }
    }
}
