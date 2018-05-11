package com.tsm.cards.definition.service;

import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.documents.SynonymsDefinition;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
@SuppressWarnings("unchecked")
public class SynonymsManageWordServiceTest {

    @InjectMocks
    private SynonymsManageWordService service;

    @Mock
    private SynonymsDefinitionService mockDefinitionSynonymsService;

    @Mock
    private SynonymsOxfordService mockOxfordService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createDefinition_NullWordGiven_ShouldThrowException() throws Exception {
        // Set up
        String word = null;

        // Do test
        try {
            service.createDefinition(word);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void createDefinition_ValidWorkGiven_ShouldNotFoundException() throws Exception {
        // Set up
        String word = "home";

        // Expectations
        when(mockOxfordService.find(word)).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            service.createDefinition(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockOxfordService).find(word);
        verifyZeroInteractions(mockDefinitionSynonymsService);
    }

    @Test
    public void createDefinition_ValidWorkGiven() throws Exception {
        // Set up
        String word = "home";
        SynonymsDefinition definition = new SynonymsDefinition();

        // Expectations
        when(mockOxfordService.find(word)).thenReturn(definition);
        when(mockDefinitionSynonymsService.save(definition)).thenReturn(definition);

        // Do test
        BaseDefinition result = service.createDefinition(word);

        // Assertions
        verify(mockOxfordService).find(word);
        verify(mockDefinitionSynonymsService).save(definition);

        assertThat(result, is(definition));
        assertThat(word, is(result.getId()));
    }
}
