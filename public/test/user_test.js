'use strict';

describe('user', function() {

    var UserService,
        AuthService,
        createController,
        createProfileController,
        $httpBackend,
        $rootScope,
        $scope;

    var testUser = {
        id : 0,
        emailAddress : 'email@address.com'
    }

    var testUser1 = {
        id : 1,
        emailAddress : 'email1@address.com'
    }

    var testUsers = [testUser, testUser1];


    beforeEach(function () {

        module('user');

        inject(function(_UserService_, _AuthService_, $controller, _$httpBackend_, _$rootScope_) {
            UserService = _UserService_;
            AuthService = _AuthService_;
            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;

            $scope = $rootScope.$new();

            createController = function () {
                return $controller('UserController', {
                    '$scope' : $scope
                });
            };

            createProfileController = function () {
                return $controller('ProfileController', {
                    '$scope' : $scope
                });
            };
        });

        $httpBackend.whenGET('/users').respond(testUsers);
        $httpBackend.whenPUT(/\/users\/[0-9]*/).respond(function(method, url, data) {
            var user = angular.fromJson(data);
            return [200, user, {}];
        });
    });

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    describe('service', function () {

        it('can get an instance', function () {
            expect(UserService).toBeDefined();
        });

        it('can get users with query', function () {
            $httpBackend.expectGET('/users');

            UserService.query().$promise.then(function (users) {
                expect(users).not.toBeNull();
                expect(users.length).toEqual(2);
            });

            $httpBackend.flush();
        });

    });

    describe('user controller', function (){

        it('can get an instance', function () {
          //spec body
          var UserController = createController();
          $httpBackend.flush();

          expect(UserController).toBeDefined();
        });
    });

    describe('profile controller', function () {

        it('can get an instance', function () {

            var ProfileController = createProfileController();

            expect(ProfileController).toBeDefined();
            expect($scope.user).toBeNull();

        });

        it('can save the user ', function () {

            $httpBackend.expectPUT('/users/33');

            //Set initial user on AuthService
            AuthService.setCurrentUser(testUser);
            expect(AuthService.currentUser()).not.toBeNull();//double check

            var ProfileController = createProfileController();

            expect($scope.user).toEqual(testUser); //test initial scope

            var modifiedUser = { id: 33, emailAddress: testUser.emailAddress};

            //Update user on scope with changes, which would happen in the template
            $scope.user = modifiedUser;
            $scope.save(); //save

            $httpBackend.flush();//save requires a flush

            expect($scope.user).toEqual(modifiedUser);
        });
    });
});