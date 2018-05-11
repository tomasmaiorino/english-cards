package com.tsm.cards.parser;

import com.tsm.cards.model.Client;
import com.tsm.cards.model.Client.ClientStatus;
import com.tsm.cards.model.ClientHosts;
import com.tsm.cards.resources.ClientResource;
import com.tsm.cards.resources.IParser;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClientParser implements IParser<ClientResource, Client> {

	public Client toModel(final ClientResource resource) {
		Assert.notNull(resource, "The resource must not be null!");
		Client client = new Client();
		client.setClientStatus(ClientStatus.valueOf(resource.getStatus()));
		client.setEmail(resource.getEmail());
		client.setName(resource.getName());
		client.setToken(resource.getToken());
		client.setClientHosts(loadClientHosts(client, resource.getHosts()));
		client.setEmailRecipient(resource.getEmailRecipient());
		return client;
	}

	private Set<ClientHosts> loadClientHosts(final Client client, final Set<String> hosts) {
		Set<ClientHosts> clientHosts = new HashSet<>();
		hosts.forEach(h -> {
			ClientHosts host = new ClientHosts();
			host.setHost(h);
			host.setClient(client);
			clientHosts.add(host);
		});
		return clientHosts;
	}

	public ClientResource toResource(final Client client) {
		Assert.notNull(client, "The client must not be null!");
		ClientResource resource = new ClientResource();
		resource.setId(client.getId());
		resource.setEmail(client.getEmail());
		resource.setName(client.getName());
		resource.setToken(client.getToken());
		resource.setStatus(client.getStatus().toString());
		resource.setId(client.getId());
		resource.setHosts(loadHosts(client));
		resource.setEmailRecipient(client.getEmailRecipient());
		return resource;

	}

	private Set<String> loadHosts(final Client client) {
		return client.getClientHosts().stream().map(ClientHosts::getHost).collect(Collectors.toSet());
	}

	@Override
	public Set<ClientResource> toResources(Set<Client> models) {
		// TODO Auto-generated method stub
		return null;
	}

}
