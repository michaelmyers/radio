module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg : grunt.file.readJSON('package.json'),
    uglify : {
      options : {
        banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
      },
      build : {
        src: 'app/<%= pkg.name %>.js',
        dest: 'build/<%= pkg.name %>.min.js'
      }
    },

    karma: {
      development: {
        configFile: 'karma.conf.js'
      }
    },

    jshint: {
        'beforeconcat': ['app/**/*.js'],
    }

  });

  // Load the plugin that provides the "uglify" task.
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-karma');
  grunt.loadNpmTasks('grunt-contrib-jshint');

  // Default task(s).
  grunt.registerTask('test', ['karma:development']);
  grunt.registerTask('default', ['uglify']);



};
