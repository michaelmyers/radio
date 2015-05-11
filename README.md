project transistor-radio
=================================

This file will be packaged with your application, when using `activator dist`.

## Dependencies

`npm`
activator
`brew install typesafe-activator`

## Install Public Dependencies

`cd public`
`npm install`

## Developing

Run Server
`activator run`

Run Server with Specified Conf
`run -Dconfig.resource=application.prod.conf`

Run Server through Docker
`boot2docker init`
`boot2docker start`
`eval "$(boot2docker shellinit)"`
`sbt docker:publishLocal`
`docker run -d -P --name radio radio:0.1-SNAPSHOT`

Build Docker Container
`docker build -tag {{name}} {{path}}`

Test Server, All
`activator test`

Test Server, Package or Single
`activator test-only models.*`

Test Client
`cd public`
`grunt test`

Install Client
`grunt install`

jasmine references: http://jasmine.github.io/2.2/introduction.html

## Project Code Style

### Getters/Setters

Play automatically generates them for you (https://www.playframework.com/documentation/2.3.x/JavaEbean),
only write them if needed.

## The API

### Versioning

Not sure yet.

### Return JSON Objects

#### Errors

```
{
  "error": {
    "userMessage": "Sorry, the requested resource does not exist",
    "internalMessage": "No car found in the database",
    "code": 34,
    "more info": "http://dev.mwaysolutions.com/blog/api/v1/errors/12345"
   }
}

//Example Stack Overflow message
{
  "error_id": 404,
  "error_message": "no method found with this name",
  "error_name": "no_method"
}
```

## Style Guides

* https://github.com/mgechev/angularjs-style-guide

## TODO

* User History, EpisodeList
* Basic Reporting
* Multiple Genres per Show (data model and import)
* Add Tags to Channels
* Reviews (1-5 stars, comments)
* Channel auto-update frequency



## Play References

* https://playcheatsheet.appspot.com/show/ArtemMedeu/armed/play-cheatsheets


## Angular References

* http://www.ng-newsletter.com/posts/directives.html

## Docker Help

* http://viget.com/extend/how-to-use-docker-on-os-x-the-missing-guide
* http://www.scala-sbt.org/sbt-native-packager/formats/docker.html
* http://crosbymichael.com/dockerfile-best-practices.html
* https://docs.docker.com/installation/mac/
* http://stackoverflow.com/questions/23898259/start-play-framework-app-from-dockerfile
* http://docs.aws.amazon.com/elasticbeanstalk/latest/dg/create_deploy_docker_image.html
