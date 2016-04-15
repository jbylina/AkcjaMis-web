(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ContactDialogController', ContactDialogController);

    ContactDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contact', 'Family'];

    function ContactDialogController ($scope, $stateParams, $uibModalInstance, entity, Contact, Family) {
        var vm = this;
        vm.contact = entity;
        vm.familys = Family.query();
        vm.load = function(id) {
            Contact.get({id : id}, function(result) {
                vm.contact = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:contactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.contact.id !== null) {
                Contact.update(vm.contact, onSaveSuccess, onSaveError);
            } else {
                Contact.save(vm.contact, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
