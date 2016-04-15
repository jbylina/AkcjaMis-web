(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageGiftDeleteController',PackageGiftDeleteController);

    PackageGiftDeleteController.$inject = ['$uibModalInstance', 'entity', 'PackageGift'];

    function PackageGiftDeleteController($uibModalInstance, entity, PackageGift) {
        var vm = this;
        vm.packageGift = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PackageGift.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
