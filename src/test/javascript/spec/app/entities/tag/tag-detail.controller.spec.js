'use strict';

describe('Controller Tests', function() {

    describe('Tag Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTag, MockFamilyNote;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTag = jasmine.createSpy('MockTag');
            MockFamilyNote = jasmine.createSpy('MockFamilyNote');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tag': MockTag,
                'FamilyNote': MockFamilyNote
            };
            createController = function() {
                $injector.get('$controller')("TagDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:tagUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
