Ext.define('Override.form.field.VTypes', {
    override: 'Ext.form.field.VTypes',

    UsPhone: function (value, field) {
        if (this.UsPhoneRe.test(value)) {
            Ext.defer(this.validateUsPhoneAreaCode, 10, this, [value, field]); // async validation
            return true;
        }
        return false;
    },

    validateUsPhoneAreaCode: function (phoneNumber, field) {
        var me = this;
        var stateCode =
            field.up('search-template-criteria-form').getFieldNamesAndValues().State;
        var cityName =
            field.up('search-template-criteria-form').getFieldNamesAndValues().City;
        var areaCode =
            0 === phoneNumber.indexOf('+') ? phoneNumber.substr(3, 3)
                : phoneNumber.substr(0, 3);

        if (stateCode && cityName) {
            Ext.Ajax.request({
                url: 'custom-api/1.0/states/' + encodeURIComponent(stateCode) + '/' + encodeURIComponent(cityName) + '/validate',
                method: 'POST',
                params: {
                    areaCode: areaCode
                },

                success: function (response) {
                    var result = Ext.decode(response.responseText, true);
                    if (null != result) {
                        if (result.success) {
                            if (result.payload.valid) {
                                field.clearInvalid();
                            } else {
                                field.markInvalid(me.UsPhoneAreaCodeInvalidText);
                            }
                        }
                    } else {
                        Ext.Msg.alert('Warning', 'Invalid server response when validating US phone area code. Response: ' + response.responseText);
                    }
                },

                failure: function (response) {
                    Ext.Msg.alert('Failure', 'Server-side failure with status code ' + response.status);
                }
            });
        }
    },

    UsPhoneRe: /^(\+1\s?)?((\([0-9]{3}\))|[0-9]{3})[\s\-]?[\0-9]{3}[\s\-]?[0-9]{4}$/,
    UsPhoneText: 'Expected format is +1 XXX-YYY-YYYY where XXX is the valid US area code',
    UsPhoneAreaCodeInvalidText: 'The area code should match the selected state',
    UsPhoneMask: /[0-9\+\-\s]/i
});
