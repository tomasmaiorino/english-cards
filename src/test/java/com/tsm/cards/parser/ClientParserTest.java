package com.tsm.cards.parser;

import com.tsm.cards.model.Client;
import com.tsm.cards.model.Client.ClientStatus;
import com.tsm.cards.resources.ClientResource;
import com.tsm.cards.util.ClientTestBuilder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class ClientParserTest {

    private ClientParser parser = new ClientParser();

    @Test(expected = IllegalArgumentException.class)
    public void toModel_NullResourceGiven_ShouldThrowException() {
        // Set up
        ClientResource resource = null;

        // Do test
        parser.toModel(resource);
    }

    @Test
    public void toModel_ValidResourceGiven_ShouldCreateClientModel() {
        // Set up
        ClientResource resource = ClientTestBuilder.buildResoure();

        // Do test
        Client result = parser.toModel(resource);

        // Assertions
        assertNotNull(result);
        assertThat(result, allOf(
            hasProperty("id", nullValue()),
            hasProperty("name", is(resource.getName())),
            hasProperty("token", is(resource.getToken())),
            hasProperty("emailRecipient", is(resource.getEmailRecipient())),
            hasProperty("clientHosts", notNullValue()),
            hasProperty("status", is(ClientStatus.ACTIVE))));

        assertThat(result.getClientHosts().size(), is(resource.getHosts().size()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toResource_NullClientGiven_ShouldThrowException() {
        // Set up
        Client client = null;

        // Do test
        parser.toResource(client);
    }

    @Test
    public void toResource_ValidClientGiven_ShouldCreateResourceModel() {
        // Set up
        Client client = ClientTestBuilder.buildModel();

        // Do test
        ClientResource result = parser.toResource(client);

        // Assertions
        assertNotNull(result);
        assertThat(result, allOf(
            hasProperty("id", is(client.getId())),
            hasProperty("name", is(client.getName())),
            hasProperty("token", is(client.getToken())),
            hasProperty("emailRecipient", is(client.getEmailRecipient())),
            hasProperty("hosts", notNullValue()),
            hasProperty("status", is(ClientStatus.ACTIVE.name()))));

        assertThat(result.getHosts().size(), is(client.getClientHosts().size()));
    }
}
