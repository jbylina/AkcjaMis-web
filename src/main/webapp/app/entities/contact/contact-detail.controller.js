(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ContactDetailController', ContactDetailController);

    ContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Contact', 'Family'];

    function ContactDetailController($scope, $rootScope, $stateParams, entity, Contact, Family) {
        var vm = this;
        vm.contact = entity;
        vm.load = function (id) {
            Contact.get({id: id}, function(result) {
                vm.contact = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:contactUpdate', function(event, result) {
            vm.contact = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
