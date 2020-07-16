package com.intellective.sample.selector;

import com.vegaecm.vspace.model.VarHandler;
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

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:configuration/custom-services.xml"
})
public class UsStateSelectorTest extends BaseSelectorTest {

    @Test
    public void getData() {
    	Selector selector = configHolder.getSelectorHolder().getSelector("UsStateSelector");
        Assert.assertNotNull(selector);
        SelectorResult result =
                selector.getData(null, new VarHandler(), 0, Integer.MAX_VALUE, false, new HashMap<>());
        assertEquals(51, result.getTotalCount());
        assertTrue(result.getValues().stream().anyMatch(selectorItem -> selectorItem.getValue().equals("CA")));
    }
}
