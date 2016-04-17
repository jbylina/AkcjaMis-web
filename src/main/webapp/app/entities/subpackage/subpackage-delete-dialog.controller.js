(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageDeleteController',SubpackageDeleteController);

    SubpackageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Subpackage'];

    function SubpackageDeleteController($uibModalInstance, entity, Subpackage) {
        var vm = this;
        vm.subpackage = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Subpackage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
