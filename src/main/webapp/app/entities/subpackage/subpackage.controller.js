(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageController', SubpackageController);

    SubpackageController.$inject = ['$scope', '$state', 'Subpackage', 'SubpackageSearch'];

    function SubpackageController ($scope, $state, Subpackage, SubpackageSearch) {
        var vm = this;
        vm.subpackages = [];
        vm.loadAll = function() {
            Subpackage.query(function(result) {
                vm.subpackages = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SubpackageSearch.query({query: vm.searchQuery}, function(result) {
                vm.subpackages = result;
            });
        };
        vm.loadAll();
        
    }
})();
