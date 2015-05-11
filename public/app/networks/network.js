angular.module('network', [])

    .factory('NetworkService', function ($resource) {
        //See https://docs.angularjs.org/api/ngResource/service/$resource
        return $resource('/networks/:id', null,
             {
                 'update': { method: 'PUT' },
                 'count' : { method: 'GET', url: '/networks/count'}
             });
    })
    .controller('NetworkListController', function ($scope, $rootScope, NetworkService) {
        $scope.networks = NetworkService.query();
        $scope.networkCount = NetworkService.count();
    })
    .controller('NetworkController', function ($scope, $routeParams, NetworkService) {
        $scope.network = NetworkService.get($routeParams);
    })

