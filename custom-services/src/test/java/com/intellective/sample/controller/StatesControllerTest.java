package com.intellective.sample.controller;

import com.intellective.sample.model.City;
import com.intellective.sample.model.OperationResult;
import com.intellective.sample.model.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:configuration/custom-services.xml"
})
public class StatesControllerTest {

    @Autowired
    StatesController statesController;

    @Test
    public void getState() {
        try {
            State state = statesController.getState("CA");
            assertNotNull(state);
            assertEquals("California", state.getDisplayValue());
            assertFalse(state.cities().isEmpty());
        } catch (StatesController.StateOrCityNotFoundException notFoundException) {
            fail();
        }

        try {
            State state = statesController.getState("XX");
            fail();
        } catch (StatesController.StateOrCityNotFoundException notFoundException) {
            // expected exception
        }
    }

    @Test
    public void getCity() {
        try {
            City city = statesController.getCity("CA", "Irvine");
            assertNotNull(city);
            assertEquals("949", city.areaCodes().get(0));
        } catch (StatesController.StateOrCityNotFoundException notFoundException) {
            fail();
        }

        try {
            City city = statesController.getCity("CA", "Exception Town");
            fail();
        } catch (StatesController.StateOrCityNotFoundException notFoundException) {
            // expected exception
        }
    }

    @Test
    public void validateCode() {
        OperationResult r = statesController.validateCode("CA", "Irvine", "949");
        assertTrue(r.isSuccess() && r.getPayload().get("valid").equals(true));

        r = statesController.validateCode("CA", "Irvine", "999");
        assertTrue(r.isSuccess() && r.getPayload().get("valid").equals(false));

        r = statesController.validateCode("XX", "YYYY", "111");
        assertFalse(r.isSuccess());
    }
}