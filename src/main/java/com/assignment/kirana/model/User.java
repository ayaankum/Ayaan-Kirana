package com.assignment.kirana.model;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user") // Specify the MongoDB collection name
public class User {
  // MongoDB will generate the ID
  @Id private String _id; // Change the type to String for MongoDB's ObjectId
  private String userName;
  private String password;
  private String email;

  @DBRef // Reference to Role documents
  private Set<Role> role = new HashSet<>();

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<Role> getRole() {
    return role;
  }

  public void setRole(Set<Role> role) {
    this.role = role;
  }
}
