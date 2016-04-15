(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChildController', ChildController);

    ChildController.$inject = ['$scope', '$state', 'Child', 'ChildSearch'];

    function ChildController ($scope, $state, Child, ChildSearch) {
        var vm = this;
        vm.children = [];
        vm.loadAll = function() {
            Child.query(function(result) {
                vm.children = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ChildSearch.query({query: vm.searchQuery}, function(result) {
                vm.children = result;
            });
        };
        vm.loadAll();
        
    }
})();
