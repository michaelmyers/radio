angular.module('auth', ['LocalStorageModule']) //do I need to add the dependency

    .constant('AUTH_EVENTS', {
      loginSuccess: 'auth-login-success',
      loginFailed: 'auth-login-failed',
      logoutSuccess: 'auth-logout-success',
      sessionTimeout: 'auth-session-timeout',
      notAuthenticated: 'auth-not-authenticated',
      notAuthorized: 'auth-not-authorized'
    })

    .factory('AuthService', function ($http, localStorageService) {

        var authService = {},
            //constants
            USER_KEY = "user",
            AUTH_TOKEN_KEY = "authToken",
            //private variables
            currentUser = null,
            currentAuthToken = null;

        authService.createUser = function (user) {

            return $http.post('/users', user)
                        .then(function (res) {
                            //Success
                            //console.log("create user success");
                            //console.log(res);

                            return res.data.user;
                    });
        }

        authService.login = function (credentials) {

            return $http
                .post('/login', credentials)
                .then(function (res) {
                    //console.log("login success");
                    //console.log(res);

                    //Set the authToken
                    authService.setAuthToken(res.data.authToken);

                    //Set the current user
                    authService.setCurrentUser(res.data.user);

                    return res.data.user;
                });
        };

        authService.logOut = function () {

            return $http
                .post('/logout')
                .then(function (res) {
                    authService.reset();

                    return res;
                })
        }

        authService.currentUser = function () {
            return currentUser;
        }

        authService.setCurrentUser = function (user) {
            //Save to private variable
            currentUser = user;

            //Save the user to local storage
            localStorageService.set(USER_KEY, JSON.stringify(currentUser));
        }

        authService.setAuthToken = function (authToken) {
            //Save to private variable
            currentAuthToken = authToken;

            //Set the auth token in the header
            $http.defaults.headers.common["X-AUTH-TOKEN"] = authToken;
            //console.log("headers:");
            //console.log($http.defaults.headers);

            //Store it locally
            localStorageService.set(AUTH_TOKEN_KEY, authToken);
        }

        authService.isAuthenticated = function () {
            return !!currentAuthToken;
        };

        authService.reset = function () {
            //console.log("resetting AuthService");
            authService.setAuthToken(null);
            authService.setCurrentUser(null);

            //Clear the local storage
            localStorageService.clearAll();

        }

        //Attempt to pull info out of local storage
        /*if (localStorageService.get(USER_KEY)) {
            authService.setCurrentUser(localStorageService.get(USER_KEY));
            console.log("Pulled user out of local storage");
            console.log(currentUser);
        }

        if (localStorageService.get(AUTH_TOKEN_KEY)) {
            authService.setAuthToken(localStorageService.get(AUTH_TOKEN_KEY));
            console.log("Pulled auth token out of local storage");
            console.log(currentAuthToken);
        } */

        return authService;
    })