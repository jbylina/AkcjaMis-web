'use strict';

describe('Controller Tests', function() {

    describe('ChristmasPackageChange Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockChristmasPackageChange, MockChristmasPackage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockChristmasPackageChange = jasmine.createSpy('MockChristmasPackageChange');
            MockChristmasPackage = jasmine.createSpy('MockChristmasPackage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ChristmasPackageChange': MockChristmasPackageChange,
                'ChristmasPackage': MockChristmasPackage
            };
            createController = function() {
                $injector.get('$controller')("ChristmasPackageChangeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:christmasPackageChangeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
