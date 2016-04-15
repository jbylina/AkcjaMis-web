(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageNoteDialogController', SubpackageNoteDialogController);

    SubpackageNoteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SubpackageNote', 'Sybpackage'];

    function SubpackageNoteDialogController ($scope, $stateParams, $uibModalInstance, entity, SubpackageNote, Sybpackage) {
        var vm = this;
        vm.subpackageNote = entity;
        vm.sybpackages = Sybpackage.query();
        vm.load = function(id) {
            SubpackageNote.get({id : id}, function(result) {
                vm.subpackageNote = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:subpackageNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.subpackageNote.id !== null) {
                SubpackageNote.update(vm.subpackageNote, onSaveSuccess, onSaveError);
            } else {
                SubpackageNote.save(vm.subpackageNote, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
