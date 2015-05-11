angular.module('show', ['ngResource'])

     //For info on extending: http://stackoverflow.com/questions/17134401/angular-extending-resource-subobject-with-custom-methods

    .factory('ShowService', function ($resource) {

        var ShowService = $resource('/shows/:id', {id: '@id'},
            {
                'update': { method: 'PUT' },
                'import': { method: 'POST', url: '/shows/import'},
                'count' : { method: 'GET', url: '/shows/count'}
            });

        return ShowService;
    })

    .controller('ShowListController', function ($scope, $rootScope, ShowService) {

        $scope.showCount = ShowService.count();
        $scope.shows = ShowService.query();

        $scope.import = function (rssFeedUrl) {
            if (rssFeedUrl !== undefined) {
                ShowService.import({rssFeedUrl: rssFeedUrl});
            }
        }
    })

    .controller('ShowController', function ($scope, $location, $routeParams, ShowService) {

        $scope.show = ShowService.get($routeParams, function (show) {
            $scope.jsonUrl = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/shows/' + show.id;
        });

        $scope.import = function (rssFeedUrl) {
            if (rssFeedUrl !== undefined) {
                console.log("import: " + rssFeedUrl);
                ShowService.import({rssFeedUrl: rssFeedUrl});
            }
        }
    })