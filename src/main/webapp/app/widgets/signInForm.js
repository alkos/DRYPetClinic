(function () {
    angular.module('petclinicApp').directive('signInForm', function () {
        return {
            restrict: 'E',
            scope: {
                user: '=?',
                onSubmit: '&'
            },
            templateUrl: 'widgets/signInForm.html',
            controller: 'SignInFormController',
            controllerAs: 'ctrl'
        }
    }).constant('config', {
        defaultUser: {

        }
    }).controller('SignInFormController', ['$scope', 'api', 'config', function ($scope, api, config) {
        this.viewModel = {
            user: angular.isDefined($scope.user) ? $scope.user : config.defaultUser,
            submit: submit
        };

        function submit () {
            api.signIn(this.user, successCallback, errorCallback);
        };

        function successCallback(responseData) {
            $scope.onSubmit(responseData);
        };

        function errorCallback(responseData) {
            //TODO display errors on form
        };
    }]);
})();
