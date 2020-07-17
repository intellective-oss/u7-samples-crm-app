&larr; [Previous step: Implementing the master-detail search template tab](./step3-implementing-master-detail.md)

On this step we are going to create a data service that will implement a set of necessary operations to work with
data sets os US states, cities, and area codes:
  * https://github.com/jasonong/List-of-US-States/blob/master/states.csv
  * https://github.com/ravisorg/Area-Code-Geolocation-Database/blob/master/us-area-code-cities.csv

Project template provides a `custom-services` modules allowing adding any server-side code using Spring 4 including
general beans and MVC capabilities. 

# Implementing data service
At first, we should introduce model objects and appropriate interface declaring all the operations we need.

[`com.intelective.sample.model.State`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/java/com/intellective/sample/model/State.java)
```java
package com.intellective.sample.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class State {

    private final String stateCode;
    private final String displayValue;
    private final Map<String,City> cities;

    public State(String stateCode, String displayValue) {
        this.stateCode = stateCode;
        this.displayValue = displayValue;
        this.cities = new HashMap<>();
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public Map<String, City> cities() {
        return cities;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateCode='" + stateCode + '\'' +
                ", displayValue='" + displayValue + '\'' +
                '}';
    }

}
```

[`com.intelective.sample.model.City`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/java/com/intellective/sample/model/City.java)
```java
package com.intellective.sample.model;

import java.util.LinkedList;
import java.util.List;

public class City {

    private String name;
    private List<String> areaCodes = new LinkedList<>();

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> areaCodes() {
        return areaCodes;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", areaCodes='" + areaCodes.toString() + '\'' +
                '}';
    }

}
```
[`com.intelective.sample.service.StatesCitiesDictionaryService`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/java/com/intellective/sample/service/StatesCitiesDictionaryService.java)
```java
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
```
During the further development you will see that we need only these 2 operations on the service layer.

&rarr; [Next step: Implementing custom selectors and criteria field validation](./step5-selectors-and-validation.md)