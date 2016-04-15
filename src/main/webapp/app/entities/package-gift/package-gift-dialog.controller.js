(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageGiftDialogController', PackageGiftDialogController);

    PackageGiftDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PackageGift', 'Family', 'Event', 'Team'];

    function PackageGiftDialogController ($scope, $stateParams, $uibModalInstance, entity, PackageGift, Family, Event, Team) {
        var vm = this;
        vm.packageGift = entity;
        vm.familys = Family.query();
        vm.events = Event.query();
        vm.teams = Team.query();
        vm.load = function(id) {
            PackageGift.get({id : id}, function(result) {
                vm.packageGift = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:packageGiftUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.packageGift.id !== null) {
                PackageGift.update(vm.packageGift, onSaveSuccess, onSaveError);
            } else {
                PackageGift.save(vm.packageGift, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
