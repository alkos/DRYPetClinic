(function () {
    angular.module('petclinicApp').directive('signUpForm', function () {
        return {
            restrict: 'E',
            scope: {
                user: '=?',
                onSubmit: '&'
            },
            templateUrl: 'widgets/signUpForm.html',
            controller: 'SignUpFormController',
            controllerAs: 'ctrl'
        }
    }).constant('config', {
        defaultUser: {

        }
    }).controller('SignUpFormController', ['$scope', 'api', 'config', function ($scope, api, config) {
        this.viewModel = {
            user: angular.isDefined($scope.user) ? $scope.user : config.defaultUser,
            submit: submit
        };

        function submit () {
            api.signUp(this.user, successCallback, errorCallback);
        };

        function successCallback(responseData) {
            $scope.onSubmit(responseData);
        };

        function errorCallback(responseData) {
            //TODO display errors on form
        };
    }]);
})();
