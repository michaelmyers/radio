'use strict';

describe('auth', function() {

    var AuthService,
        Session,
        localStorageService,
        $httpBackend;

    //test data
    var testUser = {
       id : 0,
       emailAddress: "test@test.com"
   }

    beforeEach(function () {

        // load the module
        module('auth');

        // inject your service for testing.
        // The _underscores_ are a convenience thing
        // so you can have your variable name be the
        // same as your injected service.
        inject(function(_AuthService_, _localStorageService_, _$httpBackend_) {
            AuthService = _AuthService_;
            localStorageService = _localStorageService_;
            $httpBackend = _$httpBackend_;
        });

        //Just in case there is state
        AuthService.reset();//TODO:take this out.

        $httpBackend.whenPOST('/login').respond({
            authToken:"1111-2222-3333-4444-5555",
            user : testUser
        });

        $httpBackend.whenPOST('/users').respond({
            user : testUser
        });

        $httpBackend.whenPOST('/logout').respond({});
    });

    afterEach(function () {
        //not used
    });

    it('can get an instance of AuthService', function () {
        expect(AuthService).toBeDefined();
    });

    it('is instantiated correctly', function () {
        expect(AuthService.isAuthenticated()).toBeFalsy();
        expect(AuthService.currentUser()).toBeNull();
    });

    it('can create a user', function () {

        AuthService.createUser({userName:"test@test.com", password:"password"}).then(function (user) {
            expect(user).not.toBeNull();
            expect(AuthService.isAuthenticated()).toBeFalsy();
            expect(AuthService.currentUser()).toBeNull();
            expect(localStorageService.get("user")).toBeNull();
            expect(localStorageService.get("authToken")).toBeNull();
        });

        $httpBackend.flush();

    });

    it('can login', function () {

        $httpBackend.expectPOST('/login');

        AuthService.login({userName:"test@test.com", password:"password"}).then(function (user) {
            expect(AuthService.isAuthenticated()).toBeTruthy();
            expect(AuthService.currentUser()).not.toBeNull();
            expect(localStorageService.get("user")).not.toBeNull();
            expect(localStorageService.get("authToken")).not.toBeNull();
        });

       $httpBackend.flush();

    });

    it('can logout', function () {

        $httpBackend.expectPOST('/logout');

        AuthService.logOut().then(function () {
            expect(AuthService.isAuthenticated()).toBeFalsy();
            expect(AuthService.currentUser()).toBeNull();
            expect(localStorageService.get("user")).toBeNull();
            expect(localStorageService.get("authToken")).toBeNull();

        });

        $httpBackend.flush();
    });

});