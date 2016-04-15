(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageNoteDeleteController',PackageNoteDeleteController);

    PackageNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'PackageNote'];

    function PackageNoteDeleteController($uibModalInstance, entity, PackageNote) {
        var vm = this;
        vm.packageNote = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PackageNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
