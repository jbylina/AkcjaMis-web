(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyNoteDialogController', FamilyNoteDialogController);

    FamilyNoteDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FamilyNote', 'Family', 'Tag'];

    function FamilyNoteDialogController ($scope, $stateParams, $uibModalInstance, entity, FamilyNote, Family, Tag) {
        var vm = this;
        vm.familyNote = entity;
        vm.familys = Family.query();
        vm.tags = Tag.query();
        vm.load = function(id) {
            FamilyNote.get({id : id}, function(result) {
                vm.familyNote = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:familyNoteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.familyNote.id !== null) {
                FamilyNote.update(vm.familyNote, onSaveSuccess, onSaveError);
            } else {
                FamilyNote.save(vm.familyNote, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.time = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
