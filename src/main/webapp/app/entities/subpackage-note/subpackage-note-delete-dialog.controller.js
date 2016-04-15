(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageNoteDeleteController',SubpackageNoteDeleteController);

    SubpackageNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'SubpackageNote'];

    function SubpackageNoteDeleteController($uibModalInstance, entity, SubpackageNote) {
        var vm = this;
        vm.subpackageNote = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            SubpackageNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
