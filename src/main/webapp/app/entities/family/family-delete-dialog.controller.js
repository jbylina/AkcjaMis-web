(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyDeleteController',FamilyDeleteController);

    FamilyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Family'];

    function FamilyDeleteController($uibModalInstance, entity, Family) {
        var vm = this;
        vm.family = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Family.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
