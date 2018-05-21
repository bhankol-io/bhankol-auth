package com.bhankol.application.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by pravingosavi on 21/05/18.
 */

@Document(collection = "roles")
public class Role {

    @Id
    private String roleid;

    private String rolename;

    private String username;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (roleid != null ? !roleid.equals(role.roleid) : role.roleid != null) return false;
        if (rolename != null ? !rolename.equals(role.rolename) : role.rolename != null) return false;
        return username != null ? username.equals(role.username) : role.username == null;

    }

    @Override
    public int hashCode() {
        int result = roleid != null ? roleid.hashCode() : 0;
        result = 31 * result + (rolename != null ? rolename.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
