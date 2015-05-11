angular.module('tag', ['ngResource'])

    .factory('TagService', function ($resource) {
        return $resource('/tags/:id', null,
            {
                'count': { method: 'GET', url: '/tags/count'}
            });
    })

    .controller('TagController', function ($scope, $rootScope, TagService) {
        $scope.tagCount = TagService.count();
        $scope.tags = TagService.query();


        $scope.instantTags = function (term) {

            TagService.query({term: term}, function (tags) {
                console.log(tags);
            });
        }
    })

    /*.directive('coTagSelect', ['TagService', function (TagService) {
        return {
            restrict: 'E',
            scope: {

            },
            template:
        }

    }]) */