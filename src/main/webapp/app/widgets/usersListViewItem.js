(function () {
    angular.module('testApp').directive('usersListViewItem', function () {
        return {
            restrict: 'E',
            scope: {
                user: '='
            },
            templateUrl: 'widgets/usersListViewItem.html',
            controller: 'UsersListViewItemController',
            controllerAs: 'ctrl'
        }
    }).controller('UsersListViewItemController', ['$scope', function ($scope) {
        this.viewModel.user = $scope.user;
    }]);
})();
