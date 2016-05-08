/**
 * Created by mlagod on 08.05.16.
 */
(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyCreateController', FamilyCreateController);

    FamilyCreateController.$inject = ['$scope', '$stateParams', 'entity', 'Family'];

    function FamilyCreateController ($scope, $stateParams, entity, Family) {
        var vm = this;
        vm.family = entity;

        vm.load = function(id) {
            Family.get({id : id}, function(result) {
                vm.family = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:familyUpdate', result);
           // $uibModalInstance.close(result);
            alert('dbg:on save success');
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
           if (vm.family.id !== null) {
              Family.update(vm.family, onSaveSuccess, onSaveError);
           } else {
               Family.save(vm.family, onSaveSuccess, onSaveError);
             //  $state.go('families');
           }
           //var newFamily = $resource('/api/families');
          //  vm.family.save();
        };

        vm.clear = function() {
          //  $uibModalInstance.dismiss('cancel');
            alert('dbg: clear');
        };
    }
})();
