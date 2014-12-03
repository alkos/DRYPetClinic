(function () {
    angular.module('petclinicApp').directive('signOutForm', function () {
        return {
            restrict: 'E',
            scope: {
                onSubmit: '&'
            },
            templateUrl: 'widgets/signOutForm.html',
            controller: 'SignOutFormController',
            controllerAs: 'ctrl'
        }
    }).constant('config', {
        defaultUser: {

        }
    }).controller('SignOutFormController', ['$scope', 'api', 'config', function ($scope, api, config) {
        this.viewModel = {
            user: angular.isDefined($scope.user) ? $scope.user : config.defaultUser,
            submit: submit
        };

        function submit () {
            api.signOut(successCallback, errorCallback);
        };

        function successCallback(responseData) {
            $scope.onSubmit(responseData);
        };

        function errorCallback(responseData) {
            //TODO display errors on form
        };
    }]);
})();
