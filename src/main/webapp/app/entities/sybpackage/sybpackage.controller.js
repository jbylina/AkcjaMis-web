(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SybpackageController', SybpackageController);

    SybpackageController.$inject = ['$scope', '$state', 'Sybpackage', 'SybpackageSearch'];

    function SybpackageController ($scope, $state, Sybpackage, SybpackageSearch) {
        var vm = this;
        vm.sybpackages = [];
        vm.loadAll = function() {
            Sybpackage.query(function(result) {
                vm.sybpackages = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SybpackageSearch.query({query: vm.searchQuery}, function(result) {
                vm.sybpackages = result;
            });
        };
        vm.loadAll();
        
    }
})();
