/**
 * Defines the action handler for master record (customer) selection event
 */
VSpace.action.ActionHandler['custom_view_client_details'] = function(evt) {
    if (!Ext.isEmpty(evt.gridId)) {
        var masterGrid = Ext.getCmp(evt.gridId);
        var masterTemplate = masterGrid.up('search-templates-tab');
        if (masterTemplate && masterTemplate.config && !Ext.isEmpty(masterTemplate.config.detailsTempatePanelId)) {
            var detailsSearchTemplate = Ext.getCmp(masterTemplate.config.detailsTempatePanelId);
            if (detailsSearchTemplate) {
                // Lazily add execution handler
                var detailsSearchPanel = detailsSearchTemplate.down('vspace-templates-search-panel');
                if (detailsSearchTemplate.cfg && detailsSearchTemplate.cfg.nodecontext) {
                    var masterProperty = detailsSearchTemplate.config.masterProperty;
                    var masterItem = evt.orig[3];
                    var detailsSearchContext = {
                        masterProperty: masterItem.get(masterProperty)
                    };
                    Ext.apply(detailsSearchTemplate.cfg.nodecontext, detailsSearchContext);
                    detailsSearchPanel.executeQuery();
                }
            }
        }
    }
};