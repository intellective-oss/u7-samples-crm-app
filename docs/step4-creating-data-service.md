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

The next possible step, before service implementation, is creating tests:
[`com.intellective.sample.service.StatesCitiesDictionaryServiceTest`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/test/java/com/intellective/sample/service/StatesCitiesDictionaryServiceTest.java)
```java
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
```
These tests assert the core behaviour of the service implementation. Now we will create an implementation relying on this
test.

At first, download the data sets ant put them in the resource directory of the `custom-services` module:
[`custom-services/src/main/resources/states.csv`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/resources/states.csv)
```
"State","Abbreviation"
"Alabama","AL"
"Alaska","AK"
"Arizona","AZ"
"Arkansas","AR"
"California","CA"
...
```

[`custom-services/src/main/resources/us-area-code-cities.csv`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/resources/us-area-code-cities.csv)
 ```
201,Bayonne,"New Jersey",US,40.66871,-74.11431
201,Bergenfield,"New Jersey",US,40.9276,-73.99736
201,"Cliffside Park","New Jersey",US,40.82149,-73.98764
201,Englewood,"New Jersey",US,40.89288,-73.97264
201,"Fair Lawn","New Jersey",US,40.94038,-74.13181
...
``` 

We are going to use the following library to parse CSV data sets:
```xml
    <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-csv</artifactId>
        <version>2.5.3</version>
    </dependency>
```
Add it in the parent `pom.xml` (into `<dependencyManagement>`) and in the `custom-services/pom.xml` (into `<dependencies>`).

Everything is ready to implement a service.
[`com.intellective.sample.service.DefaultStatesCitiesDictionaryServiceImpl`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/java/com/intellective/sample/service/DefaultStatesCitiesDictionaryServiceImpl.java):
```java
package com.intellective.sample.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.intellective.sample.model.City;
import com.intellective.sample.model.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultStatesCitiesDictionaryServiceImpl implements StatesCitiesDictionaryService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultStatesCitiesDictionaryServiceImpl.class);
    private static final String AREA_CODE_FILENAME = "us-area-code-cities.csv";
    private static final String STATES_FILENAME = "states.csv";

    private Map<String,State> statesByCode = new HashMap<>();
    private Map<String,State> statesByName = new HashMap<>();

    public DefaultStatesCitiesDictionaryServiceImpl() {
        logger.info("Initializing default area codes dictionary");

        logger.info("Reading states");
        for (String[] stateRecord : readData(STATES_FILENAME, true)) {
            logger.debug(Arrays.toString(stateRecord));
            State state = new State(stateRecord[1], stateRecord[0]);
            statesByCode.put(state.getStateCode(), state);
            statesByName.put(state.getDisplayValue(), state);
        }

        logger.info("Reading cities data");
        for (String[] cityRecord : readData(AREA_CODE_FILENAME, false)) {
            logger.debug(Arrays.toString(cityRecord));
            State state = statesByName.get(cityRecord[2]);
            if (state != null) {
                City city = state.cities().getOrDefault(cityRecord[1], new City(cityRecord[1]));
                city.areaCodes().add(cityRecord[0]);
                state.cities().put(city.getName(), city);
            } else logger.warn("State not found by name {}", cityRecord[2]);
        }

        logger.info("Done initializing area code dictionary. We have {} states loaded.", statesByName.values().size());
    }

    private List<String[]> readData(String fileName, boolean skipFirst) {
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withSkipFirstDataRow(skipFirst);
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            MappingIterator<String[]> readValues =
                    mapper.reader(String[].class).with(bootstrapSchema).readValues(new ClassPathResource(fileName).getInputStream());
            return readValues.readAll();
        } catch (Exception e) {
            logger.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<State> listStates() {
        return statesByCode.values();
    }

    @Override
    public State getStateByCode(String stateCode) {
        return stateCode != null ? statesByCode.get(stateCode) : null;
    }
}
```
We read states at first, putting them in two maps. After that, we read cities and put them in appropriate states. Run test to ensure the implementation we made works as expected.

To enable Spring managing new beans make sure you have correct package name in the [`configuration\custom-services.xml`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/resources/configuration/custom-services.xml) file.

