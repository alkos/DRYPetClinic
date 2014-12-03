(function () {

    angular.module('petclinicApp').directive('userView', function () {
        return {
            restrict: 'E',
            scope: {
                user: '='
            },
            templateUrl: 'widgets/userView.html',
            controller: 'UserViewController',
            controllerAs: 'ctrl'
        }
    }).controller('UserViewController', ['$scope', 'api', function ($scope, api) {
        this.viewModel = {
            user: $scope.user
        };
    }]);
})();
