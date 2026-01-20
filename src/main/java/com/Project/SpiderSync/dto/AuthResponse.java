package com.Project.SpiderSync.dto;

import java.util.Objects;

public class AuthResponse {
  private String token;
  private String username;
  private String role;

  public AuthResponse() {
  }

  public AuthResponse(String token, String username, String role) {
    this.token = token;
    this.username = username;
    this.role = role;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    AuthResponse that = (AuthResponse) o;
    return Objects.equals(token, that.token) && Objects.equals(username, that.username)
        && Objects.equals(role, that.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, username, role);
  }

  @Override
  public String toString() {
    return "AuthResponse{" +
        "token='" + token + '\'' +
        ", username='" + username + '\'' +
        ", role='" + role + '\'' +
        '}';
  }
}
