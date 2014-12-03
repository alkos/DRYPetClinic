(function () {
    'use strict';

    angular.module('petclinicApp').directive('userRoleDropDown', ['userRoleDropDownConfig', function (userRoleDropDownConfig) {
        return {
            restrict: 'E',
            scope: {
                name: '@',
                nullValueLocalizationKey: '@',
                required: '@',
                disabled: '@',
                selectedUserRole: '='
            },
            templateUrl: "widgets/userRoleDropDown.html",
            controller: 'UserRoleDropDownController',
            controllerAs: 'ctrl',
            compile: function (element, attrs) {
                if (attrs.required === '') {
                    attrs.required = 'true';
                }
                if (!attrs.nullValueLocalizationKey || attrs.nullValueLocalizationKey === '') {
                    attrs.nullValueLocalizationKey = userRoleDropDownConfig.defaultEmptyItemLocalizationKey;
                }
            }
        }
    }]).constant('userRoleDropDownConfig', {
        defaultEmptyItemLocalizationKey: 'USERROLEDROPDOWN_SELECT_ONE'
    }).controller('UserRoleDropDownController', ['$scope', 'userRoleDropDownConfig', function ($scope, userRoleDropDownConfig) {
        this.viewModel = {
            selectedUserRole: $scope.selectedUserRole,
            hasNullValue: calculateHasNullValue($scope.selectedUserRole),
            userRoles: ['ADMIN', 'USER']
        };

        $scope.$watch('ctrl.viewModel.selectedUserRole', function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.selectedUserRole = !angular.isDefined(newValue) || newValue === null ? null : newValue;
                $scope.ctrl.viewModel.hasNullValue = calculateHasNullValue(newValue);
            }
        });

        $scope.$watch('selectedUserRole', function (newValue, oldValue) {
            if (newValue != oldValue) {
                $scope.ctrl.viewModel.selectedUserRole = newValue;
                $scope.ctrl.viewModel.hasNullValue = calculateHasNullValue(newValue);
            }
        });

        function calculateHasNullValue (newValue) {
            return ($scope.required && $scope.required === 'true' && newValue !== null) ? false : true;
        };

    }]);
})();
