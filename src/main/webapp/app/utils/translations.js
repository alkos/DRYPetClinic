(function () {
    'use strict';

    angular.module('petclinicApp').config(function ($translateProvider) {
        $translateProvider.translations('en', {
            WELCOME: 'Welcome to DRY Generator',
            SIGNUPFORM_PASSWORD_HASH_MINLENGTH: 'PasswordHash max size is {{minlength}}.',
            SIGNUPFORM_USERNAME_REQUIRED: 'Username is required.',
            SIGNUPFORM_USERNAME_MAXLENGTH: 'Username max size is {{maxlength}}.',
            SIGNUPFORM_PASSWORD_HASH_REQUIRED: 'PasswordHash is required.',
            SIGNUPFORM_USERNAME: 'username',
            SIGNUPFORM_PASSWORD_HASH_MAXLENGTH: 'PasswordHash max size is {{maxlength}}.',
            SIGNUPFORM_SUBMIT: 'Submit',
            SIGNUPFORM_PASSWORD_HASH: 'passwordHash',
            SIGNUPFORM_USERNAME_MINLENGTH: 'Username max size is {{minlength}}.',
            SIGNINFORM_PASSWORD_HASH_MINLENGTH: 'PasswordHash max size is {{minlength}}.',
            SIGNINFORM_SUBMIT: 'Submit',
            SIGNINFORM_PASSWORD_HASH: 'passwordHash',
            SIGNINFORM_PASSWORD_HASH_REQUIRED: 'PasswordHash is required.',
            SIGNINFORM_USERNAME_MAXLENGTH: 'Username max size is {{maxlength}}.',
            SIGNINFORM_USERNAME: 'username',
            SIGNINFORM_PASSWORD_HASH_MAXLENGTH: 'PasswordHash max size is {{maxlength}}.',
            SIGNINFORM_USERNAME_MINLENGTH: 'Username max size is {{minlength}}.',
            SIGNINFORM_USERNAME_REQUIRED: 'Username is required.',
            SIGNOUTFORM_SUBMIT: 'Submit',
            USERROLEDROPDOWN_ADMIN: 'Admin',
            USERROLEDROPDOWN_USER: 'User',
            USERROLEDROPDOWN_SELECT_ONE: 'Select one',
            USERCREATEAPIFORM_SUBMIT: 'Submit',
            USERCREATEAPIFORM_ROLE_REQUIRED: 'Role is required.',
            USERCREATEAPIFORM_PASSWORD: 'password',
            USERCREATEAPIFORM_USERNAME_MINLENGTH: 'Username max size is {{minlength}}.',
            USERCREATEAPIFORM_USERNAME_MAXLENGTH: 'Username max size is {{maxlength}}.',
            USERCREATEAPIFORM_PASSWORD_MAXLENGTH: 'Password max size is {{maxlength}}.',
            USERCREATEAPIFORM_USERNAME_REQUIRED: 'Username is required.',
            USERCREATEAPIFORM_USERNAME: 'username',
            USERCREATEAPIFORM_ROLE: 'role',
            USERCREATEAPIFORM_PASSWORD_REQUIRED: 'Password is required.',
            USERCREATEAPIFORM_PASSWORD_MINLENGTH: 'Password max size is {{minlength}}.',
            USERUPDATEFORM_PASSWORD: 'password',
            USERUPDATEFORM_PASSWORD_MINLENGTH: 'Password max size is {{minlength}}.',
            USERUPDATEFORM_ROLE: 'role',
            USERUPDATEFORM_PASSWORD_MAXLENGTH: 'Password max size is {{maxlength}}.',
            USERUPDATEFORM_ROLE_REQUIRED: 'Role is required.',
            USERUPDATEFORM_SUBMIT: 'Submit',
            USERUPDATEFORM_ID_REQUIRED: 'Id is required.',
            USERUPDATEFORM_ID: 'id',
            USERVIEW_ID: 'id',
            USERVIEW_ROLE: 'role',
            USERVIEW_USERNAME: 'username',
            USERSAPILISTVIEWITEM_ID: 'id',
            USERSAPILISTVIEWITEM_USERNAME: 'username',
            USERSAPILISTVIEWITEM_ROLE_ID: 'roleId',
            USERSLISTVIEWITEM_ID: 'id',
            USERSLISTVIEWITEM_USERNAME: 'username',
            USERSLISTVIEWITEM_ROLE_ID: 'roleId'
        });
        $translateProvider.preferredLanguage('en');
    });
})();
