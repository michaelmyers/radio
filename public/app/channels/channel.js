angular.module('channel', ['ngResource', 'ngRoute', 'genre'])

        .factory('ChannelService', function ($resource) {

            var ChannelService = $resource('/channels/:id', {id: '@id'},
                {
                    'update': { method: 'PUT' },
                    'count' : { method: 'GET', url: '/channels/count'}
                });

            return ChannelService;
        })

        .controller('ChannelListController', function ($scope, $routeParams, $route, $location, ChannelService) {

            $scope.channelCount = ChannelService.count();
            $scope.channels = ChannelService.query();
        })

        .controller('ChannelNewController', function ($scope, $routeParams, ChannelService, GenreService) {

            $scope.genres = GenreService.query();

            $scope.createChannel = function () {
                console.log("Creating channel");
                console.log($scope.channel);
                ChannelService.save($scope.channel);
            };
        })

        .controller('ChannelController', function ($scope, $routeParams, $location, ChannelService) {

            $scope.channel = ChannelService.get($routeParams, function (channel) {
                $scope.jsonUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/channels/' + channel.id;
            });
        })