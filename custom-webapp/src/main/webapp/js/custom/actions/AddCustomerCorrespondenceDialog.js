Ext.define('custom.action.AddCustomerCorrespondenceDialog', {
    extend : 'VSpace.action.dialog.AddDialog',
    alias: 'widget.custom-action-AddCustomerCorrespondence-dialog',

    titleAutoFill: function () {
        this.callParent(arguments);

        var me = this;
        var customerName = this.nodeContext.CustomerName;
        var elements = Ext.select('#' + me.panelId + ' .criterion', true).elements;
        Ext.each(elements, function (element) {
            var input = element.component;
            if ('CustomerName' === element.component.name) {
                input.setValue(customerName);
                input.fireEvent('valueChanged', input, customerName, '');
            }
        });
    }
});
