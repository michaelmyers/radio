angular.module('user', ['ngResource', 'auth'])

    .factory('UserService', function ($resource) {
        //See https://docs.angularjs.org/api/ngResource/service/$resource
        return $resource('/users/:id', null,
             {
                 'update': { method:'PUT' }
             });
    })

    .controller('UserController', function ($scope, $rootScope, UserService) {

        $scope.users = UserService.query();

    })

    .controller('ProfileController', function ($scope, AuthService, UserService) {

        $scope.user = AuthService.currentUser();

        $scope.save = function () { //TODO: can we just have this in the template?

            UserService.update({id:$scope.user.id}, $scope.user);
        }
    })

