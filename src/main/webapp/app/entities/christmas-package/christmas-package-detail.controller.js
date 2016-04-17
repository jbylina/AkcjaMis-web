(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageDetailController', ChristmasPackageDetailController);

    ChristmasPackageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ChristmasPackage', 'Event', 'Team', 'ChristmasPackageNote', 'ChristmasPackageChange', 'Subpackage', 'Family'];

    function ChristmasPackageDetailController($scope, $rootScope, $stateParams, entity, ChristmasPackage, Event, Team, ChristmasPackageNote, ChristmasPackageChange, Subpackage, Family) {
        var vm = this;
        vm.christmasPackage = entity;
        vm.load = function (id) {
            ChristmasPackage.get({id: id}, function(result) {
                vm.christmasPackage = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:christmasPackageUpdate', function(event, result) {
            vm.christmasPackage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
