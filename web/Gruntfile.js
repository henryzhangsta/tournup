/*global module:false*/
module.exports = function(grunt) {
  // Project configuration.
  grunt.initConfig({
    // Metadata.
    pkg: grunt.file.readJSON('package.json'),
    banner: '/*! <%= pkg.title || pkg.name %> - v<%= pkg.version %> - ' +
      '<%= grunt.template.today("yyyy-mm-dd") %>\n' +
      '<%= pkg.homepage ? "* " + pkg.homepage + "\\n" : "" %>' +
      '* Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author.name %>;' +
      ' Licensed <%= _.pluck(pkg.licenses, "type").join(", ") %> */\n',
    // Task configuration.
    copy: {
      main: {
        expand: true,
        cwd: 'src/',
        src: ['css/**', 'img/**', '*'],
        dest: 'dist/'
      },
      modernizr: {
        src: 'bower_components/modernizr/modernizr.js',
        dest: 'dist/js/modernizr/modernizr.js'
      },
      jquery: {
        expand: true,
        cwd: 'bower_components/jquery/dist/',
        src: ['*'],
        dest: 'dist/js/jquery/dist/'
      },
      foundation: {
        src: 'bower_components/foundation/js/foundation.min.js',
        dest: 'dist/js/foundation/js/foundation.min.js'
      },
      angularjs: {
        expand: true,
        cwd: 'bower_components/angular/',
        src: ['*.js', '*.map'],
        dest: 'dist/js/angular/'
      },
      datatables: {
        expand: true,
        cwd: 'bower_components/angular-datatables/dist',
        src: ['*'],
        dest: 'dist/js/angular-datatables/'
      }
    },
    concat: {
      options: {
        banner: '<%= banner %>',
        stripBanners: true
      },
      dist: {
        files: {
          '.tmp/js/<%= pkg.name %>.js': ['src/js/**/*']
        }
      }
    },
    uglify: {
      options: {
        banner: '<%= banner %>'
      },
      dist: {
        src: ['.tmp/js/<%= pkg.name %>.js'],
        dest: 'dist/js/<%= pkg.name %>.min.js'
      }
    },
    jshint: {
      options: {
        curly: true,
        eqeqeq: true,
        immed: true,
        latedef: true,
        newcap: true,
        noarg: true,
        sub: true,
        undef: true,
        unused: true,
        boss: true,
        eqnull: true,
        browser: true,
        globals: {
          "$": false,
          angular: false
        }
      },
      gruntfile: {
        src: 'Gruntfile.js'
      },
      lib_test: {
        src: ['dist/js/TournUp.min.js']
      }
    },
    watch: {
      gruntfile: {
        files: '<%= jshint.gruntfile.src %>',
        tasks: ['jshint:gruntfile']
      }
    },
    compass: {
      dist: {
        options: {
          config: 'config.rb'
        } 
      } 
    }
  });
  // These plugins provide necessary tasks.
  grunt.loadNpmTasks('grunt-contrib-compass');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-watch');

  // Default task.
  grunt.registerTask('default', ['compass', 'concat', 'uglify', 'copy', 'jshint']);

};
