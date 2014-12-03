(function () {
    angular.module('petclinicApp').directive('usersListView', function () {
        return {
            restrict: 'E',
            scope: {
                users: '=',
                selectedUser: '=?'
            },
            templateUrl: 'widgets/usersListView.html',
            controller: 'UsersListViewController',
            controllerAs: 'ctrl'
        }
    }).controller('UsersListViewController', ['$scope', function ($scope) {
        this.model = {
            users: $scope.users,
            selectedUser: null,
            onUserClicked: onUserClicked
        };

        function onUserClicked(item) {
            if (this.selectedUser !== null) {
                this.selectedUser.selected = false;
            }

            if (this.selectedUser !== item) {
                item.selected = true;
                this.selectedUser = item;
                $scope.selectedUser = this.selectedUser.model;
            } else {
                this.selectedUser = null;
                $scope.selectedUser = null;
            }
        };

    }]);
})();
