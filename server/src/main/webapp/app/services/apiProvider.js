(function () {
    'use strict';

    angular.module('petclinicApp').provider('api', function () { // you will use just 'api' as a name when injecting the provider into other modules (controllers etc.)
        this.isMocked = false;

        this.$get = ['apiService', 'apiMockService', function (apiService, apiMockService) {
            if (this.isMocked) {
                return apiMockService;
            } else {
                return apiService;
            }
        }];

    }).config(function (clientConfigurationValuesService, apiProvider) { //be careful to inject provider into config function by stamping provider name with 'Provider' (api + Provider)
        apiProvider.isMocked = clientConfigurationValuesService.useApiMocks;
    });
})();
