'use strict';

describe('Controller Tests', function() {

    describe('FamilyNote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFamilyNote, MockTag, MockFamily;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFamilyNote = jasmine.createSpy('MockFamilyNote');
            MockTag = jasmine.createSpy('MockTag');
            MockFamily = jasmine.createSpy('MockFamily');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FamilyNote': MockFamilyNote,
                'Tag': MockTag,
                'Family': MockFamily
            };
            createController = function() {
                $injector.get('$controller')("FamilyNoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:familyNoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
