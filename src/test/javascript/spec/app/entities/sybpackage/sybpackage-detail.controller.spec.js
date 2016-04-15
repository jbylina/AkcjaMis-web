'use strict';

describe('Controller Tests', function() {

    describe('Sybpackage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSybpackage, MockPackageGift;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSybpackage = jasmine.createSpy('MockSybpackage');
            MockPackageGift = jasmine.createSpy('MockPackageGift');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Sybpackage': MockSybpackage,
                'PackageGift': MockPackageGift
            };
            createController = function() {
                $injector.get('$controller')("SybpackageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:sybpackageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
