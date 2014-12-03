(function () {
    angular.module('testApp').directive('allUsersListViewListViewItem', function () {
        return {
            restrict: 'E',
            scope: {
                allUsersListView: '='
            },
            templateUrl: 'widgets/allUsersListViewListViewItem.html',
            controller: 'AllUsersListViewListViewItemController',
            controllerAs: 'ctrl'
        }
    }).controller('AllUsersListViewListViewItemController', ['$scope', function ($scope) {
        this.viewModel.allUsersListView = $scope.allUsersListView;
    }]);
})();
