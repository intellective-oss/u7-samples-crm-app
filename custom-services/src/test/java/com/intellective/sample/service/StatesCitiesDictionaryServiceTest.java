package com.intellective.sample.service;

import com.intellective.sample.model.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:configuration/custom-services.xml"
})
public class StatesCitiesDictionaryServiceTest {

    @Autowired
    StatesCitiesDictionaryService statesCitiesDictionaryService;

    @Test
    public void listStates() {
        Collection<State> states = statesCitiesDictionaryService.listStates();

        assertNotNull(states);
        assertFalse(states.isEmpty());
        assertEquals(states.size(), 51);

        State state = states.iterator().next();
        assertState(state);
    }

    @Test
    public void getStateByCode() {
        State california = statesCitiesDictionaryService.getStateByCode("CA");
        assertNotNull(california);
        assertNotNull(california.getDisplayValue());
        assertState(california);

        State inexistent = statesCitiesDictionaryService.getStateByCode("AA");
        assertNull(inexistent);
    }

    private void assertState(State state) {
        assertNotNull(state.getStateCode() != null);
        assertNotNull(state.getDisplayValue() != null);
        assertFalse(state.cities().isEmpty());
    }

}