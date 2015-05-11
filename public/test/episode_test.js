'use strict';

describe('episode', function() {

    var EpisodeService,
        createListController,
        createController,
        $httpBackend,
        $rootScope,
        $routeParamsMock,
        $route,
        $scope,
        $location,
        $compile;

    var testEpisode = {
        id : 0,
        name : "Test Episode"
    }

    var testEpisode1 = {
        id : 1,
        name : "Test Episode 1"
    }

    var testEpisodes = [testEpisode, testEpisode1];

    beforeEach(function () {

        module('episode');
        module('templates');

        inject(function(_EpisodeService_, $controller, _$httpBackend_, _$route_, _$rootScope_, _$location_, _$compile_) {
            EpisodeService = _EpisodeService_;
            $httpBackend = _$httpBackend_;
            $route = _$route_;
            $routeParamsMock = {};
            $location = _$location_;
            $rootScope = _$rootScope_;
            $compile = _$compile_;

            //Controller test pattern from http://nathanleclaire.com/blog/2013/12/13/how-to-unit-test-controllers-in-angularjs-without-setting-your-hair-on-fire/
            $scope = $rootScope.$new();

            createListController = function () {
                return $controller('EpisodeListController', {
                    '$scope': $scope,
                    '$routeParams': $routeParamsMock //we mock routeParams
                });
            };

            createController = function () {
                return $controller('EpisodeController', {
                    '$scope': $scope,
                    '$routeParams': $routeParamsMock //we mock routeParams
                });
            };
        });

        //set up the mocks
        $httpBackend.whenGET(/\/episodes([^/]*)$/).respond(testEpisodes); //regex matches /episodes and /episodes?query=....
        $httpBackend.whenGET('/episodes/0').respond(testEpisode);
        $httpBackend.whenGET('/episodes/count').respond({count: 2});
    })

    afterEach(function () {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    describe('service', function () {

        it('can get an instance of EpisodeService', function () {
            expect(EpisodeService).toBeDefined();
        });

        it('can get episodes with query', function () {

            $httpBackend.expectGET('/episodes');

            EpisodeService.query().$promise.then(function (episodes) {
                expect(episodes).not.toBeNull();
                expect(episodes.length).toEqual(2);
            });

            $httpBackend.flush();
        });

        it('can get a single episode by id', function () {

            $httpBackend.expectGET('/episodes/0');

            EpisodeService.get({id: testEpisode.id}).$promise.then(function (episode) {
                expect(episode).not.toBeNull();
                expect(episode.id).toEqual(testEpisode.id);
                expect(episode.name).toEqual(testEpisode.name);
            });

            $httpBackend.flush();
        });


        it('can get a page of episodes', function () {

            $httpBackend.expectGET('/episodes?page=0&pageSize=4');

            EpisodeService.query({page: '0', pageSize: '4'}).$promise.then(function (episodes) {
                expect(episodes).not.toBeNull();
            });

            $httpBackend.flush();
        });

        it('can get episode count', function () {

            $httpBackend.expectGET('/episodes/count');

            EpisodeService.count().$promise.then(function (count) {
                expect(count).not.toBeNull();
                expect(count.count).toEqual(2);
            });

            $httpBackend.flush();

        });
    });

    describe(' list controller', function () {

        it('can get an instance', function () {

            $httpBackend.expectGET('/episodes/count');
            $httpBackend.expectGET('/episodes');

            var EpisodeListController = createListController();
            $httpBackend.flush();

            expect(EpisodeListController).toBeDefined();
            expect($scope.episodes).toBeDefined();
            expect($scope.episodeCount).toBeDefined();
        });

        /*it('can get current page from query ', function () {

            $httpBackend.expectGET('/episodes/count');
            $httpBackend.expectGET('/episodes?page=34');

            $routeParamsMock = { page : 34 };

            var EpisodeListController = createController();
            $httpBackend.flush();

            //expect($scope.query.page).toBe(34);
        }); */
    });

    describe(' controller', function () {

        it('can get an instance', function () {

            $routeParamsMock.id = '0';

            var EpisodeController = createController();
            $httpBackend.flush();

            expect(EpisodeController).toBeDefined();
            expect($scope.episode).toBeDefined();
        });
    });

    describe('directive', function () {

        it('replaces element with proper content', function () {

            var element = $compile("<co-episode-list query=\"{}\"></co-episode-list>")($rootScope);
            $httpBackend.flush();
            $rootScope.$digest();

            expect(element.html()).toContain(testEpisode.name, testEpisode1.name);

        });

        it('makes correct query for show', function () {

            $httpBackend.expectGET('/episodes?show=1');

            var element = $compile("<co-episode-list show=\"1\"></co-episode-list>")($rootScope);
            $httpBackend.flush();

        });
    });
});