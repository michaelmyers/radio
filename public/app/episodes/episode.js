angular.module('episode', ['ngResource', 'ngRoute', 'ngSanitize'])

    .factory('EpisodeService', function ($resource) {

        var EpisodeService = $resource('/episodes/:id', {id: '@id'},
            {
                'update' : { method: 'PUT' },
                'count'  : { method: 'GET', url: '/episodes/count'},
                'channel': { method: 'GET', url: '/channels/:channel/episodes', isArray: true }
            });

        return EpisodeService;
    })

    .controller('EpisodeListController', function ($scope, $rootScope, $routeParams, $route, $location, EpisodeService) {

        $scope.episodeCount = EpisodeService.count();
        $scope.episodes = EpisodeService.query($routeParams);
        $scope.query = $routeParams;

    })

    .controller('EpisodeController', function ($scope, $sce, EpisodeService, $routeParams, $location) {

        $scope.episode = EpisodeService.get($routeParams, function (episode) {
            $scope.playerUrl = $sce.trustAsResourceUrl(episode.audioUrl);
            $scope.description = $sce.trustAsHtml(episode.description);
            $scope.jsonUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/episodes/' + episode.id;
        });
    })

    .directive('coEpisodeList', ['EpisodeService', '$routeParams', function (EpisodeService, $routeParams) { //co for co-op
        return {
            restrict: 'E',
            scope: {
                network: '=',
                show: '=',
                genre: '=',
                channel: '=',
                query: '='
            },
            templateUrl: '/assets/app/episodes/episode-list-simple.tpl.html',
            controller: ['$scope', '$routeParams', '$route', '$location', function($scope, $routeParams, $route, $location) {

                $scope.maxSize = 6;
                $scope.search = $routeParams.search;

                /**
                 * Access provided page
                 */
                $scope.goToPage = function (page) {

                    if (page === undefined) {
                        page = 0;
                    }

                    $routeParams.page = page;
                    $location.search($routeParams);

                }

                $scope.pageChanged = function() {
                    $scope.goToPage($scope.page - 1);
                }

                /**
                 * Search
                 */
                $scope.searchEpisodes = function (search) {
                    $scope.search = search;

                    $routeParams.page = 0; //knock it back to 0
                    $routeParams.search = search;

                    $location.search($routeParams);
                }

                /**
                 * Get episodes with query
                 */
                $scope.getEpisodes = function (query) {

                    //this will mess with the query
                    if (query.id) {
                        delete query.id;
                    }

                    if (query.channel) {
                        $scope.episodes = EpisodeService.channel(query);
                    } else {
                        EpisodeService.query(query, function (episodes, responseHeaders) {
                            $scope.episodes = episodes;

                            var headers = responseHeaders();

                            $scope.hasNext = (headers["x-has-next"] === "true") ? true : false;
                            $scope.hasPrev = (headers["x-has-prev"] === "true") ? true : false;
                            $scope.total = Number(headers["x-total-count"]);
                            $scope.totalPages = Number(headers["x-total-pages"]);
                            $scope.page = Number(headers["x-current-page"]) + 1;
                            $scope.pageSize = Number(headers["x-page-size"]);

                        });
                    }
                }
            }],
            link: function(scope, element, attrs, controllers) {

                scope.$watch('genre', function (genreId) {

                    if (genreId) {
                        var query = angular.copy($routeParams);
                        query.genre = genreId;
                        scope.getEpisodes(query);
                    }
                });

                scope.$watch('show', function (showId) {

                    if (showId) {
                        var query = angular.copy($routeParams);
                        query.show = showId;
                        scope.getEpisodes(query);
                    }
                });

                scope.$watch('network', function (networkId) {

                    if (networkId) {
                        var query = angular.copy($routeParams);
                        query.network = networkId;
                        scope.getEpisodes(query);
                    }
                });

                scope.$watch('channel', function (channelId) {

                    if (channelId) {
                        var query = angular.copy($routeParams);
                        query.channel = channelId;
                        scope.getEpisodes(query);
                    }
                });

                scope.$watch('query', function (query) {

                    if (query) {
                        scope.getEpisodes(query);
                    }
                });
            }
        }
    }])