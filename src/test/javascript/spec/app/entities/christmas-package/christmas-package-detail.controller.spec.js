'use strict';

describe('Controller Tests', function() {

    describe('ChristmasPackage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockChristmasPackage, MockEvent, MockTeam, MockChristmasPackageNote, MockChristmasPackageChange, MockSubpackage, MockFamily;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockChristmasPackage = jasmine.createSpy('MockChristmasPackage');
            MockEvent = jasmine.createSpy('MockEvent');
            MockTeam = jasmine.createSpy('MockTeam');
            MockChristmasPackageNote = jasmine.createSpy('MockChristmasPackageNote');
            MockChristmasPackageChange = jasmine.createSpy('MockChristmasPackageChange');
            MockSubpackage = jasmine.createSpy('MockSubpackage');
            MockFamily = jasmine.createSpy('MockFamily');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ChristmasPackage': MockChristmasPackage,
                'Event': MockEvent,
                'Team': MockTeam,
                'ChristmasPackageNote': MockChristmasPackageNote,
                'ChristmasPackageChange': MockChristmasPackageChange,
                'Subpackage': MockSubpackage,
                'Family': MockFamily
            };
            createController = function() {
                $injector.get('$controller')("ChristmasPackageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:christmasPackageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
