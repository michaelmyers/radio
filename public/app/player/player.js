angular.module('player', ['ngSanitize'])

//html freq analyzer http://codepen.io/swys/pen/nxzpD
// angular media player https://github.com/mrgamer/angular-media-player/blob/master/dist/angular-media-player.js

        .controller('PlayerController', ['$sce', function ($sce) {

        }])

        .value('player.throttleSettings', {
            enabled: true,
            time: 1000
        })
        .constant('player.defaults', {
          // general properties
          currentTrack: 0,
          ended: undefined,
          network: undefined,
          playing: false,
          seeking: false,
          tracks: 0,
          volume: 1,

          // formatted properties
          formatDuration: '00:00',
          formatTime: '00:00',
          loadPercent: 0
        })
        .directive('player', ['$rootScope', 'player.throttleSettings', 'player.defaults', function ($rootScope, throttle, defaults) {
            return {
                restrict: 'E',
                scope: {

                },
                templateUrl: '/assets/app/player'


            }

        }]);
















/*
            var playerMethods = {

                load: function (mediaElement, autoplay) {
                    // method overloading
                    if (typeof mediaElement === 'boolean') {
                        autoplay = mediaElement;
                        mediaElement = null;
                    } else if (typeof mediaElement === 'object') {
                        this.$clearSourceList();
                        this.$addSourceList(mediaElement);
                    }

                    this.$domEl.load();
                    this.ended = undefined;

                    if (autoplay) {
                        this.$element.one('canplay', this.play.bind(this));
                    }
                },


                reset: function (autoplay) {
                    angular.extend(this, playerDefaults);
                    this.$clearSourceList();
                    this.load(this.$playlist, autoplay);
                },

                play: function (index, selectivePlay) {
                    // method overloading
                    if (typeof index === 'boolean') {
                      selectivePlay = index;
                      index = undefined;
                    }
                    if (selectivePlay) {
                        this.$selective = true;
                    } else if (selectivePlay == false) {
                        this.$selective = false;
                    }

                        if (this.$playlist.length > index) {
                          this.currentTrack = index + 1;
                          return this.load(this.$playlist[index], true);
                        }
                        // readyState = HAVE_NOTHING (0) means there's nothing into the <audio>/<video> tag
                        if (!this.currentTrack && this.$domEl.readyState) { this.currentTrack++; }

                        // In case the stream is completed, reboot it with a load()
                        if (this.ended) {
                          this.load(true);
                        } else {
                          this.$domEl.play();
                        }
                      },
                      playPause: function (index, selectivePlay) {
                        // method overloading
                        if (typeof index === 'boolean') {
                          selectivePlay = index;
                          index = undefined;
                        }
                        if (selectivePlay) {
                          this.$selective = true;
                        } else if (selectivePlay == false) {
                          this.$selective = false;
                        }

                        if (typeof index === 'number' && index + 1 !== this.currentTrack) {
                          this.play(index);
                        } else if (this.playing) {
                          this.pause();
                        } else {
                          this.play();
                        }
                      },
                      pause: function () {
                        this.$domEl.pause();
                      },
                      stop: function () {
                        this.reset();
                      },
                      toggleMute: function () {
                        this.muted = this.$domEl.muted = !this.$domEl.muted;
                      },


                //Private Methods

                $addSourceList: function (sourceList) {
                    var self = this;

                    if (angular.isArray(sourceList)) {
                        angular.forEach(sourceList, function (singleElement, index) {

                            var sourceElem = document.createElement('SOURCE');

                            ['src', 'type', 'media'].forEach(function (key) { // use only a subset of the properties
                                if (singleElement[key] !== undefined) { // firefox is picky if you set undefined attributes
                                    sourceElem.setAttribute(key, singleElement[key]);
                                }
                            });

                            self.$element.append(sourceElem);
                      });
                    } else if (angular.isObject(sourceList)) {

                        var sourceElem = document.createElement('SOURCE');

                        ['src', 'type', 'media'].forEach(function (key) {
                            if (sourceList[key] !== undefined) {
                                sourceElem.setAttribute(key, sourceList[key]);
                            }
                        });

                        self.$element.append(sourceElem);
                    }
                  },

                $clearSourceList: function () {
                    this.$element.contents().remove();
                },

                $formatTime: function (seconds) {
                    if (seconds === Infinity) {
                      return '∞'; // If the data is streaming
                    }
                    var hours = parseInt(seconds / 3600, 10) % 24,
                        minutes = parseInt(seconds / 60, 10) % 60,
                        secs = parseInt(seconds % 60, 10),
                        result,
                        fragment = (minutes < 10 ? '0' + minutes : minutes) + ':' + (secs  < 10 ? '0' + secs : secs);
                    if (hours > 0) {
                      result = (hours < 10 ? '0' + hours : hours) + ':' + fragment;
                    } else {
                      result = fragment;
                    }
                    return result;
                }
            }
        }]); */


    /*.controller('PlayerController', ['$scope', '$location', '$sce', function($scope, $location, $sce) {

        console.log("PlayerController");

        $scope.next = function() {

            console.log('NEXT');

        }

        $scope.src = [{scr: $sce.trustAsResourceUrl("http://static.videogular.com/assets/audios/videogular.mp3"), type: "audio/mpeg"}];

        $scope.theme = { url: "http://www.videogular.com/styles/themes/default/latest/videogular.css" };

    }]); */


    /*["$sce", function ($sce) {
                this.config = {
                    sources: [
                  {src: $sce.trustAsResourceUrl("http://static.videogular.com/assets/audios/videogular.mp3"), type: "audio/mpeg"},
                  {src: $sce.trustAsResourceUrl("http://static.videogular.com/assets/audios/videogular.ogg"), type: "audio/ogg"}
              ],
                    theme: {
              url: "http://www.videogular.com/styles/themes/default/latest/videogular.css"
                    }
                };
            }] */