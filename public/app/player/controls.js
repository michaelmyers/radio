angular.module('controls', [])

    .controller('NowPlayingControlsController', function ($scope, $rootScope) {



    })

    .directive('playerControls', function () {
        return {
            templateUrl : '/assets/app/player/controls.tpl.html'
        }

    });
