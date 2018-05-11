package com.tsm.cards.service;

import com.tsm.cards.exceptions.BadRequestException;
import com.tsm.cards.exceptions.ForbiddenRequestException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class AssertClientRequestTest {

    @InjectMocks
    private AssertClientRequest service;

    private String createClientKey = "qwetyuasdtyueyoiuwyrw";

    private String clientServiceUrl = "/api/v1/clients";

    private String validClientUrl = "http://localhost:8080/api/v1/clients";

    private String invalidKey = "qwetyuasdtyuey";

    private String messageUrl = "http://localhost:8080/api/v1/";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "clientServiceKey", createClientKey);
        ReflectionTestUtils.setField(service, "clientServiceUrl", clientServiceUrl);
    }

    @Test(expected = BadRequestException.class)
    public void assertClientHeader_NullAdminHeaderGiven__ShouldThrowException() {
        // Set Up
        String adminHeader = null;

        // Do test
        service.assertClientHeader(adminHeader);
    }

    @Test(expected = ForbiddenRequestException.class)
    public void assertClientHeader_InvalidAdminHeaderGiven__ShouldThrowException() {
        // Set Up
        String adminHeader = invalidKey;

        // Do test
        service.assertClientHeader(adminHeader);
    }

    @Test
    public void assertClientHeader_ValidAdminHeaderGiven__ShouldThrowException() {
        // Set Up
        String adminHeader = createClientKey;

        // Do test
        service.assertClientHeader(adminHeader);
    }

    @Test(expected = BadRequestException.class)
    public void assertClientHeader_EmptyAdminHeaderGiven__ShouldThrowException() {
        // Set Up
        String adminHeader = "";

        // Do test
        service.assertClientHeader(adminHeader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAllowedRequestWithNoClient_NullRequestUrlGiven__ShouldThrowException() {
        // Set Up
        String requestUrl = null;
        String adminToken = createClientKey;

        // Do test
        service.isRequestAllowedCheckingRequestUrl(requestUrl, adminToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAllowedRequestWithNoClient_NullAdminTokenGiven__ShouldThrowException() {
        // Set Up
        String requestUrl = validClientUrl;
        String adminToken = null;

        // Do test
        service.isRequestAllowedCheckingRequestUrl(requestUrl, adminToken);
    }

    @Test
    public void isAllowedRequestWithNoClient_ValidRequestUrlGiven_ShouldReturnTrue() {
        // Set Up
        String requestUrl = validClientUrl;
        String adminToken = createClientKey;

        // Do test
        Boolean result = service.isRequestAllowedCheckingRequestUrl(requestUrl, adminToken);

        // Assertions
        assertNotNull(result);
        assertThat(result, is(true));

    }

    @Test
    public void isAllowedRequestWithNoClient_InValidRequestUrlGiven_ShouldReturnFalse() {
        // Set Up
        String requestUrl = messageUrl;
        String adminToken = createClientKey;

        // Do test
        Boolean result = service.isRequestAllowedCheckingRequestUrl(requestUrl, adminToken);

        // Assertions
        assertNotNull(result);
        assertThat(result, is(false));

    }

    @Test
    public void isAllowedRequestWithNoClient_InValidKeyGiven_ShouldReturnFalse() {
        // Set Up
        String requestUrl = messageUrl;
        String adminToken = invalidKey;

        // Do test
        Boolean result = service.isRequestAllowedCheckingRequestUrl(requestUrl, adminToken);

        // Assertions
        assertNotNull(result);
        assertThat(result, is(false));

    }

}
