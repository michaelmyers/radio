angular.module('app', ['auth'])
    .controller('ApplicationController', function($scope, $location, AuthService, localStorageService) {

        $scope.currentUser = function () {

            return AuthService.currentUser();
        }

        $scope.isAuthorized = AuthService.isAuthorized;

        $scope.alerts = [];

        $scope.error = function (message) {
            $scope.alerts.push({type: 'danger', msg: message})
        }

        $scope.success = function (message) {
            $scope.alerts.push({type: 'success', msg: message});
        }

        $scope.logOut = function () {
            console.log('logging out current user ' + $scope.currentUser.emailAddress);
            AuthService.logOut();
            $location.path('/');
        }

        $scope.$on("$routeChangeStart", function(event, next, current) {
            //On route change, clear out the alerts
            $scope.alerts = [];
        });
    })

    .directive("contenteditable", function() {
      return {
        restrict: "A",
        require: "ngModel",
        link: function(scope, element, attrs, ngModel) {

          function read() {
            ngModel.$setViewValue(element.html());
          }

          ngModel.$render = function() {
            element.html(ngModel.$viewValue || "");
          };

          element.bind("blur keyup change", function() {
            scope.$apply(read);
          });
        }
      };
    });