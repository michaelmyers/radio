angular.module('admin', [
    'ngRoute',
    'ngAnimate',
    'ngResource',

    'angular-loading-bar',

    'ui.bootstrap',

    //App modules
    'notification',
    'user',
    'network',
    'genre',
    'episode',
    'show',
    'tag',
    'menu',
    'channel'
])

.config(['$compileProvider', '$routeProvider', '$locationProvider', function($compileProvider, $routeProvider, $locationProvider) {

        $locationProvider.html5Mode(true);
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel):/);

        $routeProvider

            .when('/admin', {
                templateUrl: 'assets/app/landing/landing.tpl.html'
            })

            .when('/admin/users', {
                templateUrl: '/assets/app/users/users-admin.tpl.html',
                controller: 'UserController'
            })

            .when('/admin/networks', {
                templateUrl: '/assets/app/networks/network-list-admin.tpl.html',
                controller: 'NetworkListController'
            })

            .when('/admin/networks/:id', {
                templateUrl: '/assets/app/networks/network-admin.tpl.html',
                controller: 'NetworkController'
            })

            .when('/admin/shows', {
                templateUrl: '/assets/app/shows/show-list-admin.tpl.html',
                controller: 'ShowListController'
            })

            .when('/admin/shows/import', {
                templateUrl: '/assets/app/shows/import/shows-import-admin.tpl.html',
                controller: 'ShowListController'
            })

            .when('/admin/shows/:id', {
                templateUrl: '/assets/app/shows/show-admin.tpl.html',
                controller: 'ShowController'
            })

            .when('/admin/episodes', {
                templateUrl: '/assets/app/episodes/episode-list-admin.tpl.html',
                controller: 'EpisodeListController'
            })

            .when('/admin/episodes/:id', {
                templateUrl: '/assets/app/episodes/episode-admin.tpl.html',
                controller: 'EpisodeController'
            })

            .when('/admin/channels', {
                templateUrl: '/assets/app/channels/channel-list-admin.tpl.html',
                controller: 'ChannelListController'
            })

            .when('/admin/channels/new', {
                templateUrl: '/assets/app/channels/channel-new-admin.tpl.html',
                controller: 'ChannelNewController'
            })

            .when('/admin/channels/:id', {
                templateUrl: '/assets/app/channels/channel-admin.tpl.html',
                controller: 'ChannelController'
            })

            .when('/admin/genres', {
                templateUrl: '/assets/app/genres/genre-list-admin.tpl.html',
                controller: 'GenreListController'
            })

            .when('/admin/genres/:id', {
                templateUrl: '/assets/app/genres/genre-admin.tpl.html',
                controller: 'GenreController'
            })

            .when('/admin/tags', {
                templateUrl: '/assets/app/tags/tags-admin.tpl.html',
                controller: 'TagController'
            })

        // If the url is unrecognized go to login
        $routeProvider.otherwise({
            redirectTo: '/admin'
        });
    }])