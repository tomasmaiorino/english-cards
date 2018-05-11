package com.tsm.cards.definition.service;

import com.tsm.cards.definition.repository.SentencesDefinitionRepository;
import com.tsm.cards.documents.BaseDefinition;
import com.tsm.cards.documents.SentencesDefinition;
import com.tsm.cards.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class SentencesDefinitionServiceTest {

    @InjectMocks
    private SentencesDefinitionService service;

    @Mock
    private SentencesDefinitionRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_NullOriginalGiven_ShouldThrowException() {
        // Set up
        SentencesDefinition definition = null;

        // Do test
        try {
            service.save(definition);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    @Test
    public void save_ValidOriginalGiven_ShouldSave() {
        // Set up
        SentencesDefinition definition = (SentencesDefinition) buildDefinition("home");

        // Expectations
        when(mockRepository.save(definition)).thenReturn(definition);

        // Do test
        SentencesDefinition returns = service.save(definition);

        // Assertions
        verify(mockRepository).save(definition);

        assertThat(returns, is(definition));
    }

    //
    @Test
    public void findById_NullWordId_ShouldThrowException() {
        // Set up
        String word = null;

        // Do test
        try {
            service.findById(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    //
    @Test
    public void findById_ValidWordId_ShouldReturnDefinitionNotFound() {
        // Set up
        String word = "home";

        // Expectations
        Optional<SentencesDefinition> emptyOptional = Optional.empty();
        when(mockRepository.findById(word)).thenReturn(emptyOptional);

        // Do test
        try {
            service.findById(word);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(mockRepository).findById(word);
    }

    //
    @Test
    public void findById_ValidWordIdGiven_DefinitionFound() {
        // Set up
        String word = "word";

        // Expectations
        SentencesDefinition definition = (SentencesDefinition) buildDefinition(word);
        Optional<SentencesDefinition> expectedOptional = Optional.of(definition);
        when(mockRepository.findById(word)).thenReturn(expectedOptional);

        // Do test
        BaseDefinition result = service.findById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, is(definition));
    }

    //
    @Test
    public void findByDefinitionId_NullDefinitionIdGiven_ShouldThrowException() {
        // Set up
        String definitionId = null;

        // Do test
        try {
            service.findByDefinitionId(definitionId);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    //
    @Test
    public void findByDefinitionId_ValidDefinitionIdGiven_NotResultFound() {
        // Set up
        String definitionId = "123";

        // Expectations
        Optional<SentencesDefinition> emptyOptional = Optional.empty();
        when(mockRepository.findByDefinitionId(definitionId)).thenReturn(emptyOptional);

        // Do test
        BaseDefinition result = service.findByDefinitionId(definitionId);

        // Assertions
        verify(mockRepository).findByDefinitionId(definitionId);
        assertThat(result, nullValue());
    }

    //
    @Test
    public void findByDefinitionId_ValidDefinitionIdGiven_ShouldReturnDefinition() {
        // Set up
        String definitionId = "123";
        String word = "home";
        SentencesDefinition definition = (SentencesDefinition) buildDefinition(word);

        // Expectations
        Optional<SentencesDefinition> emptyOptional = Optional.of(definition);
        when(mockRepository.findByDefinitionId(definitionId)).thenReturn(emptyOptional);

        // Do test
        BaseDefinition result = service.findByDefinitionId(definitionId);

        // Assertions
        verify(mockRepository).findByDefinitionId(definitionId);
        assertThat(result, notNullValue());
        assertThat(result, is(definition));
    }

    //
    @Test
    public void findOptionalDefinitionById_NullIdGiven_ShouldThrowException() {
        // Set up
        String word = null;

        // Do test
        try {
            service.findOptionalDefinitionById(word);
            fail();
        } catch (IllegalArgumentException e) {
        }

        // Assertions
        verifyZeroInteractions(mockRepository);
    }

    //
    @Test
    public void findOptionalDefinitionById_DefinitionNotFound() {
        // Set up
        String word = "word";

        // Expectations
        when(mockRepository.findById(word)).thenReturn(Optional.empty());

        // Do test
        Optional<SentencesDefinition> result = service.findOptionalDefinitionById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), is(false));
    }

    //
    @Test
    public void findOptionalDefinitionById_DefinitionFound() {
        // Set up
        String word = "word";
        SentencesDefinition call = (SentencesDefinition) buildDefinition(word);
        Optional<SentencesDefinition> optional = Optional.of(call);

        // Expectations
        when(mockRepository.findById(word)).thenReturn(optional);

        // Do test
        Optional<SentencesDefinition> result = service.findOptionalDefinitionById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(call));
    }

    private BaseDefinition buildDefinition(String id) {
        SentencesDefinition call = new SentencesDefinition();
        call.setId(id);
        return call;
    }
}
