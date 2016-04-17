'use strict';

describe('Controller Tests', function() {

    describe('Subpackage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSubpackage, MockSubpackageNote, MockChild, MockChristmasPackage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSubpackage = jasmine.createSpy('MockSubpackage');
            MockSubpackageNote = jasmine.createSpy('MockSubpackageNote');
            MockChild = jasmine.createSpy('MockChild');
            MockChristmasPackage = jasmine.createSpy('MockChristmasPackage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Subpackage': MockSubpackage,
                'SubpackageNote': MockSubpackageNote,
                'Child': MockChild,
                'ChristmasPackage': MockChristmasPackage
            };
            createController = function() {
                $injector.get('$controller')("SubpackageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:subpackageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
