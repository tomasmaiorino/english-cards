package com.tsm.cards.definition.service;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.JVM)
public class SentencesOxfordServiceTest {


    private static final String API_ID = "123";

	private static final String APY_KEY = "key";

	private static final String OXFORD_SYNONYMS_ENDPOINT = "http://service/X/";

    protected String oxfordServiceApiId;

    protected String oxfordServiceAppkey;

    protected String oxfordServiceSentencesServiceEndpoint;
    
    @InjectMocks
    private SentencesOxfordService service;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new SentencesOxfordService();
        service.setOxfordServiceApiId(API_ID);
        service.setOxfordServiceAppkey(APY_KEY);
        service.setOxfordServiceSentencesServiceEndpoint(OXFORD_SYNONYMS_ENDPOINT);
    }
    
    @Test
    public void find_NullWordGiven_ShouldThrowException() throws Exception {
        // Set up
        String word = null;

        // Do test
        try {
            service.find(word);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

	
}
