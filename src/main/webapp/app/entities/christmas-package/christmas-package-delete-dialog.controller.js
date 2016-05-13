(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageDeleteController',ChristmasPackageDeleteController);

    ChristmasPackageDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChristmasPackage'];

    function ChristmasPackageDeleteController($uibModalInstance, entity, ChristmasPackage) {
        var vm = this;
        vm.christmasPackage = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ChristmasPackage.delete({event_id : 1, id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
