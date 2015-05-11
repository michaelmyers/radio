'use strict';

describe('show', function() {

    var ShowService,
        httpBackend;

    var testShow = {
        id : 0,
        name : "Test Show"
    }

    var testShow1 = {
        id : 1,
        name : "Test Show 1"
    }

    var testShows = [testShow, testShow1];

    beforeEach(function () {
        module('show');

        inject(function(_ShowService_, $httpBackend) {
            ShowService = _ShowService_;
            httpBackend = $httpBackend;
        });


        httpBackend.whenGET('/shows').respond(testShows);
        httpBackend.whenGET('/shows/0').respond(testShow);

    });

    afterEach(function () {
        httpBackend.verifyNoOutstandingExpectation();
        httpBackend.verifyNoOutstandingRequest();
    });

    it('can get an instance of ShowService', function () {
        expect(ShowService).toBeDefined();
    });

    it('can get shows with query', function () {

        httpBackend.expectGET('/shows');

        ShowService.query().$promise.then(function (shows) {
            expect(shows).not.toBeNull();
            expect(shows.length).toEqual(2);
        });

        httpBackend.flush();
    });

    it('can get a single show by id', function () {

        httpBackend.expectGET('/shows/0');

        ShowService.get({id: '0'}).$promise.then(function (show) {
            expect(show).not.toBeNull();
            expect(show.id).toEqual(testShow.id);
            expect(show.name).toEqual(testShow.name);
        });

        httpBackend.flush();
    });

    it('can import a show', function () {

        //TODO: You should be able to change "test" to something more generic: jasmine.anything()
        //See: https://github.com/angular/angular.js/issues/3457
        httpBackend.expectPOST('/shows/import', {rssFeedUrl : "test"})
            .respond(200)

        ShowService.import({rssFeedUrl: "test"});

        httpBackend.flush();

    });
})