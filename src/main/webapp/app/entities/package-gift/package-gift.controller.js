(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageGiftController', PackageGiftController);

    PackageGiftController.$inject = ['$scope', '$state', 'PackageGift', 'PackageGiftSearch'];

    function PackageGiftController ($scope, $state, PackageGift, PackageGiftSearch) {
        var vm = this;
        vm.packageGifts = [];
        vm.loadAll = function() {
            PackageGift.query(function(result) {
                vm.packageGifts = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PackageGiftSearch.query({query: vm.searchQuery}, function(result) {
                vm.packageGifts = result;
            });
        };
        vm.loadAll();
        
    }
})();
