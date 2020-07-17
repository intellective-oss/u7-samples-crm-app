&larr; [Previous step: Implementing the master-detail search template tab](./step3-implementing-master-detail.md)

On this step we are going to create a data service that will implement a set of necessary operations to work with
data sets os US states, cities, and area codes:
  * https://github.com/jasonong/List-of-US-States/blob/master/states.csv
  * https://github.com/ravisorg/Area-Code-Geolocation-Database/blob/master/us-area-code-cities.csv

Project template provides a `custom-services` modules allowing adding any server-side code using Spring 4 including
general beans and MVC capabilities. 

# Implementing data service
At first, we should introduce model objects and appropriate interface declaring all the operations we need.

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

&rarr; [Next step: Implementing custom selectors and criteria field validation](./step5-selectors-and-validation.md)