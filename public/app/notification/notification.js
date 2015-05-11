angular.module('notification', [])

    .constant('NOTIFICATION_EVENTS', {
        errorMessage: 'error-message',
        successMessage: 'success-message'
    })

    .controller('NotificationController', function($scope) {

        //$scope.alerts = $;

        /** Examples
        { type: 'danger', msg: 'Oh snap! Change a few things up and try submitting again.' },
        { type: 'success', msg: 'Well done! You successfully read this important alert message.' }
        */
        $scope.addAlert = function() {
            $scope.alerts.push({msg: 'Another alert!'});
        };

        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };
    })