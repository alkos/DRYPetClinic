(function () {
    'use strict';

    angular.module('petclinicApp').factory('apiErrorHandlerService', ['$log', function ($log) {

        function apiErrorCallback(data, header, status, config, errorCallback) {
            errorCallback(data);
        }

        return apiErrorCallback;
    }]);
})();
