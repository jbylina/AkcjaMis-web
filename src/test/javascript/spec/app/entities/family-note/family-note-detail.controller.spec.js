'use strict';

describe('Controller Tests', function() {

    describe('FamilyNote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFamilyNote, MockFamily, MockTag;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFamilyNote = jasmine.createSpy('MockFamilyNote');
            MockFamily = jasmine.createSpy('MockFamily');
            MockTag = jasmine.createSpy('MockTag');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FamilyNote': MockFamilyNote,
                'Family': MockFamily,
                'Tag': MockTag
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
