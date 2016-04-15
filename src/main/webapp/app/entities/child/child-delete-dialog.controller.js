(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChildDeleteController',ChildDeleteController);

    ChildDeleteController.$inject = ['$uibModalInstance', 'entity', 'Child'];

    function ChildDeleteController($uibModalInstance, entity, Child) {
        var vm = this;
        vm.child = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Child.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
