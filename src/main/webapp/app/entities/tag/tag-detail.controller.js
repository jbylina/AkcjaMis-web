(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tag'];

    function TagDetailController($scope, $rootScope, $stateParams, entity, Tag) {
        var vm = this;
        vm.tag = entity;
        vm.load = function (id) {
            Tag.get({id: id}, function(result) {
                vm.tag = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
