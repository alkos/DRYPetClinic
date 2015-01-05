(function () {
    'use strict';

    angular.module('petclinicApp').service('apiMockService', ['$http', '$log', '$timeout', 'apiErrorHandlerService', function ($http, $log, $timeout, apiErrorHandlerService) {

        /** CreateUser (secured)
         *
         * Api URL: /api/users?
         *"Request": CreateUserDto {
         *    "role": Int,
         *    "username": String,
         *    "password": String
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": Int,
         *    "username": String
         *}
         */
        this.createUser = function (model, successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
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
            successCallback({
                //TODO fill up mocked data values
            });
        };

        /** UpdateUser (secured)
         *
         * Api URL: /api/users/:id?
         *"Request": UpdateUserDto {
         *    "id": Int,
         *    "role": Int,
         *    "password": Option[String]
         *}
         *
         *"Response": ReadUserResponseDto {
         *    "id": Int,
         *    "role": Int,
         *    "username": String
         *}
         */
        this.updateUser = function (model, successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
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
            successCallback({
                //TODO fill up mocked data values
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
            successCallback({
                //TODO fill up mocked data values
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
            successCallback({
                //TODO fill up mocked data values
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
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.signUp = function (model, successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
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
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.signIn = function (model, successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
            });
        };

        /** SignOut (secured)
         *
         * Api URL: /api/signOut?
         *"Response": Unit
         */
        this.signOut = function (successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
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
         *    "username": String,
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.authenticate = function (model, successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
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
         *    "username": String,
         *    "roleId": Int,
         *    "accessToken": String,
         *    "refreshToken": String
         *}
         */
        this.refreshToken = function (model, successCallback, errorCallback) {
            successCallback({
                //TODO fill up mocked data values
            });
        };

    }]);
})();
