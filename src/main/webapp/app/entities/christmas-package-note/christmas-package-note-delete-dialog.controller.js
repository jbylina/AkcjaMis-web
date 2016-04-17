(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageNoteDeleteController',ChristmasPackageNoteDeleteController);

    ChristmasPackageNoteDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChristmasPackageNote'];

    function ChristmasPackageNoteDeleteController($uibModalInstance, entity, ChristmasPackageNote) {
        var vm = this;
        vm.christmasPackageNote = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ChristmasPackageNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
