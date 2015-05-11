/*
 * Copyright 2012 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import models.user.User;
import play.Logger;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import controllers.AuthorizationController;

public class DeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public F.Promise<Result> beforeAuthCheck(Http.Context context) {
        Logger.debug("beforeAuthCheck()");
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return F.Promise.pure(null);
    }

    @Override
    public F.Promise<Subject> getSubject(Http.Context context) {

        Logger.debug("DeadboltHandler getSubject() for path: " + context.request().path());

        if (context.request().headers() == null) {
            Logger.debug("headers is null");
        } else {
            Logger.debug("headers was not null " + context.request().headers().toString());
        }

        final String[] authTokenHeaderValues = context.request().headers().get(AuthorizationController.AUTH_TOKEN_HEADER);

        if (authTokenHeaderValues != null) {
            Logger.debug("authTokenHeaderValues " + authTokenHeaderValues.toString());
        }

        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            Logger.debug("Searching for user by auth token " + authTokenHeaderValues[0]);

            return F.Promise.promise(new F.Function0<Subject>() {
                @Override
                public Subject apply() throws Throwable {
                    return User.findByAuthToken(authTokenHeaderValues[0]);
                }
            });

        } else {
            Logger.debug("Auth token was null or empty, trying cookie....");

            if (context.request().cookie(AuthorizationController.AUTH_TOKEN_COOKIE) == null) {
                Logger.debug("Cookie was null");
                return null;
            }

            final String cookieTokenValue = context.request().cookie(AuthorizationController.AUTH_TOKEN_COOKIE).value();

            Logger.debug("cookie: " + cookieTokenValue);

            if (!cookieTokenValue.isEmpty()) {

                return F.Promise.promise(new F.Function0<Subject>() {
                    @Override
                    public Subject apply() throws Throwable {
                        Logger.debug("cookie was not empty, findByAuthToken " + cookieTokenValue);
                        return User.findByAuthToken(cookieTokenValue);
                    }
                });
            }
        }

        return null;

    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        Logger.debug("getDynamicResourceHandler()");
        return new MyDynamicResourceHandler();
    }

    @Override
    public F.Promise<Result> onAuthFailure(Http.Context context, String content) {
        // you can return any result from here - forbidden, etc
        Logger.debug("onAuthFailure()");
        Logger.debug("redirect to login from " + context.request().path() + " with context:");
        Logger.debug(context.toString());

        context.flash().put("referer", context.request().path());

        return F.Promise.promise(new F.Function0<Result>()
        {
            @Override
            public Result apply() throws Throwable {

                return temporaryRedirect("/login");
                //return ok(index.render("transistor"));
                //return redirect(routes.Application.index("/login"));
            }
        });
    }
}
