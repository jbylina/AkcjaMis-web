(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SybpackageDetailController', SybpackageDetailController);

    SybpackageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Sybpackage', 'PackageGift'];

    function SybpackageDetailController($scope, $rootScope, $stateParams, entity, Sybpackage, PackageGift) {
        var vm = this;
        vm.sybpackage = entity;
        vm.load = function (id) {
            Sybpackage.get({id: id}, function(result) {
                vm.sybpackage = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:sybpackageUpdate', function(event, result) {
            vm.sybpackage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
