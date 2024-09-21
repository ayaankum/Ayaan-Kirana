package com.assignment.kirana.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role") // Specify the MongoDB collection name
public class Role {

    @Id // MongoDB will generate the ID
    private String id;
    private String role;

    public Role() {
        super();
    }

    public Role(String role) {
        super();
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
