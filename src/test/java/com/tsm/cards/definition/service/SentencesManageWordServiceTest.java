package com.tsm.cards.definition.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.documents.SentencesDefinition;
import com.tsm.cards.exceptions.ResourceNotFoundException;

@FixMethodOrder(MethodSorters.JVM)
@SuppressWarnings("unchecked")
public class SentencesManageWordServiceTest {

    @InjectMocks
    private SentencesManageWordService service;

    @Mock
    private SentencesDefinitionService mockDefinitionSentencesService;

    @Mock
    private SentencesOxfordService mockOxfordService;

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
        verifyZeroInteractions(mockDefinitionSentencesService);
    }

    @Test
    public void createDefinition_ValidWorkGiven() throws Exception {
        // Set up
        String word = "home";
        SentencesDefinition definition = new SentencesDefinition();

        // Expectations
        when(mockOxfordService.find(word)).thenReturn(definition);
        when(mockDefinitionSentencesService.save(definition)).thenReturn(definition);

        // Do test
        BaseDefinition result = service.createDefinition(word);

        // Assertions
        verify(mockOxfordService).find(word);
        verify(mockDefinitionSentencesService).save(definition);

        assertThat(result, is(definition));
        assertThat(word, is(result.getId()));
    }
}
