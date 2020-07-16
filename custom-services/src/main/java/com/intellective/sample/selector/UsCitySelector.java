package com.intellective.sample.selector;

import com.google.common.base.Preconditions;
import com.intellective.sample.model.State;
import com.intellective.sample.service.StatesCitiesDictionaryService;
import com.vegaecm.vspace.exception.VuRuntimeException;
import com.vegaecm.vspace.model.Properties;
import com.vegaecm.vspace.model.VarHandler;
import com.vegaecm.vspace.plugins.PluginDescriptor;
import com.vegaecm.vspace.plugins.PluginManager;
import com.vegaecm.vspace.selectors.AbstractCacheSelector;
import com.vegaecm.vspace.selectors.SelectorItem;
import com.vegaecm.vspace.selectors.SelectorResult;
import com.vegaecm.vspace.utils.ApplicationContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsCitySelector extends AbstractCacheSelector {

    private StatesCitiesDictionaryService getStatesCitiesDictionaryService() {
        ApplicationContext applicationContext =
                WebApplicationContextUtils.getWebApplicationContext(ApplicationContextHolder.getApplicationContext());
        return applicationContext.getBean(StatesCitiesDictionaryService.class);
    }

    @Override
    public SelectorResult getData(String filter, VarHandler varHandler,
                                  Integer start, Integer limit,
                                  boolean isFilterEntry, Map<String, Object> properties) throws VuRuntimeException {
        // expose This.${StateCodeProperty} context scope value at properties
        Map<String, Object> localProperties = null != properties ? properties : new HashMap<>();
        String stateCodePropertyName = (String) this.properties.get("StateCodeProperty");
        Preconditions.checkState(stateCodePropertyName != null, "'StateCodeProperty' selector property is not set");
        localProperties.put("StateCodeValue", varHandler.replace("{This." + stateCodePropertyName + "}"));
        return super.getData(filter, varHandler, start, limit, isFilterEntry, localProperties);
    }

    @Override
    protected List<SelectorItem> readData(String resolvedQuery, Map<String, Object> properties) throws VuRuntimeException {
        String stateCodePropertyValue = (String) properties.get("StateCodeValue");
        State state = getStatesCitiesDictionaryService().getStateByCode(stateCodePropertyValue);
        return state != null ? state.cities().values().stream()
                .map(city -> new SelectorItem(city.getName(), city.getName()))
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public PluginDescriptor getDescriptor() {
        return new PluginDescriptor(
                "UsCitySelector",
                PluginManager.Type.SELECTOR_PLUGIN,
                "Static Selector",
                UsCitySelector.class.getName()
        );
    }

    @Override
    protected boolean timeoutExpire() {
        return true;
    }

}
