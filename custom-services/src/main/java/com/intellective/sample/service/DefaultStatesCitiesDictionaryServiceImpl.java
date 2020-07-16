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
