(function () {
    angular.module('petclinicApp').directive('createUserForm', function () {
        return {
            restrict: 'E',
            scope: {
                user: '=?',
                onSubmit: '&'
            },
            templateUrl: 'widgets/createUserForm.html',
            controller: 'CreateUserFormController',
            controllerAs: 'ctrl'
        }
    }).constant('config', {
        defaultUser: {

        }
    }).controller('CreateUserFormController', ['$scope', 'api', 'config', function ($scope, api, config) {
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
