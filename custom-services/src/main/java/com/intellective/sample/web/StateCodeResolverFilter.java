package com.intellective.sample.web;

import com.intellective.sample.model.State;
import com.intellective.sample.service.StatesCitiesDictionaryService;
import com.vegaecm.vspace.utils.ApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet filter replacing State field value (a code) by display value
 */
public class StateCodeResolverFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(StateCodeResolverFilter.class);

    public static final String DEFAULT_STATE_PROPERTY_NAME = "State";
    public static final String DEFAULT_CITY_PROPERTY_NAME = "City";

    protected String stateCodePropertyName;
    protected String cityCodePropertyName;

    protected StatesCitiesDictionaryService statesCitiesDictionaryService;

    @Override
    public void init(FilterConfig filterConfig) {
        stateCodePropertyName = filterConfig.getInitParameter("stateCodePropertyName");
        if (null == stateCodePropertyName) {
            stateCodePropertyName = DEFAULT_STATE_PROPERTY_NAME;
        }
        cityCodePropertyName = filterConfig.getInitParameter("cityCodePropertyName");
        if (null == cityCodePropertyName) {
            cityCodePropertyName = DEFAULT_CITY_PROPERTY_NAME;
        }
        logger.info("Using state property: {} and city property: {}", stateCodePropertyName, cityCodePropertyName);

        ApplicationContext applicationContext =
                WebApplicationContextUtils.getWebApplicationContext(ApplicationContextHolder.getApplicationContext());
        statesCitiesDictionaryService = applicationContext.getBean(StatesCitiesDictionaryService.class);
    }

    @Override
    public void destroy() {
        // NO-OP
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // wrap the response and pass it further to catch the response afterwards
        ByteResponseWrapper responseWrapper = new ByteResponseWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseWrapper);

        // get RAW JSON string
        String rawJsonString = responseWrapper.getData();
        logger.trace("Got RAW JSON: {}", rawJsonString);

        if (null != rawJsonString) {
            // Catch search operation response and replace the codes on display values.
            // Thus, on the search result grid, we should see display values.
            JSONObject jsonResult = new JSONObject(rawJsonString);
            JSONArray docs = jsonResult.optJSONArray("docs");
            if (null != docs) {
                for (int i = 0; i < docs.length(); i++) {
                    JSONObject documentProperties =
                            docs.getJSONObject(i).getJSONObject("properties");

                    // Replacing a State code to State display name
                    String stateCode = documentProperties.optString(stateCodePropertyName);
                    if (StringUtils.isNotEmpty(stateCode)) {
                        State state = statesCitiesDictionaryService.getStateByCode(stateCode);
                        if (state != null) {
                            documentProperties.put(stateCodePropertyName, state.getDisplayValue());
                        }
                    }
                }
            }
            jsonResult.write(response.getWriter());
        }
    }
}
