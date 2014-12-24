(function () {
    angular.module('petclinicApp').directive('usersApiListView', function () {
        return {
            restrict: 'E',
            scope: {
                selectedUser: '=?',
                config: '='
            },
            templateUrl: 'widgets/usersApiListView.html',
            controller: 'UsersApiListViewController',
            controllerAs: 'ctrl'
        }
    }).controller('UsersApiListViewController', ['$scope', 'api', function ($scope, api) {
        var ctrl = this;

        this.viewModel = {
            users: [],
            selectedUser: null,
            onUserClicked: onUserClicked
        };

        function onUserClicked(item) {
            selectUser(item);
        };

        function selectUser(model) {
            if (ctrl.viewModel.selectedUser !== null) {
                ctrl.viewModel.selectedUser.isSelected = false;
            }

            if (ctrl.viewModel.selectedUser !== model) {
                model.isSelected = true;
                ctrl.viewModel.selectedUser = model;
                $scope.selectedUser = ctrl.viewModel.selectedUser.model;
            } else {
                ctrl.viewModel.selectedUser = null;
                $scope.selectedUser = null;
            }
        };

        function onApiSuccess(data) {
            angular.forEach(data, addUser);
        };

        function addUser(dataItem) {
            var newModel = {
                model: dataItem,
                isSelected: false
            };

            ctrl.viewModel.users.push(newModel);

            if ($scope.config.initialSelectedId && $scope.config.initialSelectedId === dataItem.id) {
                selectUser(newModel);
            }
        };

        api.users($scope.config.from, $scope.config.maxRowCount, onApiSuccess);
    }]);
})();
