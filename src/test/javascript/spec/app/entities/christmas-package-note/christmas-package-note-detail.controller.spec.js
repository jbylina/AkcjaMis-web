'use strict';

describe('Controller Tests', function() {

    describe('ChristmasPackageNote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockChristmasPackageNote, MockChristmasPackage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockChristmasPackageNote = jasmine.createSpy('MockChristmasPackageNote');
            MockChristmasPackage = jasmine.createSpy('MockChristmasPackage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ChristmasPackageNote': MockChristmasPackageNote,
                'ChristmasPackage': MockChristmasPackage
            };
            createController = function() {
                $injector.get('$controller')("ChristmasPackageNoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:christmasPackageNoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
