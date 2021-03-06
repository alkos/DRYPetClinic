(function () {
    'use strict';

    angular.module('petclinicApp').service('apiService', ['$http', '$log', 'apiErrorHandlerService', function ($http, $log, apiErrorHandlerService) {

        /** CreateUser (secured)
         *
         * Api URL: /api/users?
         *"Request": CreateUserDto {
         *    "role": Int,
         *    "username": String,
         *    "password": Option[String]
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": Int,
         *    "username": String
         *}
         */
        this.createUser = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/users',
                data: {
                  role: model.role,
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
         *    "role": Int,
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
         *    "role": Int,
         *    "password": Option[Option[String]]
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": Int,
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

        /** Users (secured)
         *
         * Api URL: /api/users?from:Int&maxRowCount:Int
         *"Request": UsersDto {
         *    "from": Int,
         *    "maxRowCount": Int
         *}
         *"Response": SearchResultDto[UsersResponseDto]
         */
        this.users = function (from, maxRowCount, model, successCallback, errorCallback) {
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

        /** AdminUsers (secured)
         *
         * Api URL: /api/adminUsers?role:Int&from:Int&maxRowCount:Int
         *"Request": AdminUsersDto {
         *    "role": Int,
         *    "from": Int,
         *    "maxRowCount": Int
         *}
         *"Response": SearchResultDto[AdminUsersResponseDto]
         */
        this.adminUsers = function (role, from, maxRowCount, model, successCallback, errorCallback) {
            $http({
                method: 'GET',
                url: '/api/adminUsers',
                params: {
                    role: role,
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
         *    "email": String,
         *    "password": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "email": String,
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.signUp = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/signUp',
                data: {
                  email: model.email,
                  password: model.password
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
         *    "email": String,
         *    "password": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "email": String,
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.signIn = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/signIn',
                data: {
                  email: model.email,
                  password: model.password
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
         *"Request": AccessTokenDto {
         *    "accessToken": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "email": String,
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.authenticate = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/authenticate',
                data: {
                  accessToken: model.accessToken
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

        /** RefreshToken
         *
         * Api URL: /api/refreshToken?
         *"Request": RefreshTokenDto {
         *    "refreshToken": String
         *}
         *
         *"Response": AuthenticationResponseDto {
         *    "email": String,
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.refreshToken = function (model, successCallback, errorCallback) {
            $http({
                method: 'POST',
                url: '/api/refreshToken',
                data: {
                  refreshToken: model.refreshToken
                }
            }).success(function (data) {
                successCallback(data);
            }).error(function (data, header, status, config) {
                apiErrorHandlerService(data, header, status, config, errorCallback);
            });
        };

    }]);
})();
