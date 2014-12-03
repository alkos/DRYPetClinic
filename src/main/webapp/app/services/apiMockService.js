(function () {
    'use strict';

    angular.module('petclinicApp').service('apiMockService', ['$http', '$log', '$timeout', 'apiErrorHandlerService', function ($http, $log, $timeout, apiErrorHandlerService) {

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
         *    "role": UserRole,
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
         *    "role": UserRole,
         *    "authenticationCode": String
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
         *    "role": UserRole,
         *    "authenticationCode": String
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
            successCallback({
                //TODO fill up mocked data values
            });
        };

    }]);
})();
