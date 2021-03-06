(function () {
    'use strict';

    angular.module('petclinicApp').directive('usersApiDropDown', function () {
        return {
            restrict: 'E',
            scope: {
                name: '@',
                required: '@',
                selectedUserId: '=',
                config: '='
            },
            templateUrl: "widgets/usersApiDropDown.html",
            controller: 'UsersApiDropDownController',
            controllerAs: 'ctrl',
            compile: function (element, attrs) {
                if (attrs.required === '') {
                    attrs.required = 'true';
                }
            }
        }
    }).constant('usersApiDropDownConfig', {
        defaultEmptyItemLocalizationKey: 'SELECT_ONE'
    }).controller('UsersApiDropDownController', ['$scope', 'api', 'usersApiDropDownConfig', function ($scope, api, usersApiDropDownConfig) {
        var ctrl = this;

        this.viewModel = {
            users: [],
            hasNullValue: calculateHasNullValue($scope.selectedUserId),
            isLoading: true
        };

        $scope.$watch('selectedUserId', function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.ctrl.viewModel.hasNullValue = calculateHasNullValue(newValue);
            }
        });

        function apiSuccessCallback(response) {
            ctrl.viewModel.users = response;
            ctrl.viewModel.isLoading = false;
        }

        function apiErrorCallback(response) {
            ctrl.viewModel.isLoading = false;
            //TODO Handle error response
        }

        function calculateHasNullValue(newValue) {
            return ($scope.required && $scope.required === 'true' && newValue !== null && !angular.isUndefined(newValue)) ? false : true;
        }

        if (!$scope.config.nullValueLocalizationKey || $scope.config.nullValueLocalizationKey === '') {
            $scope.config.nullValueLocalizationKey = usersApiDropDownConfig.defaultEmptyItemLocalizationKey;
        }

        api.users($scope.config.from, $scope.config.maxRowCount, apiSuccessCallback, apiErrorCallback);
    }]);
})();
