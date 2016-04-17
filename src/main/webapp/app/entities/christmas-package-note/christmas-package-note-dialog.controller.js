(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageNoteDialogController', ChristmasPackageNoteDialogController);

    ChristmasPackageNoteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChristmasPackageNote', 'ChristmasPackage'];

    function ChristmasPackageNoteDialogController ($scope, $stateParams, $uibModalInstance, entity, ChristmasPackageNote, ChristmasPackage) {
        var vm = this;
        vm.christmasPackageNote = entity;
        vm.christmaspackages = ChristmasPackage.query();
        vm.load = function(id) {
            ChristmasPackageNote.get({id : id}, function(result) {
                vm.christmasPackageNote = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:christmasPackageNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.christmasPackageNote.id !== null) {
                ChristmasPackageNote.update(vm.christmasPackageNote, onSaveSuccess, onSaveError);
            } else {
                ChristmasPackageNote.save(vm.christmasPackageNote, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
