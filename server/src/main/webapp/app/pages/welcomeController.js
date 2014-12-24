(function () {
    'use strict';

    angular.module('petclinicApp')
        .controller('WelcomeController', ['$scope', function ($scope) {
            $scope.awesomeThings = [
                'HTML5 Boilerplate',
                'AngularJS',
                'Karma'
            ];

            $scope.signInSuccess = function (data) {
                alert('Sign in is a success... route and blabla...')
            };

            $scope.signInFailed = function (data) {
                alert('Sign in failed... display error message somewhere')
            };

        }]);
})();
