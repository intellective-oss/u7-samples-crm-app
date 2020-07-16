VSpace.action.ActionHandler['add_CustomerCorrespondence'] = function(evt) {
   var linkedTemplatesTab = Ext.getCmp(evt.gridId).up('custom-linked-search-templates-tab');
   var masterTemplate = Ext.getCmp(linkedTemplatesTab.getId() + '-master-search-templates');
   if (masterTemplate && masterTemplate.config && !Ext.isEmpty(masterTemplate.config.detailsTempatePanelId)) {
		var detailsSearchTemplate = Ext.getCmp(masterTemplate.config.detailsTempatePanelId);
		if (detailsSearchTemplate && detailsSearchTemplate.cfg && detailsSearchTemplate.cfg.nodecontext) {
            if (Ext.isEmpty(detailsSearchTemplate.cfg.nodecontext.masterProperty)) {
                Ext.MessageBox.alert('', 'Please select Customer Info record');
                return;
            }

            var actionConfig = VSpace.UIDEF.getActionConfig(evt.actionId);
            if (!actionConfig) { //There is no such action, it is disabled probably .
                Ext.MessageBox.alert('', VSpace.action.UPDATE_PROPERTIES_DIALOG_NOT_CONFIGURED);
                return;
            }
    
            var providerId = evt.providerId ? evt.providerId : VSpace.UIDEF.getProviderId();
            var providerCustomParameters = actionConfig.customparameters[providerId];
            if (!providerCustomParameters) {
                Ext.MessageBox.alert('', VSpace.utils.i18n.localize('action.action-handler.message.no-custom-repository-section', 
                                                                    'There is no CustomParameters/RepositoryDataProvider/@ID={0} section for {1} action.', [providerId, evt.actionId]));
                return;
            }

            var dataProvider = null;
            var folderPath = null;
            var p = providerCustomParameters.folderpicker.treemodel.properties;
            for (var i = 0; i < p.length; i++) {
                if (p[i].id == 'FolderPath') {
                    folderPath = p[i].value;
                } else if (p[i].id == 'DataProviderId') {
                    dataProvider = p[i].value;
                }
            }
            Ext.apply(evt, {
                nodeContext: {
                   FolderPath: folderPath,
                   CustomerName: detailsSearchTemplate.cfg.nodecontext.masterProperty,
                   documentId: {
                       dataProviderId: dataProvider
                   }
                }
            });

            Ext.Function.pass(VSpace.action.ActionHandler.add_document, [evt], this)();
        }
    }
};
