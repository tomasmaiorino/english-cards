package com.tsm.cards.model;

import lombok.Getter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client extends BaseModel {

    @Id
    @GeneratedValue
    @Getter
    private Integer id;

    public enum ClientStatus {
        ACTIVE, INACTIVE;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client")
    private Set<ClientHosts> clientHosts;

    @Getter
    @Column(nullable = false, length = 30)
    private String name;

    @Getter
    @Column(nullable = false, length = 30)
    private String token;

    @Getter
    @Column(nullable = false, length = 30)
    private String email;

    @Getter
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @Getter
    @Column(nullable = false, length = 30)
    private String emailRecipient;

    public void setClientHosts(final Set<ClientHosts> clientHosts) {
        Assert.notNull(clientHosts, "The clientHosts must not be null!");
        Assert.isTrue(!clientHosts.isEmpty(), "The clientHosts must not be empty!");
        this.clientHosts = clientHosts;
    }

    public void setName(final String name) {
        Assert.hasText(name, "The name must not be null or empty!");
        this.name = name;
    }

    public void setEmail(final String email) {
        Assert.hasText(email, "The email must not be null or empty!");
        this.email = email;
    }

    public void setEmailRecipient(final String emailRecipient) {
        Assert.hasText(emailRecipient, "The emailRecipient must not be null or empty!");
        this.emailRecipient = emailRecipient;
    }

    public void setToken(final String token) {
        Assert.hasText(token, "The token must not be null or empty!");
        this.token = token;
    }

    public void setClientStatus(final ClientStatus status) {
        Assert.notNull(status, "The status must not be null!");
        this.status = status;
    }

    public Set<ClientHosts> getClientHosts() {
        return clientHosts;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Client other = (Client) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
