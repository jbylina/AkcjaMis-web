/**
 * Created by mlagod on 21.05.16.
 */
(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('EditAddressController', EditAddressController);

    EditAddressController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Family'];

    function EditAddressController($scope, $stateParams, $uibModalInstance, entity, Family) {
        var vm = this;
        vm.child = entity;
        vm.familys = Family.query();
        vm.load = function (id) {
            Family.get({id: id}, function (result) {
                vm.family = result;
            });
        };

    }
})();
