'use strict';

describe('Controller Tests', function() {

    describe('PackageChangelog Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPackageChangelog, MockPackageGift;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPackageChangelog = jasmine.createSpy('MockPackageChangelog');
            MockPackageGift = jasmine.createSpy('MockPackageGift');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PackageChangelog': MockPackageChangelog,
                'PackageGift': MockPackageGift
            };
            createController = function() {
                $injector.get('$controller')("PackageChangelogDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'akcjamisApp:packageChangelogUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
