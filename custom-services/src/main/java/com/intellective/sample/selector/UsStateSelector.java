package com.intellective.sample.selector;

import com.intellective.sample.service.StatesCitiesDictionaryService;
import com.vegaecm.vspace.exception.VuRuntimeException;
import com.vegaecm.vspace.plugins.PluginDescriptor;
import com.vegaecm.vspace.plugins.PluginManager;
import com.vegaecm.vspace.selectors.AbstractCacheSelector;
import com.vegaecm.vspace.selectors.SelectorItem;
import com.vegaecm.vspace.utils.ApplicationContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsStateSelector extends AbstractCacheSelector {

    private StatesCitiesDictionaryService getStatesCitiesDictionaryService() {
        ApplicationContext applicationContext =
                WebApplicationContextUtils.getWebApplicationContext(ApplicationContextHolder.getApplicationContext());
        return applicationContext.getBean(StatesCitiesDictionaryService.class);
    }

    @Override
    protected List<SelectorItem> readData(String s, Map<String, Object> map) throws VuRuntimeException {
        return getStatesCitiesDictionaryService().listStates().stream()
                .map(state -> new SelectorItem(state.getDisplayValue(), state.getStateCode()))
                .collect(Collectors.toList());
    }

    @Override
    public PluginDescriptor getDescriptor() {
        return new PluginDescriptor(
                "UsStateSelector",
                PluginManager.Type.SELECTOR_PLUGIN,
                "Static Selector",
                UsStateSelector.class.getName()
        );
    }

    @Override
    protected boolean timeoutExpire() {
        return false;
    }
}
