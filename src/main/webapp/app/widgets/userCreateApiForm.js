(function () {
    angular.module('petclinicApp').directive('userCreateApiForm', function () {
        return {
            restrict: 'E',
            scope: {
                user: '=?',
                onSubmit: '&'
            },
            templateUrl: 'widgets/userCreateApiForm.html',
            controller: 'UserCreateApiFormController',
            controllerAs: 'ctrl'
        }
    }).constant('config', {
        defaultUser: {

        }
    }).controller('UserCreateApiFormController', ['$scope', 'api', 'config', function ($scope, api, config) {
        this.viewModel = {
            user: angular.isDefined($scope.user) ? $scope.user : config.defaultUser,
            submit: submit
        };

        function submit () {
            api.createUser(this.user, successCallback, errorCallback);
        };

        function successCallback(responseData) {
            $scope.onSubmit(responseData);
        };

        function errorCallback(responseData) {
            //TODO display errors on form
        };
    }]);
})();
