'use strict';

describe('Controller Tests', function() {

    describe('Family Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFamily, MockContact, MockChild, MockFamilyNote, MockChristmasPackage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFamily = jasmine.createSpy('MockFamily');
            MockContact = jasmine.createSpy('MockContact');
            MockChild = jasmine.createSpy('MockChild');
            MockFamilyNote = jasmine.createSpy('MockFamilyNote');
            MockChristmasPackage = jasmine.createSpy('MockChristmasPackage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Family': MockFamily,
                'Contact': MockContact,
                'Child': MockChild,
                'FamilyNote': MockFamilyNote,
                'ChristmasPackage': MockChristmasPackage
            };
            createController = function() {
                $injector.get('$controller')("FamilyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:familyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
