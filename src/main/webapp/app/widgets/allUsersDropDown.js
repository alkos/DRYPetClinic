(function () {
    'use strict';

    angular.module('petclinicApp').directive('allUsersDropDown', function () {
        return {
            restrict: 'E',
            scope: {
                name: '@',
                required: '@',
                selectedUserId: '=',
                config: '='
            },
            templateUrl: "widgets/allUsersDropDown.html",
            controller: 'AllUsersDropDownController',
            controllerAs: 'ctrl',
            compile: function (element, attrs) {
                if (attrs.required === '') {
                    attrs.required = 'true';
                }
            }
        }
    }).constant('allUsersDropDownConfig', {
        defaultEmptyItemLocalizationKey: 'SELECT_ONE'
    }).controller('AllUsersDropDownController', ['$scope', 'api', 'allUsersDropDownConfig', function ($scope, api, allUsersDropDownConfig) {
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
            $scope.config.nullValueLocalizationKey = allUsersDropDownConfig.defaultEmptyItemLocalizationKey;
        }

        api.allUsers($scope.config.from, $scope.config.maxRowCount, apiSuccessCallback, apiErrorCallback);
    }]);
})();
