(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageNoteDialogController', PackageNoteDialogController);

    PackageNoteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PackageNote', 'PackageGift'];

    function PackageNoteDialogController ($scope, $stateParams, $uibModalInstance, entity, PackageNote, PackageGift) {
        var vm = this;
        vm.packageNote = entity;
        vm.packagegifts = PackageGift.query();
        vm.load = function(id) {
            PackageNote.get({id : id}, function(result) {
                vm.packageNote = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:packageNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.packageNote.id !== null) {
                PackageNote.update(vm.packageNote, onSaveSuccess, onSaveError);
            } else {
                PackageNote.save(vm.packageNote, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
