'use strict';

describe('Controller Tests', function() {

    describe('SubpackageNote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSubpackageNote, MockSubpackage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSubpackageNote = jasmine.createSpy('MockSubpackageNote');
            MockSubpackage = jasmine.createSpy('MockSubpackage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SubpackageNote': MockSubpackageNote,
                'Subpackage': MockSubpackage
            };
            createController = function() {
                $injector.get('$controller')("SubpackageNoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:subpackageNoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
