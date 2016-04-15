'use strict';

describe('Controller Tests', function() {

    describe('PackageNote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPackageNote, MockPackageGift;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPackageNote = jasmine.createSpy('MockPackageNote');
            MockPackageGift = jasmine.createSpy('MockPackageGift');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PackageNote': MockPackageNote,
                'PackageGift': MockPackageGift
            };
            createController = function() {
                $injector.get('$controller')("PackageNoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:packageNoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
