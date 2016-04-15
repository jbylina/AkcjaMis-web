(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyController', FamilyController);

    FamilyController.$inject = ['$scope', '$state', 'Family', 'FamilySearch'];

    function FamilyController ($scope, $state, Family, FamilySearch) {
        var vm = this;
        vm.families = [];
        vm.loadAll = function() {
            Family.query(function(result) {
                vm.families = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FamilySearch.query({query: vm.searchQuery}, function(result) {
                vm.families = result;
            });
        };
        vm.loadAll();
        
    }
})();
