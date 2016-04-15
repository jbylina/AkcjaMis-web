'use strict';

describe('Controller Tests', function() {

    describe('PackageGift Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPackageGift, MockFamily, MockEvent, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPackageGift = jasmine.createSpy('MockPackageGift');
            MockFamily = jasmine.createSpy('MockFamily');
            MockEvent = jasmine.createSpy('MockEvent');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PackageGift': MockPackageGift,
                'Family': MockFamily,
                'Event': MockEvent,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("PackageGiftDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:packageGiftUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
