(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageChangelogDeleteController',PackageChangelogDeleteController);

    PackageChangelogDeleteController.$inject = ['$uibModalInstance', 'entity', 'PackageChangelog'];

    function PackageChangelogDeleteController($uibModalInstance, entity, PackageChangelog) {
        var vm = this;
        vm.packageChangelog = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PackageChangelog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
