(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SybpackageDeleteController',SybpackageDeleteController);

    SybpackageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sybpackage'];

    function SybpackageDeleteController($uibModalInstance, entity, Sybpackage) {
        var vm = this;
        vm.sybpackage = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Sybpackage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
