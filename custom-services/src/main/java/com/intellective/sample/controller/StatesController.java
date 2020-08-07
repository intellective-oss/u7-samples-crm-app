package com.intellective.sample.controller;

import com.intellective.sample.model.City;
import com.intellective.sample.model.OperationResult;
import com.intellective.sample.model.State;
import com.intellective.sample.service.StatesCitiesDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/1.0/states")
public class StatesController {

    @Autowired
    private StatesCitiesDictionaryService statesCitiesDictionaryService;

    @GetMapping
    public Collection<State> listStates() {
        return statesCitiesDictionaryService.listStates();
    }

    @GetMapping("/{stateCode}")
    public State getState(@PathVariable String stateCode) {
        State state = statesCitiesDictionaryService.getStateByCode(stateCode);
        if (state == null) {
            throw new StateOrCityNotFoundException("State not found by code " + stateCode);
        }
        return state;
    }

    @GetMapping("/{stateCode}/{cityName}")
    public City getCity(@PathVariable String stateCode, @PathVariable String cityName) {
        State state = getState(stateCode);
        City city = state.cities().get(cityName);
        if (city == null) {
            throw new StateOrCityNotFoundException("City not found by name " + cityName);
        }
        return city;
    }

    @PostMapping("/{stateCode}/{cityName}/validate")
    public OperationResult validateCode(@PathVariable String stateCode, @PathVariable String cityName, @RequestParam String areaCode) {
        try {
            City city = getCity(stateCode, cityName);
            boolean valid = city.areaCodes().stream().anyMatch(s -> areaCode.equals(s));
            return OperationResult.success(Collections.singletonMap("valid", valid));
        } catch (StateOrCityNotFoundException e) {
            return OperationResult.error(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class StateOrCityNotFoundException extends RuntimeException {
        public StateOrCityNotFoundException(String message) {
            super(message);
        }
    }

}
