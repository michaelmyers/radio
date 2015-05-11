angular.module('genre', [])

    .factory('GenreService', function ($resource) {
        return $resource('/genres/:id', null,
            {
                'update': { method: 'PUT' },
                'count' : { method: 'GET', url: '/genres/count'}
            });
    })

    .controller('GenreListController', function ($scope, $rootScope, GenreService) {
        $scope.genres = GenreService.query();
        $scope.genreCount = GenreService.count();
    })

    .controller('GenreController', function ($scope, $routeParams, GenreService) {
        $scope.genre = GenreService.get($routeParams);
    })