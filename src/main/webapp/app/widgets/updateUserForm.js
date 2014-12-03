(function () {
    angular.module('petclinicApp').directive('updateUserForm', function () {
        return {
            restrict: 'E',
            scope: {
                user: '=?',
                onSubmit: '&'
            },
            templateUrl: 'widgets/updateUserForm.html',
            controller: 'UpdateUserFormController',
            controllerAs: 'ctrl'
        }
    }).constant('config', {
        defaultUser: {

        }
    }).controller('UpdateUserFormController', ['$scope', 'api', 'config', function ($scope, api, config) {
        this.viewModel = {
            user: angular.isDefined($scope.user) ? $scope.user : config.defaultUser,
            submit: submit
        };

        function submit () {
            api.updateUser(this.user, successCallback, errorCallback);
        };

        function successCallback(responseData) {
            $scope.onSubmit(responseData);
        };

        function errorCallback(responseData) {
            //TODO display errors on form
        };
    }]);
})();
