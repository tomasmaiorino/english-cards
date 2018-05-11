package com.tsm.cards.service;

import com.tsm.cards.model.Client.ClientStatus;
import com.tsm.cards.model.ClientHosts;
import com.tsm.cards.repository.ClientHostsRepository;
import com.tsm.cards.util.ClientTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class ClientHostsServiceTest {

    @InjectMocks
    private ClientHostsService service;

    @Mock
    private ClientHostsRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByClientStatus_EmptyClientTokenGiven_ShouldThrowException() {
        // Set up
        ClientStatus status = null;

        // Do test
        try {
            service.findByClientStatus(status);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void findByToken_ClientHostsNotFound_ShouldThrowException() {
        // Set up
        ClientStatus status = ClientStatus.ACTIVE;

        // Expectations
        when(mockRepository.findByClientStatus(status)).thenReturn(Collections.emptySet());

        // Do test
        Set<ClientHosts> result = service.findByClientStatus(status);

        // Assertions
        verify(mockRepository).findByClientStatus(status);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void findByToken_ClientFound_ShouldReturnClient() {
        // Set up
        ClientStatus status = ClientStatus.ACTIVE;
        Set<ClientHosts> clientHosts = ClientTestBuilder.buildClientHost();

        // Expectations
        when(mockRepository.findByClientStatus(status)).thenReturn(clientHosts);

        // Do test
        Set<ClientHosts> result = service.findByClientStatus(status);

        // Assertions
        verify(mockRepository).findByClientStatus(status);

        assertNotNull(result);
        assertThat(result.isEmpty(), is(false));
        assertThat(result.size(), is(clientHosts.size()));
    }

}
