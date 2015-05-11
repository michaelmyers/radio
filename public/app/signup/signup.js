angular.module('signup', [])

    .controller('SignUpController', function ($scope, $rootScope, AUTH_EVENTS, AuthService) {

        $scope.user = {
            emailAddress: '',
            password: '',
            passwordConfirmation: '',
            zipCode: ''
        };

        $scope.signUp = function (user) {
            console.log("signing up " + user.emailAddress + " " + user.password);

            if (user.password !== user.passwordConfirmation) {
                console.log("Passwords did not match");
            }

            //Clear out the passwordConfirmation property, it causes errors on the server
            delete user.passwordConfirmation;

            AuthService.createUser(user).then(function (user) {
                console.log("sign up success");
                $scope.success("Success, welcome " + user.emailAddress);

                //Successful, now log them in
                var credentials = {'emailAddress':user.emailAddress, 'password':user.password};
                AuthService.login(credentials).then(function (user) {
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                    $scope.setCurrentUser(user);
                }, function () {
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                });

            }, function (res) {
                console.log(res);
                $scope.error('Sign up failed with error ' + res.error);
            });
        };
    })
