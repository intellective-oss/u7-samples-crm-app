/**
 * Override Unity component to display master and detail search templates, one is just below another.
 */
Ext.override(VSpace.templates.GridsPanel, {
    getTemplateGridConfig: function () {
        var templateGridConfig = this.callParent(arguments);
        if (this.ownerCt &&
            this.ownerCt.config &&
            this.ownerCt.config.cfg &&
            Ext.isObject(this.ownerCt.config.cfg.linkedTemplate)) {
            return {
                layout: 'border',
                region: 'center',
                items: [
                    {
                        layout: 'fit',
                        region: 'center',
                        flex: 3,
                        items: templateGridConfig
                    }, {
                        layout: 'fit',
                        region: 'south',
                        flex: 2,
                        items: this.ownerCt.config.cfg.linkedTemplate
                    }
                ]
            };
        }
        return templateGridConfig;
    }
});

/**
 Linked search templates - horizontal master-details component
 Top search template defines the customer. Bottom one searches documents related to the selected customer record.
 */
Ext.define('custom.tab.LinkedSearchTemplatesTab', {
    extend: 'Ext.Panel',
    alias: 'widget.custom-linked-search-templates-tab',

    constructor: function (config) {
        var pId = config.cfg.id + '-tab' + '-' + Ext.id();
        var detailsTempatePanelId = pId + '-details-search-templates';
        Ext.applyIf(config, {
            tabId: pId,
            solution: VSpace.utils.extractSolutionId(config.cfg.id),
            cfg: config.cfg,
            title: config.cfg.title,
            id: pId,
            layout: 'border',
            items: {
                layout: 'fit',
                region: 'center',
                flex: 3,
                items: {
                    id: pId + '-master-search-templates',
                    xtype: 'search-templates-tab',
                    detailsTempatePanelId: detailsTempatePanelId, // keep the reference to the 'details' template panel
                    cfg: {
                        id: config.cfg.id + '-master',
                        dataproviderid: config.cfg.dataproviderid,
                        grid: {
                            id: config.cfg.grid.id
                        },

                        // The below 'linked' search template will be displayed inside 'master' search template
                        linkedTemplate: {
                            id: detailsTempatePanelId,
                            xtype: 'search-templates-tab',
                            masterProperty: config.cfg.masterproperty, // master record property id to fetch details records
                            getQueryPanelConfig: function () {
                                return {
                                    hidden: true,    // hide query panel for the details search template
                                    templateDefaults: {
                                        active: false // disable auto-execution of the details template
                                    }
                                };
                            },
                            cfg: {
                                id: config.cfg.id + '-details',
                                dataproviderid: config.cfg.dataproviderid,
                                grid: {
                                    id: config.cfg.grid.id
                                },
                                nodecontext: {}
                            }
                        }
                    }
                }
            },
            urlSupported: true
        });

        this.callParent([config]);
    }
});
