angular.module('login', ['auth'])

    .controller('LoginController', function ($scope, $rootScope, AUTH_EVENTS, AuthService) {

        if (AuthService.isAuthenticated()) {
            console.log("Already authenticated");
        }

        $scope.credentials = {
            emailAddress: '',
            password: ''
        };

        self.redirectUrl = '';

        $scope.login = function (credentials) {

            console.log("Logging in " + credentials.emailAddress +  " " + credentials.password);
            console.log("from referer " + document.flash.referer);
            console.log(document.flash);

            AuthService.login(credentials).then(function (user) {
                console.log("authservice.login callback");

                $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);

            }, function () {
                $scope.$broadcast(AUTH_EVENTS.loginFailed);
            });
        };

        $scope.addRedirect = function (url) {
            self.redirectUrl = url;
            console.log("added redirect URL " + self.redirectUrl);
        }

    })

    .directive('loginDialog', function (AUTH_EVENTS) {
        return {
            restrict: 'A',
            template: '<div ng-if="visible" ng-include="\'\/assets\/app\/login\/login.tpl.html\'">',
            link: function (scope) {
                var showDialog = function () {
                    scope.visible = true;
            };

            scope.visible = false;
            scope.$on(AUTH_EVENTS.notAuthenticated, showDialog);
            scope.$on(AUTH_EVENTS.sessionTimeout, showDialog)
        }
      };
    })