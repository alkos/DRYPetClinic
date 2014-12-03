(function () {
    'use strict';

    var petclinicApp = angular.module('petclinicApp', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngMessages',
        'pascalprecht.translate',
        'ui.router',
        'ui.bootstrap'
    ]).constant('clientConfigurationValuesService', {
        useApiMocks: true
    });

    petclinicApp.config(function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise('/welcome');
        $stateProvider
            .state('testApp', {
                url: '/welcome',
                templateUrl: 'pages/welcome.html',
                controller: 'WelcomeController'
            })
            .state('sandbox', {
                url: '/sandbox',
                templateUrl: 'pages/controlsSandBox.html',
                controller: 'ControlsSandBoxController'
            });
    })
        .run(function ($rootScope, $location) {
        });
})();
