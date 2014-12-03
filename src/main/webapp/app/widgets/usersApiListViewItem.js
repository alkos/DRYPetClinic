(function () {
    angular.module('testApp').directive('usersApiListViewItem', function () {
        return {
            restrict: 'E',
            scope: {
                user: '='
            },
            templateUrl: 'widgets/usersApiListViewItem.html',
            controller: 'UsersApiListViewItemController',
            controllerAs: 'ctrl'
        }
    }).controller('UsersApiListViewItemController', ['$scope', function ($scope) {
        this.viewModel.user = $scope.user;
    }]);
})();
