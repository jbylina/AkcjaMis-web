(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageChangeDeleteController',ChristmasPackageChangeDeleteController);

    ChristmasPackageChangeDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChristmasPackageChange'];

    function ChristmasPackageChangeDeleteController($uibModalInstance, entity, ChristmasPackageChange) {
        var vm = this;
        vm.christmasPackageChange = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ChristmasPackageChange.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
