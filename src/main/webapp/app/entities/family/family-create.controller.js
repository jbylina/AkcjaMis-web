/**
 * Created by mlagod on 08.05.16.
 */
(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyCreateController', FamilyCreateController);

    FamilyCreateController.$inject = ['$scope', '$stateParams', 'entity', 'Family', '$state'];

    function FamilyCreateController ($scope, $stateParams, entity, Family, $state) {
        var vm = this;
        vm.family = entity;

        vm.load = function(id) {
            Family.get({id : id}, function(result) {
                vm.family = result;
            });
        };

        var onSaveSuccess = function (result) {
            vm.isSaving = false;
            $state.go('family-detail',{id : result.id});
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
           }

        };

        vm.clear = function() {
            alert('dbg: clear');
        };
    }
})();
