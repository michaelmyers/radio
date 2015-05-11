angular.module('main', [
    'ngRoute',
    'ngAnimate',
    'ngResource',
    'ngSanitize',

    //Local Storage Module https://github.com/grevory/angular-local-storage
    'LocalStorageModule',

    // Top level modules only
    'app',
    'auth',
    'login',
    'signup',
    'notification',
    'controls',
    'user'
])

.config(['$compileProvider', '$routeProvider', '$locationProvider', function($compileProvider, $routeProvider, $locationProvider) {

        $locationProvider.html5Mode(true);
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel):/);

        $routeProvider

            .when('/', {
                templateUrl: 'assets/app/landing/landing.tpl.html',
                requireLogin: false
            })
            .when('/now-playing', {
                templateUrl: '/assets/app/player/now-playing.tpl.html',
                controller: 'NowPlayingController',
                requireLogin: true
            })
            .when('/settings', {
                templateUrl: '/assets/app/users/user/settings.tpl.html',
                requireLogin: true
            })
            .when('/profile', {
                templateUrl: '/assets/app/users/user/profile.tpl.html',
                controller: 'ProfileController',
                requireLogin: true
            })
            .when('/login', {
                templateUrl: '/assets/app/login/login.tpl.html',
                controller: 'LoginController',
                requireLogin: false
            })
            .when('/signup', {
                templateUrl: '/assets/app/signup/signup.tpl.html',
                controller: 'SignUpController',
                requireLogin: false
            })

        // If the url is unrecognized go to login
        $routeProvider.otherwise({
            redirectTo: '/'
            //TODO: should this be a $window.open(...,'_self')?
        });
    }])

    .run(['$rootScope', 'AuthService','AUTH_EVENTS', '$location','$window', function($rootScope, AuthService, AUTH_EVENTS, $location, $window) {
        console.log("main run");

        // Every time the route in our app changes check auth status
        $rootScope.$on("$routeChangeStart", function(event, next, current) {

            // if you're logged out send to login page.
            if (next.requireLogin && !AuthService.isAuthenticated()) {

                console.log("Next route requires login and not yet authenticated");
                console.log(next);
                $location.path('/login');
            }
        });

        $rootScope.$on(AUTH_EVENTS.loginSuccess, function(event, next, current) {
            console.log('login success!');

            if (document.flash.referer) {
                $window.open(document.flash.referer, '_self');
            } else {
                $location.path('/now-playing');
            }
        });
    }])
