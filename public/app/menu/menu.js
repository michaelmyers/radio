angular.module('menu', [])

    /**
     * The menu controller handles what is in the menu
     *  current page.
     *
     */
    .controller('MenuController', function($scope, $location) {

        /*$scope.$on("$routeChangeStart", function(event, next, current) {
            console.log('navigation route change start ');
            console.log(next);
        }); */

        /**
         *  Used to detect which menu item is active to apply appropriate class
         *  From: http://stackoverflow.com/questions/16199418/how-do-i-implement-the-bootstrap-navbar-active-class-with-angular-js
         */
        $scope.isActive = function (viewLocation) {
                return viewLocation === $location.path();
        };
    })