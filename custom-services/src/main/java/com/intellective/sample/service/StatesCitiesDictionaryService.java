package com.intellective.sample.service;

import com.intellective.sample.model.State;

import java.util.Collection;

public interface StatesCitiesDictionaryService {

    /**
     * Fetch full list of states
     * @return
     */
    Collection<State> listStates();

    /**
     * Find a state using its code
     * @param stateCode code of a state
     * @return an object representing a state or null if it doesn't exist
     */
    State getStateByCode(String stateCode);

}
