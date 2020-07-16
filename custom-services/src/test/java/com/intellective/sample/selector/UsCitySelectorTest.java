package com.intellective.sample.selector;

import com.vegaecm.vspace.model.MapVarScopeImpl;
import com.vegaecm.vspace.model.VarHandler;
import com.vegaecm.vspace.model.VarScope;
import com.vegaecm.vspace.selectors.Selector;
import com.vegaecm.vspace.selectors.SelectorResult;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:configuration/custom-services.xml"
})
public class UsCitySelectorTest extends BaseSelectorTest {

    @Test
    public void getData() throws Exception {
    	Selector selector = configHolder.getSelectorHolder().getSelector("UsCitySelector");
        Assert.assertNotNull(selector);

        // no state selected
        VarHandler varHandler = new VarHandler();
        SelectorResult result =
        		selector.getData(null, varHandler, 0, Integer.MAX_VALUE, false, null);
        Assert.assertEquals(0, result.getTotalCount());

        // state = CA
        varHandler = new VarHandler();
        varHandler.getHolder().registerScope(VarScope.THIS, new MapVarScopeImpl(Collections.singletonMap("StateCode", "CA")));
        result = selector.getData(null, varHandler, 0, Integer.MAX_VALUE, false, null);
        Assert.assertEquals(297, result.getTotalCount());

        // state = NY
        varHandler = new VarHandler();
        varHandler.getHolder().registerScope(VarScope.THIS, new MapVarScopeImpl(Collections.singletonMap("StateCode", "NY")));
        result = selector.getData(null, varHandler, 0, Integer.MAX_VALUE, false, null);
        Assert.assertEquals(84, result.getTotalCount());

        // non-existent state
        varHandler = new VarHandler();
        varHandler.getHolder().registerScope(VarScope.THIS, new MapVarScopeImpl(Collections.singletonMap("StateCode", "ZZ")));
        result = selector.getData(null, varHandler, 0, Integer.MAX_VALUE, false, null);
        Assert.assertEquals(0, result.getTotalCount());
    }
}