# REST Endpoint
We are going to publish our data as REST-like service. However, it will only read data, not change it. 
* `GET /1.0/states/{stateCode}` – return `State` object state with information about all the cities;
* `GET /1.0/states/{stateCode}/{cityName}` – return `City` object state with information about the city;
* `POST /1.0/states/{stateCode}/{cityName}/validate` – validates the code posted in the request body against the `State` and the `City` provided.

[`com.intellective.sample.controller.StatesController`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/main/java/com/intellective/sample/controller/StatesController.java)
```java
package com.intellective.sample.controller;

import com.intellective.sample.model.City;
import com.intellective.sample.model.OperationResult;
import com.intellective.sample.model.State;
import com.intellective.sample.service.StatesCitiesDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/1.0/states")
public class StatesController {

    @Autowired
    private StatesCitiesDictionaryService statesCitiesDictionaryService;

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
```
In the validation operation we use `com.intellective.sample.model.OperationResult` wrapper:
```java
package com.intellective.sample.model;

import java.util.Collections;
import java.util.Map;

public class OperationResult {

	public static OperationResult success(Map<String, Object> data) {
		return new OperationResult().withPayload(data);
	}

	private OperationResult withPayload(Map<String, Object> data) {
		this.payload = Collections.unmodifiableMap(data);
		return this;
	}

	public static OperationResult error(String errorMessage) {
		return new OperationResult(errorMessage);
	}
	
	private boolean success = true;
	private String errorMessage;
	private Map<String, Object> payload;

	public OperationResult() {
	}

	public OperationResult(String errorMessage) {
		this.success = false;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
```
Don't forget about a test.
[`com.intellective.sample.controller.StatesControllerTest`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-services/src/test/java/com/intellective/sample/controller/StatesControllerTest.java)
```java
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
```
To let Spring manage beans defined in the `custom-services` module in the context of the web application, 
put the file called [`custom-webapp\src\main\webapp\WEB-INF\configuration\unityApplicationContext.xml`](https://github.com/intellective-oss/u7-samples-crm-app/blob/master/custom-webapp/src/main/webapp/WEB-INF/configuration/unityApplicationContext.xml)
 with the following content:
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd">

	<import resource="classpath:configuration/ucm-core.xml"/>
	<import resource="classpath:configuration/ucm-configuration.xml"/>
	<import resource="classpath:configuration/ucm-security.xml"/>
	<import resource="classpath:configuration/ucm-remote-jbpm.xml"/>
	<import resource="classpath:configuration/ucm-case-management-jbpm.xml"/>
	<import resource="classpath:configuration/ucm-process-management-jbpm.xml"/>
	<import resource="classpath:configuration/ucm-remote-client-icm.xml"/>
	<import resource="classpath:configuration/ucm-case-management-icm.xml"/>
	<import resource="classpath:configuration/ucm-process-management-icm.xml"/>
	<import resource="classpath:configuration/ucm-document-management.xml"/>
	<import resource="classpath:configuration/ucm-application.xml"/>
	<import resource="classpath:configuration/unity-public-api.xml"/>
	<import resource="classpath:configuration/auth-cfg.xml"/>
	<import resource="classpath:configuration/vu-core.xml"/>

    <!-- add custom services to the top spring context -->
	<import resource="classpath:configuration/custom-services.xml"/>

</beans>
```

Rebuild the application and ensure everything's working fine. You can call for the custom API using browser,
curl, Postman, or SoapUI:
* GET `http://localhost:9080/vu/custom-api/1.0/states`
* GET `http://localhost:9080/vu/custom-api/1.0/states/CA`
* GET `http://localhost:9080/vu/custom-api/1.0/states/CA/Irvine`
* POST `http://localhost:9080/vu/custom-api/1.0/states/CA/Irvine` `{ 'areaCode' : '949' }`

We have a running Spring data service, and the REST API we may call from the client side.
On the next step we are going to use it implementing selectors for `State` and `City` fields, as well as
custom validation of the area code for the `PhoneNumber` field. 

&rarr; [Next step: Implementing custom selectors and criteria field validation](./step5-selectors-and-validation.md)