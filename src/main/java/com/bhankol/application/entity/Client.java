package com.bhankol.application.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by pravingosavi on 21/05/18.
 */
@Document(collection = "clients")
public class Client {

    @Id
    private String clientid;

    private String clientname;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (clientid != null ? !clientid.equals(client.clientid) : client.clientid != null) return false;
        return clientname != null ? clientname.equals(client.clientname) : client.clientname == null;

    }

    @Override
    public int hashCode() {
        int result = clientid != null ? clientid.hashCode() : 0;
        result = 31 * result + (clientname != null ? clientname.hashCode() : 0);
        return result;
    }
}
