(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyNoteDeleteController',FamilyNoteDeleteController);

    FamilyNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'FamilyNote'];

    function FamilyNoteDeleteController($uibModalInstance, entity, FamilyNote) {
        var vm = this;
        vm.familyNote = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            FamilyNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
