module.exports = function(config){
  config.set({

    basePath : './',

    files : [
      //these need to be written out
      'components/angular/angular.js',
      'components/angular-animate/angular-animate.js',
      'components/angular-local-storage/dist/angular-local-storage.js',
      'components/angular-mocks/angular-mocks.js',
      'components/angular-resource/angular-resource.js',
      'components/angular-route/angular-route.js',
      'components/angular-sanitize/angular-sanitize.js',

      'app/**/*.js', //source
      'test/**/*.js', //tests
      'app/**/*.html' //templates
    ],

    preprocessors : {
        'app/**/*.js' : ['coverage'],
        'app/**/*.html': ['ng-html2js']
    },

    ngHtml2JsPreprocessor: {
        // prepend this to the
        prependPrefix: '/assets/',

        // setting this option will create only a single module that contains templates
        // from all the files, so you can load them all with module('foo')
        moduleName: 'templates'
    },

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'], //add 'Firefox' to test on both platforms

    plugins : [
        'karma-chrome-launcher',
        'karma-firefox-launcher',
        'karma-jasmine',
        'karma-junit-reporter',
        'karma-htmlfile-reporter',
        'karma-coverage',
        'karma-ng-html2js-preprocessor'
    ],

    reporters: ['progress', 'html', 'junit', 'coverage'],

    coverageReporter: {
      type : 'html',
      dir : 'test/result/coverage'
    },

    htmlReporter: {
        outputFile: 'test/result/unit-tests.html'
    },

    junitReporter : {
      outputFile: 'test/result/junit.xml',
      suite: 'unit'
    }
  });
};