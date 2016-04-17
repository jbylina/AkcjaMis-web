(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageChangeDetailController', ChristmasPackageChangeDetailController);

    ChristmasPackageChangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ChristmasPackageChange', 'ChristmasPackage'];

    function ChristmasPackageChangeDetailController($scope, $rootScope, $stateParams, entity, ChristmasPackageChange, ChristmasPackage) {
        var vm = this;
        vm.christmasPackageChange = entity;
        vm.load = function (id) {
            ChristmasPackageChange.get({id: id}, function(result) {
                vm.christmasPackageChange = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:christmasPackageChangeUpdate', function(event, result) {
            vm.christmasPackageChange = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
