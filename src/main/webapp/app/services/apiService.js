(function () {
    'use strict';

    angular.module('petclinicApp').service('apiService', ['$http', '$log', 'apiErrorHandlerService', function ($http, $log, apiErrorHandlerService) {

        /** CreateUser (secured)
         *
         * Api URL: /api/users?
         *"Request": CreateUserDto {
         *    "username": String,
         *    "password": String
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": UserRole,
         *    "username": String
         *}
         */
        this.createUser = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/users',
                data: {
                  username: model.username,
                  password: model.password
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** ReadUser (secured)
         *
         * Api URL: /api/users/:id?
         *"Request": ReadUserDto {
         *    "id": Int
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": UserRole,
         *    "username": String
         *}
         */
        this.readUser = function (model, successCallback, errorCallback) {
            $http({
                method: 'GET',
                url: '/api/users/' + model.id + ''
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** UpdateUser (secured)
         *
         * Api URL: /api/users/:id?
         *"Request": UpdateUserDto {
         *    "id": Int,
         *    "role": UserRole,
         *    "password": Option[String]
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": UserRole,
         *    "username": String
         *}
         */
        this.updateUser = function (model, successCallback, errorCallback) {
            $http({
                method: 'PUT',
                url: '/api/users/' + model.id + '',
                data: {
                  role: model.role,
                  password: model.password
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** DeleteUser (secured)
         *
         * Api URL: /api/users/:id?
         *"Request": ReadUserDto {
         *    "id": Int
         *}
         *"Response": Unit
         */
        this.deleteUser = function (model, successCallback, errorCallback) {
            $http({
                method: 'DELETE',
                url: '/api/users/' + model.id + ''
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** AllUsers (secured)
         *
         * Api URL: /api/users?from:Int&maxRowCount:Int
         *"Request": AllUsersDto {
         *    "from": Int,
         *    "maxRowCount": Int
         *}
         *"Response": SearchResultDto[AllUsersResponseDto]
         */
        this.allUsers = function (from, maxRowCount, model, successCallback, errorCallback) {
            $http({
                method: 'GET',
                url: '/api/users',
                params: {
                    from: from,
                    maxRowCount: maxRowCount
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** SignUp
         *
         * Api URL: /api/signUp?
         *"Request": SignUpDto {
         *    "username": String,
         *    "passwordHash": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "username": String,
         *    "role": UserRole,
         *    "authenticationCode": String
         *}
         */
        this.signUp = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/signUp',
                data: {
                  username: model.username,
                  passwordHash: model.passwordHash
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** SignIn
         *
         * Api URL: /api/signIn?
         *"Request": SignInDto {
         *    "username": String,
         *    "passwordHash": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "username": String,
         *    "role": UserRole,
         *    "authenticationCode": String
         *}
         */
        this.signIn = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/signIn',
                data: {
                  username: model.username,
                  passwordHash: model.passwordHash
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** SignOut (secured)
         *
         * Api URL: /api/signOut?
         *"Response": Unit
         */
        this.signOut = function (successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/signOut'
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** Authenticate
         *
         * Api URL: /api/authenticate?
         *"Request": AuthenticationCodeDto {
         *    "authenticationCode": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "username": String,
         *    "role": UserRole,
         *    "authenticationCode": String
         *}
         */
        this.authenticate = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/authenticate',
                data: {
                  authenticationCode: model.authenticationCode
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

    }]);
})();
