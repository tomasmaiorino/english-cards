package com.tsm.cards.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.tsm.cards.exceptions.ResourceNotFoundException;
import com.tsm.cards.model.Definition;
import com.tsm.cards.repository.DefinitionRepository;

@FixMethodOrder(MethodSorters.JVM)
public class DefinitionServiceTest {

    @InjectMocks
    private DefinitionService service;

    @Mock
    private DefinitionRepository mockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void save_NullOriginalGiven_ShouldThrowException() {
        // Set up
        Definition definition = null;

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
        Definition definition = buildDefinition("home");

        // Expectations
        when(mockRepository.save(definition)).thenReturn(definition);

        // Do test
        Definition returns = service.save(definition);

        // Assertions
        verify(mockRepository).save(definition);

        assertThat(returns, is(definition));
    }

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

    @Test
    public void findById_ValidWordId_ShouldReturnDefinitionNotFound() {
        // Set up
        String word = "home";

        // Expectations
        Optional<Definition> emptyOptional = Optional.empty();
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

    @Test
    public void findById_ValidWordIdGiven_DefinitionFound() {
        // Set up
        String word = "word";

        // Expectations
        Definition definition = buildDefinition(word);
        Optional<Definition> expectedOptional = Optional.of(definition);
        when(mockRepository.findById(word)).thenReturn(expectedOptional);

        // Do test
        Definition result = service.findById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, is(definition));
    }

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

    @Test
    public void findByDefinitionId_ValidDefinitionIdGiven_NotResultFound() {
        // Set up
        String definitionId = "123";

        // Expectations
        Optional<Definition> emptyOptional = Optional.empty();
        when(mockRepository.findByDefinitionId(definitionId)).thenReturn(emptyOptional);

        // Do test
        Definition result = service.findByDefinitionId(definitionId);

        // Assertions
        verify(mockRepository).findByDefinitionId(definitionId);
        assertThat(result, nullValue());
    }

    @Test
    public void findByDefinitionId_ValidDefinitionIdGiven_ShouldReturnDefinition() {
        // Set up
        String definitionId = "123";
        String word = "home";
        Definition definition = buildDefinition(word);

        // Expectations
        Optional<Definition> optional = Optional.of(definition);
        when(mockRepository.findByDefinitionId(definitionId)).thenReturn(optional);

        // Do test
        Definition result = service.findByDefinitionId(definitionId);

        // Assertions
        verify(mockRepository).findByDefinitionId(definitionId);
        assertThat(result, notNullValue());
        assertThat(result, is(definition));
    }

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

    @Test
    public void findOptionalDefinitionById_DefinitionNotFound() {
        // Set up
        String word = "word";

        // Expectations
        when(mockRepository.findById(word)).thenReturn(Optional.empty());

        // Do test
        Optional<Definition> result = service.findOptionalDefinitionById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void findOptionalDefinitionById_DefinitionFound() {
        // Set up
        String word = "word";
        Definition call = buildDefinition(word);
        Optional<Definition> optional = Optional.of(call);

        // Expectations
        when(mockRepository.findById(word)).thenReturn(optional);

        // Do test
        Optional<Definition> result = service.findOptionalDefinitionById(word);

        // Assertions
        verify(mockRepository).findById(word);
        assertThat(result, notNullValue());
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(call));
    }

    private Definition buildDefinition(String id) {
        Definition call = new Definition();
        call.setId(id);
        return call;
    }
}
